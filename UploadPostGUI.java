import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UploadPostGUI extends JFrame {

    private JButton browseButton;
    private JButton uploadButton;
    private JPanel mainPanel;
    private JTextArea DescriptionTextArea;
    private JLabel SelectedfFileLabel;
    private JButton cancelbtn;
    private JButton editButton;
    private JButton hapusButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/postingan";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public UploadPostGUI() {
        setTitle("Upload Postingan");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel utama
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        uploadButton = new JButton("Upload");
        uploadButton.addActionListener(this::uploadButtonActionPerformed);

        browseButton = new JButton("BROWSE");
        browseButton.addActionListener(this::browseButtonActionPerformed);

        SelectedfFileLabel = new JLabel("File belum dipilih");
        DescriptionTextArea = new JTextArea("");
        cancelbtn = new JButton("Cancel");
        editButton = new JButton("Edit");
        hapusButton = new JButton("Hapus");

        mainPanel.add(SelectedfFileLabel);
        mainPanel.add(browseButton);
        mainPanel.add(DescriptionTextArea);
        mainPanel.add(uploadButton);
        mainPanel.add(cancelbtn);
        mainPanel.add(editButton);
        mainPanel.add(hapusButton);
        add(mainPanel);
        setVisible(true);
        cancelbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuutama.main(new String[0]);
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EdItPostGUI.main();
                JComponent component = (JComponent)e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeletePostGUI.main();
                JComponent component = (JComponent)e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });
    }

    private void browseButtonActionPerformed(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Gambar Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {

            java.io.File selectedFile = fileChooser.getSelectedFile();
            SelectedfFileLabel.setText("File dipilih: " + selectedFile.getName());
        }
    }

    private void uploadButtonActionPerformed(ActionEvent event) {
        String description = DescriptionTextArea.getText();
        String selectedFileName = SelectedfFileLabel.getText().replace("File dipilih: ", "");

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Deskripsi harus diisi sebelum mengupload.");
            return;
        }

        int generatedId = savePostToDatabase(selectedFileName, description);
        JOptionPane.showMessageDialog(mainPanel, "File berhasil diunggah dengan deskripsi:\n" + description );
    }

    private int savePostToDatabase(String gambar, String isi_post) {
        int generatedId = -1;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO posts (gambar, konten) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, gambar);
                preparedStatement.setString(2, isi_post);
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }
    public static List<String> getUploadedFiles() {
        List<String> fileNames = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM posts";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        fileNames.add(resultSet.getString("konten"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fileNames;
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
                new UploadPostGUI();
            }
        });
    }
}
