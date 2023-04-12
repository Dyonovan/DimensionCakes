package com.dyonovan.dimensioncakes.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class BaseCakeBlock extends Block {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);

    public BaseCakeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.getStateDefinition().any().setValue(BITES, 0)
        );
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return switch (blockState.getValue(BITES)) {
            case 1 -> Block.box(3, 0, 1, 15, 8, 15);
            case 2 -> Block.box(5, 0, 1, 15, 8, 15);
            case 3 -> Block.box(7, 0, 1, 15, 8, 15);
            case 4 -> Block.box(9, 0, 1, 15, 8, 15);
            case 5 -> Block.box(11, 0, 1, 15, 8, 15);
            case 6 -> Block.box(13, 0, 1, 15, 8, 15);
            default -> Block.box(1, 0, 1, 15, 8, 15);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }
}
