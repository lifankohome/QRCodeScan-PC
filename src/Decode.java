import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

public class Decode {
	public String StringDecodeQRCode(BufferedImage bufImg) {
		String decodedData = null;
		try {
			QRCodeDecoder decoder = new QRCodeDecoder();
			byte[] byteDecode = decoder.decode(new J2SEImage(bufImg));
			try {
				decodedData = new String(byteDecode, "UTF-8"); // 确保中文不乱码
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (DecodingFailedException dfe) {
			System.out.println("Error: " + dfe.getMessage());
			dfe.printStackTrace();
		}
		return decodedData;
	}
}