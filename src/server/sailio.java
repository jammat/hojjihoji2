package server;

import server.KoneenTila;

public class sailio extends Thread{

	private int tilavuus = 10000;
	private int amountOfLiquid;
	private boolean reserved;
	private KoneenTila tila;
	private int pump;
	private int bottlepump;
	
	public sailio(){
		amountOfLiquid=0;
		reserved=false;
		tila=KoneenTila.FREE;
		pump=-1;
		bottlepump=-1;
	}
	public void setTila(KoneenTila t){
		if(t == KoneenTila.FREE){
			setPump(-1);
			setBottlePump(-1);
		}
		tila = t;
	}
	
	public KoneenTila getTila(){
		return tila;
	}
	
	public synchronized boolean isReserved(){
		return reserved;
	}
	
	public synchronized void setReserved(boolean r){
		if(r == false){
			setBottlePump(-1);
			if(isFull()){
				setTila(KoneenTila.FULL);
			} else {
				setTila(KoneenTila.FREE);
			}
		}
		reserved = r;
	}
	
	public boolean isFull(){
		if (amountOfLiquid >= tilavuus){
			return true;
		}
		else{
			return false;
		}
	}
	public int getPump(){
		return pump;
	}
	
	public void setPump(int p){
		pump = p;
	}
	
	public int getBottlePump(){
		return bottlepump;
	}
	
	public void setBottlePump(int p){
		bottlepump = p;
	}
	
	public synchronized int getAmountOfLiquid(){
		return amountOfLiquid;
	}
	
	public synchronized void takeLiquid(int amount){
		if(amount <= amountOfLiquid){
			amountOfLiquid -= amount;
		}
	}
	
	public synchronized void addLiquid(int amount){
		if(amount <=  tilavuus - amountOfLiquid){
			amountOfLiquid += amount;
		}
	}
	
}
