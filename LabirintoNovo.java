import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import busca.Heuristica;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Estado;
import busca.MostraStatusConsole;
import busca.Nodo;
import javax.swing.JOptionPane;

public class LabirintoNovo implements Estado, Heuristica {

    final char matriz[][]; 
    int linhaEntrada1, colunaEntrada1; 
    int linhaEntrada2, colunaEntrada2;
    int linhaSaida, colunaSaida;
    final String op; 

    char [][]clonar(char origem[][]) {
        char destino[][] = new char[origem.length][origem.length];
        for (int i = 0; i < origem.length; i++) {
            for (int j = 0; j < origem.length; j++) {
                destino[i][j] = origem[i][j];
            }
        }
        return destino;
    }
    
    public LabirintoNovo(char m[][], int linhaEntrada1, int colunaEntrada1, int linhaEntrada2, int colunaEntrada2,
        int linhaSaida, int colunaSaida, String o) {
        this.matriz = m; 
        this.linhaEntrada1 = linhaEntrada1;
        this.colunaEntrada1 = colunaEntrada1;
        this.linhaEntrada2 = linhaEntrada2;
        this.colunaEntrada2 = colunaEntrada2;
        this.linhaSaida = linhaSaida;
        this.colunaSaida = colunaSaida;
        this.op = o;
    }

    public LabirintoNovo(int dimensao, String o, int porcentagemObstaculos) {
        this.matriz = new char[dimensao][dimensao];
        this.op = o;
        
        int quantidadeObstaculos = (dimensao*dimensao)* porcentagemObstaculos/100;
        System.out.println("Quantidade de obstaculos: " + quantidadeObstaculos);
        
        Random gerador = new Random();

        int entrada1 = gerador.nextInt(dimensao * dimensao); 
        int entrada2;
        do {
            entrada2 = gerador.nextInt(dimensao * dimensao);
        } while (entrada1 == entrada2);
        
        int saida;
        do {
            saida = gerador.nextInt(dimensao * dimensao);
        } while (saida == entrada1 || saida == entrada2);

        int contaPosicoes = 0;
        for (int i = 0; i < dimensao; i++) {
            for (int j = 0; j < dimensao; j++) {
                if (contaPosicoes == entrada1) {
                    this.matriz[i][j] = '¹';
                    this.linhaEntrada1 = i;
                    this.colunaEntrada1 = j;
                } else if (contaPosicoes == entrada2) {
                    this.matriz[i][j] = '²';
                    this.linhaEntrada2 = i;
                    this.colunaEntrada2 = j;
                } else if (contaPosicoes == saida) {
                    this.matriz[i][j] = 'S';
                    this.linhaSaida = i;
                    this.colunaSaida = j;
                } else if (quantidadeObstaculos > 0 && gerador.nextInt(3) == 1) {
                    quantidadeObstaculos--;
                    this.matriz[i][j] = '@';
                } else {
                    this.matriz[i][j] = 'O';
                }
                contaPosicoes++;
            }
        }
    }

    @Override
    public boolean ehMeta() {
        return (this.linhaEntrada1 == this.linhaSaida && this.colunaEntrada1 == this.colunaSaida) ||
               (this.linhaEntrada2 == this.linhaSaida && this.colunaEntrada2 == this.colunaSaida);
    }

    @Override
    public int custo() {
        return 1;
    }

    @Override 
    public int h() {
        int dist1 = Math.abs(linhaEntrada1 - linhaSaida) + Math.abs(colunaEntrada1 - colunaSaida);
        int dist2 = Math.abs(linhaEntrada2 - linhaSaida) + Math.abs(colunaEntrada2 - colunaSaida);
        return Math.min(dist1, dist2);
    }

    @Override
    public List<Estado> sucessores() {
        List<Estado> visitados = new LinkedList<Estado>();

        moverEntrada1(visitados);
        
        moverEntrada2(visitados);
        
        return visitados;
    }

