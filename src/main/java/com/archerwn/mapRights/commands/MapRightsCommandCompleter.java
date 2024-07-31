package com.archerwn.mapRights.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapRightsCommandCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>(Arrays.asList("sign", "unsign"));

        if (args.length == 1) {
            completions.removeIf(s -> !s.startsWith(args[0]));
        }

        return completions;
    }
}
