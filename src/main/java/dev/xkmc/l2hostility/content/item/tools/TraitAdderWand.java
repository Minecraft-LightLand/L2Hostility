package dev.xkmc.l2hostility.content.item.tools;

import dev.xkmc.l2hostility.content.capability.mob.MobModifierCap;
import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHModifiers;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TraitAdderWand extends Item {

	private static final String MODIFIER = "l2hostility_modifier";

	public static ItemStack set(ItemStack ans, MobModifier modifier) {
		ans.getOrCreateTag().putString(MODIFIER, modifier.getID());
		return ans;
	}

	public static MobModifier get(ItemStack stack) {
		if (stack.getOrCreateTag().contains(MODIFIER, Tag.TAG_STRING)) {
			String str = stack.getOrCreateTag().getString(MODIFIER);
			ResourceLocation id = new ResourceLocation(str);
			MobModifier ans = LHModifiers.MODIFIERS.get().getValue(id);
			if (ans != null) {
				return ans;
			}
		}
		return LHModifiers.TANK.get();
	}

	private static List<MobModifier> values() {
		return new ArrayList<>(LHModifiers.MODIFIERS.get().getValues());
	}

	private static MobModifier next(MobModifier mod) {
		var list = values();
		int index = list.indexOf(mod);
		if (index + 1 >= list.size()) {
			return list.get(0);
		}
		return list.get(index + 1);
	}

	private static MobModifier prev(MobModifier mod) {
		var list = values();
		int index = list.indexOf(mod);
		if (index == 0) {
			return list.get(list.size() - 1);
		}
		return list.get(index - 1);
	}

	@Nullable
	private static Integer inc(MobModifier k, @Nullable Integer old) {
		if (old == null || old == 0) {
			return k.getMaxLevel();
		}
		if (old == 1) {
			return null;
		}
		return old - 1;
	}

	@Nullable
	private static Integer dec(MobModifier k, @Nullable Integer old) {
		if (old == null) {
			return 1;
		}
		if (old == k.getMaxLevel()) {
			return null;
		}
		return old + 1;
	}

	public TraitAdderWand(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (MobModifierCap.HOLDER.isProper(target)) {
			if (player.level().isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			MobModifierCap cap = MobModifierCap.HOLDER.get(target);
			MobModifier modifier = get(stack);
			Integer ans;
			if (player.isShiftKeyDown()) {
				ans = cap.modifiers.compute(modifier, TraitAdderWand::inc);
			} else {
				ans = cap.modifiers.compute(modifier, TraitAdderWand::dec);
			}
			int val = ans == null ? 0 : ans;
			player.sendSystemMessage(LangData.MSG_SET_MODIFIER.get(modifier.getDesc(), target.getDisplayName(), val));
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		MobModifier old = get(stack), next;
		if (player.isShiftKeyDown()) {
			next = prev(old);
		} else {
			next = next(old);
		}
		set(stack, next);
		player.sendSystemMessage(LangData.MSG_SELECT_MODIFIER.get(next.getDesc()));
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

}
