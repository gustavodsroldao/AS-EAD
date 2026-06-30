/**
 * Tabuleiro — lista duplamente ligada circular.
 *
 * Cada Casa possui ponteiros 'proximo' e 'anterior', formando um anel.
 * O último nó aponta para o primeiro (proximo) e o primeiro aponta
 * para o último (anterior), garantindo a circularidade necessária para
 * que jogadores avancem e retrocedam indefinidamente sem tratar bordas.
 *
 * Justificativa de escolha: arrays exigiriam aritmética de módulo em
 * toda movimentação e não permitem inserção eficiente. Lista simples
 * impediria o retrocesso via cartas de VOLTAR_CASAS sem percorrer o
 * tabuleiro inteiro. A lista duplamente ligada circular atende os dois
 * sentidos de travessia em O(passos) e elimina o controle de índice.
 */
public class Tabuleiro {

    private Casa inicio;  // primeiro nó (casa INICIO)
    private Casa fim;     // último nó inserido
    private int  tamanho;

    public Tabuleiro() {
        this.inicio  = null;
        this.fim     = null;
        this.tamanho = 0;
    }

    /**
     * Insere uma nova casa no fim da lista mantendo a circularidade.
     * Após a inserção: fim.proximo == inicio e inicio.anterior == fim.
     */
    public void adicionarNoFim(Casa casa) {
        tamanho++;
        casa.numero = tamanho;

        if (inicio == null) {
            // Primeiro nó: aponta para si mesmo em ambas as direções.
            inicio        = casa;
            fim           = casa;
            casa.proximo  = casa;
            casa.anterior = casa;
        } else {
            // Encaixa o novo nó entre 'fim' e 'inicio', preservando o anel.
            casa.proximo    = inicio;
            casa.anterior   = fim;
            fim.proximo     = casa;
            inicio.anterior = casa;
            fim = casa;
        }
    }

    /**
     * Avança 'passos' casas seguindo os ponteiros 'proximo'.
     * A circularidade garante que ultrapassar o INICIO retorna
     * naturalmente ao começo sem checagem extra.
     */
    public Casa avancar(Casa atual, int passos) {
        Casa pos = atual;
        for (int i = 0; i < passos; i++) {
            pos = pos.proximo;
        }
        return pos;
    }

    /**
     * Retrocede 'passos' casas seguindo os ponteiros 'anterior'.
     * Usado por cartas de VOLTAR_CASAS. Não gera salário ao cruzar
     * o INICIO, pois a direção é contrária ao sentido normal do jogo.
     */
    public Casa retroceder(Casa atual, int passos) {
        Casa pos = atual;
        for (int i = 0; i < passos; i++) {
            pos = pos.anterior;
        }
        return pos;
    }

    /**
     * Exibe todas as casas em ordem, evidenciando a estrutura circular
     * com a nota no final (exigida pelo screenshot S5).
     */
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

    public Casa getInicio()  { return inicio;  }
    public int  getTamanho() { return tamanho; }
}
