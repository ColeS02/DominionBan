package com.unclecole.dominionban.commands;

import com.unclecole.dominionban.DominionBan;
import com.unclecole.dominionban.database.BanData;
import com.unclecole.dominionban.utils.PlaceHolder;
import com.unclecole.dominionban.utils.TL;
import cz.gameteam.dakado.multilobby.MultiLobby;
import cz.gameteam.dakado.multilobby.api.MultiLobbyAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnBanCmd extends Command {

    MultiLobbyAPI api = MultiLobby.getInstance();
    LuckPerms luckpermsAPI = LuckPermsProvider.get();

    public UnBanCmd() {
        super("unban");
    }

    public void execute(CommandSender sender, String[] args) {

        if(!(sender instanceof ProxiedPlayer)) return;
        if(!(sender.hasPermission("dominionbans.unban"))) {
            TL.NO_PERMISSION.send(sender);
            return;
        }

        if(args.length < 1) {
            TL.INVALID_COMMAND_USAGE.send(sender, new PlaceHolder("<command>", "/unban <user>"));
            return;
        }

        if(ProxyServer.getInstance().getPlayer(args[0]) == null) {
            TL.INVALID_ARGUMENT_PLAYER.send(sender);
            return;
        }

        ProxiedPlayer unbanned = ProxyServer.getInstance().getPlayer(args[0]);
        BanData.bannedPlayers.remove(unbanned.getUniqueId());
        luckpermsAPI.getUserManager().modifyUser(unbanned.getUniqueId(), user -> {
            user.data().add(Node.builder("dominionbans.banned").negated(true).build());
        });
        api.sendPlayerToGroup(unbanned, "Lobbies");
        api.directJoin(unbanned, "Hub-1");
        TL.SUCCESSFULLY_UNBANNED.send(sender, new PlaceHolder("%player%", unbanned.getDisplayName()));
    }

}
