    package com.shakalinux.biblia.service;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.*;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    @Service
    public class IaMensagemService {

        @Value("${huggingface.api.key}")
        private String apiKey;

        private static final String API_URL = "https://router.huggingface.co/v1/chat/completions";

        public String gerarMensagemBiblica(String tema) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(apiKey);

                String prompt = "Crie uma reflexão bíblica sobre o tema: " + tema + ". Use um tom inspirador e simples.";

                Map<String, Object> body = new HashMap<>();
                body.put("model", "moonshotai/Kimi-K2-Instruct-0905");
                body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
                ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);

                Map<String, Object> choice = ((List<Map<String, Object>>) response.getBody().get("choices")).get(0);
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                return message.get("content").toString();

            } catch (Exception e) {
                e.printStackTrace();
                return "Erro ao gerar estudo. Tente novamente.";
            }
        }
    }
