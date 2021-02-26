package net.velinquish.utils.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.velinquish.utils.Common;
import net.velinquish.utils.EasyComponent;

public class MapListNode implements Node {


	private List<Map<?, ?>> value;
	private List<EasyComponent> components;
	private List<String> consoleMessage;

	@Override
	public Node replace(Map<String, String> replace) {
		List<Map<?, ?>> list = new ArrayList<>();
		for (Map<?, ?> map : value)
			list.add(Common.replace(map, replace));
		return new MapListNode(list);
	}

	private EasyComponent build(EasyComponent component, Map<?, ?> map) {
		component.append((String) map.get("message"))
		.onClickRunCmd((String) map.get("run"))
		.onClickSuggestCmd((String) map.get("suggest"))
		.onHover((String) map.get("hover"));
		return component;
	}

	private EasyComponent build(EasyComponent component, Map<?, ?> map, Map<String, String> replace) {
		component.append(replacePlaceholders((String) map.get("message"), replace))
		.onClickRunCmd(replacePlaceholders((String) map.get("run"), replace))
		.onClickSuggestCmd(replacePlaceholders((String) map.get("suggest"), replace))
		.onHover(replacePlaceholders((String) map.get("hover"), replace));
		return component;
	}

	protected MapListNode(List<Map<?, ?>> value) {
		this.value = value;
		components = new ArrayList<>();
		consoleMessage = new ArrayList<>();
		for (Map<?, ?> map : value)
			if (components.isEmpty() || (Boolean) map.get("new-line")) {
				components.add(build(new EasyComponent(""), map));
				consoleMessage.add(((String) map.get("message")));
			} else {
				components.set(components.size() - 1, build(components.get(components.size() - 1), map));
				consoleMessage.set(consoleMessage.size() -1, consoleMessage.get(consoleMessage.size() - 1) + ((String) map.get("message")));
			}
	}

	private String replacePlaceholders(String str, Map<String, String> replace) {
		for (String placeholder : replace.keySet())
			str = str.replaceAll(placeholder, replace.get(placeholder));
		return str;
	}

	@Override
	public void execute(CommandSender sender) {
		if (sender instanceof Player)
			for (EasyComponent component : components)
				component.send((Player) sender);
		else
			for (String str : consoleMessage)
				Common.tell(sender, str);
	}

	@Override
	public void execute(CommandSender sender, Map<String, String> replace) {
		List<EasyComponent> toSend = new ArrayList<>();
		if (sender instanceof Player) {
			for (Map<?, ?> map : value)
				if (components.isEmpty() || (Boolean) map.get("new-line"))
					components.add(build(new EasyComponent(""), map, replace));
				else
					components.set(components.size() - 1, build(components.get(components.size() - 1), map, replace));
			for (EasyComponent component : toSend)
				component.send((Player) sender);
		}
		else
			for (String str : consoleMessage)
				Common.tell(sender, replacePlaceholders(str, replace));
	}

	@Override
	public String toString(Map<String, String> replace) {
		String str = "";
		for (int i = 0; i < consoleMessage.size(); i++)
			str += replacePlaceholders(consoleMessage.get(i) + (i != consoleMessage.size() - 1 ? "\n" : ""), replace);
		return str;
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < consoleMessage.size(); i++)
			str += consoleMessage.get(i) + (i != consoleMessage.size() - 1 ? "\n" : "");
		return str;
	}
}
