package net.velinquish.utils;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.velinquish.utils.lang.Node;

public abstract class AnyCommand {
	private VelinquishPlugin plugin = VelinquishPlugin.getInstance();

	private CommandSender sender;
	@Setter(AccessLevel.PROTECTED)
	private String[] args;
	private boolean isPlayer;
	private boolean silent;

	public boolean execute(CommandSender sender, String[] args, boolean silent) {
		this.sender = sender;
		this.args = args;
		isPlayer = sender instanceof Player;
		this.silent = silent;
		try {
			run(sender, args, silent);
		} catch (ReturnedCommandException ex) {
			if (!silent)
				ex.getNode().execute(sender);
		}
		return isPlayer;
	}

	protected boolean isPlayer() { return isPlayer; }

	protected abstract void run(CommandSender sender, String[] args, boolean silent);

	protected void tell(Node node) {
		if (!silent)
			node.execute(sender);
	}

	protected void tellRaw(String message) {
		Common.tell(sender, message);
	}

	protected void overrideSilent(Node node) {
		node.execute(sender);
	}

	protected void returnTell(Node node) {
		throw new ReturnedCommandException(node);
	}

	protected String prefix(String str) {
		return str.replaceAll("%prefix%", plugin.getPrefix());
	}

	protected int getNumber(int index, int to, int from, Node node) {
		int num = 0;
		try {
			num = Integer.parseInt(args[index]);
			Validate.isTrue(num >= from && num <= to);
		} catch (final IllegalArgumentException ex) {
			returnTell(node);
		}
		return num;
	}

	protected Location getLocation(int index, Node usage, Node consoleUsage) {
		if (args.length <= index)
			if (isPlayer())
				return ((Player) sender).getLocation();
			else
				returnTell(consoleUsage);
		double x = 0, y = 0, z = 0;
		try {
			x = Double.parseDouble(args[index]);
			y = Double.parseDouble(args[index + 1]);
			z = Double.parseDouble(args[index + 2]);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			returnTell(usage);
		}
		if (args.length >= index + 4) {
			if (Bukkit.getWorld(args[index + 3]) == null)
				returnTell(plugin.getLangManager().getNode("invalid-world").replace(Common.map("%world%", args[index + 3])));
			return new Location(Bukkit.getWorld(args[index + 3]), x, y, z);
		} else
			if (isPlayer())
				return new Location(((Player) sender).getWorld(), x, y, z);
			else
				returnTell(consoleUsage);
		return null;
	}

	protected void check(Boolean toCheck, Node node) {
		if (!toCheck)
			returnTell(node);
	}

	protected boolean checkPermission(String perm) {
		perm = perm.replace("%action%", args[0]);
		if (isPlayer)
			if (((Player) sender).hasPermission(perm) ||
					((Player) sender).hasPermission(plugin.getPermission().replace("%action%", args[0])))
				return true;
			else {
				returnTell(plugin.getLangManager().getNode("no-permission").replace(Common.map("%permission%", perm)));
				return false;
			}
		return true;
	}

	protected void checkNotNull(Object toCheck, Node nullMessage) {
		if (toCheck == null)
			returnTell(nullMessage);
	}

	protected void checkArgs(int minLength, Node message) {
		if (args.length < minLength)
			returnTell(message);
	}

	protected void checkArgsStrict(int minLength, Node message) {
		if (args.length != minLength)
			returnTell(message);
	}

	protected final class ReturnedCommandException extends RuntimeException {
		private static final long serialVersionUID = 1;

		@Getter
		protected Node node;

		protected ReturnedCommandException(Node node) {
			this.node = node;
		}

		protected void execute() {
			node.execute(sender);
		}
	}
}