    private void moverEntrada1(List<Estado> visitados) {
        if (this.linhaEntrada1 > 0 && this.matriz[this.linhaEntrada1 - 1][this.colunaEntrada1] != '@') {
            char mTemp[][] = clonar(this.matriz);
            mTemp[this.linhaEntrada1][this.colunaEntrada1] = 'O';
            mTemp[this.linhaEntrada1 - 1][this.colunaEntrada1] = '¹';
            
            LabirintoNovo novo = new LabirintoNovo(mTemp, this.linhaEntrada1 - 1, this.colunaEntrada1, 
                                     this.linhaEntrada2, this.colunaEntrada2,
                                     this.linhaSaida, this.colunaSaida,
                                     "Movendo E¹ para cima");
            if (!visitados.contains(novo)) visitados.add(novo);
        }

        if (this.linhaEntrada1 < this.matriz.length-1 && this.matriz[this.linhaEntrada1 + 1][this.colunaEntrada1] != '@') {
            char mTemp[][] = clonar(this.matriz);
            mTemp[this.linhaEntrada1][this.colunaEntrada1] = 'O';
            mTemp[this.linhaEntrada1 + 1][this.colunaEntrada1] = '¹';
            
            LabirintoNovo novo = new LabirintoNovo(mTemp, this.linhaEntrada1 + 1, this.colunaEntrada1, 
                                     this.linhaEntrada2, this.colunaEntrada2,
                                     this.linhaSaida, this.colunaSaida,
                                     "Movendo E¹ para baixo");
            if (!visitados.contains(novo)) visitados.add(novo);
        }

        if (this.colunaEntrada1 > 0 && this.matriz[this.linhaEntrada1][this.colunaEntrada1 - 1] != '@') {
            char mTemp[][] = clonar(this.matriz);
            mTemp[this.linhaEntrada1][this.colunaEntrada1] = 'O';
            mTemp[this.linhaEntrada1][this.colunaEntrada1 - 1] = '¹';
            
            LabirintoNovo novo = new LabirintoNovo(mTemp, this.linhaEntrada1, this.colunaEntrada1 - 1, 
                                     this.linhaEntrada2, this.colunaEntrada2,
                                     this.linhaSaida, this.colunaSaida,
                                     "Movendo E¹ para esquerda");
            if (!visitados.contains(novo)) visitados.add(novo);
        }

        if (this.colunaEntrada1 < this.matriz.length-1 && this.matriz[this.linhaEntrada1][this.colunaEntrada1 + 1] != '@') {
            char mTemp[][] = clonar(this.matriz);
            mTemp[this.linhaEntrada1][this.colunaEntrada1] = 'O';
            mTemp[this.linhaEntrada1][this.colunaEntrada1 + 1] = '¹';
            
            LabirintoNovo novo = new LabirintoNovo(mTemp, this.linhaEntrada1, this.colunaEntrada1 + 1, 
                                     this.linhaEntrada2, this.colunaEntrada2,
                                     this.linhaSaida, this.colunaSaida,
                                     "Movendo E¹ para direita");
            if (!visitados.contains(novo)) visitados.add(novo);
        }
    }

