package server;

public class siiloKuljetin extends ruuvikuljetin{
	
	private siilo[] siilot;
	private boolean reserved;
	private int siiloToBeFilled;
	
	public siiloKuljetin(siilo[] s){
		super();
		siilot=s;
		reserved=false;
		siiloToBeFilled=-1;
	}
	
	public void run(){
		int vanhaS;
		while(true){
			while(running){
				vanhaS=siiloToBeFilled;
				siiloToBeFilled=-1;
				
				if(vanhaS != -1 && !siilot[vanhaS].isFull() && (siilot[vanhaS].getTila()==KoneenTila.FREE || siilot[vanhaS].getTila()==KoneenTila.FILLING)&&siilot[vanhaS].isReserved() && running && siilot[vanhaS].isReserved()){
					siiloToBeFilled=vanhaS;
				}
				else{
					for(int i=0; i<4; i++){
						if(!siilot[i].isFull()&&(siilot[i].getTila()==KoneenTila.FREE||siilot[i].getTila()==KoneenTila.FILLING)&&siilot[i].isReserved()&&running&&siilot[i].isReserved()){
							siiloToBeFilled=i;
							break;
						}
					}
				}
				if(siiloToBeFilled != -1){
					siilot[siiloToBeFilled].setTila(KoneenTila.FILLING);
					siilot[siiloToBeFilled].addTosiilo(transferAmount);
					if(siilot[siiloToBeFilled].isFull()){
						siilot[siiloToBeFilled].setTila(KoneenTila.FULL);
					}
				}
				for(int i=0;i<4;i++){
					if(i != siiloToBeFilled && siilot[i].getTila()==KoneenTila.FILLING){
						siilot[i].setTila(KoneenTila.FREE);
					}
				}
				synchronized(this){
					try{
						this.wait(waitTime);
					}catch(Exception e){System.out.print(e);}
				}
			}
			synchronized(this){
				try{
					this.wait(waitTime);
				}catch(Exception e){System.out.print(e);}
			}
		}
	}
	
	public void setRunning(boolean r){
		if(!r){
			for(int i=0;i<4;i++){
				if(siilot[i].getTila()==KoneenTila.FILLING){
					siilot[i].setTila(KoneenTila.FREE);
				}
			}
		}
		running=r;
	}
}
