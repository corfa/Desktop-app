package org.example;

import org.json.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginFrame extends JFrame implements ActionListener {
    private final JTextField loginField;
    private final JPasswordField passwordField;
    private final JTextField ipField;

    public LoginFrame() {
        JLabel loginLabel = new JLabel("Логин:");
        loginField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Пароль:");
        passwordField = new JPasswordField(20);
        JLabel ipLabel = new JLabel("IP-адрес:");
        ipField = new JTextField(20);
        JButton submitButton = new JButton("Отправить");

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(loginLabel);
        panel.add(loginField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(ipLabel);
        panel.add(ipField);
        panel.add(new JLabel(""));
        panel.add(submitButton);

        submitButton.addActionListener(this);
        this.add(panel);
        this.setTitle("Вход");
        this.setSize(300, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String login = loginField.getText();
        String password = new String(passwordField.getPassword());
        String ip = ipField.getText();

        try {
            JSONObject json = new JSONObject();
            json.put("username", login);
            json.put("password", password);
            URL url = new URL("http://" + ip + ":8080/api/user/auth");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(json.toString());
            writer.flush();
            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = in.readLine();
                JSONObject jsonResponse = new JSONObject(response);
                String authToken = jsonResponse.getString("token");
                String urlScreenshot = "http://"+ip+":8080/api/screenshot";
                JFrame frame = new JFrame("Image Display");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new ImageDisplay(authToken,urlScreenshot));
                frame.pack();
                frame.setVisible(true);            } else {
                JOptionPane.showMessageDialog(this, "Неправильный логин или пароль", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            conn.disconnect();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
