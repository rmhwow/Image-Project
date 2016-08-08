///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program 2
// Files:            Receiver.java, PacketLinkedList.java, 
//                   PacketLinkedListIterator.java, BadImageHeaderException.java
//                   BrokenImageContentException.java, 
// Semester:         (CS 367) Spring 2016
//
// Author:           Jefferson Killian
// Email:            jdkillian@wisc.edu
// CS Login:          jefferson
// Lecturer's Name:  Lecture 3
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
//                   CHECK ASSIGNMENT PAGE TO see IF PAIR-PROGRAMMING IS ALLOWED
//                   If pair programming is allowed:
//                   1. Read PAIR-PROGRAMMING policy (in cs302 policy) 
//                   2. choose a partner wisely
//                   3. REGISTER THE TEAM BEFORE YOU WORK TOGETHER 
//                      a. one partner creates the team
//                      b. the other partner must join the team
//                   4. complete this section for each program file.
//
// Pair Partner:     Morgan O'Leary
// Email:            oleary@wisc.edu
// CS Login:         o-leary
// Lecturer's Name:  Lecture 3
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.IOException;
import java.util.Iterator;
/**
 * The main class. It simulates a application (image viewer) receiver by
 * maintaining a list buffer. It collects packets from the queue of 
 * InputDriver and arrange them properly, and then reconstructs the image
 * file from its list buffer.
 * @author jefferson & o-leary
 */
public class Receiver {
	private InputDriver input;
	private ImageDriver img;
	//declare list variable	
	private PacketLinkedList<SimplePacket> list;


	/**
	 * Constructs a Receiver to obtain the image file transmitted.
	 * @param file the filename you want to receive
	 */
	public Receiver(String file) {
		try {
			input = new InputDriver(file, true);
		} catch (IOException e) {
			System.out.println(
					"The file, " + file + ", isn't existed on the server.");
			System.exit(0);
		}
		img = new ImageDriver(input);
		//initialize list as a new linked list
		list = new PacketLinkedList<SimplePacket>();
	}

	/**
	 * Returns the PacketLinkedList buffer in the receiver
	 * 
	 * @return the PacketLinkedList object
	 */
	public PacketLinkedList<SimplePacket> getListBuffer() {
		return list;
	}

	/**
	 * Asks for retransmitting the packet. The new packet with the sequence 
	 * number will arrive later by using {@link #askForNextPacket()}.
	 * Notice that ONLY packet with invalid checksum will be retransmitted.
	 * 
	 * @param pkt the packet with bad checksum
	 * @return true if the requested packet is added in the receiving queue; otherwise, false
	 */
	public boolean askForRetransmit(SimplePacket pkt) {
		return input.resendPacket(pkt);
	}


	/**
	 * Asks for retransmitting the packet with a sequence number. 
	 * The requested packet will arrive later by using 
	 * {@link #askForNextPacket()}. Notice that ONLY missing
	 * packet will be retransmitted. Pass seq=0 if the missing packet is the
	 * "End of Streaming Notification" packet.
	 * 
	 * @param seq the sequence number of the requested missing packet
	 * @return true if the requested packet is added in the receiving queue; otherwise, false
	 */
	public boolean askForMissingPacket(int seq) {
		return input.resendMissingPacket(seq);
	}

	/**
	 * Returns the next packet.
	 * 
	 * @return the next SimplePacket object; returns null if no more packet to
	 *         receive
	 */
	public SimplePacket askForNextPacket() {
		return input.getNextPacket();
	}

	/**
	 * Returns true if the maintained list buffer has a valid image content. Notice
	 * that when it returns false, the image buffer could either has a bad
	 * header, or just bad body, or both.
	 * 
	 * @return true if the maintained list buffer has a valid image content;
	 *         otherwise, false
	 */
	public boolean validImageContent() {
		return input.validFile(list);
	}

	/**
	 * Returns if the maintained list buffer has a valid image header
	 * 
	 * @return true if the maintained list buffer has a valid image header;
	 *         otherwise, false
	 */
	public boolean validImageHeader() {
		return input.validHeader(list.get(0));
	}

	/**
	 * Outputs the formatted content in the PacketLinkedList buffer. If the 
	 * coming in is corrupted, it will display with surrounding brackets, 
	 * otherwise it will have a comma and space after
	 */
	public void displayList() {

		Iterator<SimplePacket> itr = list.iterator();

		while(itr.hasNext()) {
			SimplePacket packet = itr.next();
			if(!packet.isValidCheckSum()){
				System.out.print("[" + packet.getSeq() + "], ");
			}else{
				System.out.print(packet.getSeq() + ", ");
			}
		}
	}

