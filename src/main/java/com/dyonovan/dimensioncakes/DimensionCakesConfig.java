package com.dyonovan.dimensioncakes;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DimensionCakesConfig {

    public static final ForgeConfigSpec genSpec;
    public static final General GENERAL;

    static {
        final Pair<General, ForgeConfigSpec> genSpecPair = new ForgeConfigSpec.Builder().configure(General::new);
        genSpec = genSpecPair.getRight();
        GENERAL = genSpecPair.getLeft();
    }

    public static class General {

        public final ForgeConfigSpec.ConfigValue<String> endCakeRefill;
        public final ForgeConfigSpec.ConfigValue<String> netherCakeRefill;
        public final ForgeConfigSpec.ConfigValue<String> overworldCakeRefill;
        public final ForgeConfigSpec.ConfigValue<String> pairedCakeRefill;

        public final ForgeConfigSpec.ConfigValue<Boolean> disableNetherPortal;
        public final ForgeConfigSpec.ConfigValue<Boolean> disableEndPortal;

        public final ForgeConfigSpec.ConfigValue<Boolean> devMode;

        private General(ForgeConfigSpec.Builder builder) {
            builder.push("Refill Materials");

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

            pairedCakeRefill = builder
                    .comment("Item that will refill the Paired Cake ie: minecraft:apple")
                    .translation("config." + DimensionCakes.MODID + ".pairedCakeRefill")
                    .define("pairedCakeRefill", "minecraft:ender_pearl");

            builder.pop();

            builder.push("Portals");

            disableNetherPortal = builder
                    .comment("Disable the creation of Nether Portals")
                    .translation("config." + DimensionCakes.MODID + ".disableNetherPortal")
                    .define("disableNetherPortal", true);

            disableEndPortal = builder
                    .comment("Disable the creation of End Portals")
                    .translation("config." + DimensionCakes.MODID + ".disableEndPortal")
                    .define("disableEndPortal", true);

            builder.pop();

            builder.push("Dev - Not Recommended to Change");

            devMode = builder
                    .comment("Enable Dev Mode")
                    .translation("config." + DimensionCakes.MODID + ".devMode")
                    .define("devMode", false);

            builder.pop();
        }
    }
}
