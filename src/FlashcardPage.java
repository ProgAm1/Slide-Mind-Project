import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FlashcardPage extends JFrame {

    private final List<Question> questions;
    private int index = 0;
    private int score = 0;

    private final JTextArea questionArea;
    private final JRadioButton[] opt = new JRadioButton[4];
    private final ButtonGroup group = new ButtonGroup();
    private final JButton nextBtn;

    public FlashcardPage(List<Question> q) {
        this.questions = q;

        setTitle("Quiz - Flashcards");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(0xE7F1FF));
        main.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel questionBox = new JPanel(new BorderLayout());
        questionBox.setBackground(Color.WHITE);
        questionBox.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        questionArea = new JTextArea();
        questionArea.setFont(new Font("Arial", Font.BOLD, 26));
        questionArea.setForeground(new Color(0x143A5C));
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);
        questionArea.setOpaque(false);

        questionBox.add(questionArea, BorderLayout.CENTER);
        main.add(questionBox, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < 4; i++) {
            opt[i] = new JRadioButton();
            opt[i].setOpaque(false);
            opt[i].setFont(new Font("Arial", Font.PLAIN, 24));
            opt[i].setForeground(new Color(0x143A5C));
            group.add(opt[i]);
            optionsPanel.add(opt[i]);
            optionsPanel.add(Box.createVerticalStrut(15));
        }

        main.add(optionsPanel, BorderLayout.CENTER);

        nextBtn = new JButton("Next");
        nextBtn.setFont(new Font("Arial", Font.BOLD, 26));
        nextBtn.setBackground(new Color(0x2979FF));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFocusPainted(false);
        nextBtn.setBorder(BorderFactory.createEmptyBorder(12, 55, 12, 55));
        nextBtn.addActionListener(e -> onNext());

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(nextBtn);

        main.add(bottom, BorderLayout.SOUTH);

        setContentPane(main);
        loadQuestion();
    }

    private void loadQuestion() {
        group.clearSelection();

        if (index >= questions.size()) {
            JOptionPane.showMessageDialog(this,
                    "Your Score: " + score + " / " + questions.size(),
                    "Quiz Completed",
                    JOptionPane.INFORMATION_MESSAGE);
            new HelloPage().setVisible(true);
            dispose();
            return;
        }

        Question q = questions.get(index);

        questionArea.setText("Q" + (index + 1) + ": " + q.getText());

        List<String> options = q.getOptions();
        for (int i = 0; i < 4; i++) {
            opt[i].setText(options.get(i));
        }

        if (index == questions.size() - 1) {
            nextBtn.setText("Finish");
        } else {
            nextBtn.setText("Next");
        }
    }

    private void onNext() {
        int ans = -1;

        for (int i = 0; i < 4; i++) {
            if (opt[i].isSelected()) {
                ans = i;
                break;
            }
        }

        if (ans == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer.");
            return;
        }

        if (ans == questions.get(index).getCorrectIndex()) {
            score++;
        }

        index++;
        loadQuestion();
    }
}
