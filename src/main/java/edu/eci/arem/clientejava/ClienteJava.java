package edu.eci.arem.clientejava;

import static spark.Spark.get;
import static spark.Spark.port;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ClienteJava {


		public static void main( String[] args )
	    {
			URL urlServerCalculadora;
			try {
				urlServerCalculadora = new URL("https://parcialarepserver.herokuapp.com/respuesta?funcion=sin"+"&datos=1.5839");
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlServerCalculadora.openStream()));
				String inputLine;
				while ((inputLine = reader.readLine()) != null) {
						System.out.println("Respuesta Peticion a Server Principal de Calcular Seno de 1.5839");
		                System.out.println(inputLine);		               
		        }
				urlServerCalculadora = new URL("https://serverfachadaparcialarep.herokuapp.com/respuesta?funcion=cos&datos=1.5839");
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(urlServerCalculadora.openStream()));
				String inputLine2;
				while ((inputLine2 = reader2.readLine()) != null) {
						System.out.println("Respuesta Peticion a Server Fachada de Calcular Coseno de 1.5839");
		                System.out.println(inputLine2);		               
		        }
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    }
	

}