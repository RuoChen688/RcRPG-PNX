package RcRPG.Form;

import RcRPG.Handle;
import RcRPG.Main;
import RcRPG.RPG.Armour;
import RcRPG.RPG.Stone;
import RcRPG.RPG.Weapon;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.handler.FormResponseHandler;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseData;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class inlayForm {
    private static final String NO_STONE = "无";
    private static final String SUCCESS_MESSAGE = "宝石操作成功！";
    private LinkedHashMap<Player, Stone> playerStone = new LinkedHashMap<>();
    private LinkedHashMap<Player, Integer> playerClick = new LinkedHashMap<>();
    private Item handItem = null;
    private Weapon weaponItem = null;
    private Armour armourItem = null;

    public void makeInlayForm(Player player, Item item) {
        handItem = item;
        //Object state;
        if (Weapon.isWeapon(item)) {
            weaponItem = Main.loadWeapon.get(item.getNamedTag().getString("name"));
            //state = weaponItem;
        } else if (Armour.isArmour(item)) {
            armourItem = Main.loadArmour.get(item.getNamedTag().getString("name"));
            //state = armourItem;
        } else {
            return;
        }

        FormWindowSimple form = getStateWindow();
        form.addHandler(FormResponseHandler.withoutPlayer(ignored -> handleStateWindowResponse(form, player)));
        player.showFormWindow(form);
    }


    private FormWindowSimple getStateWindow() {
        String label = "";
        LinkedList<Stone> stones = new LinkedList<>();
        if (weaponItem != null) {
            stones = Weapon.getStones(handItem);
            label = weaponItem.getLabel();
        } else if (armourItem != null) {
            stones = Armour.getStones(handItem);
            label = armourItem.getLabel();
        }

        int stoneCount = stones.size();
        return new FormWindowSimple(
                label + "宝石列表",
                stoneCount > 0 ? "装备拥有 " + stoneCount + " 个宝石槽" : "本装备没有宝石槽\n"+handItem.getNamedTag().getList("stone").toSNBT(),
                stones.stream()
                        .map(stone -> new ElementButton(stone == null ? NO_STONE : stone.getLabel()))
                        .collect(Collectors.toList()));
    }

    private void handleStateWindowResponse(FormWindowSimple form, Player player) {
        if (form.wasClosed()) {
            return;
        }
        FormResponseSimple response = form.getResponse();
        String clickedStoneLabel = response.getClickedButton().getText();
        Stone clickedStone = NO_STONE.equals(clickedStoneLabel) ? null : Handle.getStoneByLabel(clickedStoneLabel);

        LinkedList<String> playerStones = Stone.getStones(player, clickedStoneLabel);
        playerStones.addFirst(clickedStoneLabel);
        if (!playerStones.contains(NO_STONE)) playerStones.addLast(NO_STONE);

        FormWindowCustom form_ = getStoneWindow(clickedStoneLabel, playerStones);
        form_.addHandler(FormResponseHandler.withoutPlayer(ignored -> {
            if (form_.wasClosed()) return;
            handleStoneWindowResponse(form_, player, clickedStone);
        }));
        player.showFormWindow(form_);

        playerClick.put(player, response.getClickedButtonId());
        playerStone.put(player, clickedStone);
    }

    private FormWindowCustom getStoneWindow(String stoneLabel, LinkedList<String> stones) {
        ElementDropdown dropdown = new ElementDropdown("", stones);
        dropdown.setDefaultOptionIndex(0);
        return new FormWindowCustom(stoneLabel, Collections.singletonList(dropdown));
    }

    private void handleStoneWindowResponse(FormWindowCustom form, Player player, Stone clickedStone) {

        String itemName = "";
        LinkedList<Stone> stones = new LinkedList<>();
        if (weaponItem != null) {
            stones = Weapon.getStones(handItem);
            itemName = weaponItem.getName();
        } else if (armourItem != null) {
            stones = Armour.getStones(handItem);
            itemName = armourItem.getName();
        }

        FormResponseCustom response = form.getResponse();
        FormResponseData responseData = response.getDropdownResponse(0);
        String responseContent = responseData.getElementContent();

        Main.getInstance().getLogger().info("宝石槽数：" + stones.size());

        Item item = player.getInventory().getItemInHand();
        if (item.getNamedTag() == null) return;
        if (!itemName.equals(item.getNamedTag().getString("name"))) return;
        player.sendMessage(SUCCESS_MESSAGE);

        if (responseData.getElementID() != 0) {
            int clickedButtonId = playerClick.get(player);
            Stone newStone = NO_STONE.equals(responseContent) ? null : Handle.getStoneByLabel(responseContent);
            updateStones(stones, clickedButtonId, newStone);

            if (newStone != null) {
                Handle.removeStoneByLabel(player, newStone.getLabel());
            }
            Stone previousStone = playerStone.get(player);// 过去的宝石
            if (previousStone != null) {
                Stone.giveStone(player, previousStone.getName(), 1);
            }

            if (weaponItem != null) {
                Weapon.setStone(player, handItem, stones);
            } else if (armourItem != null) {
                Armour.setStone(player, handItem, stones);
            }
        }

        playerStone.remove(player);
        playerClick.remove(player);
        //makeInlayForm(player, player.getInventory().getItemInHand());
    }

    private void updateStones(LinkedList<Stone> stones, int clickedButtonId, Stone newStone) {
        for (int i = 0; i < stones.size(); i++) {
            if (i == clickedButtonId) {
                if (newStone != null) {
                    stones.set(i, newStone);
                } else {
                    stones.remove(i);
                }
                break;
            }
        }
    }
}