package dev.xkmc.l2complements.content.capability.player;

import dev.xkmc.l2complements.content.logic.DifficultyInstance;
import dev.xkmc.l2complements.init.L2Hostility;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class PlayerDifficulty extends PlayerCapabilityTemplate<PlayerDifficulty> {

	public static final Capability<PlayerDifficulty> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final PlayerCapabilityHolder<PlayerDifficulty> HOLDER =
			new PlayerCapabilityHolder<>(new ResourceLocation(L2Hostility.MODID, "player"), CAPABILITY,
					PlayerDifficulty.class, PlayerDifficulty::new, PlayerCapabilityNetworkHandler::new);

	@SerialClass.SerialField
	private int difficulty = 0;

	public PlayerDifficulty() {
	}

	public static void register() {
	}

	public void onClone(boolean isWasDeath) {
		if (isWasDeath) {
			difficulty = 0;
		}
	}

	public void tick() {
	}

	public void apply(DifficultyInstance instance) {
		instance.acceptBonus(difficulty);
	}

}
