public class Tabuleiro {
    private Casa inicio;
    private Casa fim;
    private int  tamanho;

    public Tabuleiro() {
        this.inicio  = null;
        this.fim     = null;
        this.tamanho = 0;
    }

    public void adicionarNoFim(Casa casa) {
        tamanho++;
        casa.numero = tamanho;

        if (inicio == null) {
            inicio        = casa;
            fim           = casa;
            casa.proximo  = casa;
            casa.anterior = casa;
        } else {
            casa.proximo    = inicio;
            casa.anterior   = fim;
            fim.proximo     = casa;
            inicio.anterior = casa;
            fim = casa;
        }
    }

    public Casa avancar(Casa atual, int passos) {
        Casa pos = atual;
        for (int i = 0; i < passos; i++) {
            pos = pos.proximo;
        }
        return pos;
    }

    public Casa retroceder(Casa atual, int passos) {
        Casa pos = atual;
        for (int i = 0; i < passos; i++) {
            pos = pos.anterior;
        }
        return pos;
    }

    public void exibir() {
        if (inicio == null) {
            System.out.println("Tabuleiro vazio.");
            return;
        }
        System.out.println("  Total de casas: " + tamanho);
        Casa atual = inicio;
        do {
            System.out.printf("  [%2d] %s%n", atual.numero, atual);
            atual = atual.proximo;
        } while (atual != inicio);
        System.out.println("  ^ Casa " + tamanho + " aponta de volta para Casa 1 [CIRCULAR] ^");
    }

    public Casa getInicio()  { return inicio; }
    public int  getTamanho() { return tamanho; }
}
