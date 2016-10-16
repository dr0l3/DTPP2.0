package marker;

import action.VersionTwoCustomAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

import static util.EditorUtil.getVisibleTextRange;


/**
 * Created by runed on 15-10-2016.
 */
public class MarkerPanel2 extends JComponent{
    private static final String MARKER_SET = "ASKDLJQOWIEXUHCVNMBZRFTGYPÅØÆ";
    private Editor editor;
    private ArrayList<Marker2> markerCollection;
    private String searchString;
    private JComponent parent;
    private Font fontIneditor;
    private VersionTwoCustomAction callingAction;
    private int contextPoint;

    public MarkerPanel2(Editor editor, VersionTwoCustomAction callingAction, int contextPoint) {
        this.editor = editor;
        this.parent = editor.getContentComponent();
        this.markerCollection = new ArrayList<>();
        this.fontIneditor = editor.getColorsScheme().getFont(EditorFontType.BOLD);
        this.searchString = "";
        this.callingAction = callingAction;
        this.contextPoint = contextPoint;
        setupLocationAndBoundsOfPanel(editor);
    }

    public void updateMarkers(String searchString){
        String oldSearchString = this.searchString;
        if(oldSearchString.equals(searchString)){
            return;
        }

        System.out.println("Now searching for string: " + searchString);
        this.searchString = searchString;
        if(searchString.equals("")){
            markerCollection = new ArrayList<>();
            this.repaint();
            return;
        }

        this.markerCollection = getMatchesForStringInTextRange(searchString, getVisibleTextRange(editor), editor.getDocument());
        if(markerCollection.isEmpty()){
            this.markerCollection = getMatchesForStringInTextRange(searchString, new TextRange(0,editor.getDocument().getTextLength()), editor.getDocument());
        }
        SortMarkersAndAssignReplacementText();
        this.repaint();
    }

    private void SortMarkersAndAssignReplacementText() {
        this.markerCollection.sort((o1, o2) -> {
            int o1ToCaret = Math.abs(o1.getStartOffset() - this.contextPoint);
            int o2ToCaret = Math.abs(o2.getStartOffset() - this.contextPoint);
            return o1ToCaret - o2ToCaret;
        });

        char[] markerSetChars = MARKER_SET.toCharArray();
        for(int i = 0; i < markerCollection.size(); i++){
            Marker2 marker = markerCollection.get(i);
            if(i < MARKER_SET.length()-2){
                marker.setReplacementText(String.valueOf(markerSetChars[i]));
                marker.setPrimaryMarker(true);
                continue;
            }
            marker.setReplacementText(String.valueOf(markerSetChars[markerSetChars.length-1]));
            marker.setPrimaryMarker(false);
        }
        if (markerCollection.size() == 1){
            handleSelect(markerCollection.get(0).getReplacementText());
        }

    }

    private ArrayList<Marker2> getMatchesForStringInTextRange(String typedChar, TextRange visibleTextRange, Document document) {
        ArrayList<Marker2> markers = new ArrayList<>();
        int startOffset = visibleTextRange.getStartOffset();
        int currentCaretOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        String visibleText = document.getText(visibleTextRange).toLowerCase();
        visibleText = visibleText.replace("\n", " ");
        visibleText = visibleText.replace("\t", " ");
        int index = visibleText.indexOf(typedChar);
        while(index >= 0){
            int offset = startOffset + index;
            index = visibleText.indexOf(typedChar, index + 1);
            if(offset == currentCaretOffset){
                continue;
            }
            if(typedChar.equals(" ") && visibleText.charAt(offset-1) == ' '){
                continue;
            }
            markers.add(new Marker2(offset, offset, typedChar, ""));
        }
        return markers;
    }


    @Override
    public void paint(Graphics g){
        g.setFont(fontIneditor);
        MarkupModel markupModel = editor.getMarkupModel();
        markupModel.removeAllHighlighters();
        for (Marker2 marker : markerCollection){
            drawBackgroundOfMarker(g, marker);
            drawMarkerChar(g,marker);
        }
    }

