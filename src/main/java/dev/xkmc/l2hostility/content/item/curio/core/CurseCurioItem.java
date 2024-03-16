package dev.xkmc.l2hostility.content.item.curio.core;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2complements.content.item.curios.CurioItem;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CurseCurioItem extends CurioItem implements ICurioItem {

	public static List<GenericItemStack<CurseCurioItem>> getFromPlayer(LivingEntity player) {
		var list = CurioCompat.getItems(player, e -> e.getItem() instanceof CurseCurioItem);
		List<GenericItemStack<CurseCurioItem>> ans = new ArrayList<>();
		for (var e : list) {
			if (e.getItem() instanceof CurseCurioItem item) {
				ans.add(new GenericItemStack<>(item, e));
			}
		}
		return ans;
	}

	public CurseCurioItem(Properties props) {
		super(props);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
		int lv = getExtraLevel();
		if (lv > 0) {
			ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
			assert id != null;
			map.put(LHMiscs.ADD_LEVEL.get(), new AttributeModifier(uuid, id.getPath(), lv, AttributeModifier.Operation.ADDITION));
		}
		return map;
	}

	public int getExtraLevel() {
		return 0;
	}

	public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return 1;
	}

	public double getGrowFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return 1;
	}

	public boolean reflectTrait(MobTrait trait) {
		return false;
	}

	public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
	}

	public void onDamage(ItemStack stack, LivingEntity user, LivingDamageEvent event) {
	}

}
