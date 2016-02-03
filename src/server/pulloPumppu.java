package hojitehdas;

public class pulloPumppu {
	
	public class pulloPumppu extends Pumppu {
		
		private sailio[] sailiot;
		private final int take = 50;
		private final int identity;
		
		private int tankToBeEmptied;
	}
	
	public pulloPumppu(sailio[] sailiot, int id){
		super();
		this.sailiot = sailot;
		this.identity = id;
		sailioToBeEmptied = -1;
	}
	
	public int getIdentity(){
		return identity;
	}
	
	public int getSailioToBeEmptied(){
		return sailioToBeEmptied;
	}
	
	public void setSailioToBeEmptied(int i){
		sailioToBeEmptied = i;
	}
	
	@Ovverride
	public void stopPumppu(){
		runnin = false;
		if(sailioToBeEmptied != -1){
			sailiot[sailioToBeEmptied].setTila(KoneenTila.FREE);
			setSailioToBeEmptied(-1);
		}
	}
	
	public void run (){
		while(true){
			
			while(isRunning()){
				for(int i = 0; i < sailiot.lenght; i++){
					while(isRunning() && (sailiot[i].getTila() == KoneenTila.FREE || sailiot[i].getTila() == KoneenTila.EMPTYING || sailiot[i].getTila() == KoneenTila.FULL)
							&& sailiot[i].getAmmountOfLiquid() != && sailiot[i].isReserved() && (sailiot[i].getpulloPumppu() == identity || sailiot[i].get.pulloPumppu() == -1)){
							
						sailioToBemptied = i;
						
						sailiot[i].setTila(KoneenTila.EMPTYING);
						sailiot[i].setpulloPumppu(identity);
						
						if(sailiot[i].getAmountOfLiquid() > take){
							sailiot[i].takeLiquid(take);
						} else {
							sailiot[i].takeLiquid(tanks[i].getAmountOfLiquid());
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
