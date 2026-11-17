
import gui.LibraryGUI;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LibraryGUI libraryGUI = new LibraryGUI();

            libraryGUI.setVisible(true);

        });
    }
}
