package server;

import server.KoneenTila;

public class sailioPumppu extends pumppu{
	private prosessori[] prosessorit;
	private sailio[] sailiot;
	private int processorToBeEmptied;
	private int tankToBeFilled;
	private final int transferAmount=5;
	private final int wait=10;
	private int identity;
	
	public sailioPumppu(prosessori[] prosessorit, sailio[] sailiot, int id){
		super();
		this.prosessorit=prosessorit;
		this.sailiot=sailiot;
		identity=id;
		tankToBeFilled=-1;
	}
	
	private void findProcessor(){  //vapaan prosessorin etsiminen
		for(int i=0;i<3;i++){
			if((prosessorit[i].getTila()==KoneenTila.READY||prosessorit[i].getTila()==KoneenTila.EMPTYING)&&!prosessorit[i].isEmpty()&&prosessorit[i].isReserved()&&(prosessorit[i].getPump()==-1||prosessorit[i].getPump()==identity)){
				processorToBeEmptied=i;
				prosessorit[i].setPump(identity);
				break;
			}
		}
	}
	private void findTank(){  //vapaan tankin etsiminen
		for(int i=0;i<10;i++){
			if((sailiot[i].getTila()==KoneenTila.FREE||sailiot[i].getTila()==KoneenTila.FILLING)&&!sailiot[i].isFull()&&sailiot[i].isReserved()&&(sailiot[i].getPump()==-1||sailiot[i].getPump()==identity)){
				tankToBeFilled=i;
				sailiot[i].setPump(identity);
				break;
			}
		}
	}
	public void run(){
		
		int vanhaP; //vanha prosessori
		int vanhaT; //vanha tankki
		
		while(true){
			while(isRunning()){
				vanhaP=processorToBeEmptied;
				vanhaT=tankToBeFilled;
				processorToBeEmptied=-1;
				tankToBeFilled=-1;
				
				if(vanhaP != -1 && prosessorit[vanhaP].getPump()==identity&&(prosessorit[vanhaP].getTila()==KoneenTila.READY||prosessorit[vanhaP].getTila()==KoneenTila.EMPTYING)&&!prosessorit[vanhaP].isEmpty()&&prosessorit[vanhaP].isReserved()){
					processorToBeEmptied=vanhaP;  //prosessorin tyhjennys
				}
				else{
					findProcessor();  //etsi vapaa prosessori
				}
				
				if (vanhaT != -1 && sailiot[vanhaT].getPump() == identity && (sailiot[vanhaT].getTila() == KoneenTila.FREE 
						|| sailiot[vanhaT].getTila() == KoneenTila.FILLING) && !sailiot[vanhaT].isFull() && sailiot[vanhaT].isReserved()){
					//
					tankToBeFilled = vanhaT;  //täytetään tähän tankkiin
				}
				else{
					findTank();  //etsi vapaa tankki
				}
				
				if (tankToBeFilled != -1&&processorToBeEmptied != -1){  //vaihdetaan koneiden tilat oikeiksi
					sailiot[tankToBeFilled].setTila(KoneenTila.FILLING);
					prosessorit[processorToBeEmptied].setTila(KoneenTila.EMPTYING);
					
					if (prosessorit[processorToBeEmptied].getProductAmount() - transferAmount  //lisätään erotuksen verran nestettä
							<= 0){
						sailiot[tankToBeFilled].addLiquid(prosessorit[processorToBeEmptied].getProductAmount());
						prosessorit[processorToBeEmptied].emptyProcessor();
						sailiot[tankToBeFilled].setTila(KoneenTila.FREE);
						prosessorit[processorToBeEmptied].setTila(KoneenTila.FREE);
						prosessorit[processorToBeEmptied].setPump(-1);
						sailiot[tankToBeFilled].setPump(-1);
					}
					
					else if (sailiot[tankToBeFilled].getAmountOfLiquid() + transferAmount >= 10000 ){  //ei mahdu tankkiin
						prosessorit[processorToBeEmptied].removeProduct(10000 - sailiot[tankToBeFilled].getAmountOfLiquid());
						sailiot[tankToBeFilled].addLiquid(10000 - sailiot[tankToBeFilled].getAmountOfLiquid());
						prosessorit[processorToBeEmptied].setTila(KoneenTila.FREE);
						prosessorit[processorToBeEmptied].setPump(-1);
					}
					else{
						prosessorit[processorToBeEmptied].removeProduct(transferAmount);  //kaikki kunnossa -> suorita operaatio
						sailiot[tankToBeFilled].addLiquid(transferAmount);
					}
					
					if (prosessorit[processorToBeEmptied].isEmpty()){
						prosessorit[processorToBeEmptied].setTila(KoneenTila.FREE);
						prosessorit[processorToBeEmptied].setPump(-1);
					}                                                                   //koneiden tilat oikeiksi
					if (sailiot[tankToBeFilled].isFull()){
						sailiot[tankToBeFilled].setTila(KoneenTila.FULL);
					}
					
				}
				synchronized(this){
					try{
						this.wait(wait);
					}catch (Exception e){System.out.println(e);}
				}
			}
			synchronized(this){
				try{
					this.wait(wait);
				}catch (Exception e){System.out.println(e);}
			}
		}
	}
	
	public void setRunning(boolean r){
		running = r;
	}
	
	public void stopPump(){  //pumpun pysäyttämismetodi
		if (tankToBeFilled != -1 && sailiot[tankToBeFilled].getTila() == KoneenTila.FILLING){
			sailiot[tankToBeFilled].setTila(KoneenTila.FREE);
		}
		running = false;
	}
	
	
}
