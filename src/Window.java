import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Window extends JFrame {
    public static Window frame;
    public static String autoOpen = "0", autoCopy = "0";
    private static String ConfigPath;
    private static JCheckBox checkboxOpen, checkboxCopy;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                frame = new Window();
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation((dim.width - frame.getWidth()) / 2, (dim.height - frame.getHeight()) / 2 - 200);
                frame.setVisible(false);

                javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView
                        .getFileSystemView();
                ConfigPath = fsv.getDefaultDirectory().toString() + "\\config.ini";

                String configBuf = Config.read(ConfigPath);
                if (configBuf.equals("文件不存在")) {
                    Config.save(ConfigPath, "11");
                    autoOpen = autoCopy = "1";
                } else {
                    autoOpen = configBuf.substring(36, 37);
                    autoCopy = configBuf.substring(37, 38);
                    if (autoOpen.equals("0")) {
                        checkboxOpen.setSelected(false);
                    }
                    if (autoCopy.equals("0")) {
                        checkboxCopy.setSelected(false);
                    }
                }

                Min.create(); // 创建托盘图标

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void openURL(String url) {
        java.awt.Desktop dp = java.awt.Desktop.getDesktop();
        if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
            try {
                try {
                    dp.browse(new URI(url));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create the frame.
     */
    public Window() {
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(Window.class.getResource("/com/sun/javafx/webkit/prism/resources/panIcon.png")));
        setResizable(false);
        setTitle("Setting");
        setBounds(100, 100, 315, 175);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        checkboxOpen = new JCheckBox("Auto open If QRCode contains URL");
        checkboxOpen.setSelected(true);
        checkboxOpen.setFont(new Font("Consolas", Font.PLAIN, 14));

        checkboxCopy = new JCheckBox("Auto copy QRCode text to clipboard");
        checkboxCopy.setSelected(true);
        checkboxCopy.setFont(new Font("Consolas", Font.PLAIN, 14));
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        contentPane.add(checkboxOpen);
        contentPane.add(checkboxCopy);

        JButton btnScan = new JButton("About");
        btnScan.setFont(new Font("SansSerif", Font.ITALIC, 12));
        btnScan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                JOptionPane.showMessageDialog(null, "Author: lifanko\r\nGithub: lifankohome", "About this program：",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        contentPane.add(btnScan);

        JButton btnSave = new JButton("SAVE");
        btnSave.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnSave.setForeground(new Color(255, 69, 0));
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                String configBuf = "";
                if (checkboxOpen.isSelected()) {
                    configBuf += "1";
                    autoOpen = "1";
                } else {
                    configBuf += "0";
                    autoOpen = "0";
                }
                if (checkboxCopy.isSelected()) {
                    configBuf += "1";
                    autoCopy = "1";
                } else {
                    configBuf += "0";
                    autoCopy = "0";
                }
                Config.save(Window.ConfigPath, configBuf);
            }
        });
        contentPane.add(btnSave);

        JTextPane tipText = new JTextPane();
        tipText.setEditable(false);
        tipText.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
        tipText
                .setText("The screenshot image will copy to clipboard,\r\nso you can paste it in WORD or QQ directly.");
        contentPane.add(tipText);
    }
}
