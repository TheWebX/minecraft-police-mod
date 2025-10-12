package com.policemod.client.renderer;

import com.policemod.PoliceMod;
import com.policemod.entity.BulletEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.projectile.ItemSupplier;

public class BulletRenderer extends ThrownItemRenderer<BulletEntity> {
    public BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}