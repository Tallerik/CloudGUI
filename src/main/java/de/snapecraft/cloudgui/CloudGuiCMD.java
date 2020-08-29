package de.snapecraft.cloudgui;

import de.snapecraft.cloudgui.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CloudGuiCMD  implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Perms.hasPerm((Player)sender, Permission.USE)) {
            Util.openGui((Player)sender);
        } else {
            sender.sendMessage("Dazu hast du keine Rechte!");
        }
        return true;
    }
}
