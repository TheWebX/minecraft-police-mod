package com.policemod;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class PoliceModTab {
    public static final CreativeModeTab POLICE_TAB = new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
            .title(net.minecraft.network.chat.Component.translatable("itemGroup.policemod"))
            .icon(() -> new ItemStack(PoliceMod.POLICE_GUN.get()))
            .build();
}