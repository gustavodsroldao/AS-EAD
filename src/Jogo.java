import java.util.Random;
import java.util.Scanner;

/**
 * Jogo — controlador principal da partida.
 *
 * Coordena o ciclo de rodadas, a movimentação de jogadores no Tabuleiro
 * (lista duplamente ligada circular), o saque de cartas do Baralho
 * (pilha LIFO), o registro do histórico (FilaHistorico — fila FIFO) e
 * a gestão da prisão (FilaPrisao — fila FIFO).
 *
 * Fluxo de um turno normal:
 *   1. Verifica se o jogador está preso → processarTurnoPrisao()
 *   2. Lança dois dados e avança no Tabuleiro
 *   3. Detecta passagens pelo INICIO e paga salário (sem pagar em retrocessos)
 *   4. Aplica o efeito da casa de destino
 *   5. Verifica falência
 *   6. Registra o turno no histórico
 */
public class Jogo {
    private Tabuleiro tabuleiro;
    private Baralho baralho;
    private Jogador[] jogadores;
    private int qtdJogadores;
    private Imovel[] imoveis;
    private int qtdImoveis;
    private Configuracoes config;
    private int rodadaAtual;
    private Scanner sc;
    private Random rand;
    private FilaHistorico historico;
    private FilaPrisao filaPrisao;
    private Casa casaPrisao;
    private String efeitoTurno;

    public Jogo(Jogador[] jogadores, int qtdJogadores,
            Imovel[] imoveis, int qtdImoveis,
            Configuracoes config, Scanner sc) {
        this.jogadores = jogadores;
        this.qtdJogadores = qtdJogadores;
        this.imoveis = imoveis;
        this.qtdImoveis = qtdImoveis;
        this.config = config;
        this.sc = sc;
        this.rand = new Random();
        this.tabuleiro = new Tabuleiro();
        this.baralho = new Baralho(24);
        this.historico = new FilaHistorico(50);
        this.filaPrisao = new FilaPrisao(6);
    }

    public void iniciar() {
        System.out.println("\n============================================================");
        System.out.println("                   INICIANDO PARTIDA");
        System.out.println("============================================================");

        construirTabuleiro();
        construirBaralho();

        casaPrisao = null;
        Casa c = tabuleiro.getInicio();
        do {
            if ("PRISAO".equals(c.tipo)) {
                casaPrisao = c;
                break;
            }
            c = c.proximo;
        } while (c != tabuleiro.getInicio());

        Casa casaInicio = tabuleiro.getInicio();
        for (int i = 0; i < qtdJogadores; i++) {
            jogadores[i].posicaoAtual = casaInicio;
            jogadores[i].saldo = config.saldoInicial;
            jogadores[i].ativo = true;
            jogadores[i].naPrisao = false;
            jogadores[i].turnosPrisao = 0;
            jogadores[i].voltasCompletas = 0;
        }

        System.out.println("\nTabuleiro criado:");
        tabuleiro.exibir();

        System.out.println("\nJogadores:");
        for (int i = 0; i < qtdJogadores; i++) {
            System.out.println("  " + jogadores[i]);
        }

        System.out.println("\nPressione ENTER para comecar a partida...");
        sc.nextLine();

        rodadaAtual = 0;
        boolean jogoAtivo = true;

        while (jogoAtivo) {
            rodadaAtual++;
            System.out.printf("%n============================================================%n");
            System.out.printf("  RODADA %d de %d%n", rodadaAtual, config.maxRodadas);
            System.out.printf("============================================================%n");

            for (int i = 0; i < qtdJogadores; i++) {
                if (!jogadores[i].ativo)
                    continue;
                executarTurno(jogadores[i]);
                if (contarJogadoresAtivos() <= 1) {
                    jogoAtivo = false;
                    break;
                }
            }

            if (rodadaAtual >= config.maxRodadas) {
                System.out.println("\nNumero maximo de rodadas atingido!");
                jogoAtivo = false;
            }
        }

        exibirRelatorioFinal();
    }

