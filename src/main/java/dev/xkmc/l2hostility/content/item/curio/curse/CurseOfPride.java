package dev.xkmc.l2hostility.content.item.curio.curse;

import com.google.common.collect.Multimap;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CurseOfPride extends CurseCurioItem {

	public CurseOfPride(Properties props) {
		super(props);
	}

	@Override
	public void onHurtTarget(ItemStack stack, LivingEntity user, DamageData.Offence cache) {
		int level = DifficultyLevel.ofAny(user);
		float rate = LHConfig.SERVER.prideDamageBonus.get().floatValue();
		cache.addHurtModifier(DamageModifier.multBase(level * rate, getID()));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		int protect = (int) Math.round(100 * LHConfig.SERVER.prideHealthBonus.get());
		int damage = (int) Math.round(100 * LHConfig.SERVER.prideDamageBonus.get());
		int trait = (int) Math.round(100 * (1 / LHConfig.SERVER.prideTraitFactor.get() - 1));
		list.add(LangData.ITEM_CHARM_PRIDE.get(protect, damage).withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_TRAIT_CHEAP.get(trait).withStyle(ChatFormatting.RED));
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		LivingEntity wearer = slotContext.entity();
		if (wearer == null) return;
		int level = DifficultyLevel.ofAny(wearer);
		LHItems.DC_PRIDE_LEVEL.set(stack, level);
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation uuid, ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> ans = super.getAttributeModifiers(slotContext, uuid, stack);
		int level = LHItems.DC_PRIDE_LEVEL.getOrDefault(stack, 0);
		if (level > 0) {
			double rate = LHConfig.SERVER.prideHealthBonus.get() * level;
			ans.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, rate, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
		}
		return ans;
	}

}
