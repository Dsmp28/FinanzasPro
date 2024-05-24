package com.example.finanzaspro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManejadorEncriptacion {

    public static void guardarPresupuestoEnJSON(double monto, String archivoJSON) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return;
        }

        try {
            String montoEncriptado = EncryptionUtil.encrypt(Double.toString(monto));
            Map<String, String> datos = new HashMap<>();
            datos.put("monto", montoEncriptado);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(file, false)) { // `false` para no agregar sino sobreescribir
                gson.toJson(datos, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double leerPresupuestoDeJSON(String archivoJSON) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return 0.0;
        }

        double monto = 0.0;
        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("monto")) {
                String montoEncriptado = jsonObject.get("monto").getAsString();
                monto = Double.parseDouble(EncryptionUtil.decrypt(montoEncriptado));
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            return 0.0;
        }
        return monto;
    }

    public static void guardarMovimientosEnJSON(List<Movimiento> movimientos, String archivoJSON) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return;
        }

        try {
            List<Map<String, String>> datosEncriptados = new ArrayList<>();
            for (Movimiento movimiento : movimientos) {
                Map<String, String> movimientoEncriptado = new HashMap<>();
                movimientoEncriptado.put("categoria_id", EncryptionUtil.encrypt(Integer.toString(movimiento.getCategoria().getId())));
                movimientoEncriptado.put("tipo", EncryptionUtil.encrypt(movimiento.getTipo()));
                movimientoEncriptado.put("titulo", EncryptionUtil.encrypt(movimiento.getTitulo()));
                movimientoEncriptado.put("cantidad", EncryptionUtil.encrypt(Double.toString(movimiento.getCantidad())));
                movimientoEncriptado.put("fecha", EncryptionUtil.encrypt(movimiento.getFecha().toString()));
                movimientoEncriptado.put("esRecurrente", EncryptionUtil.encrypt(Boolean.toString(movimiento.isEsRecurrente())));
                if (movimiento.isEsRecurrente()) {
                    movimientoEncriptado.put("intervaloDias", EncryptionUtil.encrypt(Integer.toString(movimiento.getIntervaloDias())));
                }

                datosEncriptados.add(movimientoEncriptado);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(file, false)) { // `false` para no agregar sino sobreescribir
                gson.toJson(datosEncriptados, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Movimiento> leerMovimientosDeJSON(String archivoJSON, List<Categoria> categorias) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return FXCollections.observableArrayList();
        }

        ObservableList<Movimiento> movimientos = FXCollections.observableArrayList();
        try {
            Gson gson = new Gson();
            Type tipoListaMovimientos = new TypeToken<List<Map<String, String>>>() {}.getType();
            try (FileReader reader = new FileReader(file)) {
                List<Map<String, String>> datosEncriptados = gson.fromJson(reader, tipoListaMovimientos);

                if (datosEncriptados == null) {
                    return movimientos;
                }else {
                    for (Map<String, String> datos : datosEncriptados) {
                        int categoriaId = Integer.parseInt(EncryptionUtil.decrypt(datos.get("categoria_id")));
                        Categoria categoria = categorias.stream().filter(c -> c.getId() == categoriaId).findFirst().orElse(null);

                        if (categoria != null) {
                            String tipo = EncryptionUtil.decrypt(datos.get("tipo"));
                            String titulo = EncryptionUtil.decrypt(datos.get("titulo"));
                            double cantidad = Double.parseDouble(EncryptionUtil.decrypt(datos.get("cantidad")));
                            LocalDate fecha = LocalDate.parse(EncryptionUtil.decrypt(datos.get("fecha")));
                            boolean esRecurrente = Boolean.parseBoolean(EncryptionUtil.decrypt(datos.get("esRecurrente")));
                            int intervaloDias = esRecurrente ? Integer.parseInt(EncryptionUtil.decrypt(datos.get("intervaloDias"))) : 0;

                            Movimiento movimiento = new Movimiento(categoria, tipo, titulo, cantidad, fecha, esRecurrente, intervaloDias);
                            movimientos.add(movimiento);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movimientos;
    }

    public static void guardarCorreoEnJSON(String correo, String archivoJSON) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return;
        }

        try {
            String correoEncriptado = EncryptionUtil.encrypt(correo);
            Map<String, String> datos = new HashMap<>();
            datos.put("correo", correoEncriptado);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(file, false)) { // `false` para no agregar sino sobreescribir
                gson.toJson(datos, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String leerCorreoDeJSON(String archivoJSON) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return null;
        }

        String correo = null;
        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("correo")) {
                String correoEncriptado = jsonObject.get("correo").getAsString();
                correo = EncryptionUtil.decrypt(correoEncriptado);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al desencriptar el correo: " + e.getMessage());
        }
        return correo;
    }

    public static void guardarCategoriasEnJSON(List<Categoria> categorias, String archivoJSON) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return;
        }

        try {
            List<Map<String, String>> datosEncriptados = new ArrayList<>();
            for (Categoria categoria : categorias) {
                Map<String, String> categoriaEncriptada = new HashMap<>();
                categoriaEncriptada.put("id", EncryptionUtil.encrypt(Integer.toString(categoria.getId())));
                categoriaEncriptada.put("titulo", EncryptionUtil.encrypt(categoria.getTitulo()));
                categoriaEncriptada.put("imagen", EncryptionUtil.encrypt(categoria.getDireccionImagen()));
                datosEncriptados.add(categoriaEncriptada);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(file, false)) { // `false` para no agregar sino sobreescribir
                gson.toJson(datosEncriptados, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Categoria> leerCategoriasDeJSON(String archivoJSON) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return FXCollections.observableArrayList();
        }

        ObservableList<Categoria> categorias = FXCollections.observableArrayList();
        try {
            Gson gson = new Gson();
            Type tipoListaCategoria = new TypeToken<List<Map<String, String>>>() {}.getType();
            try (FileReader reader = new FileReader(file)) {
                List<Map<String, String>> datosEncriptados = gson.fromJson(reader, tipoListaCategoria);

                for (Map<String, String> datos : datosEncriptados) {
                    int id = Integer.parseInt(EncryptionUtil.decrypt(datos.get("id")));
                    String titulo = EncryptionUtil.decrypt(datos.get("titulo"));
                    String imagenURL = EncryptionUtil.decrypt(datos.get("imagen")).replaceAll(".*(icons/.*)", "$1");

                    Image imagen = new Image(ManejadorCategoria.class.getResource(imagenURL).toExternalForm());
                    Categoria categoria = new Categoria(id, titulo, imagen);

                    categorias.add(categoria);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public static String leerTextoDeJSON(String archivoJSON, String titulo) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return "";
        }

        String valor = "";
        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has(titulo)) {
                String montoEncriptado = jsonObject.get(titulo).getAsString();
                valor = EncryptionUtil.decrypt(montoEncriptado);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            return "";
        }
        return valor;
    }

    public static void guardarInversionEnJson(List<Inversion> inversiones, String archivoJSON) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return;
        }

        try {
            List<Map<String, String>> datosEncriptados = new ArrayList<>();
            for (Inversion inversion : inversiones) {
                Map<String, String> inversionEncriptada = new HashMap<>();
                inversionEncriptada.put("montoMeta", EncryptionUtil.encrypt(Double.toString(inversion.getMontoMeta())));
                inversionEncriptada.put("tasaRetorno", EncryptionUtil.encrypt(Double.toString(inversion.getTasaRetorno())));
                inversionEncriptada.put("plazoMeses", EncryptionUtil.encrypt(Integer.toString(inversion.getPlazoMeses())));
                inversionEncriptada.put("valorActual", EncryptionUtil.encrypt(Double.toString(inversion.getValorActual())));

                // Encriptar los abonos mensuales
                List<String> abonosMensualesEncriptados = new ArrayList<>();
                for (Double abonoMensual : inversion.getAbonosMensuales()) {
                    abonosMensualesEncriptados.add(EncryptionUtil.encrypt(Double.toString(abonoMensual)));
                }

                // Convertir la lista de abonos mensuales encriptados a una cadena JSON
                Gson gson = new Gson();
                String abonosMensualesJson = gson.toJson(abonosMensualesEncriptados);

                // Agregar la cadena JSON de abonos mensuales encriptados al mapa de la inversión encriptada
                inversionEncriptada.put("abonosMensuales", abonosMensualesJson);

                datosEncriptados.add(inversionEncriptada);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(file, false)) { // `false` para no agregar sino sobreescribir
                gson.toJson(datosEncriptados, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Inversion> leerInversionesDeJSON(String archivoJSON) {
        File file = new File(archivoJSON);
        if (!file.exists()) {
            System.err.println("El archivo JSON no existe: " + archivoJSON);
            return FXCollections.observableArrayList();
        }

        ObservableList<Inversion> inversiones = FXCollections.observableArrayList();
        try {
            Gson gson = new Gson();
            Type tipoListaInversiones = new TypeToken<List<Map<String, String>>>() {}.getType();
            try (FileReader reader = new FileReader(file)) {
                List<Map<String, String>> datosEncriptados = gson.fromJson(reader, tipoListaInversiones);

                if (datosEncriptados != null){
                    for (Map<String, String> datos : datosEncriptados) {
                        double montoMeta = Double.parseDouble(EncryptionUtil.decrypt(datos.get("montoMeta")));
                        double tasaRetorno = Double.parseDouble(EncryptionUtil.decrypt(datos.get("tasaRetorno")));
                        int plazoMeses = Integer.parseInt(EncryptionUtil.decrypt(datos.get("plazoMeses")));
                        double valorActual = Double.parseDouble(EncryptionUtil.decrypt(datos.get("valorActual")));

                        // Desencriptar los abonos mensuales
                        String abonosMensualesJson = datos.get("abonosMensuales");
                        Type tipoListaAbonosMensuales = new TypeToken<List<String>>() {}.getType();
                        List<String> abonosMensualesEncriptados = gson.fromJson(abonosMensualesJson, tipoListaAbonosMensuales);
                        List<Double> abonosMensuales = new ArrayList<>();
                        for (String abonoMensualEncriptado : abonosMensualesEncriptados) {
                            abonosMensuales.add(Double.parseDouble(EncryptionUtil.decrypt(abonoMensualEncriptado)));
                        }

                        Inversion inversion = new Inversion(montoMeta, tasaRetorno, plazoMeses);
                        for (Double abonoMensual : abonosMensuales) {
                            inversion.agregarAbono(abonoMensual);
                        }

                        inversiones.add(inversion);
                    }
                }else {
                    return inversiones;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inversiones;
    }

}
