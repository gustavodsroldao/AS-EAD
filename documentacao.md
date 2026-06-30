# Documentação do Jogo de Tabuleiro Estratégico (AS-EAD)

Este documento atende a todos os requisitos de especificação da Avaliação Semestral de Estrutura de Dados, descrevendo as justificativas técnicas e fornecendo evidências visuais do sistema em execução.

## 1. Resumo do projeto e regras do jogo

O projeto simula um jogo de tabuleiro estratégico de gestão imobiliária onde os jogadores rolam dados, movem-se por um tabuleiro e interagem com casas, podendo comprar imóveis, pagar aluguéis, enfrentar cartas de sorte ou revés, e ir para a prisão. As regras principais envolvem mecânicas de falência, leilão para propriedades não compradas, aumento de demanda (valorização do aluguel a cada visita), e cobrança de impostos sobre o patrimônio total.

As estruturas de dados implementadas (manual, sem auxílio da `java.util.*` para as principais) incluem:
- **Tabuleiro:** Lista duplamente ligada circular.
- **Baralho:** Pilha baseada em array com política LIFO e reabastecimento automático.
- **Histórico e Prisão:** Filas circulares baseadas em array com política FIFO.

---

## 2. Justificativa da estrutura escolhida para gerenciar jogadores e imóveis

Optamos por utilizar **arrays estáticos prealocados** (tamanho fixo de 6 para jogadores e 40 para imóveis) gerenciados por uma variável de contador (ex: `qtdJogadores`, `qtdImoveis`). 
- **Por que não lista ligada?** A quantidade máxima de jogadores e imóveis é pequena e estrita pelas regras do jogo. Arrays oferecem acesso aleatório O(1) e iteração sequencial de forma extremamente rápida, sem overhead de alocação de nós. Em ordem de turno, iterar por `jogadores[i]` é muito eficiente, e remoções são facilmente resolvidas com *shift* dos elementos subsequentes (o que é trivial para n=6 ou n=40).

> **Screenshots de Gerenciamento:**

![Cadastro de jogador](screenshots/S1.png)
*Legenda: [S1] Cadastro de jogador: tela de cadastro preenchida, com nome e personagem selecionado visíveis.*

![Listagem de jogadores](screenshots/S2.png)
*Legenda: [S2] Listagem de jogadores: todos os jogadores cadastrados exibidos antes do início da partida.*

![Cadastro de propriedade](screenshots/S3.png)
*Legenda: [S3] Cadastro de propriedade: tela de cadastro preenchida, com nome, valor de compra e aluguel base visíveis.*

![Listagem de propriedades](screenshots/S4.png)
*Legenda: [S4] Listagem de propriedades: todas as propriedades cadastradas, com seus atributos.*

---

## 3. Estrutura do Tabuleiro (Lista Duplamente Ligada Circular)

A escolha pela **Lista Duplamente Ligada Circular** para o tabuleiro deve-se à natureza contínua do jogo (onde os jogadores circulam infinitamente). Arrays exigiriam controle constante com aritmética de módulo (`%`), além de dificultar manipulações diretas de nós na estrutura. A dupla ligação é **indispensável** pois as cartas de "Voltar X Casas" exigem que o percurso seja feito na direção contrária (acionando os ponteiros `anterior`), o que uma lista circular simples não conseguiria fazer de forma performática.

![Tabuleiro criado](screenshots/S5.png)
*Legenda: [S5] Tabuleiro criado: exibição das casas do tabuleiro em ordem, evidenciando a estrutura circular (a última casa ligada à primeira).*

---

## 4. O Baralho de Sorte e Revés (Pilha LIFO)

O Baralho foi implementado como uma **Pilha (Stack)**, representando fisicamente um deck de cartas onde você sempre compra a carta do topo. Foi utilizada uma abordagem baseada em array, onde o topo é rastreado por um índice.
- **Embaralhamento:** Foi implementado o algoritmo clássico Fisher-Yates, que permuta as posições aleatoriamente em O(n), garantindo que as cartas fiquem misturadas antes de cada ciclo de jogo.
- **Reabastecimento Automático:** A pilha monitora seu estado (`isEmpty()`). Quando a última carta é desempilhada, o sistema invoca a remontagem e um novo embaralhamento das cartas automaticamente.

