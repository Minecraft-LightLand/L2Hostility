# Todo

- [x] Design a datapack editor for L2Hostility, reusing editor/base code from ../ModularGolems. Write design to doc/editor.md (not implemented yet).
- [ ] Copy editor/base files from ../ModularGolems into dev.xkmc.l2hostility.editor.base, write doc/migration.md, and implement the datapack editor per doc/editor.md.
  - [x] Copy base + new base files (FormScreen, ListEditScreen, ValueMapScreen, TagFile, EditorHandler)
  - [x] util layer (HostilityEditorUtil/Forms/Handlers/Lang, EditorReloadHooks)
  - [x] tag layer (TagValue, HostilityTagUtil, TagEditScreen)
  - [x] home layer (TabKind, HostilityHomeScreen)
  - [x] config screens (DifficultyFileScreen/WeaponFileScreen/TraitFileScreen/EntityFileScreen and all shared list screens)
  - [x] entry point wiring (DifficultyScreen button + lang gen in L2Hostility.gatherData)
  - [ ] runtime smoke test (build + runData pass; manual runClient check pending)
