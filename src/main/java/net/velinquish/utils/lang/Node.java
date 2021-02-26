package net.velinquish.utils.lang;

import java.util.Map;

import org.bukkit.command.CommandSender;

public interface Node {
	void execute(CommandSender sender);
	void execute(CommandSender sender, Map<String, String> replace);
	@Override
	String toString();
	String toString(Map<String, String> replace);
	Node replace(Map<String, String> replace);
}
