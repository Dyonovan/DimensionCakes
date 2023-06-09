package com.dyonovan.dimensioncakes.compat.theoneprobe;

import com.dyonovan.dimensioncakes.DimensionCakes;
import com.dyonovan.dimensioncakes.common.blockentities.PairedCakeBlockEntity;
import com.dyonovan.dimensioncakes.common.blocks.BaseCakeBlock;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class TOPPlugin implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(DimensionCakes.MODID, "top_info");

    @Override
    public ResourceLocation getID() {
        return PLUGIN_ID;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo info, Player player, Level level, BlockState blockState, IProbeHitData data) {
        if (level.getBlockState(data.getPos()).getBlock() instanceof BaseCakeBlock) {
            int bites = level.getBlockState(data.getPos()).getValue(BaseCakeBlock.BITES);

            final IProbeInfo bitesInfo = info.horizontal(info.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));

            bitesInfo.mcText(Component.translatable("tooltip." + DimensionCakes.MODID + ".bites_left"));
            info.progress(6 - bites, 6);

            BlockEntity entity = level.getBlockEntity(data.getPos());
            if (entity instanceof PairedCakeBlockEntity pair) {
                if (pair.getPairedPos() != null) {
                    final IProbeInfo linkedInfo = info.horizontal(info.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                    linkedInfo.mcText(Component.translatable("tooltip." + DimensionCakes.MODID + ".linked"));
                }
            }
        }
    }

    @Override
    public Void apply(ITheOneProbe iTheOneProbe) {
        iTheOneProbe.registerProvider(this);
        return null;
    }
}
