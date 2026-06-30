import java.util.Scanner;

public class Main {

    private static Jogador[]     jogadores    = new Jogador[6];
    private static int           qtdJogadores = 0;
    private static Imovel[]      imoveis      = new Imovel[40];
    private static int           qtdImoveis   = 0;
    private static Configuracoes config       = new Configuracoes();
    private static Scanner       sc           = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n=======================================================");
            System.out.println("      JOGO DE TABULEIRO ESTRATEGICO");
            System.out.println("=======================================================");
            System.out.println("  [1] Gerenciar Jogadores");
            System.out.println("  [2] Gerenciar Imoveis");
            System.out.println("  [3] Configuracoes da Partida");
            System.out.println("  [4] Carregar dados de exemplo");
            System.out.println("  [5] Exibir Tabuleiro (pre-visualizacao)");
            System.out.println("  [6] Iniciar Partida");
            System.out.println("  [0] Sair");
            System.out.print("Opcao: ");
            opcao = lerInt();

            switch (opcao) {
                case 1: menuJogadores();         break;
                case 2: menuImoveis();           break;
                case 3: menuConfig();            break;
                case 4: carregarDadosExemplo();  break;
                case 5: exibirTabuleiroPreview(); break;
                case 6: iniciarPartida();        break;
                case 0: System.out.println("Encerrando. Ate logo!"); break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void menuJogadores() {
        int opcao;
        do {
            System.out.println("\n-----------------------------------------------");
            System.out.println("  GERENCIAR JOGADORES (atual: " + qtdJogadores + "/6)");
            System.out.println("-----------------------------------------------");
            System.out.println("  [1] Cadastrar jogador");
            System.out.println("  [2] Listar jogadores");
            System.out.println("  [3] Editar jogador");
            System.out.println("  [4] Remover jogador");
            System.out.println("  [0] Voltar");
            System.out.print("Opcao: ");
            opcao = lerInt();

            switch (opcao) {
                case 1: cadastrarJogador(); break;
                case 2: listarJogadores();  break;
                case 3: editarJogador();    break;
                case 4: removerJogador();   break;
                case 0: break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void cadastrarJogador() {
        if (qtdJogadores >= 6) {
            System.out.println("Limite de 6 jogadores atingido.");
            return;
        }
        System.out.print("Nome do jogador: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) { System.out.println("Nome invalido."); return; }

        String personagem = escolherPersonagem();
        jogadores[qtdJogadores] = new Jogador(nome, 0, personagem);
        qtdJogadores++;
        System.out.printf("Jogador '%s' (%s) cadastrado!%n", nome, personagem);
    }

    private static String escolherPersonagem() {
        System.out.println("Escolha o personagem:");
        System.out.println("  [1] ESPECULADOR - +20% no salario, +10% de imposto");
        System.out.println("  [2] NEGOCIANTE  - Paga 10% menos de aluguel");
        System.out.println("  [3] ADVOGADO    - Isento de fianca na prisao");
        System.out.println("  [4] CONSTRUTOR  - Imoveis comprados: aluguel +15%");
        System.out.print("Opcao: ");
        int op = lerInt();
        switch (op) {
            case 1: return "ESPECULADOR";
            case 2: return "NEGOCIANTE";
            case 3: return "ADVOGADO";
            case 4: return "CONSTRUTOR";
            default:
                System.out.println("Opcao invalida. Usando NEGOCIANTE.");
                return "NEGOCIANTE";
        }
    }

    private static void listarJogadores() {
        System.out.println("\n--- Jogadores cadastrados ---");
        if (qtdJogadores == 0) { System.out.println("  Nenhum jogador."); return; }
        for (int i = 0; i < qtdJogadores; i++) {
            System.out.printf("  [%d] %s%n", i + 1, jogadores[i]);
        }
    }

    private static void editarJogador() {
        listarJogadores();
        if (qtdJogadores == 0) return;
        System.out.print("Numero do jogador a editar: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= qtdJogadores) { System.out.println("Indice invalido."); return; }

        System.out.print("Novo nome (ENTER para manter '" + jogadores[idx].nome + "'): ");
        String nome = sc.nextLine().trim();
        if (!nome.isEmpty()) jogadores[idx].nome = nome;

        System.out.print("Alterar personagem? (S/N): ");
        if ("S".equalsIgnoreCase(sc.nextLine().trim())) {
            jogadores[idx].personagem = escolherPersonagem();
        }
        System.out.println("Jogador atualizado: " + jogadores[idx]);
    }

    private static void removerJogador() {
        listarJogadores();
        if (qtdJogadores == 0) return;
        System.out.print("Numero do jogador a remover: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= qtdJogadores) { System.out.println("Indice invalido."); return; }

        String nome = jogadores[idx].nome;
        for (int i = idx; i < qtdJogadores - 1; i++) jogadores[i] = jogadores[i + 1];
        jogadores[qtdJogadores - 1] = null;
        qtdJogadores--;
        System.out.println("Jogador '" + nome + "' removido.");
    }

    private static void menuImoveis() {
        int opcao;
        do {
            System.out.println("\n-----------------------------------------------");
            System.out.println("  GERENCIAR IMOVEIS (atual: " + qtdImoveis + "/40)");
            System.out.println("-----------------------------------------------");
            System.out.println("  [1] Cadastrar imovel");
            System.out.println("  [2] Listar imoveis");
            System.out.println("  [3] Editar imovel");
            System.out.println("  [4] Remover imovel");
            System.out.println("  [0] Voltar");
            System.out.print("Opcao: ");
            opcao = lerInt();

            switch (opcao) {
                case 1: cadastrarImovel(); break;
                case 2: listarImoveis();   break;
                case 3: editarImovel();    break;
                case 4: removerImovel();   break;
                case 0: break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void cadastrarImovel() {
        if (qtdImoveis >= 40) { System.out.println("Limite de 40 imoveis atingido."); return; }

        System.out.print("Nome do imovel: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) { System.out.println("Nome invalido."); return; }

        System.out.print("Valor de compra (R$): ");
        double compra = lerDouble();

        System.out.print("Aluguel base (R$): ");
        double aluguel = lerDouble();

        imoveis[qtdImoveis] = new Imovel(nome, compra, aluguel);
        qtdImoveis++;
        System.out.printf("Imovel '%s' cadastrado!%n", nome);
    }

    private static void listarImoveis() {
        System.out.println("\n--- Imoveis cadastrados ---");
        if (qtdImoveis == 0) { System.out.println("  Nenhum imovel."); return; }
        for (int i = 0; i < qtdImoveis; i++) {
            System.out.printf("  [%2d] %s%n", i + 1, imoveis[i]);
        }
    }

    private static void editarImovel() {
        listarImoveis();
        if (qtdImoveis == 0) return;
        System.out.print("Numero do imovel a editar: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= qtdImoveis) { System.out.println("Indice invalido."); return; }

        System.out.print("Novo nome (ENTER para manter): ");
        String nome = sc.nextLine().trim();
        if (!nome.isEmpty()) imoveis[idx].nome = nome;

        System.out.print("Novo valor de compra (0 para manter): ");
        double compra = lerDouble();
        if (compra > 0) imoveis[idx].valorCompra = compra;

        System.out.print("Novo aluguel base (0 para manter): ");
        double aluguel = lerDouble();
        if (aluguel > 0) imoveis[idx].aluguelBase = aluguel;

        System.out.println("Imovel atualizado: " + imoveis[idx]);
    }

    private static void removerImovel() {
        listarImoveis();
        if (qtdImoveis == 0) return;
        System.out.print("Numero do imovel a remover: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= qtdImoveis) { System.out.println("Indice invalido."); return; }

        String nome = imoveis[idx].nome;
        for (int i = idx; i < qtdImoveis - 1; i++) imoveis[i] = imoveis[i + 1];
        imoveis[qtdImoveis - 1] = null;
        qtdImoveis--;
        System.out.println("Imovel '" + nome + "' removido.");
    }

    private static void menuConfig() {
        int opcao;
        do {
            System.out.println("\n--- Configuracoes da Partida ---");
            System.out.println(config);
            System.out.println("  [1] Alterar saldo inicial");
            System.out.println("  [2] Alterar salario por volta");
            System.out.println("  [3] Alterar numero maximo de rodadas");
            System.out.println("  [0] Voltar");
            System.out.print("Opcao: ");
            opcao = lerInt();

            switch (opcao) {
                case 1:
                    System.out.print("Novo saldo inicial (R$): ");
                    config.saldoInicial = lerDouble();
                    break;
                case 2:
                    System.out.print("Novo salario (R$): ");
                    config.salario = lerDouble();
                    break;
                case 3:
                    System.out.print("Novo maximo de rodadas: ");
                    config.maxRodadas = lerInt();
                    break;
                case 0: break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void carregarDadosExemplo() {
        System.out.println("Carregando dados de exemplo...");

        if (qtdJogadores == 0) {
            jogadores[0] = new Jogador("Alice", 0, "ESPECULADOR");
            jogadores[1] = new Jogador("Bruno", 0, "CONSTRUTOR");
            jogadores[2] = new Jogador("Carol", 0, "NEGOCIANTE");
            qtdJogadores = 3;
            System.out.println("  3 jogadores cadastrados.");
        } else {
            System.out.println("  Jogadores mantidos (ja existem " + qtdJogadores + ").");
        }

        if (qtdImoveis == 0) {
            imoveis[0]  = new Imovel("Modulo Lunar Alfa",           200000,  10000);
            imoveis[1]  = new Imovel("Estacao Orbital Beta",         420000,  21000);
            imoveis[2]  = new Imovel("Colonia de Marte",             500000,  25000);
            imoveis[3]  = new Imovel("Domo de Tita",                 350000,  17500);
            imoveis[4]  = new Imovel("Nave-Mae Andromeda",           750000,  37500);
            imoveis[5]  = new Imovel("Bunker Subterraneo",           230000,  11500);
            imoveis[6]  = new Imovel("Plataforma de Europa",         600000,  30000);
            imoveis[7]  = new Imovel("Cupula de Kepler-22b",         870000,  43500);
            imoveis[8]  = new Imovel("Laboratorio do Asteroid Belt", 460000,  23000);
            imoveis[9]  = new Imovel("Refugio de Plutao",            210000,  10500);
            imoveis[10] = new Imovel("Torre de Observacao Solar",   1000000,  50000);
            imoveis[11] = new Imovel("Base Antartica Omega",         290000,  14500);
            qtdImoveis  = 12;
            System.out.println("  12 imoveis cadastrados (Anexo C - Espaco e Ficcao Cientifica).");
        } else {
            System.out.println("  Imoveis mantidos (ja existem " + qtdImoveis + ").");
        }

        System.out.println("Pronto!");
    }

    private static void exibirTabuleiroPreview() {
        if (qtdImoveis < 10) {
            System.out.println("Cadastre ao menos 10 imoveis para pre-visualizar o tabuleiro.");
            return;
        }
        Tabuleiro t = new Tabuleiro();
        String[] especiais = {
            "SORTE_REVES", "IMPOSTO", "SORTE_REVES",
            "RESTITUICAO", "SORTE_REVES", "IMPOSTO",
            "RESTITUICAO", "SORTE_REVES", "IMPOSTO"
        };
        t.adicionarNoFim(new Casa("INICIO"));
        int espIdx = 0, imvIdx = 0;
        int propPorEsp = Math.max(1, qtdImoveis / especiais.length);
        while (imvIdx < qtdImoveis) {
            for (int k = 0; k < propPorEsp && imvIdx < qtdImoveis; k++, imvIdx++) {
                t.adicionarNoFim(new Casa("IMOVEL", imoveis[imvIdx]));
            }
            if (espIdx < especiais.length) {
                t.adicionarNoFim(new Casa(especiais[espIdx]));
                espIdx++;
            }
        }
        while (espIdx < especiais.length) {
            t.adicionarNoFim(new Casa(especiais[espIdx]));
            espIdx++;
        }
        System.out.println("\nPre-visualizacao do Tabuleiro:");
        t.exibir();
    }

    private static void iniciarPartida() {
        if (qtdJogadores < 2) {
            System.out.println("Minimo de 2 jogadores necessario (atual: " + qtdJogadores + ").");
            return;
        }
        if (qtdImoveis < 10) {
            System.out.println("Minimo de 10 imoveis necessario (atual: " + qtdImoveis + ").");
            return;
        }

        for (int i = 0; i < qtdImoveis; i++) {
            imoveis[i].dono = null;
        }

        System.out.println("\nConfiguracoes da partida:");
        System.out.println(config);
        System.out.print("\nConfirmar inicio? (S/N): ");
        if (!"S".equalsIgnoreCase(sc.nextLine().trim())) {
            System.out.println("Partida cancelada.");
            return;
        }

        Jogo jogo = new Jogo(jogadores, qtdJogadores, imoveis, qtdImoveis, config, sc);
        jogo.iniciar();
    }

    private static int lerInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Digite um numero inteiro: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Digite um numero: ");
            }
        }
    }
}
