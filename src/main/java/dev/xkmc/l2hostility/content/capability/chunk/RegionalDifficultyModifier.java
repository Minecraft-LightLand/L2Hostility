package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import net.minecraft.core.BlockPos;

public interface RegionalDifficultyModifier {

	void modifyInstance(BlockPos pos, MobDifficultyCollector instance);

}
