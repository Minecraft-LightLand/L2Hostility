package dev.xkmc.l2hostility.editor.tag;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record TagValue(String id, boolean required, boolean isTag) {

	public static TagValue parse(JsonElement e) {
		if (e.isJsonPrimitive()) {
			String s = e.getAsString();
			return new TagValue(s.startsWith("#") ? s.substring(1) : s, true, s.startsWith("#"));
		}
		if (e.isJsonObject()) {
			JsonObject obj = e.getAsJsonObject();
			String s = obj.has("id") ? obj.get("id").getAsString() : e.getAsString();
			boolean req = !obj.has("required") || obj.get("required").getAsBoolean();
			return new TagValue(s.startsWith("#") ? s.substring(1) : s, req, s.startsWith("#"));
		}
		return new TagValue(e.getAsString(), true, false);
	}

	public JsonElement toJson() {
		String raw = isTag ? "#" + id : id;
		if (required) return new JsonPrimitive(raw);
		JsonObject obj = new JsonObject();
		obj.addProperty("id", raw);
		obj.addProperty("required", false);
		return obj;
	}

	public MutableComponent toComponent() {
		MutableComponent c = isTag
				? Component.literal("#" + id).withStyle(ChatFormatting.GOLD)
				: Component.literal(id);
		if (!required) {
			c = c.copy().append(Component.literal(" (optional)").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		}
		return c;
	}

}
