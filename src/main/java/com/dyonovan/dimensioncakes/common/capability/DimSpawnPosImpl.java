package com.dyonovan.dimensioncakes.common.capability;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;

import java.util.ArrayList;

import static java.util.Objects.isNull;

public class DimSpawnPosImpl implements IDimSpawnPos {

    private ArrayList<Pair<String, BlockPos>> dimPos = new ArrayList<>();

    @Override
    public BlockPos getDimPos(String dim) {
        for (Pair<String, BlockPos> dimInfo : dimPos) {
            if (dimInfo.getFirst().equals(dim)) {
                return dimInfo.getSecond();
            }
        }
        return null;
    }

    @Override
    public void setDimPos(String dim, BlockPos pos) {
        dimPos.add(new Pair<>(dim, pos));
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Pair<String, BlockPos> pair : dimPos) {
            CompoundTag dimList = new CompoundTag();
            dimList.putString("dimension_name", pair.getFirst());
            dimList.put("blockPos", NbtUtils.writeBlockPos(pair.getSecond()));
            list.add(dimList);
        }
        tag.put("list", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        dimPos = new ArrayList<>();

        ListTag list = (ListTag) nbt.get("list");
        if (!isNull(list)) {
            for (int i = 0; i < list.size(); i++) {
                CompoundTag tag = list.getCompound(i);
                dimPos.add(new Pair<>(tag.getString("dimension_name"), NbtUtils.readBlockPos((CompoundTag) tag.get("blockPos"))));
            }
        }
    }
}

