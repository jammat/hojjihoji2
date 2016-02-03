package server;

import hojserver.KoneenTila;


public class siilo {

	private final int capacity = 10000; 
	private int degreeOfFilling; 
	
	private boolean reserved;
	private KoneenTila tila;
	private int conveyer;

	
	public siilo(){
		degreeOfFilling = 0;
		tila = KoneenTila.FREE;
		conveyer = -1;
	}

	
	public int getCapacity(){
		return capacity;
	}
	
	public int getDegreeOfFilling(){
		return degreeOfFilling;
	}
	
	public KoneenTila getTila(){
		return tila;
	}
	
	public void  setTila(KoneenTila t){
		tila = t;
	}
	public int getConveyer(){
		return conveyer;
	}
	
	public void setConveyer(int conv){
		conveyer = conv;
	}
	
	public boolean isReserved(){
		return reserved;
	}
	
	public void setReserved(boolean r){
		if (!r){
			tila = KoneenTila.FREE;
		}
		
		reserved = r;

	}
	
	/**
	 * Method tells whether the siilo is full or not
	 * @return boolean true if siilo is full, false if siilo is not full
	 */
	public boolean isFull(){
		if(degreeOfFilling >= capacity){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isEmpty(){
		if (degreeOfFilling <= 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param fill
	 */
	public void resetDegreeOfFilling(int fill){ 
		if(fill <= capacity){
			degreeOfFilling = fill;
		} else{
			degreeOfFilling = capacity;
		}
	}
	
	
	/**
	 * Method that adds material to siilo.
	 * @param addition
	 */
	public void addTosiilo(int addition){
		if(degreeOfFilling + addition <= capacity){
			degreeOfFilling += addition;
		}
	}
	
	public void removeFromsiilo(int sub){
		if (degreeOfFilling - sub >= 0){
			degreeOfFilling = degreeOfFilling - sub;
		}
	}
	
	/**
	 * Fills the siilo to the maximum capacity.
	 */
	public void fillsiilo(){
		degreeOfFilling = capacity;
	}
	
	/**
	 * Method for taking material from the siilo. 
	 * @param take
	 */
	public void takeFromsiilo(int take){
		if(take <= degreeOfFilling){
			degreeOfFilling -= take;
		} else {
			degreeOfFilling = 0;
		}
	}
	
	/**
	 * Takes everything from the siilo.
	 */
	public void emptysiilo(){
		degreeOfFilling = 0;
	}
	
} 
