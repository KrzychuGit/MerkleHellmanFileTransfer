import java.io.*;
import java.net.*;


public class Serwer {


    public static void main(String[] args)
    {
        String filePath= "/home/student/merkle_hellman_serwer/plik.txt";
        int receivePort= 45000;
        Serwer serwer= new Serwer();
        DatagramSocket socket= null;

        try {
            socket= new DatagramSocket(receivePort);
            System.out.println("Oczekiwanie na dane od klienta");



            MerkleHellmanFileReceiver merkleHellman =new MerkleHellmanFileReceiver(socket);

            enterToContinue();

            merkleHellman.prepareStringToSend();
            InetAddress destinationHost= InetAddress.getByName("localhost");
            int sendPort= 45001;
            merkleHellman.sendStringData(socket, destinationHost,sendPort);


            merkleHellman.getAllCFromClient(socket);
            merkleHellman.displayAllC();

            merkleHellman.calculateCPrimNumbers();
            merkleHellman.displayAllCPrime();


            merkleHellman.cPrimeToBits();
            merkleHellman.bitsToByteTable();
            merkleHellman.displayFileInByte();

            merkleHellman.createFileFromByteTable(filePath);
        }
        catch(IOException e) {
            System.err.println("IOException " + e);
        }
    }

    private static void enterToContinue() throws IOException {
        BufferedReader cin= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Naciœnij ENTER aby przejœæ dalej:...\n");
        cin.readLine();
    }







}
