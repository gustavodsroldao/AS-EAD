/**
 * FilaHistorico — fila circular (FIFO) de registros de rodada.
 *
 * Implementada com array circular de capacidade fixa (50 entradas).
 * Quando a fila atinge a capacidade máxima, a entrada mais antiga é
 * descartada automaticamente ao enfileirar uma nova — comportamento
 * de buffer circular, garantindo que o histórico sempre contenha as
 * N rodadas mais recentes sem crescer indefinidamente.
 *
 * Campos 'head' e 'tail' determinam início e fim lógicos na alocação
 * física circular: enqueue avança 'tail'; dequeue implícito avança
 * 'head'. Nenhuma classe de java.util.Queue ou equivalente é utilizada.
 *
 * Uso no jogo: registra rodada, jogador, resultado dos dados e efeito
 * aplicado. Exibido via tecla H durante a partida e no relatório final.
 */
public class FilaHistorico {

    /** Um registro imutável de turno no histórico. */
    public static class Registro {
        public int    rodada;
        public String jogador;
        public String dados;
        public String efeito;

        public Registro(int rodada, String jogador, String dados, String efeito) {
            this.rodada  = rodada;
            this.jogador = jogador;
            this.dados   = dados;
            this.efeito  = efeito;
        }

        @Override
        public String toString() {
            return String.format("Rd %3d | %-14s | Dados: %-8s | %s",
                    rodada, jogador, dados, efeito);
        }
    }

    private Registro[] fila;
    private int        head;       // índice do elemento mais antigo
    private int        tail;       // índice onde o próximo será inserido
    private int        tamanho;
    private int        capacidade;

    public FilaHistorico(int capacidade) {
        this.capacidade = capacidade;
        this.fila       = new Registro[capacidade];
        this.head       = 0;
        this.tail       = 0;
        this.tamanho    = 0;
    }

    /**
     * Enfileira um registro. Se a fila estiver cheia, descarta o mais
     * antigo avançando 'head' — comportamento de buffer circular.
     */
    public void enfileirar(Registro r) {
        if (tamanho == capacidade) {
            // Descarta o registro mais antigo para abrir espaço.
            head    = (head + 1) % capacidade;
            tamanho--;
        }
        fila[tail] = r;
        tail       = (tail + 1) % capacidade;
        tamanho++;
    }

    /** Exibe todos os registros da mais antiga para a mais recente. */
    public void exibir() {
        if (tamanho == 0) {
            System.out.println("  Historico vazio.");
            return;
        }
        System.out.printf("  Historico de rodadas (%d entradas, capacidade maxima: %d):%n",
                tamanho, capacidade);
        int idx = head;
        for (int i = 0; i < tamanho; i++) {
            System.out.println("  " + fila[idx]);
            idx = (idx + 1) % capacidade;
        }
    }

    public int getTamanho()    { return tamanho;    }
    public int getCapacidade() { return capacidade; }
}
