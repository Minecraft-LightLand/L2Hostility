package dev.xkmc.l2hostility.content.item.curio.curse;

import com.google.common.collect.Multimap;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
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

import java.util.List;
import java.util.UUID;

public class CurseOfPride extends CurseCurioItem {

	private static final String NAME = "l2hostility:pride";
	private static final UUID ID = MathHelper.getUUIDFromString(NAME);

	public CurseOfPride(Properties props) {
		super(props);
	}

	@Override
	public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
		int level = DifficultyLevel.ofAny(user);
		double rate = LHConfig.COMMON.prideDamageBonus.get();
		cache.addHurtModifier(DamageModifier.multTotal((float) (1 + level * rate)));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		var protect = LangData.perc(LHConfig.COMMON.prideHealthBonus.get());
		var damage = LangData.perc(LHConfig.COMMON.prideDamageBonus.get());
		var trait = LangData.perc((1 / LHConfig.COMMON.prideTraitFactor.get() - 1));
		list.add(LangData.ITEM_CHARM_PRIDE.get(protect, damage).withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_TRAIT_CHEAP.get(trait).withStyle(ChatFormatting.RED));
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		LivingEntity wearer = slotContext.entity();
		if (wearer == null) return;
		int level = DifficultyLevel.ofAny(wearer);
		stack.getOrCreateTag().putInt(NAME, level);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nullable LivingEntity wearer, UUID uuid) {
		Multimap<Attribute, AttributeModifier> ans = super.getAttributeModifiers(wearer, uuid);
		int level = wearer == null ? 0 : DifficultyLevel.ofAny(wearer);
		if (level > 0) {
			double rate = LHConfig.COMMON.prideHealthBonus.get() * level;
			ans.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, NAME, rate, AttributeModifier.Operation.MULTIPLY_BASE));
		}
		return ans;
	}

}
