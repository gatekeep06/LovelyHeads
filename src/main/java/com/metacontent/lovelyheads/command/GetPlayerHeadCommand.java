package com.metacontent.lovelyheads.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
                .then(CommandManager.argument("player", StringArgumentType.string())
                        .executes(context -> run(context.getSource().getPlayer(), context.getArgument("player", String.class)))));
    }

    /*private static int run(PlayerEntity user, PlayerEntity player) {
        if (user != null) {
            ItemStack head = Items.PLAYER_HEAD.getDefaultStack();
            NbtCompound nbt = new NbtCompound();
            nbt.putString("SkullOwner", player.getEntityName());
            head.setNbt(nbt);
            user.giveItemStack(head);
        }
        return 1;
    }*/

    private static int run(PlayerEntity user, String string) {
        if (user != null) {
            ItemStack head = Items.PLAYER_HEAD.getDefaultStack();
            NbtCompound nbt = new NbtCompound();
            nbt.putString("SkullOwner", string);
            head.setNbt(nbt);
            user.giveItemStack(head);
        }
        return 1;
    }
}
