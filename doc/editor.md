# L2Hostility Datapack Editor (`dev.xkmc.l2hostility.editor`)

Client-side datapack editor for the four `l2hostility_config` datapack types (difficulty,
trait, weapon, entity) plus an extra tab that edits the `l2hostility` namespace **tags** used
for the trait black/white lists and the global entity black/white list.

It writes a new datapack into the active singleplayer world's `datapacks/` folder and offers a
datapack reload, exactly like Modular Golems' editor. The generic UI/base layer is **copied from
`ModularGolems/src/main/java/dev/xkmc/modulargolems/editor/base/`** (which is documented there as
mod-independent and reusable by other l2-config mods); Modular Golems' files are **not edited**.
Our copy lives in `dev.xkmc.l2hostility.editor.base` with package renames only, plus the
improvements listed below. Built for Forge 1.20.1 / official mappings, Java 17.

---

## 1. Package layout

```
dev.xkmc.l2hostility.editor
├── base/      # copied from ModularGolems editor.base (renamed), NO l2hostility imports
├── util/      # l2hostility-specific shared helpers
├── home/      # the shared home screen (one class, 5 tabs)
├── config/    # per-config-kind editing screens
└── tag/       # tag editing (trait black/white list + entity black/white list)
```

### base (copied + new, mod-independent)

Copied verbatim from `ModularGolems.editor.base` with `package dev.xkmc.modulargolems.editor.base`
→ `package dev.xkmc.l2hostility.editor.base` (imports adjusted; no other edits unless noted):

| Class | Purpose |
|---|---|
| `EditorFile` | config file machinery: `save(type,id,config,packFolder)`, `copy`, `parseId`, `validNamespace`, `worldDatapacks`/`currentWorldDir`, writes `pack.mcmeta`. **Improvement:** `pack.mcmeta` description string becomes "L2Hostility Editor". |
| `EditorUtil` | generic pickers/labels: `listItems`, `listTags`, `itemName`, `tagName`, `itemIngredient`, `tagIngredient`, `ingredientIcon`, `ingredientText`, `save`, `copy`, `byId`. |
| `EditorSaveState` | static `savedFlag` (a save is pending a datapack reload) + `canEdit()` (singleplayer + cheats + creative). |
| `EditorText` | generic lang enum, keys under `editor.*` (neutral). |
| `EditorSession` | `{boolean dirty}` shared across a file's edit tree. |
| `EditorList` | `ObjectSelectionList` wrapper; `Entry` with icon + `data` payload + group header; `setOnSelect(Runnable)`. |
| `EditorTab` / `TabButton` | record `(label, onSelect)` + tab-styled button used by the home tab bar. |
| `EditorLayout` | static `centerRow(List<Button>, centerX, y, gap)`. |
| `EditorToast` | `SystemToast` wrapper. |
| `EditorScreen` | base `Screen` subclass (handles resize via `rebuildWidgets`). |
| `PickListScreen<T>` | searchable picker (EditBox + list), Cancel, Esc/parent navigation. |
| `PromptScreen` | modal labeled EditBox with validator + Cancel/Confirm. |
| `DoubleMapScreen<T>` | value-map editor (Add/Edit/Remove), optional per-entry percent display. |
| `Obj2IntMapScreen<M>` | int-map editor with per-object `maxLevel` (`Function<M,Integer>`), `Lv x/y`, validates `1..max`. |
| `ItemListScreen<T>` | set editor over candidates with icon+label (generic over `T`: reused for entity types, traits, enchantments, items, item stacks). |
| `IngredientScreen` | item/tag/clear picker for an `Ingredient`. **Not used by L2Hostility configs** (no ingredient fields) but kept in the copy for completeness. |
| `ExitConfirmScreen` | Save / Discard / Cancel dialog for leaving a dirty file. |
| `ReloadConfirmScreen` | "Reload now / Later" dialog shown on editor exit when a save is pending. |
| `EditorHomeScreen` | abstract shared home: grouped file list (namespace headers), top tab bar, New/Edit/Reload/Back bottom row. **Improvement:** add `protected boolean canCreate() { return true; }`; the New button's `active` is set from it (tag and trait tabs return `false`). |
| `LinkButton`, `package-info.java` | underline-on-hover button; `@MethodsReturnNonnullByDefault` + `@ParametersAreNonnullByDefault`. |

