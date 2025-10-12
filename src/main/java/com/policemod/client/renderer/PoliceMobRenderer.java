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
    public void render(PoliceMob entity, float entityYaw, float partialTicks, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource bufferSource, int packedLight) {
        // Modify arm position before rendering
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
        
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
    
    
}