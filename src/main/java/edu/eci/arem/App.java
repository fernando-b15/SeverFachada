package edu.eci.arem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import static spark.Spark.*;
import java.util.ArrayList;
import spark.Request;
import spark.Response;

/**
 * Esta clase es la clase de inicio de un server fachada que para realizar los calculos se conecta a otro server principal quien realiza los calculos
 *
 */
public class App 
{
	/**
     * Este metodo main inicia el servidor Fachada y define algunas peticiones y respuestas haciendo uso 
     * de algunas funciones lambda
     */
	
	public static void main( String[] args )
    {
    	port(getPort());
    	get("/", (req, res) ->  inputView(req, res));
    	get("/respuesta", (req, res) ->  resultsView(req, res));
    }
	/**
     *Este metodo se encarga de retonar el puerto por defecto que esta definido en una variable de entorno 
     *para correr el servidor web fachada sobre ese puerto.
     */
	 private static int getPort() {
	   	 if (System.getenv("PORT") != null) {
	   		 return Integer.parseInt(System.getenv("PORT"));
	   	 }
	   	 return 7000; //returns default port if heroku-port isn't set
	 }
	 /**
	    *Este metodo contruye la vista inputView apartir del string html view que retorna  
	    *
	    * @param req Tiene la informacion de la peticion que llega al servidor.
	    * @param res Tiene la informacion con la respuesta del servidor.
	    * @return String con la informacion html de la vista de entrada.
	    */
	 private static String  inputView(Request req, Response res) {
		    String view = "<!DOCTYPE html>"
		            + "<html>"
		            + "<body style=\"background-color:#CCCC00;\">"
		            +"<center>"
		            + "<h2>Calculadora Trigonometrica</h2>"
		            + "  <br><br>"
					+ "<h2>Intrucciones : Porfavor ingrese la funcion que desea calcular pueder ser sin,cos o tan y escriba el numero en radianes</h2>"
					+ "<form action=\"/respuesta\">"
					+"Funcion Trogometrica: "
					+ "  <input type=\"text\" name=\"funcion\">"
		            + "  <br><br>"
		            +"Numero en Radianes: "
		            + "  <input type=\"text\" name=\"datos\">"
		            + "  <br><br>"
		            + "  <input type=\"submit\" value=\"Calcular\">"
		            + "</form>"
		            +"</center>"
		            + "</body>"
		            + "</html>";
		    return view;
			}
	 /**
	    *Este metodo contruye la vista resultView apartir una peticion que le solicita a otro server principal y simplemente retorna
	    *la respuesta que el server principal le respondio ya que el server principal ya se encargo de realizar los calculos en base a los datos que contenia
	    *la peticion de entrada de la funcion 
	    *
	    * @param req Tiene la informacion de la peticinn que llega al servidor.
	    * @param res Tiene la informacinn con la respuesta del servidor.
	    * @return String con la informacion html de la vista de entrada.
	    */
	 private static String  resultsView(Request req, Response res) {
		 String view="";
		 try {
			URL url = new URL("https://parcialarepserver.herokuapp.com/respuesta?funcion="+req.queryParams("funcion")+"&datos="+req.queryParams("datos"));
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
	                System.out.println(inputLine);
	                view+=inputLine;
	        }
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return view;
		 
    }
}
