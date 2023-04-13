package com.dyonovan.dimensioncakes.common.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class PairedCakeList extends SavedData {

    public record UUIDInfo (UUID uuid, BlockPos blockPos, ResourceLocation dimension) {}

    private final ArrayList<UUIDInfo> pairedList = new ArrayList<>();

    public Pair<BlockPos, ResourceLocation> getPairedPos(UUID uuid, BlockPos originate) {
        if (pairedList.isEmpty()) return null;

        for (UUIDInfo record : pairedList) {
            if (record.uuid.equals(uuid)) {
                if (!record.blockPos.equals(originate)) {
                    return Pair.of(record.blockPos, record.dimension);
                }
            }
        }
        return null;
    }

    public void add(UUID uuid, BlockPos pos, ResourceLocation dimension) {
        UUIDInfo uuidInfo = new UUIDInfo(uuid, pos, dimension);
        pairedList.add(uuidInfo);
        this.setDirty();
    }

    public void removeRecord(UUID uuid, BlockPos pos, ResourceLocation dimension) {
        for (int i = 0; i < pairedList.size(); i++) {
            if (pairedList.get(i).uuid.equals(uuid)) {
                if (pairedList.get(i).blockPos.equals(pos)) {
                    if (pairedList.get(i).dimension.equals(dimension)) {
                        pairedList.remove(i);
                        this.setDirty();
                    }
                }
            }
        }
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        if (!pairedList.isEmpty()) {
            ListTag list = new ListTag();
            for (UUIDInfo info : pairedList) {
                CompoundTag ourData = new CompoundTag();
                ourData.putUUID("uuid", info.uuid);
                ourData.put("blockpos", NbtUtils.writeBlockPos(info.blockPos));
                ourData.putString("dimension", info.dimension.toString());
                list.add(ourData);
            }
            tag.put("list", list);
        }
        return tag;
    }

    public static PairedCakeList load(CompoundTag tag) {
        PairedCakeList pairedCakeList = create();
        if (tag.contains("list")) {
            ListTag list = tag.getList("list", Tag.TAG_COMPOUND);

            for (net.minecraft.nbt.Tag value : list) {
                CompoundTag data = (CompoundTag) value;
                pairedCakeList.add(data.getUUID("uuid"), NbtUtils.readBlockPos((CompoundTag) data.get("blockpos")), new ResourceLocation(data.getString("dimension")));
            }
        }
        return pairedCakeList;
    }

    public static PairedCakeList create() {
        return new PairedCakeList();
    }

    public static PairedCakeList manage(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(PairedCakeList::load, PairedCakeList::create, "pairedcakes");
    }
}
