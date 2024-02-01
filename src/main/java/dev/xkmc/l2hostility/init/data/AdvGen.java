package dev.xkmc.l2hostility.init.data;

import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.advancements.KillTraitCountTrigger;
import dev.xkmc.l2hostility.init.advancements.KillTraitEffectTrigger;
import dev.xkmc.l2hostility.init.advancements.KillTraitFlameTrigger;
import dev.xkmc.l2hostility.init.advancements.KillTraitsTrigger;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2library.serial.advancements.CriterionBuilder;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;

public class AdvGen {

	public static void genAdvancements(RegistrateAdvancementProvider pvd) {
		AdvancementGenerator gen = new AdvancementGenerator(pvd, L2Hostility.MODID);
		var root = gen.new TabBuilder("hostility").root("root", LHTraits.ENDER.get().asItem(),
				CriterionBuilder.item(LHItems.HOSTILITY_ORB.get()),
				"Welcome to L2Hostility", "Your survival guide").root();
		root.root().patchouli(L2Hostility.REGISTRATE, CriterionBuilder.item(LHItems.HOSTILITY_ORB.get()),
						new ResourceLocation(L2Hostility.MODID, "hostility_guide"),
						"Intro to L2Hostility", "Read the hostility guide")
				.root().create("detector", LHItems.DETECTOR.get(),
						CriterionBuilder.item(LHItems.DETECTOR.get()),
						"Safety Compass", "Obtain Hostility Detector")
				.create("glasses", LHItems.DETECTOR_GLASSES.get(),
						CriterionBuilder.item(LHItems.DETECTOR_GLASSES.get()),
						"The Invisible Threats", "Obtain Detector Glasses to find out invisible mobs")
				.root().create("kill_first", Items.IRON_SWORD,
						CriterionBuilder.one(KillTraitCountTrigger.ins(1)),
						"Worthy Opponent", "Kill a mob with traits")
				.create("kill_5_traits", Items.DIAMOND_SWORD,
						CriterionBuilder.one(KillTraitCountTrigger.ins(5)),
						"Legendary Battle", "Kill a mob with 5 traits")
				.type(FrameType.GOAL)
				.create("kill_10_traits", Items.NETHERITE_SWORD,
						CriterionBuilder.one(KillTraitCountTrigger.ins(10)),
						"Legend Slayer", "Kill a mob with 10 traits")
				.type(FrameType.CHALLENGE)
				.root().enter().create("kill_tanky", LHTraits.PROTECTION.get().asItem(),
						CriterionBuilder.one(KillTraitsTrigger.ins(
								LHTraits.PROTECTION.get(), LHTraits.TANK.get())),
						"Can Opener", "Kill a mob with Protection and Tanky Trait")
				.type(FrameType.GOAL)
				.create("kill_adaptive", LHTraits.ADAPTIVE.get().asItem(),
						CriterionBuilder.one(KillTraitsTrigger.ins(
								LHTraits.PROTECTION.get(), LHTraits.REGEN.get(), LHTraits.TANK.get(), LHTraits.ADAPTIVE.get())),
						"Counter-Defensive Measure", "Kill a mob with Protection, Regeneration, Tanky, and Adaptive Trait")
				.type(FrameType.CHALLENGE)
				.create("kill_dementor", LHTraits.DISPELL.get().asItem(),
						CriterionBuilder.one(KillTraitsTrigger.ins(
								LHTraits.DEMENTOR.get(), LHTraits.DISPELL.get())),
						"Immunity Invalidator", "Kill a mob with Dementor and Dispell Trait")
				.type(FrameType.CHALLENGE)
				.create("kill_ragnarok", LHTraits.RAGNAROK.get().asItem(),
						CriterionBuilder.one(KillTraitsTrigger.ins(
								LHTraits.KILLER_AURA.get(), LHTraits.RAGNAROK.get())),
						"The Final Battle", "Kill a mob with Killer Aura and Ragnarok Trait")
				.type(FrameType.CHALLENGE)
				.root().enter().create("effect_kill_regen", LHTraits.REGEN.get().asItem(),
						CriterionBuilder.one(KillTraitEffectTrigger.ins(
								LHTraits.REGEN.get(), LCEffects.CURSE.get())),
						"Prevent Healing", "Use curse effect on mobs with Regeneration and kill it")
				.type(FrameType.GOAL)
				.create("effect_kill_adaptive", LHTraits.ADAPTIVE.get().asItem(),
						CriterionBuilder.or().add(KillTraitEffectTrigger.ins(
										LHTraits.ADAPTIVE.get(), LCEffects.FLAME.get()))
								.add(KillTraitEffectTrigger.ins(
										LHTraits.ADAPTIVE.get(), MobEffects.POISON))
								.add(KillTraitEffectTrigger.ins(
										LHTraits.ADAPTIVE.get(), MobEffects.WITHER))
								.add(KillTraitFlameTrigger.ins(
										LHTraits.ADAPTIVE.get(), KillTraitFlameTrigger.Type.FLAME)),
						"Prevent Adaption", "Use poison/wither/soul flame effect or fire on mobs with Adaptive and kill it")
				.type(FrameType.GOAL)
				.create("effect_kill_undead", LHTraits.UNDYING.get().asItem(),
						CriterionBuilder.one(KillTraitEffectTrigger.ins(
								LHTraits.UNDYING.get(), LCEffects.CURSE.get())),
						"Prevent Reviving", "Use curse effect on mobs with Undying and kill it")
				.type(FrameType.CHALLENGE)
				.create("effect_kill_teleport", LHTraits.ENDER.get().asItem(),
						CriterionBuilder.one(KillTraitEffectTrigger.ins(
								LHTraits.ENDER.get(), LCEffects.STONE_CAGE.get())),
						"Prevent Teleporting", "Use incarceration effect on mobs with Teleport and kill it")
				.type(FrameType.CHALLENGE);
		var ingot = root.root().enter().create("ingot", LHItems.CHAOS_INGOT.get(),
				CriterionBuilder.item(LHItems.CHAOS_INGOT.get()),
				"Pandora's Box", "Obtain a Chaos Ingot");
		ingot.create("sloth", LHItems.CURSE_SLOTH.get(),
				CriterionBuilder.item(LHItems.CURSE_SLOTH.get()),
				"I want a break", "Obtain Curse of Sloth").type(FrameType.GOAL);
		var trait = ingot.create("envy", LHItems.CURSE_ENVY.get(),
						CriterionBuilder.item(LHItems.CURSE_ENVY.get()),
						"I want that!", "Obtain Curse of Envy")
				.type(FrameType.GOAL)
				.create("trait", LHTraits.TANK.get().asItem(),
						CriterionBuilder.item(LHTagGen.TRAIT_ITEM),
						"Gate to the New World", "Obtain a trait item");
		trait.create("greed", LHItems.CURSE_GREED.get(),
				CriterionBuilder.item(LHItems.CURSE_GREED.get()),
				"The More the Better", "Obtain Curse of Greed").type(FrameType.GOAL);
		trait.create("lust", LHItems.CURSE_LUST.get(),
				CriterionBuilder.item(LHItems.CURSE_LUST.get()),
				"Naked Corpse", "Obtain Curse of Lust").type(FrameType.GOAL);
		var miracle = trait.create("gluttony", LHItems.CURSE_GLUTTONY.get(),
						CriterionBuilder.item(LHItems.CURSE_GLUTTONY.get()),
						"Hostility Unlimited", "Obtain Curse of Gluttony")
				.create("miracle", LHItems.MIRACLE_INGOT.get(),
						CriterionBuilder.item(LHItems.MIRACLE_INGOT.get()),
						"Miracle of the World", "Obtain Miracle Ingot");
		trait.create("breed", LHTraits.REGEN.get().asItem(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(
								ItemPredicate.Builder.item().of(LHTagGen.TRAIT_ITEM).build())),
						"Breeding Mobs", "Use a trait item on mobs")
				.create("imagine_breaker", LHItems.IMAGINE_BREAKER.asStack(),
						CriterionBuilder.item(LHItems.IMAGINE_BREAKER.get()),
						"Reality Breakthrough", "Obtain Imagine Breaker").type(FrameType.CHALLENGE);
		miracle.create("wrath", LHItems.CURSE_WRATH.get(),
				CriterionBuilder.item(LHItems.CURSE_WRATH.get()),
				"Revenge Time", "Obtain Curse of Wrath").type(FrameType.CHALLENGE);
		miracle.create("pride", LHItems.CURSE_PRIDE.get(),
				CriterionBuilder.item(LHItems.CURSE_PRIDE.get()),
				"King of Hostility", "Obtain Curse of Pride").type(FrameType.CHALLENGE);
		miracle.create("abrahadabra", LHItems.ABRAHADABRA.get(),
				CriterionBuilder.item(LHItems.ABRAHADABRA.get()),
				"The Finale", "Obtain Abrahadabra").type(FrameType.CHALLENGE);
		root.finish();

	}

}
