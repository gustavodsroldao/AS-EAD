/**
 * Configuracoes — parâmetros globais da partida.
 *
 * Valores padrão equilibrados para partidas de 3–6 jogadores
 * com 12 imóveis temáticos (Espaço e Ficção Científica).
 */
public class Configuracoes {
    public double saldoInicial = 15000.0; // capital inicial de cada jogador
    public double salario      =  2000.0; // recebido ao completar uma volta pelo INICIO
    public int    maxRodadas       =    50;   // limite de rodadas antes do encerramento forçado
    public double fianca           =  1000.0; // custo para sair da prisão (ADVOGADO: isento)
    public int    historicoTamanho =    10;   // capacidade da fila de histórico

    @Override
    public String toString() {
        return String.format(
            "  Saldo inicial : R$ %.2f%n" +
            "  Salario/volta : R$ %.2f%n" +
            "  Max. rodadas  : %d%n" +
            "  Fianca prisao : R$ %.2f%n" +
            "  Tam. Historico: %d",
            saldoInicial, salario, maxRodadas, fianca, historicoTamanho);
    }
}
