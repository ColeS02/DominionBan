package com.unclecole.dominionban;

import com.unclecole.dominionban.commands.BanCmd;
import com.unclecole.dominionban.commands.UnBanCmd;
import com.unclecole.dominionban.database.BanData;
import com.unclecole.dominionban.database.seralizer.Persist;
import com.unclecole.dominionban.objects.BanObject;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class DominionBan extends Plugin {

    @Getter static private Persist persist;
    public static DominionBan instance;
    private Configuration configuration;

    @Override
    public void onEnable() {
        instance = this;
        persist = new Persist();
        BanData.load();
        //ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerJoinListener());
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        autoSaveTask();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCmd());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnBanCmd());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        BanData.save();
    }

    public void sendPlayerToServer(ProxiedPlayer player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendData("BungeeCord", b.toByteArray());
            b.close();
            out.close();
        }
        catch (Exception e) {
            //player.sendMessage(C.color("Error when trying to connect to "+server));
        }
    }

    private void autoSaveTask() {
        ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                long timeStart = System.currentTimeMillis();
                BanData.save();
                ProxyServer.getInstance().getLogger().info("[DominionBan] Saving all plugin data.");
                long timeToInit = System.currentTimeMillis();
                ProxyServer.getInstance().getLogger().info("[DominionBan] Saving >> " + (timeToInit - timeStart) + " ms.");
            }
        }, 6000L, 6000L, TimeUnit.SECONDS);
    }
}
