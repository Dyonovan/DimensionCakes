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
        return Block.box(1, 0, 1, 15, 8, 15);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }
}
