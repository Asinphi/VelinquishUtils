package net.velinquish.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

// This is the common library
public class Common {

	@Setter
	@Getter
	private static JavaPlugin instance;

	public static String colorize(String message) {
		if (message == null)
			return "";
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static List<String> colorize(List<String> messages) {
		List<String> lines = new ArrayList<>();
		for (String str : messages)
			lines.add(colorize(str));
		return lines;
	}

	public static <T> T getOrDefault(T nullable, T def) {
		return nullable != null ? nullable : def;
	}

	public static void log(String... messages) {
		for (final String message : messages)
			log(message);
	}

	public static void log(String messages) {
		tell(Bukkit.getConsoleSender(), "[" + instance.getName() + "] " + messages);
	}

	public static void registerCommand(Command command) {
		try {
			final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
			commandMap.register(command.getLabel(), command);

		} catch(final Exception e) {
			e.printStackTrace();
		}
	}

	public static void runLater(int delay, BukkitRunnable task) {
		runLater(delay, task);
	}

	public static void runLater(int delay, Runnable task) {
		Bukkit.getScheduler().runTaskLater(instance, task, delay);
	}

	public static void sendBar(Player pl, String title) {
		try {
			pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colorize(title)));

		} catch (final Throwable t) {
			tell(pl, title);
		}
	}

	public static void sendTitle(Player pl, String title, String subtitle) {
		pl.sendTitle(colorize(title), colorize(subtitle), 20, 3 * 20, 10);
	}

	public static void tell(CommandSender toWhom, String... messages) {
		for (final String message : messages)
			tell(toWhom, message);
	}

	public static void tell(CommandSender toWhom, String message) {
		toWhom.sendMessage(colorize(message));
	}

	public static Map<String, String> map(String... replace) {
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < replace.length; i += 2)
			map.put(replace[i], replace[i + 1]);
		return map;
	}

	public static Map<?, ?> replace(Map<?, ?> original, Map<String, String> replace) {
		Map<String, String> map = new HashMap<>();
		for (Object key : original.keySet())
			for (String placeholder : replace.keySet())
				map.put((String) key, ((String) original.get(key)).replaceAll(placeholder, replace.get(placeholder)));
		return map;
	}
}
