package dev.xkmc.l2hostility.content.item.consumable;

import dev.xkmc.l2complements.content.item.misc.FireChargeItem;
import dev.xkmc.l2hostility.content.entity.ChargeType;
import dev.xkmc.l2hostility.content.entity.HostilityCharge;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Supplier;

public class HostilityChargeItem extends FireChargeItem<HostilityCharge> {

	private final ChargeType type;

	public HostilityChargeItem(Properties prop, ChargeType type, Supplier<MutableComponent> tooltip) {
		super(prop, HostilityCharge::new, HostilityCharge::new, tooltip);
		this.type = type;
	}

	public ChargeType getType() {
		return type;
	}

}
