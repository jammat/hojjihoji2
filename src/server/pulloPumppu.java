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
	
	public int getIdentity(){  // palauttaa sailiopumpun numeron
		return identity;
	}
	
	public int getSailioToBeEmptied(){  // palauttaa tyhjennettavan sailion
		return tankToBeEmptied;
	}
	
	public void setSailioToBeEmptied(int i){  // asettaa tyhjennettavan sailion
		tankToBeEmptied = i;
	}
	
	@Override
	public void stopPump(){  // pysayttaa pumpun ja asettaa sailion vapaaksi
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
						
						sailiot[i].setTila(KoneenTila.EMPTYING);  // koneen tila oikeaksi
						sailiot[i].setBottlePump(identity);  // asettaa pullopumpun
						
						if(sailiot[i].getAmountOfLiquid() > take){
							sailiot[i].takeLiquid(take);  // kaikki kunnossa -> otetaan nesteet
						} else {
							sailiot[i].takeLiquid(sailiot[i].getAmountOfLiquid());  // otetaan se mita on
							sailiot[i].setTila(KoneenTila.FREE);  // koneen tila oikeaksi
							sailiot[i].setReserved(false);  // palautetaan palautetaan sailio alkutilaan
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