New in `base` (all mod-independent):

| Class | Purpose |
|---|---|
| `FormScreen` | **generic multi-field form.** `FormSpec<T> = (List<FormField> fields, Function<List<String>, T> build)`; `FormField` is either text `(label, initial, validator)` or boolean `(label, initial)` (rendered as a toggle). Confirmed values passed to `build` in field order; bools arrive as `"true"/"false"`. Used for every record/scalar edit in L2Hostility configs (see §4). |
| `ListEditScreen<T>` | **generic ordered-list editor** for `List<T>` (Add/Edit/Remove/Back). `Handler<T> = { label, icon, void onAdd(Consumer<T> onDone, Screen parent), void onEdit(T cur, Consumer<T> onDone, Screen parent) }`. Add calls `onAdd` (the handler opens whatever screen chain builds a default `T`), Edit calls `onEdit`; `onDone` replaces the item in the list + sets `session.dirty`. |
| `ValueMapScreen<K,V>` | **map editor with form-editable values**: Add (pick key from candidates via `PickListScreen` or type a key) / Edit (open `FormScreen` built from `FormSpec<V>`) / Remove. `Handler<K>` for the key label/icon + `Function<V,Component> summary` for the row text. |
| `TagFile` | generic tag-file I/O: `save(ResourceLocation tagId, JsonElement valuesArray, String packFolder)` writes `data/<ns>/tags/entity_types/<path>.json` with `{"replace": true, "values": [...]}` and (re)uses `EditorFile.writePackMeta`; `read(PackResources, tagId)` helper for raw value extraction. |

**Improvements over Modular Golems' base** (summary):
1. `EditorHomeScreen.canCreate()` hook (New disabled per tab).
2. `FormScreen`, `ListEditScreen`, `ValueMapScreen`, `TagFile` added (L2Hostility's data model is mostly records/lists, which the golem base can't edit).
3. `pack.mcmeta` description fixed to this mod.
4. *(optional polish, same as the "handler refactor" Modular Golems lists as pending)*: consolidate `PickListScreen/ItemListScreen/DoubleMapScreen/Obj2IntMapScreen` constructor functional args behind one `EditorHandler<T>` with default `icon()→null`, `percent()→false`, `maxLevel()→unbounded`. New screens (`ListEditScreen`, `ValueMapScreen`) should adopt this shape from the start; retrofitting the four copied screens is optional and can be deferred.

`base` must never import `dev.xkmc.l2hostility.*` (non-editor), matching the golem rule.

### util (l2hostility-specific shared)

| Class | Purpose |
|---|---|
| `HostilityEditorUtil` | registry/data access: `listEntityTypes` (from `ForgeRegistries.ENTITY_TYPES`, sorted, icons via `SpawnEggItem.byId`), `listTraits` (`LHTraits.TRAITS.get().getValues()`, label `MobTrait.getDesc()`, icon `trait.asItem()`), `listEnchantments`, `listBiomes` / `listStructures` (from `Minecraft.getInstance().level.registryAccess()`), `validateFileId`, `newDifficulty/newWeapon/newEntity`, `save` (wraps `EditorFile.save` with `PACK_FOLDER = "l2hostility_editor"`), tag helpers `listManagedTags()`, `traitBlackTag(trait)/traitWhiteTag(trait)`. |
| `HostilityEditorLang` | l2hostility-specific lang, keys under `l2hostility.editor.*` (tab/file titles, section labels, field labels, entity/trait/enchantment pick titles, tag labels, errors). |
| `HostilityEditorForms` | `FormSpec<T>` builders for every editable record/scalar: `DifficultyConfig`, `TraitConfig` fields, `ItemConfig`, `EnchConfig`, `TraitBase` (+ `TraitCondition`), `ItemPool`, `ItemEntry`, `MasterConfig`, `Minion`, `EntityConfig.Config` scalars. Includes the value↔string conversions (e.g. `DoubleMapScreen.format`-style trimming for doubles). |
| `HostilityEditorHandlers` | shared `EditorHandler` instances: `ENTITY_TYPE`, `TRAIT`, `ENCHANTMENT`, `BIOME`, `STRUCTURE`, `ITEM` (label/icon). |
| `EditorReloadHooks` | client Forge-bus subscriber: clears `EditorSaveState.savedFlag` on `TagsUpdatedEvent` with cause `CLIENT_PACKET_RECEIVED` (fires on manual `/reload` and world rejoin). Registered via `@Mod.EventBusSubscriber(modid = L2Hostility.MODID, value = Dist.CLIENT)`. |

