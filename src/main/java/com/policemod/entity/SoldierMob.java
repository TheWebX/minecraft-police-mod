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

public class SoldierMob extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> IS_AGGRESSIVE = SynchedEntityData.defineId(SoldierMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TARGET_VILLAGER_ID = SynchedEntityData.defineId(SoldierMob.class, EntityDataSerializers.INT);
    
    private int shootCooldown = 0;
    private LivingEntity targetVillager;
    
    public SoldierMob(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D) // Higher health than police
                .add(Attributes.MOVEMENT_SPEED, 0.3D) // Faster movement
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 100.0D); // Increased from 80 to 100 blocks
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
        this.goalSelector.addGoal(2, new AlwaysTargetHostilesGoal(this));
        this.goalSelector.addGoal(3, new DefendVillagersGoal(this));
        this.goalSelector.addGoal(4, new DefendPoliceGoal(this));
        this.goalSelector.addGoal(5, new RetaliateGoal(this));
        this.goalSelector.addGoal(6, new PatrolVillageGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        
        this.targetSelector.addGoal(1, new SafeHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new AttackSlimesGoal(this));
        this.targetSelector.addGoal(3, new AttackMonstersGoal(this));
        this.targetSelector.addGoal(4, new AttackPlayersGoal(this));
        this.targetSelector.addGoal(5, new AvoidFriendlyFireGoal(this));
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
        double timeToHit = distance / 4.0; // Bullet speed is 4.0
        
        Vec3 predictedPos = targetPos.add(targetVelocity.scale(timeToHit));
        Vec3 direction = predictedPos.subtract(startPos).normalize();
        
        BulletEntity bullet = new BulletEntity(PoliceMod.BULLET.get(), this.level);
        bullet.setPos(startPos);
        bullet.shoot(direction.x, direction.y, direction.z, 5.0F, 0.2F); // Even faster bullets, very low spread
        bullet.setOwner(this);
        bullet.setDamage(8.0D); // Higher damage than police
        
        this.level.addFreshEntity(bullet);
        this.setShootCooldown(5); // Even faster shooting rate (machine gun)
        
        this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 0.8F);
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
    
    // Custom AI Goals (same as police but with different ranges)
    private static class DefendVillagersGoal extends Goal {
        private final SoldierMob soldier;
        private Villager targetVillager;
        private int scanTimer = 0;
        
        public DefendVillagersGoal(SoldierMob soldier) {
            this.soldier = soldier;
        }
        
        @Override
        public boolean canUse() {
            return true;
        }
        
        @Override
        public void start() {
            this.scanTimer = 0;
        }
        
        @Override
        public void tick() {
            this.scanTimer++;
            
            if (this.scanTimer >= 20) {
                this.scanTimer = 0;
                this.targetVillager = this.soldier.level.getNearestEntity(Villager.class, 
                    net.minecraft.world.entity.ai.targeting.TargetingConditions.forCombat().range(32.0D), 
                    this.soldier, 
                    this.soldier.getX(), 
                    this.soldier.getY(), 
                    this.soldier.getZ(), 
                    this.soldier.getBoundingBox().inflate(32.0D, 4.0D, 32.0D));
                
                if (this.targetVillager != null) {
                    this.soldier.setTargetVillager(this.targetVillager);
                    this.soldier.setAggressive(true);
                }
            }
            
            if (this.targetVillager != null && this.targetVillager.isAlive()) {
                LivingEntity attacker = this.targetVillager.getLastHurtByMob();
                if (attacker != null && attacker != this.soldier) {
                    this.soldier.setTarget(attacker);
                    this.soldier.setAggressive(true);
                }
            }
        }
    }
    
    private static class DefendPoliceGoal extends Goal {
        private final SoldierMob soldier;
        private int scanTimer = 0;
        
        public DefendPoliceGoal(SoldierMob soldier) {
            this.soldier = soldier;
        }
        
        @Override
        public boolean canUse() {
            return true;
        }
        
        @Override
        public void start() {
            this.scanTimer = 0;
        }
        
        @Override
        public void tick() {
            this.scanTimer++;
            
            if (this.scanTimer >= 20) {
                this.scanTimer = 0;
                LivingEntity otherPolice = this.soldier.level.getNearestEntity(PoliceMob.class, 
                    net.minecraft.world.entity.ai.targeting.TargetingConditions.forCombat().range(32.0D), 
                    this.soldier, 
                    this.soldier.getX(), 
                    this.soldier.getY(), 
                    this.soldier.getZ(), 
                    this.soldier.getBoundingBox().inflate(32.0D, 4.0D, 32.0D));
                
                if (otherPolice != null && otherPolice != this.soldier && otherPolice.isAlive()) {
                    LivingEntity attacker = otherPolice.getLastHurtByMob();
                    if (attacker != null && attacker != this.soldier) {
                        this.soldier.setTarget(attacker);
                        this.soldier.setAggressive(true);
                    }
                }
            }
        }
    }
    
    private static class PatrolVillageGoal extends Goal {
        private final SoldierMob soldier;
        private BlockPos patrolPos;
        private int patrolTimer = 0;
        
        public PatrolVillageGoal(SoldierMob soldier) {
            this.soldier = soldier;
        }
        
        @Override
        public boolean canUse() {
            return !this.soldier.isAggressive() && this.soldier.getRandom().nextFloat() < 0.02F;
        }
        
        @Override
        public void start() {
            this.patrolTimer = 0;
            double x = this.soldier.getX() + (this.soldier.getRandom().nextDouble() - 0.5D) * 40.0D;
            double z = this.soldier.getZ() + (this.soldier.getRandom().nextDouble() - 0.5D) * 40.0D;
            this.patrolPos = new BlockPos(x, this.soldier.getY(), z);
        }
        
        @Override
        public void tick() {
            if (this.patrolPos != null) {
                this.soldier.getNavigation().moveTo(this.patrolPos.getX(), this.patrolPos.getY(), this.patrolPos.getZ(), 1.2D);
                this.patrolTimer++;
                if (this.patrolTimer > 200 || this.soldier.distanceToSqr(this.patrolPos.getX(), this.patrolPos.getY(), this.patrolPos.getZ()) < 4.0D) {
                    this.patrolPos = null;
                }
            }
        }
    }
    
    private static class AttackSlimesGoal extends NearestAttackableTargetGoal<net.minecraft.world.entity.monster.Slime> {
        public AttackSlimesGoal(SoldierMob soldier) {
            super(soldier, net.minecraft.world.entity.monster.Slime.class, true);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse();
        }
        
        @Override
        public void start() {
            super.start();
            // Make the soldier aggressive when they find a slime
            if (this.mob instanceof SoldierMob) {
                ((SoldierMob) this.mob).setAggressive(true);
            }
        }
    }
    
    private static class SafeHurtByTargetGoal extends HurtByTargetGoal {
        public SafeHurtByTargetGoal(SoldierMob soldier) {
            super(soldier);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && this.mob.getLastHurtByMob() != null && 
                   !(this.mob.getLastHurtByMob() instanceof PoliceMob) &&
                   !(this.mob.getLastHurtByMob() instanceof SoldierMob) &&
                   !(this.mob.getLastHurtByMob() instanceof Villager) &&
                   !(this.mob.getLastHurtByMob() instanceof IronGolem);
        }
    }
    
    private static class AvoidFriendlyFireGoal extends Goal {
        private final SoldierMob soldier;
        
        public AvoidFriendlyFireGoal(SoldierMob soldier) {
            this.soldier = soldier;
        }
        
        @Override
        public boolean canUse() {
            LivingEntity target = this.soldier.getTarget();
            if (target == null) return false;
            
            // If targeting a friendly entity, stop targeting
            if (target instanceof PoliceMob || 
                target instanceof SoldierMob || 
                target instanceof Villager ||
                target instanceof IronGolem) {
                this.soldier.setTarget(null);
                this.soldier.setAggressive(false);
                return true;
            }
            
            return false;
        }
        
        @Override
        public void start() {
            this.soldier.setTarget(null);
            this.soldier.setAggressive(false);
        }
    }
    
    private static class AlwaysTargetHostilesGoal extends Goal {
        private final SoldierMob soldier;
        private int scanTimer = 0;
        
        public AlwaysTargetHostilesGoal(SoldierMob soldier) {
            this.soldier = soldier;
        }
        
        @Override
        public boolean canUse() {
            return this.soldier.getTarget() == null;
        }
        
        @Override
        public void tick() {
            this.scanTimer++;
            if (this.scanTimer >= 20) { // Scan every second
                this.scanTimer = 0;
                this.scanForHostiles();
            }
        }
        
        private void scanForHostiles() {
            if (this.soldier.getTarget() != null) return;
            
            // Look for nearby hostile mobs
            LivingEntity nearestHostile = this.soldier.level.getNearestEntity(
                net.minecraft.world.entity.monster.Monster.class,
                net.minecraft.world.entity.ai.targeting.TargetingConditions.forCombat().range(32.0D),
                this.soldier,
                this.soldier.getX(),
                this.soldier.getY(),
                this.soldier.getZ(),
                this.soldier.getBoundingBox().inflate(32.0D)
            );
            
            if (nearestHostile != null) {
                this.soldier.setTarget(nearestHostile);
                this.soldier.setAggressive(true);
            }
        }
    }
    
    private static class AttackMonstersGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public AttackMonstersGoal(SoldierMob soldier) {
            super(soldier, LivingEntity.class, true);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse();
        }
        
        @Override
        public void start() {
            super.start();
            // Make the soldier aggressive when they find a target
            if (this.mob instanceof SoldierMob) {
                ((SoldierMob) this.mob).setAggressive(true);
            }
        }
        
        @Override
        protected boolean canAttack(LivingEntity target, net.minecraft.world.entity.ai.targeting.TargetingConditions conditions) {
            if (target == null) return false;
            
            // Don't attack peaceful animals, villagers, or golems
            if (target instanceof Villager || target instanceof IronGolem) {
                return false;
            }
            
            // Don't attack other police or soldiers
            if (target instanceof PoliceMob || target instanceof SoldierMob) {
                return false;
            }
            
            // Don't attack peaceful animals (cows, pigs, sheep, chickens, etc.)
            // But DO attack slimes and other hostile mobs
            if (target instanceof net.minecraft.world.entity.animal.Animal && 
                !(target instanceof net.minecraft.world.entity.monster.Monster) &&
                !(target instanceof net.minecraft.world.entity.monster.Slime)) {
                return false;
            }
            
            // Attack all other entities (hostile mobs including slimes, players, etc.)
            return true;
        }
        
    }
    
    private static class AttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
        public AttackPlayersGoal(SoldierMob soldier) {
            super(soldier, Player.class, true);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && this.mob.isAggressive();
        }
    }
    
    private static class RetaliateGoal extends Goal {
        private final SoldierMob soldier;
        private LivingEntity target;
        private int attackTimer = 0;
        private int followTimer = 0;
        
        public RetaliateGoal(SoldierMob soldier) {
            this.soldier = soldier;
        }
        
        @Override
        public boolean canUse() {
            LivingEntity target = this.soldier.getTarget();
            return target != null && target.isAlive() && this.soldier.isAggressive();
        }
        
        @Override
        public void start() {
            this.target = this.soldier.getTarget();
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
            if (this.soldier.distanceToSqr(this.target) > 100.0D) {
                this.soldier.getNavigation().moveTo(this.target, 1.2D);
            } else {
                this.soldier.getNavigation().stop();
            }
            
            // Look at target
            this.soldier.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            
            // Shoot at target if in range and cooldown is ready
            double distance = this.soldier.distanceToSqr(this.target);
            if (distance <= 2500.0D && this.soldier.canShoot()) { // 50 block range (increased from 40)
                this.soldier.shootAtTarget(this.target);
            }
            
            // If target is too far, stop following after 10 seconds
            if (this.followTimer > 200 && distance > 2500.0D) {
                this.soldier.setTarget(null);
                this.soldier.setAggressive(false);
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