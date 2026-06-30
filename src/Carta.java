public class Carta {
    public static final String GANHO_DINHEIRO = "GANHO_DINHEIRO";
    public static final String PERDA_DINHEIRO  = "PERDA_DINHEIRO";
    public static final String AVANCAR_CASAS   = "AVANCAR_CASAS";
    public static final String VOLTAR_CASAS    = "VOLTAR_CASAS";
    public static final String AVANCAR_INICIO  = "AVANCAR_INICIO";
    public static final String COBRAR_OUTROS   = "COBRAR_OUTROS";
    public static final String PAGAR_TODOS     = "PAGAR_TODOS";

    public String tipo;
    public String descricao;
    public double valor;
    public int    casas;

    public Carta(String tipo, String descricao, double valor, int casas) {
        this.tipo      = tipo;
        this.descricao = descricao;
        this.valor     = valor;
        this.casas     = casas;
    }

    @Override
    public String toString() {
        return "[CARTA] " + descricao;
    }
}
