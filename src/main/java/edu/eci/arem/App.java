package edu.eci.arem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static spark.Spark.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.eci.arem.reader.URLReader;
import spark.Filter;
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
	private static Map<String, String> userdates = new HashMap<>();
	public static void main( String[] args ) throws NoSuchAlgorithmException
    {
    	port(getPort());
    	userdates.put("fernando",encode("holi123"));
    	userdates.put("admin",encode("admin"));
    	secure("keystores/ecikeystore.p12","123456","keystores/myTrustStore", "789456");
    	get("/", (req, res) ->  loginView(req, res));
    	post("/home", (req, res) ->  inputView(req, res));
    	before("/home",new Filter() {
            @Override
            public void handle(Request request, Response response) throws NoSuchAlgorithmException {
                String user = request.queryParams("user");
                String password = encode(request.queryParams("password"));
                System.out.println(user);
                System.out.println(password);
                if (!(userdates.containsKey(user) && password.equals(userdates.get(user)))) {
                    halt(401, "You are NOT welcome here!!!");
                }
            }
        });
    	get("/respuesta", (req, res) ->  resultsView(req, res));
    	after("/home", (request, response) -> {
            response.header("spark", "added by after-filter");
        });
    }
	/**
     *Este metodo se encarga de retonar el puerto por defecto que esta definido en una variable de entorno 
     *para correr el servidor web fachada sobre ese puerto.
     */
	 public static String encode(String text) throws NoSuchAlgorithmException {
		 MessageDigest md = MessageDigest.getInstance( "SHA-256" );
	     md.update( text.getBytes( StandardCharsets.UTF_8 ) );
	     byte[] digest = md.digest();
	     String hex = String.format( "%064x", new BigInteger( 1, digest ) );
	     return  hex;
		 
	 }  
	 private static int getPort() {
	   	 if (System.getenv("PORT") != null) {
	   		 return Integer.parseInt(System.getenv("PORT"));
	   	 }
	   	 return 5000; //returns default port if heroku-port isn't set
	 }
	 /**
	    *Este metodo contruye la vista inputView apartir del string html view que retorna  
	    *
	    * @param req Tiene la informacion de la peticion que llega al servidor.
	    * @param res Tiene la informacion con la respuesta del servidor.
	    * @return String con la informacion html de la vista de entrada.
	    */
	 
	 private static String  loginView(Request req, Response res){
	        String view = "";

	        view = "<!DOCTYPE html>"
	                + "<html>"
	                +"<body style=\"background-color:#3374FF;\">"
	                +"<center>"
	                +"<h1>Welcome Login Server Fachada</h1>"
	                +"<br/>"
	                +"<h2>Por favor loguese para acceder al servicio</h2>"
	                +"<form name='loginForm' method='post' action='/home'>"
	                +"Usuario:<input type='text' name='user'/> <br/>"
	                + "<br><br>"
	                +"Contraseña:<input type='password' name='password'/> <br/>"
	                + "<br><br>"
	                +"<input type='submit' value='Login' />"
	                +"</form>"
	                +"</center>"
	                + "</body>"
	                + "</html>";

	        return view;
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
			view = URLReader.Reader(req.queryParams("funcion"), req.queryParams("datos"));
			System.out.println("alfa       "+view);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return view;
		 
    }
}
