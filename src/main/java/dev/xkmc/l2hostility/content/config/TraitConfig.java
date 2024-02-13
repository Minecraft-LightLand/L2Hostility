package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.BaseConfig;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

@SerialClass
public class TraitConfig extends BaseConfig {

	public static final TraitConfig DEFAULT = new TraitConfig(
			new ResourceLocation(L2Hostility.MODID, "default"),
			10, 100, 1, 10);

	@SerialClass.SerialField
	public int min_level, cost, max_rank, weight;

	@Deprecated
	public TraitConfig() {
	}

	public TraitConfig(ResourceLocation rl, int cost, int weight, int maxRank, int minLevel) {
		this.id = rl;
		this.cost = cost;
		this.weight = weight;
		this.max_rank = maxRank;
		this.min_level = minLevel;
		addBlacklist(e -> {
		});
		addWhitelist(e -> {
		});
	}

	public TagKey<EntityType<?>> getBlacklistTag() {
		assert id != null;
		String[] paths = id.getPath().split("/");
		String path = paths[paths.length - 1];
		ResourceLocation tag = new ResourceLocation(id.getNamespace(), path + "_blacklist");
		return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), tag);
	}

	public TagKey<EntityType<?>> getWhitelistTag() {
		assert id != null;
		String[] paths = id.getPath().split("/");
		String path = paths[paths.length - 1];
		ResourceLocation tag = new ResourceLocation(id.getNamespace(), path + "_whitelist");
		return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), tag);
	}

	public TraitConfig addWhitelist(Consumer<TagsProvider.TagAppender<EntityType<?>>> pvd) {
		var tag = getWhitelistTag();
		LHTagGen.ENTITY_TAG_BUILDER.put(tag.location(), e -> pvd.accept(e.tag(tag)));
		return this;
	}

	public TraitConfig addBlacklist(Consumer<TagsProvider.TagAppender<EntityType<?>>> pvd) {
		var tag = getBlacklistTag();
		LHTagGen.ENTITY_TAG_BUILDER.put(tag.location(), e -> pvd.accept(e.tag(tag)));
		return this;
	}

	public boolean allows(EntityType<?> type) {
		var blacklist = getBlacklistTag();
		var whitelist = getWhitelistTag();
		var manager = ForgeRegistries.ENTITY_TYPES.tags();
		assert manager != null;
		boolean def = true;
		if (!manager.getTag(whitelist).isEmpty()) {
			if (type.is(whitelist)) return true;
			def = false;
		}
		if (!manager.getTag(blacklist).isEmpty()) {
			if (type.is(blacklist)) return false;
			def = true;
		}
		return def;
	}

}
