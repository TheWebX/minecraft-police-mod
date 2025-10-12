package com.policemod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.RegistryObject;

public class DeferredSpawnEggItem extends SpawnEggItem {
    private final RegistryObject<?> entityType;
    
    public DeferredSpawnEggItem(RegistryObject<?> entityType, int primaryColor, int secondaryColor, Properties properties) {
        super(null, primaryColor, secondaryColor, properties);
        this.entityType = entityType;
    }
    
    @Override
    public net.minecraft.world.entity.EntityType<?> getType(net.minecraft.nbt.CompoundTag nbt) {
        return (net.minecraft.world.entity.EntityType<?>) entityType.get();
    }
}