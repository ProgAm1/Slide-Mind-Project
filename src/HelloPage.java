import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;

public class HelloPage extends JFrame {

    private File selectedPdf;
    private JLabel fileLabel;

    public HelloPage() {
        setTitle("SlideMind - Upload");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(0xE7F1FF));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JLabel title = new JLabel("Welcome to SlideMind!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(new Color(0x143A5C));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        fileLabel = new JLabel("No file selected", SwingConstants.CENTER);
        fileLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        fileLabel.setForeground(new Color(0x143A5C));
        fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton uploadBtn = new JButton("Upload .PDF file");
        styleSecondary(uploadBtn);

        JButton generateBtn = new JButton("Generate Flashcards");
        stylePrimary(generateBtn);

        JButton savedBtn = new JButton("View Saved Flashcards");
        styleSecondary(savedBtn);

        uploadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        savedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        uploadBtn.addActionListener(e -> chooseFile());
        generateBtn.addActionListener(e -> generate());
        savedBtn.addActionListener(e -> openSaved());

        center.add(Box.createVerticalStrut(30));
        center.add(fileLabel);
        center.add(Box.createVerticalStrut(40));
        center.add(uploadBtn);
        center.add(Box.createVerticalStrut(15));
        center.add(generateBtn);
        center.add(Box.createVerticalStrut(15));
        center.add(savedBtn);

        mainPanel.add(center, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPdf = chooser.getSelectedFile();
            fileLabel.setText("Selected: " + selectedPdf.getName());
        }
    }

    private void generate() {
        if (selectedPdf == null) {
            JOptionPane.showMessageDialog(this, "Please upload a PDF first.");
            return;
        }
        new LoadingPage(selectedPdf).setVisible(true);
        dispose();
    }

    private void openSaved() {
        new SavedFlashcardsPage().setVisible(true);
        dispose();
    }

    private void stylePrimary(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setBackground(new Color(0x2979FF));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 35));
        btn.setFocusPainted(false);
    }

    private void styleSecondary(JButton btn) {
        btn.setFont(new Font("Arial", Font.PLAIN, 20));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(0x143A5C));
        btn.setBorder(BorderFactory.createLineBorder(new Color(0x2979FF), 2));
        btn.setFocusPainted(false);
    }
}
