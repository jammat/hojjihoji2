package server;

import hojserver.KoneenTila;

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
	public void setTila(KoneenTila t){ //asettaa tilan
		if(t == KoneenTila.FREE){
			setPump(-1);
			setBottlePump(-1);
		}
		tila = t;
	}
	
	public KoneenTila getTila(){ //palauttaa tilan
		return tila;
	}
	
	public synchronized boolean isReserved(){  //palauttaa, onko sailio varattu
		return reserved;
	}
	
	public synchronized void setReserved(boolean r){  //asetetaan sailio varatuksi
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
	
	public boolean isFull(){  //totuusarvo sailion tayttoasteesta
		if (amountOfLiquid >= tilavuus){
			return true;
		}
		else{
			return false;
		}
	}
	public int getPump(){  //palauttaa pumpun
		return pump;
	}
	
	public void setPump(int p){  //asettaa pumpun
		pump = p;
	}
	
	public int getBottlePump(){ //palauttaa pullopumpun
		return bottlepump;
	}
	
	public void setBottlePump(int p){  //asettaa pullopumpun
		bottlepump = p;
	}
	
	public synchronized int getAmountOfLiquid(){  //palauttaa nestemaaran
		return amountOfLiquid;
	}
	
	public synchronized void takeLiquid(int amount){  // ottaa nestetta
		if(amount <= amountOfLiquid){
			amountOfLiquid -= amount;
		}
	}
	
	public synchronized void addLiquid(int amount){  // lisaa nestetta
		if(amount <=  tilavuus - amountOfLiquid){
			amountOfLiquid += amount;
		}
	}
	
}