    private void construirTabuleiro() {
        // PRISAO = just visiting / destination for prisoners
        // IR_PRISAO = landing here sends player to jail
        String[] especiais = {
                "SORTE_REVES", "IMPOSTO", "SORTE_REVES",
                "RESTITUICAO", "LEILAO", "PRISAO",
                "SORTE_REVES", "IMPOSTO", "IR_PRISAO"
        };

        tabuleiro.adicionarNoFim(new Casa("INICIO"));

        int espIdx = 0;
        int imvIdx = 0;
        int propPorEsp = Math.max(1, qtdImoveis / especiais.length);

        while (imvIdx < qtdImoveis) {
            for (int k = 0; k < propPorEsp && imvIdx < qtdImoveis; k++, imvIdx++) {
                tabuleiro.adicionarNoFim(new Casa("IMOVEL", imoveis[imvIdx]));
            }
            if (espIdx < especiais.length) {
                tabuleiro.adicionarNoFim(new Casa(especiais[espIdx]));
                espIdx++;
            }
        }
        while (espIdx < especiais.length) {
            tabuleiro.adicionarNoFim(new Casa(especiais[espIdx]));
            espIdx++;
        }
    }

    private void construirBaralho() {
        baralho.limpar();

        baralho.empilhar(new Carta(Carta.GANHO_DINHEIRO,
                "Bonus de produtividade! Receba R$ 500,00 do banco.", 500, 0));
        baralho.empilhar(new Carta(Carta.GANHO_DINHEIRO,
                "Dividendos espaciais! Receba R$ 1.000,00 do banco.", 1000, 0));
        baralho.empilhar(new Carta(Carta.GANHO_DINHEIRO,
                "Subvencao governamental! Receba R$ 800,00 do banco.", 800, 0));
        baralho.empilhar(new Carta(Carta.AVANCAR_CASAS,
                "Propulsao extra! Avance 3 casas.", 0, 3));
        baralho.empilhar(new Carta(Carta.AVANCAR_CASAS,
                "Atalho descoberto! Avance 5 casas.", 0, 5));
        baralho.empilhar(new Carta(Carta.AVANCAR_INICIO,
                "Teletransporte! Va direto para o INICIO e receba seu salario.", 0, 0));
        baralho.empilhar(new Carta(Carta.COBRAR_OUTROS,
                "Royalties de patente! Todos os jogadores pagam R$ 300,00 a voce.", 300, 0));
        baralho.empilhar(new Carta(Carta.PERDA_DINHEIRO,
                "Multa por violacao orbital! Pague R$ 400,00 ao banco.", 400, 0));
        baralho.empilhar(new Carta(Carta.PERDA_DINHEIRO,
                "Falha no reator! Pague R$ 700,00 ao banco.", 700, 0));
        baralho.empilhar(new Carta(Carta.PERDA_DINHEIRO,
                "Taxa de manutencao! Pague R$ 300,00 ao banco.", 300, 0));
        baralho.empilhar(new Carta(Carta.VOLTAR_CASAS,
                "Tempestade cosmica! Volte 3 casas.", 0, 3));
        baralho.empilhar(new Carta(Carta.VOLTAR_CASAS,
                "Rota bloqueada! Volte 5 casas.", 0, 5));
        baralho.empilhar(new Carta(Carta.PAGAR_TODOS,
                "Festa da tripulacao! Pague R$ 200,00 a cada jogador.", 200, 0));
        baralho.empilhar(new Carta(Carta.IR_PRISAO,
                "Infrator espacial! Va direto para a PRISAO.", 0, 0));

        baralho.embaralhar();
        System.out.println("Baralho montado com " + baralho.tamanho() + " cartas e embaralhado.");
    }

