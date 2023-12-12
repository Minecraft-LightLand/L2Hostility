package dev.xkmc.l2hostility.backport.damage;

import dev.xkmc.l2library.init.events.attack.AttackCache;

public class DamageModifier {

	public static void hurtMultTotal(AttackCache cache, float factor) {
		cache.setDamageModified(cache.getDamageModified() * factor);
	}

}
