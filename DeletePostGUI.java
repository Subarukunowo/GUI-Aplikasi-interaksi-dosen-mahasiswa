import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeletePostGUI extends JFrame {

    private JButton hapusButton;
    private JPanel mainPanel;
    private JList<String> postList;
    private DefaultListModel<String> listModel;
    private JButton batalkanButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/postingan";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public DeletePostGUI() {
        setTitle("Hapus Postingan");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        listModel = new DefaultListModel<>();
        postList = new JList<>(listModel);
        postList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        hapusButton = new JButton("Hapus");
        hapusButton.addActionListener(this::deleteButtonActionPerformed);

        mainPanel.add(new JScrollPane(postList));
        mainPanel.add(hapusButton);

        add(mainPanel);
        setVisible(true);

        // Isi daftar postingan saat aplikasi dijalankan
        refreshPostList();
    }

    private void deleteButtonActionPerformed(ActionEvent event) {
        int selectedIndex = postList.getSelectedIndex();
        if (selectedIndex != -1) {
            int selectedPostId = Integer.parseInt(listModel.get(selectedIndex).split("\t")[0]);
            deletePostFromDatabase(selectedPostId);

            // Refresh daftar postingan setelah dihapus
            refreshPostList();

            JOptionPane.showMessageDialog(mainPanel, "Postingan berhasil dihapus.");
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Pilih postingan yang ingin dihapus.");
        }
    }

    private void deletePostFromDatabase(int postId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM posts WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, postId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshPostList() {
        listModel.clear();
        List<String> postTitles = getUploadedFiles();
        for (String title : postTitles) {
            listModel.addElement(title);
        }
    }

    public static List<String> getUploadedFiles() {
        List<String> fileNames = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM posts";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String title = resultSet.getString("konten");
                        fileNames.add(id + "\t" + title);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fileNames;
    }

    public static void main() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            DeletePostGUI deletePostGUI = new DeletePostGUI();
        });
    }
    public JPanel getMainPanel(){
        return mainPanel;
    }
}
