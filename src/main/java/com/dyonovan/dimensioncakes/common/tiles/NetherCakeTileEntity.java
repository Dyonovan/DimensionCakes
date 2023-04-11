package com.dyonovan.dimensioncakes.common.tiles;

import com.dyonovan.dimensioncakes.common.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NetherCakeTileEntity extends BlockEntity {

    private BlockPos teleportPos;

    public NetherCakeTileEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.tileNetherCake.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.get("teleportPos") != null) this.teleportPos = NbtUtils.readBlockPos((CompoundTag) tag.get("teleportPos"));

        super.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (teleportPos != null) tag.put("teleportPos", NbtUtils.writeBlockPos(teleportPos));
    }

    public BlockPos getTeleportPos() {
        return teleportPos;
    }

    public void setTeleportPos(BlockPos teleportPos) {
        this.teleportPos = teleportPos;
    }
}
