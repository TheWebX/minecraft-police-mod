package com.policemod;

import com.policemod.client.renderer.BulletRenderer;
import com.policemod.client.renderer.PoliceMobRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = PoliceMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PoliceModClient {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(PoliceMod.POLICE_MOB.get(), PoliceMobRenderer::new);
            EntityRenderers.register(PoliceMod.BULLET.get(), BulletRenderer::new);
        });
    }
}