package dev.xkmc.l2hostility.init.data;

import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class DamageTypeGen extends DamageTypeAndTagsGen {

	public static final ResourceKey<DamageType> KILLER_AURA = create("killer_aura");

	public DamageTypeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper helper) {
		super(output, pvd, helper, L2Complements.MODID);
		new DamageTypeHolder(KILLER_AURA, new DamageType("killer_aura", DamageScaling.NEVER, 0.1f))
				.add(DamageTypeTags.BYPASSES_ARMOR, L2DamageTypes.MAGIC);
	}

	public static Holder<DamageType> forKey(Level level, ResourceKey<DamageType> key) {
		return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
	}

	private static ResourceKey<DamageType> create(String id) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(L2Complements.MODID, id));
	}

}
