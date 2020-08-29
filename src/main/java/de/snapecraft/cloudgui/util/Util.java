package de.snapecraft.cloudgui.util;

import com.google.gson.JsonObject;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.MultiValue;
import de.dytanic.cloudnet.lib.server.info.ProxyInfo;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import de.snapecraft.cloudgui.Cloudgui;
import de.snapecraft.cloudgui.Permission;
import de.snapecraft.cloudgui.Perms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Util {
    public static ItemStack getGuiItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aCloud");
        List<String> lore = new ArrayList<>();
        lore.add("§6Hier kannst du die Cloud Verwalten!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static void openGui(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, "Cloud");
        if (Perms.hasPerm(p, Permission.MAINTENANCE)) {
            boolean maintenance = Cloud.isMaintenance(p);
            String itemWartungLore;
            if (maintenance) {
                itemWartungLore = "§aWartungsmodus ist aktiv!";
            } else {
                itemWartungLore = "§aWartungsmodus ist inaktiv!";
            }

            inv.setItem(0, changeItemMeta(new ItemStack(Material.REDSTONE_BLOCK), "Wartungsmodus anschalten", itemWartungLore));
            inv.setItem(1, changeItemMeta(new ItemStack(Material.EMERALD_BLOCK), "Wartungsmodus ausschalten", itemWartungLore));
        } else {
            inv.setItem(0, changeItemMeta(new ItemStack(Material.OBSIDIAN), "Wartungsmodus", "§cDu hast keine Rechte den Wartungsmodus zu verwalten"));
        }
        if (Perms.hasPerm(p, Permission.SHUTDOWN)) {
            inv.setItem(53, changeItemMeta(new ItemStack(Material.TNT), "Cloud Herunterfahren", "§cFährt die Cloud herunter"));
        }
        if (Perms.hasPerm(p, Permission.SHOWSTATISTICS)) {
            JsonObject json = Cloud.getStatistics();
            ItemStack statsItem = new ItemStack(Material.LEGACY_REDSTONE_COMPARATOR_ON);
            ItemMeta statsMeta = statsItem.getItemMeta();
            statsMeta.setDisplayName("§aStatistiken");
            List<String> statsLore = new ArrayList<>();
            statsLore.add("");
            statsLore.add("Clouds gestartet: " + json.get("cloudStartup").getAsString());
            statsLore.add("Verbunden: " + json.get("wrapperConnections").getAsString());
            statsLore.add("Server: " + json.get("startedServers").getAsString());
            statsLore.add("Anzahl laufender Server: " + json.get("highestServerOnlineCount").getAsString());
            statsLore.add("Proxys: " + json.get("startedProxys").getAsString());
            statsLore.add("gejoint: " + json.get("playerLogin").getAsString());
            statsLore.add("Spieleranzahl: " + json.get("highestPlayerOnline").getAsString());
            statsLore.add("§6Spielern ausgeführte Befehle: " + json.get("playerCommandExecutions").getAsString());
            //statsLore.add("Online Zeit: "+ (new Date(json.get("cloudOnlineTime").getAsLong())).getHours() + " Stunden");
            statsMeta.setLore(statsLore);
            statsItem.setItemMeta(statsMeta);
            inv.addItem(new ItemStack[] { statsItem });
        }
        ItemStack servercover = changeItemMeta(new ItemStack(Material.GLASS_PANE, 1, (short) 3), "§4Laufende Server");

        for (int i = 9; i < 18; i++) {
            inv.setItem(i, servercover);
        }
        if(Perms.hasPerm(p, Permission.SHOWSERVERS)) {
            int i = 18;
            for(ServerInfo server : CloudAPI.getInstance().getServers()) {
                String add;
                String add2;
                if(server.isOnline()) {
                    add = "Online: Ja";
                }else {
                    add = "Online: Nein";
                }
                if(server.isOnline()) {
                    add2 = "Ingame: Ja";
                }else {
                    add2 = "Ingame: Nein";
                }
                ItemStack item = changeItemMeta(getHead("Hack"), "§9" + server.getServiceId().getServerId(), add, add2, "Spieler: " + server.getPlayers().size() +
                                " / " + server.getMaxPlayers(),
                        "Port: " + server.getPort(), "Ram zugewiesen: " + server.getMemory());

                if (server.getPlayers().contains(p.getName())) {
                    item = makeGlowing(item);
                }

                inv.setItem(i, item);
                i++;
            }
        }
        if(Perms.hasPerm(p, Permission.SHOWPROXIES)) {
            int i = 27;
            for(ProxyInfo server : CloudAPI.getInstance().getProxys()) {
                String add;
                if(server.isOnline()) {
                    add = "Online: Ja";
                }else {
                    add = "Online: Nein";
                }
                while (inv.getItem(i) != null) {
                    i++;
                }
                ItemStack item = changeItemMeta(getHead("Davethe"),
                        server.getServiceId().getServerId(), add, "Spieler: " + server.getOnlineCount(),
                        "Port: " + server.getPort());

                for (MultiValue<UUID, String> mv : server.getPlayers()) {
                    if(mv.getFirst().equals(p.getUniqueId())) {
                        item = makeGlowing(item);
                    }
                }
                inv.setItem(i, item);
                i++;
            }
        }
        p.openInventory(inv);
    }

    public static void alertTeam(Player who, String mess) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Perms.hasPerm(who, Permission.ALERT))
                p.sendMessage("Der Spieler: " + who.getName() + " hat versucht unautorisiert eine Aktion auszuführen " + mess);
        }
    }
    public static ItemStack changeItemMeta(ItemStack stack, String newName) {
        ItemMeta m = stack.getItemMeta();
        m.setDisplayName(newName);
        stack.setItemMeta(m);
        return stack;
    }
    public static ItemStack changeItemMeta(ItemStack stack, String newName, String... lore) {
        ItemMeta m = stack.getItemMeta();
        m.setDisplayName(newName);
        m.setLore(Arrays.asList(lore));
        stack.setItemMeta(m);
        return stack;
    }
    public static ItemStack changeItemLore(ItemStack stack, String... lore) {
        ItemMeta m = stack.getItemMeta();
        m.setLore(Arrays.asList(lore));
        stack.setItemMeta(m);
        return stack;
    }
    public static ItemStack getHead(String player) {
        ItemStack item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(player));
        item.setItemMeta(skull);
        return item;
    }
    public static ItemStack makeGlowing(ItemStack stack) {
        ItemMeta m = stack.getItemMeta();
        Glow g = new Glow(new NamespacedKey(Cloudgui.getInstance(), "glow"));
        m.addEnchant(g, 1, true);
        stack.setItemMeta(m);
        return stack;
    }
}
