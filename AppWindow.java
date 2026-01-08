import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class AppWindow {
    private JFrame frame;
    private JPanel panel;

    private Image courtImage = new ImageIcon("resources/court.jpg").getImage();
    private Toolkit kit = Toolkit.getDefaultToolkit();
    private Color backgroundColor = Color.DARK_GRAY;

    private int circleX = 100;
    private int circleY = 100;
    private int circleSize = 50;

    private boolean dragging = false;
    private int offsetX, offsetY;

    public AppWindow() {
        frame = new JFrame("Rotations");

        frame.setSize(kit.getScreenSize().width, kit.getScreenSize().height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(courtImage, 775, 420, 1000, 600, this);

                g.setColor(Color.RED);
                g.fillOval(circleX, circleY, circleSize, circleSize);

                g.setColor(Color.GREEN);
                g.drawOval(circleX, circleY, circleSize, circleSize);
            }
        };

        panel.setBackground(backgroundColor);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                if (isInsideCircle(mouseX, mouseY)) {
                    dragging = true;
                    offsetX = mouseX - circleX;
                    offsetY = mouseY - circleY;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    circleX = e.getX() - offsetX;
                    circleY = e.getY() - offsetY;
                    panel.repaint();
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private boolean isInsideCircle(int x, int y) {
        int centerX = circleX + circleSize / 2;
        int centerY = circleY + circleSize / 2;
        int radius = circleSize / 2;

        int distanceX = x - centerX;
        int distanceY = y - centerY;

        // distance formula without square roots
        return distanceX * distanceX + distanceY * distanceY <= radius * radius;
    }
}
