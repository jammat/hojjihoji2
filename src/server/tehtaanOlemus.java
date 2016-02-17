package server;
import java.rmi.*;
import java.util.UUID;

public interface tehtaanOlemus {
	//-------- Kirjautuminen--------
	
		public UUID login(String kayttajaNimi) throws RemoteException; // KÃ¤yttÃ¤jÃ¤ kirjautuu
		
		public String prosessorinKayttaja(int prosessori) throws RemoteException;
		
		public void logout(UUID idKey) throws RemoteException; //KÃ¤yttÃ¤jÃ¤ kirjautuu ulos
		
		//-------- Ruuvikuljettimet --------
		
		public void ruuvihihnanKaynnistys() throws RemoteException; //Eli ekan ruuvikuljettimen start nappi
		
		public void ruuvihihnanKaynnistysVapautus() throws RemoteException; 
		
		//-------- Siilot --------
		
		public void siilonVaraus(int siilonNro) throws RemoteException;
		
		public void siilonVarausVapautus(int siilonNro) throws RemoteException;
		
		//-------- Prosessorit eli keittimet --------
		
		public void prosessorinLataus(int kuljettimeNro, int maara) throws RemoteException;
		
		public void prosessorinLatausVapautus(int kuljettimeNro) throws RemoteException;
		
		public void prosessorinVaraus(int prosessorinNro, UUID userId)throws RemoteException;
		
		public void prosessorinVarausVapautus(int prosessorinNro, UUID userId)throws RemoteException;
		
		public void prosessorinKaynnistys(int prosessorinNro, UUID userId) throws RemoteException;
		
		public void prosessorinKaynnistysVapautus(int prosessorinNro) throws RemoteException;
		
		//-------- Tankkeja eli kypsytyssÃ¤iliÃ¶t ja niitÃ¤ kÃ¤sittelevÃ¤t pumput --------
		
		public void sailoidenTaytto(int pumpunNro) throws RemoteException; // pumpuilla
		
		public void sailoidenTayttoVapautus(int pumpunNro) throws RemoteException; 
		
		public void sailionVaraus(int sailionNro) throws RemoteException;

		public void sailionVarausVapautus(int sailionNro) throws RemoteException;
		
		//-------- Pumppaaminen pullotukseen --------
		
		public void pullojenTaytto(int pumpunNro) throws RemoteException;
		
		public void pullojenTayttoVapautus(int pumpunNro) throws RemoteException;
		
		//.-.-.-.-.-. Tietojen kerÃ¤Ã¤minen kÃ¤yttÃ¶liittymÃ¤n pÃ¤ivittÃ¤mistÃ¤ varten .-.-.-.-.-.
		
		public int[] siilojenAineMaara() throws RemoteException;
		
		public String[] prosessorienTila() throws RemoteException;
		
		public int[] sailioidenJuomanMaara() throws RemoteException;
		
		public String[] prosessorienEdistyminen() throws RemoteException;
		
		//< > < > < > Painikkeiden tilat < > < > < >
		
		public boolean[] nappiRuuvikuljettimet() throws RemoteException;
		
		public boolean[] nappiSiilot() throws RemoteException;
		
		public boolean[] nappiProsessoritReserved() throws RemoteException;
		
		public boolean[] nappiProsessoritStart() throws RemoteException;
		
		public boolean[] nappiPumput() throws RemoteException;
		
		public boolean[] nappiKypsytyssailiot() throws RemoteException;
		
}
