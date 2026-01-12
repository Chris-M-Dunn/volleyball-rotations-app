import javax.swing.*;
import java.awt.*;

public class AppWindow {
    public AppWindow() {
        JFrame frame = new JFrame("Rotations");

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        frame.setSize(screenSize.width, screenSize.height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new PlayerPanel());
        frame.setVisible(true);
    }
}
