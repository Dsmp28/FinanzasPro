package com.example.finanzaspro;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.collections.ObservableList;

public class SendGridEmailService {

    private String apiKey;

    public SendGridEmailService() {
        this.apiKey = ManejadorEncriptacion.leerTextoDeJSON("ApiKey.json", "apiKey");
    }

    public void sendEmail(String subject, String body) {
        String fromEmail = ManejadorEncriptacion.leerCorreoDeJSON("DatoCorreoFrom.json");
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

    public void sendEmailWithAttachment(String subject, String body, ObservableList<Movimiento> movimientos) {
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

        // Convert ObservableList<Movimiento> to CSV and encode it to base64
        String csvData = convertMovimientosToCsv(movimientos);
        String encodedCsv = Base64.getEncoder().encodeToString(csvData.getBytes());

        // Create the CSV attachment node
        ObjectNode csvAttachmentNode = objectMapper.createObjectNode();
        csvAttachmentNode.put("content", encodedCsv);
        csvAttachmentNode.put("type", "text/csv");
        csvAttachmentNode.put("filename", "movimientos.csv");
        csvAttachmentNode.put("disposition", "attachment");

        // Add the attachment node to the email JSON
        emailJson.putArray("attachments").add(csvAttachmentNode);

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

    public String convertMovimientosToCsv(ObservableList<Movimiento> movimientos) {
        StringWriter writer = new StringWriter();
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            // Write the header
            bufferedWriter.write("Categoria,tipo,titulo,cantidad,fecha,Es recurrente,intervalo dias");
            bufferedWriter.newLine();

            // Write the data
            for (Movimiento movimiento : movimientos) {
                double cantidad = movimiento.getCantidad();
                if (movimiento.getTipo().equals("Egreso")) {
                    cantidad = cantidad * -1;
                }
                bufferedWriter.write(String.format("%s,%s,%s,%f,%s,%b,%s",
                        movimiento.getCategoria().getTitulo(),
                        movimiento.getTipo(),
                        movimiento.getTitulo(),
                        cantidad,
                        movimiento.getFecha().toString(),
                        movimiento.isEsRecurrente(),
                        movimiento.getIntervaloDias()));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error converting ObservableList<Movimiento> to CSV", e);
        }
        return writer.toString();
    }
}


