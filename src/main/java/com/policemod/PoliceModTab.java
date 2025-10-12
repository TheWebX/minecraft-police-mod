package com.policemod;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class PoliceModTab {
    public static final CreativeModeTab POLICE_TAB = new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
            .title(net.minecraft.network.chat.Component.translatable("itemGroup.policemod"))
            .icon(() -> new ItemStack(net.minecraft.world.item.Items.IRON_SWORD)) // Use a default item for now
            .build();
}