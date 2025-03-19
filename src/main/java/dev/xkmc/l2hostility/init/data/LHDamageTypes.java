package dev.xkmc.l2hostility.init.data;

import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;
import dev.xkmc.l2damagetracker.contents.damage.DamageWrapperTagProvider;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2weaponry.init.L2Weaponry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LHDamageTypes extends DamageTypeAndTagsGen {

	public static void register() {
		L2DamageTypes.PLAYER_ATTACK.add(HostilityDamageState.BYPASS_COOLDOWN);
		L2DamageTypes.MOB_ATTACK.add(HostilityDamageState.BYPASS_COOLDOWN);
		DamageTypeRoot.configureGeneration(
				Set.of(L2DamageTracker.MODID, L2Weaponry.MODID, L2Hostility.MODID),
				L2Hostility.MODID, LIST);
	}

	protected static final List<DamageTypeWrapper> LIST = new ArrayList<>();

	public static final ResourceKey<DamageType> KILLER_AURA = create("killer_aura");

	public LHDamageTypes(PackOutput output,
						 CompletableFuture<HolderLookup.Provider> pvd,
						 ExistingFileHelper files) {
		super(output, pvd, files, L2Hostility.MODID);
		new DamageTypeHolder(KILLER_AURA, new DamageType("killer_aura", DamageScaling.NEVER, 0.1f))
				.add(DamageTypeTags.BYPASSES_ARMOR, L2DamageTypes.MAGIC);
	}

	@Override
	protected void addDamageTypes(BootstapContext<DamageType> ctx) {
		super.addDamageTypes(ctx);
		DamageTypeRoot.generateAll();
		for (DamageTypeWrapper wrapper : LIST) {
			ctx.register(wrapper.type(), wrapper.getObject());
		}
	}

	@Override
	protected void addDamageTypeTags(DamageWrapperTagProvider pvd, HolderLookup.Provider lookup) {
		super.addDamageTypeTags(pvd, lookup);
		DamageTypeRoot.generateAll();
		for (DamageTypeWrapper wrapper : LIST) {
			wrapper.gen(pvd, lookup);
		}
	}

	public static Holder<DamageType> forKey(Level level, ResourceKey<DamageType> key) {
		return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
	}

	private static ResourceKey<DamageType> create(String id) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(L2Hostility.MODID, id));
	}


}
