package net.velinquish.utils.lang;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.velinquish.utils.Common;
import net.velinquish.utils.EasyComponent;

public class MapNode implements Node {
	private Map<?, ?> value;
	private EasyComponent component;
	private String consoleMessage;

	@Override
	public Node replace(Map<String, String> replace) {
		return new MapNode(Common.replace(value, replace));
	}

	protected MapNode(Map <?, ?> map) {
		value = map;
		component = new EasyComponent(((String) map.get("message")))
				.onClickRunCmd((String) map.get("run"))
				.onClickSuggestCmd((String) map.get("suggest"))
				.onHover((String) map.get("hover"));
		consoleMessage = ((String) map.get("message"));
	}

	private String replacePlaceholders(String str, Map<String, String> replace) {
		for (String placeholder : replace.keySet())
			str = str.replaceAll(placeholder, replace.get(placeholder));
		return str;
	}


	@Override
	public void execute(CommandSender sender) {
		if (sender instanceof Player)
			component.send((Player) sender);
		else
			Common.tell(sender, consoleMessage);
	}

	@Override
	public void execute(CommandSender sender, Map<String, String> replace) {
		if (sender instanceof Player)
			new EasyComponent((String) value.get("message"))
			.onClickRunCmd(replacePlaceholders((String) value.get("run"), replace))
			.onClickSuggestCmd(replacePlaceholders((String) value.get("suggest"), replace))
			.onHover(replacePlaceholders((String) value.get("hover"), replace))
			.send((Player) sender);
		else
			Common.tell(sender, replacePlaceholders(consoleMessage, replace));
	}

	@Override
	public String toString(Map<String, String> replace) {
		return replacePlaceholders(consoleMessage, replace);
	}

	@Override
	public String toString() {
		return consoleMessage;
	}
}
