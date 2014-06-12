package net.infinitecoder.whosonline;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WhosOnline extends JavaPlugin implements Listener {
	
	private static WhosOnline instance;
	
	public Logger logger = Logger.getLogger("Minecraft");
	
	
	public static WhosOnline getInstance() {
		return instance;
	}
	
	public void onEnable() {
		WhosOnline.instance = this;
		if(this.getDataFolder().exists()) {
			Manager.init(getConfig().getString("secretKey"));
		}else {
			Manager.init();
			getConfig().set("secretKey", Manager.getSecretKey());
			saveConfig();
		}
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void login(PlayerLoginEvent event) {
		Object[] s = Request.sendLoginRequest(event.getPlayer());
		logger.log((Level)s[0], "[Who's Online]" + s[1]);
	}
	
	@EventHandler
	public void logout(PlayerQuitEvent event) {
		Object[] s = Request.sendLogoutRequest(event.getPlayer());
		logger.log((Level)s[0], "[Who's Online]" + s[1]);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage("§a[Who's Online] §4Only console can perform this command!");
			return false;
		}
		
		if (cmd.getName().equalsIgnoreCase("setdata")) {
			if(args.length == 2) {
				sender.sendMessage("§a[Who's Online] §7" + Request.sendChangeRequest(args[0], args[1]));
			}else {
				sender.sendMessage("§a[Who's Online] §4Invalid syntax. Syntax: /setdata <server name> <server address>");
			}
			return true;
		}
		return false;
	}
	
}
