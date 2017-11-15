import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScreenCapture {
    private static ScreenCapture defaultCapturer = new ScreenCapture();
    private int x1, y1, x2, y2;
    private int recX, recY, recH, recW; // 截取的图像
    private boolean isFirstPoint = true;
    private BackgroundImage labFullScreenImage = new BackgroundImage();
    private Robot robot;
    private BufferedImage fullScreenImage;
    private BufferedImage pickedImage;
    private JDialog dialog = new JDialog();

    public static void main(String[] args) throws Exception {
        ScreenCapture capture = ScreenCapture.getInstance(); // 创建实例
        capture.captureImage();

        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel imagebox = new JLabel();
        panel.add(BorderLayout.CENTER, imagebox);
        imagebox.setIcon(capture.getPickedIcon());

        frame.setContentPane(panel);
        frame.setSize(capture.getRecW(), capture.getRecH() + 40);
        frame.setVisible(true);

        System.out.println("Over");
    }

    private ScreenCapture() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.err.println("Internal Error: " + e);
            e.printStackTrace();
        }
        JPanel cp = (JPanel) dialog.getContentPane();
        cp.setLayout(new BorderLayout());
        labFullScreenImage.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evn) {
                isFirstPoint = true;
                pickedImage = fullScreenImage.getSubimage(recX, recY, recW, recH);
                dialog.setVisible(false);
            }
        });

        labFullScreenImage.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evn) {
                if (isFirstPoint) {
                    x1 = evn.getX();
                    y1 = evn.getY();
                    isFirstPoint = false;
                } else {
                    x2 = evn.getX();
                    y2 = evn.getY();
                    int maxX = Math.max(x1, x2); // 有可能是向左上角拉，保证截图区域是正值
                    int maxY = Math.max(y1, y2);
                    int minX = Math.min(x1, x2);
                    int minY = Math.min(y1, y2);
                    recX = minX;
                    recY = minY;
                    recW = maxX - minX;
                    recH = maxY - minY;
                    labFullScreenImage.drawRectangle(recX, recY, recW, recH);
                }
            }

            public void mouseMoved(MouseEvent e) {
                labFullScreenImage.drawCross(e.getX(), e.getY());
            }
        });

        cp.add(BorderLayout.CENTER, labFullScreenImage);
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        dialog.setAlwaysOnTop(true);
        dialog.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        dialog.setUndecorated(true);
        dialog.setSize(dialog.getMaximumSize());
        dialog.setModal(true);
    }

    // 单例模式
    public static ScreenCapture getInstance() {
        return defaultCapturer;
    }

    // 捕捉屏幕的一个矫形区域
    public void captureImage() {
        fullScreenImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIcon icon = new ImageIcon(fullScreenImage);
        labFullScreenImage.setIcon(icon);
        dialog.setVisible(true);
    }

    // 得到捕捉后的BufferedImage
    public BufferedImage getPickedImage() {
        return pickedImage;
    }

    // 得到捕捉后的Icon
    public ImageIcon getPickedIcon() {
        return new ImageIcon(getPickedImage());
    }

    // 获取高度
    public int getRecH() {
        return recH;
    }

    // 获取宽度
    public int getRecW() {
        return recW;
    }
}

