package net.velinquish.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PlayerCommand extends AnyCommand {

	private VelinquishPlugin plugin = VelinquishPlugin.getInstance();

	private Player player;

	@Override
	public final boolean execute(CommandSender sender, String[] args, boolean silent) {
		if (!(sender instanceof Player)) {
			tell(plugin.getLangManager().getNode("not-a-player"));
			return false;
		}
		final Player player = (Player) sender;
		this.player = player;
		super.execute(sender, args, silent);
		return false;
	}
}
