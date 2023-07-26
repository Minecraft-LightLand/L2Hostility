package dev.xkmc.l2complements.init.registrate;


import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2complements.init.L2Hostility;
import net.minecraft.world.effect.MobEffect;

public class LHEffects {

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return L2Hostility.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {
	}

}

