package fr.sunshinedev.tb;

import fr.sunshinedev.tb.listeners.PlayersListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class ThroughtBedrock extends JavaPlugin {

    public static ThroughtBedrock INSTANCE = null;
    public ArrayList<TBPlayer> tbPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.getServer().getPluginManager().registerEvents(new PlayersListener(), this);
        Bukkit.getOnlinePlayers().forEach(player -> tbPlayers.add(new TBPlayer(player)));
    }

    @Override
    public void onDisable() {
        tbPlayers.clear();
    }

    public ArrayList<TBPlayer> getTbPlayers() {
        return tbPlayers;
    }
}
