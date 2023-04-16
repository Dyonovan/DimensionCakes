package com.dyonovan.dimensioncakes.common.items;

import com.dyonovan.dimensioncakes.DimensionCakes;
import com.dyonovan.dimensioncakes.DimensionCakesConfig;
import com.dyonovan.dimensioncakes.common.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DCItemBlock extends BlockItem {

    public DCItemBlock(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        Item item = getRefillMaterial(itemStack);

        tooltip.add(Component.translatable(this.getDescriptionId(itemStack) + ".tooltip", item == null ? "" : item.getDescription()));

        if (item != null && item.equals(ModBlocks.itemPairedCake.get())) {
            if (itemStack.hasTag() && itemStack.getTagElement("BlockEntityTag") != null) {
                CompoundTag tag = itemStack.getTagElement("BlockEntityTag");
                if (tag != null && tag.contains("pairedPos"))
                    tooltip.add(Component.translatable("tooltip." + DimensionCakes.MODID + ".linked").withStyle(ChatFormatting.BLUE));
            }
        }
    }

    private Item getRefillMaterial(ItemStack itemStack) {
        ResourceLocation location = ForgeRegistries.ITEMS.getKey(itemStack.getItem());

        if (location != null) {
            switch (location.toString()) {
                case "dimensioncakes:nether_cake" -> {
                    return ForgeRegistries.ITEMS.getValue(new ResourceLocation(DimensionCakesConfig.GENERAL.netherCakeRefill.get()));
                }
                case "dimensioncakes:end_cake" -> {
                    return ForgeRegistries.ITEMS.getValue(new ResourceLocation(DimensionCakesConfig.GENERAL.endCakeRefill.get()));
                }
                case "dimensioncakes:overworld_cake" -> {
                    return ForgeRegistries.ITEMS.getValue(new ResourceLocation(DimensionCakesConfig.GENERAL.overworldCakeRefill.get()));
                }
                case "dimensioncakes:paired_cake" -> {
                    return ForgeRegistries.ITEMS.getValue(new ResourceLocation(DimensionCakesConfig.GENERAL.pairedCakeRefill.get()));
                }
            }
        }
        return null;
    }
}
