package server;

import server.ruuvikuljetin;
import server.KoneenTila;
import server.prosessori;
import server.siilo;

public class prosessoriKuljetin extends ruuvikuljetin {
	
	private siilo[] siilot;
	private prosessori[] prosessorit;
	
	private boolean reserved; 
	private int siloToBeEmptied;
	private int processorToBeFilled;
	private int limit; 
	
	private final int identity; 
	
	private final int transferAmount = 2; 
	private final int waitTime = 10; 
	
	
	
	
	public prosessoriKuljetin(siilo[] siilot, prosessori[] prosessorit, int id){
		super();
		this.siilot = siilot;
		this.prosessorit = prosessorit;
		this.identity = id;
		limit = -1;
	}
	
	private void findSilo(){
		for(int i = 0; i < 4; i++){
			if (!siilot[i].isEmpty() && 
					( siilot[i].getTila() == KoneenTila.FREE || siilot[i].getTila() == KoneenTila.EMPTYING
					|| siilot[i].getTila() == KoneenTila.FULL ) && running && siilot[i].isReserved() 
					&& (siilot[i].getConveyer() == -1 || siilot[i].getConveyer() == identity)){
				siloToBeEmptied = i;
				siilot[i].setConveyer(identity);
				break;
			} 
		} 
	}
	
	private void findProcessor(){
		for (int i = 0; i < 3; i++){
			if (!prosessorit[i].isFull() 
					&& (prosessorit[i].getTila() == KoneenTila.FREE || prosessorit[i].getTila() == KoneenTila.FILLING)
					&& running && prosessorit[i].isReserved() && prosessorit[i].getProductAmount() == 0
					&& (prosessorit[i].getConveyer() == -1 || prosessorit[i].getConveyer() == identity)){										
				prosessorit[i].setConveyer(identity);
				processorToBeFilled = i;
				break;
			}
		}
	}
	
	
	public void run(){
		
		int oldS;
		int oldP;
		
		while(true){
				
				if (limit == 0){
					running = false;
				}
				
				oldS = siloToBeEmptied;
				oldP = processorToBeFilled;
		
				siloToBeEmptied = -1;
				processorToBeFilled = -1;
				
				if (oldS != -1 && siilot[oldS].getConveyer() == identity && !siilot[oldS].isEmpty() && 
					( siilot[oldS].getTila() == KoneenTila.FREE || siilot[oldS].getTila() == KoneenTila.EMPTYING
					|| siilot[oldS].getTila() == KoneenTila.FULL ) && running && siilot[oldS].isReserved() ){
					siloToBeEmptied = oldS;
				}
				else{
					findSilo();
				}
				
				if (oldP != -1 && prosessorit[oldP].getConveyer() == identity && !prosessorit[oldP].isFull() 
						&& (prosessorit[oldP].getTila() == KoneenTila.FREE || prosessorit[oldP].getTila() == KoneenTila.FILLING)
						&& running && prosessorit[oldP].isReserved() && prosessorit[oldP].getProductAmount() == 0){
					processorToBeFilled = oldP;
				}
				else{
					findProcessor();
				}
				
				if (siloToBeEmptied != -1 && processorToBeFilled != -1){
					
					siilot[siloToBeEmptied].setTila(KoneenTila.EMPTYING);
					prosessorit[processorToBeFilled].setTila(KoneenTila.FILLING);
					
					if (limit < transferAmount && limit != -1){
						prosessorit[processorToBeFilled].addMaterial(limit);			
						siilot[siloToBeEmptied].removeFromSilo(limit);
						limit = -1;
						running = false;
						siilot[siloToBeEmptied].setTila(KoneenTila.FREE);
						siilot[siloToBeEmptied].setConveyer(-1);
						prosessorit[processorToBeFilled].setTila(KoneenTila.FREE);
						prosessorit[processorToBeFilled].setConveyer(-1);
					}
					else{
						if(prosessorit[processorToBeFilled].getMaterialAmountVolume() - prosessorit[processorToBeFilled].getMaterialAmount() < transferAmount){
							siilot[siloToBeEmptied].removeFromSilo(prosessorit[processorToBeFilled].getMaterialAmountVolume() 
									- prosessorit[processorToBeFilled].getMaterialAmount());
							prosessorit[processorToBeFilled].addMaterial(prosessorit[processorToBeFilled].getMaterialAmountVolume() 
									- prosessorit[processorToBeFilled].getMaterialAmount());
						}else if (siilot[siloToBeEmptied].getDegreeOfFilling() < transferAmount){
								prosessorit[processorToBeFilled].addMaterial(siilot[siloToBeEmptied].getDegreeOfFilling());			
								siilot[siloToBeEmptied].emptySilo();
							}
						else {
							prosessorit[processorToBeFilled].addMaterial(transferAmount);			
							siilot[siloToBeEmptied].removeFromSilo(transferAmount);
						}
						
					if (limit != -1)
						limit = limit - transferAmount;
					}
					if (prosessorit[processorToBeFilled].isFull()){
						prosessorit[processorToBeFilled].setTila(KoneenTila.FULL);
						siilot[siloToBeEmptied].setTila(KoneenTila.FREE);
						siilot[siloToBeEmptied].setConveyer(-1);
						prosessorit[processorToBeFilled].setConveyer(-1);
					}
					if (siilot[siloToBeEmptied].isEmpty()){
						siilot[siloToBeEmptied].setTila(KoneenTila.FREE);
						prosessorit[processorToBeFilled].setTila(KoneenTila.FREE);
						siilot[siloToBeEmptied].setConveyer(-1);
						prosessorit[processorToBeFilled].setConveyer(-1);
					}
				}
				
				
				if (siloToBeEmptied == -1 && processorToBeFilled != -1){
					prosessorit[processorToBeFilled].setTila(KoneenTila.FREE);
					prosessorit[processorToBeFilled].setConveyer(-1);
				}
				if (siloToBeEmptied != -1 && processorToBeFilled == -1 && siilot[siloToBeEmptied].getTila() == KoneenTila.EMPTYING){
					siilot[siloToBeEmptied].setTila(KoneenTila.FREE);
					siilot[siloToBeEmptied].setConveyer(-1);
				}
				
				synchronized(this){
					try{
						this.wait(waitTime);
					}catch (Exception e){System.out.println(e);}
				}
		}
		
	}
	
	public void setRunning(boolean r){
		for (int i = 0; i < 4; i++){
			if (siilot[i].getTila() == KoneenTila.EMPTYING){
				siilot[i].setTila(KoneenTila.FREE);
			}
		}
		for (int i = 0; i < 3; i++){
			if (prosessorit[i].getTila() == KoneenTila.FILLING){
				prosessorit[i].setTila(KoneenTila.FREE);
			}
		}
		
		running = r;
	}
	
	public void setLimit(int l){
		limit = l;
	}
}