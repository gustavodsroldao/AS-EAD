public class Imovel {
    public String  nome;
    public double  valorCompra;
    public double  aluguelBase;
    public Jogador dono;
    public double  multiplicadorDemanda;
    public double  maiorAluguelCobrado;

    public Imovel(String nome, double valorCompra, double aluguelBase) {
        this.nome                 = nome;
        this.valorCompra          = valorCompra;
        this.aluguelBase          = aluguelBase;
        this.dono                 = null;
        this.multiplicadorDemanda = 1.0;
        this.maiorAluguelCobrado  = 0.0;
    }

    public double calcularAluguel() {
        return aluguelBase * multiplicadorDemanda;
    }

    public void incrementarDemanda() {
        if (multiplicadorDemanda < 2.0) {
            multiplicadorDemanda += 0.1;
            if (multiplicadorDemanda > 2.0) multiplicadorDemanda = 2.0;
        }
    }

    public void registrarAluguelCobrado(double valor) {
        if (valor > maiorAluguelCobrado) maiorAluguelCobrado = valor;
    }

    @Override
    public String toString() {
        String donoNome = dono != null ? dono.nome : "Sem dono";
        return String.format("%-28s | Compra: R$%10.2f | Aluguel: R$%8.2f | Mult: %.1fx | Dono: %s",
                nome, valorCompra, aluguelBase, multiplicadorDemanda, donoNome);
    }
}
