package me.kalmanolah.ol;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OKCmd implements CommandExecutor {
    private static OKmain plugin;

    public OKCmd(OKmain instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean handled = false;
        if (is(label, "ol")) {
            if ((args.length == 1) && (is(args[0], "reload"))) {
                handled = true;
                if (!isPlayer(sender)) {
                    OKConfig.loadkeys();
                    OKLogger.info("Configuration reloaded!");
                } else if (OKmain.checkPermission(getPlayer(sender), "oklogger.reload")) {
                    OKConfig.loadkeys();
                    sendMessage(sender, ChatColor.GOLD + "Configuration reloaded!");
                } else {
                    sendMessage(sender, ChatColor.LIGHT_PURPLE + "You do not have permission to do this.");
                }
            } else {
                handled = true;
                sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
            }
        }
        return handled;
    }

    private boolean is(String entered, String label) {
        return entered.equalsIgnoreCase(label);
    }

    private boolean sendMessage(CommandSender sender, String message) {
        boolean sent = false;
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            player.sendMessage(message);
            sent = true;
        }
        return sent;
    }

    private String getName(CommandSender sender) {
        String name = "";
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            name = player.getName();
        }
        return name;
    }

    private boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    private Player getPlayer(CommandSender sender) {
        Player player = null;
        if (isPlayer(sender)) {
            player = (Player) sender;
        }
        return player;
    }

    private String join(String[] split, int delimiter) {
        String joined = "";
        int length = split.length;
        int i = delimiter;
        while (i < length - 1) {
            joined = joined + split[i] + " ";
            i++;
        }
        while (i == length - 1) {
            joined = joined + split[i];
            i++;
        }
        return joined;
    }

}