package server;
import java.rmi.server.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RemoteServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{

			Tehdas tehdas = new Tehdas();
			
			Registry registry = LocateRegistry.createRegistry(2020);			
			System.out.println(registry);
		
			Naming.rebind("//127.0.0.1:2020/tehdas", tehdas);

		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

}
