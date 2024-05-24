import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class forumdiskusi extends JFrame {
    private JButton uploadbtn;
    private JTextField tfjudul;
    private JComboBox<String> forumbox;
    private JTextArea taforum;
    private JPanel mainPanel;
    private JButton buatforumbtn;
    private JButton hapusbtn;
    private JButton kembaliButton;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/idoma";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    public forumdiskusi() {
        setTitle("Forum Diskusi");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();

        tfjudul = new JTextField(20);
        buatforumbtn = new JButton("Buat Forum");
        hapusbtn = new JButton("Hapus Forum");
        forumbox = new JComboBox<>();
        uploadbtn = new JButton("Upload");
        forumbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Memasukkan isi forum terpilih ke dalam JTextField
                String selectedForumItem = (String) forumbox.getSelectedItem();
                if (selectedForumItem != null) {
                    String forumIDString = selectedForumItem.split("\\)")[0].substring(1);
                    selectedForumID = Integer.parseInt(forumIDString);
                    tfjudul.setText(selectedForumItem.split("\\)")[1].trim());
                }
            }
        });

        buatforumbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createForum();
                loadForumList();
            }
        });
        hapusbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteForum();
                loadForumList();
                tfjudul.setText("");
            }
        });

        uploadbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadToForum();
            }
        });

        mainPanel.add(new JLabel("Nama Forum: "));
        mainPanel.add(tfjudul);
        mainPanel.add(buatforumbtn);
        mainPanel.add(hapusbtn);
        mainPanel.add(new JLabel("Pilih Forum: "));
        mainPanel.add(forumbox);
        mainPanel.add(uploadbtn);
        mainPanel.add(kembaliButton);

        loadForumList();

        add(mainPanel);
        setVisible(true);
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
    private int selectedForumID;
    private String generateAdminId() {
        int randomId = (int) (Math.random() * 6) + 1;
        return String.format("%03d-adm", randomId);
    }
    private void createForum() {
        String forumName = tfjudul.getText();
        if (!forumName.isEmpty()) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO pembuatan_forum (isi_forum, id_admin) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, forumName);
                    preparedStatement.setString(2, generateAdminId());
                    preparedStatement.executeUpdate();


                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            selectedForumID = generatedKeys.getInt(1);
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nama forum tidak boleh kosong.");
        }
    }

    private void deleteForum() {
        if (selectedForumID > 0) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sqlDeleteForum = "DELETE FROM pembuatan_forum WHERE id_forum = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteForum)) {
                    preparedStatement.setInt(1, selectedForumID);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Forum berhasil dihapus dari database.");
                        selectedForumID = 0;
                    } else {
                        JOptionPane.showMessageDialog(this, "Gagal menghapus forum. Forum tidak ditemukan.");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,"Gagal menghapus forum dari database.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih forum untuk dihapus.");
        }
    }

    private void uploadToForum() {
        String selectedForum = (String) forumbox.getSelectedItem();
        if (selectedForum != null) {
            JOptionPane.showMessageDialog(this, "Postingan berhasil diunggah ke forum: " + selectedForum);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih forum sebelum mengunggah.");
        }
    }

    private void loadForumList() {
        forumbox.removeAllItems();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM pembuatan_forum";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int Id_forum = resultSet.getInt("Id_forum");
                        String isiForum = resultSet.getString("isi_forum");
                        String idAdmin = resultSet.getString("id_admin");

                        String forumItem = String.format("(%s) %s", Id_forum, isiForum);
                        forumbox.addItem(forumItem);
                    }
                }
            }

            forumbox.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new forumdiskusi();
            }
        });
    }
}