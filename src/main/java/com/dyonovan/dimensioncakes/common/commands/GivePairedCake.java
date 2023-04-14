package com.dyonovan.dimensioncakes.common.commands;

import com.dyonovan.dimensioncakes.common.ModBlocks;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class GivePairedCake {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> giveCake =
                Commands.literal("give_cake")
                        .requires(commandSource -> commandSource.hasPermission(Commands.LEVEL_OWNERS))
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(commandContext -> givePlayerCake(commandContext, EntityArgument.getPlayers(commandContext, "players"))))
                        .executes(context -> givePlayerCake(context, null));

        dispatcher.register(giveCake);
    }

    static int givePlayerCake(CommandContext<CommandSourceStack> commandContext, @Nullable Collection<ServerPlayer> serverPlayers) {

        Player playerSender = commandContext.getSource().getPlayer();

        if (serverPlayers == null || serverPlayers.isEmpty()) {
            giveItem(playerSender);
        } else {
            for (ServerPlayer player : serverPlayers) {
                giveItem(player);
            }
        }
        return 1;
    }

    private static void giveItem(@NotNull Player player) {
        CompoundTag uuidTag = new CompoundTag();
        uuidTag.putUUID("uuid", UUID.randomUUID());

        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.put("BlockEntityTag", uuidTag);

        ItemStack stack = new ItemStack(ModBlocks.itemPairedCake.get(), 2);
        stack.setTag(blockEntityTag);
        ItemHandlerHelper.giveItemToPlayer(player, stack);
    }
}
