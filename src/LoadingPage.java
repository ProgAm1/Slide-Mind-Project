import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class LoadingPage extends JFrame {

    private final File pdfFile;
    private final JProgressBar bar;

    public LoadingPage(File pdfFile) {
        this.pdfFile = pdfFile;

        setTitle("Processing...");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(new Color(0xE7F1FF));
        p.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JLabel title = new JLabel("Processing slides...", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setForeground(new Color(0x143A5C));
        p.add(title, BorderLayout.NORTH);

        JPanel steps = new JPanel();
        steps.setOpaque(false);
        steps.setLayout(new BoxLayout(steps, BoxLayout.Y_AXIS));

        steps.add(label("• Converting PDF to text"));
        steps.add(label("• Sending text to Claude API"));
        steps.add(label("• Generating flashcards"));
        steps.add(label("• Finalizing output"));

        p.add(steps, BorderLayout.CENTER);

        bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setFont(new Font("Arial", Font.BOLD, 18));
        p.add(bar, BorderLayout.SOUTH);

        setContentPane(p);
        start();
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Arial", Font.PLAIN, 22));
        l.setForeground(new Color(0x143A5C));
        return l;
    }

    private void start() {
        SwingWorker<List<Question>, Integer> w = new SwingWorker<>() {
            @Override
            protected List<Question> doInBackground() throws Exception {
                int[] steps = {20, 45, 75, 100};
                for (int s : steps) {
                    Thread.sleep(500);
                    publish(s);
                }
                return QuizDataLoader.load(pdfFile);
            }

            @Override
            protected void process(List<Integer> chunks) {
                bar.setValue(chunks.get(chunks.size() - 1));
            }

            @Override
            protected void done() {
                try {
                    List<Question> q = get();
                    new CompletedPage(q).setVisible(true);
                    dispose();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoadingPage.this,
                            "Error while generating questions:\n" + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    new HelloPage().setVisible(true);
                }
            }
        };
        w.execute();
    }
}
