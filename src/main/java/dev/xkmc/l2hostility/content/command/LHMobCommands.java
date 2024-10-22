package dev.xkmc.l2hostility.content.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiPredicate;

public class LHMobCommands extends HostilityCommands {

	protected static LiteralArgumentBuilder<CommandSourceStack> build() {
		return literal("mobs")
				.then(argument("targets", EntityArgument.entities())
						.then(level())
						.then(trait())
				);
	}

	private static LiteralArgumentBuilder<CommandSourceStack> trait() {
		return literal("trait")
				.then(literal("clear")
						.requires(e -> e.hasPermission(2))
						.executes(mobRun(LHMobCommands::commandClearTrait)))
				.then(literal("remove")
						.requires(e -> e.hasPermission(2))
						.then(argument("trait", ResourceKeyArgument.key(LHTraits.TRAITS.key()))
								.executes(mobTrait(LHMobCommands::commandRemoveTrait))))
				.then(literal("set")
						.requires(e -> e.hasPermission(2))
						.then(argument("trait", ResourceKeyArgument.key(LHTraits.TRAITS.key()))
								.then(argument("rank", IntegerArgumentType.integer(0))
										.executes(mobTraitRank(LHMobCommands::commandSetTrait)))));
	}

	private static LiteralArgumentBuilder<CommandSourceStack> level() {
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
										cap.reinit(mob, cap.getLevel() + level, false)))))
				.then(literal("set")
						.requires(e -> e.hasPermission(2))
						.then(argument("level", IntegerArgumentType.integer(0))
								.executes(mobLevel((mob, cap, level) ->
										commandSetLevel(cap, mob, level)))))
				.then(literal("add")
						.requires(e -> e.hasPermission(2))
						.then(argument("level", IntegerArgumentType.integer(0))
								.executes(mobLevel((mob, cap, level) ->
										commandSetLevel(cap, mob, cap.getLevel() + level)))));
	}

	private static boolean commandSetLevel(MobTraitCap cap, LivingEntity mob, int level) {
		cap.setLevel(mob, level);
		cap.syncToClient(mob);
		return true;
	}

	private static boolean commandClearTrait(LivingEntity le, MobTraitCap cap) {
		if (cap.traits.isEmpty()) return false;
		for (var e : cap.traits.keySet())
			cap.setTrait(e, 0);
		return true;
	}

	private static boolean commandRemoveTrait(LivingEntity le, MobTraitCap cap, MobTrait trait) {
		if (!cap.hasTrait(trait)) return false;
		cap.removeTrait(trait);
		return true;
	}

	private static boolean commandSetTrait(LivingEntity le, MobTraitCap cap, MobTrait trait, int rank) {
		if (!trait.allow(le)) return false;
		if (trait.getConfig().max_rank() < rank) return false;
		cap.setTrait(trait, rank);
		return true;
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

	private static Command<CommandSourceStack> mobTrait(MobTraitCommand cmd) {
		return ctx -> {
			var trait = resolveKey(ctx, "trait", LHTraits.TRAITS.key(), ERR_INVALID_NAME);
			var list = EntityArgument.getEntities(ctx, "targets");
			int count = iterate(list, (le, cap) -> cmd.run(le, cap, trait.value()));
			printCompletion(ctx.getSource(), count);
			return 0;
		};
	}

	private static Command<CommandSourceStack> mobTraitRank(MobTraitRankCommand cmd) {
		return ctx -> {
			int rank = ctx.getArgument("rank", Integer.class);
			var trait = resolveKey(ctx, "trait", LHTraits.TRAITS.key(), ERR_INVALID_NAME);
			var list = EntityArgument.getEntities(ctx, "targets");
			int count = iterate(list, (le, cap) -> cmd.run(le, cap, trait.value(), rank));
			printCompletion(ctx.getSource(), count);
			return 0;
		};
	}

	private static int iterate(Collection<? extends Entity> list, BiPredicate<LivingEntity, MobTraitCap> task) {
		int count = 0;
		for (var e : list) {
			if (!(e instanceof LivingEntity le)) continue;
			var opt = LHMiscs.MOB.type().getExisting(le);
			if (opt.isEmpty()) continue;
			var cap = opt.get();
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

	private interface MobTraitCommand {

		boolean run(LivingEntity mob, MobTraitCap player, MobTrait trait);

	}

	private interface MobTraitRankCommand {

		boolean run(LivingEntity mob, MobTraitCap player, MobTrait trait, int rank);

	}

	private static final DynamicCommandExceptionType ERR_INVALID_NAME =
			new DynamicCommandExceptionType(LangData.COMMAND_INVALID_TRAIT::get);


	private static <T> ResourceKey<T> getRegistryKey(
			CommandContext<CommandSourceStack> ctx, String name,
			ResourceKey<Registry<T>> reg, DynamicCommandExceptionType err
	) throws CommandSyntaxException {
		ResourceKey<?> ans = ctx.getArgument(name, ResourceKey.class);
		Optional<ResourceKey<T>> optional = ans.cast(reg);
		return optional.orElseThrow(() -> err.create(ans));
	}

	private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> ctx, ResourceKey<? extends Registry<T>> reg) {
		return ctx.getSource().getServer().registryAccess().registryOrThrow(reg);
	}

	private static <T> Holder.Reference<T> resolveKey(
			CommandContext<CommandSourceStack> ctx, String name,
			ResourceKey<Registry<T>> reg, DynamicCommandExceptionType err
	) throws CommandSyntaxException {
		ResourceKey<T> ans = getRegistryKey(ctx, name, reg, err);
		return getRegistry(ctx, reg).getHolder(ans).orElseThrow(() -> err.create(ans.location()));
	}

}
