package dev.xkmc.l2hostility.content.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.function.BiPredicate;

public class LHMobCommands extends HostilityCommands {

	protected static LiteralArgumentBuilder<CommandSourceStack> build() {
		return literal("mobs")
				.then(argument("targets", EntityArgument.entities())
						.then(trait())
				);
	}

	private static LiteralArgumentBuilder<CommandSourceStack> trait() {
		return literal("level")
				.then(literal("rerollTrait")
						.requires(e -> e.hasPermission(2))
						.executes(mobRun((mob, cap) ->
								cap.reinit(mob, cap.getLevel(), false))))
				.then(literal("rerollTraitNoSuppression")
						.requires(e -> e.hasPermission(2))
						.executes(mobRun((mob, cap) ->
								cap.reinit(mob, cap.getLevel(), true))))
				.then(literal("setAndRerollTrait")
						.requires(e -> e.hasPermission(2))
						.then(argument("level", IntegerArgumentType.integer(0))
								.executes(mobLevel((mob, cap, level) ->
										cap.reinit(mob, level, false)))))
				.then(literal("addAndRerollTrait")
						.requires(e -> e.hasPermission(2))
						.then(argument("level", IntegerArgumentType.integer(0))
								.executes(mobLevel((mob, cap, level) ->
										cap.reinit(mob, cap.getLevel() + level, false)))));
	}

	private static Command<CommandSourceStack> mobRun(MobCommand cmd) {
		return ctx -> {
			var list = EntityArgument.getEntities(ctx, "targets");
			int count = iterate(list, cmd::run);
			printCompletion(ctx.getSource(), count);
			return 0;
		};
	}

	private static Command<CommandSourceStack> mobLevel(MobLevelCommand cmd) {
		return ctx -> {
			int level = ctx.getArgument("level", Integer.class);
			var list = EntityArgument.getEntities(ctx, "targets");
			int count = iterate(list, (le, cap) -> cmd.run(le, cap, level));
			printCompletion(ctx.getSource(), count);
			return 0;
		};
	}

	private static int iterate(Collection<? extends Entity> list, BiPredicate<LivingEntity, MobTraitCap> task) {
		int count = 0;
		for (var e : list) {
			if (!(e instanceof LivingEntity le) || !MobTraitCap.HOLDER.isProper(le)) continue;
			var cap = MobTraitCap.HOLDER.get(le);
			if (task.test(le, cap)) {
				count++;
			}
		}
		return count;
	}

	private static void printCompletion(CommandSourceStack ctx, int count) {
		if (count > 0) {
			ctx.sendSystemMessage(LangData.COMMAND_MOB_SUCCEED.get(count));
		} else {
			ctx.sendSystemMessage(LangData.COMMAND_PLAYER_FAIL.get().withStyle(ChatFormatting.RED));
		}
	}

	private interface MobCommand {


		boolean run(LivingEntity mob, MobTraitCap player);

	}

	private interface MobLevelCommand {

		boolean run(LivingEntity mob, MobTraitCap player, int level);

	}

}
