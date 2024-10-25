import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

public class Servidor {

	private static Socket socket;
	private static ServerSocket server;

	private static DataInputStream entrada;
	private static DataOutputStream saida;

	private int porta = 1025;

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
//-----------------------------------------------------------------------------------------------------
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
	
//----------------------------------------------------------------------------------
	public void iniciar() {
		FileReader fr = new FileReader();

		System.out.println("Servidor iniciado na porta: " + porta);
		try {

			HashMap<Integer,String> hm = new HashMap<>();
            fr.parser(hm);
            String fortunaAleatoria = fr.sorteiaFortuna(hm);

			
			// Criar porta de recepcao
			server = new ServerSocket(porta);
			socket = server.accept();  //Processo fica bloqueado, ah espera de conexoes

			// Criar os fluxos de entrada e saida
			entrada = new DataInputStream(socket.getInputStream());
			saida = new DataOutputStream(socket.getOutputStream());

			// Recebimento do valor 
			String msg = entrada.readUTF();

			// Processamento do valor
			String resultado = processarMensagem(msg, fortunaAleatoria);
			
			// Envio dos dados (resultado)
			saida.writeUTF(resultado);

			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//----------------------------------------------------------
	public String processarMensagem(String msg, String fortuna){
        try {
			String metodo = metodoUtilizado(msg);
			if("read".equals(metodo)){
				return "{\n\"result\": \"" + fortuna.trim() + "\"\n}\n"; 
			} else if("write".equals(metodo)){
				String novaFortuna = extrairArgumentos(msg);
				return "{\n\"result\": \"" + novaFortuna.trim() + "\"\n}\n"; 
			} else {
				return "{\"result\": \"false\"}\n";
			}
		} catch (Exception e){
			e.printStackTrace();
			return "{\"result\": \"false\"\n}\n";
		}
    }
	//---------------------------------------------------------
	public String extrairArgumentos(String msg){
		int argsInicio = msg.indexOf("\"args\":[\"") + 9;
		int argsFim = msg.indexOf("\"]", argsInicio);
		return msg.substring(argsInicio, argsFim);
	}
	//---------------------------------------------------------
	public String metodoUtilizado(String msg){
		int metodoInicio = msg.indexOf("\"method\":\"") + 10;
        int metodoFim = msg.indexOf("\"", metodoInicio);
        
        return msg.substring(metodoInicio, metodoFim);
	}

	public static void main(String[] args) {

		new Servidor().iniciar();

	}

}
