package com.dyonovan.dimensioncakes.common.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DimSpawnPos {

    public static final Capability<IDimSpawnPos> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IDimSpawnPos.class);
    }

    private DimSpawnPos() {

    }
}
