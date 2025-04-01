package Plot;


import io.pluginteste.maridao.Maridao;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;
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

        if(loc1 != null && loc2 != null){
            drawSquare(loc1, loc2, player, null);
        }

        event.setCancelled(true);
        switch (event.getAction()){
            case Action.RIGHT_CLICK_BLOCK:
                if(loc1 != null ) player.sendBlockChange(loc1, loc1.getBlock().getBlockData());
                loc1 = event.getClickedBlock().getLocation();
                Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Maridao.class) , ()->{player.sendBlockChange(loc1, Material.GLOWSTONE.createBlockData());}, 1);
                player.sendMessage(" §6Local1 " + loc1.getX() + ", " + loc1.getY() + ", " + loc1.getZ());
                break;
            case Action.LEFT_CLICK_BLOCK:
                if(loc2 != null ) player.sendBlockChange(loc2, loc2.getBlock().getBlockData());
                loc2 = event.getClickedBlock().getLocation();
                player.sendBlockChange(loc2, Material.GLOWSTONE.createBlockData());
                player.sendMessage(" §cLocal2 " +  loc2.getX() + ", " + loc2.getY() + ", " + loc2.getZ());
                break;
        }

        if(loc1 != null && loc2 != null){
            drawSquare(loc1, loc2, player, Material.GLOWSTONE.createBlockData());
        }
    }

    public static void drawSquare(Location loc1, Location loc2, Player player, BlockData material){
        int max_x = (int) Math.max(loc1.getX(), loc2.getX());
        int min_x = (int) Math.min(loc1.getX(), loc2.getX());
        int max_z = (int) Math.max(loc1.getZ(), loc2.getZ());
        int min_z = (int) Math.min(loc1.getZ(), loc2.getZ());

        for(int x = min_x; x < max_x; x ++){
            int low_y_loc1 = player.getWorld().getHighestBlockYAt(x, max_z);
            int low_y_loc2 = player.getWorld().getHighestBlockYAt(x, min_z);
            BlockData block_loc1 = material != null ? material : new Location(player.getWorld(), x, low_y_loc1, max_z).getBlock().getBlockData();
            BlockData block_loc2 = material != null ? material : new Location(player.getWorld(), x, low_y_loc2, max_z).getBlock().getBlockData();

            player.sendBlockChange(new Location(player.getWorld(), x, low_y_loc1 , max_z), block_loc1);
            player.sendBlockChange(new Location(player.getWorld(), x, low_y_loc2 , min_z), block_loc2);
        }

        for(int z = min_z; z < max_z; z++){
            int low_y_loc1 = player.getWorld().getHighestBlockYAt(min_x, z);
            int low_y_loc2 = player.getWorld().getHighestBlockYAt(max_x, z);
            BlockData block_loc1 = material != null ? material : new Location(player.getWorld(), min_x, low_y_loc1, z).getBlock().getBlockData();
            BlockData block_loc2 = material != null ? material : new Location(player.getWorld(), min_x, low_y_loc2, z).getBlock().getBlockData();

            player.sendBlockChange(new Location(player.getWorld(), min_x, low_y_loc1 , z), block_loc1);
            player.sendBlockChange(new Location(player.getWorld(), max_x, low_y_loc2 , z), block_loc2);
        }
    }

    public static void setClaim(Player player){
        if(!validateMinSize(loc1, loc2)){
            player.sendMessage("Seu terreno deve ter pelo menos 36 blocos de tamanho !!!");
            return;
        }
        savePlotLocation(player.getUniqueId().toString(), loc1.toString(), loc2.toString());
        player.sendMessage("Terreno assegurado com sucesso !!!");
        if(loc1 != null && loc2 != null){
            drawSquare(loc1, loc2, player, Material.DIAMOND_BLOCK.createBlockData());
        }
        loc2 = null;
        loc1 = null;
    }

    public static boolean validateMinSize(Location loc1, Location loc2) {
        double diff_x = Math.abs(loc1.getX() - loc2.getX());
        double diff_z = Math.abs(loc1.getZ() - loc2.getZ());
        double area = diff_x * diff_z;

        return area > 36;
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
        JavaPlugin.getPlugin(Maridao.class).saveConfig();
    }

}
