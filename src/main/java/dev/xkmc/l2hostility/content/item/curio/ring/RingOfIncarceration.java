package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.item.curio.core.CurioItem;
import dev.xkmc.l2hostility.content.item.curio.core.ICapItem;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class RingOfIncarceration extends CurioItem implements ICapItem<RingOfIncarceration.Cap> {

	public RingOfIncarceration(Properties properties) {
		super(properties);
	}

	@Override
	public Cap create(ItemStack stack) {
		return new Cap(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_INCARCERATION.get().withStyle(ChatFormatting.GOLD));
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
			if (!wearer.isShiftKeyDown()) return;
			var reach = ForgeMod.ENTITY_REACH.get();
			var attr = wearer.getAttribute(reach);
			var r = attr == null ? reach.getDefaultValue() : attr.getValue();
			for (var e : wearer.level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
					wearer.getBoundingBox().inflate(r), e -> wearer.distanceTo(e) < r)) {
				EffectUtil.refreshEffect(e, new MobEffectInstance(LCEffects.STONE_CAGE.get(), 40),
						EffectUtil.AddReason.NONE, wearer);
			}
		}

	}

}
