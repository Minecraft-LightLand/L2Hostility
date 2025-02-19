package dev.xkmc.l2hostility.init.data;

import com.github.L_Ender.cataclysm.Cataclysm;
import dev.xkmc.l2hostility.compat.data.CataclysmData;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class SlotGen extends CuriosDataProvider {

	public SlotGen(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
		super(modId, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider ovd, ExistingFileHelper helper) {
		createSlot("hostility_curse").icon(L2Hostility.loc("slot/empty_hostility_slot")).order(131);
		createEntities("hostility_entity").addEntities(EntityType.PLAYER).addSlots("head", "charm", "ring", "hands", "hostility_curse");

		if (ModList.get().isLoaded(Cataclysm.MODID)) {
			CataclysmData.genSlot(this);
		}
	}

}