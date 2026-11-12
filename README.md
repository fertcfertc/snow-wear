# Snow Wear

Snow settles on clothing and armor, with raised patches on the head and shoulders that follow the player's pose. Sprinting, jumping and landing shake off small flake bursts. Snow melts under cover; water and fire melt it faster.

[![Sponsored by CTF Launcher — try it out](docs/ctf-launcher.svg)](https://ctf.mom)

## Installation

Minecraft 26.3, Java 25. Install the JAR for your loader with Architectury API 22.0.5 and Cloth Config 26.3.159.

- Fabric: Fabric Loader 0.20.1 and Fabric API 0.163.0+26.3. Install Mod Menu to open settings.
- NeoForge: NeoForge 26.3.0.102. Open settings from the mod list.

Install on the client. Servers need no installation.

## Settings

Choose accumulation time, melting time, maximum coverage, player distance, and whether snow appears on yourself or other players. Your own snow is visible in third person and inventory views. Up to 64 visible players are tracked within the configured distance.

Settings are saved in `config/snowwear.json`. The settings screen includes a link to CTF Launcher.

Raised caps fit vanilla helmets and chest armor. The clothing texture follows the skin model; armor can cover this texture. Custom oversized armor and player models may cover the caps. Armor caps and movement shedding have separate toggles. Shedding releases four flakes per burst, with a 16-tick cooldown per player and a shared limit of 24 flakes per tick.

## Build

Use Java 25 and run `./gradlew :fabric:build :neoforge:build` (`gradlew.bat` on Windows). Loader-specific runtime JARs are in `fabric/build/libs/` and `neoforge/build/libs/`.

Author: **fertc**. [MIT license](LICENSE).