    private void executarTurno(Jogador jogador) {
        System.out.println("\n------------------------------------------------------------");
        System.out.printf("  Turno de: %s [%s]%s%n", jogador.nome, jogador.personagem,
                jogador.naPrisao ? " [PRESO]" : "");
        System.out.printf("  Posicao : [%2d] %s%n", jogador.posicaoAtual.numero, jogador.posicaoAtual);
        System.out.printf("  Saldo   : R$ %.2f%n", jogador.saldo);
        System.out.println("------------------------------------------------------------");

        if (jogador.naPrisao) {
            processarTurnoPrisao(jogador);
            return;
        }

        System.out.println("ENTER = lancar dados | H = historico | P = fila da prisao");
        String inp = sc.nextLine().trim().toUpperCase();
        if ("H".equals(inp)) {
            historico.exibir();
            System.out.println("Pressione ENTER para continuar...");
            sc.nextLine();
        } else if ("P".equals(inp)) {
            filaPrisao.exibir();
            System.out.println("Pressione ENTER para continuar...");
            sc.nextLine();
        }

        int[] dados = lancarDados();
        int total = dados[0] + dados[1];
        System.out.printf("Dados: %d + %d = %d%n", dados[0], dados[1], total);

        Casa posAntes = jogador.posicaoAtual;
        int passagens = contarPassagensPeloInicio(posAntes, total);
        Casa novaPosicao = tabuleiro.avancar(posAntes, total);
        jogador.posicaoAtual = novaPosicao;
        System.out.printf("Moveu para: [%2d] %s%n", novaPosicao.numero, novaPosicao);

        for (int i = 0; i < passagens; i++) {
            double salario = config.salario;
            if ("ESPECULADOR".equals(jogador.personagem)) {
                salario *= 1.20;
                System.out.println("  [Habilidade ESPECULADOR] Bonus de 20% no salario aplicado!");
            }
            jogador.saldo += salario;
            jogador.voltasCompletas++;
            System.out.printf("  >> Passou pelo INICIO! Salario: R$ %.2f | Saldo: R$ %.2f%n",
                    salario, jogador.saldo);
        }

        efeitoTurno = novaPosicao.tipo;
        aplicarEfeitoCasa(jogador, false);
        verificarFalencia(jogador);

        historico.enfileirar(new FilaHistorico.Registro(
                rodadaAtual, jogador.nome,
                dados[0] + "+" + dados[1] + "=" + total,
                efeitoTurno));
    }

    private void processarTurnoPrisao(Jogador jogador) {
        jogador.turnosPrisao++;
        int pos = filaPrisao.posicao(jogador);
        System.out.printf("  [PRISAO] Turno preso: %d/3 | Posicao na fila de espera: #%d%n",
                jogador.turnosPrisao, pos);
        filaPrisao.exibir();

        boolean liberado = false;
        int[] dados = null;

        if (jogador.turnosPrisao >= 3) {
            System.out.println("  Liberacao automatica apos 3 turnos na prisao!");
            liberado = true;
            dados = lancarDados();
            System.out.printf("  Dados (saida obrigatoria): %d + %d = %d%n",
                    dados[0], dados[1], dados[0] + dados[1]);
        } else {
            double custoFianca = "ADVOGADO".equals(jogador.personagem) ? 0 : config.fianca;
            System.out.println("  [1] Tentar sair com dados duplos");
            if ("ADVOGADO".equals(jogador.personagem)) {
                System.out.println("  [2] Sair (ADVOGADO - fianca isenta)");
            } else {
                System.out.printf("  [2] Pagar fianca: R$ %.2f%n", custoFianca);
            }
            System.out.print("  Opcao: ");
            int op = lerInt();

            if (op == 2) {
                if (jogador.saldo >= custoFianca) {
                    jogador.saldo -= custoFianca;
                    if ("ADVOGADO".equals(jogador.personagem)) {
                        System.out.println("  [Habilidade ADVOGADO] Saiu da prisao sem pagar fianca!");
                    } else {
                        System.out.printf("  Pagou fianca: R$ %.2f. Saldo: R$ %.2f%n",
                                custoFianca, jogador.saldo);
                    }
                    liberado = true;
                    dados = lancarDados();
                    System.out.printf("  Dados (saida): %d + %d = %d%n",
                            dados[0], dados[1], dados[0] + dados[1]);
                } else {
                    System.out.println("  Saldo insuficiente para fianca. Tentando dados duplos...");
                    op = 1;
                }
            }

            if (op == 1) {
                dados = lancarDados();
                System.out.printf("  Dados: %d + %d%n", dados[0], dados[1]);
                if (dados[0] == dados[1]) {
                    System.out.println("  DUPLO! Saiu da prisao!");
                    liberado = true;
                } else {
                    System.out.println("  Sem duplo. Continua preso na proxima rodada.");
                }
            }
        }

        if (liberado) {
            jogador.naPrisao = false;
            jogador.turnosPrisao = 0;
            filaPrisao.remover(jogador);
            System.out.printf("  %s saiu da prisao!%n", jogador.nome);

            int total = dados[0] + dados[1];
            int passagens = contarPassagensPeloInicio(jogador.posicaoAtual, total);
            jogador.posicaoAtual = tabuleiro.avancar(jogador.posicaoAtual, total);
            System.out.printf("  Moveu para: [%2d] %s%n",
                    jogador.posicaoAtual.numero, jogador.posicaoAtual);

            for (int i = 0; i < passagens; i++) {
                double salario = config.salario;
                if ("ESPECULADOR".equals(jogador.personagem))
                    salario *= 1.20;
                jogador.saldo += salario;
                jogador.voltasCompletas++;
                System.out.printf("  >> Passou pelo INICIO! Salario: R$ %.2f%n", salario);
            }

            efeitoTurno = "[SAIU DA PRISAO] -> " + jogador.posicaoAtual;
            aplicarEfeitoCasa(jogador, false);
            verificarFalencia(jogador);
        } else {
            efeitoTurno = "Preso (turno " + jogador.turnosPrisao + "/3)";
        }

        String dadosStr = dados != null ? dados[0] + "+" + dados[1] : "-";
        historico.enfileirar(new FilaHistorico.Registro(
                rodadaAtual, jogador.nome, dadosStr, efeitoTurno));
    }

