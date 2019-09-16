import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

public class MerkleHellmanFileReceiver
{
    private Long[] privateKey;
    private Long[] publicKey;
    private Long w;
    private Long m;
    private String liczbyPierwsze;
    private int keyLength;
    private String toSend;
    private ArrayList<Long> listOfC;
    private ArrayList<Long> listOfCPrim;
    private int lengthOfFileInByte;
    private String fileInBinary;
    private byte[] fileInBytes;



    public MerkleHellmanFileReceiver(DatagramSocket sock) throws IOException{

        initVariable();
        getFileLengthFromClient(sock);
        fileInBytes = new byte[lengthOfFileInByte];

        //Liczby pierwsze do listy
        ArrayList<Integer> listaLiczbPierwszych;
        listaLiczbPierwszych= new ArrayList<Integer>();
        listaLiczbPierwszych.add(2);
        while(listaLiczbPierwszych.get(listaLiczbPierwszych.size()-1)!= 2003)
        {
            listaLiczbPierwszych.add(Integer.valueOf(liczbyPierwsze.substring(0, liczbyPierwsze.indexOf("#")).trim()));
            liczbyPierwsze= liczbyPierwsze.substring(liczbyPierwsze.indexOf("#")+1);
        }
        ///////////////////////////////////////////////

        generatePrivateKey();
        displayPrivateKey();


        generateWMNumbers(listaLiczbPierwszych);
        displayWAndM();


        generatePublicKey();
        displayPublicKey();

    }

    private void displayPublicKey() {
        System.out.print("KLUCZ PUBLICZNY: (");
        for(Long i: publicKey)
        {
            System.out.print(i+", ");
        }
        System.out.println(").");
    }

    private void generatePublicKey() {
        for(int q=0; q<keyLength; q++)
        {
            publicKey[q]= (privateKey[q]*w)%m;
        }
    }

    private void displayWAndM() {
        System.out.println("W= "+w);
        System.out.println("M= "+m);
    }

    private void generateWMNumbers(ArrayList<Integer> listaLiczbPierwszych) {
        Random rnd= new Random();
        Long allPrivateKeyElements= new Long(0);

        for(long o:privateKey)
        {
            allPrivateKeyElements+=o;
        }

        for(int u: listaLiczbPierwszych)
        {
            if(u>allPrivateKeyElements)
            {
                m= new Long(u);
                break;
            }
        }

        w= new Long(rnd.nextInt(keyLength)+2);
        //w=new Long(7);
    }

    private void displayPrivateKey() {
        System.out.print("KLUCZ PRYWATNY: (");
        for(Long i: privateKey)
        {
            System.out.print(i+", ");
        }
        System.out.println(").");
    }

    private void generatePrivateKey() {
        long sumaElementowKluczaPrywatnego=0;
        for(int i=0; i<keyLength; i++)
        {
            Random rnd= new Random();

            if(i==0)
            {
                privateKey[i]= new Long(rnd.nextInt(2)+2);
            }
            else
            {
                privateKey[i]= rnd.nextInt(3)+sumaElementowKluczaPrywatnego+1;
            }
            sumaElementowKluczaPrywatnego+=privateKey[i];

        }
    }

    private void initVariable() {
        liczbyPierwsze= "3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97 101 103 107 " +
                "109 113 127 131 137 139 149 151 157 163 167 173 179 181 191 193 197 199 211 223 227 229 233 239 241 " +
                "251 257 263 269 271 277 281 283 293 307 311 313 317 331 337 347 349 353 359 367 373 379 383 389 397 " +
                "401 409 419 421 431 433 439 443 449 457 461 463 467 479 487 491 499 503 509 521 523 541 547 557 563 " +
                "569 571 577 587 593 599 601 607 613 617 619 631 641 643 647 653 659 661 673 677 683 691 701 709 719 " +
                "727 733 739 743 751 757 761 769 773 787 797 809 811 821 823 827 829 839 853 857 859 863 877 881 883 " +
                "887 907 911 919 929 937 941 947 953 967 971 977 983 991 997 1009 1013 1019 1021 1031 1033 1039 1049 " +
                "1051 1061 1063 1069 1087 1091 1093 1097 1103 1109 1117 1123 1129 1151 1153 1163 1171 1181 1187 1193 " +
                "1201 1213 1217 1223 1229 1231 1237 1249 1259 1277 1279 1283 1289 1291 1297 1301 1303 1307 1319 1321 " +
                "1327 1361 1367 1373 1381 1399 1409 1423 1427 1429 1433 1439 1447 1451 1453 1459 1471 1481 1483 1487 " +
                "1489 1493 1499 1511 1523 1531 1543 1549 1553 1559 1567 1571 1579 1583 1597 1601 1607 1609 1613 1619 " +
                "1621 1627 1637 1657 1663 1667 1669 1693 1697 1699 1709 1721 1723 1733 1741 1747 1753 1759 1777 1783 " +
                "1787 1789 1801 1811 1823 1831 1847 1861 1867 1871 1873 1877 1879 1889 1901 1907 1913 1931 1933 1949 " +
                "1951 1973 1979 1987 1993 1997 1999 2003 ";
        //Zmiana sk³adni Stringu z liczbami pierwszymi
        liczbyPierwsze= liczbyPierwsze.replace(' ','#' );

        privateKey= new Long[8];
        publicKey= new Long[8];
        keyLength= 8;
        listOfC= new ArrayList<Long>();
        listOfCPrim= new ArrayList<Long>();


    }


