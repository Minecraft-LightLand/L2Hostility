package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2hostility.content.item.curio.core.CurioItem;
import dev.xkmc.l2hostility.content.item.curio.core.ICapItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class RingOfHealing extends CurioItem implements ICapItem<RingOfHealing.Cap> {

	public RingOfHealing(Properties properties) {
		super(properties);
	}

	@Override
	public Cap create(ItemStack stack) {
		return new Cap(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_HEALING.get(Math.round(LHConfig.COMMON.ringOfHealingRate.get() * 100)).withStyle(ChatFormatting.GOLD));
	}

	public record Cap(ItemStack stack) implements ICurio {

		@Override
		public ItemStack getStack() {
			return stack;
		}

		@Override
		public void curioTick(SlotContext slotContext) {
			LivingEntity wearer = slotContext.entity();
			if (wearer == null) return;
			if (wearer.tickCount % 20 != 0) return;
			wearer.heal((float) (LHConfig.COMMON.ringOfHealingRate.get() * wearer.getMaxHealth()));
		}

	}

}
