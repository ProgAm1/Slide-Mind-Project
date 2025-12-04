import javax.swing.*;
import java.io.File;

public class SlideMind {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HelloPage().setVisible(true));
    }
}
