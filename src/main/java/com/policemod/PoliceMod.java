package com.policemod;

import com.policemod.entity.PoliceMob;
import com.policemod.entity.BulletEntity;
import com.policemod.item.GunItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
// import net.minecraft.world.item.SpawnEggItem; // Temporarily disabled
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(PoliceMod.MODID)
public class PoliceMod {
    public static final String MODID = "policemod";
    
    public static final CreativeModeTab POLICE_TAB = PoliceModTab.POLICE_TAB;
    
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    
    // Entity Types
    public static final RegistryObject<EntityType<PoliceMob>> POLICE_MOB = ENTITIES.register("police_mob",
            () -> EntityType.Builder.of(PoliceMob::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .build("police_mob"));
    
    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITIES.register("bullet",
            () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.1f, 0.1f)
                    .build("bullet"));
    
    // Items
    public static final RegistryObject<Item> POLICE_GUN = ITEMS.register("police_gun",
            () -> new GunItem(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)));
    
    // Spawn egg temporarily disabled due to persistent registry timing issues
    // public static final RegistryObject<Item> POLICE_SPAWN_EGG = ITEMS.register("police_spawn_egg",
    //         () -> new SpawnEggItem(POLICE_MOB.get(), 0x0000FF, 0xFFFFFF, new Item.Properties()));
    
    // Sounds - temporarily disabled to avoid missing sound warnings
    // public static final RegistryObject<SoundEvent> GUN_SHOT = SOUNDS.register("gun_shot",
    //         () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "gun_shot")));
    
    // public static final RegistryObject<SoundEvent> POLICE_HURT = SOUNDS.register("police_hurt",
    //         () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "police_hurt")));
    
    // public static final RegistryObject<SoundEvent> POLICE_DEATH = SOUNDS.register("police_death",
    //         () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "police_death")));
    
    public PoliceMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUNDS.register(modEventBus);
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::entityAttributes);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Common setup code here
    }
    
    @SubscribeEvent
    public void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(POLICE_MOB.get(), PoliceMob.createAttributes().build());
    }
}