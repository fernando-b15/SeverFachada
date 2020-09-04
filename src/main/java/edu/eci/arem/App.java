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
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
    {
    	port(getPort());
    	get("/", (req, res) ->  inputView(req, res));
    	get("/respuesta", (req, res) ->  resultsView(req, res));
    }
	 private static int getPort() {
	   	 if (System.getenv("PORT") != null) {
	   		 return Integer.parseInt(System.getenv("PORT"));
	   	 }
	   	 return 7000; //returns default port if heroku-port isn't set
	 }
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
