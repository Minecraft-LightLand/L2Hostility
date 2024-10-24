package dev.xkmc.l2hostility.init.registrate;

import dev.xkmc.l2core.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.AttReg;
import dev.xkmc.l2core.init.reg.simple.AttVal;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.menu.tab.DifficultyTab;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.chunk.LevelChunk;

import static dev.xkmc.l2hostility.init.L2Hostility.REGISTRATE;

public class LHMiscs {

	private static final AttReg ATT = AttReg.of(L2Hostility.REG);
	public static final AttVal.CapVal<LivingEntity, MobTraitCap> MOB = ATT.entity("mob",
			MobTraitCap.class, MobTraitCap::new, LivingEntity.class, e ->
					e.getType().is(LHTagGen.WHITELIST) || e instanceof Enemy && !e.getType().is(LHTagGen.BLACKLIST));
	public static final AttVal.PlayerVal<PlayerDifficulty> PLAYER = ATT.player("player",
			PlayerDifficulty.class, PlayerDifficulty::new, PlayerCapabilityNetworkHandler::new);
	public static final AttVal.CapVal<LevelChunk, ChunkDifficulty> CHUNK = ATT.entity("chunk",
			ChunkDifficulty.class, ChunkDifficulty::new, LevelChunk.class, e -> true);

	private static final ResourceLocation DUMMY = L2Tabs.loc(L2Hostility.MODID);
	public static final Val<TabToken<InvTabData, DifficultyTab>> TAB_DIFFICULTY = new Val.Registrate<>(
			L2Hostility.REGISTRATE.generic(L2Tabs.TABS, "difficulty", () ->
							L2Tabs.GROUP.registerTab(() -> DifficultyTab::new, LangData.INFO_TAB_TITLE.get()))
					.dataMap(L2Tabs.ORDER.reg(), 5000).dataMap(L2Tabs.ICON.reg(), Items.ZOMBIE_HEAD)
					.register());

	public static final SimpleEntry<Attribute> ADD_LEVEL = L2DamageTracker.reg(REGISTRATE,
			"extra_difficulty", e -> new RangedAttribute(e, 0, 0, 2000)
					.setSentiment(Attribute.Sentiment.NEGATIVE), "Extra Difficulty");

	public static void register() {

	}

}
