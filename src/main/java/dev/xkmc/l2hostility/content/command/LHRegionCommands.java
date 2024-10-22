package dev.xkmc.l2hostility.content.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.SectionDifficulty;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.function.Predicate;

import static net.minecraft.commands.arguments.coordinates.BlockPosArgument.ERROR_NOT_LOADED;

public class LHRegionCommands extends HostilityCommands {

	protected static LiteralArgumentBuilder<CommandSourceStack> build() {
		return literal("region")
				.then(section())
				.then(area());
	}

	private static LiteralArgumentBuilder<CommandSourceStack> section() {
		return literal("section")
				.then(argument("pos", BlockPosArgument.blockPos())
						.then(literal("set_base")
								.requires(e -> e.hasPermission(2))
								.then(argument("level", IntegerArgumentType.integer(0))
										.executes(sectionLevel((section, level, pos, lv) ->
												section.getLevelEditor(level, pos).setBase(lv)))))
						.then(literal("add_base")
								.requires(e -> e.hasPermission(2))
								.then(argument("level", IntegerArgumentType.integer())
										.executes(sectionLevel((section, level, pos, lv) ->
												section.getLevelEditor(level, pos).addBase(lv)))))
						.then(literal("set_clear")
								.requires(e -> e.hasPermission(2))
								.executes(sectionRun((section, level, pos) ->
										section.setClear(ChunkDifficulty.at(level, pos).get(), pos))))
						.then(literal("set_unclear")
								.requires(e -> e.hasPermission(2))
								.executes(sectionRun((section, level, pos) ->
										section.setUnclear(ChunkDifficulty.at(level, pos).get(), pos))))
						.then(literal("get_base").executes(sectionGet((section, level, pos) ->
								LangData.COMMAND_REGION_GET_BASE.get(section.getLevelEditor(level, pos).getBase()))))
						.then(literal("get_total").executes(sectionGet((section, level, pos) ->
								LangData.COMMAND_REGION_GET_TOTAL.get(section.getLevelEditor(level, pos).getTotal()))))
						.then(literal("get_scale").executes(sectionGet((section, level, pos) ->
								LangData.COMMAND_REGION_GET_SCALE.get(section.getScale(level, pos)))))
						.then(literal("is_clear").executes(sectionGet((section, level, pos) ->
								section.isCleared() ? LangData.COMMAND_REGION_CLEAR.get() :
										LangData.COMMAND_REGION_NOT_CLEAR.get()))
						)
				);
	}

	private static LiteralArgumentBuilder<CommandSourceStack> area() {
		return literal("area")
				.requires(e -> e.hasPermission(2))
				.then(argument("from", BlockPosArgument.blockPos())
						.then(argument("to", BlockPosArgument.blockPos())
								.then(literal("set_base")
										.then(argument("level", IntegerArgumentType.integer(0))
												.executes(areaLevel((section, level, pos, lv) ->
														section.getLevelEditor(level, pos).setBase(lv)))))
								.then(literal("add_base")
										.then(argument("level", IntegerArgumentType.integer())
												.executes(areaLevel((section, level, pos, lv) ->
														section.getLevelEditor(level, pos).addBase(lv)))))
								.then(literal("set_clear")
										.executes(areaRun((section, level, pos) ->
												section.setClear(ChunkDifficulty.at(level, pos).get(), pos))))
								.then(literal("set_unclear")
										.executes(areaRun((section, level, pos) ->
												section.setUnclear(ChunkDifficulty.at(level, pos).get(), pos))))
						)
				);
	}

	private static Command<CommandSourceStack> sectionLevel(SectionLevelCommand cmd) {
		return ctx -> {
			int level = ctx.getArgument("level", Integer.class);
			BlockPos sel = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
			var e = SectionDifficulty.sectionAt(ctx.getSource().getLevel(), sel);
			if (e.isEmpty()) throw ERROR_NOT_LOADED.create();
			if (!LHConfig.SERVER.allowSectionDifficulty.get()) {
				ctx.getSource().sendSystemMessage(LangData.COMMAND_REGION_LOCAL_OFF.get().withStyle(ChatFormatting.RED));
				return 1;
			}
			cmd.run(e.get(), ctx.getSource().getLevel(), sel, level);
			ctx.getSource().sendSystemMessage(LangData.COMMAND_REGION_SUCCEED.get());
			return 0;
		};
	}

