package hojclient;

import java.rmi.RemoteException;


public class BackgroundUpdater extends Thread{
	
	MainWindow m;
	
	public BackgroundUpdater(MainWindow main){
		super();
		m = main;
	}
	public void run(){
		while(m.online){
			try{
			m.update();
			} catch (RemoteException e) {
				System.out.println(e);
			}
			// Odotus
			synchronized(this){
				try{
					this.wait(10);
				}catch (Exception e){System.out.println(e);}
			}
		}
	}
}