	/**
	 * Reconstructs the file by arranging the {@link PacketLinkedList} in
	 * correct order. It uses {@link #askForNextPacket()} to get packets until
	 * no more packet to receive. It eliminates the duplicate packets and asks
	 * for retransmitting when getting a packet with invalid checksum.
	 */
	public void reconstructFile() {
		//add to list
		boolean addPacket = true; //remains true until EOS notification received
		int numPacks = 0; //number of packets in list

		//while we keep adding to the list
		while(addPacket) {

			SimplePacket pack = askForNextPacket(); // packet to add to list
			if(pack == null){
				addPacket = false;
			}
			//if packet is not null, check if valid 			
			else {
				if(pack.isValidCheckSum()) {
					//if packet sequence is negative, assign the positive value
					//to the number of packets
					if(pack.getSeq() < 0){
						numPacks = (-1) * pack.getSeq();
					}
					else{ 
						list.add(pack);
					}
				}
				else{ 
					askForRetransmit(pack);
				}
			}
		}


		//sort 
		for (int i = list.size() - 1; i >= 0; i--) {
			for (int j = 1; j <= i; j++) {
				if(list.get(j-1).getSeq() > list.get(j).getSeq()) {
					SimplePacket tmp = list.remove(j - 1);
					list.add(j, tmp);
				}
			}
		}	

		//delete duplicates
		for (int i = list.size() - 1; i > 0; i--) {
			if(list.get(i).getSeq() == list.get(i - 1).getSeq()){
				list.remove(i - 1);
			}
		}

		//process missing packets for other four images
		while (numPacks == 0) {
			askForMissingPacket(0);
			SimplePacket eos = askForNextPacket();
			if (eos.isValidCheckSum())
				numPacks = (-1) * eos.getSeq();
		}

		for (int i = 0; i < numPacks; i++) { 
			//add missing packets
			if (list.get(i).getSeq() != i + 1) {
				askForMissingPacket(i + 1);
				SimplePacket missingPack = askForNextPacket();
				//missing packet to add
				if(missingPack != null){
					while (!missingPack.isValidCheckSum()) {
						askForRetransmit(missingPack);
						missingPack = askForNextPacket();
					}
					list.add(i, missingPack);
				}
			}
		}
		while(addPacket) {

			SimplePacket pack = askForNextPacket(); // packet to add to list
			if(pack == null){
				addPacket = false;
			}
			//if packet is not null, check if valid 			
			else {
				if(pack.isValidCheckSum()) {
					//if packet sequence is negative, assign the positive value
					//to the number of packets
					if(pack.getSeq() < 0){
						numPacks = (-1) * pack.getSeq();
					}
					else{ 
						list.add(pack);
					}
				}
				else{ 
					askForRetransmit(pack);
				}
			}
		}


		//sort 
		for (int i = list.size() - 1; i >= 0; i--) {
			for (int j = 1; j <= i; j++) {
				if(list.get(j-1).getSeq() > list.get(j).getSeq()) {
					SimplePacket tmp = list.remove(j - 1);
					list.add(j, tmp);
				}
			}
		}	

		//delete duplicates
		for (int i = list.size() - 1; i > 0; i--) {
			if(list.get(i).getSeq() == list.get(i - 1).getSeq()){
				list.remove(i - 1);
			}
		}
	}



	/**
	 * Opens the image file by merging the content in the maintained list
	 * buffer.
	 */
	public void openImage() {	
		try {
			img.openImage(list);
		} 	

		catch (BrokenImageException ex) {
			try{
				if(!validImageContent())
					throw new BadImageContentException("The image is broken "
							+ "due to corrupt content");
				if(!validImageHeader())
					throw new BadImageHeaderException("The image is broken"
							+ " due to a damaged header");
			}
			catch (Exception e) {
				System.out.println(
						"Please catch the proper Image-related Exception.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initiates a Receiver to reconstruct collected packets and open the Image
	 * file, which is specified by args[0]. 
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args){
		if (args.length != 1) {
			System.out.println("Usage: java Receiver [filename_on_server]");
			return;
		}
		Receiver recv = new Receiver(args[0]);
		recv.reconstructFile();
//		recv.displayList(); //use for debugging
		recv.openImage();
	}
}
