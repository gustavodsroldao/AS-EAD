/**
 * Casa — nó da lista duplamente ligada circular (Tabuleiro).
 *
 * Cada casa possui ponteiros para o nó seguinte ('proximo') e anterior
 * ('anterior'), permitindo travessia nos dois sentidos. O campo 'tipo'
 * determina o efeito ao parar na casa. Quando tipo == "IMOVEL", o campo
 * 'imovel' referencia a propriedade associada; nos demais casos é null.
 *
 * Tipos disponíveis: INICIO, IMOVEL, IMPOSTO, RESTITUICAO,
 * SORTE_REVES, PRISAO, IR_PRISAO, LEILAO.
 */
public class Casa {
    Casa   anterior;
    Casa   proximo;
    String tipo;   // INICIO, IMOVEL, IMPOSTO, RESTITUICAO, SORTE_REVES, PRISAO, IR_PRISAO, LEILAO
    Imovel imovel; // preenchido apenas quando tipo == "IMOVEL"
    int    numero;

    public Casa(String tipo) {
        this.anterior = null;
        this.proximo  = null;
        this.tipo     = tipo;
        this.imovel   = null;
        this.numero   = 0;
    }

    public Casa(String tipo, Imovel imovel) {
        this(tipo);
        this.imovel = imovel;
    }

    @Override
    public String toString() {
        if ("IMOVEL".equals(tipo) && imovel != null) {
            return "IMOVEL [" + imovel.nome + "]";
        }
        return tipo;
    }
}