    private void enviarParaPrisao(Jogador jogador) {
        jogador.naPrisao = true;
        jogador.turnosPrisao = 0;
        if (casaPrisao != null)
            jogador.posicaoAtual = casaPrisao;
        filaPrisao.enfileirar(jogador);
        int posFila = filaPrisao.posicao(jogador);
        System.out.printf("  >> %s enviado para a PRISAO! Posicao na fila de espera: #%d%n",
                jogador.nome, posFila);
        filaPrisao.exibir();
        efeitoTurno = "IR_PRISAO (fila #" + posFila + ")";
    }

    private void realizarLeilao(Jogador ativador) {
        System.out.println("\n  === LEILAO ===");

        Imovel[] disponiveis = new Imovel[qtdImoveis];
        int qtdDisp = 0;
        for (int i = 0; i < qtdImoveis; i++) {
            if (imoveis[i].dono == null)
                disponiveis[qtdDisp++] = imoveis[i];
        }

        if (qtdDisp == 0) {
            System.out.println("  Nenhum imovel disponivel para leilao.");
            efeitoTurno = "LEILAO (sem imoveis disponiveis)";
            return;
        }

        Imovel lote = disponiveis[rand.nextInt(qtdDisp)];
        System.out.printf("  Imovel sorteado: %s%n", lote.nome);
        System.out.printf("  Valor de mercado: R$ %.2f | Aluguel: R$ %.2f (%.1fx)%n",
                lote.valorCompra, lote.calcularAluguel(), lote.multiplicadorDemanda);

        double minLance = lote.valorCompra * 0.5;
        System.out.printf("  Lance minimo: R$ %.2f (50%% do valor)%n", minLance);

        double maiorLance = 0;
        Jogador vencedor = null;

        for (int i = 0; i < qtdJogadores; i++) {
            if (!jogadores[i].ativo)
                continue;
            System.out.printf("%n  %s (Saldo: R$ %.2f)%n  Lance (0 = passar): R$ ",
                    jogadores[i].nome, jogadores[i].saldo);
            double lance = lerDouble();

            if (lance <= 0) {
                System.out.println("  Passou.");
                continue;
            }
            if (lance < minLance) {
                System.out.printf("  Lance abaixo do minimo (R$ %.2f). Descartado.%n", minLance);
                continue;
            }
            if (lance > jogadores[i].saldo) {
                System.out.println("  Saldo insuficiente. Lance descartado.");
                continue;
            }
            if (lance > maiorLance) {
                maiorLance = lance;
                vencedor = jogadores[i];
                System.out.printf("  Novo maior lance: R$ %.2f (%s)%n", maiorLance, vencedor.nome);
            } else {
                System.out.printf("  Lance menor que o atual (R$ %.2f). Descartado.%n", maiorLance);
            }
        }

        System.out.println("\n  === RESULTADO DO LEILAO ===");
        if (vencedor == null) {
            System.out.println("  Nenhum lance valido. Imovel nao arrematado.");
            lote.incrementarDemanda();
            System.out.printf("  Demanda subiu para %.1fx.%n", lote.multiplicadorDemanda);
            efeitoTurno = "LEILAO (nao arrematado - " + lote.nome + ")";
        } else {
            if ("CONSTRUTOR".equals(vencedor.personagem)) {
                lote.aluguelBase *= 1.15;
                System.out.printf("  [Habilidade CONSTRUTOR] Aluguel base aumentado 15%% -> R$ %.2f!%n",
                        lote.aluguelBase);
            }
            vencedor.saldo -= maiorLance;
            lote.dono = vencedor;
            vencedor.adicionarImovel(lote);
            System.out.printf("  ARREMATADO! %s comprou '%s' por R$ %.2f. Saldo: R$ %.2f%n",
                    vencedor.nome, lote.nome, maiorLance, vencedor.saldo);
            verificarFalencia(vencedor);
            efeitoTurno = "LEILAO (" + lote.nome + " -> " + vencedor.nome + ")";
        }
    }

