package dev.xkmc.l2complements.content.capability.chunk;

import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class ChunkDifficulty {

	public enum Stage {
		PRE_INIT, INIT, ENTERED, SAFE;
	}

	public static Capability<ChunkDifficulty> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public final LevelChunk chunk;

	private Stage stage = Stage.PRE_INIT;
	private int difficulty;

	protected ChunkDifficulty(LevelChunk chunk) {
		this.chunk = chunk;
	}

	public void init() {
	}

	public static void register() {
	}

}
