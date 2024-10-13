package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RingOfReflection extends CurseCurioItem {

	public RingOfReflection(Properties properties) {
		super(properties);
	}

	@Override
	public boolean reflectTrait(MobTrait trait) {
		return trait.is(LHTraits.POTION);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_REFLECTION.get().withStyle(ChatFormatting.GOLD));
	}

	public boolean isOn(LivingEntity le) {
		return CurioCompat.hasItemInCurioChecked(le, this);
	}

}
