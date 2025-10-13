package com.policemod;

import com.policemod.client.renderer.BulletRenderer;
import com.policemod.client.renderer.PoliceMobRenderer;
import com.policemod.client.renderer.SoldierMobRenderer;
import com.policemod.client.renderer.CustomSpawnEggRenderer;
import com.policemod.item.CustomSpawnEggItem;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = PoliceMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PoliceModClient {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(PoliceMod.POLICE_MOB.get(), PoliceMobRenderer::new);
            EntityRenderers.register(PoliceMod.SOLDIER_MOB.get(), SoldierMobRenderer::new);
            EntityRenderers.register(PoliceMod.BULLET.get(), BulletRenderer::new);
        });
    }
    
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(new CustomSpawnEggRenderer(), PoliceMod.POLICE_SPAWN_EGG.get(), PoliceMod.SOLDIER_SPAWN_EGG.get());
    }
}