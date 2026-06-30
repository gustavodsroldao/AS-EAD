# Documentação do Jogo de Tabuleiro Estratégico (AS-EAD)

Este documento descreve as principais funcionalidades do sistema, acompanhadas de texto explicativo e demonstrações do sistema em execução (screenshots do estado completo do terminal).

## 1. Menu Principal e Carregamento de Dados

Ao iniciar a aplicação, o usuário é apresentado ao Menu Principal. Para facilitar a demonstração, o sistema possui a funcionalidade de **Carregar dados de exemplo** (opção 4), que cadastra automaticamente jogadores e imóveis baseados em um cenário espacial/ficção científica.

> **Ação para obter o screenshot:** Rode o programa, digite `4` no Menu Principal e pressione Enter. Tire o print da tela inteira do terminal mostrando o menu e a mensagem de sucesso do carregamento dos dados.

![Demonstração do Menu Principal e Carregamento de Dados de Exemplo](screenshots/01_menu_carregamento.png)
*Legenda: Menu Principal do jogo e execução da funcionalidade "Carregar dados de exemplo", confirmando o cadastro automático de 3 jogadores e 12 imóveis espaciais.*

---

## 2. Gerenciamento de Jogadores

O sistema permite o cadastro, listagem, edição e remoção de até 6 jogadores. Cada jogador deve escolher uma classe (personagem) que lhe confere vantagens específicas no jogo (Especulador, Negociante, Advogado, Construtor).

> **Ação para obter o screenshot:** No Menu Principal, digite `1` (Gerenciar Jogadores). Em seguida, digite `2` (Listar jogadores) para exibir os jogadores carregados pelo sistema. Tire o print.

![Listagem de Jogadores Cadastrados](screenshots/02_listar_jogadores.png)
*Legenda: Submenu de Gerenciamento de Jogadores exibindo a listagem atual dos jogadores, seus nomes, saldo inicial e suas respectivas classes (personagens).*

---

## 3. Gerenciamento de Imóveis

É possível cadastrar até 40 imóveis no jogo, definindo o nome do imóvel, valor de compra e o valor base do aluguel.

> **Ação para obter o screenshot:** No Menu Principal, digite `2` (Gerenciar Imóveis). Depois, digite `2` (Listar imóveis) para visualizar os imóveis de exemplo previamente carregados. Tire o print.

![Listagem de Imóveis Cadastrados](screenshots/03_listar_imoveis.png)
*Legenda: Submenu de Gerenciamento de Imóveis exibindo a lista de propriedades disponíveis para compra, juntamente com seus valores de aquisição e aluguéis base.*

---

## 4. Configurações da Partida

Antes de iniciar o jogo, os jogadores podem personalizar parâmetros fundamentais da partida, como o saldo inicial de cada jogador, o valor recebido ao completar uma volta no tabuleiro (salário) e o número máximo de rodadas.

> **Ação para obter o screenshot:** No Menu Principal, digite `3` (Configurações da Partida). Tire o print da tela atual mostrando os valores padrões de configuração.

![Configurações da Partida](screenshots/04_configuracoes.png)
*Legenda: Submenu de Configurações da Partida, exibindo os parâmetros configuráveis de saldo inicial, salário por volta e limite de rodadas.*

---

## 5. Pré-visualização do Tabuleiro

O sistema gera dinamicamente o tabuleiro do jogo (uma estrutura circular baseada nos imóveis cadastrados e casas especiais). O usuário pode pré-visualizar essa estrutura antes de começar a jogar.

> **Ação para obter o screenshot:** Retorne ao Menu Principal (se necessário digitando `0`) e digite `5` (Exibir Tabuleiro). Maximize o terminal para garantir que não haja recortes e tire o print.

![Pré-visualização do Tabuleiro](screenshots/05_tabuleiro.png)
*Legenda: Execução da funcionalidade de pré-visualização do tabuleiro, mostrando as casas ordenadas (Início, Imóveis e casas de eventos especiais como Sorte/Revés, Imposto e Restituição).*

---

## 6. Início e Andamento da Partida

Com jogadores e imóveis configurados, a partida é iniciada (opção 6). O jogo exibe um resumo inicial e, a cada rodada, permite que os jogadores lancem os dados, andem pelo tabuleiro e interajam com as casas em que caem (compra de propriedades, pagamento de aluguel, recebimento de cartas de sorte/revés ou ida à prisão).

> **Ação para obter os screenshots:** 
> 1. No Menu Principal, digite `6` (Iniciar Partida) e confirme com `S`. Tire um print logo no início do turno do primeiro jogador.
> 2. Prossiga o jogo jogando os dados e interagindo com as propriedades. Quando ocorrer um evento marcante (ex: compra de um imóvel ou cair em Sorte/Revés), tire outro print.

![Início da Partida e Turno do Jogador](screenshots/06_inicio_partida.png)
*Legenda: Confirmação das configurações e início oficial da partida, mostrando o lançamento de dados e o menu de turno do primeiro jogador.*

![Interação com o Tabuleiro - Compra de Imóvel](screenshots/07_compra_imovel.png)
*Legenda: Evento de gameplay demonstrando o momento em que um jogador cai em uma casa de imóvel sem dono e tem a opção de realizar a compra, afetando seu saldo.*

---

## Notas de Observação para Avaliação
- As imagens (`.png`) referenciadas neste documento devem ser substituídas pelos prints reais retirados do terminal do sistema. 
- Foi mantido o formato original de tela inteira sem recortes, conforme exigido.
- Cada imagem está devidamente acompanhada por sua legenda explicativa detalhando a funcionalidade técnica demonstrada.
