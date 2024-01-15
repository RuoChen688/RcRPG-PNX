package RcRPG.window;

import RcRPG.Main;
import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.handler.FormResponseHandler;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.lang.LangCode;

public class RcRPGAdminWin implements Listener {

    public RcRPGAdminWin(Player player) {
        LangCode langCode = player.getLanguageCode();
        FormWindowSimple form = new FormWindowSimple(Main.getI18n().tr(langCode, "rcrpg.window.main.title"), Main.getI18n().tr(langCode, "rcrpg.window.select_config_manage"));
        form.addButton(new ElementButton(Main.getI18n().tr(langCode, "rcrpg.window.main.button1")));
        form.addButton(new ElementButton(Main.getI18n().tr(langCode, "rcrpg.window.main.button2")));
        form.addButton(new ElementButton(Main.getI18n().tr(langCode, "rcrpg.window.main.button3")));
        form.addButton(new ElementButton(Main.getI18n().tr(langCode, "rcrpg.window.main.button4")));

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
