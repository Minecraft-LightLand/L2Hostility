package dev.xkmc.l2hostility.backport.config;

import dev.xkmc.l2library.serial.config.ConfigMerger;
import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.serial.network.PacketHandlerWithConfig;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Map;

public record ConfigTypeEntry<T extends BaseConfig>(PacketHandlerWithConfig channel, String path, String name,
													Class<T> cls) {

	public ConfigTypeEntry(PacketHandlerWithConfig channel, String path, String name, Class<T> cls) {
		this.channel = channel;
		this.name = name;
		this.cls = cls;
		this.path = path;
		channel.addCachedConfig(name, new ConfigMerger<>(cls));
	}

	public String asPath(ResourceLocation rl) {
		return "data/" + rl.getNamespace() + "/" + path + "/" + name + "/" + rl.getPath();
	}

	public T getMerged() {
		return channel.getCachedConfig(name);
	}

	public Collection<T> getAll() {
		return channel.getConfigs(name).map(e -> (T) e.getValue()).toList();
	}

	public T getEntry(ResourceLocation id) {
		ResourceLocation full = new ResourceLocation(id.getNamespace(), name + "/" + id.getPath());
		return Wrappers.cast(channel.configs.get(full.toString()));
	}

	public void add(Map<String, BaseConfig> e, ResourceLocation rl, T entry) {
		e.put(rl.getNamespace() + "/" + path + "/" + name + "/" + rl.getPath(), entry);
	}
}
