package dev.xkmc.l2hostility.compat.kubejs;

import dev.xkmc.l2damagetracker.compat.CustomAttackListener;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

public class L2HHelper {

	@Nullable
	public static MobTraitCap of(Entity e) {
		if (e instanceof Mob mob) {
			return LHMiscs.MOB.type().getExisting(mob).orElse(null);
		}
		return null;
	}

	public static CustomAttackListener newAttackListener() {
		return new CustomAttackListener();
	}

	public static boolean entityIs(Entity e, String id) {
		if (id.startsWith("#")) {
			return e.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(id.substring(1))));
		} else return e.getType() == BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(id));
	}

	public static boolean sourceIs(DamageSource source, String id) {
		if (id.startsWith("#")) {
			return source.is(TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(id.substring(1))));
		} else return source.typeHolder().unwrapKey().orElseThrow().location().toString().equals(id);
	}

}
