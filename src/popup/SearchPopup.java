package popup;

import javax.swing.*;
import java.awt.*;

/**
 * Created by runed on 15-10-2016.
 */
public class SearchPopup extends JComponent {
    private JTextField searchField;


    public SearchPopup() {
        this.searchField = new JTextField();
        this.searchField.setText("hehe");
    }

    @Override
    public void paintComponent(Graphics g){
        searchField.paintComponents(g);
    }
}
