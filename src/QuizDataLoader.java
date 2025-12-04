import okhttp3.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class QuizDataLoader {

    private static final String API_KEY = System.getenv("ANTHROPIC_API_KEY");
    private static final String URL = "https://api.anthropic.com/v1/messages";
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON_MEDIA = MediaType.get("application/json; charset=utf-8");

    public static List<Question> load(File pdf) throws Exception {
        String text = readPdf(pdf);
        String prompt = buildPrompt(text);
        String response = askClaude(prompt);
        return parseQuestions(response);
    }

    private static String readPdf(File f) throws IOException {
        try (PDDocument doc = PDDocument.load(f)) {
            return new PDFTextStripper().getText(doc);
        }
    }

    private static String buildPrompt(String t) {
        return """
                You are a flashcard generator.

                Produce JSON ONLY with this format:

                {
                  "questions":[
                    {
                      "question":"string",
                      "options":["A","B","C","D"],
                      "correctIndex":0
                    }
                  ]
                }

                Make questions clear and short.
                Text:
                """ + t;
    }

    private static String askClaude(String prompt) throws IOException {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IOException("API KEY not set (ANTHROPIC_API_KEY)");
        }

        JSONObject body = new JSONObject()
                .put("model", "claude-3-haiku-20240307")
                .put("max_tokens", 1024)
                .put("messages", new JSONArray()
                        .put(new JSONObject()
                                .put("role", "user")
                                .put("content", prompt)));

        Request req = new Request.Builder()
                .url(URL)
                .addHeader("x-api-key", API_KEY)
                .addHeader("anthropic-version", "2023-06-01")
                .post(RequestBody.create(body.toString(), JSON_MEDIA))
                .build();

        Response res = client.newCall(req).execute();

        String raw = res.body().string();
        System.out.println("\n\n===== RAW CLAUDE RESPONSE =====\n" +
                raw +
                "\n================================\n");

        return raw;
    }

    private static List<Question> parseQuestions(String json) {

        JSONObject root = new JSONObject(json);
        JSONArray content = root.getJSONArray("content");

        String innerJson = content.getJSONObject(0).getString("text");

        int start = innerJson.indexOf("{");
        if (start > 0) {
            innerJson = innerJson.substring(start);
        }

        JSONObject data = new JSONObject(innerJson);
        JSONArray qArr = data.getJSONArray("questions");

        List<Question> list = new ArrayList<>();

        for (int i = 0; i < qArr.length(); i++) {
            JSONObject q = qArr.getJSONObject(i);

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
