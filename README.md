# Goeki Client

Original, client-side Minecraft Forge utility mod source for **Java Edition 1.7.10 and 1.8.9** (Java 8).
No combat automation, reach/knockback manipulation, autoclicking, or aim assistance.

## Included source
- Independent shared Java core with HUD modules and CPS tracker
- Forge-specific event registration, HUD rendering, keystrokes, potion effects, armor durability, FPS, memory/frame time, compass, ping (1.8.9)
- Dark Goeki ClickGUI: Right Shift to open, left-click to toggle, right-click to expand, category tabs, mouse wheel scrolling, draggable title bar, HEX accent editor (click accent value, type `#RRGGBB`, Enter)
- Profile persistence: default profile saved on GUI close; API supports named profiles, module states, keybind numbers, HUD location and accent color
- Fullbright gamma toggle with restoration on disable (allowed only where server rules permit)

## Important limitations
**Source release, not a verified finished binary.** No Forge build was executed in this environment; old ForgeGradle dependencies may be inaccessible. The 1.7.10 ping display currently shows `--`. The GUI does not yet expose profile switching, keybind editing, or draggable HUD editing. Zoom, Toggle Sprint, screenshot browser, crosshair editor, chat styling, advanced performance switches, and graphical settings widgets are not wired yet. Module toggles for those unimplemented features are placeholders, not functioning features. No FPS gain is claimed.

## Building
Use **JDK 8** and the historical Forge MDK/Gradle wrapper matching each version. Open `forge-1.8.9` or `forge-1.7.10` separately; both include the common source folder. See their `build.gradle` for Forge coordinates. Run `gradle setupDecompWorkspace` then `gradle build` using the compatible historical Gradle distribution.

## Controls
- Right Shift: ClickGUI
- Left-click module: enable/disable
- Right-click module: expand description
- Mouse wheel: scroll modules
- Drag top 12 px: reposition GUI
- Click HEX value: type six-digit color and press Enter

All Goeki source is newly authored for this project. Forge and Minecraft are third-party dependencies under their respective terms.

## Changes in v0.5
- 1.7.10: removed `throws IOException` from GUI overrides (did not compile on 1.7.10).
- GUI: no more overdraw on header/footer when scrolling; scroll limit computed from content; empty MOVEMENT tab removed.
- CPS is now counted from real mouse events instead of a 20 Hz poll, so fast clicking is no longer under-reported.
- Accent HEX input accepted without leading `#`.
- Explicit imports (wildcard `module.*` clashed with `java.lang.Module` on JDK 9+).
- 1.7.10 `mcmod.info` version is now expanded; 1.8.9 ping is null-safe and shows `--` in singleplayer.
- Version is defined once (`Goeki.VERSION`).
See `BUILDING.md` for what is and is not verified.