    private void aplicarEfeitoCasa(Jogador jogador, boolean veioDeRetrocesso) {
        Casa casa = jogador.posicaoAtual;

        switch (casa.tipo) {
            case "INICIO":
                System.out.println("Parou exatamente no INICIO.");
                efeitoTurno = "INICIO (parou exato)";
                break;
            case "IMOVEL":
                aplicarEfeitoImovel(jogador);
                break;
            case "IMPOSTO":
                aplicarImposto(jogador);
                break;
            case "RESTITUICAO":
                aplicarRestituicao(jogador);
                break;
            case "SORTE_REVES":
                System.out.println("Casa SORTE/REVES! Sacando carta do topo da pilha...");
                if (baralho.isEmpty()) {
                    System.out.println("  Baralho esgotado! Remontando e embaralhando...");
                    construirBaralho();
                }
                Carta carta = baralho.desempilhar();
                System.out.println("  Carta sacada: " + carta);
                aplicarCarta(jogador, carta);
                break;
            case "PRISAO":
                System.out.println("Parou na PRISAO (visita). Sem penalidade.");
                efeitoTurno = "PRISAO (visita)";
                break;
            case "IR_PRISAO":
                System.out.printf("Casa IR PARA PRISAO! %s vai preso!%n", jogador.nome);
                enviarParaPrisao(jogador);
                break;
            case "LEILAO":
                System.out.println("Casa LEILAO!");
                realizarLeilao(jogador);
                break;
        }
    }

    private void aplicarEfeitoImovel(Jogador jogador) {
        Imovel imovel = jogador.posicaoAtual.imovel;

        if (imovel.dono == null) {
            System.out.printf("Imovel disponivel: %s%n", imovel.nome);
            System.out.printf("  Preco   : R$ %.2f%n", imovel.valorCompra);
            System.out.printf("  Aluguel : R$ %.2f (multiplicador: %.1fx)%n",
                    imovel.calcularAluguel(), imovel.multiplicadorDemanda);
            System.out.printf("  Saldo   : R$ %.2f%n", jogador.saldo);

            if (jogador.saldo >= imovel.valorCompra) {
                System.out.print("Deseja comprar? (S/N): ");
                String resp = sc.nextLine().trim().toUpperCase();
                if ("S".equals(resp)) {
                    comprarImovel(jogador, imovel);
                    efeitoTurno = "Comprou " + imovel.nome;
                } else {
                    imovel.incrementarDemanda();
                    System.out.printf("  Recusou. Demanda subiu para %.1fx.%n", imovel.multiplicadorDemanda);
                    efeitoTurno = "Recusou " + imovel.nome;
                }
            } else {
                System.out.println("  Saldo insuficiente para comprar.");
                imovel.incrementarDemanda();
                System.out.printf("  Demanda subiu para %.1fx.%n", imovel.multiplicadorDemanda);
                efeitoTurno = "Sem saldo p/ " + imovel.nome;
            }

        } else if (imovel.dono == jogador) {
            System.out.printf("Voce e o dono de %s. Nada acontece.%n", imovel.nome);
            efeitoTurno = "Proprio imovel: " + imovel.nome;
        } else {
            pagarAluguel(jogador, imovel);
        }
    }

