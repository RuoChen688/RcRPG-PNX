package RcRPG.window;

import RcRPG.Main;
import RcRPG.RPG.Ornament;
import RcRPG.RPG.Stone;
import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.handler.FormResponseHandler;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;

public class SendOrnamentAdminWin implements Listener {

    public SendOrnamentAdminWin(Player player) {
        FormWindowSimple form = new FormWindowSimple("RcRPG管理 - 饰品列表", "请选择你需要管理的配置");
        for (String key : Main.loadOrnament.keySet()) {
            form.addButton(new ElementButton(key));
        }
        form.addHandler(FormResponseHandler.withoutPlayer(ignored -> {
            if (form.wasClosed()) {
                return;
            }
            FormResponseSimple response = form.getResponse();
            String key = response.getClickedButton().getText();
            SendStoneOptionsWin(player, key);
        }));
        player.showFormWindow(form);
    }

    public void SendStoneOptionsWin(Player player, String stoneKey) {
        Ornament stone = Main.loadOrnament.get(stoneKey);

        FormWindowSimple form = new FormWindowSimple("RcRPG管理 - " + stoneKey,
                "显示名称: " + stone.getShowName() +
                        "\n§r标签: " + stone.getLabel() +
                        "\n§r物品ID: " + stone.getConfig().get("物品ID") +
                        "\n§r介绍: " + stone.getMessage());
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
                    if (Stone.giveStone(player, stoneKey, 1)) {
                        player.sendMessage("给予成功");
                    } else {
                        player.sendMessage("给予失败");
                    }
                }
                case 1 -> {
                    player.sendMessage("还没完成这个");
                }
                case 2 -> {
                    new SendOrnamentAdminWin(player);
                }
            }
        }));
        player.showFormWindow(form);
    }
}