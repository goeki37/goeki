# Building Goeki Client (v0.5, source release)

**No tested JAR is included.** The shared code compiles and passes a small logic test;
the Forge parts (`forge-1.8.9`, `forge-1.7.10`) have **not** been compiled against Minecraft/Forge yet.
Build **1.8.9 first** – its toolchain is far easier than 1.7.10.

## Requirements
- **JDK 8 only** (set `JAVA_HOME` to it). Old Gradle/ForgeGradle do not run on Java 9+.
- Internet access (Forge/Minecraft download their libraries during setup).
- The Gradle wrapper is **not** included. Take it from the official Forge MDK for the matching version
  (https://files.minecraftforge.net) and copy `gradlew`, `gradlew.bat` and the `gradle/` folder into
  `forge-1.8.9/` (MDK 1.8.9 ships Gradle 2.7) or `forge-1.7.10/` (MDK 1.7.10 ships Gradle 1.12).

## 1.8.9
```
cd forge-1.8.9
gradlew setupDecompWorkspace
gradlew build
```
Result: `forge-1.8.9/build/libs/GoekiClient-1.8.9-0.5.0.jar` (use the one *without* `-sources`).
Test with Forge 1.8.9 (11.15.1.2318) in a clean instance. `gradlew runClient` starts a dev client.

## 1.7.10
Same commands in `forge-1.7.10`. ForgeGradle 1.2 is old and its download URLs have partly moved;
if setup fails with dependency/SSL errors, send the log. A community-maintained 1.7.10 toolchain
(RetroFuturaGradle) may be needed instead – the Java sources stay the same.

## If the build fails
Send me the **full Gradle output** (or the first ~50 lines of errors). Typical causes:
wrong JDK, missing wrapper, or a Minecraft/Forge method name that differs from what the source assumes.

## Known unfinished work
Zoom, Toggle Sprint, screenshot manager, HUD drag editor, crosshair editor, profile switching /
keybind editing in the GUI, and 1.7.10 ping (shows `--`).