    public void prepareStringToSend()
    {
        StringBuilder sb= new StringBuilder();

        sb.append(keyLength+"^");
        for(long y: publicKey)
        {
            sb.append(y+"&&");
        }

        sb.append("#"+ w+"$"+ m+"@");
        toSend= sb.toString();
    }

    public void sendStringData(DatagramSocket sock, InetAddress host, int port)
    {
        try{
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream( bout );
            pout.print(toSend);
            byte[] barray = bout.toByteArray();
            DatagramPacket dp1= new DatagramPacket(barray, barray.length, host, port);
            sock.send(dp1);
            System.out.println("Wys³ano do klienta: "+ toSend);
        }catch(IOException ex){
            System.out.print("IOException: "+ex.getMessage());
        }
    }

    public void getFileLengthFromClient(DatagramSocket sock) throws IOException {
        //String lengthOfFileInByte;
        byte[] bufferLength= new byte[1024];
        DatagramPacket incoming1= new DatagramPacket(bufferLength, bufferLength.length);
        sock.receive(incoming1);
        byte[] data1= incoming1.getData();

        lengthOfFileInByte = Integer.valueOf(new String(data1, 0, data1.length).trim());


        System.out.println("Odebrano dane z serwera: D³ugoœæ pliku: "+ lengthOfFileInByte+"bajtów.");

    }

    public void getAllCFromClient(DatagramSocket sock) throws IOException
    {
        int i=0;
        StringBuilder strB= new StringBuilder();

        while(i<Integer.valueOf(lengthOfFileInByte))
        {

            byte[] bufferLength1= new byte[1024];
            DatagramPacket incoming2= new DatagramPacket(bufferLength1, bufferLength1.length);
            sock.receive(incoming2);
            byte[] data2= incoming2.getData();

            String C= new String(data2, 0, data2.length).trim();
            strB.append(C);

            listOfC.add(Long.valueOf(C.trim()));
            i++;


        }
        if(i== Integer.valueOf(lengthOfFileInByte))
        {
            System.out.println("Odebrano kompletne dane");
        }
        else
        {
            System.out.println("B£¥D! Odebrano niekompletne dane");
        }
    }

    public void displayAllC()
    {
        System.out.print("Szyfrogramy (C): ");
        for(Long u: listOfC)
        {
            System.out.print(u+", ");
        }
    }

    public void calculateCPrimNumbers()
    {
        long wPrim=0;
        long a= m/w;

        while(true)
        {
            if(((w*a)% m)==1)
            {
                wPrim= a;
                break;
            }
            a++;
        }
        System.out.println("\nOdwrotnoœæ w="+wPrim);

        for(int op=0; op<listOfC.size();op++)
        {
            listOfCPrim.add((listOfC.get(op)*wPrim)% m);
        }
    }

    public void displayAllCPrime()
    {
        System.out.print("Lista liczb C': ");
        for(Long yy: listOfCPrim)
        {
            System.out.print(yy+", ");
        }
    }

    public void cPrimeToBits()
    {
        StringBuilder bitsToFile=new StringBuilder();
        int errorNumber=0;
        for(Long curCPrim: listOfCPrim)
        {

            for(int y=0; y<=255; y++)
            {

                String s= Integer.toString(y&0xff, 2);
                if(s.length()<8)
                {
                    StringBuilder sb2= new StringBuilder();
                    for(int p=0; p<8-s.length(); p++)
                    {
                        sb2.append("0");
                    }
                    sb2.append(s);
                    s= sb2.toString();
                }


                Long result= new Long(0);
                for(int j=0; j<privateKey.length; j++)
                {
                    int lo= Integer.valueOf(s.substring(j, j+1).trim());
                    result+= privateKey[j]*lo;
                }
                if(result.equals(curCPrim))
                {

                    bitsToFile.append(s);
                    y=256;
                }
                if(y==255)
                {
                    errorNumber++;
                    System.out.println(curCPrim);

                }


            }


        }
        if(errorNumber>0)
            System.out.println("\nNIE ZNALEZIONO UK£ADu BITÓW DLA: "+errorNumber+"liczb!!");

        fileInBinary= bitsToFile.toString();

        System.out.println("\nPlik w bitach: "+fileInBinary);
        System.out.println("Iloœæ bitów: "+fileInBinary.length());
    }

    public void bitsToByteTable()
    {
        for(int y = 0; y< fileInBytes.length; y++)
        {
            //fileInBytes[y]= Byte.parseByte(fileInBinary.substring(y*8, (y*8)+8).trim(),2);
            fileInBytes[y]= (byte)Integer.parseInt(fileInBinary.substring(y*8, (y*8)+8).trim(),2);
        }
    }

    public void displayFileInByte()
    {
        System.out.print("Plik w bajtach: ");
        for(byte b: fileInBytes)
        {
            System.out.print(b+",");
        }
    }

    public void createFileFromByteTable(String filePath) {

        try{
            File file = new File(filePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(fileInBytes);
            fos.close();
            System.out.println("\nPomyœlnie utworzono plik. (Rozmiar: "+fileInBytes.length+" bajtów)");
        }catch(IOException e){
            System.out.println("IOException: "+e.getMessage());
        }
    }

    public Long[] getPrivateKey() {
        return privateKey;
    }

    public Long[] getPublicKey() {
        return publicKey;
    }

    public Long getW() {
        return w;
    }

    public Long getM() {
        return m;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }

    public String getFileInBinary() {
        return fileInBinary;
    }

    public void setFileInBinary(String fileInBinary) {
        this.fileInBinary = fileInBinary;
    }

    public byte[] getFileInBytes() {
        return fileInBytes;
    }

    public void setFileInBytes(byte[] fileInBytes) {
        this.fileInBytes = fileInBytes;
    }

}