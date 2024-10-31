package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2core.util.ServerProxy;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.logic.InheritContext;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHDamageTypes;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class MobTrait extends NamedEntry<MobTrait> implements ItemLike {

	private final IntSupplier color;

	public MobTrait(ChatFormatting format) {
		this(format::getColor);
	}

	public MobTrait(IntSupplier color) {
		super(LHTraits.TRAITS);
		this.color = color;
	}

	public TraitConfig getConfig(RegistryAccess access) {
		TraitConfig ans = LHTraits.DATA.get(access, holder());
		if (ans == null) return TraitConfig.DEFAULT;
		return ans;
	}

	@Deprecated //TODO
	public TraitConfig getConfig() {
		return getConfig(ServerProxy.getRegistryAccess());
	}

	public int getCost(double factor) {
		return Math.max(1, (int) Math.round(getConfig().cost() * factor));
	}

	public int getMaxLevel() {
		return getConfig().max_rank();
	}

	public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
		if (isBanned()) return false;
		TraitConfig config = getConfig(le.level().registryAccess());
		if (difficulty < config.min_level()) return false;
		if (!EntityConfig.allow(le.getType(), this)) return false;
		return config.allows(getRegistryName(), le.getType());
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

	public void onHurtTarget(int level, LivingEntity attacker, DamageData.Offence e, TraitEffectCache traitCache) {
	}

	public void onHurtTargetMax(int level, LivingEntity attacker, DamageData.OffenceMax e, TraitEffectCache traitCache) {
		if (e.getDamageOriginal() > 0 && !e.getSource().is(LHDamageTypes.KILLER_AURA)) {
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

	public boolean onAttackedByOthers(int level, LivingEntity entity, DamageData.Attack event) {
		return false;
	}

	public void onDamaged(int level, LivingEntity entity, DamageData.Defence event) {
	}

	public void onCreateSource(int level, LivingEntity attacker, CreateSourceEvent event) {
	}

	public void onHurtByMax(int level, LivingEntity mob, DamageData.OffenceMax cache) {
	}

	public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {
	}

	public MutableComponent getFullDesc(@Nullable Integer value) {
		var ans = getDesc();
		if (value != null) ans = ans.append(CommonComponents.SPACE)
				.append(Component.translatable("enchantment.level." + value));
		return ans.withStyle(Style.EMPTY.withColor(color.getAsInt()));
	}

	public int getColor() {
		return color.getAsInt();
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
		return BuiltInRegistries.ITEM.get(getRegistryName());
	}

	public boolean isBanned() {
		if (LHConfig.SERVER.map.containsKey(getRegistryName().getPath())) {
			return !LHConfig.SERVER.map.get(getRegistryName().getPath()).get();
		}
		return false;
	}

	@Override
	public String toString() {
		return getID();
	}

	public int inherited(MobTraitCap mobTraitCap, int rank, InheritContext ctx) {
		return rank;
	}

	public boolean is(TagKey<MobTrait> tag) {
		return holder().is(tag);
	}

}
