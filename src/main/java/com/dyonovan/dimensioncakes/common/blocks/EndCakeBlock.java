package com.dyonovan.dimensioncakes.common.blocks;

import com.dyonovan.dimensioncakes.common.capability.DimSpawnPos;
import com.dyonovan.dimensioncakes.common.capability.IDimSpawnPos;
import com.dyonovan.dimensioncakes.util.CustomTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EndCakeBlock extends Block {

    private final HashMap<String, LazyOptional<IDimSpawnPos>> cache = new HashMap<>();

    public EndCakeBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        if (player.level.dimension() != Level.END) {
            LazyOptional<IDimSpawnPos> targetCap = cache.get(Level.NETHER.location().toString());

            if (targetCap == null) {
                targetCap = player.getCapability(DimSpawnPos.INSTANCE, null);
                cache.put(Level.END.location().toString(), targetCap);
                targetCap.addListener(self -> cache.put(Level.END.location().toString(), null));
            }
            targetCap.ifPresent(iDimSpawnPos -> teleportPlayer(iDimSpawnPos, player));
        }
        return InteractionResult.SUCCESS;
    }

    private void teleportPlayer(IDimSpawnPos data, Player player) {
        BlockPos storedPos = data.getDimPos(Level.END.location().toString());

        if (storedPos == null) {
            data.setDimPos(Level.END.location().toString(), ServerLevel.END_SPAWN_POINT);
            storedPos = ServerLevel.END_SPAWN_POINT;
        }

        ServerLevel serverLevel = (ServerLevel)player.level;
        MinecraftServer minecraftServer = serverLevel.getServer();
        ServerLevel end = minecraftServer.getLevel(Level.END);

        if (end.getBlockState(storedPos.below()).getBlock() != Blocks.OBSIDIAN) {
            BlockPos.MutableBlockPos blockPos = storedPos.below().mutable();

            for(int i = -2; i <= 2; ++i) {
                for (int j = -2; j <= 2; ++j) {
                    for (int k = -1; k < 3; ++k) {
                        BlockState blockState = k == -1 ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
                        end.setBlockAndUpdate(blockPos.set(storedPos.below()).move(j, k, i), blockState);
                    }
                }
            }
        }

        player.changeDimension(end, new CustomTeleporter(storedPos));
    }
}
