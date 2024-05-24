import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Laporan {
    private JButton Laporkanbtn;
    private JButton cancelButton;
    private JCheckBox spamCheckBox;
    private JCheckBox kataTidakPantasCheckBox;
    private JCheckBox melanggarKomunitasCheckBox;
    private JTextField namadilaporkantf;
    private JPanel mainPanel;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/idoma";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Laporan() {
        Laporkanbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                laporkanPengguna(spamCheckBox.isSelected(), kataTidakPantasCheckBox.isSelected(), melanggarKomunitasCheckBox.isSelected());
            }
        });


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuutama.main(new String[0]);
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });
    }

    private void laporkanPengguna(boolean isSpam, boolean isKataTidakPantas, boolean isMelanggarKomunitas) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

            StringBuilder laporanText = new StringBuilder();
            laporanText.append("Laporan Pengguna:\n");

            if (isSpam) {
                laporanText.append("- Spam\n");
            }
            if (isKataTidakPantas) {
                laporanText.append("- Kata Tidak Pantas\n");
            }
            if (isMelanggarKomunitas) {
                laporanText.append("- Melanggar Komunitas\n");
            }

            // Mendapatkan teks tambahan dari JTextField
            String namaDilaporkan = namadilaporkantf.getText();
            laporanText.append("\nNama Pengguna Dilaporkan: ").append(namaDilaporkan);

            // Menambahkan status_laporan
            String statusLaporan = "Belum Diproses"; // Default status
            laporanText.append("\nStatus Laporan: ").append(statusLaporan);

            String query = "INSERT INTO laporanidoma (isi_laporan, status_laporan, nama_user) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, laporanText.toString());
                preparedStatement.setString(2, statusLaporan);
                preparedStatement.setString(3, namaDilaporkan);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Pengguna dilaporkan!");

                    // Setelah laporan berhasil dimasukkan, update status_laporan ke "Diproses"
                    updateStatusLaporan(connection);
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal melaporkan pengguna.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatusLaporan(Connection connection) throws SQLException {
        String updateQuery = "UPDATE laporanidoma SET status_laporan = 'Diproses' ORDER BY ID_laporan DESC LIMIT 1";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Laporan");
                frame.setContentPane(new Laporan().mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
