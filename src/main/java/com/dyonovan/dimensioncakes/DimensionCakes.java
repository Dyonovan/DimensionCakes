package com.dyonovan.dimensioncakes;

import com.dyonovan.dimensioncakes.addons.theoneprobe.TOPPlugin;
import com.dyonovan.dimensioncakes.common.ModBlocks;
import com.dyonovan.dimensioncakes.common.capability.DimSpawnPos;
import com.dyonovan.dimensioncakes.common.capability.DimSpawnPosAttacher;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@SuppressWarnings("unused")
@Mod(DimensionCakes.MODID)
public class DimensionCakes {
    public static final String MODID = "dimensioncakes";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static CreativeModeTab creativeModeTab;

    public DimensionCakes() {
        ModBlocks.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(DimSpawnPos.class);
        MinecraftForge.EVENT_BUS.register(DimSpawnPosAttacher.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPPlugin::new);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}