package dev.xkmc.l2hostility.init.registrate;

import dev.xkmc.l2hostility.content.effect.AntiBuildEffect;
import dev.xkmc.l2hostility.content.effect.GravityEffect;
import dev.xkmc.l2hostility.content.effect.MoonwalkEffect;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.repack.registrate.builders.NoConfigBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.ForgeRegistries;

public class LHEffects {

	public static final RegistryEntry<GravityEffect> GRAVITY = genEffect("gravity", () -> new GravityEffect(MobEffectCategory.NEUTRAL, 0x3f3f3f),
			"Increase entity gravity.");

	public static final RegistryEntry<MoonwalkEffect> MOONWALK = genEffect("moonwalk", () -> new MoonwalkEffect(MobEffectCategory.NEUTRAL, 0xcfcfcf),
			"Decrease entity gravity.");

	public static final RegistryEntry<AntiBuildEffect> ANTIBUILD = genEffect("antibuild", () -> new AntiBuildEffect(MobEffectCategory.NEUTRAL, 0xff7f7f),
			"Make player cannot place block.");

	public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		L2Hostility.REGISTRATE.addRawLang("effect." + L2Hostility.MODID + "." + name + ".description", desc);
		return L2Hostility.REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(L2Hostility.REGISTRATE,
						L2Hostility.REGISTRATE, name, cb, ForgeRegistries.Keys.MOB_EFFECTS, sup))
				.lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {

	}

}
