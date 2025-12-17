public class JSONUtils {
    public static String getJsonString(String json, String key) {
        if (json == null) return null;
        String lower = json.toLowerCase();
        if (lower.contains("\"text\"")) {
            int idx = lower.indexOf("\"text\"");
            int colon = json.indexOf(':', idx);
            if (colon >= 0) return extractString(json, colon + 1);
        }
        if (lower.contains("\"content\"")) {
            int idx = lower.indexOf("\"content\"");
            int colon = json.indexOf(':', idx);
            if (colon >= 0) return extractString(json, colon + 1);
        }
        if (lower.contains("\"choices\"")) {
            int choices = lower.indexOf("\"choices\"");
            int msg = lower.indexOf("message", choices);
            if (msg >= 0) {
                int content = lower.indexOf("\"content\"", msg);
                if (content >= 0) {
                    int colon = json.indexOf(':', content);
                    if (colon >= 0) return extractString(json, colon + 1);
                }
            }
        }
        return null;
    }

    private static String extractString(String json, int start) {
        int firstQuote = json.indexOf('"', start);
        if (firstQuote < 0) return null;
        int endQuote = firstQuote + 1;
        StringBuilder sb = new StringBuilder();
        while (endQuote < json.length()) {
            char c = json.charAt(endQuote++);
            if (c == '"' && json.charAt(endQuote - 2) != '\\') break;
            sb.append(c);
        }
        return sb.toString();
    }
}
