package com.policemod.client.renderer;

import com.policemod.item.CustomSpawnEggItem;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public class CustomSpawnEggRenderer implements ItemColor {
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (stack.getItem() instanceof CustomSpawnEggItem) {
            return ((CustomSpawnEggItem) stack.getItem()).getColor(tintIndex);
        }
        return 0xFFFFFF;
    }
}