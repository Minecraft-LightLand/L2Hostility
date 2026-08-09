# L2Hostility Datapack Editor (`dev.xkmc.l2hostility.editor`)

Client-side datapack editor for the four `l2hostility_config` datapack types (difficulty,
trait, weapon, entity) plus an extra tab that edits the `l2hostility` namespace **tags** used for
the global entity black/white list (the per-trait black/white lists are edited from the Trait tab).

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
| `EditorFile` | config file machinery: `save(type,id,config,packFolder)`, `copy`, `parseId`, `validNamespace`, `worldDatapacks`/`currentWorldDir`, writes `pack.mcmeta`. **Improvements:** `pack.mcmeta` description string becomes "L2Hostility Editor"; save root resolved via `configRoot()`, which honors the pluggable `saveRootOverride` supplier (set from `HostilityEditorUtil` to read `LHConfig.CLIENT.editorSavePath`) before falling back to the current world's datapacks folder. |
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
| `EditorHomeScreen` | abstract shared home: grouped file list (namespace headers), top tab bar, New/Edit/Reload/Back bottom row (Reload only when `hasReload()`, which the config tab disables; New only when `hasNew()`, which the trait and config tabs disable). **Improvements:** add `protected boolean canCreate() { return true; }`; the New button's `active` is set from it (tag and trait tabs return `false`). Group headers are **foldable** (click toggles collapse, `[+]/[-]` marker; collapsed groups are skipped unless the search query matches the namespace). `hasSearch()` (default `false`) shows an `EditBox` search bar above the list; it filters rows by `namespace path` (collapsed groups auto-expand on a namespace match). Further overridable hooks: `rowSuffix(id)` (default `"   (count)"`), `fileTooltip(id)` (nullable tooltip per row, rendered via `EditorList`), `extraButtons()` (extra widgets inserted into the bottom row before New), `groupName(ns)` (translated group header; `ConfigHomeScreen` maps `client`/`common`), `hasReload()` (default `true`), `hasNew()` (default `true`). |
| `EditorList` | the `ObjectSelectionList` behind `EditorHomeScreen`; `EditorList.TooltipHolder` wraps a row entry + its tooltip, `renderRowTooltips(g)`/`renderRowTooltip(...)` draw the hovered row's tooltip. |
| `LinkButton`, `package-info.java` | underline-on-hover button; `@MethodsReturnNonnullByDefault` + `@ParametersAreNonnullByDefault`. |

New in `base` (all mod-independent):

| Class | Purpose |
|---|---|
| `FormScreen` | **generic multi-field form.** `FormSpec<T> = (List<FormField> fields, Function<List<String>, T> build)`; `FormField` is either text `(label, initial, validator)` or boolean `(label, initial)` (rendered as a toggle). Labels and controls share a row (label left, control right) and are rendered inside an `ObjectSelectionList` panel, so rows (label + edit box/toggle) are clipped to the content band and scrolled with the mouse wheel while the bottom buttons stay fixed. Confirmed values passed to `build` in field order; bools arrive as `"true"/"false"`. Used for every record/scalar edit in L2Hostility configs (see §4). |
| `ListEditScreen<T>` | **generic ordered-list editor** for `List<T>` (Add/Edit/Remove/Back). `Handler<T> = { label, icon, void onAdd(Consumer<T> onDone, Screen parent), void onEdit(T cur, Consumer<T> onDone, Screen parent) }`. Add calls `onAdd` (the handler opens whatever screen chain builds a default `T`), Edit calls `onEdit`; `onDone` replaces the item in the list + sets `session.dirty`. |
| `ValueMapScreen<K,V>` | **map editor with form-editable values**: Add (pick key from candidates via `PickListScreen` or type a key) / Edit (open `FormScreen` built from `FormSpec<V>`) / Remove. `Handler<K>` for the key label/icon + `Function<V,Component> summary` for the row text. |
| `TagFile` | generic tag-file I/O: `save(ResourceLocation tagId, JsonElement valuesArray, String packFolder)` writes `data/<ns>/tags/entity_types/<path>.json` with `{"replace": true, "values": [...]}` and (re)uses `EditorFile.writePackMeta`; `read(PackResources, tagId)` helper for raw value extraction. |

