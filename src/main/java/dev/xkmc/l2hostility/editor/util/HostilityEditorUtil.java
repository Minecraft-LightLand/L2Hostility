package dev.xkmc.l2hostility.editor.util;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.config.TraitExclusion;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.editor.base.EditorFile;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HostilityEditorUtil {

	public static final String PACK_FOLDER = "l2hostility_editor";

	static {
		EditorFile.saveRootOverride = () -> {
			String s = LHConfig.CLIENT.editorSavePath.get();
			return s == null || s.isBlank() ? null : Path.of(s.trim());
		};
	}

	public static List<EntityType<?>> listEntityTypes() {
		List<EntityType<?>> ans = new ArrayList<>(ForgeRegistries.ENTITY_TYPES.getValues());
		ans.sort(EditorUtil.byId(e -> ForgeRegistries.ENTITY_TYPES.getKey(e).toString()));
		return ans;
	}

	public static List<MobTrait> listTraits() {
		List<MobTrait> ans = new ArrayList<>(LHTraits.TRAITS.get().getValues());
		ans.sort(EditorUtil.byId(e -> e.getRegistryName().toString()));
		return ans;
	}

	public static List<ResourceLocation> listTraitIds() {
		List<ResourceLocation> ans = new ArrayList<>(LHTraits.TRAITS.get().getKeys());
		ans.sort(ResourceLocation::compareTo);
		return ans;
	}

	public static Component traitIdName(ResourceLocation id) {
		MobTrait trait = LHTraits.TRAITS.get().getValue(id);
		return trait != null ? trait.getDesc() : Component.literal(id.toString());
	}

	@Nullable
	public static ItemStack traitIdIcon(ResourceLocation id) {
		MobTrait trait = LHTraits.TRAITS.get().getValue(id);
		return trait == null ? null : traitIcon(trait);
	}

	public static List<Enchantment> listEnchantments() {
		List<Enchantment> ans = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
		ans.sort(EditorUtil.byId(e -> ForgeRegistries.ENCHANTMENTS.getKey(e).toString()));
		return ans;
	}

	public static List<ResourceKey<Biome>> listBiomes() {
		var level = Minecraft.getInstance().level;
		if (level == null) return List.of();
		Registry<Biome> reg = level.registryAccess().registryOrThrow(Registries.BIOME);
		List<ResourceKey<Biome>> ans = new ArrayList<>(reg.registryKeySet());
		ans.sort((a, b) -> a.location().toString().compareToIgnoreCase(b.location().toString()));
		return ans;
	}

	public static boolean hasStructureRegistry() {
		return Minecraft.getInstance().getSingleplayerServer() != null;
	}

	public static List<ResourceKey<Structure>> listStructures() {
		var server = Minecraft.getInstance().getSingleplayerServer();
		if (server == null) return List.of();
		Registry<Structure> reg = server.registryAccess().registryOrThrow(Registries.STRUCTURE);
		List<ResourceKey<Structure>> ans = new ArrayList<>(reg.registryKeySet());
		ans.sort((a, b) -> a.location().toString().compareToIgnoreCase(b.location().toString()));
		return ans;
	}

	public static Component biomeName(ResourceKey<Biome> key) {
		return Component.translatable("biome." + key.location().getNamespace() + "." + key.location().getPath());
	}

	public static Component structureName(ResourceKey<Structure> key) {
		return Component.literal(key.location().toString());
	}

	public static Component entityName(EntityType<?> type) {
		return Component.translatable(type.getDescriptionId());
	}

	public static Component traitName(MobTrait trait) {
		return trait.getDesc();
	}

	public static ItemStack traitIcon(MobTrait trait) {
		return new ItemStack(trait.asItem());
	}

	public static ItemStack entityIcon(EntityType<?> type) {
		SpawnEggItem egg = SpawnEggItem.byId(type);
		return egg == null ? ItemStack.EMPTY : new ItemStack(egg);
	}

	public static ItemStack enchantIcon() {
		return new ItemStack(Items.ENCHANTED_BOOK);
	}

	public static Component enchantName(Enchantment ench) {
		return Component.translatable(ench.getDescriptionId());
	}

	@Nullable
	public static Component validateFileId(String s) {
		ResourceLocation id = EditorFile.parseId(s);
		if (id == null) return EditorText.INVALID_ID.get(s);
		if (!EditorFile.validNamespace(id.getNamespace())) return EditorText.NAMESPACE_HINT.get();
		return null;
	}

	public static WorldDifficultyConfig newDifficulty() {
		return new WorldDifficultyConfig();
	}

	public static TraitConfig newTrait() {
		return new TraitConfig();
	}

	public static WeaponConfig newWeapon() {
		return new WeaponConfig();
	}

	public static EntityConfig newEntity() {
		return new EntityConfig();
	}

	public static Path saveDifficulty(ResourceLocation id, WorldDifficultyConfig config) throws IOException {
		return EditorUtil.save(L2Hostility.DIFFICULTY, id, config, PACK_FOLDER);
	}

	public static Path saveTrait(ResourceLocation id, TraitConfig config) throws IOException {
		return EditorUtil.save(L2Hostility.TRAIT, id, config, PACK_FOLDER);
	}

	public static Path saveWeapon(ResourceLocation id, WeaponConfig config) throws IOException {
		return EditorUtil.save(L2Hostility.WEAPON, id, config, PACK_FOLDER);
	}

	public static Path saveEntity(ResourceLocation id, EntityConfig config) throws IOException {
		return EditorUtil.save(L2Hostility.ENTITY, id, config, PACK_FOLDER);
	}

	public static TraitExclusion newTraitExclusion() {
		return new TraitExclusion();
	}

	public static Path saveTraitExclusion(ResourceLocation id, TraitExclusion config) throws IOException {
		return EditorUtil.save(L2Hostility.TRAIT_EXCLUSION, id, config, PACK_FOLDER);
	}

	/**
	 * Connected components of the undirected exclusion graph over all loaded trait_exclusion data.
	 * A cluster lists every trait involved in at least one relation (as carrier or target).
	 */
	public static List<List<ResourceLocation>> exclusionClusters() {
		Map<ResourceLocation, ResourceLocation> parent = buildExclusionGraph();
		Map<ResourceLocation, List<ResourceLocation>> groups = new LinkedHashMap<>();
		for (ResourceLocation id : parent.keySet()) {
			groups.computeIfAbsent(findRoot(parent, id), k -> new ArrayList<>()).add(id);
		}
		List<List<ResourceLocation>> ans = new ArrayList<>(groups.values());
		ans.forEach(l -> l.sort(ResourceLocation::compareTo));
		ans.sort((a, b) -> a.get(0).toString().compareToIgnoreCase(b.get(0).toString()));
		return ans;
	}

	/**
	 * Cluster of the loaded exclusion graph that contains {@code id}, or a singleton list when the
	 * trait takes part in no relation.
	 */
	public static List<ResourceLocation> groupOf(ResourceLocation id) {
		Map<ResourceLocation, ResourceLocation> parent = buildExclusionGraph();
		if (!parent.containsKey(id)) return List.of(id);
		ResourceLocation root = findRoot(parent, id);
		List<ResourceLocation> ans = new ArrayList<>();
		for (ResourceLocation x : parent.keySet()) {
			if (findRoot(parent, x).equals(root)) ans.add(x);
		}
		ans.sort(ResourceLocation::compareTo);
		return ans;
	}

	/**
	 * Ids of traits that take part in at least one exclusion relation (carrier or target).
	 */
	public static Set<ResourceLocation> activeExclusionIds() {
		return new LinkedHashSet<>(buildExclusionGraph().keySet());
	}

	private static Map<ResourceLocation, ResourceLocation> buildExclusionGraph() {
		Map<ResourceLocation, ResourceLocation> parent = new LinkedHashMap<>();
		for (var cfg : L2Hostility.TRAIT_EXCLUSION.getAll()) {
			ResourceLocation carrier = cfg.getID();
			if (carrier == null) continue;
			for (ResourceLocation target : cfg.getExcluded().keySet()) {
				ResourceLocation cr = findRoot(parent, carrier);
				ResourceLocation tr = findRoot(parent, target);
				if (!cr.equals(tr)) parent.put(cr, tr);
			}
		}
		return parent;
	}

	private static ResourceLocation findRoot(Map<ResourceLocation, ResourceLocation> parent, ResourceLocation x) {
		ResourceLocation p = parent.get(x);
		if (p == null) {
			parent.put(x, x);
			return x;
		}
		if (p.equals(x)) return x;
		parent.put(x, findRoot(parent, p));
		return parent.get(x);
	}

	public static <T> LinkedHashSet<T> writeThroughSet(ArrayList<T> list) {
		return new WriteThroughSet<>(list);
	}

	public static List<ResourceLocation> listManagedTags() {
		return List.of(
				new ResourceLocation(L2Hostility.MODID, "blacklist"),
				new ResourceLocation(L2Hostility.MODID, "whitelist"),
				new ResourceLocation(L2Hostility.MODID, "default_blacklist"),
				new ResourceLocation(L2Hostility.MODID, "default_whitelist"),
				new ResourceLocation(L2Hostility.MODID, "no_scaling"),
				new ResourceLocation(L2Hostility.MODID, "no_trait"),
				new ResourceLocation(L2Hostility.MODID, "semiboss"),
				new ResourceLocation(L2Hostility.MODID, "effect_immune"),
				new ResourceLocation(L2Hostility.MODID, "no_drop"),
				new ResourceLocation(L2Hostility.MODID, "hide_traits"),
				new ResourceLocation(L2Hostility.MODID, "hide_level"),
				new ResourceLocation(L2Hostility.MODID, "hide_title"),
				new ResourceLocation(L2Hostility.MODID, "armor_target"),
				new ResourceLocation(L2Hostility.MODID, "melee_weapon_target"),
				new ResourceLocation(L2Hostility.MODID, "ranged_weapon_target"),
				new ResourceLocation(L2Hostility.MODID, "hostility_spawner_blacklist"));
	}

	public static final class WriteThroughSet<T> extends LinkedHashSet<T> {

		private final ArrayList<T> target;

		public WriteThroughSet(ArrayList<T> target) {
			this.target = target;
			addAll(target);
		}

		private void sync() {
			target.clear();
			target.addAll(this);
		}

		@Override
		public boolean add(T t) {
			boolean ans = super.add(t);
			sync();
			return ans;
		}

		@Override
		public boolean remove(Object o) {
			boolean ans = super.remove(o);
			sync();
			return ans;
		}

		@Override
		public void clear() {
			super.clear();
			sync();
		}

	}

}
