package dev.xkmc.l2hostility.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.shadowsoffire.gateways.Gateways;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2core.compat.patchouli.PatchouliHelper;
import dev.xkmc.l2core.init.L2TagGen;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.ConfigTypeEntry;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2core.serial.config.RegistrateNestedProvider;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2hostility.compat.gateway.GatewayConfigGen;
import dev.xkmc.l2hostility.compat.gateway.GatewayToEternityCompat;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkCapSyncToClient;
import dev.xkmc.l2hostility.content.capability.mob.MobCapSyncToClient;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.events.LHAttackListener;
import dev.xkmc.l2hostility.init.advancements.HostilityTriggers;
import dev.xkmc.l2hostility.init.data.*;
import dev.xkmc.l2hostility.init.entries.LHRegistrate;
import dev.xkmc.l2hostility.init.loot.TraitGLMProvider;
import dev.xkmc.l2hostility.init.network.LootDataToClient;
import dev.xkmc.l2hostility.init.network.TraitEffectToClient;
import dev.xkmc.l2hostility.init.registrate.*;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Hostility.MODID)
@EventBusSubscriber(modid = L2Hostility.MODID, bus = EventBusSubscriber.Bus.MOD)
public class L2Hostility {

	public static final String MODID = "l2hostility";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			MODID, 2,
			e -> e.create(MobCapSyncToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(TraitEffectToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(LootDataToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(ChunkCapSyncToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT)
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final LHRegistrate REGISTRATE = new LHRegistrate(MODID);
	public static final PatchouliHelper PATCHOULI = new PatchouliHelper(REGISTRATE, "hostility_guide");

	public static final ConfigTypeEntry<WorldDifficultyConfig> DIFFICULTY = new ConfigTypeEntry<>(HANDLER, "difficulty", WorldDifficultyConfig.class);
	public static final ConfigTypeEntry<WeaponConfig> WEAPON = new ConfigTypeEntry<>(HANDLER, "weapon", WeaponConfig.class);
	public static final ConfigTypeEntry<EntityConfig> ENTITY = new ConfigTypeEntry<>(HANDLER, "entity", EntityConfig.class);

	public L2Hostility() {

		LHBlocks.register();
		LHItems.register();
		LHEffects.register();
		LHTraits.register();
		LHEntities.register();
		LHMiscs.register();
		LHConfig.init();
		LHEnchantments.register();
		TraitGLMProvider.register();
		HostilityTriggers.register();

		PATCHOULI.buildModel().buildShapelessRecipe(e -> e
								.requires(Items.BOOK).requires(Items.ROTTEN_FLESH).requires(Items.BONE),
						() -> Items.BOOK)
				.buildBook("L2Hostility Guide",
						"Find out the mechanics and mob traits to know what to prepare for",
						1, LHBlocks.TAB.key());
		AttackEventHandler.register(4500, new LHAttackListener());
		if (ModList.get().isLoaded(Gateways.MODID)) {
			NeoForge.EVENT_BUS.register(GatewayToEternityCompat.class);
		}
	}

	@SubscribeEvent
	public static void modifyAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, LHMiscs.ADD_LEVEL);
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {

		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::addTranslations);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, LHTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(L2TagGen.ENCH_TAGS, LHTagGen::onEnchTagGen);
		REGISTRATE.addDataGenerator(L2TagGen.EFF_TAGS, LHTagGen::onEffTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, LHTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, LHTagGen::onEntityTagGen);
		REGISTRATE.addDataGenerator(LHTraits.TRAIT_TAGS, LHTagGen::onTraitTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, AdvGen::genAdvancements);
		REGISTRATE.addDataGenerator(RegistrateNestedProvider.TYPE, pvd -> pvd.add(LHConfigGen::new));

		boolean server = event.includeServer();
		PackOutput output = event.getGenerator().getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		var gen = event.getGenerator();
		gen.addProvider(server, new TraitGLMProvider(output, pvd));
		gen.addProvider(server, new SlotGen(MODID, output, helper, pvd));
		var init = REGISTRATE.getDataGenInitializer();
		init.addDependency(ProviderType.RECIPE, ProviderType.DYNAMIC);
		init.addDependency(RegistrateNestedProvider.TYPE, ProviderType.DYNAMIC);
		LHEnchantments.DC.addParent(LCEnchantments.REG);
		new LHDamageTypes().generate();
		if (ModList.get().isLoaded(Gateways.MODID)) {
			gen.addProvider(server, new GatewayConfigGen(gen));
		}
	}

	public static void toTrackingChunk(LevelChunk chunk, SerialPacketBase<?> packet) {
		if (chunk.getLevel() instanceof ServerLevel sl)
			HANDLER.toTrackingChunk(sl, chunk.getPos(), packet);
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
