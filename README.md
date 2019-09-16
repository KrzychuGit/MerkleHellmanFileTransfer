# MerkleHellmanFileTransfer
File sending using UDP protocol and Merkle Hellman encryption.

This is a simply implementation of Merkle Hellman encryption method.


<h1>Instruction:</h1>

<b>To encrypt and send file you must:</b>
1. Import MerkleHellmanFileSender.class file to your project.
2. Create new instance of this class:
  <i>MerkleHellmanFileSender mhfs= new MerkleHellmanFileSender();</i>
3. Call methods:
  prepareFileToSend(String filePath),
  sendLengthOfFileToSerwer(DatagramSocket socket, String host, int sendPort),
  getStringDataFormSerwer(int receivePort),
  byteTableToBits(),
  merkleHellman.calculateCNumbersAndSendToSerwer(DatagramSocket socket, String host, int sendPort).

<b>To receive and decrypt file you must:</b>
1.Import MerkleHellmanFileReceiver.class file to your project.
2. Create new instance of this class:
  <i>MerkleHellmanFileReceiverr mhfs= new MerkleHellmanFileReceiver(DatagramSocket socket);</i>
3. Call methods:
  prepareStringToSend(),
  sendStringData(DatagramSocket socket, String destinationHost, int sendPort),
  getAllCFromClient(DatagramSocket socket),
  displayAllC(),
  calculateCPrimNumbers(),
  cPrimeToBits(),
  bitsToByteTable(),
  createFileFromByteTable(String filePath).
  
  