### home

| Class | Purpose |
|---|---|
| `HostilityHomeScreen` | **one** `EditorHomeScreen` subclass parameterized by `TabKind { DIFFICULTY, TRAIT, WEAPON, ENTITY, TAGS }` (instead of five near-identical home subclasses). `tabs()` always returns all five tabs; `activeTab()`/`listFiles()`/`fileCount()`/`emptyMessage()`/`newFileDefault()`/`openNew()`/`openEdit()`/`validateId()`/`canCreate()` dispatch on the kind. Holds the entry `parent` (the screen that opened the editor); all tab switches construct a new `HostilityHomeScreen(kind, parent)`. |

### config

Per-kind screens (details in §4):

| Kind | Classes |
|---|---|
| difficulty | `DifficultyFileScreen`, `DimLevelMapScreen`, `BiomeMapScreen`, `DefaultTraitsListScreen` |
| trait | `TraitFileScreen` |
| weapon | `WeaponFileScreen`, `ItemConfigListScreen`, `SpecialWeaponListScreen`, `EnchConfigListScreen` |
| entity | `EntityFileScreen`, `ConfigListScreen` (shared list of `EntityConfig.Config`, reused by difficulty default-traits), `EntityConfigEntryScreen`, `TraitBaseListScreen`, `ItemPoolListScreen`, `ItemEntryListScreen`, `MasterConfigScreen` |

### tag

| Class | Purpose |
|---|---|
| `TagValue` | record `(String id, boolean required, boolean isTag)`; `parse(JsonElement)` / `toJson()` / `toComponent()` (plain id; `#tag` colored; `required:false` shown gray/italic " (optional)"). |
| `HostilityTagUtil` | `load(tagId)` → effective raw value list by merging the tag JSONs across the integrated server's selected packs in order, applying `replace` semantics and preserving raw forms (see §5); `listTraitTags()` = for each registered trait path `p`: `l2hostility:p_blacklist` / `l2hostility:p_whitelist`; `save(tagId, List<TagValue>)` via `TagFile`. |
| `TagEditScreen` | tag values editor: Add entity (`PickListScreen` over entity types), Add tag (`PromptScreen` for `#ns:path`), Edit (toggle `required`), Remove, Save, dirty tracking. |

---

## 2. Entry point & gating

- The **"Edit Datapacks"** button is added in `DifficultyScreen.init()` (the l2tabs
  "Difficulty Information" screen), mirroring Modular Golems' button in its tracker screen.
  Placement: below the info lines at `topPos + 8 + <info height>`, left-aligned like the info text.
- It is only added when `EditorSaveState.canEdit()` is true: singleplayer server present AND
  `getWorldData().getAllowCommands()` AND the player is creative (`player.isCreative()`).
- `HostilityHomeScreen(TabKind.DIFFICULTY, this)` is the landing screen; the other four kinds are
  reached via the top tab bar (rendered generically by `EditorHomeScreen` from `tabs()` +
  `activeTab()`).
