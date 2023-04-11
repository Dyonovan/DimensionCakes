package com.dyonovan.dimensioncakes.common;

import com.dyonovan.dimensioncakes.DimensionCakes;
import com.dyonovan.dimensioncakes.common.blocks.EndCakeBlock;
import com.dyonovan.dimensioncakes.common.blocks.NetherCakeBlock;
import com.dyonovan.dimensioncakes.common.blocks.OverworldCakeBlock;
import com.dyonovan.dimensioncakes.common.items.DCItemBlock;
import com.dyonovan.dimensioncakes.common.tiles.NetherCakeTileEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DimensionCakes.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DimensionCakes.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DimensionCakes.MODID);

    public static RegistryObject<NetherCakeBlock> blockNetherCake = BLOCKS.register("nether_cake", () ->
            new NetherCakeBlock(BlockBehaviour.Properties
                    .of(Material.CAKE)
                    .sound(SoundType.SAND)
                    .noOcclusion()
                    .strength(1.0f, 1.0f)));
    public static RegistryObject<DCItemBlock> itemNetherCake = fromBlock(blockNetherCake, new Item.Properties().tab(DCCreativeTab.INSTANCE));
    public static RegistryObject<BlockEntityType<NetherCakeTileEntity>> tileNetherCake =
            BLOCK_ENTITIES.register("nether_cake", () ->
                    BlockEntityType.Builder
                            .of(NetherCakeTileEntity::new, blockNetherCake.get())
                            .build(null));

    public static RegistryObject<EndCakeBlock> blockEndCake = BLOCKS.register("end_cake", () ->
            new EndCakeBlock(BlockBehaviour.Properties
                    .of(Material.CAKE)
                    .sound(SoundType.SAND)
                    .noOcclusion()
                    .strength(1.0f, 1.0f)));
    public static RegistryObject<DCItemBlock> itemEndCake = fromBlock(blockEndCake, new Item.Properties().tab(DCCreativeTab.INSTANCE));

    public static RegistryObject<OverworldCakeBlock> blockOverworkdCake = BLOCKS.register("overworld_cake", () ->
            new OverworldCakeBlock(BlockBehaviour.Properties
                    .of(Material.CAKE)
                    .sound(SoundType.SAND)
                    .noOcclusion()
                    .strength(1.0f, 1.0f)));
    public static RegistryObject<DCItemBlock> itemOverworldCake = fromBlock(blockOverworkdCake, new Item.Properties().tab(DCCreativeTab.INSTANCE));

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }

    private ModBlocks() {}

    private static <B extends Block>RegistryObject<DCItemBlock> fromBlock(RegistryObject<B> block, Item.Properties properties) {
        return ITEMS.register(block.getId().getPath(), () -> new DCItemBlock(block.get(), properties));
    }
}
