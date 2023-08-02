package fr.sunshinedev.tb;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TBPlayer {

    public BukkitRunnable bukkitRunnable;
    public long tickBreakBedrock = 20L;
    public UUID uuid;

    public TBPlayer(Player player) {
        this.uuid = player.getUniqueId();
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getTickBreakBedrock() {
        return tickBreakBedrock;
    }

    public void setTickBreakBedrock(long tickBreakBedrock) {
        this.tickBreakBedrock = tickBreakBedrock;
    }

    public void incrementTickBreakBedrock() {
        this.tickBreakBedrock = this.tickBreakBedrock + 20L;
    }

    public void startBreakTickBedrock(Block block) {
        bukkitRunnable = new BukkitRunnable() {

            @Override
            public void run() {
                Player p = Bukkit.getPlayer(uuid);
                if(p != null) {
                    Location locPlayer = p.getLocation();
                    World world = locPlayer.getWorld();
                    if(world == null) return;
                    Location locBlock = block.getLocation();
                    ItemStack itemHand = p.getInventory().getItemInMainHand();
                    long secondsRemaining = 11 - (tickBreakBedrock / 20);
                    incrementTickBreakBedrock();
                    Block blockPlayer = p.getLocation().subtract(p.getWidth() / 2, 1, p.getWidth() / 2).getBlock();
                    if(itemHand.getType() != Material.WOODEN_PICKAXE) {
                        resetBreakTickBedrock();
                        return;
                    }
                    if(block.getType() != Material.BEDROCK) {
                        resetBreakTickBedrock();
                        return;
                    }
                    if(world.getEnvironment() == World.Environment.NORMAL) {
                        if(locBlock.getY() != -64 || locPlayer.getY() != -63 || locBlock.getX() != blockPlayer.getX() || locBlock.getZ() != blockPlayer.getZ()) {
                            resetBreakTickBedrock();
                            return;
                        }
                    }else if(world.getEnvironment() == World.Environment.NETHER) {
                        if(locBlock.getY() != 0 || locPlayer.getY() != 1 || locBlock.getX() != blockPlayer.getX() || locBlock.getZ() != blockPlayer.getZ()) {
                            resetBreakTickBedrock();
                            return;
                        }
                    }
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA + "Transposition dans %s secondes..".formatted(secondsRemaining)));
                    if(getTickBreakBedrock() >= 240L) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA + "POOF!"));
                        if(world.getEnvironment() == World.Environment.NORMAL && locPlayer.getY() == -63 || world.getEnvironment() == World.Environment.NETHER && locPlayer.getY() == 1) {
                            p.teleport(locPlayer.add(0, -3, 0));
                        }
                        resetBreakTickBedrock();
                    }
                }
            }
        };
        bukkitRunnable.runTaskTimer(ThroughtBedrock.INSTANCE, 0L, 20L);
    }

    public void stopBreakTickBedrock() {
        bukkitRunnable.cancel();
    }

    public void resetBreakTickBedrock() {
        if(this.tickBreakBedrock > 20L && this.bukkitRunnable != null) {
            this.tickBreakBedrock = 20L;
            this.bukkitRunnable.cancel();
        }
    }
}
