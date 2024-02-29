package RcRPG;

import RcRPG.RPG.Armour;
import RcRPG.RPG.Ornament;
import RcRPG.RPG.Stone;
import RcRPG.RPG.Weapon;
import cn.ankele.plugin.MagicItem;
import cn.ankele.plugin.bean.ItemBean;
import cn.ankele.plugin.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;

import java.util.LinkedHashMap;

import static cn.ankele.plugin.utils.BaseCommand.createItem;

public class Utils {

    /**
     * 向玩家背包添加物品
     *
     * @param player 要添加物品的玩家
     * @param item   要添加到玩家背包的物品
     */
    public static void addItemToPlayer(Player player, Item item) {
        if (player.getInventory().canAddItem(item)) {
            player.getInventory().addItem(item);
        } else {
            player.sendPopup("你有一些 "+item.getName()+" 装不下掉到了地上。");
            player.getLevel().dropItem(player, item);
        }
    }

    public static Item parseItemString(String str) {
        String[] arr = str.split("@");
        if (arr[0].equals("mi")) {// mi@1 代金券
            if (Server.getInstance().getPluginManager().getPlugin("MagicItem") == null) {
                RcRPGMain.getInstance().getLogger().warning("你没有使用 MagicItem 插件却在试图获取它的物品：" + str);
                return Item.AIR_ITEM;
            }
            LinkedHashMap<String, ItemBean> items = MagicItem.getItemsMap();
            LinkedHashMap<String, Object> otherItems = MagicItem.getOthers();
            String[] args = arr[1].split(" ");
            if (items.containsKey(args[1])) {
                ItemBean item = items.get(args[1]);
                Item back = createItem(item);
                back.setCount(Integer.parseInt(args[0]));
                return back;
            } else if (otherItems.containsKey(args[1])) {
                String[] otherItemArr = ((String) otherItems.get(args[1])).split(":");
                Item item = Item.get(Integer.parseInt(otherItemArr[0]), Integer.parseInt(otherItemArr[1]));
                item.setCount(Integer.parseInt(args[0]));
                item.setCompoundTag(Tools.hexStringToBytes(otherItemArr[3]));
                return item;
            } else {
                RcRPGMain.getInstance().getLogger().warning("MagicItem物品不存在：" + args[1]);
            }
        } else if (arr[0].equals("item")) {
            String[] args = arr[1].split(" ");
            Item item = Item.fromString(args[0]);
            if (args.length == 2) {
                item.setCount(Integer.parseInt(args[1]));
            } else {
                item.setDamage(Integer.parseInt(args[1]));
                item.setCount(Integer.parseInt(args[2]));
            }
            return item;
        } else if (arr[0].equals("nweapon") || arr[0].equals("rcrpg")) {
            if (Server.getInstance().getPluginManager().getPlugin("RcRPG") == null) {
                RcRPGMain.getInstance().getLogger().warning("你没有使用 RcRPG、NWeapon 插件却在试图获取它的物品：" + str);
                return Item.AIR_ITEM;
            }
            String[] args = arr[1].split(" ");//Main.loadWeapon
            String type = args[0];
            String itemName = args[1];
            int count = 1;

            if (args.length > 2) {
                count = Integer.parseInt(args[2]);
            }

            switch (type) {
                case "护甲":
                case "防具":
                case "armour":
                case "armor": {
                    if (RcRPGMain.loadArmour.containsKey(itemName)) {
                        return Armour.getItem(itemName, count);
                    }
                    break;
                }
                case "武器":
                case "weapon": {
                    if (RcRPGMain.loadWeapon.containsKey(itemName)) {
                        return Weapon.getItem(itemName, count);
                    }
                    break;
                }
                case "宝石":
                case "stone":
                case "gem": {
                    if (RcRPGMain.loadStone.containsKey(itemName)) {
                        return Stone.getItem(itemName, count);
                    }
                    break;
                }
                case "饰品":
                case "ornament":
                case "jewelry": {
                    if (RcRPGMain.loadOrnament.containsKey(itemName)) {
                        return Ornament.getItem(itemName, count);
                    }
                    break;
                }
                case "锻造图": {
                    break;
                }
                case "宝石券":
                case "精工石":
                case "强化石":
                case "锻造石": {
                    break;
                }
            }
            return Item.AIR_ITEM;
            //return nWeapon.onlyNameGetItem(args[0], args[1], args[2], null);
        } else {
            RcRPGMain.getInstance().getLogger().warning("物品配置有误：" + str);
        }
        return Item.AIR_ITEM;
    }
}
