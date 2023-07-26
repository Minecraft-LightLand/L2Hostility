package dev.xkmc.l2complements.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2complements.init.data.*;
import dev.xkmc.l2complements.init.registrate.*;
import dev.xkmc.l2damagetracker.contents.materials.vanilla.GenItemVanillaType;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Hostility.MODID)
@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2Hostility {

	public static final String MODID = "l2hostility";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(MODID, "main"), 1
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);
	public static final GenItemVanillaType MATS = new GenItemVanillaType(MODID, REGISTRATE);

	private static void registerRegistrates(IEventBus bus) {
		ForgeMod.enableMilkFluid();
		LHItems.register();
		LHBlocks.register();
		LHEffects.register();
		LHParticle.register();
		LHEnchantments.register();
		LHEntities.register();
		LHRecipes.register(bus);
		LHConfig.init();

		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::addTranslations);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, TagGen::onEntityTagGen);
	}

	public L2Hostility() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		registerRegistrates(bus);
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {

		});
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		boolean gen = event.includeServer();
		PackOutput output = event.getGenerator().getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		event.getGenerator().addProvider(gen, new LHConfigGen(event.getGenerator()));
	}

}
