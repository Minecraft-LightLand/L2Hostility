package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2core.base.effects.EffectUtil;
import dev.xkmc.l2hostility.content.item.curio.core.SingletonItem;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.entity.EntityTypeTest;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class RingOfIncarceration extends SingletonItem {

	public RingOfIncarceration(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_INCARCERATION.get().withStyle(ChatFormatting.GOLD));
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		LivingEntity wearer = slotContext.entity();
		if (wearer == null) return;
		if (!wearer.isShiftKeyDown()) return;
		if (wearer.isSpectator()) return;
		var reach = Attributes.ENTITY_INTERACTION_RANGE;
		var attr = wearer.getAttribute(reach);
		var r = attr == null ? reach.value().getDefaultValue() : attr.getValue();
		for (var e : wearer.level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
				wearer.getBoundingBox().inflate(r), e -> wearer.distanceTo(e) < r)) {
			if (e.isSpectator() || e instanceof Player player && player.isCreative()) continue;
			EffectUtil.refreshEffect(e, new MobEffectInstance(LCEffects.INCARCERATE, 40,
					0, true, true), wearer);
		}
	}

}
