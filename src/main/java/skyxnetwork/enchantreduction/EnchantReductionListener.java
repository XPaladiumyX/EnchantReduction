package skyxnetwork.enchantreduction;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

public class EnchantReductionListener implements Listener {

    private final Enchantreduction plugin;

    public EnchantReductionListener(Enchantreduction plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareItemEnchant(PrepareItemEnchantEvent event) {
        Player player = event.getEnchanter();
        int reduction = getReduction(player);

        // Modifier les coûts des niveaux d'expérience offerts
        int[] expLevelCosts = event.getExpLevelCostsOffered();

        for (int i = 0; i < expLevelCosts.length; i++) {
            int originalCost = expLevelCosts[i];
            int reducedCost = applyReduction(originalCost, reduction); // Réduction appliquée
            expLevelCosts[i] = reducedCost;
        }
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory(); // Récupérer l'inventaire de l'enclume
        Player player = (Player) event.getView().getPlayer(); // Joueur utilisant l'enclume

        if (player != null && event.getResult() != null) {
            int reduction = getReduction(player);
            int originalCost = anvilInventory.getRepairCost();
            int reducedCost = applyReduction(originalCost, reduction); // Réduction appliquée
            anvilInventory.setRepairCost(reducedCost);
        }
    }

    /**
     * Calculer la réduction basée sur les permissions.
     *
     * @param player Le joueur pour lequel on vérifie les permissions.
     * @return Le pourcentage de réduction applicable (1-100).
     */
    private int getReduction(Player player) {
        int maxReduction = 0;

        // Vérifier toutes les permissions entre 1 et 100
        for (int i = 1; i <= 100; i++) {
            if (player.hasPermission("skyxnetwork.enchantreduction." + i)) {
                maxReduction = Math.max(maxReduction, i); // Prendre la réduction la plus élevée
            }
        }
        return maxReduction;
    }

    /**
     * Appliquer la réduction sur un coût donné.
     *
     * @param originalCost Le coût initial.
     * @param reduction    Le pourcentage de réduction à appliquer.
     * @return Le coût réduit (minimum 1).
     */
    private int applyReduction(int originalCost, int reduction) {
        return Math.max(1, originalCost - (originalCost * reduction / 100));
    }
}