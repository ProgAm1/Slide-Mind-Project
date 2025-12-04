import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

public class StorageManager {

    private static final String SAVE_DIR = "saved_quizzes";

    private static void ensureDir() throws IOException {
        Path p = Paths.get(SAVE_DIR);
        if (!Files.exists(p)) {
            Files.createDirectories(p);
        }
    }

    public static void saveQuiz(String name, List<Question> questions) throws IOException {
        ensureDir();

        JSONObject root = new JSONObject();
        root.put("name", name);
        root.put("timestamp", System.currentTimeMillis());

        JSONArray arr = new JSONArray();
        for (Question q : questions) {
            JSONObject obj = new JSONObject();
            obj.put("question", q.getText());
            obj.put("options", new JSONArray(q.getOptions()));
            obj.put("correctIndex", q.getCorrectIndex());
            arr.put(obj);
        }
        root.put("questions", arr);

        String safe = name.replaceAll("[^a-zA-Z0-9-_]", "_");
        String fileName = safe + "_" + System.currentTimeMillis() + ".json";

        Path file = Paths.get(SAVE_DIR, fileName);
        Files.writeString(file, root.toString(2), StandardCharsets.UTF_8);
    }

    public static List<SavedQuizInfo> listQuizzes() {
        List<SavedQuizInfo> list = new ArrayList<>();
        Path dir = Paths.get(SAVE_DIR);
        if (!Files.exists(dir)) return list;

        try {
            Files.list(dir)
                    .filter(p -> p.toString().endsWith(".json"))
                    .forEach(p -> {
                        try {
                            String text = Files.readString(p, StandardCharsets.UTF_8);
                            JSONObject root = new JSONObject(text);
                            String name = root.optString("name", p.getFileName().toString());
                            long ts = root.optLong("timestamp", 0L);
                            list.add(new SavedQuizInfo(name, ts, p.toFile()));
                        } catch (Exception ignored) {}
                    });
        } catch (IOException ignored) {}

        return list;
    }

    public static List<Question> loadQuiz(File file) throws IOException {
        String text = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        JSONObject root = new JSONObject(text);
        JSONArray arr = root.getJSONArray("questions");

        List<Question> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject q = arr.getJSONObject(i);
            JSONArray optArr = q.getJSONArray("options");
            List<String> options = new ArrayList<>();
            for (int j = 0; j < optArr.length(); j++) {
                options.add(optArr.getString(j));
            }
            list.add(new Question(
                    q.getString("question"),
                    options,
                    q.getInt("correctIndex")
            ));
        }
        return list;
    }
}
