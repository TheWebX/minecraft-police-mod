package com.policemod.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class BulletEntity extends Projectile implements ItemSupplier {
    private int life = 0;
    private double damage = 3.0D;
    
    public BulletEntity(EntityType<? extends BulletEntity> type, Level level) {
        super(type, level);
    }
    
    @Override
    protected void defineSynchedData() {
        // No additional data needed
    }
    
    @Override
    public void tick() {
        super.tick();
        
        this.life++;
        if (this.life > 200) { // Remove after 10 seconds
            this.discard();
            return;
        }
        
        // Add particle trail
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleTypes.SMOKE, 
                this.getX(), this.getY(), this.getZ(), 
                0.0D, 0.0D, 0.0D);
        }
        
        // Check for collisions
        HitResult hitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
        }
        
        // Move the bullet
        Vec3 velocity = this.getDeltaMovement();
        this.setPos(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);
    }
    
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        
        // Don't hit the shooter
        if (entity == owner) {
            return;
        }
        
        // Don't hit other police or villagers
        if (entity instanceof PoliceMob || entity instanceof net.minecraft.world.entity.npc.Villager) {
            return;
        }
        
        // Damage the target
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            DamageSource damageSource = DamageSource.mobAttack((LivingEntity) owner);
            livingEntity.hurt(damageSource, (float) this.damage);
            
            // Add hit effect
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.CRIT, 
                    entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 
                    0.0D, 0.0D, 0.0D);
            }
        }
        
        this.discard();
    }
    
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        
        if (result.getType() == HitResult.Type.BLOCK) {
            // Add impact particles
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.SMOKE, 
                    this.getX(), this.getY(), this.getZ(), 
                    0.0D, 0.0D, 0.0D);
            }
        }
        
        this.discard();
    }
    
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }
    
    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Life", this.life);
        compound.putDouble("Damage", this.damage);
    }
    
    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.life = compound.getInt("Life");
        this.damage = compound.getDouble("Damage");
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    public void setDamage(double damage) {
        this.damage = damage;
    }
    
    public double getDamage() {
        return this.damage;
    }
    
    @Override
    public net.minecraft.world.item.ItemStack getItem() {
        return new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ARROW);
    }
}