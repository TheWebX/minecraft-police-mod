package com.policemod.item;

import com.policemod.PoliceMod;
import com.policemod.entity.PoliceMob;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.CreativeModeTab;

public class PoliceSpawnEggItem extends SpawnEggItem {
    public PoliceSpawnEggItem() {
        super(PoliceMod.POLICE_MOB.get(), 0x0000FF, 0xFFFFFF, new Properties());
    }
}