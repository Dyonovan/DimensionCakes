package com.dyonovan.dimensioncakes.common.blocks;

import com.dyonovan.dimensioncakes.common.tiles.NetherCakeTileEntity;
import com.dyonovan.dimensioncakes.util.CustomTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class NetherCakeBlock extends BaseCakeBlock implements EntityBlock {

    public NetherCakeBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new NetherCakeTileEntity(pos, state);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (world.isClientSide || player.level.dimension() == Level.NETHER || state.getValue(BITES) == 6) return InteractionResult.SUCCESS;

        BlockState newState = state.setValue(BITES, state.getValue(BITES) + 1);
        world.setBlockAndUpdate(pos, newState);

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof NetherCakeTileEntity) {
            BlockPos teleportPos = ((NetherCakeTileEntity) entity).getTeleportPos();
            teleportPlayer(teleportPos, player, (NetherCakeTileEntity) entity);
        }
        return InteractionResult.SUCCESS;
    }


    private void teleportPlayer(BlockPos storedPos, Player player, NetherCakeTileEntity entity) {
        ServerLevel serverLevel = player.level.getServer().getLevel(Level.NETHER);

        if (serverLevel == null) return;

        if (storedPos == null) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(player.getX() / 8, player.getY(), player.getZ() / 8);
            BlockPos.MutableBlockPos spawnPosition = new BlockPos.MutableBlockPos(-1, -1, -1);

            int possibleYPosition;

            do {
                possibleYPosition = scanColumn(serverLevel, pos.getX(), pos.getZ(), pos.getY());
                if (possibleYPosition == -1) {
                    incrementColumn(pos, new BlockPos(player.getX(), player.getY(), player.getZ()));
                } else {
                    spawnPosition.set(pos.getX(), possibleYPosition, pos.getZ());
                }
            } while(spawnPosition.getY() == -1);

            entity.setTeleportPos(spawnPosition.immutable());
            entity.setChanged();
            storedPos = spawnPosition;
        }
        player.changeDimension(serverLevel, new CustomTeleporter(storedPos));
    }

    private int scanColumn(ServerLevel destWorld, int x, int z, int targetY) {
        int possibleY = -1;

        for(int currentY = 4; currentY < destWorld.getHeight(); currentY++) {
            BlockPos pos = new BlockPos(x, currentY, z);

            boolean isBlockBelowSolid = destWorld.getBlockState(pos.below()).isValidSpawn(destWorld, pos, EntityType.PLAYER);
            boolean isLegBlockAir = destWorld.getBlockState(pos).getBlock() == Blocks.AIR;
            boolean isChestBlockAir = destWorld.getBlockState(pos.above()).getBlock() == Blocks.AIR;

            if (isBlockBelowSolid && isLegBlockAir && isChestBlockAir) {
                if (possibleY == -1) {
                    possibleY = currentY;
                } else {
                    if (Math.abs(possibleY - targetY) > Math.abs(pos.getY() - targetY)) {
                        possibleY = currentY;
                    }
                }
            }
        }
        return possibleY;
    }

    private void incrementColumn(BlockPos.MutableBlockPos currentPos, BlockPos originalPos) {
        double tempPosIncX = originalPos.distToCenterSqr(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ());
        double tempPosIncY = originalPos.distToCenterSqr(currentPos.getX(), currentPos.getY(), currentPos.getZ());

        if (tempPosIncX > tempPosIncY) {
            currentPos.set(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1);
        } else {
            currentPos.set(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ());
        }
    }
}
