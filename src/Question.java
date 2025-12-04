import java.util.List;

public class Question {
    private final String text;
    private final List<String> options;
    private final int correctIndex;

    public Question(String t, List<String> o, int c) {
        text = t;
        options = o;
        correctIndex = c;
    }

    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
}
