package de.snapecraft.cloudgui.listener;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.api.player.PlayerExecutorBridge;
import de.dytanic.cloudnet.lib.server.ServerGroup;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import de.snapecraft.cloudgui.Permission;
import de.snapecraft.cloudgui.Perms;
import de.snapecraft.cloudgui.ServerSubGui;
import de.snapecraft.cloudgui.util.Cloud;
import de.snapecraft.cloudgui.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ClickListener implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        if (event.getItem() != null &&
                event.getItem().getItemMeta() != null &&
                event.getItem().getItemMeta() != null &&
                event.getItem().getItemMeta().getDisplayName() != null) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR &&
                    event.getItem().getItemMeta().getDisplayName().equals("§aCloud") &&
                    Perms.hasPerm(event.getPlayer(), Permission.USE))
                Util.openGui(event.getPlayer());
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                    event.getItem().getItemMeta().getDisplayName().equals("§aCloud") &&
                    Perms.hasPerm(event.getPlayer(), Permission.USE))
                Util.openGui(event.getPlayer());
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Cloud")) {
            if(event.getCurrentItem() == null) return;
            event.setCancelled(true);
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("Wartungsmodus An")) {
                if (Perms.hasPerm((Player)event.getWhoClicked(), Permission.MAINTENANCE)) {
                    Cloud.setMaintenance((Player)event.getWhoClicked(), true);
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage("Wartungsmodus wurde aktiviert!");
                } else {
                    event.getWhoClicked().sendMessage("hasst du keine RECHTE!");
                    Util.alertTeam((Player)event.getWhoClicked(), "Wartung an");
                }
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("Wartungsmodus Aus")) {
                if (Perms.hasPerm((Player)event.getWhoClicked(), Permission.MAINTENANCE)) {
                    Cloud.setMaintenance((Player)event.getWhoClicked(), false);
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage("Wartungsmodus wurde deaktiviert!");
                } else {
                    event.getWhoClicked().sendMessage("hasst du keine RECHTE!");
                    Util.alertTeam((Player)event.getWhoClicked(), "Wartung aus");
                }
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("SHUTDOWN CLOUD")) {
                if (Perms.hasPerm((Player)event.getWhoClicked(), Permission.SHUTDOWN)) {
                    Cloud.shutdown();
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage("Cloud wird heruntergefahren!");
                } else {
                    event.getWhoClicked().sendMessage("hasst du keine RECHTE!");
                    Util.alertTeam((Player)event.getWhoClicked(), "SHUTDOWN");
                }
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§9")) {
                ServerSubGui g = new ServerSubGui(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§9", ""));
                g.open((Player) event.getWhoClicked());
            }

        }
        // Server Sub GUI
        if(event.getView().getTitle().contains("Server")) {
            
            event.setCancelled(true);
            if(event.getCurrentItem() == null) return;
            if(Perms.hasPerm((Player) event.getWhoClicked(), Permission.MANAGESERVER)) {
                String name = event.getView().getTitle().replaceAll("§6Server§7: §a", "");
                if(event.getCurrentItem().getType().equals(Material.ENDER_PEARL)) { // Switch to selected server
                    ServerInfo server = CloudAPI.getInstance().getServerInfo(name);
                    if(server != null) {
                        PlayerExecutorBridge.INSTANCE.sendPlayer(CloudAPI.getInstance().getOnlinePlayer(event.getWhoClicked().getUniqueId()), name);
                    }
                } else if (event.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) { // Toggle Category Maintenance
                    ServerGroup g = CloudAPI.getInstance().getServerGroup(name.split("-")[0]);
                    g.setMaintenance(!g.isMaintenance());
                    CloudAPI.getInstance().updateServerGroup(g);
                    if(g.isMaintenance()) {
                        event.getWhoClicked().sendMessage("§4Die Servergruppe ist jetzt in Wartung.");
                    } else {
                        event.getWhoClicked().sendMessage("§4Die Servergruppe ist jetzt nicht mehr in Wartung.");
                    }
                } else if(event.getCurrentItem().getType().equals(Material.DIAMOND)) { // Start Server in this Category
                    ServerGroup g = CloudAPI.getInstance().getServerGroup(name.split("-")[0]);
                    CloudAPI.getInstance().startGameServer(g.toSimple());
                } else if(event.getCurrentItem().getType().equals(Material.BARRIER)) { // Stop selected Server
                    ServerInfo server = CloudAPI.getInstance().getServerInfo(name);
                    CloudAPI.getInstance().stopServer(server.getServiceId().getServerId());
                } else if(event.getCurrentItem().getType().equals(Material.BEDROCK)) { // Stop all servers in category
                    ServerGroup g = CloudAPI.getInstance().getServerGroup(name.split("-")[0]);
                    for(ServerInfo server :CloudAPI.getInstance().getServers(g.getName())) {
                        CloudAPI.getInstance().stopServer(server.getServiceId().getServerId());
                    }
                }
            } else {
                event.getWhoClicked().sendMessage("§cDazu hast du keine Rechte!");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Perms.hasPerm(event.getPlayer(), Permission.GETITEM))
            event.getPlayer().getInventory().setItem(7, Util.getGuiItem());
    }
}
