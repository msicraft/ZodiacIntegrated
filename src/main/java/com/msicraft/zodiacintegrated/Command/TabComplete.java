package com.msicraft.zodiacintegrated.Command;

import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    private GuildUtil guildUtil = new GuildUtil();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("zodiac")) {
            if (args.length == 1 && sender.isOp()) {
                List<String> arguments = new ArrayList<>();
                arguments.add("help");
                arguments.add("reload");
                arguments.add("guild");
                return arguments;
            }
            if (args.length == 2 && args[0].equals("guild") && sender.isOp()) {
                List<String> arguments = new ArrayList<>();
                arguments.add("register");
                arguments.add("unregister");
                arguments.add("menu");
                arguments.add("invite");
                arguments.add("banish");
                return arguments;
            }
            if (args.length == 2 && args[0].equals("guild")) {
                List<String> arguments = new ArrayList<>();
                if (sender instanceof Player player) {
                    if (guildUtil.isGuildOwner(player.getUniqueId())) {
                        arguments.add("invite");
                        arguments.add("banish");
                    }
                }
                arguments.add("menu");
                return arguments;
            }
        }
        return null;
    }

}
