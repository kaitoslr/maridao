package io.pluginteste.maridao;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Maridao extends JavaPlugin {

    @Override
    public void onEnable() {
        // Este método é chamado quando o plugin é carregado
        getLogger().info("MeuPluginPaper foi habilitado!");
    }

    @Override
    public void onDisable() {
        // Este método é chamado quando o plugin é desativado
        getLogger().info("MeuPluginPaper foi desabilitado!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hello")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("Olá, " + player.getName() + "!");
            } else {
                getLogger().info("Este comando só pode ser usado por um jogador!");
            }
            return true;
        }
        return false;
    }
}