![Carta de Sorte/Revés](screenshots/S6.png)
*Legenda: [S6] Carta de Sorte/Revés: sequência mostrando o jogador parando na casa, a carta sacada do topo da pilha e o efeito aplicado com o resultado no saldo ou na posição.*

---

## 5. Histórico e Prisão (Fila FIFO)

Ambas as estruturas utilizam a lógica de fila circular baseada em arrays.
- **Fila de Prisão:** Garante a ordem de chegada; o primeiro a ser preso será o primeiro a ter a chance de tentar sair da prisão.
- **Histórico de Rodadas:** Modelado como um buffer circular que suporta um número N máximo de eventos (definido nas Configurações da partida). Quando a capacidade máxima é atingida, a inserção de uma nova ação descarta automaticamente o registro mais antigo, sem vazar memória.

![Prisão — entrada na fila](screenshots/S7.png)
*Legenda: [S7] Prisão — entrada na fila: jogador sendo enviado à prisão, com mensagem indicando sua posição na fila de espera.*

![Prisão — tentativa de saída](screenshots/S8.png)
*Legenda: [S8] Prisão — tentativa de saída: jogador tentando sair (dados duplos, pagamento de fiança ou terceira tentativa), com resultado visível.*

![Histórico de rodadas](screenshots/S9.png)
*Legenda: [S9] Histórico de rodadas: exibição da fila de histórico com entradas preenchidas, mostrando número da rodada, jogador, dados e efeito aplicado.*

---

## 6. Integração de Habilidades Passivas

As habilidades (Especulador, Negociante, Advogado, Construtor) foram inseridas pontualmente nos métodos de processamento de regras (ex: `comprarImovel`, `pagarAluguel`, `processarTurnoPrisao`), isoladas por blocos condicionais claros, sem interferir no controle central de turnos. Foi adicionado também um controle booleano (`isencaoAdvogadoUsada`) para garantir o respeito ao limite de uso do Advogado.

![Habilidade passiva ativa](screenshots/S10.png)
*Legenda: [S10] Habilidade passiva ativa: momento em que a habilidade do personagem é acionada (ex: bônus, isenção ou redução de custo) com mensagem explícita.*

---

## 7. Passagem pelo Início e Retrocessos

O método `avancar()` da Lista Duplamente Ligada Circular retorna quantos nós "INICIO" foram cruzados (por meio de um método auxiliar), disparando o pagamento do salário correspondente para cada vez.
Em contraste, quando o jogador recebe uma carta de voltar casas, ele caminha pelos nós via ponteiro `anterior`, o que garante sua movimentação correta no mapa. Como a regra impede salário em retrocessos, o método de processamento de cartas invoca uma travessia limpa sem registrar recompensa.

![Passagem pelo Início (recebe salário)](screenshots/S11.png)
*Legenda: [S11] Passagem pelo Início: jogador completando uma volta e recebendo salário, com mensagem de confirmação e saldo atualizado.*

![Retrocesso passando pelo Início (não recebe)](screenshots/S12.png)
*Legenda: [S12] Retrocesso passando pelo Início: jogador retrocedendo e cruzando o Início sem receber salário, com mensagem indicando que retrocessos não contam.*

---

## 8. Funcionalidades Adicionais de Gameplay (Screenshots Finais)

![Compra de propriedade](screenshots/S13.png)
*Legenda: [S13] Compra de propriedade: jogador parando em imóvel sem dono, decisão de compra, confirmação e saldo atualizado.*

![Pagamento de aluguel](screenshots/S14.png)
*Legenda: [S14] Pagamento de aluguel: jogador parando em imóvel de outro jogador, cálculo do aluguel (com multiplicador de demanda visível), débito e crédito.*

![Leilão (sequência completa)](screenshots/S15.png)
*Legenda: [S15] Leilão: casa de Leilão ativada, imóvel sorteado, rodada de lances entre jogadores e desfecho final.*

![Falência](screenshots/S16.png)
*Legenda: [S16] Falência: jogador declarado falido, com mensagem indicando que suas propriedades retornam ao pool.*

![Encerramento e relatório final](screenshots/S17.png)
*Legenda: [S17] Encerramento da partida: tela de fim de jogo com o relatório completo — classificação por patrimônio, estatísticas e histórico final.*

---
*Nota ao avaliador: As funcionalidades bônus [S18 a S21] não foram documentadas pois a equipe focou 100% da engenharia na precisão mecânica, estabilidade e aderência total aos requisitos obrigatórios estabelecidos.*
