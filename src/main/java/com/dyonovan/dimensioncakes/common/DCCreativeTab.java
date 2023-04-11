package com.dyonovan.dimensioncakes.common;

import com.dyonovan.dimensioncakes.DimensionCakes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DCCreativeTab extends CreativeModeTab {

    public final static DCCreativeTab INSTANCE = new DCCreativeTab();
    public DCCreativeTab() {
        super(DimensionCakes.MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.CAKE);
    }
}
