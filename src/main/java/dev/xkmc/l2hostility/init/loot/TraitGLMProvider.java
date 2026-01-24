package dev.xkmc.l2hostility.init.loot;

import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2serial.serialization.codec.MapCodecAdaptor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.concurrent.CompletableFuture;

public class TraitGLMProvider extends GlobalLootModifierProvider {

	private static final SR<LootItemConditionType> LICT = SR.of(L2Hostility.REG, Registries.LOOT_CONDITION_TYPE);
	private static final CdcReg<IGlobalLootModifier> GLM = CdcReg.of(L2Hostility.REG, NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS);

	public static final Val<LootItemConditionType> TRAIT_AND_LEVEL, MOB_LEVEL, HAS_ITEM, MIN_HEALTH;
	public static final CdcVal<TraitLootModifier> TRAIT_SCALED;
	public static final CdcVal<EnvyLootModifier> LOOT_ENVY;
	public static final CdcVal<GluttonyLootModifier> LOOT_GLUTTONY;


	static {
		TRAIT_AND_LEVEL = LICT.reg("trait_and_level",
				() -> new LootItemConditionType(MapCodecAdaptor.of(TraitLootCondition.class)));
		MOB_LEVEL = LICT.reg("mob_level",
				() -> new LootItemConditionType(MapCodecAdaptor.of(MobCapLootCondition.class)));
		HAS_ITEM = LICT.reg("player_has_item",
				() -> new LootItemConditionType(MapCodecAdaptor.of(PlayerHasItemCondition.class)));
		MIN_HEALTH = LICT.reg("min_health",
				() -> new LootItemConditionType(MapCodecAdaptor.of(MobHealthCondition.class)));

		TRAIT_SCALED = GLM.reg("trait_scaled", TraitLootModifier.CODEC);
		LOOT_ENVY = GLM.reg("loot_envy", EnvyLootModifier.CODEC);
		LOOT_GLUTTONY = GLM.reg("loot_gluttony", GluttonyLootModifier.CODEC);

	}

	public static void register() {

	}

