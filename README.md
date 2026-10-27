# Sip Moment — NeoForge 1.21.1

This branch targets Minecraft **1.21.1**, NeoForge **21.1.264**, and **Java 21**.
The build uses [ModDevGradle's 1.21.1 MDK](https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle).

## Development

Install a JDK 21 and set `JAVA_HOME` to its installation directory.

- `./gradlew build` — compile and package the mod in `build/libs/`.
- `./gradlew runClient` — launch the development client.
- `./gradlew runServer` — launch the dedicated development server.
- `./gradlew runGameTestServer` — run the seven server game tests and exit.
- `./gradlew runData` — generate resources into `src/generated/resources/`.

On Windows, use `.\gradlew.bat` instead of `./gradlew`.

Models with multi-axis element rotations use the `sip_moment:rotated_elements` loader. Their original geometry is stored in `sip_elements`; inventory and held-item perspectives use NeoForge separate transforms.

## Upstream sync

Merged [upstream commit e0035a2](https://github.com/Renyigesai/sipmoment-template-26.1.2/commit/e0035a2ddb72da209b3dc706b595ea0405d3b763) (2026-10-27). This includes David Martinez's Sandevistan effect, nearby entity slowing, colored afterimages, the screen overlay and activation sound, Shipwreck Vintage, and updated drink tooltips.

The backport uses 1.21.1 food effects, entity data serializers, packet interpolation, and immediate rendering. Slow leases expire on world ticks so skipped entity ticks and players cannot leave slowing stuck. Afterimage attributes are registered on the mod event bus, and client renderers are registered only on the client. Shipwreck Vintage includes pile-aware block loot and separate inventory/held models.

The seven game tests cover slowed ticking and recovery, passengers, aura range and afterimage spawning, afterimage immunity and lifetime, drink effects and servings, Shipwreck Vintage drops, and player resistance and recovery. Tests live in `src/gameTest/` and are excluded from the release JAR. CI runs them after the build.

## Backport verification

Verified on 1.21.1: Gradle build, client startup and resource loading, dedicated-server startup, mixed-display item/component persistence across a server restart, mixed-display drops, orange-juice loot, and spear entity spawning. The upstream sync also passes all seven server game tests and client world loading. Resource checks cover all 115 JSON files and local model/texture/sound references.

The upstream sync was built and game-tested with Oracle JDK 21.0.11 without extra JVM flags.

The following interactive gameplay checks remain to be performed in survival, creative, and adventure modes where applicable:

- Drink every beverage, checking sip counts, effects, tooltips, and the drunken camera effect.
- Place, stack, combine, retrieve, and break drinks; reload a world containing mixed displays.
- Pour each bottled wine into its matching glass and check the bottle's remaining servings.
- Open the bar table, scroll the wine list, and buy drinks on a dedicated server.
- Sit on and dismount a bar chair; play the music disc in a jukebox.
- Throw enchanted and unenchanted tridents with Sincerely for You, including Loyalty and Riptide.
- Check inventory icons, held models, pouring animation, particles, and advancements.
- Visually check the Sandevistan overlay, afterimage colors/fade and slowed movement on a dedicated server; check activation audio and subtitles.


## License
### 1. 代码(Code)
本模组所有位于`src/main/java` 目录下的源代码，基于 [**BSD 3-Clause License**](https://github.com/Renyigesai/bakery/blob/master/LICENSE-CODE.txt) 开源。
### 2. 资源文件(Assets)
其它所有非代码资源文件（位于`src/main/resources`目录下的文件），均采用[**CC BY-NC-SA 4.0**](https://github.com/Renyigesai/bakery/blob/master/LICENSE-ASSETS.txt) 进行许可。
