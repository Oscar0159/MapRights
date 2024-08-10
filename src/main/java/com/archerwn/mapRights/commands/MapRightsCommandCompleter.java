package com.archerwn.mapRights.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MapRightsCommandCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("maprights.sign")) {
                completions.add("sign");
            }
            if (sender.hasPermission("maprights.unsign")) {
                completions.add("unsign");
            }
            if (sender.hasPermission("maprights.forcesign")) {
                completions.add("forcesign");
            }
            if (sender.hasPermission("maprights.forceunsign")) {
                completions.add("forceunsign");
            }
            if (sender.hasPermission("maprights.info")) {
                completions.add("info");
            }
            completions.removeIf(s -> !s.startsWith(args[0]));
        }

        return completions;
    }
}
