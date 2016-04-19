package server;

import java.util.UUID;


 
 public class prosessori extends Thread {
 
 	private final int waterAmountVolume = 10000; //litraa
 	private final int materialAmountVolume = 2000; //kiloa
 	private final double processingtime = 20000; //millisekuntia
 	
 	private int waterAmount;
 	private int materialAmount;
 	private int productAmount;
 	private double progress;
 	private boolean running;
 	private boolean reserved;
 	private KoneenTila tila;
 	private int conveyer;
 	private int pump;
 	private String user;
 	private UUID userId;
 	
 	public prosessori(){
 		waterAmount = 0;
 		materialAmount = 0;
 		tila = KoneenTila.FREE;
 		reserved = false;
 		running = false;
 		user = null;
 		userId = null;
 		conveyer = -1;
 		pump = -1;
 		
 	} 
 
 
  	
  	public void run(){
  		

  		int timespent = 0;
  		
		while(true){
			
			while(running && tila == KoneenTila.PROCESSING && getProductAmount() == 0){
 				synchronized (this) {
 					try {
 						this.wait(250); 
 					} catch (InterruptedException e) {
 						System.out.println("Juoman keittäminen keskeytyi keittimessä " + this);
 						e.printStackTrace();
 					}
 				}

 				addProgress(5);
 				timespent += 500;
 				
 				System.out.println("Time spent processing: " + timespent + " milliseconds. Progress " + getProgress() + " %");
 				
 				if(progress == 100){ 
 					running = false;
 					makeProduct();
 					this.setTila(KoneenTila.READY);
 					this.setReserved(false);
 					System.out.println("Processor: Juoma valmis keittimessä " + this + ", juomaa " + productAmount + " litraa.");
 				}
 			}
			synchronized (this) {
 				try {
 					this.wait(100); 
 				} catch (InterruptedException e) { e.printStackTrace(); }
 			}
 			resetProgress(); 
 		}
  	}
 	
 	public void setUser(String k){  // aseta kayttaja
 		user = k;
 	}
 	
 	public String getUser(){ // palauta kayttaja
 		return user;
 	}
 	
 	public UUID getUserId(){ // palauttaa UUID:n
 		return userId;
 	}
 	
 	public void setUserId(UUID id){  // asettaa UUID:n
 		userId = id;
 	}
 	
 	public void setConveyer(int c){  // asettaa kuljettimen
 		conveyer = c;
 	}
 	
 	public int getConveyer(){  // palauttaa kuljettimen
 		return conveyer;
 	}
 	
 	public void setPump(int p){  // asettaa pumpun
 		pump = p;
 	}
 	
 	public int getPump(){  // palauttaa pumpun
 		return pump;
 	}
 	
 	
 	public void setReserved(boolean r){  // asettaa varauksen
 	
 		if(r == false && tila != KoneenTila.PROCESSING){
 			reserved = r;
 			userId = null;
 			user = null;
 		}
 		else if(r == true){
 			reserved = r;
 			System.out.println("Vapautetaan prosessori!" + reserved);
 			
 			if(getProductAmount() != 0){	
 				setTila(KoneenTila.READY);
 			} 
 			else if(isFull() && getProductAmount() == 0){
 				setTila(KoneenTila.FULL);
 			} else {
 				setTila(KoneenTila.FREE); 
 			}
 		}
 	}
 	public void setRunning(boolean r){  // aseta kayntiin
 		
 		if(r == true){
 			if(reserved && tila != KoneenTila.EMPTYING && tila != KoneenTila.FILLING && tila != KoneenTila.READY && !isEmpty() && getProductAmount() == 0){
 				running = r;
 				setTila(KoneenTila.PROCESSING);
 				System.out.println("Processor: Käynnistetään prosessori.");
 			} else {
 				System.out.println("Prosessorin " + this + " start-painiketta ei voi painaa.");
 				System.out.println("Prosessori tilassa: " + getTila());
 			}
 		}
 		else if(r == false){
 			if(tila == KoneenTila.READY || tila == KoneenTila.FULL || tila == KoneenTila.FREE){
 				running = false;
 				if(isFull() && getProductAmount() == 0){ 
 					setTila(KoneenTila.FULL);
 				} else if(getProductAmount() != 0){ 
 					setTila(KoneenTila.READY);
 				}
 				else {
 					setTila(KoneenTila.FREE); 
 				}
 			}
 		}
 	}
 	public int getWaterAmount(){  // palauttaa nesteen maaran
 		return waterAmount;			
 	}
 	
 	public int getMaterialAmount(){  // palauttaa materiaalin maaran
 		return materialAmount;
 	}
 	
 	public void addMaterial(int maara){  // lisaa materiaalia
 		materialAmount += maara;
 		if(isFull()){
 			setTila(KoneenTila.FULL);
 		}
 	}
 	
 	public void emptyProcessor(){  // tyhjentaa prosessorin
 		waterAmount = 0;
 		materialAmount = 0;
 		productAmount = 0;
 	}
 	 
 	public int getMaterialAmountVolume(){  // palauttaa materiaalin maaran
 		return materialAmountVolume;
 	}
 	
 	
 	public void makeProduct(){  // valmistaa tuotetta
 		productAmount = 5 * materialAmount;
 	}
 	
 	
 	public void removeProduct(int amount){  // poistaa tuotetta
 		productAmount = productAmount - amount;
 	}
 	
 	public void setProductAmount(int amount){  // asettaa tuotteen maaran
 		productAmount = amount;
 	}
 	
 	public int getProductAmount(){  // palauttaa tuotteen maaran
 		return productAmount;
 	}
 	public double getProgress(){  // palauttaa etenemistilanteen
 		return progress;
 	}
 	
 	public void addProgress(double amount){  // lisaa etenemistilannetta
 		progress += amount;
 	}
 	
 	public void resetProgress(){  // resetoi etenemistilanteen
 		progress = 0;
 	}
 	
 	public int getFillPercentage(){  // palauttaa tayttomaaran prosenteissa
 		return (int)(100 * ((double)materialAmount / (double)materialAmountVolume) ); 
 	}
 	
 	public int getProductPercentage(){  // palauttaa tuotteen prosentit
 		return (int) (100 * ((double)productAmount / (double)waterAmountVolume));
 	}
 	
	public void setTila(KoneenTila t){  // asettaa tilan laitteelle
 		if(t == KoneenTila.FREE || t == KoneenTila.FILLING || t == KoneenTila.PROCESSING){
 			progress = 0;
 		}
 		tila = t;
 	}
 	
 	public KoneenTila getTila(){  // palauttaa laitteen tilan
 		return tila;
 	}
 	
 	public boolean isFull(){  // palauttaa totuusarvon, onko taynna
 		if (materialAmount >= materialAmountVolume){
 			return true;
 		}
 		else{
 			return false;
 		}
 	}
 	
 	public boolean isEmpty(){  // palauttaa totuusarvon, onko tyhja
 		return (materialAmount == 0 && waterAmount == 0 && productAmount == 0);
 	}
 	
 	public boolean isReserved(){  // palauttaa totuusarvon, onko varattu
 		return reserved;
 	}
 	
 	public boolean isRunning(){  // palauttaa totuusarvon, onko kaynnissa
 		return running;
 	}
 	 	 	
 }
