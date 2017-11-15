import javax.swing.*;
import java.awt.*;

/**
 * 显示图片的Label
 */
@SuppressWarnings("serial")
class BackgroundImage extends JLabel {
    int lineX, lineY;
    int x, y, h, w;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(x, y, w, h);
        String area = Integer.toString(w) + " * " + Integer.toString(h);
        g.drawString(area, x + w / 2 - 15, y + h / 2);
        g.drawLine(lineX, 0, lineX, getHeight());

        g.drawLine(0, lineY, getWidth(), lineY);
    }

    public void drawRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        h = height;
        w = width;
        repaint();
    }

    public void drawCross(int x, int y) {
        lineX = x;
        lineY = y;
        repaint();
    }
}
