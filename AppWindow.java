import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class AppWindow {
    private JFrame frame;
    private JPanel panel;

    private Image courtImage = new ImageIcon("resources/court.jpg").getImage();
    private Toolkit kit = Toolkit.getDefaultToolkit();
    private Color backgroundColor = Color.DARK_GRAY;

    private Dimension screenSize = kit.getScreenSize();

    private int screenWidth = screenSize.width;
    private int screenHeight = screenSize.height;

    private int imageX = screenWidth / 2 - 500;
    private int imageY = screenHeight / 2 - 300;
    private int imageWidth = 1000;
    private int imageHeight = 600;

    private ArrayList<Integer> playerXCoordinates = new ArrayList<Integer>();
    private ArrayList<Integer> playerYCoordinates = new ArrayList<Integer>();
    ArrayList<String> playerLabels = new ArrayList<>(Arrays.asList("S","OH1","MB","OP","OH2","L"));

    private int circleSize = 50;
    private int draggedCircleIndex = -1;

    private boolean dragging = false;
    private int offsetX, offsetY;

    public AppWindow() {
        frame = new JFrame("Rotations");

        frame.setSize(kit.getScreenSize().width, kit.getScreenSize().height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPlayerCoordinates();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(courtImage, imageX, imageY, imageWidth, imageHeight, this);

                drawPlayers(g);
            }
        };

        panel.setBackground(backgroundColor);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                draggedCircleIndex = getCircleAt(mouseX, mouseY);
                if (draggedCircleIndex != -1) {
                    if (e.getClickCount() == 2) {
                        String newText = JOptionPane.showInputDialog(panel, "New label:", playerLabels.get(draggedCircleIndex));
                        
                        if (newText != null && !newText.trim().isEmpty()) {
                            playerLabels.set(draggedCircleIndex, newText.trim());
                            panel.repaint();
                        }
                    } else {
                        dragging = true;
                        offsetX = mouseX - playerXCoordinates.get(draggedCircleIndex);
                        offsetY = mouseY - playerYCoordinates.get(draggedCircleIndex);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
                draggedCircleIndex = -1;
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging && draggedCircleIndex != -1) {
                    playerXCoordinates.set(draggedCircleIndex, e.getX() - offsetX);
                    playerYCoordinates.set(draggedCircleIndex, e.getY() - offsetY);
                    panel.repaint();
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private int getCircleAt(int x, int y) {
        for (int i = 0; i < playerXCoordinates.size(); i++) {
            int centerX = playerXCoordinates.get(i) + circleSize / 2;
            int centerY = playerYCoordinates.get(i) + circleSize / 2;
            int radius = circleSize / 2;
            int dx = x - centerX;
            int dy = y - centerY;
            if (dx * dx + dy * dy <= radius * radius) {
                return i;
            }
        }

        return -1;
    }

    /*
    Default circle coordinates (imageX +, imageY +):
    1) 175, 400
    2) 400, 400
    3) 400, 275
    4) 400, 150
    5) 175, 150
    6) 175, 275
    */
    private void addPlayerCoordinates() {
        // Position 1
        playerXCoordinates.add(imageX + 175);
        playerYCoordinates.add(imageY + 400);

        // Position 2
        playerXCoordinates.add(imageX + 400);
        playerYCoordinates.add(imageY + 400);

        // Position 3
        playerXCoordinates.add(imageX + 400);
        playerYCoordinates.add(imageY + 275);

        // Position 4
        playerXCoordinates.add(imageX + 400);
        playerYCoordinates.add(imageY + 150);

        // Position 5
        playerXCoordinates.add(imageX + 175);
        playerYCoordinates.add(imageY + 150);

        // Position 6
        playerXCoordinates.add(imageX + 175);
        playerYCoordinates.add(imageY + 275);
    }

    private void drawPlayers(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();

        for (int i = 0; i < playerXCoordinates.size(); i++) {
            int x = playerXCoordinates.get(i);
            int y = playerYCoordinates.get(i);

            g.setColor(Color.BLUE);
            g.fillOval(x, y, circleSize, circleSize);

            g.setColor(Color.RED);
            g.drawOval(x, y, circleSize, circleSize);

            String text = playerLabels.get(i);
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            int textX = x + (circleSize - textWidth) / 2;
            int textY = y + (circleSize + textHeight) / 2 - 3;

            g.setColor(Color.WHITE);
            g.drawString(text, textX, textY);
        }
    }
}
