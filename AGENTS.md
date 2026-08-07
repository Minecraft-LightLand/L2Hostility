# AGENTS.md

L2Hostility: a Minecraft Forge mod (MC 1.20.1 / Forge 47.4.0 / Java 17) that gives mobs levels and scaling traits. Part of the L2 mod family by xkmc; depends on l2library, l2complements, curios, patchouli, and many optional mods.

## Build & run
- Java 17, Gradle 8.8 wrapper. `gradle.properties` sets `org.gradle.daemon=false`, so every invocation is a cold build (slow).
- `./gradlew build` — compiles and reobfs into `build/libs/l2hostility-<ll_version>.jar`
- `./gradlew runClient` / `./gradlew runServer` — dev runs in the committed `run/` dir (mods, config, kubejs scripts, saves)
- `./gradlew runData` — datagen writes to `src/generated/resources/` (see `data` run config in build.gradle:69). Generated JSON is checked into git; commit the diff after regenerating.
- No unit tests. Verification = `build` + `runData`; manual checks via runServer/runClient.

## Architecture
- `init/L2Hostility.java` is the entrypoint; `init()` registers all content (blocks, items, traits, entities, effects, enchantments, capabilities).
- `init/registrate/` — all registration via Registrate (`LHRegistrate`, subclass of l2library's `L2Registrate`); traits registered in `LHTraits.java`.
- `init/data/` — Forge configs (`LHConfig`), lang, recipes, tags, advancements, damage types.
- `content/` — gameplay logic: `traits/` (mob traits), `capability/` (chunk/mob/player caps), `config/` (datapack config classes), `item/`, `effect/`, `entity/`, `menu/`.
- `compat/` — optional-mod integration (curios, kubejs, jei, jade, gateways) plus `compat/data/` with per-mod default config generators.
- `mixin/` — every new mixin must also be added to `src/main/resources/l2hostility.mixins.json` (client-only mixins go under the `"client"` array).
- Datapack config: four registries declared in `L2Hostility.java` (`DIFFICULTY`, `TRAIT`, `WEAPON`, `ENTITY`). Default JSONs are generated into `src/generated/resources/data/<namespace>/l2hostility_config/{difficulty,trait,weapon,entity}/` and overridden by datapacks at runtime.

## Gotchas
- `libs/` is a flatDir repository for locally-built deps (l2library, l2complements, mixinextras, l2damagetracker, etc.). To test against a changed dependency, drop its jar here — versions in `libs/` win over maven.
- `rootMod=false`, `lljij=true` — jarJar is on; `build` produces both slim and reobf jars.
- Publish tasks (`publishCurseForge`, modrinth) read `secrets.properties` (gitignored; holds CF/Modrinth tokens). Missing it breaks only publishing, not `build`. Never commit it.
- `ll_version` in `gradle.properties` is the mod version; the first block of `changelog.txt` becomes the release notes.
- KubeJS integration: plugin declared in `src/main/resources/kubejs.plugins.txt`; registration builders in `compat/kubejs/`; working examples in `examples/kubejs/`.
- In `LHConfigGen.java`, per-mod compat defaults are gated on `ModList.get().isLoaded(...)` — `runData` output changes depending on which deps resolve at generation time.

## Code style

- Annotate methods and fields as `@Nullable` when they are nullable.
- Try not to use anonymous classes. Use records instead.

## Guidelines

- Minecraft source code is at `./build/fg_cache/net/minecraftforge/forge/1.20.1-47.4.0_mapped_official_1.20.1/forge-1.20.1-47.4.0_mapped_official_1.20.1-sources.jar`
- Read `doc/*.md` if relevant.
- **Before analyzing or executing any task**, first write a short description of the request to `doc/todo.md` (append to the list if it is non-empty). Remove that entry once the task is fully completed. This is a hard requirement: do not skip it, do not do it later, and do not remove the entry before the work is actually done.
- Do not try to access files you don't have permission over unless otherwise agreed.
- Do not try to analyze texture unless explicitly stated.
