package de.snapecraft.cloudgui;

import de.snapecraft.cloudgui.listener.ClickListener;
import de.snapecraft.cloudgui.util.Glow;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class Cloudgui extends JavaPlugin {
    public static Cloudgui instance;

    private String art = "  _____ _                 _    _____ _    _ _____ \n  / ____| |               | |  / ____| |  | |_   _|\n | |    | | ___  _   _  __| | | |  __| |  | | | |  \n | |    | |/ _ \\| | | |/ _` | | | |_ | |  | | | |  \n | |____| | (_) | |_| | (_| | | |__| | |__| |_| |_ \n  \\_____|_|\\___/ \\__,_|\\__,_|  \\_____|\\____/|_____|\n                                                     ";

    public void onEnable() {
        instance = this;
        getCommand("cloudgui").setExecutor(new CloudGuiCMD());
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        System.out.println("\n" + art);
        registerGlow();
    }

    public void onDisable() {
        System.out.println(this.art);
    }

    public static Cloudgui getInstance() {
        return instance;
    }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(new NamespacedKey(this, "glow"));
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
