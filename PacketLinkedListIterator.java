///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Receiver.java
// File:             PacketLinkedListIterator.java
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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The iterator implementation for PacketLinkedList.
 */
public class PacketLinkedListIterator<T> implements Iterator<T> {
	//create private filed to store the head	
	private Listnode<T> curr;

	/**
	 * Constructs a PacketLinkedListIterator by passing a head node of
	 * PacketLinkedList.
	 * 
	 * @param head
	 */
	public PacketLinkedListIterator(Listnode<T> head) {
		curr = head.getNext();
		
	}
	
	/**
	 * Returns the next element in the iteration.
	 * @return the next element in the iteration
	 * @throws NoSuchElementException if the iteration has no more elements
	 */
	@Override
	public T next() {
		//if the current node is null, throw an error	
		if(curr == null){
			throw new NoSuchElementException();
		}
		//grab the data of the current node		
		T item = curr.getData();
		//assign the pointer node to next node in the list		
		curr = curr.getNext();
		//return the data		
		return item;
	}
	
	/**
	 * Returns true if the iteration has more elements.
	 * @return true if the iteration has more elements
	 */

	public boolean hasNext() {
		//return whether or not the current node is null		
		return curr != null;
	}

        /**
         * The remove operation is not supported by this iterator
         * @throws UnsupportedOperationException if the remove operation is not supported by this iterator
         */

	public void remove() {		
	  throw new UnsupportedOperationException();
	}
}
