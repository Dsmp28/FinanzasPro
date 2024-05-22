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

}