**Improvements over Modular Golems' base** (summary):
1. `EditorHomeScreen.canCreate()` hook (New disabled per tab).
2. `FormScreen`, `ListEditScreen`, `ValueMapScreen`, `TagFile` added (L2Hostility's data model is mostly records/lists, which the golem base can't edit).
3. `pack.mcmeta` description fixed to this mod.
4. `EditorHomeScreen` foldable group headers + optional `hasSearch()` search bar.
5. `EditorFile.configRoot()` — configurable save root (`LHConfig.CLIENT.editorSavePath`, default empty = world datapacks).
6. `EditorList.Entry` header clicks (fold toggle) — headers may carry an `onClick` + `collapsed` marker; a combined `(text, icon, onClick, data, grey)` constructor for styled file rows.
7. `EditorHomeScreen.isDisabled(id)` hook — rows render **light gray** when disabled (used by the trait tab).
8. *(optional polish, same as the "handler refactor" Modular Golems lists as pending)*: consolidate `PickListScreen/ItemListScreen/DoubleMapScreen/Obj2IntMapScreen` constructor functional args behind one `EditorHandler<T>` with default `icon()→null`, `percent()→false`, `maxLevel()→unbounded`. New screens (`ListEditScreen`, `ValueMapScreen`) should adopt this shape from the start; retrofitting the four copied screens is optional and can be deferred.

`base` must never import `dev.xkmc.l2hostility.*` (non-editor), matching the golem rule.

### util (l2hostility-specific shared)

| Class | Purpose |
|---|---|
| `HostilityEditorUtil` | registry/data access: `listEntityTypes` (from `ForgeRegistries.ENTITY_TYPES`, sorted, icons via `SpawnEggItem.byId`), `listTraits` (`LHTraits.TRAITS.get().getValues()`, label `MobTrait.getDesc()`, icon `trait.asItem()`), `listEnchantments`, `listBiomes` (from `Minecraft.getInstance().level.registryAccess()`), `listStructures` (from the **integrated server** registry access, `getSingleplayerServer()`; empty when no singleplayer server), `validateFileId`, `newDifficulty/newWeapon/newEntity`, `save` (wraps `EditorFile.save` with `PACK_FOLDER = "l2hostility_editor"`), tag helpers `listManagedTags()`, `traitBlackTag(trait)/traitWhiteTag(trait)`. |
| `HostilityEditorLang` | l2hostility-specific lang, keys under `l2hostility.editor.*` (tab/file titles, section labels, field labels, entity/trait/enchantment pick titles, tag labels, errors). |
| `HostilityEditorForms` | `FormSpec<T>` builders for every editable record/scalar: `DifficultyConfig`, `TraitConfig` fields, `ItemConfig`, `EnchConfig`, `TraitBase` (+ `TraitCondition`), `ItemPool`, `ItemEntry`, `MasterConfig`, `Minion`, `EntityConfig.Config` scalars. Includes the value↔string conversions (e.g. `DoubleMapScreen.format`-style trimming for doubles). |
| `HostilityEditorHandlers` | shared `EditorHandler` instances: `ENTITY_TYPE`, `TRAIT`, `ENCHANTMENT`, `BIOME`, `STRUCTURE`, `ITEM` (label/icon). |
| `EditorReloadHooks` | client Forge-bus subscriber: clears `EditorSaveState.savedFlag` on `TagsUpdatedEvent` with cause `CLIENT_PACKET_RECEIVED` (fires on manual `/reload` and world rejoin). Registered via `@Mod.EventBusSubscriber(modid = L2Hostility.MODID, value = Dist.CLIENT)`. |

### home

