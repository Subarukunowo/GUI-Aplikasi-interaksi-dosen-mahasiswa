import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EdItPostGUI extends JFrame {

    private JButton Browsebtn;
    private JButton edibtn;
    private JPanel mainPanel;
    private JTextArea DescriptionTextArea;
    private JLabel selectedFileLabel;
    private JButton batalkanButton;
    private JLabel editlabel;
    private JList<String> postList;
    private DefaultListModel<String> listModel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/postingan";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private int selectedPostId;

    public EdItPostGUI() {
        setTitle("Edit Postingan");
        setSize(600, 400); // Ubah ukuran JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Inisialisasi JList dan DefaultListModel
        listModel = new DefaultListModel<>();
        postList = new JList<>(listModel);
        postList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        postList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = postList.getSelectedIndex();
                if (selectedIndex != -1) {
                    // Perubahan: Mengambil id_post dari listModel yang diindeks
                    selectedPostId = Integer.parseInt(listModel.get(selectedIndex).split("\t")[0]);

                    // Tampilkan konten postingan yang dipilih di JTextArea
                    String selectedContent = listModel.get(selectedIndex).split("\t")[1];
                    DescriptionTextArea.setText(selectedContent);
                }
            }
        });

        edibtn = new JButton("Edit");
        edibtn.addActionListener(this::editButtonActionPerformed);

        Browsebtn = new JButton("BROWSE");
        Browsebtn.addActionListener(this::browseButtonActionPerformed);

        selectedFileLabel = new JLabel("File belum dipilih");
        DescriptionTextArea = new JTextArea("");
        batalkanButton = new JButton("Cancel");
        editlabel = new JLabel("");

        mainPanel.add(editlabel);
        mainPanel.add(new JScrollPane(postList));
        mainPanel.add(Browsebtn);
        mainPanel.add(DescriptionTextArea);
        mainPanel.add(edibtn);
        mainPanel.add(batalkanButton);

        add(mainPanel);
        setVisible(true);

        batalkanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        refreshPostList();
    }

    private void browseButtonActionPerformed(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText("File dipilih: " + selectedFile.getName());
        }
    }

    private void editButtonActionPerformed(ActionEvent event) {
        String description = DescriptionTextArea.getText();
        String selectedFileName = selectedFileLabel.getText().replace("File dipilih: ", "");

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Deskripsi harus diisi sebelum mengedit.");
            return;
        }

        updatePostInDatabase(selectedFileName, description);

        JOptionPane.showMessageDialog(mainPanel, "File berhasil diedit dengan deskripsi:\n" + description);
        refreshPostList();
    }

    private void updatePostInDatabase(String gambar, String isi_post) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE posts SET gambar = ?, konten = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, gambar);
                preparedStatement.setString(2, isi_post);
                preparedStatement.setInt(3, selectedPostId);
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
                        // Perubahan: Mengambil id_post dan judul dari database
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

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EdItPostGUI editPostGUI = new EdItPostGUI();
            }
        });
    }
    public  JPanel getMainPanel() {
        return mainPanel;
    }
}
