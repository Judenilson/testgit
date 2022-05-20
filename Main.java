import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Main {

  public static void main(String args[]) throws IOException {
    System.out.println("::::: JOGO ADIVINHE SE PUDER :::::");
    System.out.println("Instruções: Você tentará adivinhar a palavra sorteada, \n que terá o tamanho especificado por você, na quantidade \n de tentativas que você também determinará. Como dica, \n será informado em minúscula as letras que existem na \n palavra sorteada porém podem estar em qualquer lugar da \n mesma, e em maiúscula as letras que existirem e \n estiverem na posição correta da palavra. \n Boa sorte e Divirta-se!");
    System.out.println("Preparando Banco de Palavras...");
    Path path = Paths.get("words.txt"); //caminho do arquivo contendo as palavras
    List<String> linhasArquivo = Files.readAllLines(path); //colocando os arquvios numa lista

    HashMap<Integer,List<String>> dicPalavras = new HashMap<Integer,List<String>>(); 
    // separando as palavras num dicionário, colocando o tamanho das palavras como índice.
    for (String item : linhasArquivo){
    	
		String nfdNormalizedString = Normalizer.normalize(item, Normalizer.Form.NFD);//bloco para retirar acentuacao das palavras 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    item = pattern.matcher(nfdNormalizedString).replaceAll("");
	    
	      if (dicPalavras.get(item.length()) != null){		    
	        dicPalavras.get(item.length()).add(item);
	      }
	      else{
	        List<String> vazia = new ArrayList<String>();
	        dicPalavras.put(item.length(),vazia);
	        dicPalavras.get(item.length()).add(item);
	      }
    }
    //RF1: O jogador pode escolher o tamanho da palavra (o mínimo é 2 letras)    
    Scanner lerEntrada = new Scanner(System.in);
    System.out.println("Informe o tamanho da palavra, entre 2 e 23:");
    Integer tamanho = lerEntrada.nextInt();
    Integer tentativas = 1;
    boolean flag = false;
    while (flag != true){
      if (tamanho<2 || tamanho>24){
        System.out.println("Tamanho de palavra não permmitido.");
        System.out.println("Sério!? A palavra deve ter tamanho entre 2 e 23:");
        tamanho = lerEntrada.nextInt();
      } else {
        flag = true;
      }
    }
    flag = false;
    while (flag != true){
      System.out.println("Quantas tentativas você quer para adivinhar?");
      tentativas = lerEntrada.nextInt();
      if (tentativas < 1){          
        System.out.println("Você entendeu? A resposta tem que ser maior que zero!");
      } else {        
        flag = true;
      }
    }    
        
    System.out.println("....................................................");
    System.out.printf("Sorteando palavra de tamanho %d para iniciar o Jogo.\n", tamanho);
    System.out.println("....................................................");
    
    //RF2: A palavra deve ser sorteada da lista que se encontra nesse link
    Random sorteio = new Random();
    Integer countNumeroPalavras = dicPalavras.get(tamanho).size();
    Integer numSorteado = sorteio.nextInt(countNumeroPalavras);
    String palavraSorteada = dicPalavras.get(tamanho).get(numSorteado);
    Integer contarVitoria = 0;

    //palavraSorteada = "leite";//PARA TESTE: FORCA A PALAVRA
    char palavraSorteadaAnalisada[] = palavraSorteada.toCharArray();
    // System.out.printf("PARA TESTE! A palavra sorteada foi: ");
    // System.out.println(palavraSorteadaAnalisada); //print para saber a palavra e poder testar!!! Quando OK deve ser comentado ou deletado.
    
    System.out.println("::::: Jogo Iniciado, Divirta-se! :::::");
    // System.out.println("Para sair escreva 'x'");
    while (true){
      palavraSorteadaAnalisada = palavraSorteada.toCharArray(); //a cada rodada a palavra tem q ser resetada, para analisar sem o "cincos" inseridos.      

      System.out.println("Informe uma palavra:");
      String palavra = lerEntrada.next().toLowerCase();//ignora letras maiúsculas digitadas.   
      String palavraSemAcento = Normalizer.normalize(palavra, Normalizer.Form.NFD);//bloco para retirar acentuacao das palavras 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
      palavra = pattern.matcher(palavraSemAcento).replaceAll("");
      
      char palavraAnalisada[] = palavra.toCharArray();
      if (palavra.equals(palavraSorteada)){ //condição de vitória do jogo
        System.out.println("Você acertou, Parabéns!!!");   
        break;
      }
      // if (palavra.equals("x")){ break;} //condição de parada do jogo apenas durante TESTES, não está nos requisitos, deve ser REMOVIDO!
       
      //erro caso o usuário digite palavras de tamanhos diferentes do determinado no início do jogo.
      if (palavra.length() != tamanho){
        System.out.println("Tamanho errado da palavra digitada!");        
      } 
      //verifica se o usuário escreveu, de fato, uma palavra ou letras aleatórias.
      else if(!dicPalavras.get(tamanho).contains(palavra)){ 
        System.out.println("A palavra digitada não existe!");       
      } 
      //RF4: Letra descoberta que está no local correto deve ser exibida em caixa alta.
      else{
        contarVitoria = 0;
        
        String [] saida = new String[tamanho];//saida substitue resposta no codigo antigo
        for (int i=0; i<palavra.length(); i++) {//esse for procura somente match perfeito(letra e posicao iguais)
        	char letra = palavraAnalisada[i];
        	for (int j=0; j<tamanho; j++){
        		if (letra == palavraSorteadaAnalisada[j] && i==j){
        			palavraSorteadaAnalisada[i] = '8';//marca a palavra sorteada
        			palavraAnalisada[i] = '5';//marca a palavra digitada
        			saida[i]= String.valueOf(palavra.charAt(i)).toUpperCase();//saida recebe a letra na posicao correta
        		}
        	}
        }
        
        for (int i=0; i<palavra.length(); i++) {//esse for procura match imperfeito(letra fora de posicao)
        	char letra = palavraAnalisada[i];
        	for (int j=0; j<tamanho; j++){
        		if (letra == palavraSorteadaAnalisada[j]){
        			if(saida[i] == null) {//verifica se a posicao nao foi analisada ainda no for acima
        				saida[i] = String.valueOf(palavra.charAt(i));
        				palavraSorteadaAnalisada[j]='8';//marca a palavra sorteada
        				continue;
        			}
        			
        		}
        	}
        }
        //tratamento para impressao
        String resposta = "";
        for(String letra : saida) {
        	if (letra == null) {
        		resposta+="-";
        	}
        	else {
        		resposta += letra;
        	}
        	
        }
        System.out.println(resposta); 
        if (contarVitoria == tamanho){
          System.out.println("Você acertou, Parabéns!!!");
          break;          
        } 
        else{
          tentativas --;
          if (tentativas < 1){            
            System.out.println("Que pena, você perdeu!!!");            
            System.out.printf("A palavra era: %s \n", palavraSorteada);
            break;
          }          
        System.out.printf("Restam %d tentativas.\n", tentativas);
        }
      }
    }       
    System.out.printf("Fim do Jogo.");
  }
}