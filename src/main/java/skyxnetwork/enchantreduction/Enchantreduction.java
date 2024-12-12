package skyxnetwork.enchantreduction;

import org.bukkit.plugin.java.JavaPlugin;

public final class Enchantreduction extends JavaPlugin {

    @Override
    public void onEnable() {
        // Enregistrer les événements
        getServer().getPluginManager().registerEvents(new EnchantReductionListener(this), this);
        getLogger().info("Enchantreduction activé !");
    }

    @Override
    public void onDisable() {
        getLogger().info("Enchantreduction désactivé !");
    }
}