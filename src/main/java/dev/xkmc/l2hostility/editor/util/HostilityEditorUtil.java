package dev.xkmc.l2hostility.editor.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.TraitConfig;
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
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
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

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class HostilityEditorUtil {

	public static final String PACK_FOLDER = "l2hostility_editor";

	public static final String TRAIT_DATAMAP = "data/l2hostility/data_maps/l2hostility/trait/trait_data.json";

	private static final com.google.gson.Gson GSON = new com.google.gson.GsonBuilder()
			.setPrettyPrinting().disableHtmlEscaping().create();

	static {
		EditorFile.saveRootOverride = () -> {
			String s = LHConfig.CLIENT.editorSavePath.get();
			return s == null || s.isBlank() ? null : Path.of(s.trim());
		};
	}

	public static List<EntityType<?>> listEntityTypes() {
		List<EntityType<?>> ans = new ArrayList<>(BuiltInRegistries.ENTITY_TYPE.stream().toList());
		ans.sort(EditorUtil.byId(e -> BuiltInRegistries.ENTITY_TYPE.getKey(e).toString()));
		return ans;
	}

	public static List<MobTrait> listTraits() {
		List<MobTrait> ans = new ArrayList<>(LHTraits.TRAITS.get().stream().toList());
		ans.sort(EditorUtil.byId(e -> LHTraits.TRAITS.get().getKey(e).toString()));
		return ans;
	}

	public static List<Enchantment> listEnchantments() {
		Registry<Enchantment> reg = enchantRegistry();
		if (reg == null) return List.of();
		List<Enchantment> ans = new ArrayList<>(reg.stream().toList());
		ans.sort(EditorUtil.byId(e -> reg.getKey(e).toString()));
		return ans;
	}

	/**
	 * Enchantments moved to a dynamic registry in 1.21; resolve via the client's registry access.
	 */
	@Nullable
	private static Registry<Enchantment> enchantRegistry() {
		var level = Minecraft.getInstance().level;
		if (level == null) return null;
		return level.registryAccess().registry(Registries.ENCHANTMENT).orElse(null);
	}

	/**
	 * Registry key of an enchantment, or null when the client has no world or the enchantment is
	 * not in the registry.
	 */
	@Nullable
	public static ResourceLocation enchantKey(Enchantment ench) {
		Registry<Enchantment> reg = enchantRegistry();
		return reg == null ? null : reg.getKey(ench);
	}

	/**
	 * Enchantment for a registry key, or null when the client has no world or the key is unknown.
	 */
	@Nullable
	public static Enchantment enchantById(ResourceLocation id) {
		Registry<Enchantment> reg = enchantRegistry();
		return reg == null ? null : reg.get(id);
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
		return ench.description();
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
		return TraitConfig.DEFAULT;
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

	/**
	 * Current value of a trait's config from the trait data map, or null when the client has no
	 * world, the trait is unknown, or the trait has no datamap entry.
	 */
	@Nullable
	public static TraitConfig currentTraitConfig(ResourceLocation id) {
		var level = Minecraft.getInstance().level;
		if (level == null) return null;
		var registry = LHTraits.TRAITS.get();
		ResourceKey<MobTrait> key = ResourceKey.create(LHTraits.TRAITS.key(), id);
		Holder<MobTrait> holder = registry.getHolder(key).orElse(null);
		if (holder == null) return null;
		return LHTraits.DATA.get(level.registryAccess(), holder);
	}

	/**
	 * Writes a single trait's datamap entry into the editor datapack. The entry is merged into the
	 * {@code trait_data.json} datamap already present in the editor pack, so repeated edits of
	 * different traits accumulate in the same file.
	 */
	public static Path saveTrait(ResourceLocation id, TraitConfig config) throws IOException {
		Path root = EditorFile.configRoot();
		if (root == null) {
			throw new IOException("no active world");
		}
		Path pack = root.resolve(PACK_FOLDER);
		Files.createDirectories(pack);
		Path meta = pack.resolve("pack.mcmeta");
		if (!Files.exists(meta)) {
			String content = "{\n  \"pack\": {\n    \"description\": \"L2Hostility Editor\",\n    \"pack_format\": "
					+ EditorFile.PACK_FORMAT + "\n  }\n}";
			Files.writeString(meta, content, StandardCharsets.UTF_8);
		}
		Path file = pack.resolve(TRAIT_DATAMAP);
		Files.createDirectories(file.getParent());
		JsonObject values = new JsonObject();
		if (Files.exists(file)) {
			JsonElement old = GSON.fromJson(Files.readString(file, StandardCharsets.UTF_8), JsonElement.class);
			if (old != null && old.isJsonObject() && old.getAsJsonObject().has("values")
					&& old.getAsJsonObject().get("values").isJsonObject()) {
				values = old.getAsJsonObject().get("values").getAsJsonObject();
			}
		}
		DataResult<JsonElement> result = LHTraits.DATA.reg().codec().encodeStart(JsonOps.INSTANCE, config);
		values.add(id.toString(), result.getOrThrow());
		JsonObject obj = new JsonObject();
		obj.addProperty("replace", false);
		obj.add("values", values);
		Files.writeString(file, GSON.toJson(obj), StandardCharsets.UTF_8);
		return file;
	}

	public static Path saveWeapon(ResourceLocation id, WeaponConfig config) throws IOException {
		return EditorUtil.save(L2Hostility.WEAPON, id, config, PACK_FOLDER);
	}

	public static Path saveEntity(ResourceLocation id, EntityConfig config) throws IOException {
		return EditorUtil.save(L2Hostility.ENTITY, id, config, PACK_FOLDER);
	}

	public static <T> LinkedHashSet<T> writeThroughSet(ArrayList<T> list) {
		return new WriteThroughSet<>(list);
	}

	public static List<ResourceLocation> listManagedTags() {
		return List.of(
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "blacklist"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "whitelist"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "default_blacklist"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "default_whitelist"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "no_scaling"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "no_trait"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "semiboss"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "effect_immune"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "no_drop"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "hide_traits"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "hide_level"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "hide_title"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "armor_target"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "melee_weapon_target"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "ranged_weapon_target"),
				ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "hostility_spawner_blacklist"));
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