    private void comprarImovel(Jogador jogador, Imovel imovel) {
        if ("CONSTRUTOR".equals(jogador.personagem)) {
            imovel.aluguelBase *= 1.15;
            System.out.printf("  [Habilidade CONSTRUTOR] Aluguel base aumentado 15%% -> R$ %.2f!%n",
                    imovel.aluguelBase);
        }
        double saldoAntes = jogador.saldo;
        jogador.saldo -= imovel.valorCompra;
        imovel.dono = jogador;
        jogador.adicionarImovel(imovel);
        System.out.printf("  Comprou '%s' por R$ %.2f. Saldo: R$ %.2f -> R$ %.2f%n",
                imovel.nome, imovel.valorCompra, saldoAntes, jogador.saldo);
    }

    private void pagarAluguel(Jogador jogador, Imovel imovel) {
        double aluguel = imovel.calcularAluguel();
        System.out.printf("Imovel de %s. Multiplicador de demanda: %.1fx%n",
                imovel.dono.nome, imovel.multiplicadorDemanda);

        if ("NEGOCIANTE".equals(jogador.personagem)) {
            aluguel *= 0.90;
            System.out.println("  [Habilidade NEGOCIANTE] Aluguel reduzido em 10%!");
        }

        double saldoAntes = jogador.saldo;
        jogador.saldo -= aluguel;
        imovel.dono.saldo += aluguel;
        imovel.registrarAluguelCobrado(aluguel);
        imovel.incrementarDemanda();

        System.out.printf("  Aluguel pago: R$ %.2f | Saldo: R$ %.2f -> R$ %.2f%n",
                aluguel, saldoAntes, jogador.saldo);
        System.out.printf("  %s recebeu R$ %.2f. Demanda subiu para %.1fx.%n",
                imovel.dono.nome, aluguel, imovel.multiplicadorDemanda);
        efeitoTurno = "Aluguel R$" + String.format("%.0f", aluguel) + " -> " + imovel.dono.nome;
    }

    private void aplicarImposto(Jogador jogador) {
        double patrimonio = jogador.calcularPatrimonio();
        double imposto = patrimonio * 0.05;

        if ("ESPECULADOR".equals(jogador.personagem)) {
            imposto *= 1.10;
            System.out.println("  [Habilidade ESPECULADOR] Imposto acrescido de 10%!");
        }

        System.out.printf("Casa IMPOSTO. Patrimonio: R$ %.2f | Imposto (5%%): R$ %.2f%n",
                patrimonio, imposto);
        double saldoAntes = jogador.saldo;
        jogador.saldo -= imposto;
        System.out.printf("  Saldo: R$ %.2f -> R$ %.2f%n", saldoAntes, jogador.saldo);
        efeitoTurno = "Imposto R$" + String.format("%.0f", imposto);
    }

    private void aplicarRestituicao(Jogador jogador) {
        double valor = config.salario * 0.10;
        System.out.printf("Casa RESTITUICAO! Recebeu 10%% do salario: R$ %.2f%n", valor);
        double saldoAntes = jogador.saldo;
        jogador.saldo += valor;
        System.out.printf("  Saldo: R$ %.2f -> R$ %.2f%n", saldoAntes, jogador.saldo);
        efeitoTurno = "Restituicao +R$" + (int) valor;
    }

