package com.metacontent.lovelyheads.command;

import com.metacontent.lovelyheads.command.custom.GetPlayerHeadCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class LovelyCommands {
    public static void registerLovelyCommands() {
        CommandRegistrationCallback.EVENT.register(GetPlayerHeadCommand::register);
    }
}