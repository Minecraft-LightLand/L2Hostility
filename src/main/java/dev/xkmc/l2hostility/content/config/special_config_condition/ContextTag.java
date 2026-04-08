package dev.xkmc.l2hostility.content.config.special_config_condition;

import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class ContextTag {
	@SerialClass.SerialField
	public String key;
	@SerialClass.SerialField
	public String value;
}
