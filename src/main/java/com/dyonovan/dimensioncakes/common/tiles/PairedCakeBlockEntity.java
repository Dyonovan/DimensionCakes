package com.dyonovan.dimensioncakes.common.tiles;

import com.dyonovan.dimensioncakes.common.ModBlocks;
import com.dyonovan.dimensioncakes.common.data.PairedCakeList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PairedCakeBlockEntity extends BlockEntity {

    private UUID uuid;
    private ResourceLocation dimension;

    public PairedCakeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.tilePairedCake.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("uuid")) {
            setUuid(tag.getUUID("uuid"));

            if (!tag.contains("dimension") && this.level != null) {
                setDimension(this.level.dimension().location());

                if (this.level.getServer() != null) {
                    PairedCakeList list = PairedCakeList.manage(this.level.getServer());
                    list.add(tag.getUUID("uuid"), this.getBlockPos(), this.level.dimension().location());
                }
            } else {
                setDimension(new ResourceLocation(tag.getString("dimension")));
            }
        }
        super.load(tag);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (getUuid() != null) {
            tag.putUUID("uuid", getUuid());
            tag.putString("dimension", getDimension().toString());
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
