# Título do projeto

Terminal Minesweeper.

## Descrição

Jogo de campo minado no terminal feito em Java.

## Iniciando

### Dependências

* JAVA SE 13.
* Suportado em todos os OS.

### Instalando

* Projeto feito em terminal, não é necessária instalação.

### Executando programa

* Executar código através de um compilador que suporte Java.

## Ajuda

* Sugestões ou reporte de erros, contatar: mtsneri@gmail.com.

## Autor

Mateus Neri.

## Histórico de Versões

* 0.0.1
	* Lançamento inicial.
	* As minas são geradas somente após a primeira abertura de um campo.
	* Não implementados tabuleiros com o visual adequado a partir de 10 linhas/colunas (caso sejam aumentados os limites das constantes que determinam o máximo de linhas e colunas, não haverá responsividade visual).
	* Determinados via terminal, os parâmetros de entrada corretos. Assim como implementados os tratamentos para as entradas incorretas.

* 0.0.2
	* Refatorado o método construtor da classe Gameboard, criado método auxiliar para teste das entradas. 
	* Refatorada a apresentação do software, sendo criadas constantes na classe Application para determinar a versão e autor do software.

* 0.0.3
	* Implementado visual adequado para tabuleiros com dimensões de linhas e colunas maiores que 9 e menor que 100.

## Licença

N/A

## Agradecimentos

* [Documentação Java](https://docs.oracle.com/en/java/)
* [Curso Java Udemy](https://www.udemy.com/course/fundamentos-de-programacao-com-java/)