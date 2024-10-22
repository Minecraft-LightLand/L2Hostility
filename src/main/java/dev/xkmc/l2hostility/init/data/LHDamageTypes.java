package dev.xkmc.l2hostility.init.data;

import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

public class LHDamageTypes extends DamageTypeAndTagsGen {

	public static final ResourceKey<DamageType> KILLER_AURA = create("killer_aura");

	public LHDamageTypes() {
		super(L2Hostility.REGISTRATE);
		new DamageTypeHolder(KILLER_AURA, new DamageType("killer_aura", DamageScaling.NEVER, 0.1f))
				.add(DamageTypeTags.BYPASSES_ARMOR, Tags.DamageTypes.IS_MAGIC);
	}

	public static Holder<DamageType> forKey(Level level, ResourceKey<DamageType> key) {
		return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
	}

	private static ResourceKey<DamageType> create(String id) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, L2Hostility.loc(id));
	}


}
