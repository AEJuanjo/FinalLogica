import java.util.*;

public class LuckyArcade {

    private static final Scanner sc = new Scanner(System.in);
    private static Jugador jugador;
    private static String[][] rodillos = new String[3][3];

    // Easter Egg
    private static boolean easterEggActivo = false;
    private static int turnosEasterEgg = 0;
    private static boolean minijuegoRealizado = false;

    public static void main(String[] args) {
        Pantalla.limpiarConsola();
        Pantalla.imprimirLogo();

        // PASO 1: Persistencia — cargar jugador
        jugador = iniciarSesion();

        // Bucle principal del menú
        boolean jugando = true;
        while (jugando) {
            mostrarMenu();
            int opcion = leerEnteroSeguro("  → Elige una opción: ", 1, 4);

            switch (opcion) {
                case 1 -> apostar();
                case 2 -> verSaldo();
                case 3 -> verRanking();
                case 4 -> {
                    jugando = false;
                    salir();
                }
            }
        }
    }

    // ─────────────────────────────────────────
    //  INICIO DE SESIÓN
    // ─────────────────────────────────────────
    private static Jugador iniciarSesion() {
        System.out.println(Colores.CYAN + "  ╔══════════════════════════════╗");
        System.out.println("  ║     BIENVENIDO AL CASINO     ║");
        System.out.println("  ╚══════════════════════════════╝" + Colores.RESET);
        System.out.print(Colores.BLANCO + "  Ingresa tu nombre de jugador: " + Colores.RESET);
        String nombre = sc.nextLine().trim();

        if (nombre.isEmpty()) nombre = "Anonimo";

        Jugador j = Persistencia.cargarSaldo(nombre);

        // Easter Egg: nombre LUCKY
        if (nombre.equalsIgnoreCase("LUCKY")) {
            activarEasterEgg(j, 0);
        }

        return j;
    }

    // ─────────────────────────────────────────
    //  MENÚ PRINCIPAL
    // ─────────────────────────────────────────
    private static void mostrarMenu() {
        System.out.println();
        System.out.println(Colores.AMARILLO + "  ┌──────────────────────────────┐");
        System.out.println("  │          MENÚ PRINCIPAL      │");
        System.out.println("  ├──────────────────────────────┤");
        System.out.printf("  │  Jugador: %-19s│%n", jugador.getNombre());
        System.out.printf("  │  Saldo:   $%-18s│%n", String.format("%.2f", jugador.getSaldo()));
        if (easterEggActivo && turnosEasterEgg > 0)
            System.out.printf("  │  " + Colores.VERDE + "🍀 AMULETO ACTIVO (%d turnos)   " + Colores.AMARILLO + "│%n", turnosEasterEgg);
        System.out.println("  ├──────────────────────────────┤");
        System.out.println("  │  1. Apostar                  │");
        System.out.println("  │  2. Ver Saldo                │");
        System.out.println("  │  3. Ranking                  │");
        System.out.println("  │  4. Salir                    │");
        System.out.println("  └──────────────────────────────┘" + Colores.RESET);
    }

