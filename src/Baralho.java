/**
 * Baralho — pilha (LIFO) implementada manualmente com array.
 *
 * Modelagem LIFO: a carta no topo do baralho (índice tamanho-1) é sempre
 * a próxima a ser sacada, exatamente como um baralho físico. Empilhar
 * corresponde a colocar uma carta no topo; desempilhar a retirar do topo.
 *
 * Reabastecimento automático: quando a pilha esgota (isEmpty() == true),
 * o jogo reconstrói e embaralha o baralho antes da próxima saca,
 * simulando o comportamento de reembaralhar o descarte.
 *
 * Embaralhamento: algoritmo Fisher-Yates implementado manualmente
 * (sem Collections.shuffle), garantindo distribuição uniforme em O(n).
 *
 * Nenhuma classe de java.util.Stack ou java.util.Deque é utilizada.
 */
public class Baralho {

    private Carta[] cartas;
    private int     tamanho;
    private int     capacidade;

    public Baralho(int capacidade) {
        this.cartas     = new Carta[capacidade];
        this.tamanho    = 0;
        this.capacidade = capacidade;
    }

    public boolean isEmpty() { return tamanho == 0;           }
    public boolean isFull()  { return tamanho == capacidade;  }
    public int     tamanho() { return tamanho;                }

    /** Push: insere carta no topo da pilha (índice tamanho). */
    public void empilhar(Carta c) {
        if (!isFull()) {
            cartas[tamanho] = c;
            tamanho++;
        } else {
            System.out.println("Baralho cheio.");
        }
    }

    /** Pop: remove e retorna a carta do topo (LIFO). */
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

    /** Peek: consulta o topo sem remover. */
    public Carta espiar() {
        if (!isEmpty()) return cartas[tamanho - 1];
        return null;
    }

    /** Fisher-Yates — embaralhamento in-place sem Collections.shuffle. */
    public void embaralhar() {
        java.util.Random rand = new java.util.Random();
        for (int i = tamanho - 1; i > 0; i--) {
            int   j    = rand.nextInt(i + 1);
            Carta temp = cartas[i];
            cartas[i]  = cartas[j];
            cartas[j]  = temp;
        }
    }

    /** Esvazia a pilha para permitir remontagem do baralho. */
    public void limpar() {
        for (int i = 0; i < tamanho; i++) cartas[i] = null;
        tamanho = 0;
    }
}
