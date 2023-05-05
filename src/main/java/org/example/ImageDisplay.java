package org.example;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageDisplay extends JPanel {
   // private static final String SERVICE_URL = "http://localhost:8080/api/screenshot";
    private final String SERVICE_URL;
    private final String token;
    private Image image;
    private JButton updateButton;
    private Timer timer;

    public ImageDisplay(String token,String SERVICE_URL) {
        this.token = token;
        this.SERVICE_URL = SERVICE_URL;
        setPreferredSize(new Dimension(640, 480));
//        updateButton = new JButton("Update");
//        updateButton.addActionListener(e -> updateImage());
//        add(updateButton);
        updateImage();
        timer = new Timer(1000, e -> {
            updateImage();
            repaint();
        });
        timer.start();
    }

    private void updateImage() {
        try {
            URL url = new URL(SERVICE_URL);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Authorization", token);
            InputStream inputStream = connection.getInputStream();
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            System.err.println("Failed to update image: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
