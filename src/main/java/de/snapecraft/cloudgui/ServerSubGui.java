package de.snapecraft.cloudgui;

import de.snapecraft.cloudgui.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ServerSubGui {
    public String name;
    Inventory inv;

    public ServerSubGui(String name) {
        this.name = name;
        inv = Bukkit.createInventory(null, 9, "§6Server§7: §a" + name);
        construct();
    }

    private void construct() {
        inv.setItem(0, Util.changeItemMeta(new ItemStack(Material.ENDER_PEARL), "§aZum Server Teleportieren"));
        inv.setItem(2, Util.changeItemMeta(new ItemStack(Material.REDSTONE_BLOCK), "§aServer Gruppe Maintenance ändern"));
        inv.setItem(4, Util.changeItemMeta(new ItemStack(Material.DIAMOND), "§aNeuen Server dieser Gruppe starten."));
        inv.setItem(6, Util.changeItemMeta(new ItemStack(Material.BARRIER), "§aDiesen Server stoppen."));
        inv.setItem(8, Util.changeItemMeta(new ItemStack(Material.BEDROCK), "§aAlle Server dieser Gruppe stoppen."));
    }

    public void open(Player p) {
        p.openInventory(inv);
    }

}
