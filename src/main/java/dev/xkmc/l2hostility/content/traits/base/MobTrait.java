package dev.xkmc.l2hostility.content.traits.base;


import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.logic.InheritContext;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class MobTrait extends NamedEntry<MobTrait> {

	private final IntSupplier color;

	public MobTrait(ChatFormatting format) {
		this(format::getColor);
	}

	public MobTrait(IntSupplier color) {
		super(LHTraits.TRAITS);
		this.color = color;
	}

	@SuppressWarnings("ConstantConditions")
	public TraitConfig getConfig() {
		TraitConfig ans = L2Hostility.TRAIT.getEntry(getRegistryName());
		if (ans == null) return TraitConfig.DEFAULT;
		return ans;
	}

	public int getCost(double factor) {
		return Math.max(1, (int) Math.round(getConfig().cost * factor));
	}

	public int getMaxLevel() {
		return getConfig().max_rank;
	}

	public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
		if (isBanned()) return false;
		TraitConfig config = getConfig();
		if (difficulty < config.min_level) return false;
		if (difficulty < config.cost) return false;
		if (!EntityConfig.allow(le.getType(), this)) return false;
		return config.allows(le.getType());
	}

	public final boolean allow(LivingEntity le) {
		return allow(le, Integer.MAX_VALUE, TraitManager.getMaxLevel() + 1);
	}

	public void initialize(LivingEntity mob, int level) {
	}

	public void postInit(LivingEntity mob, int lv) {
	}

	public void tick(LivingEntity mob, int level) {
	}

	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
		var e = cache.getLivingHurtEvent();
		assert e != null;
		if (e.getAmount() > 0 && !e.getSource().getMsgId().equals("killer_aura")) {
			postHurtPlayer(level, attacker, traitCache);
		}
	}

	public void postHurtPlayer(int level, LivingEntity attacker, TraitEffectCache traitCache) {
		if (traitCache.reflectTrait(this)) {
			for (var e : traitCache.getTargets()) {
				postHurtImpl(level, attacker, e);
			}
		} else {
			postHurtImpl(level, attacker, traitCache.target);
		}
	}

	public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
	}

	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
	}

	public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
	}

	public void onCreateSource(int level, LivingEntity attacker, LivingAttackEvent event) {
	}

	public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {
	}

	public MutableComponent getFullDesc(@Nullable Integer value) {
		var ans = getDesc();
		if (value != null) ans = ans.append(" ")
				.append(Component.translatable("enchantment.level." + value));
		return ans.withStyle(Style.EMPTY.withColor(color.getAsInt()));
	}

	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}

	protected MutableComponent mapLevel(Function<Integer, MutableComponent> func) {
		MutableComponent comp = null;
		for (int i = 1; i <= getMaxLevel(); i++) {
			if (comp == null) {
				comp = func.apply(i);
			} else {
				comp = comp.append(Component.literal("/").withStyle(ChatFormatting.GRAY)).append(func.apply(i));
			}
		}
		assert comp != null;
		return comp;
	}

	public Item asItem() {
		Item item = ForgeRegistries.ITEMS.getValue(getRegistryName());
		if (item == null) {
			item = Items.AIR;
		}
		return item;
	}

	public boolean isBanned() {
		if (LHConfig.COMMON.map.containsKey(getRegistryName().getPath())) {
			return !LHConfig.COMMON.map.get(getRegistryName().getPath()).get();
		}
		return false;
	}

	public int inherited(MobTraitCap mobTraitCap, int rank, InheritContext ctx) {
		return rank;
	}

	public boolean is(TagKey<MobTrait> tag) {
		return LHTraits.TRAITS.get().tags().getTag(tag).contains(this);
	}

}
