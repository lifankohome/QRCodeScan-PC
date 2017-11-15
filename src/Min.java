import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Min implements ActionListener {
	public static TrayIcon trayIcon;

	public static void creat() {
		if (SystemTray.isSupported()) {// 判断是否支持系统托盘

			URL url = Min.class.getResource("icon.png");// 获取图片所在的URL

			ImageIcon icon = new ImageIcon(url);// 实例化图像对象

			Image image = icon.getImage();// 获得Image对象

			trayIcon = new TrayIcon(image);// 创建托盘图标

			trayIcon.addMouseListener(new MouseAdapter() {// 为托盘添加鼠标适配器

				public void mouseClicked(MouseEvent e) {// 鼠标事件
					if (e.getClickCount() == 2) {// 双击鼠标
						cut();
					}
				}
			});

			trayIcon.setToolTip("使用提示：\r\n双击：屏幕截图\r\n右键：功能菜单\r\n");// 添加工具提示文本

			// 创建弹出菜单
			PopupMenu popupMenu = new PopupMenu();

			MenuItem cut = new MenuItem("Screenshot");
			MenuItem qrcode = new MenuItem("QRCode Droid");
			MenuItem setting = new MenuItem("Setting");
			MenuItem exit = new MenuItem("Exit");
			popupMenu.add(cut);
			popupMenu.add(qrcode);
			popupMenu.add(setting);
			popupMenu.addSeparator();
			popupMenu.add(exit);

			trayIcon.setPopupMenu(popupMenu);// 为托盘图标加弹出菜弹

			SystemTray systemTray = SystemTray.getSystemTray();// 获得系统托盘对象
			try {
				systemTray.add(trayIcon);// 为系统托盘加托盘图标
			} catch (Exception e) {
				 e.printStackTrace();
			}
			setting.addActionListener(e -> {
                Window.frame.setVisible(true);
                Window.frame.setExtendedState(Frame.NORMAL);
            });
			cut.addActionListener(e -> cut());
			qrcode.addActionListener(e -> qrcode());
			exit.addActionListener(e -> System.exit(0));
		} else {
			JOptionPane.showMessageDialog(null, "无法创建托盘菜单，请使用“任务管理器”关闭程序！");
		}
	}

	private static void qrcode() {
		ScreenCapture capture = ScreenCapture.getInstance(); // 创建实例
		capture.captureImage();

		String decoderContent = new Decode().StringDecodeQRCode(capture.getPickedImage());
		if (decoderContent == null) {
			JOptionPane.showMessageDialog(null, "未检测到二维码，请尝试缩小扫描范围", "扫描失败：", JOptionPane.ERROR_MESSAGE);
		} else {
			Pattern pattern = Pattern
					.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$");
			if (pattern.matcher(decoderContent).matches() && Window.autoOpen.equals("1")) {
				Window.openURL(decoderContent);
			} else {
				JOptionPane.showMessageDialog(null, decoderContent, "扫描结果：", JOptionPane.INFORMATION_MESSAGE);
			}

			if (Window.autoCopy.equals("1")) {
				setSysClipboardText(decoderContent);
			}
		}
	}

	private static void cut() {
		ScreenCapture capture = ScreenCapture.getInstance(); // 创建实例
		capture.captureImage();
		setClipboardImage(capture.getPickedImage());
	}

	/**
	 * 将字符串复制到剪切板。
	 */
	private static void setSysClipboardText(String writeMe) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(writeMe);
		clip.setContents(tText, null);
	}

	/**
	 * 复制图片到剪切板。
	 */
	public static void setClipboardImage(final Image image) {
		Transferable trans = new Transferable() {
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor))
					return image;
				throw new UnsupportedFlavorException(flavor);
			}

		};
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
