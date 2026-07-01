/**
 * Imovel — representa uma propriedade do tabuleiro.
 *
 * O aluguel efetivo é calculado como aluguelBase * multiplicadorDemanda.
 * O multiplicador sobe 0,1x toda vez que a propriedade é visitada sem
 * ser comprada ou quando aluguel é cobrado, até o máximo de 2,0x —
 * modelando valorização por demanda de mercado.
 *
 * 'dono' é null enquanto a propriedade pertence ao banco.
 * 'maiorAluguelCobrado' é usado no relatório final para estatísticas.
 */
public class Imovel {
    public String nome;
    public double valorCompra;
    public double aluguelBase;
    public Jogador dono;
    public double multiplicadorDemanda;
    public double maiorAluguelCobrado;

    public Imovel(String nome, double valorCompra, double aluguelBase) {
        this.nome = nome;
        this.valorCompra = valorCompra;
        this.aluguelBase = aluguelBase;
        this.dono = null;
        this.multiplicadorDemanda = 1.0;
        this.maiorAluguelCobrado = 0.0;
    }

    /** Aluguel real = base × multiplicador de demanda. */
    public double calcularAluguel() {
        return aluguelBase * multiplicadorDemanda;
    }

    /** Sobe o multiplicador em 0,1 a cada visita, até o teto de 2,0. */
    public void incrementarDemanda() {
        if (multiplicadorDemanda < 2.0) {
            multiplicadorDemanda += 0.1;
            if (multiplicadorDemanda > 2.0)
                multiplicadorDemanda = 2.0;
        }
    }

    public void registrarAluguelCobrado(double valor) {
        if (valor > maiorAluguelCobrado)
            maiorAluguelCobrado = valor;
    }

    @Override
    public String toString() {
        String donoNome = dono != null ? dono.nome : "Sem dono";
        return String.format("%-28s | Compra: R$%10.2f | Aluguel: R$%8.2f | Mult: %.1fx | Dono: %s",
                nome, valorCompra, aluguelBase, multiplicadorDemanda, donoNome);
    }
}
