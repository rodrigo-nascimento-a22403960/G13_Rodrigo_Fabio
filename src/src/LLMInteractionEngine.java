import javax.net.ssl.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class LLMInteractionEngine {

    private static final String API_URL = "https://modelos.ai.ulusofona.pt/v1/completions";
    private static final String API_KEY = "sk-Uhv5ddkIyTMP-cqdl7DeEg"; // <-- coloca aqui a tua chave
    private static final String MODEL = "gpt-4-turbo";

    private HttpClient httpClient;

    public LLMInteractionEngine() {
        try {
            this.httpClient = createUnsafeHttpClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HttpClient createUnsafeHttpClient() throws Exception {
        TrustManager[] trustAll = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] xcs, String string) {}
                    public void checkServerTrusted(X509Certificate[] xcs, String string) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAll, new SecureRandom());

        return HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
    }

    public String sendPrompt(String prompt) {
        try {
            String jsonBody = """
            {
              "model": "%s",
              "prompt": "%s",
              "max_tokens": 100
            }
            """.formatted(MODEL, prompt.replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com o LLM", e);
        }
    }
}
