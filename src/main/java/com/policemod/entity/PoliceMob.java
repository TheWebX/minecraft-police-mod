package com.policemod.entity;

import com.policemod.PoliceMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class PoliceMob extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> IS_AGGRESSIVE = SynchedEntityData.defineId(PoliceMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TARGET_VILLAGER_ID = SynchedEntityData.defineId(PoliceMob.class, EntityDataSerializers.INT);
    
    private int shootCooldown = 0;
    private LivingEntity targetVillager;
    
    public PoliceMob(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        // setMaxUpStep is not available in 1.19.3
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D); // Increased from 48 to 64 blocks
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_AGGRESSIVE, false);
        this.entityData.define(TARGET_VILLAGER_ID, -1);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new DefendVillagersGoal(this));
        this.goalSelector.addGoal(3, new DefendPoliceGoal(this));
        this.goalSelector.addGoal(4, new RetaliateGoal(this));
        this.goalSelector.addGoal(5, new PatrolVillageGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new AttackMonstersGoal(this));
        this.targetSelector.addGoal(3, new AttackPlayersGoal(this));
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        
        // Update target villager reference
        if (this.level instanceof ServerLevel) {
            int villagerId = this.entityData.get(TARGET_VILLAGER_ID);
            if (villagerId != -1) {
                Entity entity = ((ServerLevel) this.level).getEntity(villagerId);
                if (entity instanceof LivingEntity) {
                    this.targetVillager = (LivingEntity) entity;
                } else {
                    this.targetVillager = null;
                    this.entityData.set(TARGET_VILLAGER_ID, -1);
                }
            }
        }
    }
    
    public boolean canShoot() {
        return shootCooldown <= 0;
    }
    
    public void setShootCooldown(int cooldown) {
        this.shootCooldown = cooldown;
    }
    
    public void shootAtTarget(LivingEntity target) {
        if (!canShoot() || target == null) return;
        
        Vec3 startPos = this.position().add(0, this.getEyeHeight(), 0);
        Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
        
        // Add some prediction for moving targets
        Vec3 targetVelocity = target.getDeltaMovement();
        double distance = startPos.distanceTo(targetPos);
        double timeToHit = distance / 2.0; // Bullet speed is 2.0
        
        Vec3 predictedPos = targetPos.add(targetVelocity.scale(timeToHit));
        Vec3 direction = predictedPos.subtract(startPos).normalize();
        
        BulletEntity bullet = new BulletEntity(PoliceMod.BULLET.get(), this.level);
        bullet.setPos(startPos);
        bullet.shoot(direction.x, direction.y, direction.z, 2.5F, 0.5F); // Increased speed, reduced spread
        bullet.setOwner(this);
        bullet.setDamage(6.0D); // Increased damage from 4 to 6
        
        this.level.addFreshEntity(bullet);
        this.setShootCooldown(15); // Reduced cooldown for more aggressive shooting
        
        this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.VILLAGER_AMBIENT, 0.15F, 1.0F);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsAggressive", this.entityData.get(IS_AGGRESSIVE));
        compound.putInt("TargetVillagerId", this.entityData.get(TARGET_VILLAGER_ID));
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(IS_AGGRESSIVE, compound.getBoolean("IsAggressive"));
        this.entityData.set(TARGET_VILLAGER_ID, compound.getInt("TargetVillagerId"));
    }
    
    public boolean isAggressive() {
        return this.entityData.get(IS_AGGRESSIVE);
    }
    
    public void setAggressive(boolean aggressive) {
        this.entityData.set(IS_AGGRESSIVE, aggressive);
    }
    
    public LivingEntity getTargetVillager() {
        return targetVillager;
    }
    
    public void setTargetVillager(LivingEntity villager) {
        this.targetVillager = villager;
        if (villager != null) {
            this.entityData.set(TARGET_VILLAGER_ID, villager.getId());
        } else {
            this.entityData.set(TARGET_VILLAGER_ID, -1);
        }
    }
    
    // Custom AI Goals
    private static class DefendVillagersGoal extends Goal {
        private final PoliceMob police;
        private Villager targetVillager;
        private int scanTimer = 0;
        
        public DefendVillagersGoal(PoliceMob police) {
            this.police = police;
        }
        
        @Override
        public boolean canUse() {
            return true; // Always try to defend villagers
        }
        
        @Override
        public void start() {
            this.scanTimer = 0;
        }
        
        @Override
        public void tick() {
            this.scanTimer++;
            
            // Scan for villagers in danger every 20 ticks (1 second)
            if (this.scanTimer >= 20) {
                this.scanTimer = 0;
                this.targetVillager = this.police.level.getNearestEntity(Villager.class, 
                    net.minecraft.world.entity.ai.targeting.TargetingConditions.forCombat().range(24.0D), 
                    this.police, 
                    this.police.getX(), 
                    this.police.getY(), 
                    this.police.getZ(), 
                    this.police.getBoundingBox().inflate(24.0D, 4.0D, 24.0D));
                
                if (this.targetVillager != null) {
                    this.police.setTargetVillager(this.targetVillager);
                    this.police.setAggressive(true);
                }
            }
            
            // If we have a target villager, protect them
            if (this.targetVillager != null && this.targetVillager.isAlive()) {
                LivingEntity attacker = this.targetVillager.getLastHurtByMob();
                if (attacker != null && attacker != this.police) {
                    this.police.setTarget(attacker);
                    this.police.setAggressive(true);
                }
            }
        }
    }
    
    private static class DefendPoliceGoal extends Goal {
        private final PoliceMob police;
        private int scanTimer = 0;
        
        public DefendPoliceGoal(PoliceMob police) {
            this.police = police;
        }
        
        @Override
        public boolean canUse() {
            return true; // Always try to defend other police
        }
        
        @Override
        public void start() {
            this.scanTimer = 0;
        }
        
        @Override
        public void tick() {
            this.scanTimer++;
            
            // Scan for other police in danger every 20 ticks (1 second)
            if (this.scanTimer >= 20) {
                this.scanTimer = 0;
                PoliceMob otherPolice = this.police.level.getNearestEntity(PoliceMob.class, 
                    net.minecraft.world.entity.ai.targeting.TargetingConditions.forCombat().range(24.0D), 
                    this.police, 
                    this.police.getX(), 
                    this.police.getY(), 
                    this.police.getZ(), 
                    this.police.getBoundingBox().inflate(24.0D, 4.0D, 24.0D));
                
                if (otherPolice != null && otherPolice != this.police && otherPolice.isAlive()) {
                    LivingEntity attacker = otherPolice.getLastHurtByMob();
                    if (attacker != null && attacker != this.police) {
                        this.police.setTarget(attacker);
                        this.police.setAggressive(true);
                    }
                }
            }
        }
    }
    
    private static class PatrolVillageGoal extends Goal {
        private final PoliceMob police;
        private BlockPos patrolPos;
        private int patrolTimer = 0;
        
        public PatrolVillageGoal(PoliceMob police) {
            this.police = police;
        }
        
        @Override
        public boolean canUse() {
            return !this.police.isAggressive() && this.police.getRandom().nextFloat() < 0.02F;
        }
        
        @Override
        public void start() {
            this.patrolTimer = 0;
            // Find a random position within 16 blocks to patrol to
            double x = this.police.getX() + (this.police.getRandom().nextDouble() - 0.5D) * 32.0D;
            double z = this.police.getZ() + (this.police.getRandom().nextDouble() - 0.5D) * 32.0D;
            this.patrolPos = new BlockPos(x, this.police.getY(), z);
        }
        
        @Override
        public void tick() {
            if (this.patrolPos != null) {
                this.police.getNavigation().moveTo(this.patrolPos.getX(), this.patrolPos.getY(), this.patrolPos.getZ(), 1.0D);
                this.patrolTimer++;
                if (this.patrolTimer > 200 || this.police.distanceToSqr(this.patrolPos.getX(), this.patrolPos.getY(), this.patrolPos.getZ()) < 4.0D) {
                    this.patrolPos = null;
                }
            }
        }
    }
    
    private static class AttackMonstersGoal extends NearestAttackableTargetGoal<Monster> {
        public AttackMonstersGoal(PoliceMob police) {
            super(police, Monster.class, true);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && this.mob.isAggressive();
        }
    }
    
    private static class AttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
        public AttackPlayersGoal(PoliceMob police) {
            super(police, Player.class, true);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && this.mob.isAggressive();
        }
    }
    
    private static class RetaliateGoal extends Goal {
        private final PoliceMob police;
        private LivingEntity target;
        private int attackTimer = 0;
        private int followTimer = 0;
        
        public RetaliateGoal(PoliceMob police) {
            this.police = police;
        }
        
        @Override
        public boolean canUse() {
            LivingEntity target = this.police.getTarget();
            return target != null && target.isAlive() && this.police.isAggressive();
        }
        
        @Override
        public void start() {
            this.target = this.police.getTarget();
            this.attackTimer = 0;
            this.followTimer = 0;
        }
        
        @Override
        public void tick() {
            if (this.target == null || !this.target.isAlive()) {
                this.stop();
                return;
            }
            
            this.followTimer++;
            this.attackTimer++;
            
            // Follow the target
            if (this.police.distanceToSqr(this.target) > 64.0D) {
                this.police.getNavigation().moveTo(this.target, 1.0D);
            } else {
                this.police.getNavigation().stop();
            }
            
            // Look at target
            this.police.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            
            // Shoot at target if in range and cooldown is ready
            double distance = this.police.distanceToSqr(this.target);
            if (distance <= 1024.0D && this.police.canShoot()) { // 32 block range (increased from 24)
                this.police.shootAtTarget(this.target);
            }
            
            // If target is too far, stop following after 10 seconds
            if (this.followTimer > 200 && distance > 400.0D) {
                this.police.setTarget(null);
                this.police.setAggressive(false);
                this.stop();
            }
        }
        
        @Override
        public void stop() {
            this.target = null;
            this.attackTimer = 0;
            this.followTimer = 0;
        }
        
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}