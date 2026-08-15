# MKCorp

A Fabric utility client for Minecraft. Built for **single-player, your own
servers, and anarchy servers** where utility clients are allowed. Using it on
servers that prohibit cheats violates their rules and gets accounts banned —
that's on you.

## Modules

| Module     | Key | Category | What it does |
|------------|-----|----------|--------------|
| KillAura   | K   | Combat   | Auto-attacks the nearest living entity in range (4.2 blocks), ~5–10 attacks/sec with jittered timing |
| Flight     | G   | Movement | Velocity flight — WASD to move, jump up, sneak down, hover |
| Speed      | H   | Movement | Capped ground-speed boost with bhop auto-jump |
| Sprint     | N   | Movement | Never stops sprinting while moving forward |
| NoFall     | J   | Player   | On-ground packet spoof past 2 blocks of falling |
| FullBright | B   | Render   | Gamma boost past the slider max, restores on disable |
| ESP        | U   | Render   | Through-wall entity boxes (players red, hostiles orange, items green) |
| Xray       | X   | Render   | Hides all non-whitelisted blocks (ores, chests, spawners stay visible). Pair with FullBright |

All keybinds are real keybindings — rebind them in **Options → Controls**.
Enabled modules show in the top-left HUD array list, colored by category.

## Build

Requirements: **JDK 21** and Gradle 8.8+ (or generate the wrapper with
`gradle wrapper` after first build, or copy `gradlew` from the Fabric example mod).

```bash
gradle build
```

The built jar lands in `build/libs/mkcorp-0.1.0.jar`.

## Install

1. Install **Fabric Loader 0.16+** for Minecraft **1.21.1**.
2. Put **Fabric API** (1.21.1 build) and the MKCorp jar in `.minecraft/mods/`.
3. Launch. Press keys, see HUD, done.

## Version note: why 1.21.1 and not 26.1

The newest Minecraft release (26.1) shipped a brand-new toolchain: the game is
no longer obfuscated, Yarn mappings are discontinued, and Fabric moved to a new
Loom plugin (1.15+), Gradle 9.4, Java 25, and renamed Fabric API entry points
(`HudRenderCallback` was removed in favor of `HudElementRegistry`, etc.).

MKCorp pins to 1.21.1 because that toolchain is mature and everything here
compiles against stable APIs. When you want to port to 26.1:

- `build.gradle`: switch to the new `net.fabricmc.fabric-loom` plugin, use
  `implementation`/`compileOnly` instead of `modImplementation`, and `jar`
  instead of `remapJar`.
- Mappings: none needed — Mojang's official names ship in the game now. Class
  and method names in the code will need renaming to the official ones.
- `HudOverlay`: move from `HudRenderCallback` to `HudElementRegistry`.
- Set `sourceCompatibility`/`release` to Java 25, and update
  `gradle.properties` to 26.1 versions from https://fabricmc.net/develop.

## Detection reality (read this)

**No client is undetectable.** Anyone who promises that is selling something.
Here's the honest breakdown per module:

- **Client-side only (FullBright, ESP, Xray, Sprint):** effectively invisible
  to anticheats — they change nothing the server can observe. Exception: some
  servers run anti-xray *obfuscation* (fake ore data), which defeats Xray from
  the server side no matter what your client does.
- **KillAura:** reach above 3.0 and instant aim snaps are detectable by
  combat checks. Drop `RANGE` to 3.0 for a much lower profile.
- **Flight / Speed:** the most-flagged modules on any server running Grim,
  Vulcan, or similar movement simulation. Treat them as anarchy-server tools.
- **NoFall:** moderate risk — good anticheats simulate your fall and notice
  the discrepancy.

Flying under the radar is about *behavior*, not magic: lower settings,
jittered timing (already built into KillAura), and not staring at players
through walls.

## Roadmap

- Click GUI + per-module settings (currently constants at the top of each module file)
- Config save/load
- More modules: Tracers, AutoTotem, Velocity, Jesus
- 26.1 port once the new toolchain settles

## License

MIT. Do whatever, blame nobody.
