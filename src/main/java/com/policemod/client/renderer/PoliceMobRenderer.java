package com.policemod.client.renderer;

import com.policemod.PoliceMod;
import com.policemod.entity.PoliceMob;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PoliceMobRenderer extends HumanoidMobRenderer<PoliceMob, HumanoidModel<PoliceMob>> {
    private static final ResourceLocation POLICE_TEXTURE = new ResourceLocation(PoliceMod.MODID, "textures/entity/police_mob.png");
    
    public PoliceMobRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, 
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
    }
    
    @Override
    public ResourceLocation getTextureLocation(PoliceMob entity) {
        return POLICE_TEXTURE;
    }
    
    @Override
    protected void setupRotations(PoliceMob entityLiving, com.mojang.blaze3d.vertex.PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, poseStack, ageInTicks, rotationYaw, partialTicks);
    }
    
    @Override
    protected void prepareMobModel(PoliceMob entity, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        
        // Modify arm position for shooting animation
        if (entity.isAggressive() && entity.getTarget() != null) {
            // Raise right arm when shooting
            this.getModel().rightArm.xRot = -1.5F;
            this.getModel().rightArm.yRot = 0.0F;
            this.getModel().rightArm.zRot = 0.0F;
        } else {
            // Reset arm position when not shooting
            this.getModel().rightArm.xRot = 0.0F;
            this.getModel().rightArm.yRot = 0.0F;
            this.getModel().rightArm.zRot = 0.0F;
        }
    }
    
    
}