/**
 * FilaPrisao — fila (FIFO) de jogadores detidos na prisão.
 *
 * Modela a ordem de espera: o primeiro jogador preso é o primeiro a
 * tentar a saída (princípio FIFO). Capacidade máxima igual ao número
 * máximo de jogadores (6), pois todos podem ser presos simultaneamente.
 *
 * O método posicao() permite exibir ao jogador sua posição na fila
 * de espera assim que é enviado à prisão.
 *
 * Implementação por array circular com ponteiros 'head' e 'tail'.
 * Nenhuma classe de java.util.Queue ou equivalente é utilizada.
 */
public class FilaPrisao {

    private Jogador[] fila;
    private int       head;
    private int       tail;
    private int       tamanho;
    private int       capacidade;

    public FilaPrisao(int capacidade) {
        this.capacidade = capacidade;
        this.fila       = new Jogador[capacidade];
        this.head       = 0;
        this.tail       = 0;
        this.tamanho    = 0;
    }

    public boolean isEmpty()     { return tamanho == 0;          }
    public boolean isFull()      { return tamanho == capacidade; }
    public int     getTamanho()  { return tamanho;               }

    /** Enfileira o jogador se a fila não estiver cheia e ele ainda não estiver nela. */
    public void enfileirar(Jogador j) {
        if (isFull() || contem(j)) return;
        fila[tail] = j;
        tail       = (tail + 1) % capacidade;
        tamanho++;
    }

    /**
     * Remove um jogador específico da fila (saída antecipada por dados
     * duplos, fiança ou liberação automática no 3º turno).
     * Reconstrói o array preservando a ordem dos demais.
     */
    public void remover(Jogador j) {
        if (isEmpty()) return;
        Jogador[] novo     = new Jogador[capacidade];
        int       novoTail = 0;
        int       idx      = head;
        for (int i = 0; i < tamanho; i++) {
            if (fila[idx] != j) novo[novoTail++] = fila[idx];
            idx = (idx + 1) % capacidade;
        }
        fila    = novo;
        head    = 0;
        tail    = novoTail;
        tamanho = novoTail;
    }

    /** Verifica se o jogador já está na fila (evita duplicatas). */
    public boolean contem(Jogador j) {
        int idx = head;
        for (int i = 0; i < tamanho; i++) {
            if (fila[idx] == j) return true;
            idx = (idx + 1) % capacidade;
        }
        return false;
    }

    /** Retorna a posição 1-based do jogador na fila, ou -1 se ausente. */
    public int posicao(Jogador j) {
        int idx = head;
        for (int i = 0; i < tamanho; i++) {
            if (fila[idx] == j) return i + 1;
            idx = (idx + 1) % capacidade;
        }
        return -1;
    }

    /** Exibe todos os presos com sua posição e turnos cumpridos. */
    public void exibir() {
        if (isEmpty()) {
            System.out.println("  Fila da prisao: vazia.");
            return;
        }
        System.out.printf("  Fila da prisao (%d preso(s)):%n", tamanho);
        int idx = head;
        for (int i = 0; i < tamanho; i++) {
            System.out.printf("    [%d] %s (turno(s) preso: %d)%n",
                    i + 1, fila[idx].nome, fila[idx].turnosPrisao);
            idx = (idx + 1) % capacidade;
        }
    }
}