	private static Command<CommandSourceStack> sectionRun(SectionCommand cmd) {
		return ctx -> {
			BlockPos sel = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
			var e = SectionDifficulty.sectionAt(ctx.getSource().getLevel(), sel);
			if (e.isEmpty()) throw ERROR_NOT_LOADED.create();
			cmd.run(e.get(), ctx.getSource().getLevel(), sel);
			ctx.getSource().sendSystemMessage(LangData.COMMAND_REGION_SUCCEED.get());
			return 0;
		};
	}


	private static Command<CommandSourceStack> sectionGet(SectionGet cmd) {
		return ctx -> {
			BlockPos sel = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
			var e = SectionDifficulty.sectionAt(ctx.getSource().getLevel(), sel);
			if (e.isEmpty()) throw ERROR_NOT_LOADED.create();
			ctx.getSource().sendSystemMessage(cmd.run(e.get(), ctx.getSource().getLevel(), sel));
			return 0;
		};
	}

	private static Command<CommandSourceStack> areaLevel(SectionLevelCommand cmd) {
		return ctx -> {
			int level = ctx.getArgument("level", Integer.class);
			BlockPos from = BlockPosArgument.getLoadedBlockPos(ctx, "from");
			BlockPos to = BlockPosArgument.getLoadedBlockPos(ctx, "to");
			if (!LHConfig.SERVER.allowSectionDifficulty.get()) {
				ctx.getSource().sendSystemMessage(LangData.COMMAND_REGION_LOCAL_OFF.get().withStyle(ChatFormatting.RED));
				return 1;
			}
			int count = iterate(from, to, sel -> {
				var e = SectionDifficulty.sectionAt(ctx.getSource().getLevel(), sel);
				if (e.isEmpty()) return false;
				return cmd.run(e.get(), ctx.getSource().getLevel(), sel, level);
			});
			ctx.getSource().sendSystemMessage(LangData.COMMAND_REGION_COUNT.get(count));
			return 0;
		};
	}

	private static Command<CommandSourceStack> areaRun(SectionCommand cmd) {
		return ctx -> {
			BlockPos from = BlockPosArgument.getLoadedBlockPos(ctx, "from");
			BlockPos to = BlockPosArgument.getLoadedBlockPos(ctx, "to");
			int count = iterate(from, to, sel -> {
				var e = SectionDifficulty.sectionAt(ctx.getSource().getLevel(), sel);
				if (e.isEmpty()) return false;
				return cmd.run(e.get(), ctx.getSource().getLevel(), sel);
			});
			ctx.getSource().sendSystemMessage(LangData.COMMAND_REGION_COUNT.get(count));
			return 0;
		};
	}

	private static int iterate(BlockPos from, BlockPos to, Predicate<BlockPos> pred) {
		AABB aabb = AABB.encapsulatingFullBlocks(from, to);
		int x0 = ((int) aabb.minX) & -15;
		int y0 = ((int) aabb.minY) & -15;
		int z0 = ((int) aabb.minZ) & -15;
		int x1 = ((int) aabb.maxX) & -15;
		int y1 = ((int) aabb.maxY) & -15;
		int z1 = ((int) aabb.maxZ) & -15;
		int count = 0;
		for (int x = x0; x <= x1; x += 16) {
			for (int y = y0; y <= y1; y += 16) {
				for (int z = z0; z <= z1; z += 16) {
					if (pred.test(new BlockPos(x, y, z))) {
						count++;
					}
				}
			}
		}
		return count;
	}

	private interface SectionCommand {


		boolean run(SectionDifficulty section, Level level, BlockPos pos);

	}

	private interface SectionGet {

		Component run(SectionDifficulty section, Level level, BlockPos pos);

	}

	private interface SectionLevelCommand {

		boolean run(SectionDifficulty section, Level level, BlockPos pos, int lv);

	}

}
