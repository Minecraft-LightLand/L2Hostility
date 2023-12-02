package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2hostility.content.effect.AntiBuildEffect;
import dev.xkmc.l2hostility.content.effect.GravityEffect;
import dev.xkmc.l2hostility.content.effect.MoonwalkEffect;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class LHEffects {

	public static final RegistryEntry<GravityEffect> GRAVITY = genEffect("gravity", () -> new GravityEffect(MobEffectCategory.NEUTRAL, 0x3f3f3f),
			"Increase entity gravity.");

	public static final RegistryEntry<MoonwalkEffect> MOONWALK = genEffect("moonwalk", () -> new MoonwalkEffect(MobEffectCategory.NEUTRAL, 0xcfcfcf),
			"Decrease entity gravity.");

	public static final RegistryEntry<AntiBuildEffect> ANTIBUILD = genEffect("antibuild", () -> new AntiBuildEffect(MobEffectCategory.NEUTRAL, 0xff7f7f),
			"Make player cannot place block.");

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return L2Hostility.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {

	}

}
