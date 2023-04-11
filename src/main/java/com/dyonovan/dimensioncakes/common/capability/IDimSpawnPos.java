package com.dyonovan.dimensioncakes.common.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IDimSpawnPos extends INBTSerializable<CompoundTag> {

    public BlockPos getDimPos(String dim);
    public void setDimPos(String dim, BlockPos pos);

}
