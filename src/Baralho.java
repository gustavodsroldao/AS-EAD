public class Baralho {
    private Carta[] cartas;
    private int     tamanho;
    private int     capacidade;

    public Baralho(int capacidade) {
        this.cartas     = new Carta[capacidade];
        this.tamanho    = 0;
        this.capacidade = capacidade;
    }

    public boolean isEmpty() { return tamanho == 0; }
    public boolean isFull()  { return tamanho == capacidade; }
    public int     tamanho() { return tamanho; }

    public void empilhar(Carta c) {
        if (!isFull()) {
            cartas[tamanho] = c;
            tamanho++;
        } else {
            System.out.println("Baralho cheio.");
        }
    }

    public Carta desempilhar() {
        if (!isEmpty()) {
            Carta c = cartas[tamanho - 1];
            cartas[tamanho - 1] = null;
            tamanho--;
            return c;
        }
        System.out.println("Baralho vazio.");
        return null;
    }

    public Carta espiar() {
        if (!isEmpty()) return cartas[tamanho - 1];
        return null;
    }

    // Fisher-Yates manual — sem Collections.shuffle
    public void embaralhar() {
        java.util.Random rand = new java.util.Random();
        for (int i = tamanho - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Carta temp = cartas[i];
            cartas[i]  = cartas[j];
            cartas[j]  = temp;
        }
    }

    public void limpar() {
        for (int i = 0; i < tamanho; i++) cartas[i] = null;
        tamanho = 0;
    }
}
