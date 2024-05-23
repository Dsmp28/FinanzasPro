package com.example.finanzaspro;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SendGridEmailService {

    private String apiKey;

    public SendGridEmailService() {
        this.apiKey = ManejadorEncriptacion.leerTextoDeJSON("ApiKey.json", "apiKey");
    }

    public void sendEmail(String subject, String body) {
        String fromEmail = "finanzaspro01@outlook.com";
        String toEmail = ManejadorEncriptacion.leerCorreoDeJSON("DatoCorreo.json");

        // Crear el cuerpo del correo electrónico en formato JSON
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode emailJson = objectMapper.createObjectNode();
        emailJson.put("subject", subject);

        ObjectNode fromNode = objectMapper.createObjectNode();
        fromNode.put("email", fromEmail);
        emailJson.set("from", fromNode);

        ObjectNode toNode = objectMapper.createObjectNode();
        toNode.put("email", toEmail);
        ObjectNode personalizationNode = objectMapper.createObjectNode();
        personalizationNode.putArray("to").add(toNode);
        emailJson.putArray("personalizations").add(personalizationNode);

        ObjectNode contentNode = objectMapper.createObjectNode();
        contentNode.put("type", "text/plain");
        contentNode.put("value", body);
        emailJson.putArray("content").add(contentNode);

        String emailBody;
        try {
            emailBody = objectMapper.writeValueAsString(emailJson);
        } catch (IOException e) {
            throw new RuntimeException("Error creating email JSON body", e);
        }

        // Crear el cliente HTTP y la solicitud
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.sendgrid.com/v3/mail/send"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(emailBody))
                .build();

        // Enviar la solicitud y manejar la respuesta
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}