- *(alternative, not chosen)*: register a dedicated "Editor" l2tabs tab like `TAB_DIFFICULTY`;
  rejected because tabs can't be gated on `canEdit()`.

---

## 3. Navigation / screen-return contract

Identical to Modular Golems: every editor screen stores its `parent` `Screen` and overrides
`onClose()` to `setScreen(parent)`; Back and Esc return to the previous screen. `Minecraft.setScreen`
re-inits the parent, which refreshes button enabled-state and lists. `HostilityHomeScreen`'s `parent`
is the entry screen, so Back from any tab leaves the editor.

## 4. File editing lifecycle

- Home list groups files by namespace; group header = mod display name
  (`ModList.getModContainerById(ns)…getDisplayName()`, namespace fallback). Difficulty/weapon/entity
  files come from `L2Hostility.DIFFICULTY/WEAPON/ENTITY.getAll()` (all loaded datapack files,
  including per-mod compat ones like `twilightforest:ur_ghast`).
- **Edit** = deep copy (`EditorUtil.copy` → `JsonCodec` round-trip), **New** = fresh config with a
  default id. Dirty tracking via a shared `EditorSession`; **Save** disabled unless dirty; Edit/Remove
  disabled until a row is selected; bottom buttons on one centered row (`EditorLayout.centerRow`).
- **Save** prompts for a file id (prefilled), writes via
  `HostilityEditorUtil.save` → `EditorFile.save(type, id, config, PACK_FOLDER)`, stays on the file
  screen (dirty cleared), sets `EditorSaveState.savedFlag = true`. Exit with unsaved changes →
  `ExitConfirmScreen`.

Per-kind specifics:

### Difficulty
`DifficultyFileScreen` lists four fixed rows (like `MaterialEntryScreen`):
- **Dimensions** → `DimLevelMapScreen`: entries of `levelMap` (key = typed `ResourceLocation`,
  `PromptScreen`; value = `DifficultyConfig`). Add opens the key prompt then a `FormScreen`;
  Edit opens the `FormScreen` for the 7 fields
  (`min, base, variation, scale, apply_chance, trait_chance, suppression`). `ValueMapScreen`-style.
- **Biomes** → `BiomeMapScreen`: same over `biomeMap`; keys picked via `PickListScreen` over
  `listBiomes()` (icon `BIOME` egg/`null`, label biome name).
- **Level default traits** → `DefaultTraitsListScreen`: entries of `levelDefaultTraits`
  (dimension key → `ArrayList<EntityConfig.Config>`); value edited with the shared `ConfigListScreen`
  (§ entity). Note: a `Config` with an **empty `entities` list is the "all entities" fallback**
  (see `WorldDifficultyConfig.get`); the list screen lets Add create one without picking an entity.
- **Structure default traits** → same over `structureDefaultTraits`, keys picked via
  `PickListScreen` over `listStructures()`.

### Trait
`TraitHomeScreen`'s file list = **all registered traits** (`LHTraits.TRAITS.get().getKeys()`),
**not** `L2Hostility.TRAIT.getAll()` — a trait without a datapack file still exists and falls back to
`TraitConfig.DEFAULT`, so editing must be possible for every trait. `canCreate()` returns `false`
(traits are code-defined). `TraitFileScreen` loads the current entry
(`TRAIT.getEntry(id)` or `TraitConfig.DEFAULT` as baseline), deep-copies it, and edits the four
scalar fields `min_level, cost, max_rank, weight` in a `FormScreen`. It also shows a hint row with
the trait's derived blacklist/whitelist tag ids (jumping to the tag tab).

### Weapon
`WeaponFileScreen` lists six fixed rows:
- **Melee / Ranged / Armors** → `ItemConfigListScreen` over `melee_weapons` / `ranged_weapons` /
  `armors` (`ArrayList<ItemConfig>`). Add = pick one or more items (multi-select via
  `PickListScreen`/`ItemListScreen`) then `FormScreen` for `level, weight`; builds
  `new ItemConfig(defaultStacks, level, weight)`. Edit re-opens both sub-editors; an existing
  non-null `ItemCondition` is **carried over unchanged** (see §7), the empty/`AIR` entry from the
  default configs keeps working as-is.
