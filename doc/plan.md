# Plan: Trait Exclusion Editor Tab (grid view)

Adds a seventh editor tab, **Trait Exclusion**, to the in-game datapack editor. Navigation stays
**per trait** (like the Trait tab), but editing happens on **relation groups**: entering a trait
opens the exclusion **grid** for the connected component that trait belongs to, and saving the grid
writes configs for **all traits in the group**. The data model is unchanged — groups are purely a
display/editing view over the existing per-trait `trait_exclusion` files.

Design written against commit `61891ee` ("design v1"). Status: **proposal — not implemented.**

---

## 1. Background

### 1.1 Data model (unchanged)

`content/config/TraitExclusion.java` — per **carrier** trait, a `HashMap<ResourceLocation, Double>`
`excluded` (excluded trait id → factor). At roll time `chance *= 1 − factor` per the two-directional
logic in TraitGenerator.java:178-243. Files live at
`data/<ns>/l2hostility_config/trait_exclusion/<carrier>.json`, merged per carrier id
(`TRAIT_EXCLUSION`, L2Hostility.java:62; read via `MobTrait.getExclusion()`, MobTrait.java:58-63).

### 1.2 The relation graph (why a grid works)

Interpret the data as a **directed graph**: edge `A→B` with factor `f` = "A present reduces B's
weight". Grouping uses **connected components** of the *undirected* version (an undirected edge per
`A→B` pair). From the shipped defaults this yields 7 clusters of size 2–5, e.g.:

| Cluster | Members |
|---|---|
| 1 | dementor, adaptive, dispell |
| 3 | freezing, blindness, levitation, nausea, slowness |
| 6 | soul_burner, fiery, poison, wither |

Every target of a member lies in the same cluster (by the definition of connected component), so each
cluster's **square member×member grid** contains all of that cluster's exclusions with no
cross-cluster leakage. The remaining ~35 traits have no exclusions → they belong to size-1 groups
("inactive"). A member with no owned file (`blindness` is excluded by `freezing` but has no
`blindness.json`) is still a row *and* a column — every member is a potential carrier.

### 1.3 Editor gap

`HostilityHomeScreen.TABS` (editor/home/HostilityHomeScreen.java:32-38) lists 6 tabs; nothing in
`editor/` imports `TraitExclusion`. `HostilityEditorUtil` lacks a `saveTraitExclusion`/`newTraitExclusion`.

---

## 2. Design decisions

### 2.1 Tab placement: index 2, right after **Trait**

Exclusion is trait semantics. Reindexes Weapon 2→3, Entity 3→4, Tags 4→5, Config 5→6 (ctor index
args + `tabTooltip(i)` switch in HostilityHomeScreen.java:81-89).

### 2.2 Home: a trait list split **Active / Inactive** — add/remove lives in the grid, not here

`TraitExclusionHomeScreen` mirrors `TraitHomeScreen` (rows = one per **registered trait** via
`LHTraits.TRAITS.get().getKeys()`; search bar; banned traits grey with the same tooltips;
`canCreate()/hasNew()` false — no New/Delete rows on the home; **Add / Remove exist only in the
group (grid) screen**, which is the one place relations are edited). The list is visually split into
two foldable sections:

- **Active** — traits that participate in ≥1 exclusion relation (as carrier *or* as target),
  loaded from `L2Hostility.TRAIT_EXCLUSION.getAll()`.
- **Inactive** — every registered trait with no exclusions at all.

Splitting is computed on open (a membership set over the loaded data) and is just a display grouping;
it is not stored. Search (`hasSearch`) matches trait names/ids across both sections.

### 2.3 Entering a trait opens its group's grid

Row click (`openEdit(id)`) opens `TraitExclusionGridScreen` for the **connected component containing
`id`** (union-find/DFS over the undirected edge set of the loaded `TRAIT_EXCLUSION` data; a trait
with no edges yields a 1-member group). So:

- clicking an **Active** trait opens the grid of its whole cluster (e.g. `freezing` opens the
  5-member freezing group);
- clicking an **Inactive** trait opens a 1×1 grid that is the entry point for *starting* relations.

Because groups are a view, a grid edit may span what were two clusters; after save, reopening the
home simply shows the merged group (see 2.5).

### 2.4 Grid widget: `ExclusionGridScreen<T>` (new in `editor/base/`)

Mod-independent and generic over the row/col key type (`base` never imports
`dev.xkmc.l2hostility.*`); `T=MobTrait`, with a `Handler<T>` (label, icon, header hint) mirroring
`DoubleMapScreen.Handler`.

