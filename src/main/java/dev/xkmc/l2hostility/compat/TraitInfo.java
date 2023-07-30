package dev.xkmc.l2hostility.compat;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class TraitInfo implements IEntityComponentProvider {

	public static final ResourceLocation ID = new ResourceLocation(L2Hostility.MODID, "mob");

	@Override
	public void appendTooltip(ITooltip list, EntityAccessor entity, IPluginConfig config) {
		if (entity.getEntity() instanceof LivingEntity le) {
			if (MobTraitCap.HOLDER.isProper(le)) {
				list.addAll(MobTraitCap.HOLDER.get(le).getTitle(true));
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return ID;
	}

}
