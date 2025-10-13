package com.policemod.item;

import com.policemod.PoliceMod;
import com.policemod.entity.BulletEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GunItem extends Item {
    public GunItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            // Find target
            HitResult hitResult = player.pick(64.0D, 1.0F, true);
            
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                LivingEntity target = (LivingEntity) entityHitResult.getEntity();
                
                // Don't shoot villagers or other police
                if (target instanceof net.minecraft.world.entity.npc.Villager || 
                    target instanceof com.policemod.entity.PoliceMob) {
                    return InteractionResultHolder.pass(itemStack);
                }
                
                // Shoot bullet
                shootBullet(level, player, target);
            } else {
                // Shoot in the direction the player is looking
                Vec3 lookDirection = player.getLookAngle();
                shootBullet(level, player, lookDirection);
            }
            
            // Play sound
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
            
            // Add cooldown
            player.getCooldowns().addCooldown(this, 20); // 1 second cooldown
            
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
    
    private void shootBullet(Level level, Player player, LivingEntity target) {
        Vec3 startPos = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
        Vec3 direction = targetPos.subtract(startPos).normalize();
        
        BulletEntity bullet = new BulletEntity(PoliceMod.BULLET.get(), level);
        bullet.setPos(startPos);
        bullet.shoot(direction.x, direction.y, direction.z, 2.0F, 1.0F);
        bullet.setOwner(player);
        
        level.addFreshEntity(bullet);
    }
    
    private void shootBullet(Level level, Player player, Vec3 direction) {
        Vec3 startPos = player.position().add(0, player.getEyeHeight(), 0);
        
        BulletEntity bullet = new BulletEntity(PoliceMod.BULLET.get(), level);
        bullet.setPos(startPos);
        bullet.shoot(direction.x, direction.y, direction.z, 2.0F, 1.0F);
        bullet.setOwner(player);
        
        level.addFreshEntity(bullet);
    }
}