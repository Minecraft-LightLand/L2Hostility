package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TraitEffectCache {

	public final LivingEntity target;

	@Nullable
	private List<GenericItemStack<CurseCurioItem>> curios;
	@Nullable
	private List<Mob> reflectedTargets;

	public TraitEffectCache(LivingEntity target) {
		this.target = target;
	}

	public boolean reflectTrait(MobTrait trait) {
		for (var e : buildCurio()) {
			if (e.item().reflectTrait(trait)) {
				return true;
			}
		}
		return false;
	}

	private List<GenericItemStack<CurseCurioItem>> buildCurio() {
		if (curios == null)
			curios = CurseCurioItem.getFromPlayer(target);
		return curios;
	}

	public List<Mob> getTargets() {
		if (reflectedTargets == null) {
			reflectedTargets = new ArrayList<>();
			int radius = LHConfig.COMMON.ringOfReflectionRadius.get();
			for (var e : target.level().getEntities(target, target.getBoundingBox().inflate(radius))) {
				if (!(e instanceof Mob mob)) continue;
				if (!MobTraitCap.HOLDER.isProper(mob)) continue;
				if (mob.distanceTo(target) > radius) continue;
				reflectedTargets.add(mob);
			}
		}
		return reflectedTargets;
	}

}
