import javax.swing.*;
import java.awt.*;
/**
 * Created by Sorka on 10.12.2016.
 */
public class Window extends  JFrame{
    private TablePanel tablePanel;

    public Window() {
        super("Tennis");
        Container c = getContentPane();
        setSize(750, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        tablePanel = new TablePanel(this);
        c.add(tablePanel, "Center");
        setVisible(true);
    }
    public TablePanel getTablePanel(){
        return tablePanel;
    }
}
