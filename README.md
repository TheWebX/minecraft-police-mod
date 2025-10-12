# Police Mod for Minecraft 1.19.3

A Minecraft Forge mod that adds police mobs armed with guns to defend villagers from threats.

## Features

- **Police Mobs**: Custom police officer entities that patrol villages
- **Gun Weapons**: Police officers are armed with guns that shoot bullets
- **Villager Protection**: Police officers automatically defend villagers from hostile mobs and players
- **Smart AI**: Police officers have intelligent behavior to patrol, detect threats, and protect villagers
- **Custom Items**: Police gun weapon and spawn egg for spawning police officers

## How to Use

1. **Installation**: Place the mod jar file in your Minecraft mods folder
2. **Spawning Police**: Use the Police Spawn Egg from the creative menu or craft it
3. **Crafting**: Craft a Police Gun using iron ingots and sticks
4. **Protection**: Police officers will automatically spawn in villages and protect villagers

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
```
 E 
EVE
 E 
```
- E = Egg
- V = Iron Ingot

## Behavior

- Police officers patrol villages and surrounding areas
- They automatically detect and attack hostile mobs (zombies, skeletons, etc.)
- They will attack players who harm villagers
- Police officers have a 1-second cooldown between gunshots
- They drop their gun when killed

## Technical Details

- Built for Minecraft 1.19.3 with Forge
- Uses custom AI goals for intelligent behavior
- Implements custom projectile system for bullets
- Includes custom textures and models

## Building from Source

1. Clone this repository
2. Run `./gradlew build` to build the mod
3. The built jar will be in `build/libs/`

## License

MIT License - feel free to modify and distribute.