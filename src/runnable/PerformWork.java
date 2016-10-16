package runnable;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import keylisteners.SearchKeyListener;
import marker.MarkerPanel2;

import javax.swing.*;
import java.awt.event.KeyListener;

/**
 * Created by runed on 15-10-2016.
 */
public class PerformWork implements Runnable {
    private Project project;
    private Editor editor;
    private JComponent contentComponent;

    public PerformWork(Project project, Editor editor) {
        this.project = project;
        this.editor = editor;
        this.contentComponent = editor.getContentComponent();
    }

    @Override
    public void run() {

    }
}