    private void aplicarCarta(Jogador jogador, Carta carta) {
        double saldoAntes = jogador.saldo;

        switch (carta.tipo) {

            case Carta.GANHO_DINHEIRO:
                jogador.saldo += carta.valor;
                System.out.printf("  Recebeu R$ %.2f do banco. Saldo: R$ %.2f -> R$ %.2f%n",
                        carta.valor, saldoAntes, jogador.saldo);
                efeitoTurno = "Carta: +R$" + (int) carta.valor;
                break;

            case Carta.PERDA_DINHEIRO:
                jogador.saldo -= carta.valor;
                System.out.printf("  Pagou R$ %.2f ao banco. Saldo: R$ %.2f -> R$ %.2f%n",
                        carta.valor, saldoAntes, jogador.saldo);
                efeitoTurno = "Carta: -R$" + (int) carta.valor;
                break;

            case Carta.AVANCAR_CASAS:
                int passaAv = contarPassagensPeloInicio(jogador.posicaoAtual, carta.casas);
                jogador.posicaoAtual = tabuleiro.avancar(jogador.posicaoAtual, carta.casas);
                System.out.printf("  Avancou %d casas -> [%2d] %s%n",
                        carta.casas, jogador.posicaoAtual.numero, jogador.posicaoAtual);
                for (int i = 0; i < passaAv; i++) {
                    double sal = config.salario;
                    if ("ESPECULADOR".equals(jogador.personagem))
                        sal *= 1.20;
                    jogador.saldo += sal;
                    jogador.voltasCompletas++;
                    System.out.printf("  >> Passou pelo INICIO! Salario: R$ %.2f%n", sal);
                }
                efeitoTurno = "Carta: avancou " + carta.casas + " casas";
                aplicarEfeitoCasa(jogador, false);
                break;

            case Carta.VOLTAR_CASAS:
                jogador.posicaoAtual = tabuleiro.retroceder(jogador.posicaoAtual, carta.casas);
                System.out.printf("  Retrocedeu %d casas -> [%2d] %s%n",
                        carta.casas, jogador.posicaoAtual.numero, jogador.posicaoAtual);
                System.out.println("  [Retrocesso nao gera salario ao cruzar INICIO]");
                efeitoTurno = "Carta: retrocedeu " + carta.casas + " casas";
                aplicarEfeitoCasa(jogador, true);
                break;

            case Carta.AVANCAR_INICIO:
                jogador.posicaoAtual = buscarProximoInicio(jogador.posicaoAtual);
                double sal = config.salario;
                if ("ESPECULADOR".equals(jogador.personagem)) {
                    sal *= 1.20;
                    System.out.println("  [Habilidade ESPECULADOR] Bonus de 20% no salario!");
                }
                jogador.saldo += sal;
                jogador.voltasCompletas++;
                System.out.printf("  Teletransporte para INICIO! Salario: R$ %.2f | Saldo: R$ %.2f%n",
                        sal, jogador.saldo);
                efeitoTurno = "Carta: teletransporte INICIO";
                break;

            case Carta.COBRAR_OUTROS:
                System.out.printf("  Cobrando R$ %.2f de cada jogador:%n", carta.valor);
                for (int i = 0; i < qtdJogadores; i++) {
                    if (jogadores[i] != jogador && jogadores[i].ativo) {
                        jogadores[i].saldo -= carta.valor;
                        jogador.saldo += carta.valor;
                        System.out.printf("    %s pagou R$ %.2f a %s.%n",
                                jogadores[i].nome, carta.valor, jogador.nome);
                        verificarFalencia(jogadores[i]);
                    }
                }
                System.out.printf("  Saldo de %s: R$ %.2f%n", jogador.nome, jogador.saldo);
                efeitoTurno = "Carta: cobrou R$" + (int) carta.valor + " de todos";
                break;

            case Carta.PAGAR_TODOS:
                System.out.printf("  Pagando R$ %.2f a cada jogador:%n", carta.valor);
                for (int i = 0; i < qtdJogadores; i++) {
                    if (jogadores[i] != jogador && jogadores[i].ativo) {
                        jogador.saldo -= carta.valor;
                        jogadores[i].saldo += carta.valor;
                        System.out.printf("    %s pagou R$ %.2f a %s.%n",
                                jogador.nome, carta.valor, jogadores[i].nome);
                    }
                }
                System.out.printf("  Saldo de %s: R$ %.2f%n", jogador.nome, jogador.saldo);
                efeitoTurno = "Carta: pagou R$" + (int) carta.valor + " a todos";
                break;

            case Carta.IR_PRISAO:
                System.out.printf("  %s vai preso!%n", jogador.nome);
                enviarParaPrisao(jogador);
                break;
        }
    }

