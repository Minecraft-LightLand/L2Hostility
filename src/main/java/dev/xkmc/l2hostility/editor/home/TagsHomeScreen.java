package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.tag.HostilityTagUtil;
import dev.xkmc.l2hostility.editor.tag.TagEditScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TagsHomeScreen extends HostilityHomeScreen {

	public TagsHomeScreen(Screen parent) {
		super(HostilityEditorLang.TAGS.get(), 4, parent);
	}

	@Override
	protected boolean canCreate() {
		return false;
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		return HostilityEditorUtil.listManagedTags();
	}

	@Override
	protected int fileCount(ResourceLocation id) {
		return HostilityTagUtil.load(id).size();
	}

	@Override
	protected Component emptyMessage() {
		return HostilityEditorLang.TAG_EMPTY.get();
	}

	@Override
	protected String newFileDefault() {
		return "l2hostility:new";
	}

	@Override
	protected void openNew(ResourceLocation id) {
		EditorToast.show(EditorText.NEW.get(), EditorText.NO_FILE.get());
	}

	@Override
	protected void openEdit(ResourceLocation id) {
		Minecraft.getInstance().setScreen(new TagEditScreen(id, this));
	}

}