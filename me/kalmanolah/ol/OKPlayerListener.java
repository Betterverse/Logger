package me.kalmanolah.ol;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class OKPlayerListener extends PlayerListener
{
  private static OKmain plugin;

  public OKPlayerListener(OKmain instance)
  {
    plugin = instance;
  }

  public void onPlayerChat(PlayerChatEvent event) {
    Player plr = event.getPlayer();
    try {
      OKLogFile.writeChat("[CHAT] <" + plr.getName() + "(" + plr.getAddress().getAddress().getHostAddress() + ")>@" + plr.getWorld().getName() + "(" + plr.getLocation().getBlockX() + "," + plr.getLocation().getBlockY() + "," + 
        plr.getLocation().getBlockZ() + ") : " + event.getMessage());
    } catch (Exception e) {
      OKLogger.info("Error logging.");
    }
  }

  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    Player plr = event.getPlayer();
    String cmd = event.getMessage();
    if (OKmain.cmdconsolelogs) {
      OKLogger.info("[COMMAND] <" + plr.getName() + "(" + plr.getAddress().getAddress().getHostAddress() + ")>@" + plr.getWorld().getName() + "(" + plr.getLocation().getBlockX() + "," + plr.getLocation().getBlockY() + "," + 
        plr.getLocation().getBlockZ() + ") : " + cmd);
    }
    if ((!OKmain.chatlogname.equals(OKmain.cmdlogname)) && (OKmain.cmdchatoverlap)) {
      int j = 0;
      Iterator overcmds = OKmain.overlappedcmds.iterator();
      while ((overcmds.hasNext()) && (j == 0)) {
        String nextcmd = (String)overcmds.next();
        if ((!cmd.startsWith(nextcmd + " ")) && (!cmd.equals(nextcmd))) continue;
        try {
          OKLogFile.writeChat("[COMMAND] <" + plr.getName() + "(" + plr.getAddress().getAddress().getHostAddress() + ")>@" + plr.getWorld().getName() + "(" + plr.getLocation().getBlockX() + "," + plr.getLocation().getBlockY() + "," + 
            plr.getLocation().getBlockZ() + ") : " + cmd);
        } catch (Exception e) {
          OKLogger.info("Error logging.");
        }
        j++;
      }
    }

    if (OKmain.cmdfilter) {
      Iterator filters = OKmain.cmdfiltered.iterator();
      int i = 0;
      do {
        String nextfilter = (String)filters.next();
        if ((cmd.startsWith(nextfilter + " ")) || (cmd.equals(nextfilter))) {
          try {
            OKLogFile.writeCmd("[COMMAND] <" + plr.getName() + "(" + plr.getAddress().getAddress().getHostAddress() + ")>@" + plr.getWorld().getName() + "(" + plr.getLocation().getBlockX() + "," + plr.getLocation().getBlockY() + "," + 
              plr.getLocation().getBlockZ() + ") : " + cmd);
          } catch (Exception e) {
            OKLogger.info("Error logging.");
          }
          i++;
        }
        if (!filters.hasNext()) break; 
      }while (i == 0);
    }
    else
    {
      try
      {
        OKLogFile.writeCmd("[COMMAND] <" + plr.getName() + "(" + plr.getAddress().getAddress().getHostAddress() + ")>@" + plr.getWorld().getName() + "(" + plr.getLocation().getBlockX() + "," + plr.getLocation().getBlockY() + "," + 
          plr.getLocation().getBlockZ() + ") : " + event.getMessage());
      } catch (Exception e) {
        OKLogger.info("Error logging.");
      }
    }
  }
}