package com.metacontent.lovelyheads.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class LovelyCommands {
    public static void registerLovelyCommands() {
        CommandRegistrationCallback.EVENT.register(GetPlayerHeadCommand::register);
    }
}