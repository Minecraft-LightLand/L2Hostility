package dev.xkmc.l2hostility.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.shadowsoffire.gateways.Gateways;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2hostility.compat.gateway.GatewayConfigGen;
import dev.xkmc.l2hostility.compat.gateway.GatewayToEternityCompat;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkCapSyncToClient;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.MobCapSyncToClient;
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
import dev.xkmc.l2library.compat.patchouli.PatchouliHelper;
import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
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
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(MODID, "main"), 1,
			e -> e.create(MobCapSyncToClient.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(TraitEffectToClient.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(LootDataToClient.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(ChunkCapSyncToClient.class, NetworkDirection.PLAY_TO_CLIENT)
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final LHRegistrate REGISTRATE = new LHRegistrate(MODID);

	public static final ConfigTypeEntry<WorldDifficultyConfig> DIFFICULTY = new ConfigTypeEntry<>(HANDLER, "difficulty", WorldDifficultyConfig.class);
	public static final ConfigTypeEntry<TraitConfig> TRAIT = new ConfigTypeEntry<>(HANDLER, "trait", TraitConfig.class);
	public static final ConfigTypeEntry<WeaponConfig> WEAPON = new ConfigTypeEntry<>(HANDLER, "weapon", WeaponConfig.class);
	public static final ConfigTypeEntry<EntityConfig> ENTITY = new ConfigTypeEntry<>(HANDLER, "entity", EntityConfig.class);

	public L2Hostility() {

		LHBlocks.register();
		LHItems.register();
		LHTraits.register();
		LHEntities.register();
		LHMiscs.register();
		LHConfig.init();
		LHDamageTypes.register();
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
		REGISTRATE.addDataGenerator(TagGen.EFF_TAGS, LHTagGen::onEffTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, LHTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, LHTagGen::onEntityTagGen);
		REGISTRATE.addDataGenerator(LHTraits.TRAIT_TAGS, LHTagGen::onTraitTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, AdvGen::genAdvancements);


		new PatchouliHelper(REGISTRATE, "hostility_guide")
				.buildModel().buildShapelessRecipe(e -> e
								.requires(Items.BOOK).requires(Items.ROTTEN_FLESH).requires(Items.BONE),
						() -> Items.BOOK)
				.buildBook("L2Hostility Guide",
						"Find out the mechanics and mob traits to know what to prepare for",
						1, LHBlocks.TAB.getKey());
		AttackEventHandler.register(4500, new LHAttackListener());

		if (ModList.get().isLoaded(Gateways.MODID)) {
			MinecraftForge.EVENT_BUS.register(GatewayToEternityCompat.class);
		}
	}


	@SubscribeEvent
	public static void modifyAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, LHMiscs.ADD_LEVEL.get());
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {

		});
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		boolean server = event.includeServer();
		PackOutput output = event.getGenerator().getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		var gen = event.getGenerator();
		gen.addProvider(server, new TraitGLMProvider(gen));
		gen.addProvider(server, new SlotGen(gen));
		new LHDamageTypes(output, pvd, helper).generate(server, gen);
		if (ModList.get().isLoaded(Gateways.MODID)) {
			gen.addProvider(server, new GatewayConfigGen(gen));
		}
		gen.addProvider(server, new LHConfigGen(gen));
	}

	public static void toTrackingChunk(LevelChunk chunk, SerialPacketBase packet) {
		HANDLER.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), packet);
	}

}
