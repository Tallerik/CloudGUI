package de.snapecraft.cloudgui;

import de.dytanic.cloudnet.api.CloudAPI;
import org.bukkit.entity.Player;

public class Perms {
    public static boolean hasPerm(Player p, Permission permission) {
        String role = CloudAPI.getInstance().getOnlinePlayer(p.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool()).getName();
        switch (role) {
            case "Inhaber":
                if (permission.equals(Permission.MAINTENANCE))
                    return true;
                if (permission.equals(Permission.GETITEM))
                    return true;
                if (permission.equals(Permission.ALERT))
                    return true;
                if (permission.equals(Permission.USE))
                    return true;
                if (permission.equals(Permission.SHUTDOWN))
                    return true;
                if (permission.equals(Permission.SHOWSTATISTICS))
                    return true;
                if (permission.equals(Permission.SHOWSERVERS))
                    return true;
                if (permission.equals(Permission.SHOWPROXYS))
                    return true;
                if (permission.equals(Permission.MANAGESERVER))
                    return true;
                break;
            case "Developer":
                if (permission.equals(Permission.MAINTENANCE))
                    return true;
                if (permission.equals(Permission.GETITEM))
                    return true;
                if (permission.equals(Permission.ALERT))
                    return true;
                if (permission.equals(Permission.USE))
                    return true;
                if (permission.equals(Permission.SHOWSTATISTICS))
                    return true;
                break;
            case "Admin":
                if (permission.equals(Permission.GETITEM))
                    return true;
                if (permission.equals(Permission.ALERT))
                    return true;
                if (permission.equals(Permission.USE))
                    return true;
                break;
        }
        return false;
    }
}
