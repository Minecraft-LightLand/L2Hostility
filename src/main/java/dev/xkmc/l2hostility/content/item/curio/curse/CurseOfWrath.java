package dev.xkmc.l2hostility.content.item.curio.curse;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2damagetracker.init.data.L2DTLangData;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class CurseOfWrath extends CurseCurioItem {

	public CurseOfWrath(Properties props) {
		super(props);
	}

	@Override
	public int getExtraLevel() {
		return LHConfig.SERVER.wrathExtraLevel.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		int rate = (int) Math.round(100 * LHConfig.SERVER.wrathDamageBonus.get());
		list.add(LangData.ITEM_CHARM_WRATH.get(rate).withStyle(ChatFormatting.GOLD));
		if (ctx.level() != null)
			addTooltip(list, L2DamageTracker.ARMOR.get(ctx.level().registryAccess(), builtInRegistryHolder()));
	}

	@Override
	public void onHurtTarget(ItemStack stack, LivingEntity user, DamageData.Offence cache) {
		int level = DifficultyLevel.ofAny(cache.getTarget()) - DifficultyLevel.ofAny(user);
		if (level > 0) {
			double rate = LHConfig.SERVER.wrathDamageBonus.get();
			cache.addHurtModifier(DamageModifier.multTotal((float) (1 + level * rate), getID()));
		}
	}

	private void addTooltip(List<Component> list, Set<MobEffect> set) {
		TreeMap<ResourceLocation, MobEffect> map = new TreeMap<>();
		for (MobEffect e : set) {
			map.put(ForgeRegistries.MOB_EFFECTS.getKey(e), e);
		}
		MutableComponent comp = L2DTLangData.ARMOR_IMMUNE.get();
		boolean comma = false;
		for (MobEffect e : map.values()) {
			if (comma) {
				comp = comp.append(", ");
			}
			comp = comp.append(Component.translatable(e.getDescriptionId())
					.withStyle(e.getCategory().getTooltipFormatting()));
			comma = true;
		}
		list.add(comp.withStyle(ChatFormatting.LIGHT_PURPLE));
	}

}
