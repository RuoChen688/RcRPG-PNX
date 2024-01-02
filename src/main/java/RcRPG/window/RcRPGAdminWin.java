package RcRPG.window;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.handler.FormResponseHandler;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;

public class RcRPGAdminWin implements Listener {

    public RcRPGAdminWin(Player player) {
        FormWindowSimple form = new FormWindowSimple("RcRPG管理 - 饰品列表", "请选择你需要管理的配置");
        form.addButton(new ElementButton("武器"));
        form.addButton(new ElementButton("护甲"));
        form.addButton(new ElementButton("宝石"));
        form.addButton(new ElementButton("饰品"));

        form.addHandler(FormResponseHandler.withoutPlayer(ignored -> {
            if (form.wasClosed()) {
                return;
            }
            FormResponseSimple response = form.getResponse();
            int key = response.getClickedButtonId();
            switch (key) {
                case 0 -> {
                    new SendWeaponAdminWin(player);
                }
                case 1 -> {
                    new SendArmourAdminWin(player);
                }
                case 2 -> {
                    new SendStoneAdminWin(player);
                }
                case 3 -> {
                    new SendOrnamentAdminWin(player);
                }
            }
        }));
        player.showFormWindow(form);
    }
}
