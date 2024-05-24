import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Postingan extends JFrame {
    private JTable postingan;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private JButton ascendingButton;
    private JButton descendingButton;
    private JButton cancelButton;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/postingan";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Postingan() {
        setTitle("Postingan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        mainPanel = new JPanel();
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Gambar");
        tableModel.addColumn("Konten");

        postingan = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(postingan);

        ascendingButton = new JButton("Ascending");
        descendingButton = new JButton("Descending");
        cancelButton = new JButton("Cancel");

        ascendingButton.addActionListener(e -> sortData("ASC"));
        descendingButton.addActionListener(e -> sortData("DESC"));
        cancelButton.addActionListener(e -> {
            menuutamaadmin.main(new String[0]);
            JComponent component = (JComponent) e.getSource();
            Window window = SwingUtilities.getWindowAncestor(component);
            window.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ascendingButton);
        buttonPanel.add(descendingButton);
        buttonPanel.add(cancelButton);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadData();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadData() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM posts";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                tableModel.setRowCount(0);

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String gambar = resultSet.getString("gambar");
                    String konten = resultSet.getString("konten");

                    String[] rowData = {id, gambar, konten};
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortData(String sortOrder) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String columnName = "konten";
            String query = "SELECT * FROM posts ORDER BY " + columnName + " " + sortOrder;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                tableModel.setRowCount(0);

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String gambar = resultSet.getString("gambar");
                    String konten = resultSet.getString("konten");

                    String[] rowData = {id, gambar, konten};
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectionSortAscending() {
        int rowCount = tableModel.getRowCount();

        for (int i = 0; i < rowCount - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < rowCount; j++) {
                String currentData = (String) tableModel.getValueAt(j, 2);
                String minData = (String) tableModel.getValueAt(minIndex, 2);

                if (currentData.compareTo(minData) < 0) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                swapRows(i, minIndex);
            }
        }
    }

    private void selectionSortDescending() {
        int rowCount = tableModel.getRowCount();

        for (int i = 0; i < rowCount - 1; i++) {
            int maxIndex = i;

            for (int j = i + 1; j < rowCount; j++) {
                String currentData = (String) tableModel.getValueAt(j, 2);
                String maxData = (String) tableModel.getValueAt(maxIndex, 2);

                if (currentData.compareTo(maxData) > 0) {
                    maxIndex = j;
                }
            }

            if (maxIndex != i) {
                swapRows(i, maxIndex);
            }
        }
    }

    private void swapRows(int i, int j) {
        for (int col = 0; col < tableModel.getColumnCount(); col++) {
            Object temp = tableModel.getValueAt(i, col);
            tableModel.setValueAt(tableModel.getValueAt(j, col), i, col);
            tableModel.setValueAt(temp, j, col);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Postingan::new);
    }
}
