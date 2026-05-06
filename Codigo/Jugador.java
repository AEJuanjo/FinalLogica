public class Jugador {
    private String nombre;
    private double saldo;

    public Jugador(String nombre, double saldo) {
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public String getNombre() { return nombre; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public void agregarSaldo(double monto) { this.saldo += monto; }
    public void restarSaldo(double monto) { this.saldo -= monto; }

    @Override
    public String toString() {
        return nombre + "," + saldo;
    }
}
