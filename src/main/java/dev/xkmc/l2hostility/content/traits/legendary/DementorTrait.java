package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

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
			return LHConfig.COMMON.dementorDamageFactor.get();
		}
		return 1;
	}

	@Override
	public void onDamaged(int level, LivingEntity entity, AttackCache cache) {
		var event = cache.getLivingDamageEvent();
		if (event == null) return;
		var source = event.getSource();
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ||
				source.is(DamageTypeTags.BYPASSES_EFFECTS) ||
				source.is(L2DamageTypes.MAGIC))
			return;
		double def = LHConfig.COMMON.dementorDamageReductionBase.get();
		cache.addDealtModifier(DamageModifier.nonlinearPre(7436, val -> val < def ? val : (float) (Math.log(val) / Math.log(def))));
	}

}
