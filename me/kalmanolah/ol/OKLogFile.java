package me.kalmanolah.ol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;

public class OKLogFile
{
  private static OKmain plugin;
  public static String directory = "plugins" + File.separator + OKmain.name;
  public static String subdirectory = "plugins" + File.separator + OKmain.name + File.separator + "logs";
  public static String subsubdirectory = subdirectory + File.separator + OKmain.getDate();
  static File chatfile = new File(subdirectory + File.separator + OKmain.getDate() + File.separator + OKmain.chatlogname + ".txt");
  static File cmdfile = new File(subdirectory + File.separator + OKmain.getDate() + File.separator + OKmain.cmdlogname + ".txt");

  public OKLogFile(OKmain instance)
  {
    plugin = instance;
  }

  public void logCheck()
  {
    new File(directory).mkdir();
    new File(subdirectory).mkdir();
    new File(subsubdirectory).mkdir();
    if (!chatfile.exists()) {
      try {
        chatfile.createNewFile();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    if (!cmdfile.exists()) {
      try {
        cmdfile.createNewFile();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    prune();
    plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
      public void run() {
        OKLogFile.prune();
      }
    }
    , 3600L, 72000L);
  }

  public static void writeCmd(String args) {
    subsubdirectory = subdirectory + File.separator + OKmain.getDate();
    new File(subsubdirectory).mkdir();
    File file = new File(subsubdirectory + File.separator + OKmain.cmdlogname + ".txt");
    if (!file.exists())
      try {
        file.createNewFile();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    try
    {
      FileWriter fstream = new FileWriter(file, true);
      BufferedWriter out = new BufferedWriter(fstream);
      out.write(OKmain.getDateTime() + " | " + args);
      out.newLine();
      out.close();
    } catch (Exception localException1) {
    }
  }

  public static void writeChat(String args) {
    subsubdirectory = subdirectory + File.separator + OKmain.getDate();
    new File(subsubdirectory).mkdir();
    File file = new File(subsubdirectory + File.separator + OKmain.chatlogname + ".txt");
    if (!file.exists())
      try {
        file.createNewFile();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    try
    {
      FileWriter fstream = new FileWriter(file, true);
      BufferedWriter out = new BufferedWriter(fstream);
      out.write(OKmain.getDateTime() + " | " + args);
      out.newLine();
      out.close();
    } catch (Exception localException1) {
    }
  }

  public static void prune() {
    File oldfolder = new File(subdirectory + File.separator + OKmain.getPruneDate());
    if (oldfolder.exists())
      oldfolder.delete();
  }
}