    private void drawBackgroundOfMarker(Graphics graphics, Marker2 marker) {
        Rectangle2D fontRect = parent.getFontMetrics(fontIneditor).getStringBounds(String.valueOf(marker.getMarkerText()), graphics);

        graphics.setColor(JBColor.WHITE);

        double x = parent.getX() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getStartOffset())).getX();
        double y = parent.getY() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getEndOffset())).getY();

        graphics.fillRect((int)x, (int)y, (int) fontRect.getWidth(), (int) fontRect.getHeight());
        if(marker.getMarkerText().length()> 1) {
            graphics.setColor(JBColor.GRAY);
            x = parent.getX() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getStartOffset()+1)).getX();
            y = parent.getY() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getEndOffset())).getY();
            float bottomYOfMarkerChar = (float) (y + fontIneditor.getSize());
            graphics.drawString(marker.getMarkerText().substring(1, marker.getMarkerText().length()), (int) x, (int) bottomYOfMarkerChar);
        }
    }

    private void drawMarkerChar(Graphics g, Marker2 marker) {
        double x = parent.getX() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getStartOffset())).getX();
        double y = parent.getY() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getStartOffset())).getY();
        float bottomYOfMarkerChar = (float) (y + fontIneditor.getSize());

        if(marker.isPrimaryMarker()) {
            g.setColor(JBColor.RED);
        } else {
            g.setColor(JBColor.YELLOW);
        }

        ((Graphics2D)g).drawString(marker.getReplacementText(), (float)x, bottomYOfMarkerChar);
        MarkupModel markupModel = editor.getMarkupModel();
        TextAttributes attributes = new TextAttributes();
        attributes.setEffectType(EffectType.SEARCH_MATCH);
        attributes.setBackgroundColor(JBColor.WHITE);
        markupModel.addRangeHighlighter(marker.getStartOffset(), marker.getEndOffset(), HighlighterLayer.SELECTION, attributes, HighlighterTargetArea.EXACT_RANGE);

    }

    private void setupLocationAndBoundsOfPanel(Editor editor) {
        this.setLocation(0, 0);
        Rectangle visibleArea = editor.getScrollingModel().getVisibleArea();
        int x = (int) (parent.getLocation().getX() + visibleArea.getX() + editor.getScrollingModel().getHorizontalScrollOffset());
        this.setBounds(x, (int) (visibleArea.getY()), (int) visibleArea.getWidth(), (int) visibleArea.getHeight());
    }

    public void handleSelectFirstOccurence(boolean upwards){
        Optional<Marker2> goTomarker = markerCollection.stream()
                .filter(marker2 -> marker2.getStartOffset() < this.contextPoint ==  upwards)
                .sorted((o1, o2) -> {
                    int o1ToCaret = Math.abs(o1.getStartOffset() - this.contextPoint);
                    int o2ToCaret = Math.abs(o2.getStartOffset() - this.contextPoint);
                    return o1ToCaret - o2ToCaret;
                })
                .findFirst();
        if(goTomarker.isPresent()) {
            callingAction.PerformActionAtMarker(goTomarker.get());
        }
    }

    public void handleSelect(String selectedChar) {
        if(selectedChar.equals(MARKER_SET.substring(MARKER_SET.length()-1).toLowerCase())){
            System.out.println("Æ selected");
            ArrayList<Marker2> selectedMarkers = new ArrayList<>();
            markerCollection.stream()
                    .filter(marker2 -> marker2.getReplacementText().toLowerCase().equals(selectedChar))
                    .forEach(selectedMarkers::add);
            markerCollection = selectedMarkers;
            SortMarkersAndAssignReplacementText();
            this.repaint();
            return;
        }
        Optional<Marker2> marker = markerCollection.stream()
                .filter(marker2 -> marker2.getReplacementText().toLowerCase().equals(selectedChar))
                .findFirst();
        if(marker.isPresent()) {
            callingAction.PerformActionAtMarker(marker.get());
        }
    }

    public void handleSelectAll() {
        callingAction.PerformActionAtMultipleMarkers(markerCollection);
    }

    public int getContextPoint() {
        return contextPoint;
    }

    public void setContextPoint(int contextPoint) {
        this.contextPoint = contextPoint;
    }
}
