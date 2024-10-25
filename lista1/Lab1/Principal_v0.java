/**
 * Lab0: Leitura de Base de Dados Não-Distribuida
 * 
 * Autor: Lucio A. Rocha
 * Ultima atualizacao: 20/02/2023
 * 
 * Referencias: 
 * https://docs.oracle.com/javase/tutorial/essential/io
 * 
 */

 import java.io.*;
 import java.nio.file.*;
 import static java.nio.file.StandardCopyOption.*;

import java.security.SecureRandom;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Map;
 import java.util.Scanner;
 import java.util.Set;
 import java.util.Random;
//---------------------------------------------------------------------------------------------------
 public class Principal_v0 {
 
     public final static Path path = Paths.get("lista1\\Lab1\\fortune-br.txt");
     private int NUM_FORTUNES = 0;
 
     public class FileReader {
//--------------------------------------------------------------------------------------------------- 
         public int countFortunes() throws FileNotFoundException {
 
             int lineCount = 0;
 
             InputStream is = new BufferedInputStream(new FileInputStream(path.toString()));
             try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                 String line = "";
                 while (!(line == null)) {
 
                     if (line.equals("%"))
                         lineCount++;
 
                     line = br.readLine();
 
                 }// fim while
 
                 System.out.println(lineCount);
             } catch (IOException e) {
                 System.out.println("SHOW: Excecao na leitura do arquivo.");
             }
             return lineCount;
         }
//--------------------------------------------------------------------------------------------------- 
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
                     System.out.println(fortune.toString());
 
                     System.out.println(lineCount);
                 }// fim while
 
             } catch (IOException e) {
                 System.out.println("SHOW: Excecao na leitura do arquivo.");
             }
         }
//--------------------------------------------------------------------------------------------------- 
         public void read(HashMap<Integer, String> hm) throws FileNotFoundException {
            //SEU CODIGO AQUI
            int alcance = hm.size();
            Random aleatorio = new Random();
            int min = 0;
            int max = alcance;

            int index = aleatorio.nextInt((max-min)+1) + min;
            System.out.println("INDEX " + index);
            if(hm.containsKey(index)){
                
                System.out.println(hm.get(index));
            }

         }
//--------------------------------------------------------------------------------------------------- 
         public void write(HashMap<Integer, String> hm) throws FileNotFoundException {
             //SEU CODIGO AQUI
             String newFortune = "Hello World! ";
         
              try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.write(newFortune);
                writer.newLine();
                writer.write("%");
                writer.newLine();
            } catch (IOException e) {
                System.out.println("SHOW: Exceção na escrita do arquivo.");
            }
         }
     }
//---------------------------------------------------------------------------------------------------
     public void iniciar() {
 
         FileReader fr = new FileReader();
         try {
             NUM_FORTUNES = fr.countFortunes();
             HashMap hm = new HashMap<Integer, String>();
             fr.parser(hm);
             fr.read(hm);
             fr.write(hm);

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
 
     }
//--------------------------------------------------------------------------------------------------- 
     public static void main(String[] args) {
         new Principal_v0().iniciar();
     }
 
 }