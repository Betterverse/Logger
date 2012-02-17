package me.kalmanolah.cubelist.classfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class cubelist
{
  private static Plugin plugin;
  private static String cubelistserverurl = "http://cubelist.me/sync/syncanon.php";

  public cubelist(Plugin plug) {
    plugin = plug;
    File thelockfile = new File("cubelist.lck");
    if (thelockfile.exists()) {
      thelockfile.delete();
    }
    File lockfile = thelockfile;
    plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(lockfile)
    {
      public void run()
      {
        if ((!this.val$lockfile.exists()) && 
          (cubelist.plugin.getServer().getPluginManager().getPlugin("CubeList") == null)) {
          try {
            this.val$lockfile.createNewFile();
          } catch (IOException e) {
            e.printStackTrace();
          }
          cubelist.plugin.getServer().getScheduler().scheduleSyncDelayedTask(cubelist.plugin, new Runnable(this.val$lockfile) {
            public void run() {
              this.val$lockfile.delete();
            }
          }
          , 10800L);
          cubelist.syncData();
        }
      }
    }
    , 2400L, 12000L);
  }

  public static String sendPost(String vars)
  {
    String result = null;
    try {
      URL url = new URL(cubelistserverurl);
      URLConnection conn = url.openConnection();
      conn.setDoOutput(true);
      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
      wr.write(vars);
      wr.flush();
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuffer sb = new StringBuffer();
      String line;
      while ((line = rd.readLine()) != null)
      {
        String line;
        sb.append(line);
      }
      rd.close();
      result = sb.toString();
      wr.close();
    } catch (Exception localException) {
    }
    return result;
  }

  public static void syncData() {
    Boolean error = Boolean.valueOf(false);
    try
    {
      String players = "";
      Player[] plrs = plugin.getServer().getOnlinePlayers();
      String[] tempplayerstwo;
      String plr;
      if (plrs.length > 1) {
        List tempplayers = new ArrayList();
        for (Player p : plrs) {
          tempplayers.add(p.getName());
        }
        tempplayerstwo = new String[tempplayers.size()];
        tempplayers.toArray(tempplayerstwo);
        Arrays.sort(tempplayerstwo, String.CASE_INSENSITIVE_ORDER);
        for (plr : tempplayerstwo)
          players = players + plr + ",";
      }
      else
      {
        Player[] arrayOfPlayer1;
        String[] arrayOfString1 = (arrayOfPlayer1 = plrs).length; for (tempplayerstwo = 0; tempplayerstwo < arrayOfString1; tempplayerstwo++) { Player p = arrayOfPlayer1[tempplayerstwo];
          players = players + p.getName() + ",";
        }
      }
      String result = sendPost("seven=" + URLEncoder.encode(String.valueOf(plugin.getServer().getPort()), "UTF-8") + "&nine=" + URLEncoder.encode(String.valueOf(plugin.getServer().getOnlinePlayers().length), "UTF-8") + "&ten=" + 
        URLEncoder.encode(players, "UTF-8"));
      Plugin[] arrayOfPlugin;
      String str2 = (arrayOfPlugin = plugin.getServer().getPluginManager().getPlugins()).length; for (String str1 = 0; str1 < str2; str1++) { Plugin p = arrayOfPlugin[str1];
        if (!p.getDescription().getName().equals("CubeList")) {
          String authors = "";
          Iterator author = p.getDescription().getAuthors().iterator();
          while (author.hasNext()) {
            authors = authors + (String)author.next() + ",";
          }
          String syncedplugins = p.getDescription().getName() + "%BREAK%" + p.getDescription().getVersion() + "%BREAK%" + p.getDescription().getDescription() + "%BREAK%" + authors + "%BREAK%" + p.getDescription().getWebsite();
          String results = sendPost("six=" + URLEncoder.encode(syncedplugins, "UTF-8") + "&seven=" + URLEncoder.encode(String.valueOf(plugin.getServer().getPort()), "UTF-8"));
          if (results != null) {
            if (!results.equalsIgnoreCase("OK"))
              error = Boolean.valueOf(true);
          }
          else {
            error = Boolean.valueOf(true);
          }
        }
      }
      if (result != null) {
        if (!result.equalsIgnoreCase("OK"))
          error = Boolean.valueOf(true);
      }
      else
        error = Boolean.valueOf(true);
    }
    catch (Exception e) {
      error = Boolean.valueOf(true);
    }
    error.booleanValue();
  }
}