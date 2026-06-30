public class Casa {
    Casa anterior;
    Casa proximo;
    String tipo;   // INICIO, IMOVEL, IMPOSTO, RESTITUICAO, SORTE_REVES
    Imovel imovel; // preenchido apenas quando tipo == "IMOVEL"
    int numero;

    public Casa(String tipo) {
        this.anterior = null;
        this.proximo = null;
        this.tipo = tipo;
        this.imovel = null;
        this.numero = 0;
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
