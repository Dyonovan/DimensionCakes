package com.dyonovan.dimensioncakes.common.blocks;

import com.dyonovan.dimensioncakes.DimensionCakesConfig;
import com.dyonovan.dimensioncakes.util.CustomTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class OverworldCakeBlock extends BaseCakeBlock {

    public OverworldCakeBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.sidedSuccess(false);

        String repairItem = DimensionCakesConfig.GENERAL.overworldCakeRefill.get();
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

        if (state.getValue(BITES) < 6) {
            if (!player.isCreative()) {
                BlockState newState = state.setValue(BITES, state.getValue(BITES) + 1);
                world.setBlockAndUpdate(pos, newState);
            }

            if (((ServerPlayer) player).getRespawnPosition() != null) {
                ResourceKey<Level> dim = ((ServerPlayer) player).getRespawnDimension();
                BlockPos spawnPos = ((ServerPlayer) player).getRespawnPosition();

                player.changeDimension(player.level.getServer().getLevel(dim), new CustomTeleporter(spawnPos));
            } else {
                ServerLevel overworld = player.level.getServer().getLevel(Level.OVERWORLD);

                BlockPos spawnPos = overworld.getSharedSpawnPos();
                player.changeDimension(overworld, new CustomTeleporter(spawnPos));
            }
        }
        return InteractionResult.SUCCESS;
    }
}
