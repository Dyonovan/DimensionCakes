package com.dyonovan.dimensioncakes.common.blocks;

import com.dyonovan.dimensioncakes.DimensionCakesConfig;
import com.dyonovan.dimensioncakes.util.CustomTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class EndCakeBlock extends BaseCakeBlock {

    public EndCakeBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (world.isClientSide || player.level.dimension() == Level.END)
            return InteractionResult.sidedSuccess(!world.isClientSide);

        String repairItem = DimensionCakesConfig.GENERAL.endCakeRefill.get();
        Item item =  ForgeRegistries.ITEMS.getValue(new ResourceLocation(repairItem));

        if (player.getItemInHand(hand).getItem().equals(item)) {
            if (state.getValue(BITES) != 0) {
                BlockState newState = state.setValue(BITES, state.getValue(BITES) - 1);
                world.setBlockAndUpdate(pos, newState);

                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (!player.isCreative()) {
            BlockState newState = state.setValue(BITES, state.getValue(BITES) + 1);
            world.setBlockAndUpdate(pos, newState);
        }

        teleportPlayer(player);

        return InteractionResult.SUCCESS;
    }

    private void teleportPlayer(Player player) {

        BlockPos teleportPos = ServerLevel.END_SPAWN_POINT;

        ServerLevel end = player.level.getServer().getLevel(Level.END);

        if (end.getBlockState(teleportPos.below()).getBlock() != Blocks.OBSIDIAN) {
            BlockPos.MutableBlockPos blockPos = teleportPos.below().mutable();

            for (int i = -2; i <= 2; ++i) {
                for (int j = -2; j <= 2; ++j) {
                    for (int k = -1; k < 3; ++k) {
                        BlockState blockState = k == -1 ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
                        end.setBlockAndUpdate(blockPos.set(teleportPos.below()).move(j, k, i), blockState);
                    }
                }
            }
        }
        player.changeDimension(end, new CustomTeleporter(teleportPos));
    }
}
