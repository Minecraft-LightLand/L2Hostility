package dev.xkmc.l2hostility.content.item.curio;

import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2damagetracker.init.data.L2DTLangData;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class CurseOfWrath extends CurseCurioItem {

	public CurseOfWrath(Properties props) {
		super(props);
	}

	@Override
	public int getExtraLevel(ItemStack stack) {
		return LHConfig.COMMON.wrathExtraLevel.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		int rate = (int) Math.round(100 * LHConfig.COMMON.wrathDamageBonus.get());
		int lv = LHConfig.COMMON.wrathExtraLevel.get();
		list.add(LangData.ITEM_CHARM_WRATH.get(rate).withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_ADD_LEVEL.get(lv).withStyle(ChatFormatting.RED));
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
		assert id != null;
		addTooltip(list, ArmorEffectConfig.get().getImmunity(id.toString()));
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