- **Special weapons** → `SpecialWeaponListScreen` over `special_weapons`
  (`LinkedHashMap<LinkedHashSet<EntityType<?>>, ArrayList<ItemConfig>>`), shown as a list of
  `(entity-set, item-config-list)` entries. Add = pick multiple entity types (set editor) then an
  `ItemConfigListScreen`.
- **Weapon / Armor enchantments** → `EnchConfigListScreen` over `weapon_enchantments` /
  `armor_enchantments`. Add = pick enchantments (multi-select, icons `Items.ENCHANTED_BOOK`) then
  `FormScreen` for `level, chance`; builds `new EnchConfig(list, level, chance)`.

### Entity
`EntityFileScreen` lists `EntityConfig.list` via the shared `ConfigListScreen`
(`ArrayList<EntityConfig.Config>`). Add = pick an entity type (`PickListScreen`) → new `Config`
with that entity + default difficulty, then open the entry editor. Each row opens
`EntityConfigEntryScreen` with rows:
- **Entities** → `ItemListScreen` over a `LinkedHashSet<EntityType<?>>` write-through view of
  `config.entities` (ArrayList-backed; sync back before save).
- **Difficulty** → `FormScreen` (7 fields) rebuilt into `DifficultyConfig` and stored back.
- **Traits** → `TraitBaseListScreen` over `config.traits()` (`ArrayList<TraitBase>`). Add = pick
  trait then `FormScreen` for `free, min` + `cap` (bool) + optional `TraitCondition`
  (`lv, chance, advancement id`; leaving `lv` blank stores a null condition → 3-arg
  `TraitBase`-style serialization).
- **Trait blacklist** → `ItemListScreen` over `config.blacklist()` (`LinkedHashSet<MobTrait>`).
- **Items** → `ItemPoolListScreen` over `config.items`. Add = `FormScreen`
  (`level, chance, slot`) then `ItemEntryListScreen` over `entries`
  (`ItemEntry(weight, ItemStack)`; Add = pick item then prompt `weight`).
- **Values** → `FormScreen` editing the public scalar fields directly: `minSpawnLevel, maxLevel,
  maxTraitCount` (ints), `healthScale, attackScale` (doubles), `presetTraitsOnly` (bool).
- **Master** → `MasterConfigScreen` (nullable): if `asMaster == null` a row offers "Add";
  otherwise edit `MasterConfig(maxTotalCount, spawnInterval)` via `FormScreen` plus
  `MinionListScreen` over `minions` (`Minion` is edited with a `FormScreen` for its 11 scalar
  fields + optional nested `Config`).
- `specialConditions` (code-defined subclasses) are **not editable**; they survive the
  `JsonCodec` round-trip copy and are preserved (see §7).

---

## 5. Tag editing (the extra tab)

### Managed tags
`listManagedTags()` returns (all in the `l2hostility` namespace, group header "L2Hostility"):
- **Entity black/white list**: `l2hostility:blacklist`, `l2hostility:whitelist` — consumed by
  `MobTraitCap.HOLDER` (`WHITELIST` grants the cap to any entity; otherwise `Enemy` not in
  `BLACKLIST`).
- **Trait black/white lists**: for every registered trait path `p`,
  `l2hostility:p_blacklist` and `l2hostility:p_whitelist` — consumed by `TraitConfig.allows(type)`
  (`MobTrait.allow`, e.g. `l2hostility:split_whitelist`, `l2hostility:invisible_blacklist`).
  Plus the fallback `l2hostility:default_blacklist` / `l2hostility:default_whitelist` from
  `TraitConfig.DEFAULT`, which apply to any trait without its own datapack file
  (`MobTrait.getConfig()` → `L2Hostility.TRAIT.getEntry(...)`, falls back to `DEFAULT`).
  Derived from the trait registry (`LHTraits.TRAITS.get().getKeys()`), which is exactly the set the
  mod's `LHTagGen.ENTITY_TAG_BUILDER` emits (every `TraitConfig` constructor registers both tags).

