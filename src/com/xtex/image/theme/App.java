package com.xtex.image.theme;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class App {

	public static BufferedImage preImage = null;
	public static JPanel image = null;
	public static ImageTheme theme = null;
	public static JPanel oldColorPreview = null;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("ImageTheme");
		frame.setSize(400, 590);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1, 1, 1));
		image = null;
		preImage = new BufferedImage(1, 2, BufferedImage.TYPE_INT_RGB);
		theme = new ImageTheme(preImage);
		// try{preImage = ImageIO.read(new
		// File("F:\\Site\\FTP\\A.jpg"));}catch(Exception e){}
		{
			// Preview image
			image = new JPanel() {

				private static final long serialVersionUID = 1L;

				@Override
				protected void paintComponent(Graphics g) {
					g.drawImage(preImage, 0, 0, getWidth(), getHeight(), null);
				}

			};
			panel.add(image);
		}
		{
			// Buttons
			JPanel pane = new JPanel();
			{
				// Load image
				JButton btn = new JButton("Load");
				btn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						FileDialog dlg = new FileDialog(frame, "Load image");
						dlg.setVisible(true);
						File f = new File(dlg.getDirectory() + "/" + dlg.getFile());
						try {
							preImage = ImageIO.read(f);
							image.repaint();
							theme.setImage(preImage);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				});
				pane.add(btn);
			}
			{
				// Get color
				JButton btn = new JButton("Get color");
				btn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						theme.getThemeColor(new ImageTheme.GetThemeCallback() {

							@Override
							public void on(int color) {
								System.out.println(color);
								oldColorPreview.setBackground(new Color(color));
							}

						});
					}

				});
				pane.add(btn);
			}
			{
				// Old color preview
				oldColorPreview = new JPanel();
				oldColorPreview.setBackground(new Color(0));
				pane.add(oldColorPreview);
			}
			{
				// ->
				JLabel label = new JLabel("->");
				pane.add(label);
			}
			{
				// New color select
				JButton btn = new JButton("Set color");
				btn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						Color newColor = JColorChooser.showDialog(frame, "Choose new color",
								oldColorPreview.getBackground());
						theme.convert(oldColorPreview.getBackground(), newColor,
								new ImageTheme.ConvertedCallback() {

									@Override
									public void on() {
										preImage = theme.getImage();
										image.repaint();
									}

								});
					}

				});
				pane.add(btn);
			}
			panel.add(pane);
		}
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
