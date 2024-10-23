package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;

public record ChunkCapHolder(LevelChunk chunk, ChunkDifficulty cap) implements RegionalDifficultyModifier {

	public SectionDifficulty getSection(int y) {
		cap.check(chunk);
		int index = (y >> 4) - chunk.getMinSection();
		index = Mth.clamp(index, 0, cap.sections.length - 1);
		return cap.sections[index];
	}

	public void modifyInstance(BlockPos pos, MobDifficultyCollector instance) {
		cap.check(chunk);
		getSection(pos.getY()).modifyInstance(chunk.getLevel(), pos, instance);
	}

	public void addKillHistory(Player player, LivingEntity mob, MobTraitCap cap) {
		cap().check(chunk);
		BlockPos pos = mob.blockPosition();
		int index = -chunk.getMinSection() + (pos.getY() >> 4);
		if (index >= 0 && index < cap().sections.length) {
			cap().sections[index].addKillHistory(this, player, mob, cap);
		}
	}

}
