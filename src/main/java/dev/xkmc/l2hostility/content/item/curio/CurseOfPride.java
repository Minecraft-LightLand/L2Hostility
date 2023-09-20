package dev.xkmc.l2hostility.content.item.curio;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurseOfPride extends CurseCurioItem implements ICapItem<CurseOfPride.PrideCap> {

	private static final String NAME = "l2hostility:pride";
	private static final UUID ID = MathHelper.getUUIDFromString(NAME);

	public CurseOfPride(Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		int protect = (int) Math.round(100 * LHConfig.COMMON.prideHealthBonus.get());
		int damage = (int) Math.round(100 * LHConfig.COMMON.prideDamageBonus.get());
		int trait = (int) Math.round(100 * (1 / LHConfig.COMMON.prideTraitFactor.get() - 1));
		list.add(LangData.ITEM_CHARM_PRIDE.get(protect, damage).withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_TRAIT_CHEAP.get(trait).withStyle(ChatFormatting.RED));
	}

	@Override
	public PrideCap create(ItemStack stack) {
		return new PrideCap(stack);
	}

	public record PrideCap(ItemStack stack) implements ICurio {

		@Override
		public ItemStack getStack() {
			return stack;
		}

		@Override
		public void curioTick(SlotContext slotContext) {
			LivingEntity wearer = slotContext.entity();
			if (wearer == null) return;
			int level = DifficultyLevel.ofAny(wearer);
			stack.getOrCreateTag().putInt(NAME, level);
		}

		@Override
		public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
			Multimap<Attribute, AttributeModifier> ans = HashMultimap.create();
			LivingEntity wearer = slotContext.entity();
			int level = wearer == null ? 0 : DifficultyLevel.ofAny(wearer);
			if (level > 0) {
				double rate = LHConfig.COMMON.prideHealthBonus.get() * level;
				ans.put(Attributes.MAX_HEALTH, new AttributeModifier(ID, NAME, rate, AttributeModifier.Operation.MULTIPLY_BASE));
			}
			return ans;
		}

	}

}
