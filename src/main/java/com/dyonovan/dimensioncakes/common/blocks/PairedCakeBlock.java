package com.dyonovan.dimensioncakes.common.blocks;

import com.dyonovan.dimensioncakes.DimensionCakes;
import com.dyonovan.dimensioncakes.DimensionCakesConfig;
import com.dyonovan.dimensioncakes.common.ModBlocks;
import com.dyonovan.dimensioncakes.common.data.PairedCakeList;
import com.dyonovan.dimensioncakes.common.tiles.PairedCakeBlockEntity;
import com.dyonovan.dimensioncakes.util.CustomTeleporter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }

        PairedCakeBlockEntity blockEntity = (PairedCakeBlockEntity) level.getBlockEntity(pos);

        if (level.getServer() != null) {
            PairedCakeList list = PairedCakeList.manage(level.getServer());
            Pair<BlockPos, ResourceLocation> destination = list.getPairedPos(blockEntity.getUuid(), pos);

            if (destination != null) {
                if (state.getValue(BITES) < 6) {
                    if (!player.isCreative()) {
                        BlockState newState = state.setValue(BITES, state.getValue(BITES) + 1);
                        level.setBlockAndUpdate(pos, newState);
                    }

                    ResourceKey<Level> levelResourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, destination.getRight());
                    player.changeDimension(level.getServer().getLevel(levelResourceKey), new CustomTeleporter(destination.getLeft()));
                }
            } else {
                player.sendSystemMessage(Component.translatable("msg." + DimensionCakes.MODID + ".pairedcake.nopair").withStyle(ChatFormatting.RED));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PairedCakeBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state1, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state2, boolean bool) {

        if (!state2.getBlock().equals(ModBlocks.blockPairedCake.get())) {

            if (level.getServer() != null) {
                PairedCakeList list = PairedCakeList.manage(level.getServer());

                if (level.getBlockEntity(pos) instanceof PairedCakeBlockEntity blockEntity) {
                    list.removeRecord(blockEntity.getUuid(), pos, blockEntity.getDimension());
                }
            }
        }
        super.onRemove(state1, level, pos, state2, bool);
    }
}
