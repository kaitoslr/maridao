package ComandoHome;


import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import io.pluginteste.maridao.Maridao;

public class ComandoSetHome implements CommandExecutor {
    private final JavaPlugin plugin;
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
            maridao.saveHomeLocation(player.getUniqueId().toString(), locString);
            maridao.saveConfig();
            player.sendMessage("Casa definida");
        }
        return true;
    }
}
