
/**
 * Laboratorio 1 de Sistemas Distribuidos
 * 
 * Autor: Erik Noda e Thiago Berto
 * Ultima atualizacao: 25/10/2024
 */

 import java.io.BufferedInputStream;
 import java.io.BufferedReader;
 import java.io.DataInputStream;
 import java.io.DataOutputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.FileReader;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.net.Socket;
 import java.nio.file.Path;
 import java.nio.file.Paths;
 import java.util.HashMap;
 import java.util.Random;
 
 import javax.print.DocFlavor.STRING;
 
 public class Cliente {
     
     private static Socket socket;
     private static DataInputStream entrada;
     private static DataOutputStream saida;
     
     private int porta=1025;
 
     public final static Path path = Paths.get("lista1\\Lab2\\fortune-br.txt");
     public class FileReader{
 //------------------------------------------------------------------------------------------------
         public String sorteiaFortuna(HashMap<Integer, String> hm) throws FileNotFoundException{
             int alcance = hm.size();
                 Random aleatorio = new Random();
                 int min = 0;
                 int max = alcance;
 
                 int index = aleatorio.nextInt((max-min)+1) + min;
                 //System.out.println("INDEX " + index);
                 if(hm.containsKey(index)){
                     
                     // System.out.println(hm.get(index));
                     String fortuna = hm.get(index);
                     return fortuna;
                 }else{
                     return("Erro");
                 }
 
         }    
     //------------------------------------------------------------------------------------------------    
         public String montarJson(String metodo, String [] args) throws FileNotFoundException{
             StringBuilder json = new StringBuilder();
             json.append("{\n");
             json.append("\"method\":\"").append(metodo).append("\",\n");
             json.append("\"args\":[");
             for(int i=0; i<args.length; i++){
                 json.append("\"").append(args[i]).append("\"");
                 if(i<args.length-1){
                     json.append(",");
                 }
             }
             json.append("]\n");
             json.append("}\n");
             return json.toString();
         }
     
 //------------------------------------------------------------------------------------------------
     public void parser(HashMap<Integer, String> hm) throws FileNotFoundException {
     
         InputStream is = new BufferedInputStream(new FileInputStream(path.toString()));
         try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
 
             int lineCount = 0;
 
             String line = "";
             while (!(line == null)) {
 
                 if (line.equals("%"))
                     lineCount++;
 
                 line = br.readLine();
                 StringBuffer fortune = new StringBuffer();
                 while (!(line == null) && !line.equals("%")) {
                     fortune.append(line + "\n");
                     line = br.readLine();
                     // System.out.print(lineCount + ".");
                 }
 
                 hm.put(lineCount, fortune.toString());
                 //System.out.println(fortune.toString());
 
                 //System.out.println(lineCount);
             }// fim while
 
         } catch (IOException e) {
             System.out.println("SHOW: Excecao na leitura do arquivo.");
         }
     }
 }
 //------------------------------------------------------------------------------------------------
     public void iniciar(){
         FileReader fr = new FileReader();
 
         System.out.println("Cliente iniciado na porta: "+porta);
         
         try {
             
             HashMap<Integer,String> hm = new HashMap<>();
             fr.parser(hm);
             String fortunaAleatoria = fr.sorteiaFortuna(hm);
 
 
             socket = new Socket("127.0.0.1", porta);    
             entrada = new DataInputStream(socket.getInputStream());
             saida = new DataOutputStream(socket.getOutputStream());
             
 
             //Recebe do usuario algum valor
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
             System.out.println("Menu: ");
             System.out.println("1 -- leitura");
             System.out.println("2 -- escrita");
             
             int valor = Integer.parseInt(br.readLine());
             
             if(valor==1){
                 String msg = fr.montarJson("read", new String[]{});
                 System.out.println("---CLIENTE---");
                 System.out.println(msg);
                 //O valor eh enviado ao servidor
                 saida.writeUTF(msg);
 
             }else if(valor==2){
                 String msg =fr.montarJson("write", new String[]{fortunaAleatoria});
                 System.out.println("---CLIENTE---");
                 System.out.println(msg);
                 //O valor eh enviado ao servidor
                 saida.writeUTF(msg);
 
             }else{
                 System.out.println("Comando inexistente");
             }
 
             
             //Recebe-se o resultado do servidor
             String resultado = entrada.readUTF();
             
             //Mostra o resultado na tela
             
             System.out.println("---SERVIDOR---");
             System.out.println(resultado);
             
 
             socket.close();
             
         } catch(Exception e) {
             e.printStackTrace();
         }
     }
     
     public static void main(String[] args) {
         new Cliente().iniciar();
     }
     
 }
 