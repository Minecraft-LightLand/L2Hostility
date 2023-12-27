package dev.xkmc.l2hostility.init.loot;

import com.mojang.serialization.Codec;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class TraitGLMProvider extends GlobalLootModifierProvider {

	private static final DeferredRegister<LootItemConditionType> LOOT_ITEM = DeferredRegister.create(Registry.LOOT_ITEM_REGISTRY, L2Hostility.MODID);

	public static final RegistryObject<LootItemConditionType> TRAIT_AND_LEVEL, MOB_LEVEL, HAS_ITEM,MIN_HEALTH;
	public static final RegistryEntry<Codec<TraitLootModifier>> TRAIT_SCALED;
	public static final RegistryEntry<Codec<EnvyLootModifier>> LOOT_ENVY;
	public static final RegistryEntry<Codec<GluttonyLootModifier>> LOOT_GLUTTONY;


	static {
		TRAIT_AND_LEVEL = LOOT_ITEM.register("trait_and_level",
				() -> new LootItemConditionType(new TraitSerializer<>(TraitLootCondition.class)));
		MOB_LEVEL = LOOT_ITEM.register("mob_level",
				() -> new LootItemConditionType(new TraitSerializer<>(MobCapLootCondition.class)));
		HAS_ITEM = LOOT_ITEM.register("player_has_item",
				() -> new LootItemConditionType(new TraitSerializer<>(PlayerHasItemCondition.class)));
		MIN_HEALTH = L2Hostility.REGISTRATE.simple("min_health",
				Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(
						new TraitSerializer<>(MobHealthCondition.class)));

		TRAIT_SCALED = L2Hostility.REGISTRATE.simple("trait_scaled",
				ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> TraitLootModifier.CODEC);
		LOOT_ENVY = L2Hostility.REGISTRATE.simple("loot_envy",
				ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> EnvyLootModifier.CODEC);
		LOOT_GLUTTONY = L2Hostility.REGISTRATE.simple("loot_gluttony",
				ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> GluttonyLootModifier.CODEC);

	}

	public static void register(IEventBus bus) {
		LOOT_ITEM.register(bus);
	}

	public TraitGLMProvider(DataGenerator gen) {
		super(gen, L2Hostility.MODID);
	}

	@Override
	protected void start() {
		add("loot_envy", new EnvyLootModifier(LootTableTemplate.byPlayer().build(),
				new PlayerHasItemCondition(LHItems.CURSE_ENVY.get())));
		add("loot_gluttony", new GluttonyLootModifier(LootTableTemplate.byPlayer().build(),
				new PlayerHasItemCondition(LHItems.CURSE_GLUTTONY.get())));

		add(LHTraits.TANK.get(), new ItemStack(Items.DIAMOND, 4), 1, 0, 0.1);
		add(LHTraits.TANK.get(), new ItemStack(Items.NETHERITE_SCRAP, 1), 3, 0, 0.1);
		add(LHTraits.SPEEDY.get(), new ItemStack(Items.RABBIT_FOOT, 2), 1, 0, 0.1);
		add(LHTraits.SPEEDY.get(), new ItemStack(LCItems.CAPTURED_WIND.get(), 1), 3, 0, 0.1, 50);
		add(LHTraits.PROTECTION.get(), new ItemStack(Items.SCUTE, 4), 1, 0, 0.1);
		add(LHTraits.PROTECTION.get(), new ItemStack(Items.SHULKER_SHELL, 1), 3, 0, 0.1);
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
		add(LHTraits.LEVITATION.get(), new ItemStack(LCItems.CAPTURED_BULLET.get(), 1), 1, 0, 0.1);
		add(LHTraits.BLIND.get(), new ItemStack(Items.INK_SAC, 8), 1, 0, 0.1);
		add(LHTraits.CONFUSION.get(), new ItemStack(Items.PUFFERFISH, 4), 1, 0, 0.1);
		add(LHTraits.SOUL_BURNER.get(), new ItemStack(LCItems.SOUL_FLAME.get(), 2), 1, 0, 0.1);
		add(LHTraits.FREEZING.get(), new ItemStack(LCItems.HARD_ICE.get(), 2), 1, 0, 0.1);
		add(LHTraits.CURSED.get(), PotionUtils.setPotion(Items.POTION.getDefaultInstance(), Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(new ResourceLocation(L2Complements.MODID, "curse")))), 1, 0, 0.2);
		add(LHTraits.CURSED.get(), new ItemStack(LCItems.CURSED_DROPLET.get(), 1), 3, 0, 0.05);
		add(LHTraits.CORROSION.get(), new ItemStack(LCItems.CURSED_DROPLET.get(), 1), 1, 0, 0.1);
		add(LHTraits.EROSION.get(), new ItemStack(LCItems.CURSED_DROPLET.get(), 1), 1, 0, 0.1);
		add(LHTraits.KILLER_AURA.get(), new ItemStack(LCItems.EMERALD.get(), 1), 1, 0, 0.02);
		add(LHTraits.RAGNAROK.get(), new ItemStack(LCMats.ETERNIUM.getNugget(), 4), 1, 0, 0.1);
		add(LHTraits.SHULKER.get(), new ItemStack(LCItems.CAPTURED_BULLET.get(), 1), 1, 0, 0.2);
		add(LHTraits.GRENADE.get(), new ItemStack(LCItems.STORM_CORE.get(), 1), 3, 0, 0.1);
		add(LHTraits.GRENADE.get(), new ItemStack(Items.GUNPOWDER, 4), 1, 0, 0.1);
		add(LHTraits.GRENADE.get(), new ItemStack(Items.CREEPER_HEAD, 1), 5, 0.25, 0);
		add(LHTraits.DRAIN.get(), new ItemStack(LHItems.WITCH_DROPLET.get(), 1), 1, 0, 0.1);
		add(LHTraits.GROWTH.get(), new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1), 1, 0, 0.02);
		add(LHTraits.SPLIT.get(), new ItemStack(LCItems.GUARDIAN_EYE.get(), 1), 1, 0, 0.05);
		add(LHTraits.GRAVITY.get(), new ItemStack(Items.DRAGON_BREATH, 4), 1, 0, 0.1);
		add(LHTraits.MOONWALK.get(), new ItemStack(Items.DRAGON_BREATH, 4), 1, 0, 0.1);
		add(LHTraits.STRIKE.get(), new ItemStack(LCItems.EXPLOSION_SHARD.get(), 1), 1, 0, 0.1);
		add(LHTraits.PULLING.get(), new ItemStack(LCItems.BLACKSTONE_CORE.get(), 1), 1, 0, 0.05);

		add(LHTraits.REPRINT.get(), new ItemStack(LHItems.BOOK_COPY.get()), 1, 1, 0);
		add(LHTraits.DISPELL.get(), new ItemStack(LHItems.IMAGINE_BREAKER.get()), 3, 1, 0);

		add(LHTraits.TANK.get(), new ItemStack(LCMats.SHULKERATE.getNugget(), 6), 0, 0.1,
				LootTableTemplate.byPlayer().build(),
				new TraitLootCondition(LHTraits.TANK.get(), 3, 5),
				new TraitLootCondition(LHTraits.PROTECTION.get(), 1, 3)
		);
		add(LHTraits.TANK.get(), new ItemStack(LCMats.SHULKERATE.getIngot(), 2), 0, 0.1,
				LootTableTemplate.byPlayer().build(),
				new TraitLootCondition(LHTraits.TANK.get(), 3, 5),
				new TraitLootCondition(LHTraits.PROTECTION.get(), 4, 5)
		);
		add(LHTraits.SPEEDY.get(), new ItemStack(LCMats.SCULKIUM.getNugget(), 4), 0, 0.1,
				LootTableTemplate.byPlayer().build(),
				new TraitLootCondition(LHTraits.SPEEDY.get(), 3, 5),
				new TraitLootCondition(LHTraits.TANK.get(), 3, 5)
		);

		add(LHTraits.DEMENTOR.get(), new ItemStack(LHItems.CHAOS_INGOT.get(), 1), 1, 0,
				LootTableTemplate.byPlayer().build(),
				new TraitLootCondition(LHTraits.KILLER_AURA.get(), 1, 5),
				new TraitLootCondition(LHTraits.RAGNAROK.get(), 1, 5)
		);


	}

	private void add(MobTrait trait, ItemStack stack, int start, double chance, double bonus, int min) {
		add(trait, stack, chance, bonus,
				LootTableTemplate.byPlayer().build(),
				new TraitLootCondition(trait, start, 5),
				new MobCapLootCondition(min)
		);
	}

	private void add(MobTrait trait, ItemStack stack, int start, double chance, double bonus) {
		add(trait, stack, chance, bonus,
				LootTableTemplate.byPlayer().build(),
				new TraitLootCondition(trait, start, 5)
		);
	}

	private void add(MobTrait trait, ItemStack stack, double chance, double bonus, LootItemCondition... conditions) {
		String name = trait.getRegistryName().getPath() + "_drop_" + ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
		add(name, new TraitLootModifier(trait, chance, bonus, stack, conditions));
	}

}
