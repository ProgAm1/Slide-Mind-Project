import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SavedFlashcardsPage extends JFrame {

    private final DefaultListModel<SavedQuizInfo> model = new DefaultListModel<>();
    private final JList<SavedQuizInfo> list = new JList<>(model);

    public SavedFlashcardsPage() {
        setTitle("Saved Flashcards");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(0xE7F1FF));
        main.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("Saved Flashcards", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(0x143A5C));
        main.add(title, BorderLayout.NORTH);

        list.setCellRenderer(new SavedQuizRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(list);
        main.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);

        JButton openBtn = new JButton("Open");
        openBtn.setFont(new Font("Arial", Font.BOLD, 20));
        openBtn.setBackground(new Color(0x2979FF));
        openBtn.setForeground(Color.WHITE);
        openBtn.setFocusPainted(false);
        openBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(new Color(0x143A5C));
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(0x2979FF), 2));
        backBtn.setFocusPainted(false);

        openBtn.addActionListener(e -> openSelected());
        backBtn.addActionListener(e -> {
            new HelloPage().setVisible(true);
            dispose();
        });

        bottom.add(openBtn);
        bottom.add(Box.createHorizontalStrut(15));
        bottom.add(backBtn);

        main.add(bottom, BorderLayout.SOUTH);

        setContentPane(main);
        loadSaved();
    }

    private void loadSaved() {
        model.clear();
        List<SavedQuizInfo> infos = StorageManager.listQuizzes();
        for (SavedQuizInfo info : infos) {
            model.addElement(info);
        }
    }

    private void openSelected() {
        SavedQuizInfo info = list.getSelectedValue();
        if (info == null) {
            JOptionPane.showMessageDialog(this, "Please select a flashcard set.");
            return;
        }

        try {
            List<Question> questions = StorageManager.loadQuiz(info.file());
            new FlashcardPage(questions).setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to open flashcards:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class SavedQuizRenderer extends JLabel implements ListCellRenderer<SavedQuizInfo> {
        private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        public SavedQuizRenderer() {
            setOpaque(true);
            setFont(new Font("Arial", Font.PLAIN, 18));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends SavedQuizInfo> list,
                                                      SavedQuizInfo value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            String text = value.name() + "  (" + fmt.format(new Date(value.timestamp())) + ")";
            setText(text);

            if (isSelected) {
                setBackground(new Color(0x2979FF));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(new Color(0x143A5C));
            }
            return this;
        }
    }
}