| Class | Purpose |
|---|---|
| `HostilityHomeScreen` | **one** `EditorHomeScreen` subclass parameterized by `TabKind { DIFFICULTY, TRAIT, WEAPON, ENTITY, TAGS, CONFIG }` (instead of five near-identical home subclasses). `tabs()` always returns all six tabs; `activeTab()`/`listFiles()`/`fileCount()`/`emptyMessage()`/`newFileDefault()`/`openNew()`/`openEdit()`/`validateId()`/`canCreate()` dispatch on the kind. `hasSearch()` returns `true` for **trait and entity** tabs (search bar); group headers are foldable everywhere. `isDisabled()` returns `true` for **trait** rows whose `MobTrait.isBanned()` holds (own `allow_*` toggle or, for legendary traits, the general legendary toggle) — drawn **light gray**, with a `fileTooltip` explaining which toggle disables them. The **Weapon / Entity tab labels** are drawn **red + strikethrough** when `enableEquipmentDatapack` / `enableEntitySpecificDatapack` is off (`featureDisabled`). The **Config** tab is `ConfigHomeScreen` (not `HostilityHomeScreen`): its list rows are the **client / common** config sections and it adds a **Reset** button. Holds the entry `parent` (the screen that opened the editor); all tab switches construct a new `HostilityHomeScreen(kind, parent)`. |

### config

Per-kind screens (details in §4):

