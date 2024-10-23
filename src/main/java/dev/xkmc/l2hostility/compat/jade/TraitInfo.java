package dev.xkmc.l2hostility.compat.jade;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class TraitInfo implements IEntityComponentProvider {

	public static final ResourceLocation ID = L2Hostility.loc("mob");

	@Override
	public void appendTooltip(ITooltip list, EntityAccessor entity, IPluginConfig config) {
		if (entity.getEntity() instanceof LivingEntity le) {
			var opt = LHMiscs.MOB.type().getExisting(le);
			opt.ifPresent(cap -> list.addAll(cap.getTitle(true, true)));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return ID;
	}

}
