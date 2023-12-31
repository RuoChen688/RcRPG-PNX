package RcRPG.panel.dismantle;

import RcRPG.Main;
import RcRPG.RPG.Armour;
import RcRPG.RPG.Weapon;
import RcRPG.Utils;
import RcRPG.panel.lib.ChestFakeInventory;
import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.utils.ConfigSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DismantleInventory extends ChestFakeInventory {

    public long id;

    public DismantleInventory(InventoryHolder holder, String name) {
        super(InventoryType.CHEST, holder, name);
        this.setName(name);
    }

    @Override
    public void onOpen(Player who) {
        super.onOpen(who);
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = who.getWindowId(this);
        pk.entityId = id;
        pk.type = InventoryType.CHEST.getNetworkType();
        who.dataPacket(pk);
    }

    @Override
    public void onClose(Player who) {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = id;
        who.dataPacket(pk);
        super.onClose(who);
        Map<Integer, Item> content = this.getContents();
        List<Item> invItemList = content.values().stream().toList();
        // TODO: 处理物品分解
        ArrayList<String> tipPopup = new ArrayList<>();
        for (int i = 1; i < invItemList.size(); i++) {
            String dismantlePlan = "";
            Item item = invItemList.get(i);
            String itemName = item.getNamedTag().getString("name");
            if (Weapon.isWeapon(item)) {
                Weapon weapon = Main.loadWeapon.get(itemName);
                if (weapon != null) {
                    dismantlePlan = weapon.getDismantle();
                }
            } else if (Armour.isArmour(item)) {
                Armour armour = Main.loadArmour.get(itemName);
                if (armour != null) {
                    dismantlePlan = armour.getDismantle();
                }
            }
            if (dismantlePlan.isEmpty() || !Main.getInstance().dismantleConfig.exists(dismantlePlan)) {
                Utils.addItemToPlayer(who, item);// 返还物品
                continue;
            }
            tipPopup.add("已成功分解 " + item.getCustomName()+ "。");
            ConfigSection plan = Main.getInstance().dismantleConfig.getSection(dismantlePlan);

            for (String round : plan.keySet()) {
                ArrayList<String> list = (ArrayList<String>) plan.get(round);
                if(new Random().nextInt(100)+1 <= Integer.parseInt(round)) {
                    list.forEach(str -> {
                        Item result = Utils.parseItemString(str);
                        if (!result.isNull()) {
                            Utils.addItemToPlayer(who, result);
                        }
                    });
                }
            }
        }
        if (tipPopup.size() > 0) {
            who.sendPopup(String.join("\n§r§f", tipPopup)+"\n§r§f共计 "+tipPopup.size()+" 件");
        } else {
            who.sendPopup("所有装备均分解失败。");
        }
    }

}
