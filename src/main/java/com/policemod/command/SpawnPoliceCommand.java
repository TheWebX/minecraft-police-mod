package com.policemod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.policemod.PoliceMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;

public class SpawnPoliceCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawnpolice")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("count", IntegerArgumentType.integer(1, 10))
                .executes(context -> {
                    int count = IntegerArgumentType.getInteger(context, "count");
                    CommandSourceStack source = context.getSource();
                    ServerLevel level = source.getLevel();
                    BlockPos pos = new BlockPos(source.getPosition());
                    
                    for (int i = 0; i < count; i++) {
                        EntityType<?> policeType = PoliceMod.POLICE_MOB.get();
                        policeType.spawn(level, (net.minecraft.world.item.ItemStack) null, null, pos, net.minecraft.world.entity.MobSpawnType.COMMAND, false, false);
                    }
                    
                    source.sendSuccess(Component.literal("Spawned " + count + " police officer(s)"), true);
                    return count;
                }))
            .executes(context -> {
                CommandSourceStack source = context.getSource();
                ServerLevel level = source.getLevel();
                BlockPos pos = new BlockPos(source.getPosition());
                
                EntityType<?> policeType = PoliceMod.POLICE_MOB.get();
                policeType.spawn(level, (net.minecraft.world.item.ItemStack) null, null, pos, net.minecraft.world.entity.MobSpawnType.COMMAND, false, false);
                
                source.sendSuccess(Component.literal("Spawned 1 police officer"), true);
                return 1;
            }));
    }
}