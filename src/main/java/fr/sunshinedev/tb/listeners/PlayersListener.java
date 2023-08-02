package fr.sunshinedev.tb.listeners;

import fr.sunshinedev.tb.TBPlayer;
import fr.sunshinedev.tb.ThroughtBedrock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayersListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(ThroughtBedrock.INSTANCE.getTbPlayers().stream().noneMatch(tbPlayer -> tbPlayer.getUuid().equals(p.getUniqueId()))) {
            ThroughtBedrock.INSTANCE.getTbPlayers().add(new TBPlayer(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ThroughtBedrock.INSTANCE.getTbPlayers().removeIf(tbPlayer -> tbPlayer.getUuid().equals(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerBlockDamage(BlockDamageEvent event) {
        Player p = event.getPlayer();
        Optional<TBPlayer> tbPlayerOptional = ThroughtBedrock.INSTANCE.getTbPlayers().stream().filter(tbPlayer -> tbPlayer.getUuid().equals(p.getUniqueId())).findFirst();
        if(tbPlayerOptional.isPresent()) {
            TBPlayer tbPlayer = tbPlayerOptional.get();
            tbPlayer.resetBreakTickBedrock();
            tbPlayer.startBreakTickBedrock(event.getBlock());
        }
    }

    @EventHandler
    public void onPlayerBlockDamageAbort(BlockDamageAbortEvent event) {
        Player p = event.getPlayer();
        Optional<TBPlayer> tbPlayerOptional = ThroughtBedrock.INSTANCE.getTbPlayers().stream().filter(tbPlayer -> tbPlayer.getUuid().equals(p.getUniqueId())).findFirst();
        if(tbPlayerOptional.isPresent()) {
            TBPlayer tbPlayer = tbPlayerOptional.get();
            tbPlayer.setTickBreakBedrock(20L);
            if(tbPlayer.bukkitRunnable != null) tbPlayer.stopBreakTickBedrock();
        }
    }

}