Layout (drawn with `GuiGraphics`; `ObjectSelectionList` is 1-D and can't do a grid):
- **Sticky left column**: row trait names + rotation icons (handler supplies label/icon).
- **Header row**: column trait names (icons + short names, truncated with `…` at a cell-width cap so
  groups up to ~6 fit without horizontal scrolling).
- **Cells**: empty when no exclusion, else the factor (`0.5`, raw JSON value, not %). Diagonal
  (self) cells hidden. Directional: cell (A,B) and (B,A) are independent.
- **Vertical scrolling** via a clip + mouse wheel; a horizontal scrollbar is deferred (see §6 Q3) —
  shipped group sizes never exceed 5 columns.
- **Interaction**:
  - Click empty cell → `PromptScreen` ("A excludes B"); valid non-blank parse sets the factor.
  - Click filled cell → edit; **blank or `0` deletes the entry** (a 0-factor entry is meaningless —
    `getOrDefault` returns 0 anyway).
  - **`+ Add trait`** → `PickListScreen` over all registered traits
    (`HostilityEditorUtil.listTraits()`). Picking a trait brings it in as a row and a column
    (and if it belongs to another group, that group's members merge in — §2.5). This is **purely
    transient UI state**: it does *not* set `session.dirty` (no data was touched), so an add that is
    never backed by a real cell edit can be exited without a save prompt.
- **`- Remove trait`** → **nuke button, by design**: removes the selected trait from the grid
  entirely — deletes its row, its column, every cell referencing it in other rows, and its own
  carrier file. The trait moves to **Inactive** on the next home open. No confirm: this is the
  explicit "disconnect this trait from everything" action (see §2.5 for the non-destructive way to
  *split two groups*).

Under the hood the widget mutates a `Map<MobTrait, Map<MobTrait, Double>>` in place and sets
`session.dirty`, same as `DoubleMapScreen.promptValue/removeValue` (DoubleMapScreen.java:136-155).

### 2.5 Group editor `TraitExclusionGridScreen` and group merge

In `editor/config/`, extending `HostilityFileScreen` (reuses Save / exit-confirm / reload):

- **Load**: deep-copy each member of the entered trait's group from
  `TRAIT_EXCLUSION.getAll()` (`EditorUtil.copy`) into the local map. Unregistered ids present in
  loaded data (an addon removed that trait) stay as raw-id rows/columns so data round-trips.
- **Add trait / merge**: when the picker selects a trait *not* in the open group, its own group's
  members are pulled into the working map as rows+columns (**merge two groups together**). Nothing
  persists about the merge — the data is still just per-carrier maps; on `doSave()` every carrier in
  the merged set is written, and the next home open recomputes a single merged cluster.
- **Batch save — write non-empty, delete empty.** For every carrier in the working set:
  - ≥1 entry → write `trait_exclusion/<carrier>.json` (`EditorUtil.save(TRAIT_EXCLUSION, ...)`).
  - 0 entries → **delete the editor-pack file** for that carrier if it exists. A deletion only ever
    removes the editor's own file under `<save-root>/l2hostility_editor/`; the mod's built-in
    default (inside the jar datapack) is untouched and simply re-applies — so clearing a carried
    relation back to nothing restores the shipped defaults instead of shadowing them with an empty
    map. Files never opened/edited this session are not touched.
- **Freshly-added traits are transient by construction.** A trait pulled in via `+ Add trait` but
  left with **zero relations in either direction** produces *no* file change for itself: it has no
  carrier entries, so the save filter skips (writes nothing) and the delete branch is a no-op. Only
  real edits change files:
  - a new cell `A→X` re-writes **A's** file (the edge is persisted inside an existing member);
  - X only gains a *carrier* file if the user also sets cells in X's own row.
  Because home membership is recomputed from actual loaded relations, an untouched add disappears
  from the group on the next home open (back under **Inactive**) — exactly the desired behavior.
- Return to home after save → `EditorHomeScreen` re-derives the Active/Inactive split and the group
  boundaries, showing post-merge reality (a trait whose relations were all removed shows up under
  **Inactive**).
- **Splitting a merged group = zero the weakest link, not remove.** After an `+ Add trait` merge, two
  former groups are joined by the bridging edge(s). To split them back apart, set the weakest
  bridging cell to `0`/blank (`delete entry`) instead of nuking a trait: saving removes just that
  undirected edge, so the next home open derives **two separate components** again. Nuking a bridging
  trait would tear *both* sides apart, not just un-merge them.

---

## 3. Tab registration

`HostilityHomeScreen.TABS` insert `TraitExclusionHomeScreen::new` at index 2 + `tabTooltip(i)`
`case 2 → TAB_TRAIT_EXCLUSION_TIP`; reindex ctor args of `WeaponHomeScreen` 2→3,
`EntityHomeScreen` 3→4, `TagsHomeScreen` 4→5, `ConfigHomeScreen` 5→6.

---

## 4. Lang (`HostilityEditorLang`)

| Constant | key | default |
|---|---|---|
| `TRAIT_EXCLUSION` | `editor.trait_exclusion` | `Trait exclusion` |
| `ACTIVE` | `editor.active` | `Active` |
| `INACTIVE` | `editor.inactive` | `Inactive` |
| `EXCLUDED_SUMMARY` | `editor.summary.excluded` | `%s traits, %s links` |
| `TAB_TRAIT_EXCLUSION_TIP` | `editor.tip_tab_trait_exclusion` | `Group traits by which traits they exclude. Each group opens an exclusion grid.` (GRAY) |
| `GRID_HINT` | `editor.grid_hint` | `Row trait excludes the column trait. Empty cell = no exclusion; click to edit, clear to remove.` (GRAY) |
| `ADD_EXCLUDED` | `editor.add_excluded` | `Add trait` |
| `CLEAR_CELL` | `editor.clear_cell` | `Leave blank to remove the exclusion.` (GRAY) |
| `EXCLUDES_LABEL` | `editor.excludes` | `%s excludes` |

Reuses: `EditorText.ADD/EDIT/REMOVE/BACK/VALUE/INVALID_NUMBER/PICK_TARGET`, `SELECT_TRAIT`,
`HostilityEditorForms.counted` (the grid's Add/Remove buttons use the existing `ADD`/`REMOVE`).
Rerun `./gradlew runData` after adding (`HostilityEditorLang.genLang`).

---

## 5. Gotchas & risks

- **Group semantics are purely derived**: row count and membership must come from *loaded* data, not
  the registry — unregistered ids in existing files must survive as raw-id rows/columns, both for
  display and round-trip.
- **A carrier belongs to exactly one group**, so two open grids can never edit the same carrier file
  → no write conflicts; only cross-tab `session.dirty` of the *same* grid matters.
- **Deletion is editor-pack scoped**: `deleteTraitExclusion(carrier)` may only delete under
  `<save-root>/l2hostility_editor/`; a missing file is a no-op (mod defaults re-apply). Never attempt
  to delete inside the mod's jar datapack.
- **Diagonal cells hidden**; cell `0`/blank deletes the entry, and `- Remove trait` clears a whole
  row+column — the two ways relations disappear.
- **Adds are transient, removes are a nuke.** An untouched `+ Add trait` writes nothing and reverts
  on re-entry (no `session.dirty`). `- Remove trait` intentionally wipes the trait's *entire*
  relation set (including relations it had before a merge) and its carrier file — deliberate,
  no confirm. To split two merged groups, set the bridging cell to `0` instead.
- **Horizontal fit** caps column labels; shipped groups are ≤5 wide.
- **Index renumbering** is the only cross-tab blast radius (all `int`, so a wrong index won't fail
  compile — check in runClient).
- `base` stays mod-independent: `ExclusionGridScreen` is generic; only the handler + screens in
  `editor/util`/`editor/config` know about `MobTrait`.
- Editor classes stay client-only (referenced from client code only, `EditorReloadHooks` is
  `Dist.CLIENT`) — doc/editor.md §9.

---

## 6. Open questions

1. **`Home` split boundaries**: "Active" = carrier *or* target (chosen) vs. carrier-only. Carrier-
   or-target is the honest reading of "has relations".
2. **Horizontal scrollbar** for >6-column groups: defer (label truncation + full name in tooltip)
   until a real data case needs it.
3. **Search across sections** reuses `hasSearch`; foldable section headers reuses the existing
   `EditorHomeScreen` group-collapse mechanism (`[+]/[-]`, EditorHomeScreen in `base`).

---

## 7. Implementation checklist

1. `editor/base/ExclusionGridScreen.java` (new, generic) — §2.4.
2. `editor/util/HostilityEditorUtil`: `exclusionClusters()` (union-find/DFS over loaded data;
   returns clusters + active/inactive split), `groupOf(id)`, `newTraitExclusion()`,
   `saveTraitExclusion(id, config)` (per-carrier `EditorUtil.save(TRAIT_EXCLUSION, ...)`), and
   `deleteTraitExclusion(id)` (delete the editor-pack file only, no-op when absent).
3. `editor/home/TraitExclusionHomeScreen.java` (new) — §2.2/§2.3 (Active/Inactive sections; row →
   `groupOf(trait)` grid).
4. `editor/config/TraitExclusionGridScreen.java` (new) — §2.5 (load group; `+ Add trait` merge;
   `- Remove trait` clears a row+column; batch save = write non-empty, delete empty).
5. `HostilityHomeScreen.java`: `TABS` insert + `tabTooltip` case; reindex the four later home ctors.
6. `HostilityEditorLang`: §4 constants.
7. `./gradlew runData` → commit generated lang; `./gradlew build`; verify grid in runClient.
8. Optional zh_cn: `src/test/resources/l2hostility/lang/zh_cn/editor.json`
   (`editor.trait_exclusion*`) + lang organizer rerun.
9. On implementation: update `doc/editor.md` (§1 home table, new §4 grid block, §6 JSON path) and
   `doc/design.md` §14 (six → seven tabs).