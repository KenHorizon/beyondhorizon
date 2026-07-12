package com.kenhorizon.beyondhorizon.server.command;

import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.level_system.LevelSystem;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class LevelSystemCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("roleclass")
                .requires((source) -> {
                    return source.hasPermission(2);
                }).then(Commands.literal("level").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("amount", IntegerArgumentType.integer()).executes(context -> {
                    return executeSetLevels(context.getSource(), EntityArgument.getPlayer(context, "target"), IntegerArgumentType.getInteger(context, "amount"));
                })))).then(Commands.literal("points").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("amount", IntegerArgumentType.integer()).executes(context -> {
                    return executeSetPoints(context.getSource(), EntityArgument.getPlayer(context, "target"), IntegerArgumentType.getInteger(context, "amount"));
                })))).then(Commands.literal("reset").then(Commands.argument("target", EntityArgument.player()).executes(context -> {
                    return executeReset(context.getSource(), EntityArgument.getPlayer(context, "target"));
                })))
        );
    }

    private static int executeSetLevels(CommandSourceStack commandSource, ServerPlayer player, int amount) {
        LevelSystem role = Capabilities.levelSystem(player);
        if (role == null) {
            commandSource.sendFailure(Component.translatable(Tooltips.COMMAND_LEVEL_SET_FAILED, amount));
            return 0;
        } else {
            role.setLevel(amount);
            commandSource.sendSuccess(() -> Component.translatable(Tooltips.COMMAND_LEVEL_SET_SUCCESS, amount), true);
            return 1;
        }
    }
    private static int executeSetPoints(CommandSourceStack commandSource, ServerPlayer player, int amount) {
        LevelSystem role = Capabilities.levelSystem(player);
        if (role == null) {
            commandSource.sendFailure(Component.translatable(Tooltips.COMMAND_POINTS_FAILED, amount));
            return 0;
        } else {
            role.setPoints(amount);
            commandSource.sendSuccess(() -> Component.translatable(Tooltips.COMMAND_POINTS_SUCCESS, amount), true);
            return 1;
        }
    }
    private static int executeReset(CommandSourceStack commandSource, ServerPlayer player) {
        LevelSystem role = Capabilities.levelSystem(player);
        if (role == null) {
            commandSource.sendFailure(Component.translatable(Tooltips.COMMAND_RESET_FAILED));
            return 0;
        } else {
            role.resetEverything();
            commandSource.sendSuccess(() -> Component.translatable(Tooltips.COMMAND_RESET_SUCCESS), true);
            return 1;
        }
    }
}
