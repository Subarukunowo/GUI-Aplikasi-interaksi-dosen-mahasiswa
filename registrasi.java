import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class registrasi {
    private JFrame frame;
    private JPanel registrasi;
    private JTextField tfnama;
    private JPasswordField passwordpa2;
    private JPasswordField passwordpa1;
    private JButton registerbtn;
    private JButton cancelButton;
    private JButton kembaliButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/user";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public registrasi() {
        frame = new JFrame("Registrasi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        registrasi = new JPanel(new GridLayout(5, 2, 5, 5));

        JLabel lblNama = new JLabel("Nama:");
        tfnama = new JTextField();

        JLabel lblPassword1 = new JLabel("Password:");
        passwordpa1 = new JPasswordField();

        JLabel lblPassword2 = new JLabel("Ulangi Password:");
        passwordpa2 = new JPasswordField();

        registerbtn = new JButton("Register");
        cancelButton = new JButton("Cancel");
        kembaliButton = new JButton("Kembali");


        registerbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPasswordMatch()) {
                    if (registerUser()) {
                        JOptionPane.showMessageDialog(frame, "Registrasi berhasil!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Registrasi gagal. Silakan coba lagi.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Password tidak cocok. Silakan coba lagi.");
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        kembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login.main(new String[0]);
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });



        registrasi.add(lblNama);
        registrasi.add(tfnama);
        registrasi.add(lblPassword1);
        registrasi.add(passwordpa1);
        registrasi.add(lblPassword2);
        registrasi.add(passwordpa2);
        registrasi.add(new JLabel()); // Placeholder
        registrasi.add(registerbtn);
        registrasi.add(cancelButton);
        registrasi.add(kembaliButton);

        frame.getContentPane().add(BorderLayout.CENTER, registrasi);
        frame.setVisible(true);
    }

    private boolean isPasswordMatch() {
        String pass1 = new String(passwordpa1.getPassword());
        String pass2 = new String(passwordpa2.getPassword());
        return pass1.equals(pass2);
    }

    private boolean registerUser() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "INSERT INTO pengguna (username, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, tfnama.getText());
                preparedStatement.setString(2, new String(passwordpa1.getPassword()));
                int result = preparedStatement.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new registrasi();
            }
        });
    }
}
