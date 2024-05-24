import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class tabelforumdiskusi {
    private JTable forum;
    private JPanel forumdiskusi;
    private JButton ascendingButton;
    private JButton descendingButton;
    private JButton kembaliButton;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/idoma";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public tabelforumdiskusi() {
        ascendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionSortAscending();
            }
        });

        descendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionSortDescending();
            }
        });

        kembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuutamaadmin.main(new String[0]);
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID_forum");
        tableModel.addColumn("isi_forum");
        tableModel.addColumn("ID_ADMIN");
        tableModel.addColumn("tanggal_pembuatan");


        forum.setModel(tableModel);

        loadData();
    }

    private void loadData() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String query = "SELECT ID_forum, isi_forum, ID_ADMIN, tanggal_pembuatan FROM pembuatan_forum";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                DefaultTableModel tableModel = (DefaultTableModel) forum.getModel();
                tableModel.setRowCount(0);

                while (resultSet.next()) {
                    String ID_forum = resultSet.getString("ID_forum");
                    String isi_forum = resultSet.getString("isi_forum");
                    String ID_ADMIN = resultSet.getString("ID_ADMIN");
                    String tanggal_pembuatan = resultSet.getString("tanggal_pembuatan");

                    String[] rowData = {ID_forum, isi_forum, ID_ADMIN, tanggal_pembuatan};
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectionSortAscending() {
        DefaultTableModel tableModel = (DefaultTableModel) forum.getModel();
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
                swapRows(tableModel, i, minIndex);
            }
        }
    }

    private void selectionSortDescending() {
        DefaultTableModel tableModel = (DefaultTableModel) forum.getModel();
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
                swapRows(tableModel, i, maxIndex);
            }
        }
    }

    private void swapRows(DefaultTableModel tableModel, int i, int j) {
        for (int col = 0; col < tableModel.getColumnCount(); col++) {
            Object temp = tableModel.getValueAt(i, col);
            tableModel.setValueAt(tableModel.getValueAt(j, col), i, col);
            tableModel.setValueAt(temp, j, col);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Forum Diskusi");
            tabelforumdiskusi forumDiskusi = new tabelforumdiskusi();
            frame.setContentPane(forumDiskusi.forumdiskusi);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
