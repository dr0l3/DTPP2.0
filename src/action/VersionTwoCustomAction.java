package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import keylistener.SearchKeyListener;
import marker.Marker2;
import marker.MarkerPanel2;
import popup.SearchPopupListener;
import util.ListenerUtil;
import util.OneOffsetEditorAction;
import util.TwoOffsetEditorAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * Created by runed on 15-10-2016.
 */
public abstract class VersionTwoCustomAction extends AnAction {
    protected JComponent contentComponent;
    protected MarkerPanel2 markerPanel;
    protected SearchPopupListener popupListener;
    protected JBPopup popup;
    protected boolean isSelecting;
    protected boolean isSecondOverlay;
    protected int offsetFromFirstOverlay;
    protected boolean exitOnEscape;
    protected KeyListener[] suspendedKeylisteners;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        if (editor == null || project == null) {
            return;
        }
        onActionPerformed(editor, "");
    }

    public void onActionPerformed(Editor editor, String initialString){
        //setup
        exitOnEscape = true;
        Project project = editor.getProject();
        contentComponent = editor.getContentComponent();
        markerPanel = new MarkerPanel2(editor, this, editor.getCaretModel().getCurrentCaret().getOffset());
        contentComponent.add(markerPanel);
        isSelecting = false;
        isSecondOverlay = false;
        JTextField searchTextField = setupSearchTextField();
        //listens for acitons when the textfield is locked and we are selecting
        searchTextField.addKeyListener(new SearchKeyListener(this, markerPanel));

        //listens for actions when the textfield is not locked and we are not selecting
        ListenerUtil.getDocumentListeners(this, markerPanel)
                .forEach(searchTextField.getDocument()::addDocumentListener);
        //listens for special actions like enter, scroll etc.
        ListenerUtil.getKeybindings(this,markerPanel,searchTextField)
                .forEach(binding ->{
                    searchTextField.getInputMap().put(binding.getKeyStroke(),binding.getStringIntermediary());
                    searchTextField.getActionMap().put(binding.getStringIntermediary(), binding.getAction());
                });
        popupListener = new SearchPopupListener(this, editor);
        JPanel panel = new JPanel(new BorderLayout());

        decommissionPopup(popup);
        popup = setupPopupAndBindActionListeners(searchTextField, popupListener, panel);
        calculatePositionAndShowPopup(popup, contentComponent);
        searchTextField.setText(initialString);
        searchTextField.requestFocus();
    }

    protected void decommissionPopup(JBPopup popup) {
        if (popup != null) {
            if(popupListener != null) {
                popup.removeListener(popupListener);
            }
            popup.cancel();
        }
    }

    private JBPopup setupPopupAndBindActionListeners(JTextField searchTextField, SearchPopupListener listener, JPanel panel) {
        JBPopup popup;
        panel.add(searchTextField, BorderLayout.WEST);

        popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, panel)
                .addListener(listener)
                .setFocusable(true)
                .setMovable(false)
                .setShowBorder(true)
                .createPopup();
        return popup;
    }

    private void calculatePositionAndShowPopup(JBPopup popup, JComponent contentComponent) {
        RelativePoint popupLocation = JBPopupFactory.getInstance().guessBestPopupLocation(contentComponent);
        popup.show(popupLocation);
    }

    private JTextField setupSearchTextField() {
        JTextField textField = new JTextField();
        textField.setText("");
        textField.setColumns(10);

        return textField;
    }

    public void exitAction(Editor editor) {
        if(!exitOnEscape){
            return;
        }
        if(isSecondOverlay && !isSelecting){
            onActionPerformed(editor, "");
            editor.getMarkupModel().removeAllHighlighters();
            return;
        }
        editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
        editor.getMarkupModel().removeAllHighlighters();
        contentComponent.remove(markerPanel);
        contentComponent.repaint();
        if (popup != null && !popup.isDisposed()) {
            popup.cancel();
        }
        this.popup = null;
    }

    public boolean isSelecting() {
        return isSelecting;
    }

    public void setupSecondOverLay(Marker2 marker, MarkerPanel2 markerPanel, Editor editor) {
        markerPanel.setSelectCharCount(0);
        isSecondOverlay = true;
        offsetFromFirstOverlay = marker.getStartOffset();
        isSelecting = false;
        decommissionPopup(popup);
        JTextField searchTextField = setupSearchTextField();
        searchTextField.addKeyListener(new SearchKeyListener(this, markerPanel));
        ListenerUtil.getDocumentListeners(this, markerPanel)
                .forEach(searchTextField.getDocument()::addDocumentListener);
        ListenerUtil.getKeybindings(this,markerPanel,searchTextField)
                .forEach(binding ->{
                    searchTextField.getInputMap().put(binding.getKeyStroke(),binding.getStringIntermediary());
                    searchTextField.getActionMap().put(binding.getStringIntermediary(), binding.getAction());
                });
        SearchPopupListener popupListener = new SearchPopupListener(this, editor);
        JPanel panel = new JPanel(new BorderLayout());

        popup = setupPopupAndBindActionListeners(searchTextField, popupListener, panel);
        calculatePositionAndShowPopup(popup, contentComponent);
        searchTextField.requestFocus();
        markerPanel.updateMarkers("");
    }

    public abstract void initiateActionAtMarker(Marker2 marker, Editor editor, MarkerPanel2 markerPanel);

    public abstract void initiateActionAtMarkers(List<Marker2> markers, Editor editor, MarkerPanel2 markerPanel);

    public void findOffsetsAndPerformAction(TwoOffsetEditorAction toBePerformed, Marker2 marker, Editor editor) {
        int offset = marker.getStartOffset();
        int currentOffset;
        if (isSecondOverlay) {
            currentOffset = offsetFromFirstOverlay;
        } else {
            currentOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        }
        if (currentOffset < offset) {
            toBePerformed.performAction(currentOffset, offset, editor);
        } else {
            toBePerformed.performAction(offset, currentOffset, editor);
        }
    }

    public void findSingleOffsetAndPerformAction(OneOffsetEditorAction toBePerformed, Marker2 marker, Editor editor) {
        int offset = marker.getStartOffset();
        toBePerformed.performAction(offset, editor);
    }

    public void setSelecting(boolean selecting) {
        isSelecting = selecting;
    }

    public boolean isSecondOverlay() {
        return isSecondOverlay;
    }

    public void setSecondOverlay(boolean secondOverlay) {
        isSecondOverlay = secondOverlay;
    }

    public void setExitOnEscape(boolean exitOnEscape) {
        this.exitOnEscape = exitOnEscape;
    }

    public KeyListener[] getSuspendedKeylisteners() {
        return suspendedKeylisteners;
    }
}
