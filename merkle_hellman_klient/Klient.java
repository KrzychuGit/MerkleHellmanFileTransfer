import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Klient {

    public static void main(String[] args)
    {
        int sendPort= 45000;
        int receivePort= 45001;
        InetAddress host;
        String filePath= "/home/student/merkle_hellman_klient/plik.txt";

        try {

            host= InetAddress.getByName("localhost");

            DatagramSocket socket= null;
            socket= new DatagramSocket();

            MerkleHellmanFileSender merkleHellman= new MerkleHellmanFileSender();

            merkleHellman.prepareFileToSend(filePath);

            merkleHellman.sendLengthOfFileToSerwer(socket, host, sendPort);

            merkleHellman.getStringDataFormSerwer(receivePort);

            enterToContinue();

            merkleHellman.byteTableToBits();

            merkleHellman.displayInfoAboutFile();

            merkleHellman.calculateCNumbersAndSendToSerwer(socket, host, sendPort);

            merkleHellman.displayAllC();


        }
        catch(IOException e) {
            System.err.println("IOException " + e);
        }
    }

    private static void enterToContinue() throws IOException {
        BufferedReader cin= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter:...\n");
        cin.readLine();
    }

}
