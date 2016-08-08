///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Receiver.java
// File:             PacketLinkedList.java
// Semester:         (CS 367) Spring 2016
//
// Author:           Jefferson Killian
// Email:            jdkillian@wisc.edu
// CS Login:         jefferson
// Lecturer's Name:  Lecture 3
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
// Pair Partner:     Morgan O'Leary
// Email:            oleary@wisc.edu
// CS Login:         o-leary
// Lecturer's Name:  Lecture 3
//////////////////////////// 80 columns wide //////////////////////////////////
/**
 * A Single-linked linkedlist with a "dumb" header node (no data in the node), but
 * without a tail node. It implements ListADT<E> and returns
 * PacketLinkedListIterator when requiring a iterator.
 */
public class PacketLinkedList<E> implements ListADT<E> {
	// create a node that will hold the header node and a variable that will 
	//	hold the number of items
		private Listnode<E> head;
		private int numItems;
	/**
	 * Constructs a empty PacketLinkedList
	 */
	public PacketLinkedList() {
		//initialize first node to a dummy node		
		head = new Listnode<E>(null);
		numItems = 0;
	}

	/**
	 * This method adds the item passed in to the list if the item is
	 * legitimate. If the item is null, then an exception is thrown. 
	 *
	 * @param (E item) (the packet that needs to be added to the list)
	 */
	public void add(E item) {
		//if the item is equal to null, throw an exception		
		if(item == null){
			throw new IllegalArgumentException();
		}
		//assign the item to a new node		
		Listnode<E> addItem = new Listnode<E>(item);
		//assign the header node to a variable curr		
		Listnode<E> curr = head;
		//traverse through the list until we get to the very end		
		while(curr.getNext() != null){
			curr = curr.getNext();	
		}
		//set the last node in the list's next node to the new node		
		curr.setNext(addItem);
		//increase the number of items in the list		
		numItems++;
	}

	/**
	 * This method adds an item to a specific index in the list, if the item
	 * is legitimate. If the item is null or the position is out of bounds, 
	 * exceptions will be thrown. 
	 *
	 * @param (int pos) (the index in which to add the node)
	 * @param (E item) (the item that needs to be added to the list)
	 * @return (description of the return value)
	 */
	public void add(int pos, E item) {
		//if the position is out of the bounds of the list, throw exception		
		if(pos < 0 || pos > numItems){
			throw new IndexOutOfBoundsException();
		}
		//create new node with the passed in item		
		Listnode<E> newNode = new Listnode<E>(item);
		//assign a node variable to the header 		
		Listnode<E> current = head;
		//traverse through the list until the correct position is reached		
		for(int p = 0; p < pos -1; p++ ){
			current = current.getNext();
		}
		//assign the newnode's next node to the current node's next node		
		newNode.setNext(current.getNext());
		//assign the current node's next node to the new node		
		current.setNext(newNode);
		//increase the number of items	
		numItems++;
	}

	/**
	 * This method receives an item and returns true if the item is within the 
	 * list and false if it is not. 
	 *
	 * @param (E item) the item to search for in the list. 
	 * @return true if item is in the list, false if not. 
	 */
	public boolean contains(E item) {
		//assign the item to a node variable		
		Listnode<E> thingToFind = new Listnode<E>(item);
		//assign the head to a variable		
		Listnode<E> top = head;
		//traverse through the list until the correct node and return true		
		while(top.getNext() != null){
			top = top.getNext();
			if(top.getData().equals(thingToFind.getData())){
				return true;
			}
		}
		//if the node is not in the list return false		
		return false;
	}

	/**
	 *This method retrieves the data of a  node stored at a certain position. 
	 *
	 * @param (int pos) (the index in which the node is located)
	 * @return E item (the data of a specific node)
	 */
	public E get(int pos) {
		//assign the first element to a variable			
		Listnode<E> pointer = head;
		//traverse 	through the list until the correct node is reached	
		for(int p = 0; p <= pos; p++ ){
			pointer = pointer.getNext();	
		}
		//return the correct node's data		
		return pointer.getData();
	}

	/**
	 * This method says whether there are any elements in the list. 
	 *
	 * @return (true if the list contains items and false if it does not)
	 */
	public boolean isEmpty() {
		//if there are no items in the list return true, else false		
		if(numItems == 0){
			return true;
		}
		return false;
	}

	/**
	 *This method receives an index and removes the Listnode at that index.
	 *
	 * @param (int pos) (the index in which to remove the item from the list)
	 * @return (E item) the data of the node that was removed
	 */
	public E remove(int pos) {
		//assign a variable to the head		
		Listnode<E> takeItFromTheTop = head;
		//if the position is the first position		
		if(pos == 0){
			//assign the data to variable deleted			
			E deleted = head.getNext().getData();
			//set the next node to the node two away			
			 head.setNext(takeItFromTheTop.getNext().getNext());
			 //decrease the number of items			 
			 numItems--;
			 //return information			 
			return deleted;
			
		}else{
			takeItFromTheTop = takeItFromTheTop.getNext();
			//traverse through the list until the correct node is reached			
			for(int p = 0; p < pos -1; p++ ){
				takeItFromTheTop = takeItFromTheTop.getNext();	
			}
			//save the node to delete in variable			
			Listnode<E> delete = takeItFromTheTop.getNext();
			//set the next node to the next node in list			
			takeItFromTheTop.setNext(takeItFromTheTop.getNext().getNext());
			//decrease the number of items			
			numItems--;
			//return deleted data			
			return delete.getData();
		}
	}

	/**
	 * This method returns how many items are currently in the list.
	 *
	 * @return (int numItems) the number of items in the list 
	 */
	public int size() {
		//return the size of the list		
		return numItems;
	}

	/**
	 * This method constructs a new iterator
	
	 */
	public PacketLinkedListIterator<E> iterator() {
		//return the iterator with start the first non-null node		
		return new PacketLinkedListIterator<E>(head);
	}

}
