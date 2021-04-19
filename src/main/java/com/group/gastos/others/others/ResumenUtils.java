package com.group.gastos.others.others;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.gastos.models.DolarAPI;
import com.group.gastos.models.Resumen;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class ResumenUtils {

    public Float getPrecioDolar() throws IOException, InterruptedException {
        Float result;
        URI url = URI.create("https://api-dolar-argentina.herokuapp.com/api/galicia");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        DolarAPI dolarAPI = objectMapper.readValue(response.body(), new TypeReference<DolarAPI>() { });
        result = dolarAPI.getVenta();
        result = result + result * 0.3F + result * 0.35F;
        return  result;
    }

    public void calculateGastoTotal(Resumen resumen){
        AtomicReference<Float> totalGasto = new AtomicReference<>(0F);
        resumen.getItems().forEach(s -> {
            if(s.getIsPesos()){
                totalGasto.updateAndGet(v -> v + s.getMonto());
            } else {
                totalGasto.updateAndGet(v -> v + s.getMonto() * resumen.getValorDolar());
            }
        });
        resumen.setTotalGasto(totalGasto.get());
    }
}
