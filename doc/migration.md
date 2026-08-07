# Migration: ModularGolems editor/base → L2Hostility editor/base

The generic editor base layer is **copied** from `../ModularGolems` and lives in
`dev.xkmc.l2hostility.editor.base`. This file records exactly what was copied, what changed, and
how to verify/update the copy. **Do not edit files under `../ModularGolems`.** Future base fixes in
ModularGolems must be ported here by hand.

## Source

`../ModularGolems/src/main/java/dev/xkmc/modulargolems/editor/base/` (22 files), package
`dev.xkmc.modulargolems.editor.base`.

## Destination

`src/main/java/dev/xkmc/l2hostility/editor/base/` (26 files), package
`dev.xkmc.l2hostility.editor.base`.

## Copied files (package renamed only)

`DoubleMapScreen`, `EditorFile`, `EditorHomeScreen`, `EditorLayout`, `EditorList`,
`EditorSaveState`, `EditorScreen`, `EditorSession`, `EditorTab`, `EditorText`, `EditorToast`,
`EditorUtil`, `ExitConfirmScreen`, `IngredientScreen`, `ItemListScreen`, `LinkButton`,
`Obj2IntMapScreen`, `PickListScreen`, `PromptScreen`, `ReloadConfirmScreen`, `TabButton`,
`package-info.java`.

Copy procedure (zsh, from repo root):

```sh
mkdir -p src/main/java/dev/xkmc/l2hostility/editor/base
cp ../ModularGolems/src/main/java/dev/xkmc/modulargolems/editor/base/*.java \
   src/main/java/dev/xkmc/l2hostility/editor/base/
sed -i '' 's/dev\.xkmc\.modulargolems\.editor\.base/dev.xkmc.l2hostility.editor.base/g' \
   src/main/java/dev/xkmc/l2hostility/editor/base/*.java
```

## Edits to copied files (beyond package rename)

1. `EditorFile.java` — `pack.mcmeta` description string: `"L2Hostility Editor"` (was golems' text).
2. `EditorHomeScreen.java` — added `protected boolean canCreate() { return true; }`; the bottom
   **New** button's `active` is initialized from it (tag and trait tabs override it to `false`).
3. `package-info.java` — javadoc updated to describe the L2Hostility copy.

## New files in base (not from ModularGolems)

All mod-independent; added because L2Hostility's data model is mostly records/lists:

- `FormScreen<T>` — generic multi-field form (`FormSpec<T>`/`FormField`; bool fields render as
  toggles; Enter submits; validation inline).
- `ListEditScreen<T>` — ordered `List<T>` editor (Add/Edit/Remove/Back) driven by
  `Handler<T> { label, icon, summary, onAdd(Consumer<T>, Screen), onEdit(T, Consumer<T>, Screen) }`.
- `ValueMapScreen<K,V>` — `Map<K,V>` editor (Add via pick or typed key / Edit via form / Remove).
- `TagFile` — generic tag-file I/O (`data/<ns>/tags/entity_types/<path>.json`), saves with
  `"replace": true`, reuses `EditorFile.writePackMeta`.
- `EditorHandler<T>` — one interface extending `PickListScreen.Handler`, `ItemListScreen.Handler`,
  `DoubleMapScreen.Handler`, `Obj2IntMapScreen.Handler` with default `icon()→null`,
  `percent()→false`, `maxLevel()→100`, `no-op onSelect()`; `of(label, icon)` factory returning the
  `Impl` record, plus a `Pick` record wrapper to attach an `onSelect`.

## Verification

1. `./gradlew build` — compile + reobf (the base must compile standalone).
2. Grep the base package for accidental imports:
   `rg 'import dev\.xkmc\.l2hostility' src/main/java/dev/xkmc/l2hostility/editor/base/` → must be empty.
3. Every copied class name matches the ModularGolems origin (diff with `diff -r` on the jar sources
   or the original directory, ignoring package + the three documented edits).
4. Runtime check: open the editor from the Difficulty screen, switch all five tabs, edit + save a
   file, reload, and confirm the JSON lands in the world `datapacks/l2hostility_editor/`.

## Known deviations / notes

- `IngredientScreen` is copied but currently unused by L2Hostility (no config uses `Ingredient`);
  kept for completeness, drop later if it stays dead (see `doc/editor.md` §11).
- The four copied screen classes still take functional constructor args; the `EditorHandler`
  consolidation is applied to new screens only (retrofitting is optional/deferred).
