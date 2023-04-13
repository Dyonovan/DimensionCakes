package com.dyonovan.dimensioncakes.common.tiles;

import com.dyonovan.dimensioncakes.common.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class PairedCakeTileEntity extends BlockEntity {

    private UUID uuid;
    private ResourceLocation dimension;

    public PairedCakeTileEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.tilePairedCake.get(), pos, state);
    }



    @Override
    public void load(CompoundTag tag) {
        if (tag.hasUUID("uuid")) {
            setUuid(tag.getUUID("uuid"));
            setDimension(new ResourceLocation(tag.getString("dimension")));
        }
        super.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (uuid != null) {
            tag.putUUID("uuid", getUuid());
            tag.putString("dimension", dimension.toString());
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ResourceLocation getDimension() {
        return dimension;
    }

    public void setDimension(ResourceLocation dimension) {
        this.dimension = dimension;
    }
}
