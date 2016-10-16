/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package marker;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MarkersPanel extends JComponent {
  public static final Color PANEL_BACKGROUND_COLOR = new Color(128, 138, 142);
  private Editor _editor;
  private MarkerCollection _markerCollection;
  private JComponent _parent;
  private Font _fontInEditor;

  public MarkersPanel(Editor editor, MarkerCollection markerCollection) {
    _editor = editor;
    _markerCollection = markerCollection;
    _parent = _editor.getContentComponent();
    _fontInEditor = _editor.getColorsScheme().getFont(EditorFontType.BOLD);
    setupLocationAndBoundsOfPanel(editor);
  }

  @Override
  public void paint(Graphics g) {
    drawPanelBackground(g);
    g.setFont(_fontInEditor);

    for (Marker marker : _markerCollection.values()) {
      for (int offset : marker.getOffsets()) {

        double x = getVisiblePosition(offset).getX() + _parent.getLocation().getX();
        double y = getVisiblePosition(offset).getY() + _parent.getLocation().getY();

        drawBackgroundOfMarupChar(g, marker, x, y);
        drawMarkerChar(g, marker, x, y);
      }
    }

    super.paint(g);
  }

  private void drawMarkerChar(Graphics g, Marker marker, double x, double y) {
    float buttomYOfMarkerChar = (float) (y + _fontInEditor.getSize());

    if (marker.isMappingToMultipleOffset()) {
      g.setColor(Color.BLACK);
    } else {
      g.setColor(Color.RED);
    }

    ((Graphics2D)g).drawString(String.valueOf(marker.getMarkerChar()), (float)x, buttomYOfMarkerChar);
  }

  private void drawBackgroundOfMarupChar(Graphics g, Marker marker, double x, double y) {
    Rectangle2D fontRect = _parent.getFontMetrics(_fontInEditor).getStringBounds(String.valueOf(marker.getMarkerChar()), g);

    if (marker.isMappingToMultipleOffset()) {
      g.setColor(Color.YELLOW);
    } else {
      g.setColor(Color.WHITE);
    }

    g.fillRect((int)x, (int)y, (int) fontRect.getWidth(), (int) fontRect.getHeight());
  }

  private Point getVisiblePosition(int offset) {
    return _editor.visualPositionToXY(_editor.offsetToVisualPosition(offset));
  }

  private void drawPanelBackground(Graphics g) {
    ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
    g.setColor(PANEL_BACKGROUND_COLOR);
    g.fillRect(0, 0, (int) this.getBounds().getWidth(), (int) this.getBounds().getHeight());
    ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
  }

  private void setupLocationAndBoundsOfPanel(Editor editor) {
    this.setLocation(0, 0);
    Rectangle visibleArea = editor.getScrollingModel().getVisibleArea();
    int x = (int) (_parent.getLocation().getX() + visibleArea.getX() + _editor.getScrollingModel().getHorizontalScrollOffset());
    this.setBounds(x, (int) (visibleArea.getY()), (int) visibleArea.getWidth(), (int) visibleArea.getHeight());
  }
}