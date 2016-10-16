package API;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.DialogWrapperPeer;
import com.intellij.openapi.ui.impl.DialogWrapperPeerImpl;
import com.intellij.structuralsearch.plugin.StructuralSearchPlugin;
import com.intellij.structuralsearch.plugin.ui.SubstitutionShortInfoHandler;
import com.intellij.ui.JBColor;
import org.apache.batik.apps.svgbrowser.JPEGOptionPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

import static com.intellij.testFramework.LightPlatformTestCase.getProject;

/**
 * Created by runed on 15-10-2016.
 */
public class SearchDialog extends DialogWrapper {
    private JTextField searchTextField;
    private JPanel panel;
    public SearchDialog(@Nullable Project project) {
        super(project, true, true);

        this.searchTextField = new JTextField();
        this.searchTextField.setText("hehe");
        this.searchTextField.setColumns(10);
        this.panel = new JPanel(new BorderLayout());
        this.panel.add(this.searchTextField, BorderLayout.WEST);


        init();
    }


    @Override
    public JRootPane getRootPane() {
        return new JRootPane();
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.panel;
    }


    @Override
    public JComponent getPreferredFocusedComponent(){
        return this.searchTextField;
    }

    @Override
    public Action[] createActions(){
        return new Action[0];
    }


    @Override
    public JComponent createSouthPanel(){

        return new JPanel(
        );
    }

    @Override
    public void show(){
        super.show();
    }
}
