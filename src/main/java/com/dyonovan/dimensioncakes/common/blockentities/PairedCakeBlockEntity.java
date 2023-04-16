package com.dyonovan.dimensioncakes.common.blockentities;

import com.dyonovan.dimensioncakes.common.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PairedCakeBlockEntity extends BlockEntity {

    private BlockPos pairedPos;
    private ResourceLocation pairedDimension;

    public PairedCakeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.tilePairedCake.get(), pos, state);
    }

    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains("pairedPos")) {
            setPairedPos(NbtUtils.readBlockPos(tag.getCompound("pairedPos")));
            setPairedDimension(new ResourceLocation(tag.getString("pairedDimension")));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (getPairedPos() != null) {
            tag.put("pairedPos", NbtUtils.writeBlockPos(getPairedPos()));
            tag.putString("pairedDimension", getPairedDimension().toString());
        }
    }

    public BlockPos getPairedPos() {
        return pairedPos;
    }

    public void setPairedPos(BlockPos pairedPos) {
        this.pairedPos = pairedPos;
    }

    public ResourceLocation getPairedDimension() {
        return pairedDimension;
    }

    public void setPairedDimension(ResourceLocation pairedDimension) {
        this.pairedDimension = pairedDimension;
    }
}
