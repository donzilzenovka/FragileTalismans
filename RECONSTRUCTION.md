# FragileTalismans reconstruction plan

Standalone Forge 1.7.10 replacement for Talismans 2 v1.1.6, using the original JAR as a behavioural reference, **not** as a runtime dependency.

## Scope
- Audit original registrations, recipes, textures, effects and optional integrations before reimplementing.
- Recreate working core talismans and omit confirmed WIP/broken ones; Crafting Talisman is provisionally excluded as WIP.
- Preserve registry identifiers for world compatibility where possible, subject to verification.
- Retain Baubles compatibility. Audit Botania, Blood Magic and Thaumcraft integrations separately.
- Provide extensible architecture for future original talismans.

## Durability design
- Maximum durability configurable, default **256**; 0 disables wear.
- Server-side shared Forge damage handler, targeting each equipped talisman after qualifying player damage.
- Approximate vanilla armour durability scaling, including minimum wear on a qualifying hit and no wear for armour-bypassing damage.
- Breakage must clear the Baubles slot and run equivalent unequip cleanup, particularly potion effects.
- Test cancellation, multiplayer sync, existing stacks and damage interactions with other mods.

## Status
Specification only; not yet a playable mod. No original JAR or decompiled source is committed.
