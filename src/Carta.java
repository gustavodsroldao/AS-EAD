/**
 * Carta — representa uma carta do baralho de Sorte/Revés.
 *
 * Cada carta possui um 'tipo' que determina o efeito aplicado ao jogador
 * quando sacada do topo da pilha (Baralho). Os campos 'valor' e 'casas'
 * carregam os parâmetros do efeito (ex: quantia em R$ ou casas a mover).
 *
 * Tipos disponíveis:
 * GANHO_DINHEIRO — jogador recebe 'valor' do banco
 * PERDA_DINHEIRO — jogador paga 'valor' ao banco
 * AVANCAR_CASAS — jogador avança 'casas' posições
 * VOLTAR_CASAS — jogador retrocede 'casas' (sem salário ao cruzar INICIO)
 * AVANCAR_INICIO — jogador vai direto ao INICIO e recebe salário
 * COBRAR_OUTROS — todos os outros jogadores pagam 'valor' ao sacador
 * PAGAR_TODOS — sacador paga 'valor' a cada outro jogador
 * IR_PRISAO — sacador é enviado imediatamente à prisão
 */
public class Carta {
    public static final String GANHO_DINHEIRO = "GANHO_DINHEIRO";
    public static final String PERDA_DINHEIRO = "PERDA_DINHEIRO";
    public static final String AVANCAR_CASAS = "AVANCAR_CASAS";
    public static final String VOLTAR_CASAS = "VOLTAR_CASAS";
    public static final String AVANCAR_INICIO = "AVANCAR_INICIO";
    public static final String COBRAR_OUTROS = "COBRAR_OUTROS";
    public static final String PAGAR_TODOS = "PAGAR_TODOS";
    public static final String IR_PRISAO = "IR_PRISAO";

    public String tipo;
    public String descricao;
    public double valor;
    public int casas;

    public Carta(String tipo, String descricao, double valor, int casas) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.casas = casas;
    }

    @Override
    public String toString() {
        return "[CARTA] " + descricao;
    }
}
