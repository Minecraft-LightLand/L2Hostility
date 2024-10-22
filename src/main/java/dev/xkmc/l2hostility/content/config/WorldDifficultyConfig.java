package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2core.serial.config.BaseConfig;
import dev.xkmc.l2core.serial.config.CollectType;
import dev.xkmc.l2core.serial.config.ConfigCollect;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.apache.commons.lang3.mutable.MutableBoolean;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

@SerialClass
public class WorldDifficultyConfig extends BaseConfig {

	public static DifficultyConfig defaultLevel() {
		int base = LHConfig.SERVER.defaultLevelBase.get();
		double var = LHConfig.SERVER.defaultLevelVar.get();
		double scale = LHConfig.SERVER.defaultLevelScale.get();
		return new DifficultyConfig(0, base, var, scale, 1, 1);
	}

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialField
	public final HashMap<ResourceLocation, DifficultyConfig> levelMap = new HashMap<>();

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialField
	public final HashMap<ResourceLocation, DifficultyConfig> biomeMap = new HashMap<>();

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public final HashMap<ResourceLocation, ArrayList<EntityConfig.Config>> levelDefaultTraits = new HashMap<>();

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public final HashMap<ResourceLocation, ArrayList<EntityConfig.Config>> structureDefaultTraits = new HashMap<>();

	@Nullable
	public EntityConfig.Config get(ServerLevel level, BlockPos pos, EntityType<?> type) {
		if (!LHConfig.SERVER.enableEntitySpecificDatapack.get())
			return null;
		if (!LHConfig.SERVER.enableStructureSpecificDatapack.get())
			return null;
		if (structureDefaultTraits.isEmpty())
			return null;
		var manager = level.structureManager();
		var map = manager.getAllStructuresAt(pos);
		EntityConfig.Config def = null;
		for (var ent : map.entrySet()) {
			var structure = ent.getKey();
			var key = level.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
			var list = structureDefaultTraits.get(key);
			if (list == null) continue;
			MutableBoolean ans = new MutableBoolean(false);
			manager.fillStartsForStructure(structure, ent.getValue(), (e) -> {
				if (ans.isFalse() && manager.structureHasPieceAt(pos, e)) {
					ans.setTrue();
				}
			});
			if (ans.isFalse()) continue;
			for (var e : list) {
				if (e.entities.contains(type)) {
					return e;
				}
				if (e.entities.isEmpty()) {
					def = e;
				}
			}
		}
		return def;
	}

	@Nullable
	public EntityConfig.Config get(ResourceLocation level, EntityType<?> type) {
		if (!LHConfig.SERVER.enableEntitySpecificDatapack.get())
			return null;
		var list = levelDefaultTraits.get(level);
		if (list == null) return null;
		EntityConfig.Config def = null;
		for (var e : list) {
			if (e.entities.contains(type)) {
				return e;
			}
			if (e.entities.isEmpty()) {
				def = e;
			}
		}
		return def;
	}

	public record DifficultyConfig(int min, int base, double variation, double scale, double apply_chance,
								   double trait_chance) {

	}

	public WorldDifficultyConfig putDim(ResourceKey<Level> key, int min, int base, double var, double scale) {
		levelMap.put(key.location(), new DifficultyConfig(min, base, var, scale, 1, 1));
		return this;
	}

	@SafeVarargs
	public final WorldDifficultyConfig putBiome(int min, int base, double var, double scale, ResourceKey<Biome>... keys) {
		for (var key : keys) {
			biomeMap.put(key.location(), new DifficultyConfig(min, base, var, scale, 1, 1));
		}
		return this;
	}

	public WorldDifficultyConfig putLevelDef(ResourceKey<Level> id, EntityConfig.Config config) {
		levelDefaultTraits.computeIfAbsent(id.location(), l -> new ArrayList<>()).add(config);
		return this;
	}

	public WorldDifficultyConfig putStructureDef(ResourceKey<Structure> id, EntityConfig.Config config) {
		structureDefaultTraits.computeIfAbsent(id.location(), l -> new ArrayList<>()).add(config);
		return this;
	}

}
