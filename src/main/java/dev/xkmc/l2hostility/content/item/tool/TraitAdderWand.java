package dev.xkmc.l2hostility.content.item.tool;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2magic.content.item.utility.BaseWand;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import java.util.List;

public class TraitAdderWand extends BaseWand {

	public static ItemStack set(ItemStack ans, MobTrait trait) {
		return LHItems.DC_TRAIT_WAND.set(ans, trait.holder());
	}

	public static MobTrait get(ItemStack stack) {
		return LHItems.DC_TRAIT_WAND.getOrDefault(stack, LHTraits.TANK).value();
	}

	private static List<MobTrait> values() {
		return LHTraits.TRAITS.get().stream().toList();
	}

	private static MobTrait next(MobTrait mod) {
		var list = values();
		int index = list.indexOf(mod);
		if (index + 1 >= list.size()) {
			return list.getFirst();
		}
		return list.get(index + 1);
	}

	private static MobTrait prev(MobTrait mod) {
		var list = values();
		int index = list.indexOf(mod);
		if (index == 0) {
			return list.getLast();
		}
		return list.get(index - 1);
	}

	@Nullable
	public static Integer decrease(RegistryAccess access, MobTrait k, @Nullable Integer old) {
		if (old == null || old == 0) {
			return k.getMaxLevel(access);
		}
		if (old == 1) {
			return null;
		}
		return old - 1;
	}

	@Nullable
	public static Integer increase(RegistryAccess access, MobTrait k, @Nullable Integer old) {
		if (old == null) {
			return 1;
		}
		if (old == k.getMaxLevel(access)) {
			return null;
		}
		return old + 1;
	}

	public TraitAdderWand(Properties properties) {
		super(properties);
	}


	@Override
	public void clickTarget(ItemStack stack, Player player, LivingEntity target) {
		var opt = LHMiscs.MOB.type().getExisting(target);
		if (opt.isEmpty() && !LHMiscs.MOB.type().isProper(target)) return;
		MobTraitCap cap = opt.orElse(LHMiscs.MOB.type().getOrCreate(target));
		MobTrait trait = get(stack);
		Integer ans;
		var access = player.registryAccess();
		if (player.isShiftKeyDown()) {
			ans = cap.traits.compute(trait, (k, i) -> decrease(access, k, i));
		} else {
			ans = cap.traits.compute(trait, (k, i) -> increase(access, k, i));
		}
		int val = ans == null ? 0 : ans;
		trait.initialize(target, val);
		trait.postInit(target, val);
		cap.syncToClient(target);
		target.setHealth(target.getMaxHealth());
		player.sendSystemMessage(LangData.MSG_SET_TRAIT.get(trait.getDesc(), target.getDisplayName(), val));
	}

	@Override
	public void clickNothing(ItemStack stack, Player player) {
		MobTrait old = get(stack), next;
		if (player.isShiftKeyDown()) {
			next = prev(old);
		} else {
			next = next(old);
		}
		set(stack, next);
		player.sendSystemMessage(LangData.MSG_SELECT_TRAIT.get(next.getDesc()));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_WAND_ADDER.get().withStyle(ChatFormatting.GRAY));
		MobTrait trait = get(stack);
		list.add(LangData.MSG_SELECT_TRAIT.get(trait.getDesc().withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
	}

}
