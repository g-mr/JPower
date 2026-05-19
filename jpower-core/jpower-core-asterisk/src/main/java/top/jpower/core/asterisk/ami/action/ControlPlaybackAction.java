package top.jpower.core.asterisk.ami.action;

import lombok.Data;
import org.asteriskjava.manager.action.AbstractManagerAction;

@Data
public class ControlPlaybackAction extends AbstractManagerAction {
    private String channel;
    private String control;

    @Override
    public String getAction() {
        return "ControlPlayback";
    }
}
