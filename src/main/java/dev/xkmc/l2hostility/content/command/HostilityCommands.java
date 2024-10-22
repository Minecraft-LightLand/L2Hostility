package dev.xkmc.l2hostility.content.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = L2Hostility.MODID, bus = EventBusSubscriber.Bus.GAME)
public class HostilityCommands {

	@SubscribeEvent
	public static void register(RegisterCommandsEvent event) {
		event.getDispatcher().register(literal("hostility")
				.then(LHPlayerCommands.build())
				.then(LHRegionCommands.build())
				.then(LHMobCommands.build())
		);
	}

	protected static LiteralArgumentBuilder<CommandSourceStack> literal(String str) {
		return LiteralArgumentBuilder.literal(str);
	}

	protected static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

}
