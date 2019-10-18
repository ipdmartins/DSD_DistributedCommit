Transações Distribuídas com Protocolo de confirmação (commit) em duas fases. Referência: Coulouris/17.3.1 e Tanenbaum/8.5.1 

Rodar em diferentes hosts e usar TCP (tenta confirmar a integralidade no envio de dados) ou UDP (foca na velocidade e ignora
a perda de fragmentos de dados, ideal para vídeos) para comunicação. Servem para enviar e receber pequenos pacotes de dados 
de para um IP, seja na internet ou rede local.

Testar na própria máquina com vários processos e portas.

Deve usar o intervalo de portas liberadas no laboratório: 56000 à 56005. 

O host do lab será o IP interno fixo de cada computador.

IP externo: https://www.meuip.com.br/

IP interno: cmd > ipconfig > ipv4

Localhost: 127.0.0.1

CMD > ping localhost (teste de conexão da máquina).

???????????????????? DÚVIDAS ??????????????????


- Se o Socket conecta no host e na porta, o receptor recebe alguma notificação automaticamente?

- O QUE SERIA O REGISTRO LOCAL DETALHADO NO ALGORITMO?

- Como ocorre o fluxo out e in, ele consegue enviar uma String para a conexão?

- COMO REGISTRAR VOTE ABORT VARIÁVEL LOCAL E TERMINAR?????????

- Participante QUAL A FORMA CERTA DE RETORNAR O SUCESSO DA CONEXÃO??



SocketConnection = SOCKET
RequestManager = SOCKET, OBJECTSTREAM, CONSTANT
Coordenador = OBJECTSTREAM, CONSTANT
