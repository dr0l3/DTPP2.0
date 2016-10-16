package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import runnable.ScrollRunnable;

/**
 * Created by runed on 12-10-2016.
 */
public class ScrollDownCommandAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        Runnable scroll = new ScrollRunnable(editor, 15);
        scroll.run();
    }
}
