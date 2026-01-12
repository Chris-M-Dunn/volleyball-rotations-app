import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {
    private Image courtImage = new ImageIcon("resources/court.jpg").getImage();
    
    private Color backgroundColor = Color.DARK_GRAY;

    private int imageWidth = 1000;
    private int imageHeight = 600;
    private int imageX;
    private int imageY;
    private int circleSize = 50;
    private int offsetX;
    private int offsetY;
    private int draggedCircleIndex = -1;

    private boolean dragging = false;

    private ArrayList<Integer> playerXCoordinates = new ArrayList<Integer>();
    private ArrayList<Integer> playerYCoordinates = new ArrayList<Integer>();
    ArrayList<String> playerLabels = new ArrayList<>(Arrays.asList("S","OH1","MB","OP","OH2","L"));
    
    PlayerPanel() {
        setBackground(backgroundColor);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        centerCourtOnScreen();
        addPlayerCoordinates();
        setupMouseListeners();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(courtImage, imageX, imageY, imageWidth, imageHeight, this);
        drawPlayers(g);
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

    private void centerCourtOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        imageX = screenSize.width / 2 - imageWidth / 2;
        imageY = screenSize.height / 2 - imageHeight / 2;
    }

    /*
    Default player circle coordinates (imageX +, imageY +):
    1) 175, 400
    2) 400, 400
    3) 400, 275
    4) 400, 150
    5) 175, 150
    6) 175, 275
    */
    private void addPlayerCoordinates() {
        // 1
        playerXCoordinates.add(imageX + 175);
        playerYCoordinates.add(imageY + 400);

        // 2
        playerXCoordinates.add(imageX + 400);
        playerYCoordinates.add(imageY + 400);

        // 3
        playerXCoordinates.add(imageX + 400);
        playerYCoordinates.add(imageY + 275);

        // 4
        playerXCoordinates.add(imageX + 400);
        playerYCoordinates.add(imageY + 150);

        // 5
        playerXCoordinates.add(imageX + 175);
        playerYCoordinates.add(imageY + 150);

        // 6
        playerXCoordinates.add(imageX + 175);
        playerYCoordinates.add(imageY + 275);
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                draggedCircleIndex = getCircleAt(mouseX, mouseY);

                if (draggedCircleIndex != -1) {
                    if (e.getClickCount() == 2) {
                        editLabel(draggedCircleIndex);
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

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging && draggedCircleIndex != -1) {
                    playerXCoordinates.set(draggedCircleIndex, e.getX() - offsetX);
                    playerYCoordinates.set(draggedCircleIndex, e.getY() - offsetY);
                    repaint();
                }
            }
        });
    }

    private int getCircleAt(int x, int y) {
        for (int i=0; i<playerXCoordinates.size(); i++) {
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

    private void editLabel(int index) {
        String newText = JOptionPane.showInputDialog(
                this,
                "New label:",
                playerLabels.get(index)
        );

        if (newText != null && !newText.trim().isEmpty()) {
            playerLabels.set(index, newText.trim());
            repaint();
        }
    }
}
