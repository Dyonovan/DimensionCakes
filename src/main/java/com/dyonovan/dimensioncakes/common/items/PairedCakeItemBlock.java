package com.dyonovan.dimensioncakes.common.items;

import com.dyonovan.dimensioncakes.common.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PairedCakeItemBlock extends BlockItem {

    public PairedCakeItemBlock(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player) {
        super.onCraftedBy(stack, level, player);

        if (stack.getItem().equals(ModBlocks.itemPairedCake.get())) {
            CompoundTag uuidTag = new CompoundTag();
            uuidTag.putUUID("uuid", UUID.randomUUID());

            CompoundTag tag = new CompoundTag();
            tag.put("BlockEntityTag", uuidTag);

            stack.setTag(tag);
        }
    }

    @Override
    protected boolean canPlace(BlockPlaceContext placeContext, @NotNull BlockState blockState) {
        if (placeContext.getItemInHand().hasTag()) {
            CompoundTag tag = placeContext.getItemInHand().getTagElement("BlockEntityTag");
            if (tag != null) {
                if (tag.contains("uuid"))
                    return super.canPlace(placeContext, blockState);
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTagElement("BlockEntityTag");
            if (tag != null) {
                if (tag.contains("uuid")) {
                    UUID uuid = tag.getUUID("uuid");
                    tooltip.add(Component.translatable(this.getDescriptionId() + ".tooltip", uuid.toString()));
                    return;
                }
            }
        }
        tooltip.add(Component.translatable("error." + this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.RED));
    }
}
