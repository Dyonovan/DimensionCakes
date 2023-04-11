package com.dyonovan.dimensioncakes.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DCItemBlock extends BlockItem {
    public DCItemBlock(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        tooltip.add(Component.translatable(this.getDescriptionId(itemStack) + ".tooltip"));
    }
}
