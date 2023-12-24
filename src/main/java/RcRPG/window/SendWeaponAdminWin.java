package RcRPG.window;

import RcRPG.Main;
import RcRPG.RPG.Weapon;
import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.handler.FormResponseHandler;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;

public class SendWeaponAdminWin implements Listener { //一般实际开发中不在这个类中写监听器

    public SendWeaponAdminWin(Player player) {
        FormWindowSimple form = new FormWindowSimple("RcRPG管理 - 武器列表", "请选择你需要管理的配置");
        for (String key : Main.loadWeapon.keySet()) {
            form.addButton(new ElementButton(key));
        }
        form.addHandler(FormResponseHandler.withoutPlayer(ignored -> {
            if (form.wasClosed()) {
                return;
            }
            FormResponseSimple response = form.getResponse();
            String key = response.getClickedButton().getText();
            SendWeaponOptionsWin(player, key);
        }));
        player.showFormWindow(form);
    }

    public void SendWeaponOptionsWin(Player player, String weaponKey) {
        Weapon weapon = Main.loadWeapon.get(weaponKey);

        FormWindowSimple form = new FormWindowSimple("RcRPG管理 - " + weaponKey,
                "显示名称: " + weapon.getShowName() +
                        "\n§r标签: " + weapon.getLabel() +
                        "\n§r物品ID: " + weapon.getConfig().get("物品ID") +
                        "\n§r最低使用等级: " + weapon.getLevel() +
                        "\n§r介绍: " + weapon.getMessage());
        form.addButton(new ElementButton("获取"));
        form.addButton(new ElementButton("修改 (未完成)"));
        form.addButton(new ElementButton("返回"));
        form.addHandler(FormResponseHandler.withoutPlayer(ignored -> {
            if (form.wasClosed()) {
                return;
            }
            FormResponseSimple response = form.getResponse();
            int key = response.getClickedButtonId();
            switch (key) {
                case 0 -> {
                    if (Weapon.giveWeapon(player, weaponKey, 1)) {
                        player.sendMessage("给予成功");
                    } else {
                        player.sendMessage("给予失败");
                    }
                }
                case 1 -> {
                    player.sendMessage("还没完成这个");
                }
                case 2 -> {
                    new SendWeaponAdminWin(player);
                }
            }
        }));
        player.showFormWindow(form);
    }
}