    private void verificarFalencia(Jogador jogador) {
        if (!jogador.ativo)
            return;
        if (jogador.saldo >= 0)
            return;

        System.out.printf("%n>>> FALENCIA: %s com saldo negativo (R$ %.2f)! <<<<%n",
                jogador.nome, jogador.saldo);

        while (jogador.temImoveis()) {
            Imovel im = jogador.imoveis[0];
            im.dono = null;
            System.out.printf("    Imovel '%s' devolvido ao pool (sem dono).%n", im.nome);
            jogador.removerImovel(im);
        }

        if (jogador.naPrisao) {
            filaPrisao.remover(jogador);
            jogador.naPrisao = false;
        }

        jogador.ativo = false;
        System.out.printf(">>> %s foi eliminado da partida! <<<<%n%n", jogador.nome);
    }

    private void exibirRelatorioFinal() {
        System.out.println("\n============================================================");
        System.out.println("                ENCERRAMENTO DA PARTIDA");
        System.out.println("============================================================");
        System.out.println("Rodadas jogadas: " + rodadaAtual);

        Jogador[] ranking = new Jogador[qtdJogadores];
        for (int i = 0; i < qtdJogadores; i++)
            ranking[i] = jogadores[i];
        ordenarPorPatrimonio(ranking, qtdJogadores);

        System.out.println("\n--- Classificacao por Patrimonio ---");
        for (int i = 0; i < qtdJogadores; i++) {
            System.out.printf("  %d. %-15s | Patrimonio: R$%10.2f | Voltas: %d | %s%n",
                    i + 1, ranking[i].nome, ranking[i].calcularPatrimonio(),
                    ranking[i].voltasCompletas, ranking[i].ativo ? "Ativo" : "Falido");
        }

        System.out.println("\n--- Voltas completas por jogador ---");
        for (int i = 0; i < qtdJogadores; i++) {
            System.out.printf("  %-15s : %d volta(s)%n",
                    jogadores[i].nome, jogadores[i].voltasCompletas);
        }

        Imovel top = null;
        for (int i = 0; i < qtdImoveis; i++) {
            if (top == null || imoveis[i].maiorAluguelCobrado > top.maiorAluguelCobrado) {
                top = imoveis[i];
            }
        }
        System.out.println("\n--- Imovel com maior aluguel cobrado ---");
        if (top != null && top.maiorAluguelCobrado > 0) {
            System.out.printf("  %s (R$ %.2f)%n", top.nome, top.maiorAluguelCobrado);
        } else {
            System.out.println("  Nenhum aluguel cobrado na partida.");
        }

        System.out.println("\n--- Historico de Rodadas (Fila - " + historico.getTamanho() + " entradas) ---");
        historico.exibir();

        System.out.println("\n============================================================");
        if (contarJogadoresAtivos() == 1) {
            for (int i = 0; i < qtdJogadores; i++) {
                if (jogadores[i].ativo) {
                    System.out.println("  VENCEDOR (ultimo em pe): " + jogadores[i].nome + "!");
                    break;
                }
            }
        } else {
            System.out.println("  VENCEDOR (maior patrimonio): " + ranking[0].nome + "!");
        }
        System.out.println("============================================================");
    }

    private int contarPassagensPeloInicio(Casa posicaoAtual, int passos) {
        Casa pos = posicaoAtual;
        int count = 0;
        for (int i = 0; i < passos; i++) {
            pos = pos.proximo;
            if ("INICIO".equals(pos.tipo))
                count++;
        }
        return count;
    }

    private Casa buscarProximoInicio(Casa posicaoAtual) {
        Casa pos = posicaoAtual;
        do {
            pos = pos.proximo;
        } while (!"INICIO".equals(pos.tipo));
        return pos;
    }

    private void ordenarPorPatrimonio(Jogador[] arr, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j].calcularPatrimonio() < arr[j + 1].calcularPatrimonio()) {
                    Jogador temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    private int contarJogadoresAtivos() {
        int c = 0;
        for (int i = 0; i < qtdJogadores; i++) {
            if (jogadores[i].ativo)
                c++;
        }
        return c;
    }

    private int[] lancarDados() {
        return new int[] { rand.nextInt(6) + 1, rand.nextInt(6) + 1 };
    }

    private int lerInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Digite um numero inteiro: ");
            }
        }
    }

    private double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Digite um numero: ");
            }
        }
    }
}
