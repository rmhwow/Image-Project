///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Receiver.java
// File:             BadImageHeaderException.java
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
	 * Constructs a BadImageHeader Exception
	 *<p> No known bugs </p<
	 *@author jefferson & o-leary
	 */
public class BadImageHeaderException extends RuntimeException {
	/**
	 * Constructs a BadImageException with a message
	 * @param s the error message
	 */
	public BadImageHeaderException(String s) {
		super(s);
	}

	/**
	 * Constructs a BadImageException with no params
	 */
	public BadImageHeaderException() {
	}
}

