package com.dyonovan.dimensioncakes.common.blocks;

import com.dyonovan.dimensioncakes.util.CustomTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class OverworldCakeBlock extends Block {

    public OverworldCakeBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        if (((ServerPlayer) player).getRespawnPosition() != null) {
             ResourceKey<Level> dim = ((ServerPlayer) player).getRespawnDimension();
             BlockPos spawnPos = ((ServerPlayer) player).getRespawnPosition();

             player.changeDimension(((ServerLevel) player.level).getServer().getLevel(dim), new CustomTeleporter(spawnPos));
        } else {
            ServerLevel serverLevel = (ServerLevel) player.level;
            MinecraftServer minecraftServer = serverLevel.getServer();
            ServerLevel overworld = minecraftServer.getLevel(Level.OVERWORLD);

            BlockPos spawnPos = overworld.getSharedSpawnPos();
            player.changeDimension(overworld, new CustomTeleporter(spawnPos));
        }

        return InteractionResult.SUCCESS;
    }
}
