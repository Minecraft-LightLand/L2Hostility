package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.Tags;

public class DementorTrait extends LegendaryTrait {

	public DementorTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onCreateSource(int level, LivingEntity attacker, CreateSourceEvent event) {
		if (event.getResult() == L2DamageTypes.MOB_ATTACK)
			event.enable(DefaultDamageState.BYPASS_ARMOR);
	}

	public double modifyBonusDamage(DamageSource source, double factor, int lv) {
		if (source.getMsgId().equals("mob") && source.is(DamageTypeTags.BYPASSES_ARMOR)) {
			return LHConfig.SERVER.dementorDamageFactor.get();
		}
		return 1;
	}

	@Override
	public void onDamaged(int level, LivingEntity entity, DamageData.Defence event) {
		if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) ||
				event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) ||
				event.getSource().is(Tags.DamageTypes.IS_MAGIC))
			return;
		double def = LHConfig.SERVER.dementorDamageReductionBase.get();
		event.addDealtModifier(DamageModifier.nonlinearPre(7436, val -> (float) (val < def ? val / def : Math.log(val) / Math.log(def)), getRegistryName()));
	}

}
