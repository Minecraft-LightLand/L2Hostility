package dev.xkmc.l2hostility.content.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.BiPredicate;

public class LHPlayerCommands extends HostilityCommands {

	protected static LiteralArgumentBuilder<CommandSourceStack> build() {
		return literal("player")
				.then(argument("player", EntityArgument.players())
						.then(difficulty())
						.then(trait())
						.then(dim())
				);
	}

	private static LiteralArgumentBuilder<CommandSourceStack> difficulty() {
		return literal("difficulty")
				.then(literal("base")
						.then(literal("set")
								.requires(e -> e.hasPermission(2))
								.then(argument("level", IntegerArgumentType.integer(0))
										.executes(playerLevel((cap, pl, level) ->
												cap.getLevelEditor(pl).setBase(level)))))
						.then(literal("add")
								.requires(e -> e.hasPermission(2))
								.then(argument("level", IntegerArgumentType.integer())
										.executes(playerLevel((cap, pl, level) ->
												cap.getLevelEditor(pl).addBase(level)))))
						.then(literal("get").executes(playerGet((cap, pl) ->
								LangData.COMMAND_PLAYER_GET_BASE.get(pl.getDisplayName(), cap.getLevelEditor(pl).getBase())))))
				.then(literal("total")
						.then(literal("set")
								.requires(e -> e.hasPermission(2))
								.then(argument("level", IntegerArgumentType.integer(0))
										.executes(playerLevel((cap, pl, level) ->
												cap.getLevelEditor(pl).setTotal(level)))))
						.then(literal("add")
								.requires(e -> e.hasPermission(2))
								.then(argument("level", IntegerArgumentType.integer())
										.executes(playerLevel((cap, pl, level) ->
												cap.getLevelEditor(pl).addTotal(level)))))
						.then(literal("get").executes(playerGet((cap, pl) ->
								LangData.COMMAND_PLAYER_GET_TOTAL.get(pl.getDisplayName(), cap.getLevelEditor(pl).getBase())))));
	}

	private static LiteralArgumentBuilder<CommandSourceStack> trait() {
		return literal("traitCap")
				.then(literal("set")
						.requires(e -> e.hasPermission(2))
						.then(argument("level", IntegerArgumentType.integer(0, TraitManager.getMaxLevel()))
								.executes(playerLevel((cap, pl, level) -> {
									cap.maxRankKilled = level;
									return true;
								}))))
				.then(literal("get").executes(playerGet((cap, pl) ->
						LangData.COMMAND_PLAYER_GET_TRAIT_CAP.get(pl.getDisplayName(), cap.maxRankKilled))));
	}

	private static LiteralArgumentBuilder<CommandSourceStack> dim() {
		return literal("dimensions")
				.then(literal("clear")
						.requires(e -> e.hasPermission(2))
						.executes(playerRun((cap, pl) -> {
							boolean ans = !cap.dimensions.isEmpty();
							cap.dimensions.clear();
							return ans;
						})))
				.then(literal("get").executes(playerGet((cap, pl) ->
						LangData.COMMAND_PLAYER_GET_DIM.get(pl.getDisplayName(), cap.dimensions.size()))));
	}

	private static Command<CommandSourceStack> playerRun(PlayerCommand cmd) {
		return ctx -> {
			EntitySelector sel = ctx.getArgument("player", EntitySelector.class);
			var list = sel.findPlayers(ctx.getSource());
			int count = iterate(list, cmd::run);
			printCompletion(ctx.getSource(), count);
			return 0;
		};
	}

	private static Command<CommandSourceStack> playerGet(PlayerGet cmd) {
		return ctx -> {
			EntitySelector sel = ctx.getArgument("player", EntitySelector.class);
			var list = sel.findPlayers(ctx.getSource());
			for (var e : list) {
				ctx.getSource().sendSystemMessage(cmd.run(LHMiscs.PLAYER.type().getOrCreate(e), e));
			}
			return 0;
		};
	}

	private static Command<CommandSourceStack> playerLevel(PlayerLevelCommand cmd) {
		return ctx -> {
			int level = ctx.getArgument("level", Integer.class);
			EntitySelector sel = ctx.getArgument("player", EntitySelector.class);
			var list = sel.findPlayers(ctx.getSource());
			int count = iterate(list, (cap, pl) -> cmd.run(cap, pl, level));
			printCompletion(ctx.getSource(), count);
			return 0;
		};
	}

	private static int iterate(List<ServerPlayer> list, BiPredicate<PlayerDifficulty, Player> task) {
		int count = 0;
		for (var e : list) {
			PlayerDifficulty cap = LHMiscs.PLAYER.type().getOrCreate(e);
			if (task.test(cap, e)) {
				cap.sync(e);
				count++;
			}
		}
		return count;
	}

	private static void printCompletion(CommandSourceStack ctx, int count) {
		if (count > 0) {
			ctx.sendSystemMessage(LangData.COMMAND_PLAYER_SUCCEED.get(count));
		} else {
			ctx.sendSystemMessage(LangData.COMMAND_PLAYER_FAIL.get().withStyle(ChatFormatting.RED));
		}
	}

	private interface PlayerCommand {


		boolean run(PlayerDifficulty player, Player pl);

	}

	private interface PlayerGet {

		Component run(PlayerDifficulty player, Player pl);

	}

	private interface PlayerLevelCommand {

		boolean run(PlayerDifficulty player, Player pl, int level);

	}

}
