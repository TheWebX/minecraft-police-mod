# Police Mod for Minecraft 1.19.3

A Minecraft Forge mod that adds police mobs armed with guns to defend villagers from threats.

## Features

- **Police Mobs**: Custom police officer entities that patrol villages
- **Gun Weapons**: Police officers are armed with guns that shoot bullets
- **Villager Protection**: Police officers automatically defend villagers from hostile mobs and players
- **Smart AI**: Police officers have intelligent behavior to patrol, detect threats, and protect villagers
- **Custom Items**: Police gun weapon (spawn egg temporarily unavailable)

## How to Use

1. **Installation**: Place the mod jar file in your Minecraft mods folder
2. **Spawning Police**: Use the command `/spawnpolice` or `/summon policemod:police_mob` to spawn police officers
3. **Spawning Soldiers**: Use the command `/spawnsoldier` or `/summon policemod:soldier_mob` to spawn soldiers
4. **Crafting**: Craft a Police Gun using iron ingots and sticks
5. **Protection**: Police officers and soldiers will automatically defend villagers and each other from threats

### Commands

- `/spawnpolice [count]` - Spawn 1-10 police officers (requires OP)
- `/spawnsoldier [count]` - Spawn 1-10 soldiers (requires OP)
- `/summon policemod:police_mob` - Spawn 1 police officer
- `/summon policemod:soldier_mob` - Spawn 1 soldier

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
**Note**: Spawn egg is currently unavailable due to Forge 1.19.3 registry timing issues. Use `/summon policemod:police_mob` command instead.

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