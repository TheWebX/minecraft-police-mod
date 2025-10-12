package com.policemod.item;

import com.policemod.PoliceMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class CustomSpawnEggItem extends Item {
    private final Supplier<EntityType<?>> entityTypeSupplier;
    private final int primaryColor;
    private final int secondaryColor;
    private final String entityName;
    
    public CustomSpawnEggItem(Supplier<EntityType<?>> entityTypeSupplier, int primaryColor, int secondaryColor, String entityName, Properties properties) {
        super(properties);
        this.entityTypeSupplier = entityTypeSupplier;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.entityName = entityName;
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockState blockState = level.getBlockState(blockPos);
        
        BlockPos spawnPos;
        if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
            spawnPos = blockPos;
        } else {
            spawnPos = blockPos.relative(direction);
        }
        
        EntityType<?> entityType = this.entityTypeSupplier.get();
        if (entityType != null) {
            entityType.spawn((ServerLevel) level, itemStack, context.getPlayer(), spawnPos, MobSpawnType.SPAWN_EGG, true, false);
            if (context.getPlayer() != null) {
                itemStack.shrink(1);
            }
        }
        
        return InteractionResult.CONSUME;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("Spawns a " + entityName));
    }
    
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? this.primaryColor : this.secondaryColor;
    }
}