package com.dyonovan.dimensioncakes.common.blocks;

import com.dyonovan.dimensioncakes.DimensionCakes;
import com.dyonovan.dimensioncakes.DimensionCakesConfig;
import com.dyonovan.dimensioncakes.common.data.PairedCakeList;
import com.dyonovan.dimensioncakes.common.tiles.PairedCakeTileEntity;
import com.dyonovan.dimensioncakes.util.CustomTeleporter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PairedCakeBlock extends BaseCakeBlock implements EntityBlock {

    public PairedCakeBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        if (level.isClientSide) return InteractionResult.CONSUME;

        String repairItem = DimensionCakesConfig.GENERAL.pairedCakeRefill.get();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(repairItem));

        if (player.getItemInHand(hand).getItem().equals(item)) {
            if (state.getValue(BITES) != 0) {
                BlockState newState = state.setValue(BITES, state.getValue(BITES) - 1);
                level.setBlockAndUpdate(pos, newState);

                if (!player.hasPermissions(4)) {
                    player.getItemInHand(hand).shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }

        PairedCakeTileEntity tile = (PairedCakeTileEntity) level.getBlockEntity(pos);

        PairedCakeList list = PairedCakeList.manage(level.getServer());
        Pair<BlockPos, ResourceLocation> destination = list.getPairedPos(tile.getUuid(), pos);

        if (destination != null) {
            if (state.getValue(BITES) < 6) {
                BlockState newState = state.setValue(BITES, state.getValue(BITES) + 1);
                level.setBlockAndUpdate(pos, newState);

                ResourceKey<Level> levelResourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, destination.getRight());
                player.changeDimension(level.getServer().getLevel(levelResourceKey), new CustomTeleporter(destination.getLeft()));
            }
        } else {
            player.sendSystemMessage(Component.translatable("msg." + DimensionCakes.MODID + ".pairedcake.nopair").withStyle(ChatFormatting.RED));
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PairedCakeTileEntity(pos, state);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        if (level.isClientSide) return;

        UUID uuid = stack.getTag() != null ? stack.getTag().getUUID("uuid") : null;

        if (uuid != null) {
            PairedCakeTileEntity tile = (PairedCakeTileEntity) level.getBlockEntity(pos);
            if (tile != null) {
                tile.setUuid(uuid);
                tile.setDimension(level.dimension().location());
                PairedCakeList list = PairedCakeList.manage(level.getServer());
                list.add(uuid, pos, level.dimension().location());
            }
        }
        super.setPlacedBy(level, pos, state, entity, stack);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state1, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state2, boolean bool) {

        if (state2.getBlock().equals(Blocks.AIR)) {
            PairedCakeList list = PairedCakeList.manage(level.getServer());
            if (level.getBlockEntity(pos) instanceof PairedCakeTileEntity tile) {
                list.removeRecord(tile.getUuid(), pos, level.dimension().location());
            }
        }
        super.onRemove(state1, level, pos, state2, bool);
    }
}
