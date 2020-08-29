package de.snapecraft.cloudgui.util;

import com.google.gson.JsonObject;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.server.info.ProxyInfo;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import org.bukkit.entity.Player;

import java.util.Collection;

public class Cloud {
    public static boolean isMaintenance(Player p) {
        return CloudAPI.getInstance().getProxyGroupData(CloudAPI.getInstance().getOnlinePlayer(p.getUniqueId()).getProxy().split("-")[0]).getProxyConfig().isMaintenance();
    }

    public static void setMaintenance(Player p, boolean state) {
        CloudAPI.getInstance().getProxyGroupData(CloudAPI.getInstance().getOnlinePlayer(p.getUniqueId()).getProxy().split("-")[0]).getProxyConfig().setMaintenance(state);
        CloudAPI.getInstance().updateProxyGroup(CloudAPI.getInstance().getProxyGroupData(CloudAPI.getInstance().getOnlinePlayer(p.getUniqueId()).getProxy().split("-")[0]));
    }

    public static void stopServer(String serverid, ServerType type) {
        if (type.equals(ServerType.BUNGEECORD))
            CloudAPI.getInstance().stopProxy(serverid);
        if (type.equals(ServerType.SPIGOT))
            CloudAPI.getInstance().stopServer(serverid);
    }

    public static Collection<ServerInfo> getServers() {
        return CloudAPI.getInstance().getServers();
    }

    public static Collection<ProxyInfo> getProxy() {
        return CloudAPI.getInstance().getProxys();
    }

    public static void startServer(String groupname) {
        CloudAPI.getInstance().startGameServer(CloudAPI.getInstance().getServerGroup(groupname).toSimple());
    }

    public static JsonObject getStatistics() {
        return CloudAPI.getInstance().getStatistics().obj();
    }

    public static void shutdown() {
        CloudAPI.getInstance().getShutdownTask().run();
    }
}