	public TraitGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, L2Hostility.MODID);
	}

	@Override
	protected void start() {
		var loot1 = LHItems.LOOT_1.get();
		var loot2 = LHItems.LOOT_2.get();
		var loot3 = LHItems.LOOT_3.get();
		var loot4 = LHItems.LOOT_4.get();


		add("loot_envy", new EnvyLootModifier(byPlayer().build(),
				new PlayerHasItemCondition(LHItems.CURSE_ENVY.get())));
		add("loot_gluttony", new GluttonyLootModifier(byPlayer().build(),
				new PlayerHasItemCondition(LHItems.CURSE_GLUTTONY.get())));

		add(LHTraits.TANK.get(), loot1, new ItemStack(Items.DIAMOND, 4), 1, 0, 0.1);
		add(LHTraits.TANK.get(), loot1, new ItemStack(Items.NETHERITE_SCRAP, 1), 3, 0, 0.1);
		add(LHTraits.SPEEDY.get(), loot1, new ItemStack(Items.RABBIT_FOOT, 2), 1, 0, 0.1);
		add(LHTraits.SPEEDY.get(), loot3, LCItems.CAPTURED_WIND.asStack(), 3, 0, 0.1, 100);
		add(LHTraits.PROTECTION.get(), loot1, new ItemStack(Items.TURTLE_SCUTE, 4), 1, 0, 0.1);
		add(LHTraits.PROTECTION.get(), loot1, new ItemStack(Items.SHULKER_SHELL, 1), 3, 0, 0.1);
		add(LHTraits.INVISIBLE.get(), loot1, new ItemStack(Items.PHANTOM_MEMBRANE, 4), 1, 0.25, 0);
		add(LHTraits.FIERY.get(), loot1, new ItemStack(Items.BLAZE_ROD, 8), 1, 0.25, 0);
		add(LHTraits.REGEN.get(), loot1, new ItemStack(Items.GHAST_TEAR, 4), 1, 0, 0.1);
		add(LHTraits.REGEN.get(), loot3, new ItemStack(LCMats.TOTEMIC_GOLD.getNugget(), 4), 3, 0, 0.1);
		add(LHTraits.REGEN.get(), loot2, new ItemStack(LCItems.LIFE_ESSENCE.get(), 1), 2, -0.1, 0.1);
		add(LHTraits.ADAPTIVE.get(), loot2, new ItemStack(LCItems.CURSED_DROPLET.get(), 1), 1, 0, 0.15);
		add(LHTraits.REFLECT.get(), loot2, new ItemStack(LCItems.EXPLOSION_SHARD.get(), 1), 1, 0, 0.1);
		add(LHTraits.DEMENTOR.get(), loot4, new ItemStack(LCItems.SUN_MEMBRANE.get(), 1), 1, 0.2, 0.1);
		add(LHTraits.DISPELL.get(), loot4, new ItemStack(LCItems.RESONANT_FEATHER.get(), 1), 1, 0.2, 0.1);
		add(LHTraits.UNDYING.get(), loot1, new ItemStack(Items.TOTEM_OF_UNDYING, 1), 1, 1, 0);
		add(LHTraits.UNDYING.get(), loot2, new ItemStack(LCItems.LIFE_ESSENCE.get(), 1), 1, 0.5, 0);
		add(LHTraits.ENDER.get(), loot4, new ItemStack(LCItems.VOID_EYE.get(), 1), 1, 0.2, 0.1);
		add(LHTraits.REPELLING.get(), loot3, new ItemStack(LCItems.FORCE_FIELD.get(), 1), 1, 0.2, 0.1);

		add(LHTraits.WEAKNESS.get(), loot1, new ItemStack(Items.FERMENTED_SPIDER_EYE, 8), 1, 0, 0.1);
		add(LHTraits.SLOWNESS.get(), loot1, new ItemStack(Items.COBWEB, 4), 1, 0, 0.1);
		add(LHTraits.POISON.get(), loot1, new ItemStack(Items.SPIDER_EYE, 8), 1, 0, 0.1);
		add(LHTraits.WITHER.get(), loot1, new ItemStack(Items.WITHER_ROSE, 8), 1, 0, 0.1);
		add(LHTraits.WITHER.get(), loot1, new ItemStack(Items.WITHER_SKELETON_SKULL, 1), 3, 0, 0.1);
		add(LHTraits.LEVITATION.get(), loot2, LCItems.CAPTURED_BULLET.asStack(), 1, 0, 0.1);
		add(LHTraits.BLIND.get(), loot1, new ItemStack(Items.INK_SAC, 8), 1, 0, 0.1);
		add(LHTraits.CONFUSION.get(), loot1, new ItemStack(Items.PUFFERFISH, 4), 1, 0, 0.1);
		add(LHTraits.SOUL_BURNER.get(), loot2, LCItems.SOUL_FLAME.asStack(2), 1, 0, 0.1);
		add(LHTraits.FREEZING.get(), loot2, LCItems.HARD_ICE.asStack(2), 1, 0, 0.1);
		add(LHTraits.CURSED.get(), loot1, getPotion(LCEffects.CURSE.key().location()), 1, 0, 0.2);
		add(LHTraits.CURSED.get(), loot2, LCItems.CURSED_DROPLET.asStack(), 2, 0, 0.05);
		add(LHTraits.CORROSION.get(), loot2, LCItems.CURSED_DROPLET.asStack(), 1, 0, 0.2);
		add(LHTraits.EROSION.get(), loot2, LCItems.CURSED_DROPLET.asStack(), 1, 0, 0.2);
		add(LHTraits.KILLER_AURA.get(), loot4, LCItems.EMERALD.asStack(), 1, 0, 0.02);
		add(LHTraits.RAGNAROK.get(), loot4, new ItemStack(LCMats.ETERNIUM.getNugget(), 4), 1, 0, 0.1);
		add(LHTraits.SHULKER.get(), loot2, LCItems.CAPTURED_BULLET.asStack(), 1, 0, 0.2);
		add(LHTraits.GRENADE.get(), loot3, LCItems.STORM_CORE.asStack(), 3, 0, 0.1);
		add(LHTraits.GRENADE.get(), loot1, new ItemStack(Items.GUNPOWDER, 4), 1, 0, 0.1);
		add(LHTraits.GRENADE.get(), loot1, new ItemStack(Items.CREEPER_HEAD, 1), 5, 0.25, 0);
		add(LHTraits.DRAIN.get(), loot2, LHItems.WITCH_DROPLET.asStack(), 1, 0, 0.1);
		add(LHTraits.GROWTH.get(), loot1, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1), 1, 0, 0.02);
		add(LHTraits.SPLIT.get(), loot2, LCItems.GUARDIAN_EYE.asStack(), 1, 0, 0.05);
		add(LHTraits.GRAVITY.get(), loot1, new ItemStack(Items.DRAGON_BREATH, 4), 1, 0, 0.1);
		add(LHTraits.MOONWALK.get(), loot1, new ItemStack(Items.DRAGON_BREATH, 4), 1, 0, 0.1);
		add(LHTraits.STRIKE.get(), loot2, LCItems.EXPLOSION_SHARD.asStack(), 1, 0, 0.1);
		add(LHTraits.PULLING.get(), loot3, LCItems.BLACKSTONE_CORE.asStack(), 1, 0, 0.05);

		add(LHTraits.REPRINT.get(), loot3, new ItemStack(LHItems.BOOK_COPY.get()), 1, 1, 0);
		add(LHTraits.DISPELL.get(), loot4, new ItemStack(LHItems.IMAGINE_BREAKER.get()), 3, 1, 0);

		add(LHTraits.ARENA.get(), loot4, new ItemStack(LHItems.CHAOS_INGOT.get()), 1, 0.5, 0);
		add(LHTraits.MASTER.get(), loot4, new ItemStack(LHItems.CHAOS_INGOT.get(), 4), 1, 1, 0);

		add(LHTraits.TANK.get(), new ItemStack(LCMats.SHULKERATE.getNugget(), 6), 0, 0.1,
				byPlayer().build(),
				new TraitLootCondition(LHTraits.TANK.get(), 3, 5),
				new TraitLootCondition(LHTraits.PROTECTION.get(), 1, 3),
				new PlayerHasItemCondition(loot2)
		);
		add(LHTraits.TANK.get(), new ItemStack(LCMats.SHULKERATE.getIngot(), 2), 0, 0.1,
				byPlayer().build(),
				new TraitLootCondition(LHTraits.TANK.get(), 3, 5),
				new TraitLootCondition(LHTraits.PROTECTION.get(), 4, 5),
				new PlayerHasItemCondition(loot2)
		);
		add(LHTraits.SPEEDY.get(), new ItemStack(LCMats.SCULKIUM.getNugget(), 4), 0, 0.1,
				byPlayer().build(),
				new TraitLootCondition(LHTraits.SPEEDY.get(), 3, 5),
				new TraitLootCondition(LHTraits.TANK.get(), 3, 5),
				new PlayerHasItemCondition(loot3)
		);

		add(LHTraits.DEMENTOR.get(), new ItemStack(LHItems.CHAOS_INGOT.get(), 1), 1, 0,
				byPlayer().build(),
				new TraitLootCondition(LHTraits.KILLER_AURA.get(), 1, 5),
				new TraitLootCondition(LHTraits.RAGNAROK.get(), 1, 5),
				new PlayerHasItemCondition(loot4)
		);

	}

	private ItemStack getPotion(ResourceLocation holder) {
		ItemStack stack = Items.POTION.getDefaultInstance();
		var potion = BuiltInRegistries.POTION.getHolder(holder).orElseThrow();
		stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
		return stack;
	}

	private void add(MobTrait trait, Item curio, ItemStack stack, int start, double chance, double bonus, int min) {
		add(trait, stack, chance, bonus,
				byPlayer().build(),
				new TraitLootCondition(trait, start, 5),
				new MobCapLootCondition(min),
				new PlayerHasItemCondition(curio)
		);
	}

	private void add(MobTrait trait, Item curio, ItemStack stack, int start, double chance, double bonus) {
		add(trait, stack, chance, bonus,
				byPlayer().build(),
				new TraitLootCondition(trait, start, 5),
				new PlayerHasItemCondition(curio)
		);
	}

	public static LootItemCondition.Builder byPlayer() {
		return LootItemKilledByPlayerCondition.killedByPlayer();
	}

	private void add(MobTrait trait, ItemStack stack, double chance, double bonus, LootItemCondition... conditions) {
		String name = trait.getRegistryName().getPath() + "_drop_" + BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
		add(name, new TraitLootModifier(trait, chance, bonus, stack, conditions));
	}

}
