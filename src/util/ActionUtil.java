package util;

import action.VersionTwoCustomAction;
import com.intellij.openapi.actionSystem.AnAction;

import java.awt.event.ActionEvent;

/**
 * Created by runed on 22-10-2016.
 */
public class ActionUtil {
    private VersionTwoCustomAction action;

    public ActionUtil(VersionTwoCustomAction action) {
        this.action = action;
    }

    public void handleSelectAll(ActionEvent e){
        action.handleSelectAll();
    }
}
