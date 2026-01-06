package com.kenhorizon.beyondhorizon.server.command;

import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Locale;

public class RoleClassCommand {
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
                }))).then(Commands.literal("roles").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("role", StringArgumentType.word()).suggests((context, builder) -> {
                    for (RoleClass roleClass : BHRegistries.ROLE_CLASS_KEY.get().getValues()) {
                        builder.suggest(roleClass.getName().toLowerCase(Locale.ROOT));
                    }
                    return builder.buildFuture();
                }).executes(context -> {
                    return executeSetRoles(context.getSource(), EntityArgument.getPlayer(context, "target"), context.getArgument("role", RoleClass.class));
                }))))
        );
    }

    private static int executeSetRoles(CommandSourceStack commandSource, ServerPlayer player, RoleClass roleClass) {
        RoleClass role = CapabilityCaller.roleClass(player);
        if (role == null) {
            commandSource.sendFailure(Component.translatable(Tooltips.COMMAND_ROLE_SET_FAILED, roleClass.getName()));
            return 0;
        } else {
            role.setRoles(roleClass);
            commandSource.sendSuccess(() -> Component.translatable(Tooltips.COMMAND_ROLE_SET_SUCCESS, roleClass.getName()), true);
            return 1;
        }
    }

    private static int executeSetLevels(CommandSourceStack commandSource, ServerPlayer player, int amount) {
        RoleClass role = CapabilityCaller.roleClass(player);
        if (role == null) {
            commandSource.sendFailure(Component.translatable(Tooltips.COMMAND_ROLE_SET_FAILED, amount));
            return 0;
        } else {
            role.setLevel(amount);
            commandSource.sendSuccess(() -> Component.translatable(Tooltips.COMMAND_ROLE_SET_SUCCESS, amount), true);
            return 1;
        }
    }
    private static int executeSetPoints(CommandSourceStack commandSource, ServerPlayer player, int amount) {
        RoleClass role = CapabilityCaller.roleClass(player);
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
        RoleClass role = CapabilityCaller.roleClass(player);
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
