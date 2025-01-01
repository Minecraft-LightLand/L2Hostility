package dev.xkmc.l2hostility.compat.kubejs;

import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.xkmc.l2hostility.content.item.traits.TraitSymbol;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class TraitItemBuilder extends ItemBuilder {

	public TraitItemBuilder(ResourceLocation i) {
		super(i);
	}

	@Override
	public Item createObject() {
		return new TraitSymbol(new Item.Properties());
	}

	public void generateAssetJsons(AssetJsonGenerator generator) {
		if (this.modelJson != null) {
			generator.json(AssetJsonGenerator.asItemModelLocation(this.id), this.modelJson);
		} else {
			generator.itemModel(this.id, (m) -> {
				if (!this.parentModel.isEmpty()) {
					m.parent(this.parentModel);
				} else {
					m.parent("minecraft:item/generated");
				}
				if (this.textureJson.size() == 0) {
					this.textureJson.addProperty("layer0", "l2hostility:item/bg");
					this.textureJson.addProperty("layer1", newID("item/", "").toString());
				}
				m.textures(this.textureJson);
			});
		}
	}

}
