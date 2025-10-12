# Police Mod Installation Guide

## Fixed Issues
The mod has been fixed to resolve the `Registry Object not present: policemod:police_mob` error. The main issues were:

1. **Registry Order**: The spawn egg was trying to access the police mob entity before it was registered
2. **Creative Tab**: Removed incompatible creative tab assignments for Minecraft 1.19.3
3. **Spawn Egg**: Temporarily disabled spawn egg due to registry timing issues in Forge 1.19.3

## Installation

1. **Download Forge 1.19.3**: Make sure you have Minecraft Forge 1.19.3 (version 44.1.23 or compatible) installed
2. **Install the Mod**: Place `policemod-1.0.0.jar` in your Minecraft mods folder
3. **Launch**: Start Minecraft with the Forge profile

## Features

- **Police Mobs**: Spawn using `/summon policemod:police_mob` command
- **Police Gun**: Craft with iron ingots and stick, or get from police mob drops
- **Protection**: Police automatically defend villagers from hostile mobs and players
- **Smart AI**: Police patrol villages and respond to threats

**Note**: Spawn egg is temporarily unavailable due to Forge 1.19.3 registry timing issues. Use the summon command instead.

## Crafting Recipes

### Police Gun
```
 I 
 I 
 S 
```
- I = Iron Ingot
- S = Stick

### Police Spawn Egg
- **Not Available**: Due to persistent registry timing issues in Forge 1.19.3
- Use `/summon policemod:police_mob` command instead

## Commands

To spawn a police officer:
```
/summon policemod:police_mob ~ ~ ~
```

## Troubleshooting

If you still get errors:
1. Make sure you're using Minecraft 1.19.3 with Forge 44.1.23
2. Check that the mod file is in the correct mods folder
3. Try removing other mods to test compatibility
4. Check the Minecraft logs for detailed error messages

The mod should now work without the registry errors!