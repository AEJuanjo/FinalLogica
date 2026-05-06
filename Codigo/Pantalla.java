public class Pantalla {

    // Símbolos disponibles con su rareza
    public static final String[] SIMBOLOS = {"7", "X", "$", "*", "@", "#"};

    // Premios por símbolo (multiplicadores sobre la apuesta)
    public static int obtenerMultiplicador(String simbolo) {
        return switch (simbolo) {
            case "7" -> 10;
            case "$" -> 5;
            case "X" -> 4;
            case "*" -> 3;
            case "@" -> 2;
            case "#" -> 1;
            default  -> 1;
        };
    }

    // Color por símbolo
    public static String colorSimbolo(String simbolo) {
        return switch (simbolo) {
            case "7" -> Colores.AMARILLO + Colores.NEGRITA;
            case "$" -> Colores.VERDE    + Colores.NEGRITA;
            case "X" -> Colores.ROJO     + Colores.NEGRITA;
            case "*" -> Colores.CYAN     + Colores.NEGRITA;
            case "@" -> Colores.MAGENTA  + Colores.NEGRITA;
            case "#" -> Colores.BLANCO;
            default  -> Colores.RESET;
        };
    }

    /**
     * Llena la matriz 3x3 con símbolos aleatorios.
     * Si easterEggActivo, "7" aparece el doble de veces.
     */
    public static void girar(String[][] rodillos, boolean easterEggActivo) {
        String[] pool = easterEggActivo
                ? new String[]{"7", "7", "X", "$", "$", "*", "@", "#"}
                : SIMBOLOS;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int idx = (int) (Math.random() * pool.length);
                rodillos[i][j] = pool[idx];
            }
        }
    }

    /**
     * Dibuja la pantalla de la máquina con la matriz actual.
     */
    public static void dibujarPantalla(String[][] rodillos) {
        System.out.println(Colores.AMARILLO + "  ╔═══════════════════╗" + Colores.RESET);
        System.out.println(Colores.AMARILLO + "  ║   THE LUCKY ARCADE ║" + Colores.RESET);
        System.out.println(Colores.AMARILLO + "  ╠═══════╦═══════╦═══════╣" + Colores.RESET);

        for (int i = 0; i < 3; i++) {
            System.out.print(Colores.AMARILLO + "  ║" + Colores.RESET);
            for (int j = 0; j < 3; j++) {
                String sim = rodillos[i][j];
                System.out.print("   " + colorSimbolo(sim) + sim + Colores.RESET + "   ");
                System.out.print(Colores.AMARILLO + "║" + Colores.RESET);
            }
            System.out.println();
            if (i < 2)
                System.out.println(Colores.AMARILLO + "  ╠═══════╬═══════╬═══════╣" + Colores.RESET);
        }

        System.out.println(Colores.AMARILLO + "  ╚═══════╩═══════╩═══════╝" + Colores.RESET);
    }

    /**
     * Animación de giro: imprime matrices aleatorias varias veces.
     */
    public static void animarGiro(String[][] rodillos, boolean easterEggActivo) throws InterruptedException {
        int frames = 8;
        for (int f = 0; f < frames; f++) {
            limpiarConsola();
            imprimirLogo();
            girar(rodillos, false); // frames sin easter egg para ver variedad
            dibujarPantalla(rodillos);
            System.out.println();
            Thread.sleep(120);
        }
        // Frame final con estado real
        limpiarConsola();
        imprimirLogo();
        girar(rodillos, easterEggActivo);
        dibujarPantalla(rodillos);
        System.out.println();
    }

    /**
     * Limpia la consola con secuencias ANSI.
     */
    public static void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Imprime el logo ASCII art.
     */
    public static void imprimirLogo() {
        System.out.println(Colores.AMARILLO + Colores.NEGRITA);
        System.out.println("  ██╗     ██╗   ██╗ ██████╗██╗  ██╗██╗   ██╗");
        System.out.println("  ██║     ██║   ██║██╔════╝██║ ██╔╝╚██╗ ██╔╝");
        System.out.println("  ██║     ██║   ██║██║     █████╔╝  ╚████╔╝ ");
        System.out.println("  ██║     ██║   ██║██║     ██╔═██╗   ╚██╔╝  ");
        System.out.println("  ███████╗╚██████╔╝╚██████╗██║  ██╗   ██║   ");
        System.out.println("  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝  ");
        System.out.println("        *** A R C A D E  🎰  C A S I N O ***");
        System.out.println(Colores.RESET);
    }

    /**
     * Tabla de símbolos y premios.
     */
    public static void imprimirTablaSimbolos() {
        System.out.println(Colores.CYAN + "  ┌─────────────────────────────────┐");
        System.out.println("  │       TABLA DE PREMIOS          │");
        System.out.println("  ├────────┬────────────────────────┤");
        System.out.println("  │ Símbolo│ Premio (x apuesta)     │");
        System.out.println("  ├────────┼────────────────────────┤");
        for (String s : SIMBOLOS) {
            System.out.printf("  │  %s%-3s%s   │  x%-21s│%n",
                    colorSimbolo(s), s, Colores.CYAN,
                    obtenerMultiplicador(s) + Colores.CYAN);
        }
        System.out.println("  └────────┴────────────────────────┘" + Colores.RESET);
    }
}
