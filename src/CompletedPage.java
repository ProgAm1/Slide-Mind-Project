import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CompletedPage extends JFrame {

    private final List<Question> questions;

    public CompletedPage(List<Question> questions) {
        this.questions = questions;

        setTitle("Flashcards Generated!");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(0xE7F1FF));
        p.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JLabel t = new JLabel("Flashcards Generated!", SwingConstants.CENTER);
        t.setFont(new Font("Arial", Font.BOLD, 36));
        t.setForeground(new Color(0x143A5C));

        JLabel s = new JLabel("You can now start the quiz or save it for later.", SwingConstants.CENTER);
        s.setFont(new Font("Arial", Font.PLAIN, 22));
        s.setForeground(new Color(0x143A5C));

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalGlue());
        center.add(t);
        center.add(Box.createVerticalStrut(15));
        center.add(s);
        center.add(Box.createVerticalGlue());

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);

        JButton startBtn = new JButton("Start Quiz");
        startBtn.setFont(new Font("Arial", Font.BOLD, 22));
        startBtn.setBackground(new Color(0x2979FF));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 35));

        JButton saveBtn = new JButton("Save Flashcards");
        saveBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        saveBtn.setBackground(Color.WHITE);
        saveBtn.setForeground(new Color(0x143A5C));
        saveBtn.setBorder(BorderFactory.createLineBorder(new Color(0x2979FF), 2));
        saveBtn.setFocusPainted(false);

        JButton homeBtn = new JButton("Back to Home");
        homeBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        homeBtn.setBackground(Color.WHITE);
        homeBtn.setForeground(new Color(0x143A5C));
        homeBtn.setBorder(BorderFactory.createLineBorder(new Color(0x2979FF), 2));
        homeBtn.setFocusPainted(false);

        startBtn.addActionListener(e -> {
            new FlashcardPage(questions).setVisible(true);
            dispose();
        });

        saveBtn.addActionListener(e -> onSave());
        homeBtn.addActionListener(e -> {
            new HelloPage().setVisible(true);
            dispose();
        });

        bottom.add(startBtn);
        bottom.add(Box.createHorizontalStrut(15));
        bottom.add(saveBtn);
        bottom.add(Box.createHorizontalStrut(15));
        bottom.add(homeBtn);

        p.add(center, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        setContentPane(p);
    }

    private void onSave() {
        String name = JOptionPane.showInputDialog(this,
                "Enter a name for this flashcard set:",
                "Save Flashcards",
                JOptionPane.PLAIN_MESSAGE);

        if (name == null || name.trim().isEmpty()) {
            return;
        }

        try {
            StorageManager.saveQuiz(name.trim(), questions);
            JOptionPane.showMessageDialog(this, "Flashcards saved successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save flashcards:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
