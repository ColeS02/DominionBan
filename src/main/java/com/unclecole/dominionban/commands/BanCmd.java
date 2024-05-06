package com.unclecole.dominionban.commands;

import com.sun.org.apache.xpath.internal.operations.Mult;
import com.unclecole.dominionban.DominionBan;
import com.unclecole.dominionban.database.BanData;
import com.unclecole.dominionban.objects.BanObject;
import com.unclecole.dominionban.utils.PlaceHolder;
import com.unclecole.dominionban.utils.TL;
import cz.gameteam.dakado.multilobby.MultiLobby;
import cz.gameteam.dakado.multilobby.api.MultiLobbyAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BanCmd extends Command {

    MultiLobbyAPI api = MultiLobby.getInstance();
    LuckPerms luckpermsAPI = LuckPermsProvider.get();

    public BanCmd() {
        super("ban");
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) return;
        if(!(sender.hasPermission("dominionbans.ban"))) {
            TL.NO_PERMISSION.send(sender);
            return;
        }

        if(args == null || args.length < 2) {
            TL.INVALID_COMMAND_USAGE.send(sender, new PlaceHolder("<command>", "/ban <user> <reason> (length)"));
            return;
        }

        if(ProxyServer.getInstance().getPlayer(args[0]) == null) {
            TL.INVALID_ARGUMENT_PLAYER.send(sender);
            return;
        }

        String reason = args[1];
        long length = -1;

        if(args.length > 2) {
            length = getBanTime(args[2]);
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ProxiedPlayer banned = ProxyServer.getInstance().getPlayer(args[0]);
        luckpermsAPI.getUserManager().modifyUser(banned.getUniqueId(), user -> {
            user.data().add(Node.builder("dominionbans.banned").negated(false).build());
        });
        BanData.bannedPlayers.put(banned.getUniqueId(),
                new BanObject(player.getUniqueId().toString(),
                        banned.getUniqueId().toString(), reason, length, System.currentTimeMillis()));
        banned.setPermission("dominionbans.banned", true);
        api.sendPlayerToGroup(banned, "JailHub");
        api.directJoin(banned, "Jail");
        TL.SUCCESSFULLY_BANNED.send(sender, new PlaceHolder("%player%", banned.getDisplayName()));
    }

    public long getBanTime(String time) {

        //10m5d20h200min60s
        Pattern years = Pattern.compile("(?i)^d(?:a(?:ys?)?)?$");
        Pattern month = Pattern.compile("(?i)^m(?:o(?:n(?:th)?)?)?$\n");
        Pattern days = Pattern.compile("(?i)^d(?:a(?:ys?)?)?$");
        Pattern hours = Pattern.compile("(?i)^d(?:a(?:ys?)?)?$");
        Pattern minutes = Pattern.compile("(?i)^mi(?:n(?:u(?:t(?:es?)?)?)?)?$");
        Pattern seconds = Pattern.compile("(?i)^s(?:e(?:c(?:o(?:n(?:ds?)?)?)?)?)?$");

        String parsed;
        long timeLong = 0L;
        for(int i = 0; i<time.length(); i++) {
            parsed = time.substring(0,i);
            System.out.println(parsed);
            System.out.println(days.matcher(parsed).find());
            if(years.matcher(parsed).find()) {
                timeLong += Long.parseLong(parsed.substring(0,years.matcher(parsed).start()));
            }
            if(month.matcher(parsed).find()) {
                timeLong += Long.parseLong(parsed.substring(0,month.matcher(parsed).start()));
            }
            if(days.matcher(parsed).find()) {
                System.out.println(parsed.substring(0,days.matcher(parsed).start()));
                timeLong += Long.parseLong(parsed.substring(0,days.matcher(parsed).start()));
            }
            if(hours.matcher(parsed).find()) {
                timeLong += Long.parseLong(parsed.substring(0,hours.matcher(parsed).start()));
            }
            if(minutes.matcher(parsed).find()) {
                timeLong += Long.parseLong(parsed.substring(0,minutes.matcher(parsed).start()));
            }
            if(seconds.matcher(parsed).find()) {
                timeLong += Long.parseLong(parsed.substring(0,seconds.matcher(parsed).start()));
            }
        }
        System.out.println(timeLong);
        return timeLong;
    }

}
