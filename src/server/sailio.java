package server;

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
	