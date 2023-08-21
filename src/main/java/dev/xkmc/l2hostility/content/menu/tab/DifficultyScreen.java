package dev.xkmc.l2hostility.content.menu.tab;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.init.L2HostilityClient;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class DifficultyScreen extends BaseTextScreen {

	protected DifficultyScreen(Component title) {
		super(title, new ResourceLocation("l2tabs:textures/gui/empty.png"));
	}

	public void init() {
		super.init();
		new TabManager(this).init(this::addRenderableWidget, L2HostilityClient.TAB_DIFFICULTY);
	}

	public void render(GuiGraphics g, int mx, int my, float ptick) {
		super.render(g, mx, my, ptick);
		int x = this.leftPos + 8;
		int y = this.topPos + 6;
		List<Component> list = new ArrayList<>();
		add(list);
		for (Component c : list) {
			g.drawString(this.font, c, x, y += 10, 0, false);
		}
	}

	private void add(List<Component> list) {
		Player player = Minecraft.getInstance().player;
		assert player != null;
		PlayerDifficulty cap = PlayerDifficulty.HOLDER.get(player);
		list.add(LangData.INFO_PLAYER_LEVEL.get(cap.getLevel().getLevel()));
		int perc = Math.round(100f * cap.getLevel().getExp() / cap.getLevel().getMaxExp());
		list.add(LangData.INFO_PLAYER_EXP.get(perc));
		list.add(LangData.INFO_PLAYER_CAP.get(cap.getRankCap()));
		var opt = ChunkDifficulty.at(player.level(), player.blockPosition());
		if (opt.isPresent()) {
			ChunkDifficulty chunk = opt.get();
			MobDifficultyCollector ins = new MobDifficultyCollector();
			chunk.modifyInstance(player.blockPosition(), ins);
			list.add(LangData.INFO_CHUNK_LEVEL.get(ins.base));
			list.add(LangData.INFO_CHUNK_SCALE.get(ins.scale));
		}
	}

}
