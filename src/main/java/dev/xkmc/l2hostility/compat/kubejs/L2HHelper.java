package dev.xkmc.l2hostility.compat.kubejs;

import dev.xkmc.l2damagetracker.compat.CustomAttackListener;
import dev.xkmc.l2damagetracker.compat.SingletonDamageTypeWrapper;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.HostilityDamageState;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class L2HHelper {

	@Nullable
	public static MobTraitCap of(Entity e) {
		if (e instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob)) {
			return MobTraitCap.HOLDER.get(mob);
		}
		return null;
	}

	public static CustomAttackListener newAttackListener() {
		return new CustomAttackListener();
	}

	public static boolean entityIs(Entity e, String id) {
		if (id.startsWith("#")) {
			return e.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(id.substring(1))));
		} else return e.getType() == ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(id));
	}

	public static boolean sourceIs(DamageSource source, String id) {
		if (id.startsWith("#")) {
			return source.is(TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(id.substring(1))));
		} else return source.typeHolder().unwrapKey().orElseThrow().location().toString().equals(id);
	}

	public static boolean sourceIs(CreateSourceEvent event, String id) {
		if (id.startsWith("#")) {
			return event.getRegistry().getHolderOrThrow(event.getOriginal())
					.is(TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(id.substring(1))));
		} else return event.getOriginal().location().toString().equals(id);
	}

	public static void enable(CreateSourceEvent event, String type) {
		DamageState state = switch (type) {
			case "bypass_armor" -> DefaultDamageState.BYPASS_ARMOR;
			case "bypass_magic" -> DefaultDamageState.BYPASS_MAGIC;
			case "bypass_cooldown" -> HostilityDamageState.BYPASS_COOLDOWN;
			default -> null;
		};
		if (state != null) {
			event.enable(state);
		}
	}

	public static void setTo(CreateSourceEvent event, String id) {
		event.setResult(new SingletonDamageTypeWrapper(
				ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(id))
		));
	}

}
