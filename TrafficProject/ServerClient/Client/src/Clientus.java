import java.io.*;
import java.net.*;

public class Clientus {
    public static void main(String[] args) throws IOException {
    	
    	String hostName = "localhost"; // pentru testele locale folosesti local host. cand o sa avem serveru tinut sus o sa ai IP
    	
    	try (
                Socket echoSocket = new Socket(hostName, 4040);
                PrintWriter out =
                    new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                    new BufferedReader(
                        new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                    new BufferedReader(
                        new InputStreamReader(System.in))
            ) {
                String userInput;
                while (true) {
                	//user input ii standard in, out.println primeste ce trimiti la server, in.readLine iti da ce iti trimite serveru
                	userInput = stdIn.readLine();
                    out.println(userInput);
                    if(userInput.toLowerCase().contains("close")) break; //daca primeste close se inchide conexiunea
                    System.out.println("echo: " + in.readLine());
                }
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + hostName);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
                System.exit(1);
            } 
    	System.out.println("Closing client...");
    }
}
