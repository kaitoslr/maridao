package ComandoHome;


import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.configuration.file.FileConfiguration;
import io.pluginteste.maridao.Maridao;

public class ComandoSetHome implements CommandExecutor {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    Maridao maridao = Maridao.getInstance();

    public ComandoSetHome(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Location loc = player.getLocation();

            String locString = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
            maridao.saveHomeLocation(player.getName(), locString);
            player.sendMessage("Casa definida");
        }
        return true;
    }
}
