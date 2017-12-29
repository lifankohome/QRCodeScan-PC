import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.swetake.util.Qrcode;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class CreateQR extends JFrame {
    private JTextField QRContent;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CreateQR frame = new CreateQR();
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    frame.setLocation((dim.width - frame.getWidth()) / 2, (dim.height - frame.getHeight()) / 2 - 100);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public CreateQR() throws MalformedURLException {
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(Window.class.getResource("/com/sun/javafx/webkit/prism/resources/panIcon.png")));

        setTitle("QRCode Create");
        setResizable(false);
        setBounds(100, 100, 215, 270);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        QRContent = new JTextField();
        QRContent.setHorizontalAlignment(SwingConstants.CENTER);
        QRContent.setToolTipText("Input text here");
        contentPane.add(QRContent, BorderLayout.NORTH);
        QRContent.setColumns(10);

        JLabel QRCodeBox = new JLabel();
        QRCodeBox.setHorizontalAlignment(SwingConstants.CENTER);
        QRCodeBox.setBorder(null);
        QRCodeBox.setVerticalAlignment(SwingConstants.BOTTOM);
        contentPane.add(QRCodeBox, BorderLayout.CENTER);

        Qrcode qrcode = new Qrcode();
        qrcode.setQrcodeErrorCorrect('M');
        qrcode.setQrcodeEncodeMode('B');

        int size = 12;
        qrcode.setQrcodeVersion(size);

        QRContent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent arg0) {
                String content = QRContent.getText();

                byte[] contentBytes = null;
                try {
                    contentBytes = content.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                int imgSize = 67 + 12 * (size - 1);
                BufferedImage bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
                Graphics2D gs = bufImg.createGraphics();
                gs.setBackground(new Color(214, 217, 223));
                gs.clearRect(0, 0, imgSize, imgSize);

                gs.setColor(Color.BLACK);

                int offset = 2;
                assert contentBytes != null;
                if (contentBytes.length > 0 && contentBytes.length < 800) {
                    boolean[][] codeOut = qrcode.calQrcode(contentBytes);
                    for (int i = 0; i < codeOut.length; i++) {
                        for (int j = 0; j < codeOut.length; j++) {
                            if (codeOut[j][i]) {
                                gs.fillRect(j * 3 + offset, i * 3 + offset, 3, 3);
                            }
                        }
                    }
                }
                gs.dispose();
                bufImg.flush();

                QRCodeBox.setIcon(new ImageIcon(bufImg));
            }
        });
    }
}
