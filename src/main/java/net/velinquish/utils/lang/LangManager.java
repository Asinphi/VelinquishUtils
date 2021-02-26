package net.velinquish.utils.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Setter;

public class LangManager {
	private Map<String, Node> nodes = new HashMap<>();
	private Node defaultNode = new StringNode("", " ");
	@Setter
	private String prefix;

	public void loadLang(YamlConfiguration lang) {
		for (String key : lang.getKeys(false))
			if (lang.isString(key))
				nodes.put(key, new StringNode(lang.getString(key), prefix));
			else if (lang.isList(key)) {
				List<?> list = lang.getList(key);
				if (!list.isEmpty())
					if (list.get(0) instanceof Map<?, ?>)
						nodes.put(key, new MapListNode(lang.getMapList(key)));
					else
						nodes.put(key,  new StringListNode(lang.getStringList(key)));
			} else
				//testing lines
				if (lang.isBoolean(key))
					nodes.put(key, new StringNode(Boolean.toString(lang.getBoolean(key)), prefix));
				else if (lang.isInt(key))
					nodes.put(key, new StringNode(Integer.toString(lang.getInt(key)), prefix));
				else
					nodes.put(key, new StringNode(String.valueOf(lang.get(key)), prefix));
	}

	public Node getNode(String key) {
		Node node = nodes.get(key);
		if (node == null)
			return defaultNode;
		return node;
	}

	public void clear() {
		nodes.clear();
	}
}
