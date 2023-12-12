package dev.xkmc.l2hostility.init;

import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2complements.network.ArmorEffectConfig;
import dev.xkmc.l2complements.network.NetworkManager;
import dev.xkmc.l2hostility.backport.config.ConfigTypeEntry;
import dev.xkmc.l2hostility.content.capability.chunk.CapSyncToClient;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.InfoRequestToServer;
import dev.xkmc.l2hostility.content.capability.mob.CapSyncPacket;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.TraitConfig;
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
import dev.xkmc.l2library.init.events.attack.AttackEventHandler;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.l2library.serial.network.PacketHandlerWithConfig;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Hostility.MODID)
@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2Hostility {

	public static final String MODID = "l2hostility";

	private static final String PATH = "l2hostility_config";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(MODID, "main"), 1, PATH,
			e -> e.create(CapSyncPacket.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(TraitEffectToClient.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(LootDataToClient.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(InfoRequestToServer.class, NetworkDirection.PLAY_TO_SERVER),
			e -> e.create(CapSyncToClient.class, NetworkDirection.PLAY_TO_CLIENT)
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final LHRegistrate REGISTRATE = new LHRegistrate(MODID);

	public static final ConfigTypeEntry<WorldDifficultyConfig> DIFFICULTY = new ConfigTypeEntry<>(HANDLER, PATH, "difficulty", WorldDifficultyConfig.class);
	public static final ConfigTypeEntry<TraitConfig> TRAIT = new ConfigTypeEntry<>(HANDLER, PATH, "trait", TraitConfig.class);
	public static final ConfigTypeEntry<WeaponConfig> WEAPON = new ConfigTypeEntry<>(HANDLER, PATH, "weapon", WeaponConfig.class);
	public static final ConfigTypeEntry<EntityConfig> ENTITY = new ConfigTypeEntry<>(HANDLER, PATH, "entity", EntityConfig.class);
	public static final ConfigTypeEntry<ArmorEffectConfig> ARMOR = new ConfigTypeEntry<>(NetworkManager.HANDLER, "l2complements",
			NetworkManager.ARMOR.getID(), ArmorEffectConfig.class);

	public L2Hostility() {

		LHBlocks.register();
		LHItems.register();
		LHTraits.register();
		LHEntities.register();
		LHMiscs.register();
		LHConfig.init();
		LHEnchantments.register();
		LHEffects.register();

		TraitGLMProvider.register();

		MobTraitCap.register();
		ChunkDifficulty.register();
		PlayerDifficulty.register();

		HostilityTriggers.register();

		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::addTranslations);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, LHTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(LHTagGen.ENCH_TAGS, LHTagGen::onEnchTagGen);
		//TODO REGISTRATE.addDataGenerator(TagGen.EFF_TAGS, LHTagGen::onEffTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, LHTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, LHTagGen::onEntityTagGen);
		REGISTRATE.addDataGenerator(LHTraits.TRAIT_TAGS, LHTagGen::onTraitTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, AdvGen::genAdvancements);

		AttackEventHandler.LISTENERS.add(new LHAttackListener());
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {

		});
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		boolean server = event.includeServer();
		var gen = event.getGenerator();
		gen.addProvider(server, new LHConfigGen(gen));
		gen.addProvider(server, new TraitGLMProvider(gen));
	}

	public static void toTrackingChunk(LevelChunk chunk, SerialPacketBase packet) {
		HANDLER.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), packet);
	}

}
