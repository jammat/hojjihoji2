package server;
	
	public class pulloPumppu extends pumppu {
		
		private sailio[] sailiot;
		private final int take = 50;
		private final int identity;
		
		private int tankToBeEmptied;
	
	public pulloPumppu(sailio[] sailiot, int id){
		super();
		this.sailiot = sailiot;
		this.identity = id;
		tankToBeEmptied = -1;
	}
	
	public int getIdentity(){
		return identity;
	}
	
	public int getSailioToBeEmptied(){
		return tankToBeEmptied;
	}
	
	public void setSailioToBeEmptied(int i){
		tankToBeEmptied = i;
	}
	
	@Override
	public void stoppumppu(){
		running = false;
		if(tankToBeEmptied != -1){
			sailiot[tankToBeEmptied].setTila(KoneenTila.FREE);
			setSailioToBeEmptied(-1);
		}
	}
	
	public void run (){
		while(true){
			
			while(isRunning()){
				for(int i = 0; i < sailiot.length; i++){
					while(isRunning() && (sailiot[i].getTila() == KoneenTila.FREE || sailiot[i].getTila() == KoneenTila.EMPTYING || sailiot[i].getTila() == KoneenTila.FULL)
							&& sailiot[i].getAmountOfLiquid() != 0 && sailiot[i].isReserved() && (sailiot[i].getBottlePump() == identity || sailiot[i].getBottlePump() == -1)){
							
						tankToBeEmptied = i;
						
						sailiot[i].setTila(KoneenTila.EMPTYING);
						sailiot[i].setBottlePump(identity);
						
						if(sailiot[i].getAmountOfLiquid() > take){
							sailiot[i].takeLiquid(take);
						} else {
							sailiot[i].takeLiquid(sailiot[i].getAmountOfLiquid());
							sailiot[i].setTila(KoneenTila.FREE);
							sailiot[i].setReserved(false);
							sailiot[i].setBottlePump(-1);
							setSailioToBeEmptied(-1);
						}
						synchronized(this){
							try {
								this.wait(100);
							} catch (InterruptedException e) { e.printStackTrace(); }
						}
					}
			}
		}
			synchronized(this){
				try {
					this.wait(500);
				} catch (InterruptedException e) {	e.printStackTrace(); }
			}
		}
	} 
}
