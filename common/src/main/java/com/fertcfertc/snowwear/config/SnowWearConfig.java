package com.fertcfertc.snowwear.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class SnowWearConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("snowwear");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;
    private SnowWearSettings settings = SnowWearSettings.defaults();

    public SnowWearConfig(Path path) {
        this.path = path;
    }

    public SnowWearSettings get() {
        return settings;
    }

    public void load() {
        if (!Files.exists(path)) {
            save(settings);
            return;
        }
        try {
            JsonObject values = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
            JsonObject merged = GSON.toJsonTree(SnowWearSettings.defaults()).getAsJsonObject();
            values.entrySet().forEach(entry -> merged.add(entry.getKey(), entry.getValue()));
            settings = GSON.fromJson(merged, SnowWearSettings.class).normalized();
        } catch (IOException | RuntimeException exception) {
            LOGGER.warn("Could not read {}. Using defaults; the file has been preserved.", path, exception);
        }
    }

    public boolean save(SnowWearSettings next) {
        SnowWearSettings normalized = next.normalized();
        Path temporary = path.resolveSibling(path.getFileName() + ".tmp");
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(temporary, GSON.toJson(normalized) + System.lineSeparator());
            try {
                Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
            }
            settings = normalized;
            return true;
        } catch (IOException exception) {
            LOGGER.error("Could not save {}. Settings have not changed.", path, exception);
            return false;
        }
    }
}
