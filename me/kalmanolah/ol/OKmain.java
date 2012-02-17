package me.kalmanolah.ol;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import me.kalmanolah.cubelist.classfile.cubelist;
import me.kalmanolah.extras.OKUpdater;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OKmain extends JavaPlugin
{
  public static String name;
  public static String version;
  public static ArrayList<String> authors;
  public static PermissionHandler permissionHandler;
  public static PermissionManager permissionManager;
  public static WorldsHolder groupManager;
  public PlayerListener playerListener = new OKPlayerListener(this);
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

  public void onEnable()
  {
    name = getDescription().getName();
    version = getDescription().getVersion();
    authors = getDescription().getAuthors();
    OKLogger.initialize(Logger.getLogger("Minecraft"));
    OKLogger.info("Attempting to enable " + name + " v" + version + " by " + (String)authors.get(0) + "...");
    OKUpdater.update(name, version, "http://kalmanolah.net/files/check.php", "http://kalmanolah.net/files/dl.php", OKLogger.getLog(), OKLogger.getPrefix());
    PluginManager pm = getServer().getPluginManager();
    Plugin p = pm.getPlugin("Permissions");
    Plugin pex = pm.getPlugin("PermissionsEx");
    Plugin gm = pm.getPlugin("GroupManager");
    if (p != null) {
      if (!pm.isPluginEnabled(p)) {
        pm.enablePlugin(p);
      }
      permissionHandler = ((Permissions)p).getHandler();
    }
    if (pex != null) {
      permissionHandler = null;
      if (!pm.isPluginEnabled(pex)) {
        pm.enablePlugin(pex);
      }
      permissionManager = PermissionsEx.getPermissionManager();
    }
    if (gm != null) {
      permissionHandler = null;
      if (!pm.isPluginEnabled(gm)) {
        pm.enablePlugin(gm);
      }
      groupManager = ((GroupManager)gm).getWorldsHolder();
    }
    if ((pex == null) && (p == null) && (gm == null)) {
      OKLogger.info("Permissions plugin not found, defaulting to Bukkit's permissions...");
    }
    OKLogger.info("Successfully hooked into Permissions plugin.");
    OKConfig config = new OKConfig(this);
    config.configCheck();
    OKLogFile logfile = new OKLogFile(this);
    logfile.logCheck();
    if (chatlogs) {
      pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener, Event.Priority.Monitor, this);
    }
    if (cmdlogs) {
      pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this.playerListener, Event.Priority.Monitor, this);
    }
    setupCommands();
    if (stats.booleanValue()) {
      new cubelist(this);
    }
    OKLogger.info(name + " v" + version + " enabled successfully.");
  }

  public void onDisable() {
    OKLogger.info("Attempting to disable " + name + "...");
    OKLogger.info(name + " disabled successfully.");
  }

  public static boolean CheckPermission(Player player, String string) {
    if (permissionHandler != null)
      return permissionHandler.has(player, string);
    if (permissionManager != null) {
      if (!permissionManager.has(player, string))
        return false;
    }
    else if (groupManager != null) {
      if (!groupManager.getWorldPermissions(player).has(player, string)) {
        return false;
      }
    }
    else if (!player.hasPermission(string)) {
      return false;
    }

    return true;
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