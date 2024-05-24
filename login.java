import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login {
    private JPanel loginform;
    private JTextField tfusername;
    private JPasswordField tfpassword;
    private JButton OKButton;
    private JButton registerButton;
    private ImageIcon myIcon;

    public login() {
        myIcon = new ImageIcon("res/images/logo idoma.jpg");
        Image scaledImage = myIcon.getImage().getScaledInstance(1, 1, Image.SCALE_SMOOTH);
        myIcon = new ImageIcon(scaledImage);
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesLogin();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrasi.main(new String[0]);
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });
    }

    private Connection buatKoneksi() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/user";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }

    private void prosesLogin() {
        try (Connection connection = buatKoneksi()) {
            String username = tfusername.getText();
            String password = new String(tfpassword.getPassword());

            String sql = "SELECT * FROM pengguna WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(loginform, "Login berhasil!");
                    bukaMenuUtama();
                } else {
                    JOptionPane.showMessageDialog(loginform, "Login gagal. Periksa kembali username dan password Anda.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void bukaMenuUtama() {
        SwingUtilities.invokeLater(() -> {
            JFrame menuUtamaFrame = new JFrame("Menu Utama");
            menuutama menuUtamaForm = new menuutama();
            menuUtamaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuUtamaFrame.getContentPane().add(menuUtamaForm.getMenuutama());
            menuUtamaFrame.pack();
            menuUtamaFrame.setLocationRelativeTo(null);
            menuUtamaFrame.setVisible(true);

            // Tutup frame login saat ini
            JComponent component = (JComponent) OKButton;
            Window window = SwingUtilities.getWindowAncestor(component);
            window.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login Form");
            login loginForm = new login();
            frame.setContentPane(loginForm.loginform);
            frame.setIconImage(loginForm.myIcon.getImage());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
