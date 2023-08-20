package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
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

	public int getCost() {
		return getConfig().cost;
	}

	public int getMaxLevel() {
		return getConfig().maxLevel;
	}

	public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
		TraitConfig config = getConfig();
		if (difficulty < config.cost) return false;
		if (config.blacklist.contains(le.getType())) return false;
		if (!config.whitelist.isEmpty() && !config.whitelist.contains(le.getType())) return false;
		return le.getRandom().nextDouble() < config.chance;
	}

	public void initialize(LivingEntity mob, int level) {
	}

	public void postInit(LivingEntity mob, int lv) {

	}

	public void tick(LivingEntity mob, int level) {
	}

	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache) {
	}

	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
	}

	public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
	}

	public void onCreateSource(int level, LivingEntity attacker, CreateSourceEvent event) {
	}

	public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {

	}

	public MutableComponent getFullDesc(@Nullable Integer value) {
		var ans = getDesc();
		if (value != null) ans = ans.append(CommonComponents.SPACE)
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

}
