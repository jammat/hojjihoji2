package server;
import java.rmi.*;
import java.rmi.server.*;



	public class Tehdas extends UnicastRemoteObject implements tehtaanOlemus {

		private siilo[] siilot;
		private ruuvikuljetin[] ruuvikuljettimet;
		private pumppu[] pumput;
		private prosessori[] prosessorit;
		private sailio[] kypsytyssailiot; 
		
		
		public Tehdas() throws RemoteException {
			super();
			siilot = new siilo[4]; 								//4 siiloa
			ruuvikuljettimet = new ruuvikuljetin[3]; 			//3 kuljetinta
			pumput = new pumppu[4]; 							//4 pumppua
			prosessorit = new prosessori[3];					//3 prosessoria
			kypsytyssailiot	  = new sailio[10]; 				//10 kypsytyssäiliötä
			
			//Kutsutaan koneet luovaa ja koneiden threadit käynnistävää metodia.
			alustaKoneet();
			
		}
			
}
