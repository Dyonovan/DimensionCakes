package com.dyonovan.dimensioncakes.common.blocks;

import com.dyonovan.dimensioncakes.DimensionCakes;
import com.dyonovan.dimensioncakes.DimensionCakesConfig;
import com.dyonovan.dimensioncakes.common.ModBlocks;
import com.dyonovan.dimensioncakes.common.blockentities.PairedCakeBlockEntity;
import com.dyonovan.dimensioncakes.util.CustomTeleporter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PairedCakeBlock extends BaseCakeBlock implements EntityBlock {

    public PairedCakeBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState initialState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState finishedState, boolean bool) {
        if (!level.isClientSide) {
            if (!(finishedState.getBlock().equals(initialState.getBlock()))) {
                PairedCakeBlockEntity myBlockEntity = (PairedCakeBlockEntity) level.getBlockEntity(blockPos);
                if (myBlockEntity != null) {
                    ResourceKey<Level> levelResourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, myBlockEntity.getPairedDimension());
                    if (level.getServer() != null) {
                        ServerLevel destLevel = level.getServer().getLevel(levelResourceKey);
                        if (destLevel != null) {
                            PairedCakeBlockEntity destBlockEntity = (PairedCakeBlockEntity) destLevel.getBlockEntity(myBlockEntity.getPairedPos());
                            if (destBlockEntity != null) {
                                destBlockEntity.setPairedDimension(null);
                                destBlockEntity.setPairedPos(null);
                            }
                        }
                    }
                }
            }
        }
        super.onRemove(initialState, level, blockPos, finishedState, bool);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable LivingEntity entity, @NotNull ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, entity, itemStack);

        if (!level.isClientSide && itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTagElement("BlockEntityTag");
            if (tag != null && tag.contains("pairedPos")) {
                ResourceKey<Level> levelResourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString("pairedDimension")));
                if (level.getServer() != null) {
                    ServerLevel serverLevel = level.getServer().getLevel(levelResourceKey);
                    if (serverLevel != null && serverLevel.getBlockEntity(NbtUtils.readBlockPos(tag.getCompound("pairedPos"))) instanceof PairedCakeBlockEntity destEntity) {
                        destEntity.setPairedPos(blockPos);
                        destEntity.setPairedDimension(level.dimension().location());
                    } else {
                        if (entity != null & entity instanceof Player) {
                            entity.sendSystemMessage(Component.translatable("msg." + DimensionCakes.MODID + ".pairedcake.lostdestination"));
                        }
                        PairedCakeBlockEntity myEntity = (PairedCakeBlockEntity) level.getBlockEntity(blockPos);
                        if (myEntity != null) {
                            myEntity.setPairedPos(null);
                            myEntity.setPairedDimension(null);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        if (level.isClientSide) return InteractionResult.CONSUME;

        if (player.getItemInHand(hand).getItem().equals(ModBlocks.itemPairedCake.get())) {
            PairedCakeBlockEntity be = (PairedCakeBlockEntity) level.getBlockEntity(pos);

            if (be != null && be.getLevel() != null) {
                CompoundTag innerTag = new CompoundTag();
                innerTag.put("pairedPos", NbtUtils.writeBlockPos(be.getBlockPos()));
                innerTag.putString("pairedDimension", be.getLevel().dimension().location().toString());

                ItemStack itemStack = player.getItemInHand(hand);
                CompoundTag stackTag = new CompoundTag();
                stackTag.put("BlockEntityTag", innerTag);
                itemStack.setTag(stackTag);
                player.sendSystemMessage(Component.translatable("tooltip." + DimensionCakes.MODID + ".linked"));
            }
            return InteractionResult.SUCCESS;
        }

        String repairItem = DimensionCakesConfig.GENERAL.pairedCakeRefill.get();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(repairItem));

        if (player.getItemInHand(hand).getItem().equals(item)) {
            if (state.getValue(BITES) != 0) {
                BlockState newState = state.setValue(BITES, state.getValue(BITES) - 1);
                level.setBlockAndUpdate(pos, newState);

                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }

        PairedCakeBlockEntity myEntity = (PairedCakeBlockEntity) level.getBlockEntity(pos);
        if (myEntity != null && myEntity.getPairedPos() != null) {
            ResourceKey<Level> levelResourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, myEntity.getPairedDimension());
            if (level.getServer() != null) {
                ServerLevel destLevel = level.getServer().getLevel(levelResourceKey);
                if (destLevel != null) {
                    PairedCakeBlockEntity pairedEntity = (PairedCakeBlockEntity) destLevel.getBlockEntity(myEntity.getPairedPos());
                    if (pairedEntity != null) {
                        if (state.getValue(BITES) < 6) {
                            if (!player.isCreative()) {
                                BlockState newState = state.setValue(BITES, state.getValue(BITES) + 1);
                                level.setBlockAndUpdate(pos, newState);
                            }
                            player.changeDimension(destLevel, new CustomTeleporter(pairedEntity.getBlockPos()));
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        player.sendSystemMessage(Component.translatable("msg." + DimensionCakes.MODID + ".pairedcake.nopair").withStyle(ChatFormatting.RED));
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PairedCakeBlockEntity(pos, state);
    }
}
