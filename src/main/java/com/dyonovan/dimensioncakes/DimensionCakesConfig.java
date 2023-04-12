package com.dyonovan.dimensioncakes;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DimensionCakesConfig {

    public static final ForgeConfigSpec spec;
    public static final General GENERAL;

    static {
        final Pair<General, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(General::new);
        spec = specPair.getRight();
        GENERAL = specPair.getLeft();
    }

    public static class General {

        public final ForgeConfigSpec.ConfigValue<Boolean> disableNetherPortal;

        public final ForgeConfigSpec.ConfigValue<String> endCakeRefill;
        public final ForgeConfigSpec.ConfigValue<String> netherCakeRefill;
        public final ForgeConfigSpec.ConfigValue<String> overworldCakeRefill;

        private General(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            disableNetherPortal = builder
                    .comment("Disable the creation of Nether Portals")
                    .translation("config." + DimensionCakes.MODID + ".disableNetherPortal")
                    .define("disableNetherPortal", true);

            endCakeRefill = builder
                    .comment("Item that will refill the End Cake ie: minecraft:apple")
                    .translation("config." + DimensionCakes.MODID + ".endCakeRefill")
                    .define("endCakeRefill", "minecraft:end_stone");

            netherCakeRefill = builder
                    .comment("Item that will refill the Nether Cake ie: minecraft:apple")
                    .translation("config." + DimensionCakes.MODID + ".netherCakeRefill")
                    .define("netherCakeRefill", "minecraft:netherrack");

            overworldCakeRefill = builder
                    .comment("Item that will refill the Overworld Cake ie: minecraft:apple")
                    .translation("config." + DimensionCakes.MODID + ".overworldCakeRefill")
                    .define("overworldCakeRefill", "minecraft:stone");

            builder.pop();
        }
    }
}
