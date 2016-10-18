package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import util.EditorUtil;

/**
 * Created by runed on 16-10-2016.
 */
public class ScrollUp extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        int linesToMove = -15;
        EditorUtil.performScrollByLinenumber(editor,linesToMove);
    }
}
