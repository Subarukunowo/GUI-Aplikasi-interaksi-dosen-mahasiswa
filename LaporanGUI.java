import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LaporanGUI extends JFrame {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/idoma";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DefaultTableModel tableModel;
    private JTable dataLaporan;
    private JPanel mainPanel;
    private JButton ascendingButton;
    private JButton descendingButton;
    private JButton cancelButton;

    public LaporanGUI() {
        setTitle("Laporan Pengguna");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        mainPanel = new JPanel();
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Isi Laporan");
        tableModel.addColumn("Status Laporan");
        tableModel.addColumn("Nama User");

        dataLaporan = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataLaporan);

        JButton ascendingButton = new JButton("Sort Ascending");
        JButton descendingButton = new JButton("Sort Descending");

        ascendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortData("ASC");
            }
        });

        descendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortData("DESC");
            }
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
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuutamaadmin.main(new String[0]);
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });
    }

    private void loadData() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM laporanidoma";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                tableModel.setRowCount(0);

                while (resultSet.next()) {
                    String isiLaporan = resultSet.getString("isi_laporan");
                    String statusLaporan = resultSet.getString("status_laporan");
                    String namaUser = resultSet.getString("nama_user");

                    String[] rowData = {isiLaporan, statusLaporan, namaUser};
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
            String columnName = "isi_laporan";
            String query = "SELECT * FROM laporanidoma ORDER BY " + columnName + " " + sortOrder;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                tableModel.setRowCount(0);

                while (resultSet.next()) {
                    String isiLaporan = resultSet.getString("isi_laporan");
                    String statusLaporan = resultSet.getString("status_laporan");
                    String namaUser = resultSet.getString("nama_user");

                    String[] rowData = {isiLaporan, statusLaporan, namaUser};
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
                String currentData = (String) tableModel.getValueAt(j, 0);
                String minData = (String) tableModel.getValueAt(minIndex, 0);

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
                String currentData = (String) tableModel.getValueAt(j, 0);
                String maxData = (String) tableModel.getValueAt(maxIndex, 0);

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
        SwingUtilities.invokeLater(() -> new LaporanGUI());
    }
}
