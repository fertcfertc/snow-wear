package com.fertcfertc.snowwear.weather;

import com.fertcfertc.snowwear.SnowWear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SnowTracker {
    private final Map<Integer, Float> coverage = new HashMap<>();
    private final Map<Integer, Motion> motion = new HashMap<>();
    private ClientLevel level;
    private int emitted;
    private long emissionTick = Long.MIN_VALUE;

    private record Motion(double x, double y, double z, boolean grounded, long lastShed) {}

    public void tick(Minecraft client) {
        if (level != client.level) {
            coverage.clear();
            motion.clear();
            level = client.level;
        }
        var settings = SnowWear.CONFIG.get();
        if (!settings.enabled() || level == null || client.player == null) {
            coverage.clear();
            motion.clear();
            return;
        }
        if (client.isPaused()) return;
        if (emissionTick != level.getGameTime()) {
            emissionTick = level.getGameTime();
            emitted = 0;
        }
        Set<Integer> present = new HashSet<>();
        if (settings.showSelf()) update(client.player, present);
        if (settings.showOthers()) {
            double range = settings.renderDistance() * settings.renderDistance();
            for (AbstractClientPlayer player : level.players()) {
                if (present.size() >= 64) break;
                if (player != client.player && player.distanceToSqr(client.player) <= range) update(player, present);
            }
        }
        coverage.keySet().retainAll(present);
        motion.keySet().retainAll(present);
    }

    private void update(AbstractClientPlayer player, Set<Integer> present) {
        if (player.isSpectator() || player.isInvisible()) return;
        var settings = SnowWear.CONFIG.get();
        BlockPos head = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
        boolean snowing = level.getRainLevel(1) > 0.01f && level.canSeeSky(head)
                && level.getPrecipitationAt(head) == Biome.Precipitation.SNOW;
        float value = SnowCover.advance(coverage.getOrDefault(player.getId(), 0f), snowing,
                player.isInWater() || player.isOnFire(), settings.accumulationSeconds(), settings.meltSeconds(),
                settings.maxCoverage() / 100f);
        Motion previous = motion.get(player.getId());
        long now = level.getGameTime();
        long lastShed = previous == null ? now - 16 : previous.lastShed();
        if (previous != null && settings.movementShedding() && !player.isInWater() && !player.isOnFire()) {
            double dx = player.getX() - previous.x(), dz = player.getZ() - previous.z();
            boolean landed = player.onGround() && !previous.grounded();
            boolean jumping = !player.onGround() && previous.grounded() && player.getY() > previous.y() + .05;
            if (emitted <= 20 && SnowMotion.sheds(value, dx * dx + dz * dz,
                    player.isSprinting(), landed, jumping, now - lastShed)) {
                shed(player, dx, dz);
                value = Math.max(0, value - .08f);
                lastShed = now;
            }
        }
        motion.put(player.getId(), new Motion(player.getX(), player.getY(), player.getZ(), player.onGround(), lastShed));
        coverage.put(player.getId(), value);
        present.add(player.getId());
    }

    private void shed(AbstractClientPlayer player, double dx, double dz) {
        var random = level.getRandom();
        for (int index = 0; index < 4; index++) {
            double x = player.getX() + (random.nextDouble() - .5) * .7;
            double y = player.getY() + player.getBbHeight() * (.7 + random.nextDouble() * .3);
            double z = player.getZ() + (random.nextDouble() - .5) * .7;
            level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z,
                    dx * .15 + (random.nextDouble() - .5) * .04, -.015, dz * .15 + (random.nextDouble() - .5) * .04);
            emitted++;
        }
    }

    public float coverage(int entityId) {
        return coverage.getOrDefault(entityId, 0f);
    }
}
