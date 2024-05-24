import javax.swing.*;
import java.awt.*;

public class menuutamaadmin {
    private JPanel admin;
    private JButton periksaLaporanButton;
    private JButton periksaPostinganButton;
    private JButton keluarButton;
    private JButton periksaForumButton;

    public menuutamaadmin() {
        periksaForumButton.addActionListener(e -> {
            tabelforumdiskusi.main(new String[0]);
            JComponent component = (JComponent) e.getSource();
            Window window = SwingUtilities.getWindowAncestor(component);
            window.dispose();
        });

        periksaLaporanButton.addActionListener(e -> {
            LaporanGUI.main(new String[0]);
            JComponent component = (JComponent) e.getSource();
            Window window = SwingUtilities.getWindowAncestor(component);
            window.dispose();
        });

        periksaPostinganButton.addActionListener(e -> {
            Postingan.main(new String[0]);
            JComponent component = (JComponent) e.getSource();
            Window window = SwingUtilities.getWindowAncestor(component);
            window.dispose();
        });

        keluarButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menu Utama Admin");
            menuutamaadmin adminMenu = new menuutamaadmin();
            frame.setContentPane(adminMenu.admin);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
