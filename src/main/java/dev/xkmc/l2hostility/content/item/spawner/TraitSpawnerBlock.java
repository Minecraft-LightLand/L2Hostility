package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Locale;

public class TraitSpawnerBlock {

	public enum State implements StringRepresentable {
		IDLE(7),
		ACTIVATED(11),
		CLEAR(15),
		FAILED(11);

		private final int light;

		State(int light) {
			this.light = light;
		}

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}

		public int light() {
			return light;
		}
	}

	public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);

	public static final BlockEntityBlockMethodImpl<BurstSpawnerBlockEntity> BE_BURST =
			new BlockEntityBlockMethodImpl<>(LHBlocks.BE_BURST, BurstSpawnerBlockEntity.class);

	public static final BaseTraitMethod BASE = new BaseTraitMethod();
	public static final BurstTraitMethod BURST = new BurstTraitMethod();
	public static final ClickTraitMethod CLICK = new ClickTraitMethod();

}
