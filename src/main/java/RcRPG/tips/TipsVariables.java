package RcRPG.tips;

import RcRPG.Handle;
import RcRPG.RPG.Level;
import cn.nukkit.Player;
import tip.utils.variables.BaseVariable;

public class TipsVariables extends BaseVariable {

    public TipsVariables(Player player) {
        super(player);
    }

    @Override
    public void strReplace() {
        if(player != null) {
            repl();
        }
    }
    private void repl(){
        addStrReplaceString("{rcrpg_exp}", String.valueOf(Level.getExp(player)));
        addStrReplaceString("{rcrpg_max_exp}", String.valueOf(Level.getMaxExp(player)));
        addStrReplaceString("{rcrpg_level}", String.valueOf(Level.getLevel(player)));
        addStrReplaceString("{rcrpg_prefix}", String.valueOf(Handle.getPlayerConfig(player.getName()).getString("称号")));
        addStrReplaceString("{rcrpg_guild}", String.valueOf(Handle.getPlayerConfig(player.getName()).getString("公会")));
    }

}