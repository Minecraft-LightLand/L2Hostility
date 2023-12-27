package dev.xkmc.l2hostility.backport.config;

import dev.xkmc.l2library.serial.config.ConfigMerger;
import dev.xkmc.l2library.serial.network.BaseConfig;

import java.util.Map;
import java.util.stream.Stream;

public class AdvMerger<T extends BaseConfig> extends ConfigMerger<T> {
	public AdvMerger(Class<T> cls) {
		super(cls);
	}

	@Override
	public T merge(Stream<Map.Entry<String, BaseConfig>> s) throws Exception {
		T ans = super.merge(s);
		if (ans instanceof MergableConfig c) {
			c.postMerge();
		}
		return ans;
	}
}
