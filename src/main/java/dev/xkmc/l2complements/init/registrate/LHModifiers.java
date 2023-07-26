package dev.xkmc.l2complements.init.registrate;

import dev.xkmc.l2complements.content.modifiers.core.MobModifier;
import dev.xkmc.l2complements.init.L2Hostility;
import dev.xkmc.l2library.base.L2Registrate;

public class LHModifiers {

	public static final L2Registrate.RegistryInstance<MobModifier> MODIFIERS = L2Hostility.REGISTRATE.newRegistry("modifier", MobModifier.class);

	public static void register() {
	}

}
