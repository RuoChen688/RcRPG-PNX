package RcRPG.Task;

import RcRPG.FloatingText;
import RcRPG.RcRPGMain;
import cn.nukkit.scheduler.PluginTask;

public class removeFloatingText extends PluginTask {

    protected FloatingText floatingText;

    public removeFloatingText(RcRPGMain rcRPGMain, FloatingText floatingText){
        super(rcRPGMain);
        this.floatingText = floatingText;
    }

    @Override
    public void onRun(int i) {
        this.floatingText.respawnAll();
        this.cancel();
    }
}
