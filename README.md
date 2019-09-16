# MerkleHellmanFileTransfer
File sending using UDP protocol and Merkle Hellman encryption.

This is a simply implementation of Merkle Hellman encryption method.

<br/><br/>
<h2>Instruction:</h2>

<b>To encrypt and send file you must:</b>
1. Import MerkleHellmanFileSender.class file to your project.<br/>
2. Create new instance of this class:<br/>
  <i>MerkleHellmanFileSender mhfs= new MerkleHellmanFileSender();</i><br/>
3. Call methods:<br/>
  prepareFileToSend(String filePath),<br/>
  sendLengthOfFileToSerwer(DatagramSocket socket, String host, int sendPort),<br/>
  getStringDataFormSerwer(int receivePort),<br/>
  byteTableToBits(),<br/>
  merkleHellman.calculateCNumbersAndSendToSerwer(DatagramSocket socket, String host, int sendPort).<br/>

<b>To receive and decrypt file you must:</b><br/>
1. Import MerkleHellmanFileReceiver.class file to your project.<br/>
2. Create new instance of this class:<br/>
  <i>MerkleHellmanFileReceiverr mhfs= new MerkleHellmanFileReceiver(DatagramSocket socket);</i><br/>
3. Call methods:<br/>
  prepareStringToSend(),<br/>
  sendStringData(DatagramSocket socket, String destinationHost, int sendPort),<br/>
  getAllCFromClient(DatagramSocket socket),<br/>
  calculateCPrimNumbers(),<br/>
  cPrimeToBits(),<br/>
  bitsToByteTable(),<br/>
  createFileFromByteTable(String filePath).<br/>
  
  
