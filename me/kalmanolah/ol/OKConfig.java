package me.kalmanolah.ol;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public class OKConfig
{
  private static OKmain plugin;
  public static String directory = "plugins" + File.separator + OKmain.name;
  static File file = new File(directory + File.separator + "config.yml");

  public OKConfig(OKmain instance)
  {
    plugin = instance;
  }

  public void configCheck()
  {
    new File(directory).mkdir();
    if (!file.exists()) {
      try {
        OKLogger.info("Attempting to create configuration file...");
        file.createNewFile();
        addDefaults();
        OKLogger.info("Configuration file successfully created.");
      } catch (Exception ex) {
        ex.printStackTrace();
        OKLogger.error("Erorr creating configuration file.");
      }
    } else {
      OKLogger.info("Attempting to load configuration file...");
      loadkeys();
      OKLogger.info("Configuration file successfully loaded.");
    }
  }

  private static Boolean readBoolean(String root) {
    YamlConfiguration config = load();
    return Boolean.valueOf(config.getBoolean(root));
  }

  private static void write(String root, Object x) {
    YamlConfiguration config = load();
    config.set(root, x);
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String readString(String root) {
    YamlConfiguration config = load();
    return config.getString(root);
  }

  private static YamlConfiguration load() {
    try {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
      return config;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void addDefaults() {
    write("max-logs-to-keep", "100");
    write("enable-chat-logging", Boolean.valueOf(true));
    write("enable-command-logging", Boolean.valueOf(true));
    write("enable-command-logging-to-console", Boolean.valueOf(true));
    write("command-chat-overlapping", "");
    write("command-chat-overlapping.enable-overlapping", Boolean.valueOf(false));
    write("command-chat-overlapping.overlapped-commands", "/example,/example2,/example3");
    write("command-filter", "");
    write("command-filter.enable-command-filtering", Boolean.valueOf(false));
    write("command-filter.logged-commands", "/example,/example2,/example3");
    write("logs", "");
    write("logs.chat-log-name", "log");
    write("logs.command-log-name", "log");
    write("enable-anonymous-stat-tracking", "true");
    loadkeys();
  }

  public static void loadkeys() {
    OKmain.maxlogs = readString("max-logs-to-keep");
    OKmain.chatlogs = readBoolean("enable-chat-logging").booleanValue();
    OKmain.cmdlogs = readBoolean("enable-command-logging").booleanValue();
    OKmain.cmdconsolelogs = readBoolean("enable-command-logging-to-console").booleanValue();
    OKmain.cmdchatoverlap = readBoolean("command-chat-overlapping.enable-overlapping").booleanValue();
    String[] arrayOfString1;
    int j;
    int i;
    if (OKmain.cmdchatoverlap) {
      String[] overlapped = readString("command-chat-overlapping.overlapped-commands").split(",");
      arrayOfString1 = overlapped; j = overlapped.length; for (i = 0; i < j; i++) { String o = arrayOfString1[i];
        OKmain.overlappedcmds.add(o);
      }
    }
    OKmain.cmdfilter = readBoolean("command-filter.enable-command-filtering").booleanValue();
    if (OKmain.cmdfilter) {
      String[] filtered = readString("command-filter.logged-commands").split(",");
      arrayOfString1 = filtered; j = filtered.length; for (i = 0; i < j; i++) { String f = arrayOfString1[i];
        OKmain.cmdfiltered.add(f);
      }
    }
    OKmain.chatlogname = readString("logs.chat-log-name");
    OKmain.cmdlogname = readString("logs.command-log-name");
    Boolean stats = readBoolean("enable-anonymous-stat-tracking");
    if (stats == null)
      OKmain.stats = Boolean.valueOf(true);
    else
      OKmain.stats = stats;
  }
}