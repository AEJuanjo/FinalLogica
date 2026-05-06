import java.io.*;
import java.util.*;

public class Persistencia {

    private static final double SALDO_INICIAL = 1000.0;

    // Siempre en el directorio de trabajo actual donde se ejecuta el programa
    private static String getCSV() {
        return System.getProperty("user.dir") + File.separator + "jugadores.csv";
    }

    public static Jugador cargarSaldo(String nombre) {
        File archivo = new File(getCSV());


        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.trim().isEmpty()) continue;
                    String[] partes = linea.split(",");
                    if (partes.length >= 2 && partes[0].trim().equalsIgnoreCase(nombre.trim())) {
                        double saldo = Double.parseDouble(partes[1].trim());
                        System.out.println(Colores.AMARILLO + "  Bienvenido de vuelta, " + nombre + "! Saldo: $" + String.format("%.2f", saldo) + Colores.RESET);
                        return new Jugador(partes[0].trim(), saldo);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println(Colores.ROJO + "  Error leyendo CSV: " + e.getMessage() + Colores.RESET);
            }
        } else {
        }

        Jugador nuevo = new Jugador(nombre, SALDO_INICIAL);
        System.out.println(Colores.VERDE + "  Nuevo jugador! Saldo inicial: $" + String.format("%.2f", SALDO_INICIAL) + Colores.RESET);
        guardarSaldo(nuevo);
        return nuevo;
    }

    public static void guardarSaldo(Jugador jugador) {
        File archivo = new File(getCSV());
        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.trim().isEmpty()) continue;
                    String[] partes = linea.split(",");
                    if (partes.length >= 2 && partes[0].trim().equalsIgnoreCase(jugador.getNombre())) {
                        lineas.add(jugador.getNombre() + "," + String.format("%.2f", jugador.getSaldo()));
                        encontrado = true;
                    } else {
                        lineas.add(linea.trim());
                    }
                }
            } catch (IOException e) {
                System.out.println(Colores.ROJO + "  Error leyendo CSV al guardar: " + e.getMessage() + Colores.RESET);
            }
        }

        if (!encontrado) {
            lineas.add(jugador.getNombre() + "," + String.format("%.2f", jugador.getSaldo()));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(Colores.ROJO + "  Error guardando CSV: " + e.getMessage() + Colores.RESET);
        }
    }

    public static List<Jugador> obtenerTodos() {
        List<Jugador> jugadores = new ArrayList<>();
        File archivo = new File(getCSV());

        if (!archivo.exists()) return jugadores;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    try {
                        jugadores.add(new Jugador(partes[0].trim(), Double.parseDouble(partes[1].trim())));
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.out.println(Colores.ROJO + "  Error leyendo ranking: " + e.getMessage() + Colores.RESET);
        }

        jugadores.sort((a, b) -> Double.compare(b.getSaldo(), a.getSaldo()));
        return jugadores;
    }
}