`TAG_EDIT` rows show the tag id + `(n)` where `n` = number of effective raw values. `canCreate()`
returns `false` (New disabled with a toast); editing an existing tag is the only flow.

### Raw-value model
Tags are edited as **raw `TagValue` entries**, not resolved entities, so tag references and
`required:false` optional entries survive a round trip:
- `"minecraft:zombie"` → `TagValue("minecraft:zombie", required=true, isTag=false)`
- `"#l2hostility:semiboss"` → `TagValue("l2hostility:semiboss", required=true, isTag=true)`
- `{"id":"mod:boss","required":false}` → `TagValue("mod:boss", required=false, isTag=false)`

### Read / merge
`HostilityTagUtil.load(tagId)`:
1. `IntegratedServer server = Minecraft.getInstance().getSingleplayerServer()` (guaranteed by
   `canEdit()`; same thread as the client, safe for file I/O like `EditorFile.save`).
2. Iterate `server.getPackRepository().getSelectedPacks()` **in selection order**.
3. For each `PackResources`, read `data/<ns>/tags/entity_types/<path>.json`
   (`PackType.SERVER_DATA`, `PackResources.getResource`), parse with the shared GSON.
4. Merge in order: a `"replace": true` clears the accumulated list; then `values` are appended,
   each preserved in raw form. Result = the ordered effective list MC would load (nested tag
   references stay opaque `#…` strings — no recursive expansion, no data loss).

### Save
`HostilityTagUtil.save(tagId, List<TagValue>)` writes, via `TagFile`:
```
<world>/datapacks/l2hostility_editor/data/<ns>/tags/entity_types/<path>.json
{
  "replace": true,
  "values": ["minecraft:zombie", {"id": "mod:boss", "required": false}, "#l2hostility:semiboss"]
}
```
`replace: true` is deliberate: the editor pack is a world datapack that sits above the mod's
built-in datapack, so the edited tag fully overrides the generated one (required for removals).
Tradeoff (surfaced in a `SAVE_NOTE`-style hint): once a tag is edited, later additions to that tag
from mod datapacks are overridden until the editor tag is re-edited/removed. Tag ids are fixed (the
row you opened), so there is no file-id prompt; dirty tracking, Save enablement and exit-confirm
work as usual, and save sets `EditorSaveState.savedFlag`.

`TagEditScreen` actions: **Add entity** (`PickListScreen` over `listEntityTypes()`),
**Add tag** (`PromptScreen`, input `#namespace:path`), **Edit** toggles `required`, **Remove** deletes
the selected raw value.

---

## 6. JSON output paths

Configs:
```
<world>/datapacks/l2hostility_editor/data/<namespace>/l2hostility_config/<difficulty|trait|weapon|entity>/<path>.json
```
(`PACK_FOLDER = "l2hostility_editor"`; path via `ConfigTypeEntry.asPath` = `data/<ns>/l2hostility_config/<name>/<path>`.
Serialization uses `JsonCodec.toJson(config, type.cls())` + pretty GSON, exactly like datagen.)
`pack.mcmeta` (`pack_format: 15`, description "L2Hostility Editor") is written once.

Tags:
```
<world>/datapacks/l2hostility_editor/data/l2hostility/tags/entity_types/<path>.json
```

New packs written to a world's `datapacks/` are **not auto-enabled**; apply via `/reload`, the
editor's Reload button, or the world's Datapack Selection screen.

## 7. Reload handling