    private void moverEntrada2(List<Estado> visitados) {
        if (this.linhaEntrada2 > 0 && this.matriz[this.linhaEntrada2 - 1][this.colunaEntrada2] != '@') {
            char mTemp[][] = clonar(this.matriz);
            mTemp[this.linhaEntrada2][this.colunaEntrada2] = 'O';
            mTemp[this.linhaEntrada2 - 1][this.colunaEntrada2] = '²';
            
            LabirintoNovo novo = new LabirintoNovo(mTemp, this.linhaEntrada1, this.colunaEntrada1, 
                                     this.linhaEntrada2 - 1, this.colunaEntrada2,
                                     this.linhaSaida, this.colunaSaida,
                                     "Movendo E² para cima");
            if (!visitados.contains(novo)) visitados.add(novo);
        }

        if (this.linhaEntrada2 < this.matriz.length-1 && this.matriz[this.linhaEntrada2 + 1][this.colunaEntrada2] != '@') {
            char mTemp[][] = clonar(this.matriz);
            mTemp[this.linhaEntrada2][this.colunaEntrada2] = 'O';
            mTemp[this.linhaEntrada2 + 1][this.colunaEntrada2] = '²';
            
            LabirintoNovo novo = new LabirintoNovo(mTemp, this.linhaEntrada1, this.colunaEntrada1, 
                                     this.linhaEntrada2 + 1, this.colunaEntrada2,
                                     this.linhaSaida, this.colunaSaida,
                                     "Movendo E² para baixo");
            if (!visitados.contains(novo)) visitados.add(novo);
        }

        if (this.colunaEntrada2 > 0 && this.matriz[this.linhaEntrada2][this.colunaEntrada2 - 1] != '@') {
            char mTemp[][] = clonar(this.matriz);
            mTemp[this.linhaEntrada2][this.colunaEntrada2] = 'O';
            mTemp[this.linhaEntrada2][this.colunaEntrada2 - 1] = '²';
            
            LabirintoNovo novo = new LabirintoNovo(mTemp, this.linhaEntrada1, this.colunaEntrada1, 
                                     this.linhaEntrada2, this.colunaEntrada2 - 1,
                                     this.linhaSaida, this.colunaSaida,
                                     "Movendo E² para esquerda");
            if (!visitados.contains(novo)) visitados.add(novo);
        }

        if (this.colunaEntrada2 < this.matriz.length-1 && this.matriz[this.linhaEntrada2][this.colunaEntrada2 + 1] != '@') {
            char mTemp[][] = clonar(this.matriz);
            mTemp[this.linhaEntrada2][this.colunaEntrada2] = 'O';
            mTemp[this.linhaEntrada2][this.colunaEntrada2 + 1] = '²';
            
            LabirintoNovo novo = new LabirintoNovo(mTemp, this.linhaEntrada1, this.colunaEntrada1, 
                                     this.linhaEntrada2, this.colunaEntrada2 + 1,
                                     this.linhaSaida, this.colunaSaida,
                                     "Movendo E² para direita");
            if (!visitados.contains(novo)) visitados.add(novo);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof LabirintoNovo) {
            LabirintoNovo e = (LabirintoNovo) o;
            for (int i = 0; i < e.matriz.length; i++) {
                for (int j = 0; j < e.matriz.length; j++) {
                    if (e.matriz[i][j] != this.matriz[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        String estado = "";
        
        for (int i = 0; i < this.matriz.length; i++) {
            for (int j = 0; j < this.matriz.length; j++) {
                estado = estado + this.matriz[i][j];
            }
        }
        return estado.hashCode();
    }

    @Override
    public String toString() {
        final String RESET = "\u001B[0m";
        
        StringBuffer resultado = new StringBuffer();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                char c = this.matriz[i][j];
                switch (c) {
                    case '¹':
                        resultado.append(RESET).append("E¹").append(RESET);
                        break;
                    case '²':
                        resultado.append(RESET).append("E²").append(RESET);
                        break;
                    case 'S':
                        resultado.append(RESET).append("S").append(RESET).append(" ");
                        break;
                    default:
                        resultado.append(c).append(" ");
                }
                resultado.append(" ");
            }
            resultado.append("\n");
        }
        resultado.append("\nPosicao Entrada E¹: (").append(linhaEntrada1).append(",").append(colunaEntrada1).append(")");
        resultado.append("\nPosicao Entrada E²: (").append(linhaEntrada2).append(",").append(colunaEntrada2).append(")");
        resultado.append("\nPosicao Saida S: (").append(linhaSaida).append(",").append(colunaSaida).append(")\n");
        return "\n" + op + "\n" + resultado.toString();
    }
    public static void main(String[] a) {
        int dimensao;
        int porcentagemObstaculos;
        
        try {
            dimensao = Integer.parseInt(JOptionPane.showInputDialog(null,"Entre com a dimensao do Puzzle!"));
            porcentagemObstaculos = Integer.parseInt(JOptionPane.showInputDialog(null,"Porcentagem de obstaculos!"));
            final LabirintoNovo estadoInicial = new LabirintoNovo(dimensao, "estado inicial", porcentagemObstaculos);
            
            System.out.println("Estado Inicial:");
            System.out.println(estadoInicial);
            
            final Nodo[] resultadoProfundidade = {null};
            final Nodo[] resultadoLargura = {null};
            final long[] tempoProfundidade = {0};
            final long[] tempoLargura = {0};
            
            ExecutorService executor = Executors.newFixedThreadPool(2);
            
            executor.execute(() -> {
                long inicio = System.currentTimeMillis();
                System.out.println("\nIniciando busca em Profundidade");
                resultadoProfundidade[0] = new BuscaProfundidade(new MostraStatusConsole()).busca(estadoInicial);
                tempoProfundidade[0] = System.currentTimeMillis() - inicio;
                
                if (resultadoProfundidade[0] == null) {
                    System.out.println("Busca em profundidade: sem solucao!");
                } else {
                    System.out.println("Solucao por profundidade:\n" + resultadoProfundidade[0].montaCaminho() + "\n\n");
                }
            });
            
            executor.execute(() -> {
                long inicio = System.currentTimeMillis();
                System.out.println("\nIniciando busca em Largura");
                resultadoLargura[0] = new BuscaLargura(new MostraStatusConsole()).busca(estadoInicial);
                tempoLargura[0] = System.currentTimeMillis() - inicio;
                
                if (resultadoLargura[0] == null) {
                    System.out.println("Busca em largura: sem solucao!");
                } else {
                    System.out.println("Solucao por largura:\n" + resultadoLargura[0].montaCaminho() + "\n\n");
                }
            });
            
            executor.shutdown();
            
            while (!executor.isTerminated()) {
                Thread.sleep(100);
            }
            
            System.out.println("\nCOMPARAÇÃO DE EFICIÊNCIA:");
            System.out.println("Tempo Profundidade: " + tempoProfundidade[0] + "ms");
            System.out.println("Tempo Largura: " + tempoLargura[0] + "ms");
            
            String maisEficiente;
            if (resultadoProfundidade[0] == null && resultadoLargura[0] == null) {
                maisEficiente = "Nenhum dos dois conseguiu achar solucao!";
            } else if (resultadoProfundidade[0] == null) {
                maisEficiente = "Apenas largura achou solucao!";
            } else if (resultadoLargura[0] == null) {
                maisEficiente = "Apenas profundidade achou solucao!";
            } else {
                if (tempoProfundidade[0] < tempoLargura[0]) {
                    maisEficiente = "Busca por profundidade foi mais rapida.";
                } else if (tempoLargura[0] < tempoProfundidade[0]) {
                    maisEficiente = "Busca por largura foi mais rapida.";
                } else {
                    maisEficiente = "Ambas tiveram o mesmo tempo de execucao.";
                }
            }
            
            System.out.println("\nConclusao: " + maisEficiente);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
