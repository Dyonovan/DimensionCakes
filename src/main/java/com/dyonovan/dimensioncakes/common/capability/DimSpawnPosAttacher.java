package com.dyonovan.dimensioncakes.common.capability;

import com.dyonovan.dimensioncakes.DimensionCakes;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DimSpawnPosAttacher {

    private static class DimSpawnPosProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(DimensionCakes.MODID, "dimspawnpos");

        private final IDimSpawnPos backend = new DimSpawnPosImpl();
        private final LazyOptional<IDimSpawnPos> optionalData = LazyOptional.of(() -> backend);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return DimSpawnPos.INSTANCE.orEmpty(cap, this.optionalData);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    @SubscribeEvent
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            final DimSpawnPosProvider provider = new DimSpawnPosProvider();
            event.addCapability(DimSpawnPosProvider.IDENTIFIER, provider);
        }

    }

    public DimSpawnPosAttacher() {
    }
}
