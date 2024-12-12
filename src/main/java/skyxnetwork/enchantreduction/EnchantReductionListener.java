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

        // Utiliser les coûts offerts directement
        int[] expLevelCosts = event.getExpLevelCostsOffered();

        for (int i = 0; i < expLevelCosts.length; i++) {
            int originalCost = expLevelCosts[i]; // Obtenir le coût original
            int reducedCost = Math.max(1, originalCost - (originalCost * reduction / 100)); // Appliquer la réduction
            expLevelCosts[i] = reducedCost; // Mettre à jour le coût réduit
        }
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory(); // No casting needed
        Player player = (Player) event.getView().getPlayer(); // Get the player using the anvil

        if (player != null && event.getResult() != null) {
            int reduction = getReduction(player);
            int originalCost = anvilInventory.getRepairCost();
            int reducedCost = Math.max(1, originalCost - (originalCost * reduction / 100));
            anvilInventory.setRepairCost(reducedCost);
        }
    }

    private int getReduction(Player player) {
        // Check permissions from 100% to 10%
        for (int i = 100; i >= 10; i -= 10) {
            if (player.hasPermission("skyxnetwork.enchantreduction." + i)) {
                return i;
            }
        }
        return 0; // Default no reduction
    }
}