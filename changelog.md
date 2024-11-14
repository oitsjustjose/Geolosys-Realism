# Natural Progression Changelog (1.20.1)

## 2.3.9

I've been super busy with work lately, so further updates are on an as-needed basis or unless someone provides a PR for
a fix or requested feature, like this one! Thanks for understanding ❤️

### Fixed:

- Pebbles not generating anywhere *except* for underwater.
    - Thank you to kaiser_czar for [this PR](https://github.com/oitsjustjose/Natural-Progression/pull/97) which fixed
      this issue.

## 2.3.8

### Fixed:

- Not being able to break Wood/Stone using the *right* tool.
- "This tool is too brittle to use" tooltip showing on tools when ToolNeutering is disabled

### Changed:

- Improved the layout and readability of the Config File
- Internal refactors to deduplicate the code that is responsible for checking if a block can be broken and handling
  damaging the player & cancelling the break progress

## 2.3.7

### Added:

- Options to Disable Wood & Stone Punching damage

### Fixed:

- Jade incompatibility, causing damage when just _looking_ at wood or stone with a bare hand
- Twig placement being inconsistent and being able to replace non-solid blocks

## 2.3.6

### Added:

- Ported over the config option for the "Incorrect Tool"
  damage - [thank you Saereth!](https://github.com/oitsjustjose/Natural-Progression/pull/83)
- Config option to control the Knapping mechanic entirely

### Fixed:

- Some of the Create Pebbles having a Missing Texture

## 2.3.5

### Initial Port to 1.20.1 and Neo

### Added:

- Support for Bamboo & Cherry Wood in Sawing/Axing Recipes

### Changed:

- If you were using the `makeGroundBlocksHarder` feature, the blocks that are considered "ground" are now determined via
  the Block tag `natprog:earthy_blocks`
- If you were using the ability to require tools to break *all* wood-based blocks, you will now need to add *every
  single block* to the tag `natprog:woods_requiring_tool` because Mojang decided to remove a **fundamental** component
  of Blocks that has been there since before I started modding in Beta 1.7.3 😡
- Same as above, but for the requirement of a pickaxe-adjacent tool to mine stone-based blocks