package com.metacontent.lovelyheads.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class GetPlayerHeadCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("getHead")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(context -> run(context.getSource().getPlayer(), context.getArgument("name", String.class))))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> run(context.getSource().getPlayer(), context.getArgument("player", EntitySelector.class).getPlayer(context.getSource()).getEntityName()))));
    }

    private static int run(PlayerEntity user, String player) {
        if (user != null) {
            ItemStack head = Items.PLAYER_HEAD.getDefaultStack();
            NbtCompound nbt = new NbtCompound();
            nbt.putString("SkullOwner", player);
            head.setNbt(nbt);
            user.giveItemStack(head);
        }
        return 1;
    }
}
