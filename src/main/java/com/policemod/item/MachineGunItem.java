package com.policemod.item;

import com.policemod.PoliceMod;
import com.policemod.entity.BulletEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MachineGunItem extends Item {
    public MachineGunItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            // Get player's look direction
            Vec3 lookDirection = player.getLookAngle();
            
            // Create bullet
            BulletEntity bullet = new BulletEntity(PoliceMod.BULLET.get(), level);
            bullet.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
            bullet.shoot(lookDirection.x, lookDirection.y, lookDirection.z, 5.0F, 0.1F); // Very fast, very accurate
            bullet.setOwner(player);
            bullet.setDamage(8.0D); // High damage
            
            level.addFreshEntity(bullet);
            
            // Play sound
            player.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 0.8F);
            
            // Cooldown
            player.getCooldowns().addCooldown(this, 5); // Very fast shooting
        }
        
        return InteractionResultHolder.success(itemStack);
    }
}