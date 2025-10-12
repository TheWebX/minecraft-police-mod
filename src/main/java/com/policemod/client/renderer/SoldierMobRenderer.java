package com.policemod.client.renderer;

import com.policemod.PoliceMod;
import com.policemod.entity.SoldierMob;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class SoldierMobRenderer extends HumanoidMobRenderer<SoldierMob, HumanoidModel<SoldierMob>> {
    private static final ResourceLocation SOLDIER_TEXTURE = new ResourceLocation(PoliceMod.MODID, "textures/entity/soldier_mob.png");
    
    public SoldierMobRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, 
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
    }
    
    @Override
    public ResourceLocation getTextureLocation(SoldierMob entity) {
        return SOLDIER_TEXTURE;
    }
}