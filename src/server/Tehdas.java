package server;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Hashtable;
import java.util.UUID;
import server.prosessori;
import server.KoneenTila;
import server.prosessoriKuljetin;
import server.pulloPumppu;
import server.siilo;
import server.siiloKuljetin;
import server.sailio;
import server.sailioPumppu;



	public class Tehdas extends UnicastRemoteObject implements tehtaanOlemus {

		private siilo[] siilot;
		private ruuvikuljetin[] ruuvikuljettimet;
		private pumppu[] pumput;
		private prosessori[] prosessorit;
		private sailio[] kypsytyssailiot; 
		private Hashtable<UUID, String> userIdKeys;
		
		
		public Tehdas() throws RemoteException {
			super();
			siilot = new siilo[4]; 								//4 siiloa
			ruuvikuljettimet = new ruuvikuljetin[3]; 			//3 kuljetinta
			pumput = new pumppu[4]; 							//4 pumppua
			prosessorit = new prosessori[3];					//3 prosessoria
			kypsytyssailiot	  = new sailio[10]; 				//10 kypsytyssailiota
			
			//Kutsutaan koneet luovaa ja koneiden threadit kaynnistavaa metodia.
			alustaKoneet();
			userIdKeys = new Hashtable<UUID, String>();
		}
		//-------- Kirjautuminen --------
		
		// kayttaja kirjautuu
		public UUID login(String kayttajaNimi) throws RemoteException {
			UUID id = UUID.randomUUID();
			userIdKeys.put(id, kayttajaNimi);
			System.out.println("KÃ¤yttÃ¤jÃ¤ " + kayttajaNimi + ", " + id + " kirjautui sisÃ¤Ã¤n.");
			return id;
		}
		
		// 
		public String prosessorinKayttaja(int prosessori) throws RemoteException {
			return prosessorit[prosessori].getUser();
		}
		
		// ULOSKIRJOITUS
		public void logout(UUID idKey) throws RemoteException {
			userIdKeys.remove(idKey);
			System.out.println("kayttaja " + userIdKeys.get(idKey) + ", " + idKey + " kirjautui ulos.");
		}
		
		//-------- Ruuvikuljettimet -------------
		
		//ruuvikuljettimen kaynnistys
		public void ruuvihihnanKaynnistys() throws RemoteException {
			System.out.println("Tehdas: kaynnistetaan siiloja tayttava ruuvikuljetin");
			ruuvikuljettimet[0].setRunning(true);
		}
		
		//ruuvikuljettimen sammutus
		public void ruuvihihnanKaynnistysVapautus() throws RemoteException {
			System.out.println("Tehdas: Sammutetaan siiloja tayttava ruuvikuletin.");
			ruuvikuljettimet[0].setRunning(false);
		}
		
		//-------- Siilot --------------
		// Siilon varaus
		public void siilonVaraus(int siilonNro) throws RemoteException {
			System.out.println("Tehdas: Varataan siilo nro. " + (siilonNro+1));
			siilot[siilonNro].setReserved(true);
		}
		
		// Siilon vapautus
		public void siilonVarausVapautus(int siilonNro) throws RemoteException {
			System.out.println("Tehdas: Vapautetaan siilo nro. " + (siilonNro+1));
			siilot[siilonNro].setReserved(false);
		}
		
		//-------- Prosessorit eli keittimet --------
		
		// prosessoriKuljettimen kaynnistys
		public void prosessorinLataus(int kuljettimenNro, int maara) throws RemoteException {
			if (maara != -1){
				System.out.println("Tehdas: Ruuvikuljetin " + (kuljettimenNro+1) + " alkaa siirtÃ¤Ã¤ " + maara + "kg materiaalia prosessoriin");
			}
			else{
				System.out.println("Tehdas: Ruuvikuljetin " + (kuljettimenNro+1) + " alkaa siirtÃ¤Ã¤ materiaalia prosessoriin");
			}
			ruuvikuljettimet[kuljettimenNro].setRunning(true);
			ruuvikuljettimet[kuljettimenNro].setLimit(maara);
		}

		// prosessoriKuljettimen vapautus
		public void prosessorinLatausVapautus(int kuljettimenNro) throws RemoteException {
			System.out.println("Tehdas: Sammutetaan ruuvikuljetin nro. " + (kuljettimenNro+1));
			ruuvikuljettimet[kuljettimenNro].setRunning(false);
		}

		
		// prosessorin varaaminen
		public void prosessorinVaraus(int prosessorinNro, UUID userId) throws RemoteException {
			if(prosessorit[prosessorinNro].getUserId() == null){
				prosessorit[prosessorinNro].setReserved(true);
				prosessorit[prosessorinNro].setUser(userIdKeys.get(userId));
				prosessorit[prosessorinNro].setUserId(userId);
				System.out.println("Tehdas: kayttaja " + userIdKeys.get(userId) + ", " + userId + " varaa prosessorin nro. " + (prosessorinNro+1));
			} else {
				System.out.println("kayttaja: " + userIdKeys.get(userId) + " yritti varata prosessorin " + (prosessorinNro+1) + ".");
				System.out.println("Tehdas: Ei voida varata prosessoria. Prosessori on kayttajan " + userIdKeys.get(prosessorit[prosessorinNro].getUserId()) + ", " + prosessorit[prosessorinNro].getUserId() + " hallussa.");
			}
		}
		
		// Prosessorin vapautuksen palautus
		public void prosessorinVarausVapautus(int prosessorinNro, UUID userId) throws RemoteException {
			if(prosessorit[prosessorinNro].getUserId().equals(userId)){
				prosessorit[prosessorinNro].setReserved(false);
				System.out.println("Tehdas: Vapautetaan prosessori nro. " + (prosessorinNro+1)); //TODO
			} else {
				System.out.println("kayttaja: " + userIdKeys.get(userId) + ", " + userId + " yritti vapauttaa prosessorin " + (prosessorinNro+1) + ".");
				System.out.println("Tehdas: Ei voida vapauttaa. Prosessori on kayttajan " + userIdKeys.get(prosessorit[prosessorinNro].getUserId()) + ", " + userId + " hallussa.");
			}
		}

		// Prosessorin kaynnistys
		public void prosessorinKaynnistys(int prosessorinNro, UUID userId) throws RemoteException {
			if(prosessorit[prosessorinNro].getUserId().equals(userId)){
				prosessorit[prosessorinNro].setRunning(true);
			} else {
				System.out.println("KÃ¤yttÃ¤jÃ¤: " + userIdKeys.get(userId) + ", " + userId + " yritti kÃ¤ynnistÃ¤Ã¤ prosessorin " + (prosessorinNro+1) + ".");
				System.out.println("Tehdas: Ei voida kÃ¤ynnistÃ¤Ã¤. Prosessori on kÃ¤yttÃ¤jÃ¤n " + userIdKeys.get(userId) + ", " + userId + " hallussa.");
			}
		}
		
		// Prosessorin sammutus
		public void prosessorinKaynnistysVapautus(int prosessorinNro) throws RemoteException {
			System.out.println("Tehdas: Sammutetaan prosessori " + (prosessorinNro+1));
			prosessorit[prosessorinNro].setRunning(false);
		}

		//-------- Sailioit eli kypsytysSailijot ja niita kasittelevat pumput --------
		
		// Sailioiden pumppujen kaynnistys
		public void sailoidenTaytto(int pumpunNro) throws RemoteException {
			pumput[pumpunNro].runPump();
			System.out.println("Tehdas: Kaynnistetaan pumppu nro. " + (pumpunNro+1) + " prosessoreista -> kypsytysSailioihin.");
		}

		// Sailioiden pumppujen varaus
		public void sailoidenTayttoVapautus(int pumpunNro) throws RemoteException {
			pumput[pumpunNro].stopPump();
			System.out.println("Tehdas: Sammutetaan kypsytysSailioiden pumppu nro. " + (pumpunNro+1));
		}
		
		// Sailion varaus
		public void sailionVaraus(int sailionNro) throws RemoteException {
			kypsytyssailiot[sailionNro].setReserved(true);		
			System.out.println("Tehdas: Varataan kypsytysSailio nro. " + (sailionNro+1));
		}
		
		// Sailioiden varauksen purku
		public void sailionVarausVapautus(int sailionNro) throws RemoteException {
			kypsytyssailiot[sailionNro].setReserved(false);
			System.out.print("Tehdas: Vapautetaan kypsytysSailio nro. " + (sailionNro+1));
		}
		
		//-------- Pumppaaminen pullotukseen --------
		
		// pulloPumpun kaynnistys
		public void pullojenTaytto(int pumpunNro) throws RemoteException {
			pumput[pumpunNro].runPump();
			System.out.println("Tehdas: kaynnistetaan pullotuspumppu nro. " + (pumpunNro+1));
		}
		
		public void pullojenTayttoVapautus(int pumpunNro) throws RemoteException {
			pumput[pumpunNro].stopPump();
			System.out.println("Tehdas: Sammutetaan pullotuspumppu nro. " + (pumpunNro+1));
		}

		//.-.-.-.-.-. Tietojen keraaminen kayttoliittyman paivittamista varten .-.-.-.-.-.
		
		public int[] siilojenAineMaara() throws RemoteException {
			int[] sam = new int[4];
			
			for(int i = 0; i < siilot.length; i++){
				sam[i] = siilot[i].getDegreeOfFilling();
			}
			return sam;
		}

		// prosentteina
		public String[] prosessorienTila() throws RemoteException {
			String[] tila = new String[prosessorit.length];
			for(int i = 0; i < prosessorit.length; i++){
				if(prosessorit[i].getTila() == KoneenTila.EMPTYING){
					tila[i] = "Emptying";
				}
				else if(prosessorit[i].getTila() == KoneenTila.FILLING){
					tila[i] = "Filling";
				}
				else if(prosessorit[i].getTila() == KoneenTila.PROCESSING){
					tila[i] = "Processing";
				}
				else if(prosessorit[i].getTila() == KoneenTila.READY){
					tila[i] = "Ready";
				}
				else if(prosessorit[i].getTila() == KoneenTila.FREE){
					tila[i] = "Waiting";
				}
				else if (prosessorit[i].getTila() == KoneenTila.FULL){
					tila[i] = "Full";
				}
			}
			return tila;
		}

		/**
		 * Metodi keraa tiedot prosessorien tiloista prosentteina. Prosessorin tilasta riippuen kayttoliittymalle kerrotaan joko
		 * a) valmiin tuotteen prosentti (kapasiteetista)
		 * b) kuinka suuri osuus raaka-ainekapasiteetista on taytetty
		 * c) juoman keittamisen edistyminen prosentteina
		 */
		public String[] prosessorienEdistyminen() throws RemoteException {
			String[] progress = new String[prosessorit.length];
			
			for(int i = 0; i < prosessorit.length; i++){
				if (prosessorit[i].getTila() == KoneenTila.EMPTYING || prosessorit[i].getTila() == KoneenTila.READY){
					progress[i] = Integer.toString(prosessorit[i].getProductPercentage()) + " %";
				}
				else if(prosessorit[i].getTila() != KoneenTila.PROCESSING || prosessorit[i].getTila() == KoneenTila.FULL || prosessorit[i].getTila() == KoneenTila.FREE){
					progress[i] = Integer.toString(prosessorit[i].getFillPercentage()) + " %";

				}
				else if(prosessorit[i].getTila() == KoneenTila.PROCESSING){
					progress[i] = Double.toString(prosessorit[i].getProgress()) + " %";
					
				}
				else{
					progress[i] = null;
				}
			}
			return progress;
		}

		/**
		 * Palauttaa tiedot, paljonko kussakin kypsytysSailiossa (sailio-luokka) on juomaa.
		 */
		public int[] sailioidenJuomanMaara() throws RemoteException {
			int[] sjm = new int[10];
			for (int i = 0; i < 10; i++){
				sjm[i] = kypsytyssailiot[i].getAmountOfLiquid();
			}
			return sjm;
		}
		
		//< > < > < > Painikkeiden tilat < > < > < >
		
		public boolean[] nappiRuuvikuljettimet() throws RemoteException {
			boolean[] napit = new boolean[3];
			for (int i = 0; i < 3; i++){
				napit[i] = ruuvikuljettimet[i].isRunning();
			}
			return napit;
		}

		public boolean[] nappiSiilot() throws RemoteException {
			boolean[] napit = new boolean[4];
			for (int i = 0; i < 4; i++){
				napit[i] = siilot[i].isReserved();
			}
			return napit;
		}

		public boolean[] nappiProsessoritReserved() throws RemoteException {
			boolean[] napit = new boolean[3];
			for (int i = 0; i < 3; i++){
				napit[i] = prosessorit[i].isReserved();
			}
			return napit;
		}

		public boolean[] nappiProsessoritStart() throws RemoteException {
			boolean[] napit = new boolean[3];
			for (int i = 0; i < 3; i++){
				napit[i] = prosessorit[i].isRunning();
			}
			return napit;
		}

		public boolean[] nappiPumput() throws RemoteException {
			boolean[] napit = new boolean[4];
			for (int i = 0; i < 4; i++){
				napit[i] = pumput[i].isRunning();
			}
			return napit;
		}

		public boolean[] nappiKypsytyssailiot() throws RemoteException {
			boolean[] napit = new boolean[10];
			for (int i = 0; i < 10; i++){
				napit[i] = kypsytyssailiot[i].isReserved();
			}
			return napit;
		}
			
		/**
		 * Alustaa kaikki tehtaan koneet ja sita mukaa kuin luo koneet, kaynnistetaan niiden Threadit.
		 */
		public void alustaKoneet(){

			//Siilot, 4 kpl
			for(int i = 0; i < siilot.length; i++){
				siilot[i] = new siilo();
			}
			
			//prosessorit, 3 kpl
			for(int i = 0; i < prosessorit.length; i++){
				prosessorit[i] = new prosessori();
				prosessorit[i].start();
			}

			//Ruuvikuljettimet, 3 kpl
			for(int i = 0; i < ruuvikuljettimet.length; i++){
				if(i == 0){
					ruuvikuljettimet[i] = new ruuvikuljetin(siilot);
					ruuvikuljettimet[i].start(); //Start thread
				} else {
					ruuvikuljettimet[i] = new prosessoriKuljetin(siilot, prosessorit, i);
					ruuvikuljettimet[i].start(); //Start thread
				}
			}
			
			//Pumput, 4 kpl
			for (int i = 0; i < pumput.length; i++){
				if(i < 2){
				pumput[i] = new sailioPumppu(prosessorit, kypsytyssailiot, i);
				} else {
					pumput[i] = new pulloPumppu(kypsytyssailiot, i);
				}
				pumput[i].start();
			}
			
			//KypsytysSailiot, 10 kpl
			for(int i = 0; i < kypsytyssailiot.length; i++){
				kypsytyssailiot[i] = new sailio();
				kypsytyssailiot[i].start();
			}
		}
			
}
