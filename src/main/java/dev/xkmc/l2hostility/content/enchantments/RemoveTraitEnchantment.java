package dev.xkmc.l2hostility.content.enchantments;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;

import java.util.function.Supplier;

public class RemoveTraitEnchantment extends HostilityEnchantment implements HitTargetEnchantment {

	private final Holder<MobTrait> sup;

	public RemoveTraitEnchantment(Holder<MobTrait> sup) {
		this.sup = sup;
	}

	@Override
	public void hitMob(LivingEntity target, MobTraitCap cap, int value, DamageData.Offence cache) {
		cap.removeTrait(sup.value());
		cap.syncToClient(target);
		if (this == LHEnchantments.SPLIT_SUPPRESS.legacy().value() && target instanceof Slime slime) {
			slime.addTag("SuppressSplit");
		}
	}

}
