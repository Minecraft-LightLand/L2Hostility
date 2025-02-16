package dev.xkmc.l2hostility.compat.kubejs;

import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
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

	@Override
	public void generateAssets(KubeAssetGenerator generator) {
		generator.itemModel(this.id, (m) -> {
			if (this.modelGenerator != null) {
				this.modelGenerator.accept(m);
			} else {
				m.parent(this.parentModel != null ? this.parentModel : KubeAssetGenerator.GENERATED_ITEM_MODEL);
				if (this.textures.isEmpty()) {
					m.texture("layer0", "l2hostility:item/bg");
					m.texture("layer1", newID("item/", "").toString());
				} else {
					m.textures(this.textures);
				}

			}
		});
	}

}
