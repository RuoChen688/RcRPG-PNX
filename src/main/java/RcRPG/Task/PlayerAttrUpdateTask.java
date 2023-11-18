package RcRPG.Task;

import RcRPG.AttrManager.PlayerAttr;
import RcRPG.Main;
import cn.nukkit.Player;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.PluginTask;
import healthapi.PlayerHealth;

import java.util.Objects;

import static RcRPG.Handle.classExists;

public class PlayerAttrUpdateTask extends PluginTask {
    public PlayerAttrUpdateTask(Main main){
        super(main);
    }

    @Override
    public void onRun(int i) {
        for (Player player : Main.instance.getServer().getOnlinePlayers().values()) {
            if (!player.isAlive()) continue;
            int addHealth = 0;
            if (!PlayerAttr.playerslist.containsKey(player)) continue;
            Objects.requireNonNull(PlayerAttr.getPlayerAttr(player)).update();
            PlayerAttr pAttr = PlayerAttr.getPlayerAttr(player);
            if (pAttr == null) continue;
            int maxh;
            if (pAttr.hp != 0) {
                maxh = (int) (pAttr.hp * (1 + pAttr.hpRegenMultiplier));
            } else {
                maxh = 20 * (1 + (int) pAttr.hpRegenMultiplier);
            }



            addHealth += pAttr.hpPerSecond;

            // TODO: 这是依赖于 AttrManager 的 Effect 实现
            /*
            effect = GetPlayerAttr(player, 1).Effect;
            const nowTime =  Number((new Date().getTime()/1000).toFixed(0))+0.5;
            for (let n in effect) {
                if (nowTime > effect[n].time) {
                    SetPlayerAttr(player, "Effect", {id: n, level: 0, time: 0});
                    continue;
                }
            }*/

            if (player.hasEffect(20)) {// 凋零每秒减 效果等级 x 1%最大生命
                Effect effect = player.getEffect(20);
                addHealth -= ((effect.getAmplifier() + 1) * Math.ceil(0.01 * maxh));
            }
            if (addHealth != 0) {
                player.heal(addHealth);
            }
            if (classExists("healthapi.PlayerHealth")) {// 若存在血量核心
                PlayerHealth playerHealth = PlayerHealth.getPlayerHealth(player);
                playerHealth.setMaxHealth("rcrpg", maxh);
                double H = playerHealth.getHealth();
                int MaxH = playerHealth.getMaxHealth();
                if (H != 0 && H < MaxH && (int) player.getHealth() == player.getMaxHealth()) {
                    player.setHealth((float) Math.floor(40 * H / MaxH));
                }
            }
        }
    }
}