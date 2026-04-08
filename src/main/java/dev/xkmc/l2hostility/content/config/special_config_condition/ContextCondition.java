package dev.xkmc.l2hostility.content.config.special_config_condition;

import dev.xkmc.l2hostility.content.config.SpecialConfigCondition;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

@SerialClass
public class ContextCondition extends SpecialConfigCondition<EntityContext> {
	@SerialClass.SerialField
	public String dimension;

	@SerialClass.SerialField
	public ContextTag[] nbt;

	private ResourceLocation dimensionId;

	public ResourceLocation getDimensionId() {
		if (dimensionId == null) {
			dimensionId = new ResourceLocation(dimension);
		}
		return dimensionId;
	}

	public ContextCondition() {
		super(EntityContext.class);
	}

	@Override
	public boolean test(EntityContext context) {
		return matchLocation(context) && matchNbt(context);
	}

	private boolean matchLocation(EntityContext context) {
		if (dimension == null || context.le().level().isClientSide()) {
			return true;
		}

		return this.getDimensionId().equals(context.le().level().dimension().location());
	}

	private boolean matchNbt(EntityContext context) {
		if (nbt == null || nbt.length == 0) {
			return true;
		}
		var tag = context.le().serializeNBT();

		for (var i : nbt) {
			if (tag.contains(i.key) && !tag.get(i.key).getAsString().equals(i.value)) {
				return false;
			}
		}
		return true;
	}

}
