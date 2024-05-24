import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class register {
    private JPanel registrationform;
    private JButton registerbtn;
    private JButton cancelbtn;
    private JTextField tfnama;
    private JTextField tfnoinduk;
    private JTextField tfalamat;
    private JTextField tfkontak;
    private JComboBox<String> jurusanbox;
    private JComboBox<String> prodibox;
    private JPasswordField passwordpf1;
    private JPasswordField passwordpf2;
    private JButton kembaliButton;
    private JComboBox<String> profesibox;

    private String jenisPengguna;

    public register() {
        prodibox.addItem("");

        isiComboJurusan();
        isiprofesibox();

        jurusanbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProdiOptions();
            }
        });

        registerbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesRegistrasi();
            }
        });

        cancelbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        kembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuutama.main(new String[0]);
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });
    }

    private void isiComboJurusan() {
        jurusanbox.addItem("");
        jurusanbox.addItem("Teknik Informatika");
        jurusanbox.addItem("Teknik Sipil");
        jurusanbox.addItem("Teknik Elektro");
    }

    private void updateProdiOptions() {
        prodibox.removeAllItems();
        prodibox.addItem("");

        String selectedJurusan = (String) jurusanbox.getSelectedItem();
        if ("Teknik Informatika".equals(selectedJurusan)) {
            prodibox.addItem("D4 - Rekayasa Perangkat Lunak");
            prodibox.addItem("D3 - Teknik Informatika");
            prodibox.addItem("D4 - Keamanan Sistem Informasi");
            prodibox.addItem("D2 - Administrasi Jaringan Komputer");
        } else if ("Teknik Sipil".equals(selectedJurusan)) {
            prodibox.addItem("D4 - Teknik Perancangan Jalan & Jembatan");
            prodibox.addItem("D3 - Teknik Sipil");
        } else if ("Teknik Elektro".equals(selectedJurusan)) {
            prodibox.addItem("D3 - Teknik Elektronika");
            prodibox.addItem("D4 - Teknik Listrik");
        }
    }

    private void isiprofesibox() {
        profesibox.removeAllItems();
        profesibox.addItem("Mahasiswa");
        profesibox.addItem("Dosen");
    }

    private Connection buatKoneksiIdoma() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/idoma";
        String username = "root";
        String password = "";

        return DriverManager.getConnection(url, username, password);
    }

    private void masukkandatalogin() {
        try (Connection connectionUser = buatKoneksiIdoma()) {
            String ID = tfnoinduk.getText();
            String username = tfnama.getText();
            char[] passwordChars = passwordpf2.getPassword();
            String password = new String(passwordChars);

            String insertSql = "INSERT no_induk,nama,password INTO usernamepass (ID,username, password) VALUES (?, ?,?)";

            try (PreparedStatement statement = connectionUser.prepareStatement(insertSql)) {
                statement.setString(1, ID);
                statement.setString(2, username);
                statement.setString(3, password);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(registrationform, "Informasi login berhasil dimasukkan!");
                } else {
                    JOptionPane.showMessageDialog(registrationform, "Gagal memasukkan informasi login. Coba lagi.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void prosesRegistrasi() {
        try (Connection connectionIdoma = buatKoneksiIdoma()) {
            String nama = tfnama.getText();
            String no_induk = tfnoinduk.getText();
            String alamat = tfalamat.getText();
            String jurusan = (String) jurusanbox.getSelectedItem();
            String prodi = (String) prodibox.getSelectedItem();
            String no_kontak = tfkontak.getText();
            String password1 = new String(passwordpf1.getPassword());
            String password2 = new String(passwordpf2.getPassword());
            String profesi = (String) profesibox.getSelectedItem();

            if (!password1.equals(password2)) {
                JOptionPane.showMessageDialog(registrationform, "Password tidak cocok. Coba lagi.");
                return;
            }


            jenisPengguna = profesi;


            String targetTable = (jenisPengguna.equals("Mahasiswa")) ? "mahasiswa" : "dosen";

            String insertSql = (jenisPengguna.equals("Mahasiswa"))
                    ? "INSERT INTO " + targetTable + " (NIM, NAMA, PRODI, NO_KONTAK, ALAMAT) VALUES (?, ?, ?, ?, ?)"
                    : "INSERT INTO " + targetTable + " (NIDN, NAMA, PRODI, JURUSAN, NO_KONTAK) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connectionIdoma.prepareStatement(insertSql)) {

                if (jenisPengguna.equals("Mahasiswa")) {
                    statement.setString(1, no_induk);
                    statement.setString(2, nama);
                    statement.setString(3, prodi);
                    statement.setString(4, no_kontak);
                    statement.setString(5, alamat);
                } else {
                    statement.setString(1, no_induk);
                    statement.setString(2, nama);
                    statement.setString(3, prodi);
                    statement.setString(4, jurusan);
                    statement.setString(5, no_kontak);
                }

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(registrationform, "Registrasi berhasil!");
                } else {
                    JOptionPane.showMessageDialog(registrationform, "Registrasi gagal. Coba lagi.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                register registerForm = new register();
                JFrame frame = new JFrame("Registration Form");
                frame.setContentPane(registerForm.registrationform);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
