package ComandoHome;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import io.pluginteste.maridao.Maridao;


public class ComandoHome implements CommandExecutor {

    private final JavaPlugin plugin;
    Maridao maridao = Maridao.getInstance();

    public ComandoHome(JavaPlugin plugin) {
        this.plugin = plugin;

    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String locString = maridao.getHomeLocation(player.getName());

            String [] converte = locString.split(",");
            String worldName = converte[0];
            int x = Integer.parseInt(converte[1]);
            int y = Integer.parseInt(converte[2]);
            int z = Integer.parseInt(converte[3]);

            Location homeLoc = new Location(player.getWorld(), x, y, z);

            player.teleport(homeLoc);
            player.sendMessage("Você foi teleportado para casa");

        }
        return true;
    }
}










