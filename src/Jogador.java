public class Jogador {
    public String   nome;
    public double   saldo;
    public Casa     posicaoAtual;
    public Imovel[] imoveis;
    public int      qtdImoveis;
    public String   personagem; // ESPECULADOR, NEGOCIANTE, ADVOGADO, CONSTRUTOR
    public int      voltasCompletas;
    public boolean  ativo;

    public Jogador(String nome, double saldo, String personagem) {
        this.nome            = nome;
        this.saldo           = saldo;
        this.personagem      = personagem;
        this.imoveis         = new Imovel[40];
        this.qtdImoveis      = 0;
        this.voltasCompletas = 0;
        this.ativo           = true;
    }

    public double calcularPatrimonio() {
        double total = saldo;
        for (int i = 0; i < qtdImoveis; i++) {
            total += imoveis[i].valorCompra;
        }
        return total;
    }

    public void adicionarImovel(Imovel imovel) {
        if (qtdImoveis < 40) {
            imoveis[qtdImoveis] = imovel;
            qtdImoveis++;
        }
    }

    public void removerImovel(Imovel imovel) {
        for (int i = 0; i < qtdImoveis; i++) {
            if (imoveis[i] == imovel) {
                for (int j = i; j < qtdImoveis - 1; j++) {
                    imoveis[j] = imoveis[j + 1];
                }
                imoveis[qtdImoveis - 1] = null;
                qtdImoveis--;
                return;
            }
        }
    }

    public boolean temImoveis() {
        return qtdImoveis > 0;
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-12s | Saldo: R$%10.2f | Imoveis: %d | Voltas: %d | %s",
                nome, personagem, saldo, qtdImoveis, voltasCompletas,
                ativo ? "Ativo" : "FALIDO");
    }
}
