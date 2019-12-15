package com.xtex.image.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageTheme {

	private BufferedImage image = null;

	public ImageTheme(BufferedImage image) {
		super();
		this.image = image;
	}

	@Override
	public String toString() {
		return "ImageTheme []";
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void getThemeColor(GetThemeCallback cb) {
		new GetThemeThread(cb).start();
	}

	public void convert(Color old, Color newColor, ConvertedCallback cb) {
		new ConvertThread(old, newColor, cb).start();
	}

	public static abstract class GetThemeCallback {
		public abstract void on(int color);
	}

	public static abstract class ConvertedCallback {
		public abstract void on();
	}

	class GetThemeThread extends Thread {

		private GetThemeCallback cb;

		public GetThemeThread(GetThemeCallback cb) {
			super();
			this.cb = cb;
		}

		@Override
		public void run() {
			long colorX = 0;
			long colorY = 0;
			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {
					colorY += image.getRGB(x, y);
				}
				colorX += colorY / image.getHeight();
			}
			int color = (int) (colorX / image.getWidth());
			cb.on(color);
		}

	}

	class ConvertThread extends Thread {

		private Color old;
		private Color newColor;
		private ConvertedCallback cb;

		public ConvertThread(Color old, Color newColor, ConvertedCallback cb) {
			super();
			this.old = old;
			this.newColor = newColor;
			this.cb = cb;
		}

		@Override
		public void run() {
			int diffR = newColor.getRed() - old.getRed();
			int diffG = newColor.getGreen() - old.getGreen();
			int diffB = newColor.getBlue() - old.getBlue();
			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {
					Color color = new Color(image.getRGB(x, y));
					int R = color.getRed() + diffR;
					int G = color.getGreen() + diffG;
					int B = color.getBlue() + diffB;
					if(R > 255) R = 255;
					if(G > 255) G = 255;
					if(B > 255) B = 255;
					if(R < 0) R = 0;
					if(G < 0) G = 0;
					if(B < 0) B = 0;
					color = new Color(R, G, B);
					image.setRGB(x, y, color.getRGB());
				}
			}
			cb.on();
		}
		
	}

}