Identical to Modular Golems: `EditorSaveState.savedFlag` set on every successful save; cleared when
an actual reload happens (Reload button / exit "Reload now") or on
`TagsUpdatedEvent`/`CLIENT_PACKET_RECEIVED` (manual `/reload`, world rejoin) via `EditorReloadHooks`.
Home screens show a **Reload** button (enabled while `savedFlag`); exiting with the flag set shows
`ReloadConfirmScreen`. Reload runs
`server.execute(() -> server.reloadResources(server.getPackRepository().getSelectedIds()))`.

## 8. Lang system

- Registered in `L2Hostility.gatherData` alongside `LangData::addTranslations`:
  `REGISTRATE.addDataGenerator(ProviderType.LANG, EditorText::genLang)` and
  `REGISTRATE.addDataGenerator(ProviderType.LANG, HostilityEditorLang::genLang)`.
- Rule of thumb (same as golems): anything not l2hostility-specific lives in `base/EditorText`
  (`editor.*`); content-category names (difficulty/trait/weapon/entity/tags, section labels, field
  labels, pick titles, tag labels) stay in `HostilityEditorLang` (`l2hostility.editor.*`).
- Chinese translations: extend `src/test/resources/l2hostility/lang/zh_cn/main.json` with nested
  `editor.*` / `l2hostility.editor.*` sections and run the `organize.ResourceOrganizer` lang merger
  (`LangFileOrganizer`) with Java 17 + gson/guava/datafixerupper from the gradle cache (see the
  Modular Golems note for the exact classpath invocation pattern).

## 9. Build / verification

- `./gradlew build` — compiles + reobf + jars. This is the "verification".
- `./gradlew runData` — regenerates `src/generated/resources` lang (`en_us.json`, `en_ud.json`).
  **Re-run and commit after changing any lang enum.** (`LHConfigGen` output also depends on which
  deps resolve at generation time.)
- No unit tests / CI / lint.
- Editor classes live in the main source set but must only be **referenced from client code**
  (`DifficultyScreen`, `L2HostilityClient`, `EditorReloadHooks` is `Dist.CLIENT`) so dedicated
  servers never load them — same placement strategy as Modular Golems.

## 10. Gotchas

- `src/generated/resources` is a real source set — never delete it.
- `base` must never import `dev.xkmc.l2hostility.*`; only l2library/l2serial/vanilla/net/registrate
  (those are already on our classpath).
- **Trait tab lists registered traits, not loaded config files** — otherwise traits whose datapack
  file is missing (falling back to `TraitConfig.DEFAULT`) couldn't be edited.
- **Empty `entities` list in a `EntityConfig.Config` inside difficulty default traits = "all
  entities" fallback** — the difficulty default-traits editor must allow creating one without a
  picked entity.
- **Tag edits write `replace: true`** — by design, but warn the user (mod-sourced additions to the
  same tag are overridden).
- `WeaponConfig.ItemConfig.condition` and `EntityConfig.Config.specialConditions` are
  code-typed/preserved but **not editable in v1** — they survive the `JsonCodec` deep copy, and
  `ItemConfig` edits must carry an existing `condition` forward rather than dropping it.
- Dimension keys aren't enumerable on the client (dynamic registry), so difficulty dimension keys
  are entered as typed `ResourceLocation`s (`PromptScreen`), while biomes/structures use pickers
  from the client `registryAccess()`.
- Do not edit any file under `../ModularGolems`; the editor's base layer is a **copy**, and future
  golem base fixes must be ported here by hand.

## 11. Known / pending items

- **Handler refactor** (optional): unify the four copied screens' constructor functional args behind
  one `EditorHandler<T>` (default `icon/percent/maxLevel`); new screens adopt it directly.
- **ItemCondition / specialConditions editing**: currently preserved-but-read-only; a full editor
  (advancement list + `BooleanValueCondition` config keys) can be added later.
- **Tag value drag-order / dedupe**: order is preserved but the UI does not reorder; duplicate raw
  values are shown as-is (MC tolerates them). Consider a dedupe warning.
- **`IngredientScreen`** is copied but unused (no L2Hostility config uses `Ingredient`); drop later
  if it stays dead.
