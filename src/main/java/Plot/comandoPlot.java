package Plot;


import io.pluginteste.maridao.Maridao;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class comandoPlot implements Listener {
    static private FileConfiguration configuration  = JavaPlugin.getPlugin(Maridao.class).getConfig();
    static Location loc1 = null;
    static Location loc2 = null;

    @EventHandler
    public void useSetPlot(PlayerInteractEvent event){
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            return;
        }


        Player player = event.getPlayer();

        ItemStack stick = player.getInventory().getItemInMainHand();

        if(stick.getItemMeta() == null ||!stick.getItemMeta().getDisplayName().equals("Plot") || stick.getType() != Material.STICK || event.getHand() != EquipmentSlot.HAND){
            return;
        }

        event.setCancelled(true);
        switch (event.getAction()){
            case Action.RIGHT_CLICK_BLOCK:
                if(loc1 != null ) player.sendBlockChange(loc1, loc1.getBlock().getBlockData());
                loc1 = event.getClickedBlock().getLocation();
                Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Maridao.class) , ()->{player.sendBlockChange(loc1, Material.GLOWSTONE.createBlockData());}, 1);
                player.sendMessage(" §6Local1 " + loc1.getX() + ", " + loc1.getZ());
                break;
            case Action.LEFT_CLICK_BLOCK:
                if(loc2 != null ) player.sendBlockChange(loc2, loc2.getBlock().getBlockData());
                loc2 = event.getClickedBlock().getLocation();
                player.sendBlockChange(loc2, Material.GLOWSTONE.createBlockData());
                player.sendMessage(" §cLocal2 " + + loc2.getX() + ", " + loc2.getZ());
                break;
        }
    }

    public static void setClaim(Player player){
        savePlotLocation(player.getUniqueId().toString(), loc1.toString(), loc2.toString());
    }


    public static ItemStack getStick(){
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();

        meta.setDisplayName("Plot");
        stick.setItemMeta(meta);
        return stick;
    }

    public static String getPlotLocation(String playerName) {
        return configuration.getString("plot." + playerName, null);
    }
    public static void savePlotLocation(String playerName, String loc1, String loc2){
        List<String> plotCord = new ArrayList<>();
        plotCord.add(loc1);
        plotCord.add(loc2);
        configuration.set("plot." + playerName, plotCord);
    }
}
