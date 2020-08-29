package de.snapecraft.cloudgui;

import org.bukkit.entity.Player;

public class Perms {
    public static boolean hasPerm(Player p, Permission permission) {
        return p.hasPermission(permission.getNode());
    }
}