    // ─────────────────────────────────────────
    //  APOSTAR
    // ─────────────────────────────────────────
    private static void apostar() {
        if (jugador.getSaldo() <= 0) {
            System.out.println(Colores.ROJO + "  ¡Sin saldo! No puedes apostar." + Colores.RESET);
            return;
        }

        Pantalla.imprimirTablaSimbolos();

        double maxApuesta = jugador.getSaldo();
        System.out.printf("%n  Tu saldo actual: $%.2f%n", jugador.getSaldo());
        System.out.print(Colores.BLANCO + "  ¿Cuánto deseas apostar? $" + Colores.RESET);

        double apuesta = leerDoubleSeguro();

        if (apuesta <= 0 || apuesta > maxApuesta) {
            System.out.println(Colores.ROJO + "  Apuesta inválida." + Colores.RESET);
            return;
        }

        // Easter Egg: apuesta 777
        if (apuesta == 777 && !minijuegoRealizado) {
            activarEasterEgg(jugador, apuesta);
            return;
        }

        // Descontar apuesta
        jugador.restarSaldo(apuesta);
        Persistencia.guardarSaldo(jugador);

        // Animación de giro
        try {
            Pantalla.animarGiro(rodillos, easterEggActivo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Calcular premio
        System.out.println(Colores.BLANCO + "  ── Resultado ──────────────────" + Colores.RESET);
        double premio = Premios.calcularPremio(rodillos, apuesta);

        if (premio > 0) {
            jugador.agregarSaldo(premio);
            System.out.println(Colores.VERDE + Colores.NEGRITA
                    + "  >> Premio total ganado: $" + String.format("%.2f", premio)
                    + Colores.RESET);
        } else {
            System.out.println(Colores.ROJO + "  >> Sin premio esta vez. ¡Suerte la proxima!" + Colores.RESET);
        }

        // Actualizar saldo
        System.out.printf(Colores.CYAN + "  Saldo actualizado: $%.2f%n" + Colores.RESET, jugador.getSaldo());
        Persistencia.guardarSaldo(jugador);

        // Descontar turno easter egg
        if (easterEggActivo && turnosEasterEgg > 0) {
            turnosEasterEgg--;
            if (turnosEasterEgg == 0) {
                easterEggActivo = false;
                System.out.println(Colores.AMARILLO + "  El amuleto se ha agotado." + Colores.RESET);
            }
        }

        pausar();
    }

    // ─────────────────────────────────────────
    //  VER SALDO
    // ─────────────────────────────────────────
    private static void verSaldo() {
        System.out.println();
        System.out.println(Colores.CYAN + "  ╔══════════════════════════════╗");
        System.out.printf("  ║  Jugador: %-19s║%n", jugador.getNombre());
        System.out.printf("  ║  Saldo:   $%-18s║%n", String.format("%.2f", jugador.getSaldo()));
        System.out.println("  ╚══════════════════════════════╝" + Colores.RESET);
        pausar();
    }

    // ─────────────────────────────────────────
    //  RANKING
    // ─────────────────────────────────────────
    private static void verRanking() {
        List<Jugador> todos = Persistencia.obtenerTodos();
        System.out.println();
        System.out.println(Colores.AMARILLO + "  ╔════╦═══════════════════╦════════════╗");
        System.out.println("  ║ #  ║ Jugador            ║   Saldo    ║");
        System.out.println("  ╠════╬═══════════════════╬════════════╣" + Colores.RESET);

        if (todos.isEmpty()) {
            System.out.println(Colores.BLANCO + "  ║          Sin jugadores registrados         ║" + Colores.RESET);
        } else {
            int pos = 1;
            for (Jugador j : todos) {
                String lugar = switch (pos) {
                    case 1 -> " 1°";
                    case 2 -> " 2°";
                    case 3 -> " 3°";
                    default -> " " + pos + "°";
                };
                String nombre = j.getNombre();
                if (nombre.length() > 18) nombre = nombre.substring(0, 18);
                String saldo = String.format("$%.2f", j.getSaldo());

                System.out.printf(Colores.BLANCO + "  ║%-4s║ %-18s ║ %-10s║%n" + Colores.RESET,
                        lugar, nombre, saldo);
                pos++;
            }
        }

        System.out.println(Colores.AMARILLO + "  ╚════╩═══════════════════╩════════════╝" + Colores.RESET);
        pausar();
    }

    // ─────────────────────────────────────────
    //  SALIR
    // ─────────────────────────────────────────
    private static void salir() {
        Persistencia.guardarSaldo(jugador);
        Pantalla.limpiarConsola();
        System.out.println(Colores.AMARILLO + Colores.NEGRITA);
        System.out.println("  ╔══════════════════════════════════╗");
        System.out.println("  ║   ¡Gracias por jugar, " + jugador.getNombre() + "!");
        System.out.printf("  ║   Saldo final: $%-17s║%n", String.format("%.2f", jugador.getSaldo()));
        System.out.println("  ║   ¡Hasta la proxima!              ║");
        System.out.println("  ╚══════════════════════════════════╝");
        System.out.println(Colores.RESET);
        sc.close();
    }

    // ─────────────────────────────────────────
    //  EASTER EGG — EL AMULETO
    // ─────────────────────────────────────────
    private static void activarEasterEgg(Jugador j, double apuesta) {
        System.out.println(Colores.AMARILLO + Colores.NEGRITA);
        System.out.println("  ╔══════════════════════════════════╗");
        System.out.println("  ║   *** EASTER EGG ACTIVADO! ***   ║");
        System.out.println("  ║      EL AMULETO DE LA SUERTE     ║");
        System.out.println("  ╚══════════════════════════════════╝" + Colores.RESET);

        if (apuesta == 777) {
            // Mini-juego de dados
            System.out.println(Colores.CYAN + "  ¡Apuesta mágica 777! Activando mini-juego de dados..." + Colores.RESET);
            pausar();
            double ganancia = Premios.minijuegoDados(apuesta);
            j.restarSaldo(apuesta);
            j.agregarSaldo(ganancia);
            Persistencia.guardarSaldo(j);
            minijuegoRealizado = true;
        } else {
            // Nombre LUCKY: doblar probabilidades por 3 turnos
            System.out.println(Colores.VERDE + "  ¡Eres LUCKY! Las probabilidades se duplican por 3 turnos." + Colores.RESET);
            easterEggActivo = true;
            turnosEasterEgg = 3;
        }
        pausar();
    }

    // ─────────────────────────────────────────
    //  UTILIDADES
    // ─────────────────────────────────────────
    private static int leerEnteroSeguro(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println(Colores.ROJO + "  Opción fuera de rango (" + min + "-" + max + ")." + Colores.RESET);
            } catch (NumberFormatException e) {
                System.out.println(Colores.ROJO + "  Ingresa un número válido." + Colores.RESET);
            }
        }
    }

    private static double leerDoubleSeguro() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(Colores.ROJO + "  Número inválido. Intenta de nuevo: $" + Colores.RESET);
            }
        }
    }

    private static void pausar() {
        System.out.print(Colores.BLANCO + "\n  [Presiona ENTER para continuar...]" + Colores.RESET);
        sc.nextLine();
        Pantalla.limpiarConsola();
        Pantalla.imprimirLogo();
    }
}
