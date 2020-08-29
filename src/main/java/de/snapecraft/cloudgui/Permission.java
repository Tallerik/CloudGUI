package de.snapecraft.cloudgui;

public enum Permission {
    MAINTENANCE("cloudgui.globalmaintenance"),//
    GETITEM("cloudgui.getitem"), //
    ALERT("cloudgui.alertonabuse"),//
    USE("cloudgui.useitem"),//
    SHOWSTATISTICS("cloudgui.showstats"),//
    SHUTDOWN("cloudgui.shutdowncloud"),
    SHOWSERVERS("cloudgui.show.servers"),//
    SHOWPROXIES("cloudgui.show.proxies"),//
    MANAGESERVER("cloudgui.manageservers");//


    private String node;
    Permission(String s) {
        node = s;
    }

    public String getNode() {
        return node;
    }
}
