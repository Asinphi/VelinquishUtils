package net.velinquish.utils.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import net.velinquish.utils.Common;

public class StringListNode implements Node {
	private List<String> value;

	public StringListNode(List<String> list) {
		value = new ArrayList<>();
		for (String line : list)
			value.add(line);
	}

	@Override
	public Node replace(Map<String, String> replace) {
		List<String> list = new ArrayList<>();
		for (String line : value)
			list.add(replacePlaceholders(line, replace));
		return new StringListNode(list);
	}

	private String replacePlaceholders(String str, Map<String, String> replace) {
		String text = new String(str);
		for (String placeholder : replace.keySet())
			text = text.replaceAll(placeholder, replace.get(placeholder));
		return text;
	}

	@Override
	public void execute(CommandSender sender) {
		for (String str : value)
			Common.tell(sender, str);
	}

	@Override
	public void execute(CommandSender sender, Map<String, String> replace) {
		for (String str : value)
			Common.tell(sender, replacePlaceholders(str, replace));
	}

	@Override
	public String toString(Map<String, String> replace) {
		String str = "";
		for (int i = 0; i < value.size(); i++)
			str += value.get(i) + (i != value.size() - 1 ? "\n" : "");
		return replacePlaceholders(str, replace);
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < value.size(); i++)
			str += value.get(i) + (i != value.size() - 1 ? "\n" : "");
		return str;
	}
}
