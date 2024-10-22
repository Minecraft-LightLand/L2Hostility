package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2hostility.content.effect.AntiBuildEffect;
import dev.xkmc.l2hostility.content.effect.GravityEffect;
import dev.xkmc.l2hostility.content.effect.MoonwalkEffect;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class LHEffects {

	public static final SimpleEntry<MobEffect> GRAVITY = genEffect("gravity",
			() -> new GravityEffect(MobEffectCategory.NEUTRAL, 0x3f3f3f),
			"Increase entity gravity.");

	public static final SimpleEntry<MobEffect> MOONWALK = genEffect("moonwalk",
			() -> new MoonwalkEffect(MobEffectCategory.NEUTRAL, 0xcfcfcf),
			"Decrease entity gravity.");

	public static final SimpleEntry<MobEffect> ANTIBUILD = genEffect("antibuild",
			() -> new AntiBuildEffect(MobEffectCategory.NEUTRAL, 0xff7f7f),
			"Make player cannot place block.");

	private static <T extends MobEffect> SimpleEntry<MobEffect> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return new SimpleEntry<>(L2Hostility.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register());
	}

	public static void register() {

	}

}
