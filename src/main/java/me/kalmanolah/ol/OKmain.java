package me.kalmanolah.ol;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OKmain extends JavaPlugin {

	public static String name;
	public static String version;
	public static List<String> authors;
	public OKCommandManager commandManager = new OKCommandManager(this);
	public static String maxlogs;
	public static boolean chatlogs = true;
	public static boolean cmdlogs = true;
	public static boolean cmdconsolelogs = false;
	public static boolean cmdchatoverlap = false;
	public static List<String> overlappedcmds = new ArrayList();
	public static boolean cmdfilter = false;
	public static List<String> cmdfiltered = new ArrayList();
	public static String chatlogname = "log";
	public static String cmdlogname = "log";
	public static Boolean stats;

	@Override
	public void onEnable() {
		name = getDescription().getName();
		version = getDescription().getVersion();
		authors = getDescription().getAuthors();
		OKLogger.initialize(Logger.getLogger("Minecraft"));
		PluginManager pm = getServer().getPluginManager();
		OKConfig config = new OKConfig(this);
		config.configCheck();
		OKLogFile logfile = new OKLogFile(this);
		logfile.logCheck();
		new OKPlayerListener(this);
		setupCommands();
	}

	@Override
	public void onDisable() {
		OKLogger.info("Attempting to disable " + name + "...");
		OKLogger.info(name + " disabled successfully.");
	}

	public static boolean CheckPermission(Player player, String string) {
		return player.hasPermission(string);
	}

	private void setupCommands() {
		addCommand("ol", new OKCmd(this));
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return this.commandManager.dispatch(sender, cmd, label, args);
	}

	private void addCommand(String command, CommandExecutor executor) {
		getCommand(command).setExecutor(executor);
		this.commandManager.addCommand(command, executor);
	}

	public static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getPruneDate() {
		long backDateMS = System.currentTimeMillis() - Integer.parseInt(maxlogs) * 24L * 60L * 60L * 1000L;
		Date backDate = new Date();
		backDate.setTime(backDateMS);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(backDate);
	}
}