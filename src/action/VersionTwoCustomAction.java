package action;

import popup.SearchPopupListener;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import keylistener.SearchKeyListener;
import marker.Marker2;
import marker.MarkerPanel2;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by runed on 15-10-2016.
 */
public abstract class VersionTwoCustomAction extends AnAction{
    protected JComponent contentComponent;
    protected Editor editor;
    protected KeyListener searchListener;
    protected MarkerPanel2 markerPanel;
    protected JTextField searchTextField;
    protected SearchPopupListener popupListener;
    protected JBPopup popup;
    protected boolean isSelecting;
    protected boolean isSecondOverlay;
    protected int offsetFromFirstOverlay;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //setup
        Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        if (editor == null || project == null) {
            return;

        }
        contentComponent = editor.getContentComponent();
        searchListener = new SearchKeyListener(this);

        markerPanel = new MarkerPanel2(editor, this, editor.getCaretModel().getCurrentCaret().getOffset());
        contentComponent.add(markerPanel);
        isSelecting = false;
        isSecondOverlay = false;

        setupPopup();
    }

    protected void setupPopup() {
        if(popup != null){
            popup.removeListener(popupListener);
            popup.cancel();
        }
        RelativePoint popupLocation = JBPopupFactory.getInstance().guessBestPopupLocation(contentComponent);

        JPanel panel = new JPanel(new BorderLayout());
        searchTextField = new JTextField();
        searchTextField.setText("");
        searchTextField.setColumns(10);
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(isSelecting()){
                    return;
                }
                handleSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(isSelecting()){
                    return;
                }
                handleSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if(isSelecting()){
                    return;
                }
                handleSearch();
            }
        });
        panel.add(searchTextField, BorderLayout.WEST);
        searchTextField.addKeyListener(searchListener);

        searchTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "setSelecting");
        searchTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK), "performActionAtFirstUp");
        searchTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.ALT_DOWN_MASK), "performActionAtFirstDown");
        searchTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK), "performActionAtAll");


        searchTextField.getActionMap().put("setSelecting", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSelecting = true;
                searchTextField.setEditable(false);
                System.out.println("now selecting");
            }
        });

        searchTextField.getActionMap().put("performActionAtFirstUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("going to first occurence up");
                handleSelectFirstOccurence(true);
            }
        });

        searchTextField.getActionMap().put("performActionAtFirstDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("going to first occurence down");
                handleSelectFirstOccurence(false);
            }
        });

        searchTextField.getActionMap().put("performActionAtAll", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("doing all the things");
                handleSelectAll();
            }
        });

        popupListener = new SearchPopupListener(this);

        popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, panel)
                .addListener(popupListener)
                .setFocusable(true)
                .setMovable(false)
                .setShowBorder(true)
                .createPopup();

        popup.show(popupLocation);

        searchTextField.requestFocus();
    }

    private void handleSelectAll() {
        markerPanel.handleSelectAll();
    }

    public void handleSelectFirstOccurence(boolean upwards){
        markerPanel.handleSelectFirstOccurence(upwards);
    }

    public void handleSearch(){
        markerPanel.updateMarkers(searchTextField.getText());
        contentComponent.repaint();
    }

    public void handleSelect(String selectedChar){
        markerPanel.handleSelect(selectedChar);
    }

    public void exitAction(){
        markerPanel.updateMarkers("");
        contentComponent.remove(markerPanel);
        contentComponent.repaint();
        if(!popup.isDisposed()){
            popup.cancel();
        }
        this.popup = null;
    }

    public boolean isSelecting() {
        return isSelecting;
    }

    protected void setupSecondOverLay(Marker2 marker) {
        isSecondOverlay = true;
        offsetFromFirstOverlay = marker.getStartOffset();
        isSelecting = false;
        setupPopup();
        handleSearch();
    }

    public abstract void PerformActionAtMarker(Marker2 marker);

    public abstract void PerformActionAtMultipleMarkers(List<Marker2> markers);
}
