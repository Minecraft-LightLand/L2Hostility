package dev.xkmc.l2hostility.editor.base;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public interface EditorHandler<T> extends PickListScreen.Handler<T>, ItemListScreen.Handler<T>,
		DoubleMapScreen.Handler<T>, Obj2IntMapScreen.Handler<T> {

	@Override
	@Nullable
	default ItemStack icon(T t) {
		return null;
	}

	@Override
	default boolean percent(T t) {
		return false;
	}

	@Override
	default int maxLevel(T t) {
		return 100;
	}

	@Override
	default void onSelect(T t) {
	}

	static <T> EditorHandler<T> of(Function<T, Component> label, @Nullable Function<T, ItemStack> icon) {
		return new Impl<>(label, icon);
	}

	record Impl<T>(Function<T, Component> label, @Nullable Function<T, ItemStack> icon) implements EditorHandler<T> {

		@Override
		public Component label(T t) {
			return label.apply(t);
		}

		@Override
		@Nullable
		public ItemStack icon(T t) {
			return icon == null ? null : icon.apply(t);
		}

	}

	record Pick<T>(EditorHandler<T> handler, Consumer<T> onSelect) implements PickListScreen.Handler<T> {

		public static <T> Pick<T> of(EditorHandler<T> handler, Consumer<T> onSelect) {
			return new Pick<>(handler, onSelect);
		}

		@Override
		public Component label(T t) {
			return handler.label(t);
		}

		@Override
		@Nullable
		public ItemStack icon(T t) {
			return handler.icon(t);
		}

		@Override
		public void onSelect(T t) {
			onSelect.accept(t);
		}

	}

}
