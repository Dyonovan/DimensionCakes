package com.dyonovan.dimensioncakes.common.items;

import com.dyonovan.dimensioncakes.common.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PairedCakeItemBlock extends BlockItem {

    public PairedCakeItemBlock(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);

        if (stack.getItem().equals(ModBlocks.itemPairedCake.get())) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("uuid", UUID.randomUUID());
            stack.setTag(tag);
        }
    }


    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        if (itemStack.hasTag()) {
            UUID uuid = itemStack.getTag().getUUID("uuid");
            tooltip.add(Component.translatable(this.getDescriptionId() + ".tooltip", uuid.toString()));
        } else {
            tooltip.add(Component.translatable("error." + this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.RED));
        }
    }
}
