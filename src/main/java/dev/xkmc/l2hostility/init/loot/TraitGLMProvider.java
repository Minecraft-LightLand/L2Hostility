package dev.xkmc.l2hostility.init.loot;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class TraitGLMProvider extends GlobalLootModifierProvider {

	public static final RegistryEntry<LootItemConditionType> TRAIT_AND_LEVEL;
	public static final RegistryEntry<LootItemConditionType> MOB_LEVEL;
	public static final RegistryEntry<Codec<TraitLootModifier>> TRAIT_SCALED;

	static {
		TRAIT_AND_LEVEL = L2Hostility.REGISTRATE.simple("trait_and_level",
				Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(
						new TraitSerializer<>(TraitLootCondition.class)));
		MOB_LEVEL = L2Hostility.REGISTRATE.simple("mob_level",
				Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(
						new TraitSerializer<>(MobCapLootCondition.class)));
		TRAIT_SCALED = L2Hostility.REGISTRATE.simple("trait_scaled",
				ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> TraitLootModifier.CODEC);

	}

	public static void register() {

	}

	public TraitGLMProvider(DataGenerator gen) {
		super(gen.getPackOutput(), L2Hostility.MODID);
	}

	@Override
	protected void start() {
		add(LHTraits.TANK.get(), new ItemStack(Items.DIAMOND, 4), 1, 0, 0.1);
		add(LHTraits.TANK.get(), new ItemStack(Items.NETHERITE_SCRAP, 1), 3, 0, 0.1);
		add(LHTraits.SPEEDY.get(), new ItemStack(Items.RABBIT_FOOT, 2), 1, 0, 0.1);
		add(LHTraits.SPEEDY.get(), new ItemStack(LCItems.CAPTURED_WIND, 1), 3, 0, 0.1, 50);
		add(LHTraits.PROTECTION.get(), new ItemStack(Items.SCUTE, 4), 1, 0, 0.1);
		add(LHTraits.PROTECTION.get(), new ItemStack(LCItems.EXPLOSION_SHARD, 1), 3, 0, 0.1);
		add(LHTraits.INVISIBLE.get(), new ItemStack(Items.PHANTOM_MEMBRANE, 4), 1, 0.25, 0);
		add(LHTraits.FIERY.get(), new ItemStack(Items.BLAZE_ROD, 8), 1, 0.25, 0);
		add(LHTraits.REGEN.get(), new ItemStack(Items.GHAST_TEAR, 4), 1, 0, 0.1);
		add(LHTraits.REGEN.get(), new ItemStack(LCMats.TOTEMIC_GOLD.getNugget(), 4), 3, 0, 0.1);
		add(LHTraits.REGEN.get(), new ItemStack(LCItems.LIFE_ESSENCE.get(), 1), 5, 0.2, 0);
		add(LHTraits.ADAPTIVE.get(), new ItemStack(LCItems.CURSED_DROPLET.get(), 1), 1, 0, 0.1);
		add(LHTraits.REFLECT.get(), new ItemStack(LCItems.EXPLOSION_SHARD.get(), 1), 1, 0, 0.1);
		add(LHTraits.DEMENTOR.get(), new ItemStack(LCItems.SUN_MEMBRANE.get(), 1), 1, 0.2, 0.1);
		add(LHTraits.DISPELL.get(), new ItemStack(LCItems.RESONANT_FEATHER.get(), 1), 1, 0.2, 0.1);
		add(LHTraits.UNDYING.get(), new ItemStack(Items.TOTEM_OF_UNDYING, 1), 1, 1, 0);
		add(LHTraits.UNDYING.get(), new ItemStack(LCItems.LIFE_ESSENCE.get(), 1), 1, 0.5, 0);
		add(LHTraits.ENDER.get(), new ItemStack(LCItems.VOID_EYE.get(), 1), 1, 0.2, 0.1);
		add(LHTraits.REPELLING.get(), new ItemStack(LCItems.FORCE_FIELD.get(), 1), 1, 0.2, 0.1);

		add(LHTraits.WEAKNESS.get(), new ItemStack(Items.FERMENTED_SPIDER_EYE, 8), 1, 0, 0.1);
		add(LHTraits.SLOWNESS.get(), new ItemStack(Items.COBWEB, 4), 1, 0, 0.1);
		add(LHTraits.POISON.get(), new ItemStack(Items.SPIDER_EYE, 8), 1, 0, 0.1);
		add(LHTraits.WITHER.get(), new ItemStack(Items.WITHER_ROSE, 8), 1, 0, 0.1);
		add(LHTraits.WITHER.get(), new ItemStack(Items.WITHER_SKELETON_SKULL, 1), 3, 0, 0.1);
		add(LHTraits.LEVITATION.get(), new ItemStack(LCItems.CAPTURED_BULLET, 1), 1, 0, 0.1);
		add(LHTraits.BLIND.get(), new ItemStack(Items.INK_SAC, 8), 1, 0, 0.1);
		add(LHTraits.CONFUSION.get(), new ItemStack(Items.PUFFERFISH, 4), 1, 0, 0.1);
		add(LHTraits.SOUL_BURNER.get(), new ItemStack(LCItems.SOUL_FLAME, 2), 1, 0, 0.1);
		add(LHTraits.FREEZING.get(), new ItemStack(LCItems.HARD_ICE, 2), 1, 0, 0.1);
		add(LHTraits.CURSED.get(), PotionUtils.setPotion(Items.POTION.getDefaultInstance(), Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(new ResourceLocation(L2Complements.MODID, "curse")))), 1, 0, 0.2);
		add(LHTraits.CURSED.get(), new ItemStack(LCItems.CURSED_DROPLET, 1), 3, 0, 0.05);
	}

	private void add(MobTrait trait, ItemStack stack, int start, double chance, double bonus, int min) {
		String name = trait.getRegistryName().getPath() + "_drop_" + ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
		add(name, new TraitLootModifier(trait, chance, bonus, stack,
				LootTableTemplate.byPlayer().build(),
				new TraitLootCondition(trait, start, 5),
				new MobCapLootCondition(min)
		));
	}

	private void add(MobTrait trait, ItemStack stack, int start, double chance, double bonus) {
		String name = trait.getRegistryName().getPath() + "_drop_" + ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
		add(name, new TraitLootModifier(trait, chance, bonus, stack,
				LootTableTemplate.byPlayer().build(),
				new TraitLootCondition(trait, start, 5)
		));
	}
}