| Kind | Classes |
|---|---|
| difficulty | `DifficultyFileScreen`, `DimLevelMapScreen`, `BiomeMapScreen`, `DefaultTraitsListScreen` |
| trait | `TraitFileScreen` |
| weapon | `WeaponFileScreen`, `ItemConfigListScreen`, `SpecialWeaponListScreen`, `EnchConfigListScreen`, `SetValueEditScreen` (shared value page: the picked set shown and edited inline via Add/Remove + scalar fields) |
| entity | `EntityFileScreen` (lists `EntityConfig.Config` directly), `ConfigListScreen` (shared list of `EntityConfig.Config`, used by difficulty default-traits only), `EntityConfigEntryScreen`, `TraitBaseListScreen`, `ItemPoolListScreen`, `ItemEntryListScreen`, `MasterConfigScreen` |
| config | `ConfigHomeScreen` (client/common section list + Reset), `LHConfigEdit` — read/write access to `LHConfig.COMMON`/`LHConfig.CLIENT` for the editor: `FieldDef(label, kind, ConfigValue)` with get/set/`toFormField()`/`reset()` (restores `getDefault()` switched on `Kind`), `traitToggle(path)` / `traitConfigFields(path)` (trait → Forge config mapping), `clientSections()` (non-trait client sections) / `generalSections()` (non-trait common sections) / `allHomeSections()`, `openSectionForm(...)` (a `FormScreen` that applies + saves the config and reopens the parent), `resetToDefault()` (resets every home section field then saves), `saveConfig()` (finds the `ModConfig`s for `COMMON_SPEC`/`CLIENT_SPEC` via `ConfigTracker` and calls `save()` on both). Lives in the config package on purpose (not base — the base copy stays mod-independent). |

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
- Every file row shows the file path + `(count)` (file's effective value count); a single-file group
  (e.g. an addon's one trait) shows the path too — no more modid-only fallback.
- **Edit** = deep copy (`EditorUtil.copy` → `JsonCodec` round-trip), **New** = fresh config with a
  default id. Dirty tracking via a shared `EditorSession`; **Save** disabled unless dirty; Edit/Remove
  disabled until a row is selected; bottom buttons on one centered row (`EditorLayout.centerRow`).
- **Save** prompts for a file id (prefilled), writes via
  `HostilityEditorUtil.save` → `EditorFile.save(type, id, config, PACK_FOLDER)` into
  `EditorFile.configRoot()`, stays on the file
  screen (dirty cleared), sets `EditorSaveState.savedFlag = true`. Exit with unsaved changes →
  `ExitConfirmScreen`.

Per-kind specifics:

### Difficulty
`DifficultyFileScreen` lists four fixed rows (like `MaterialEntryScreen`), each labelled with its
entry count via `HostilityEditorForms.counted(label, n)`:
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
  `PickListScreen` over `listStructures()` (fetched from the **integrated server** registry, so the
  row is disabled/greyed with a "singleplayer only" hint when no singleplayer server is present).

The "Difficulty" tab itself is labelled **World** (`HostilityEditorLang.WORLD`, key
`l2hostility.editor.world`); the section that lists this screen's rows is
`DifficultyFileScreen`, reachable from that tab.

### Trait
`TraitHomeScreen`'s file list = **all registered traits** (`LHTraits.TRAITS.get().getKeys()`),
**not** `L2Hostility.TRAIT.getAll()` — a trait without a datapack file still exists and falls back to
`TraitConfig.DEFAULT`, so editing must be possible for every trait. Rows show the **trait name**
(`MobTrait.getDesc()` via the `fileLabel` hook) instead of the raw registry id. Trait rows whose
`isBanned()` holds (own `allow_*` toggle, or the general legendary toggle for legendary traits) are
drawn **light gray**; their tooltip (`fileTooltip` hook) explains which toggle disables them —
`TRAIT_DISABLED_TOGGLE` when the trait's own `allow_*` is off, `TRAIT_DISABLED_LEGENDARY` when it is a
`LegendaryTrait` and the global legendary toggle is off. `canCreate()` returns `false`
(traits are code-defined). `TraitFileScreen` loads the current entry
(`TRAIT.getEntry(id)` or `TraitConfig.DEFAULT` as baseline), deep-copies it (restoring the id, which
the `JsonCodec` round-trip drops), and edits the four scalar fields `min_level, cost, max_rank,
weight` in a `FormScreen`. In addition it edits, **saved straight to the Forge config on confirm**
(via `LHConfigEdit.openSectionForm` → `saveConfig()`): a **Trait toggle** row (the `allow_<path>`
boolean, shown `Enabled`/`Disabled`) and a **Trait config** row (the config values that trait reads,
e.g. `tankHealth`/`tankArmor`/`tankTough` for `tank`, `fieryTime` for `fiery`, ...; traits without
dedicated config skip the row). The trait's derived blacklist/whitelist tags are edited **here** (each row
opens `TagEditScreen` for `<id>_blacklist` / `<id>_whitelist`); per-trait tags are no longer listed
in the Tags tab. The traffic-fields row appends a brief of the current values
(`HostilityEditorForms.traitFieldsSummary`); tag rows show plain **Blacklist** / **Whitelist**
labels (no tag id) + the effective entry count.

### Config
The **Config** tab (`TabKind.CONFIG`) edits the Forge configs directly, **excluding trait-related
config** (the `traits` section + the per-trait `Trait toggle` map, which are edited from the Trait
tab). `ConfigHomeScreen` groups its rows into **two categories** (`client` / `common`): the client
config (`l2hostility-client.toml`, sections `Overhead` / `Glasses` / `Misc`) and the common config
(`l2hostility-common.toml`, sections `Datapack` / `Scaling` / `Difficulty` / `Orb & Spawner` /
`Items` / `Performance`); each row opens a `FormScreen` over that section's fields, and confirming
 applies the values and **saves the config file(s)** (`LHConfigEdit.saveConfig`). A **Reset** button
 in the top row resets every editable field to its declared default
 (`FieldDef.reset` → `value.getDefault()` switched on `Kind`), saves, and calls
 `rebuildWidgets()` to refresh the page and tabs. The config tab has **no Reload button**
 (`hasReload()` returns `false` — it edits the Forge config, not datapacks). Rows read live values so
 returning from the form shows the new values.

### Weapon
`WeaponFileScreen` lists six fixed rows (each row shows its entry **count** and is drawn **grey**
when empty):
- **Melee / Ranged / Armors** → `ItemConfigListScreen` over `melee_weapons` / `ranged_weapons` /
  `armors` (`ArrayList<ItemConfig>`). Add/Edit open `SetValueEditScreen` — the value page
  (`level, weight`) with the item set edited **inline** (Add opens the single-select picker over
  the remaining items, Remove deletes the selection); building keeps the existing
  non-null `ItemCondition` **carried over unchanged** (see §7), and the empty/`AIR` entry from the
  default configs keeps working as-is. Rows whose `ItemConfig` holds several items **rotate through
  the item icons** (one per second, time-based).
- **Special weapons** → `SpecialWeaponListScreen` over `special_weapons`
  (`LinkedHashMap<LinkedHashSet<EntityType<?>>, ArrayList<ItemConfig>>`), shown as a list of
  `(entity-set, item-config-list)` entries. Row summary = **first entity name + entity count**
  (`HostilityEditorForms.entityListName`; `All entities` when the set is empty); the icon is the
  **first non-empty item** from the entry's item configs (spawn eggs are not used), **rotating
  through all items** when there are several (one per second). Add = pick multiple entity types (set
  editor) then an `ItemConfigListScreen`. The entry editor's two entry-point rows carry **count
  summaries** (`Entities (n)` and `Item config (m)` via `HostilityEditorForms.counted`).
- **Weapon / Armor enchantments** → `EnchConfigListScreen` over `weapon_enchantments` /
  `armor_enchantments`. Add/Edit open `SetValueEditScreen` — the value page (`level, chance`) with
  the enchantment set edited **inline** (Add opens the single-select picker over the remaining
  enchantments, Remove deletes the selection; icons `Items.ENCHANTED_BOOK`).

### Entity
`EntityHomeScreen` (the **Entity tab**) lists the entity config files (`L2Hostility.ENTITY.getAll()`);
when a file holds exactly **one** config whose `entities` list has exactly **one** entry, its row
suffix shows that entity's name in white instead of the `(1)` count (`rowSuffix` override).
`EntityFileScreen` lists `EntityConfig.list` (`ArrayList<EntityConfig.Config>`) directly, with
Add/Edit/Remove buttons on the bottom row (no intermediate "Configs" layer). Add = pick an entity
type (`PickListScreen`) → new `Config` with that entity + default difficulty, then open the entry
editor. Each row shows the **first entity name + entity count** (e.g. `Zombie ... (3)`, or
`All entities` for the empty fallback) followed by the trait count + difficulty summary; multi-entity
lists insert `...` between the name and count. Each row
opens `EntityConfigEntryScreen`; its rows append a **brief of the current values** via
`HostilityEditorForms` summaries (built from translatable `l2hostility.editor.summary_*` keys):
- **Entities** → prefixed with the translatable **`Applies to:`** label, then `ItemListScreen` over a
  `LinkedHashSet<EntityType<?>>` write-through view of
  `config.entities` (ArrayList-backed; sync back before save); the row label is the same
  first-entity + count name.
- **Difficulty** → `FormScreen` (7 fields) rebuilt into `DifficultyConfig` and stored back; the row
  appends `difficultySummary` (`minLv/base/var/scale%` brief; `scale` is shown as a percentage).
- **Traits** → `TraitBaseListScreen` over `config.traits()` (`ArrayList<TraitBase>`); row shows the
  trait count. Add = pick trait then `FormScreen` for `free, min` + `cap` (bool) + optional
  `TraitCondition` (`lv, chance, advancement id`; leaving `lv` blank stores a null condition →
  3-arg `TraitBase`-style serialization).
- **Trait blacklist** → `ItemListScreen` over `config.blacklist()` (`LinkedHashSet<MobTrait>`); row
  shows the blacklist count.
- **Items** → `ItemPoolListScreen` over `config.items`; row shows the pool count + the items/enchants
  summary. Add = `FormScreen` (`level, chance, slot`) then `ItemEntryListScreen` over `entries`
  (`ItemEntry(weight, ItemStack)`; Add = pick item then prompt `weight`).
- **Values** → `FormScreen` editing the public scalar fields directly: `minSpawnLevel, maxLevel,
  maxTraitCount` (ints), `healthScale, attackScale` (doubles, edited as floating point), `presetTraitsOnly`
  (bool); the row appends `entityValuesSummary` (min spawn / max level / max trait / hpScale / atkScale
  brief, scales shown as percentages; `maxLevel 0` shows **N/A** and `maxTraitCount -1` shows **∞**).
- **Master** → shown only when the config is a master (has an `asMaster` or includes the
  `l2hostility:master` trait). `MasterConfigScreen` edits the master fields
  (`maxTotalCount, spawnInterval` as inline edit boxes, first box focused on open; the boxes render
  with their normal background — `EditorList` disables the top/bottom shading strips that used to
  paint over widgets added before the list) and the **minions
  list on the same page**
  (Add/Edit/Remove; `Minion` is edited with a `FormScreen` for its 11 scalar fields + optional
  nested `Config`, labeled `max count`/`min level`/`hp scale`/`spawn range`/`cooldown`/...).
  Minion rows show the entity name + **health percentage** brief. The row appends
  `masterSummary` (minions/cap/interval). "Remove master" clears
  `asMaster`; with no master yet, a single "Add master"
  row creates one. On Back the (possibly new/edited) master is written back via the parent's
  `setMaster`.
- Rows are drawn **grey** when their content is empty or left at the default value (entities,
  traits, blacklist, items, values, difficulty, and master-with-no-config), using the `grey` flag
  on `EditorList.Entry`.
- `specialConditions` (code-defined subclasses) are **not editable**; they survive the
  `JsonCodec` round-trip copy and are preserved (see §7).

---

## 5. Tag editing (the extra tab)

### Managed tags
`listManagedTags()` returns (all in the `l2hostility` namespace, group header "L2Hostility"):
- **Entity black/white list**: `l2hostility:blacklist`, `l2hostility:whitelist` — consumed by
  `MobTraitCap.HOLDER` (`WHITELIST` grants the cap to any entity; otherwise `Enemy` not in
  `BLACKLIST`).
- **Fallback black/white lists**: `l2hostility:default_blacklist` / `l2hostility:default_whitelist`
  from `TraitConfig.DEFAULT`, which apply to any trait without its own datapack file
  (`MobTrait.getConfig()` → `L2Hostility.TRAIT.getEntry(...)`, falls back to `DEFAULT`).
- **Non-trait entity tags** (all the entity-type tags from `LHTagGen`):
  `no_scaling`, `no_trait`, `semiboss`, `effect_immune`, `no_drop`, `hide_traits`, `hide_level`,
  `hide_title`, `armor_target`, `melee_weapon_target`, `ranged_weapon_target`,
  `hostility_spawner_blacklist`.

Per-trait black/white tags (`l2hostility:<trait>_blacklist` / `..._whitelist`) are **not** listed
here — they are edited from the Trait tab, which opens `TagEditScreen` for each trait's tags
(consumed by `TraitConfig.allows(type)`, e.g. `l2hostility:split_whitelist`,
`l2hostility:invisible_blacklist`).

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
<save-root>/l2hostility_editor/data/<ns>/tags/entity_types/<path>.json
{
  "replace": true,
  "values": ["minecraft:zombie", {"id": "mod:boss", "required": false}, "#l2hostility:semiboss"]
}
```
where `<save-root>` is the current world's `datapacks/` folder by default (see §6 for the
configurable override). `replace: true` is deliberate: the editor pack is a world datapack that sits
above the mod's built-in datapack, so the edited tag fully overrides the generated one (required for
removals). Tradeoff (surfaced in a `SAVE_NOTE`-style hint): once a tag is edited, later additions to
that tag from mod datapacks are overridden until the editor tag is re-edited/removed. Tag ids are
fixed (the row you opened), so there is no file-id prompt; dirty tracking, Save enablement and
exit-confirm work as usual, and save sets `EditorSaveState.savedFlag`.

`TagEditScreen` actions: **Add entity** (`PickListScreen` over `listEntityTypes()`),
**Add tag** (`PromptScreen`, input `#namespace:path`), **Edit** toggles `required`, **Remove** deletes
the selected raw value.

---

## 6. JSON output paths

### Save root (configurable)
Every editor save goes through `EditorFile.configRoot()`. The base layer keeps a `saveRootOverride`
supplier (null by default); `HostilityEditorUtil` sets it to read
`LHConfig.CLIENT.editorSavePath` (client config, `l2hostility-client.toml`, default `""`). When the
override returns a path, it is used as the **datapacks folder** root; otherwise saves go to the
current world's `datapacks/` folder (`worldDatapacks()`). Set the config to the absolute path of a
datapacks folder (the folder that contains datapack pack folders) to save into a global datapack
loader location instead of the world — e.g. OpenLoader's datapacks folder or a modpack's global data
folder. The editor pack folder name (`l2hostility_editor`) is still resolved underneath the chosen
root. Defaulting to the world path keeps existing saves working unchanged.

Configs:
```
<save-root>/l2hostility_editor/data/<namespace>/l2hostility_config/<difficulty|trait|weapon|entity>/<path>.json
```
(`PACK_FOLDER = "l2hostility_editor"`; path via `ConfigTypeEntry.asPath` = `data/<ns>/l2hostility_config/<name>/<path>`.
Serialization uses `JsonCodec.toJson(config, type.cls())` + pretty GSON, exactly like datagen.)
`pack.mcmeta` (`pack_format: 15`, description "L2Hostility Editor") is written once.

Tags:
```
<save-root>/l2hostility_editor/data/l2hostility/tags/entity_types/<path>.json
```

New packs written to a world's `datapacks/` are **not auto-enabled**; apply via `/reload`, the
editor's Reload button, or the world's Datapack Selection screen. When saving to a custom
`editorSavePath`, enable the pack through the loader that reads that folder.

## 7. Reload handling

Identical to Modular Golems: `EditorSaveState.savedFlag` set on every successful save; cleared when
an actual reload happens (Reload button / exit "Reload now") or on
`TagsUpdatedEvent`/`CLIENT_PACKET_RECEIVED` (manual `/reload`, world rejoin) via `EditorReloadHooks`.
Home screens show a **Reload** button (enabled while `savedFlag`; the config tab hides it via
`hasReload()`); exiting with the flag set shows
`ReloadConfirmScreen`. Reload runs
`server.execute(() -> server.reloadResources(server.getPackRepository().getSelectedIds()))`.

## 8. Lang system

- Registered in `L2Hostility.gatherData` alongside `LangData::addTranslations`:
  `REGISTRATE.addDataGenerator(ProviderType.LANG, EditorText::genLang)` and
  `REGISTRATE.addDataGenerator(ProviderType.LANG, HostilityEditorLang::genLang)`.
- Rule of thumb (same as golems): anything not l2hostility-specific lives in `base/EditorText`
  (`editor.*`); content-category names (difficulty/trait/weapon/entity/tags, section labels, field
  labels, pick titles, tag labels) stay in `HostilityEditorLang` (`l2hostility.editor.*`).
- Row text uses translatable keys wherever a literal string made sense before: entry counts
  (`HostilityEditorForms.counted`), value summaries (`HostilityEditorLang.SUMMARY_*`), and the
  black/whitelist tag labels (plain `Blacklist`/`Whitelist`, no tag id).
- Chinese translations live in their **own file**
  `src/test/resources/l2hostility/lang/zh_cn/editor.json` (nested `editor.*` and
  `l2hostility.editor.*` sections, alongside the existing `main.json`/`item.json`/etc.); run the
  `organize.ResourceOrganizer` lang merger (`LangFileOrganizer`) with Java 17 +
  gson/guava/datafixerupper from the gradle cache (see the Modular Golems note for the exact
  classpath invocation pattern) to regenerate `src/main/resources/assets/l2hostility/lang/zh_cn.json`.
- The difficulty **tab title** is `World` (`HostilityEditorLang.WORLD`, `l2hostility.editor.world`).

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
  (those are already on our classpath). The configurable save root keeps this rule: base exposes
  `EditorFile.saveRootOverride` (a `Supplier<Path>`), and the l2hostility layer wires it to
  `LHConfig.CLIENT.editorSavePath` from `HostilityEditorUtil`'s static init.
- **Trait tab lists registered traits, not loaded config files** — otherwise traits whose datapack
  file is missing (falling back to `TraitConfig.DEFAULT`) couldn't be edited.
- **Empty `entities` list in a `EntityConfig.Config` inside difficulty default traits = "all
  entities" fallback** — the difficulty default-traits editor must allow creating one without a
  picked entity.
- **Tag edits write `replace: true`** — by design, but warn the user (mod-sourced additions to the
  same tag are overridden).
- **Trait configs are per-file, not merged** — the Trait tab edits each trait's own
  `L2Hostility.TRAIT.getEntry(id)`; the `JsonCodec` deep copy drops the protected `id`, so
  `TraitFileScreen` restores it (`TraitConfig.setId`) before editing (otherwise `getBlacklistTag()`
  crashes).
- **Config editing lives in the `config` package, not `base`** — config-related classes would have
  gone into `base` in an older port, but they must stay out of it (the base copy is a reusable
  mod-independent layer; 1.21.1 ships an official config editor so this won't be needed there).
  `LHConfigEdit` is the only place that reads/writes `LHConfig.COMMON` from the editor.
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
