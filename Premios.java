public class Premios {

    /**
     * Calcula el premio total ganado según la matriz y la apuesta.
     * Detecta: 3 filas horizontales, diagonal principal y secundaria.
     * Si gana en más de una línea, el premio se multiplica.
     * Retorna 0 si no hay premio.
     */
    public static double calcularPremio(String[][] m, double apuesta) {
        double totalPremio = 0;
        int lineasGanadoras = 0;

        // --- LÍNEAS HORIZONTALES ---
        for (int i = 0; i < 3; i++) {
            if (m[i][0].equals(m[i][1]) && m[i][1].equals(m[i][2])) {
                int mult = Pantalla.obtenerMultiplicador(m[i][0]);
                totalPremio += apuesta * mult;
                lineasGanadoras++;
                System.out.println(Colores.VERDE + Colores.NEGRITA
                        + "  ¡LÍNEA HORIZONTAL " + (i + 1) + "! → "
                        + Pantalla.colorSimbolo(m[i][0]) + m[i][0]
                        + Colores.VERDE + Colores.NEGRITA
                        + "  Premio: $" + String.format("%.2f", apuesta * mult)
                        + Colores.RESET);
            }
        }

        // --- DIAGONAL PRINCIPAL (↘) ---
        if (m[0][0].equals(m[1][1]) && m[1][1].equals(m[2][2])) {
            int mult = Pantalla.obtenerMultiplicador(m[0][0]);
            totalPremio += apuesta * mult;
            lineasGanadoras++;
            System.out.println(Colores.CYAN + Colores.NEGRITA
                    + "  ¡DIAGONAL PRINCIPAL! → "
                    + Pantalla.colorSimbolo(m[0][0]) + m[0][0]
                    + Colores.CYAN + Colores.NEGRITA
                    + "  Premio: $" + String.format("%.2f", apuesta * mult)
                    + Colores.RESET);
        }

        // --- DIAGONAL SECUNDARIA (↙) ---
        if (m[0][2].equals(m[1][1]) && m[1][1].equals(m[2][0])) {
            int mult = Pantalla.obtenerMultiplicador(m[0][2]);
            totalPremio += apuesta * mult;
            lineasGanadoras++;
            System.out.println(Colores.MAGENTA + Colores.NEGRITA
                    + "  ¡DIAGONAL SECUNDARIA! → "
                    + Pantalla.colorSimbolo(m[0][2]) + m[0][2]
                    + Colores.MAGENTA + Colores.NEGRITA
                    + "  Premio: $" + String.format("%.2f", apuesta * mult)
                    + Colores.RESET);
        }

        // --- MULTIPLICADOR: más de una línea ganadora ---
        if (lineasGanadoras > 1) {
            System.out.println(Colores.AMARILLO + Colores.NEGRITA
                    + "  *** MULTIPLICADOR x" + lineasGanadoras + "! Premio total multiplicado. ***"
                    + Colores.RESET);
            totalPremio *= lineasGanadoras;
        }

        return totalPremio;
    }

    /**
     * Mini-juego de dados: el jugador lanza 2 dados.
     * Si el total es > 7, gana el doble de la apuesta.
     */
    public static double minijuegoDados(double apuesta) {
        int dado1 = (int) (Math.random() * 6) + 1;
        int dado2 = (int) (Math.random() * 6) + 1;
        int total = dado1 + dado2;

        System.out.println(Colores.AMARILLO + Colores.NEGRITA);
        System.out.println("  ╔══════════════════════════════╗");
        System.out.println("  ║    *** MINI-JUEGO DE DADOS ***║");
        System.out.println("  ╚══════════════════════════════╝" + Colores.RESET);
        System.out.println(Colores.CYAN + "  Dado 1: [" + dado1 + "]   Dado 2: [" + dado2 + "]" + Colores.RESET);
        System.out.println(Colores.BLANCO + "  Total: " + total + Colores.RESET);

        if (total > 7) {
            double ganancia = apuesta * 2;
            System.out.println(Colores.VERDE + Colores.NEGRITA
                    + "  ¡Mayor que 7! ¡Ganaste $" + String.format("%.2f", ganancia) + "!"
                    + Colores.RESET);
            return ganancia;
        } else {
            System.out.println(Colores.ROJO + "  Menor o igual a 7. Sin premio en los dados." + Colores.RESET);
            return 0;
        }
    }
}
