package RcRPG.panel.dismantle;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;

import java.util.LinkedHashMap;
import java.util.Map;

public class DismantlePanel implements InventoryHolder {

    public static Map<Integer, Item> getPanel(Player player) {
        Map<Integer, Item> panel = new LinkedHashMap<>();
        Item tipItem = Item.get("minecraft:oak_hanging_sign");
        tipItem.setCustomName("提示");
        tipItem.setLore(
                "将武器、防具放入`分解炉`中关闭即可",
                "请确保背包空闲空间充足"
        );
        panel.put(0, tipItem);
        return panel;
    }

    public void sendPanel(Player player) {
        Map<Integer, Item> panel = getPanel(player);
        displayPlayer(player, panel);
    }

    public void displayPlayer(Player player, Map<Integer, Item> itemMap) {
        DismantleInventory inv = new DismantleInventory(this, "分解炉");
        inv.setContents(itemMap);
        inv.id = Entity.entityCount++;
        player.addWindow(inv);
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
