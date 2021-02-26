package net.velinquish.utils.lang;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import net.velinquish.utils.Common;

@AllArgsConstructor
public class StringNode implements Node {
	private String value;
	private String prefix;

	public StringNode(String value) {
		this.value = value;
		prefix = " ";
	}

	@Override
	public Node replace(Map<String, String> replace) {
		return new StringNode(replacePlaceholders(replace), prefix);
	}

	private String replacePlaceholders(Map<String, String> replace) {
		String text = new String(value);
		for (String placeholder : replace.keySet())
			text = text.replaceAll(placeholder, replace.get(placeholder));
		return text;
	}

	@Override
	public void execute(CommandSender sender) {
		if (sender instanceof Player) {
			Common.tell(sender, value.replaceAll("%prefix%", prefix));
			return;
		}
		Common.tell(sender, value.replaceAll("%prefix%", prefix));
	}

	@Override
	public void execute(CommandSender sender, Map<String, String> replace) {
		if (sender instanceof Player) {
			Common.tell(sender, replacePlaceholders(replace));
			return;
		}
		Common.tell(sender, replacePlaceholders(replace));
	}

	@Override
	public String toString(Map<String, String> replace) {
		return replacePlaceholders(replace);
	}

	@Override
	public String toString() {
		return value;
	}
}
