import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class MerkleHellmanFileSender {

    private long[] publicKey;
    private long w, m;
    private File fileToSend;
    private int fileInBytesLength;
    private byte[] fileInBytes;
    private String fileInBits;
    ArrayList<Long> listOfC;



    public MerkleHellmanFileSender() {

    }

    public void prepareFileToSend(String filePath)throws IOException
    {
        fileToSend= new File(filePath);
        if(!fileToSend.exists())
        {
            fileToSend.createNewFile();
            System.out.println("Nie znaleziono pliku! Utworzono nowy.");
        }
        FileInputStream file= new FileInputStream(fileToSend);

        fileInBytesLength = file.available();

        fileInBytes = new byte[fileInBytesLength];
        file.read(fileInBytes);

    }

    public void byteTableToBits()
    {
        StringBuilder bits= new StringBuilder();
        StringBuilder sb= new StringBuilder();
        for(byte byt: fileInBytes)
        {
            String s= Integer.toString(byt&0xff,2);
            //String s = String.format("%8s", Integer.toBinaryString(byt & 0xFF)).replace(' ', '0');
            if(s.length()!=8)

                for(int r=0; r<8-s.length(); r++)
                {
                    sb.append("0");
                }
            sb.append(s);
            s= sb.toString();
            bits.append(s);
            sb=new StringBuilder();
        }
        fileInBits= bits.toString().trim();
    }

    public void displayInfoAboutFile()
    {
        System.out.println("\nInfo o pliku:");
        System.out.println("==================");
        System.out.print("Plik w bajtach: ");
        for(byte u: fileInBytes)
        {
            System.out.print(u+",");
        }
        System.out.println("\nPlik w bitach: "+fileInBits);
        System.out.println("Iloœæ bajtów="+ fileInBytesLength);
        System.out.println("Iloœæ bitów="+fileInBits.length());
        System.out.println("==================");

    }

    public void calculateCNumbersAndSendToSerwer(DatagramSocket socket, InetAddress host, int sendPort)throws IOException
    {
        long C=0;
        int currentBit=0;
        int keyIterator=1;
        int loopIterator= 1;
        //int numbOfC=0;
        listOfC= new ArrayList<Long>();

        while(loopIterator*(keyIterator)<=fileInBits.length())
        {
            currentBit=Integer.valueOf(fileInBits.substring((keyIterator-1)+((loopIterator-1)*8), ((keyIterator-1)+((loopIterator-1)*8))+1).trim());
            C+= publicKey[keyIterator-1]*currentBit;
            keyIterator++;
            if(keyIterator==publicKey.length+1)
            {
                listOfC.add(C);

                //Wys³anie szyfrogramu
                ByteArrayOutputStream bout1 = new ByteArrayOutputStream();
                PrintStream pout1 = new PrintStream( bout1 );
                pout1.print(C);
                byte[] barray1 = bout1.toByteArray();
                DatagramPacket dp2  = new DatagramPacket(barray1, barray1.length, host, sendPort);
                socket.send(dp2);
                ////////////////////////////////

                if(loopIterator*(keyIterator-1)>=fileInBits.length())
                {
                    break;
                }

                loopIterator++;

                C=0;
                keyIterator=1;
            }

        }



        if(loopIterator*8== fileInBits.length())
        {
            System.out.println("Przes³ano wszystkie dane.");
        }
        else
        {
            System.out.println("Przes³ano nieprawid³ow¹ iloœæ danych!");
        }
    }


    public void sendLengthOfFileToSerwer(DatagramSocket socket, InetAddress host, int sendPort)throws IOException
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream( bout );
        pout.print(fileInBytesLength);
        byte[] barray = bout.toByteArray();
        DatagramPacket dp1= new DatagramPacket(barray, barray.length, host, sendPort);
        socket.send(dp1);
        System.out.println("Wys³ano iloœæ bajtów w pliku: "+ fileInBytesLength);
    }


    public void getStringDataFormSerwer(int receivePort) throws IOException
    {
        DatagramSocket sock2= new DatagramSocket(receivePort);//45001
        byte[] bufferLength= new byte[1024];
        DatagramPacket incoming1= new DatagramPacket(bufferLength, bufferLength.length);
        sock2.receive(incoming1);
        byte[] data1= incoming1.getData();

        String receiveData= new String(data1, 0, data1.length).trim();
        System.out.println("Z serwera odebrano:"+receiveData);


        w= Integer.valueOf(receiveData.substring(receiveData.lastIndexOf('#')+1, receiveData.indexOf('$')).trim());
        m= Integer.valueOf(receiveData.substring(receiveData.lastIndexOf('$')+1, receiveData.indexOf('@')).trim());

        int numberOfElements= Integer.valueOf(receiveData.substring(0, receiveData.indexOf('^')));
        //numberOfElements= numberOfElements;
        receiveData= receiveData.substring(receiveData.indexOf('^')+1);
        publicKey= new long[numberOfElements];


        System.out.println("D³ugoœæ klucza= "+ numberOfElements);

        for(int p=0; p<numberOfElements; p++) {
            publicKey[p] =  Long.valueOf( receiveData.substring(0, receiveData.indexOf('&')).trim());
            receiveData= receiveData.substring(receiveData.indexOf('&'));
            String s= receiveData.substring(0,1);
            while(s.equals("&"))
            {
                receiveData= receiveData.substring(1);
                s= receiveData.substring(0,1);
            }
        }
        System.out.println("Klucz publiczny: ");
        for(long v:publicKey)
        {
            System.out.print(v+", ");
        }
        System.out.println("\n");

        System.out.println("W= "+w);
        System.out.println("M= "+m);
    }


    public void displayAllC()
    {
        System.out.print("Szyfrogramy(C): (");
        for(Long c: listOfC)
        {
            System.out.print(c+",");
        }
        System.out.println(")");
    }

    public long[] getPublicKey() {
        return publicKey;
    }

    public long getW() {
        return w;
    }

    public long getM() {
        return m;
    }

    public File getFileToSend() {
        return fileToSend;
    }

    public int getFileInBytesLength() {
        return fileInBytesLength;
    }

    public byte[] getFileInBytes() {
        return fileInBytes;
    }

    public String getFileInBits() {
        return fileInBits;
    }
}
