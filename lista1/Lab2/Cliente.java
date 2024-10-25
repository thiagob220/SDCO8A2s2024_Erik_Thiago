
/**
 * Laboratorio 1 de Sistemas Distribuidos
 * 
 * Autor: Lucio A. Rocha
 * Ultima atualizacao: 17/12/2022
 */

 import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
 import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
 import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;

 public class Cliente {
     
     private static Socket socket;
     private static DataInputStream entrada;
     private static DataOutputStream saida;
     
     private int porta=1025;
     public final static Path path = Paths.get("lista1\\Lab2\\fortune-br.txt");

//--------------------------------------------------------------------------------------------------- 
         public String read(HashMap<Integer, String> hm) throws FileNotFoundException {
            int alcance = hm.size();
            Random aleatorio = new Random();
            int min = 0;
            int max = alcance;
            

            int index = aleatorio.nextInt((max-min)+1) + min;
            System.out.println("INDEX " + index);
            if(hm.containsKey(index)){
                
                System.out.println(hm.get(index));
            }
            return hm.get(index);

         }
//--------------------------------------------------------------------------------------------------- 
         public String write(HashMap<Integer, String> hm) throws FileNotFoundException {
             String newFortune = read(hm);
             String resultado = "";
         
              try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                resultado = "result: " + newFortune;

            } catch (IOException e) {
                System.out.println("SHOW: Exceção na escrita do arquivo.");
            }
            return resultado;
         }
     
    public String montarJson(String metodo, String[] args){
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"method\":\"").append(metodo).append("\",");
        json.append("\"args\":[");
        for(int i=0; i<args.length; i++){
            json.append("\"").append(args[i]).append("\"");
            if(i<args.length-1){
                json.append(",");
            }
        }
        json.append("]");
        json.append("}\n");
        return json.toString();
    }
     
    public String menu(Hashmap<Integer, String> hm) throws FileNotFoundException{
        
        System.out.println("Menu de usuario:\n");
        System.out.println("1 -- leitura do arquivo\n");
        System.out.println("2 -- escrita do arquivo\n");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //Scanner scan = new Scanner(System.in);
        int opcao = Integer.parseInt(br.readLine()); 

        
        if (opcao==1) {
            String json = montarJson("read", new String[]{});
            return json;
               
        }else if(opcao==2){
            String fortuna = write(hm);
            String json = montarJson("write", new String[]{fortuna});
            return json;
        }else{
            System.out.println("Opcao invalida");
            return null;
        }

    } 


     public void iniciar(){
         System.out.println("Cliente iniciado na porta: "+porta);
         
         HashMap hm = new HashMap<Integer, String>();
         try {
             
             socket = new Socket("127.0.0.1", porta);
             
             entrada = new DataInputStream(socket.getInputStream());
             saida = new DataOutputStream(socket.getOutputStream());
             
             //Recebe do usuario algum valor
             //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //  System.out.println("Digite um numero: ");
            //  int valor = Integer.parseInt(br.readLine());
            
            String msg = menu(hm);

             //O valor eh enviado ao servidor
            saida.writeUTF(msg);
             
             //Recebe-se o resultado do servidor
             String resultado = entrada.readUTF();
             
             //Mostra o resultado na tela
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