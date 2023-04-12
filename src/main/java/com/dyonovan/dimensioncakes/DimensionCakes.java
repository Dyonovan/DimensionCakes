package com.dyonovan.dimensioncakes;

import com.dyonovan.dimensioncakes.common.ModBlocks;
import com.dyonovan.dimensioncakes.compat.theoneprobe.TOPPlugin;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, DimensionCakesConfig.genSpec, "dimensioncakes-server.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPPlugin::new);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @SubscribeEvent
    public void cancelNetherPortalCreation(BlockEvent.PortalSpawnEvent event) {
        if (DimensionCakesConfig.GENERAL.disableNetherPortal.get()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void cancelEndPortalCreation(PlayerInteractEvent.RightClickBlock event) {
        if (DimensionCakesConfig.GENERAL.disableEndPortal.get() && !event.getLevel().isClientSide()) {
            ItemStack stack = event.getItemStack();
            Player player = event.getEntity();
            if (!stack.isEmpty() && stack.getItem().equals(Items.ENDER_EYE) && event.getLevel().getBlockState(event.getPos()).getBlock() == Blocks.END_PORTAL_FRAME) {
                event.setCanceled(true);
                player.sendSystemMessage(Component.translatable("msg." + DimensionCakes.MODID + ".endportal").withStyle(ChatFormatting.RED));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}