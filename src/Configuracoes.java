public class Configuracoes {
    public double saldoInicial = 15000.0;
    public double salario      = 2000.0;
    public int    maxRodadas   = 50;

    @Override
    public String toString() {
        return String.format(
            "  Saldo inicial : R$ %.2f%n  Salario/volta : R$ %.2f%n  Max. rodadas  : %d",
            saldoInicial, salario, maxRodadas);
    }
}
