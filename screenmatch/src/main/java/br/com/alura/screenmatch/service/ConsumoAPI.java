package br.com.alura.screenmatch.service;


import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.text.Normalizer;

public class ConsumoAPI {

    public String obterDados(String endereco) {

        endereco = Normalizer.normalize(endereco, Normalizer.Form.NFD)
                .replace("\\p{InCombiningDiacritical}", "")
                .replace(" ","+")
                .toLowerCase();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String json = response.body();
        return json;
    }

}
