package com.dyonovan.dimensioncakes.common.items;

import com.dyonovan.dimensioncakes.DimensionCakesConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DCItemBlock extends BlockItem {
    public DCItemBlock(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        Item item = getRefillMaterial(itemStack);

        tooltip.add(Component.translatable(this.getDescriptionId(itemStack) + ".tooltip", item == null ? "" : item.getDescription()));
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
            }
        }
        return null;
    }
}
