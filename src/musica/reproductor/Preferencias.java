package musica.reproductor;

import java.io.Serializable;

/**
 * Preferencias.java
 * Clase encargada de gestionar las preferencias que se pasarán como objeto serializable a la actividad
 * de preferencias, contiene entre otras cosas las direcciones de os ficheros de configuración del sistema
 * así como par�ametros que se utilizarán en las opciones.
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 */
public class Preferencias implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int maxPistasRecomendadas;
	public static int tiempoSinReproducir;
	public static String URL;
	public static String PATH_DIRPREFERENCIAS;
	public static String PATH_DIRLISTA;
	public static String PATH_DIRBBDD;
	public static String PATH_USOPISTAS;
	
	/**
	 * Constructor de la clase Preferencias
	 * Inicializa los parámetros con unos valores inicales
	 */
	Preferencias()
	{
		//asignamos un valor standard por si falla la carga del archivo
		URL="http://localhost/joomla/";
		maxPistasRecomendadas=15;
		tiempoSinReproducir=5;
		PATH_DIRPREFERENCIAS="/sdcard/"; 
		PATH_DIRLISTA="/sdcard/";
		PATH_DIRBBDD="/sdcard/";
		PATH_USOPISTAS="/sdcard/";
	}
	
	/**
	 * Constructor parametrizado de la clase preferencias
	 * @param localizacionFicheroPreferencias localización  del fichero de preferencias
	 * @param localizacionFicheroLista localización del fichero de lista de reproducción actual
	 * @param localizacionFicheroBBDD localización del fichero de la base de datos
	 * @param localizacionFicheroUsoPistas localización del fichero de uso de las pistas
	 */
	Preferencias(String localizacionFicheroPreferencias,String localizacionFicheroLista, 
			String localizacionFicheroBBDD,String localizacionFicheroUsoPistas)
	{
		URL=getURLwebdefecto();
		maxPistasRecomendadas=getMaxPistasDefecto();
		tiempoSinReproducir=getTiempoSinReproducirDefecto();
		PATH_DIRPREFERENCIAS=localizacionFicheroPreferencias;
		PATH_DIRLISTA=localizacionFicheroLista;
		PATH_DIRBBDD=localizacionFicheroBBDD;
		PATH_USOPISTAS=localizacionFicheroUsoPistas;
	}
	
	/**
	 * Método que devuelve la dirección URL del sitio web
	 * @return String con la URL
	 */
	public String getURLwebdefecto()
	{
		return "http://localhost/joomla"; 
	}
	
	/**
	 * Método que devuelve el máximo número de pistas por defecto
	 * @return int con el máximo número de  elemntos por defecto 
	 */
	public int getMaxPistasDefecto()
	{
		return 15;
	}
	
	/**
	 * Método que devuelve el máximo número de tiempo sin reproducir
	 * @return int con el número de tiempo por defecto
	 */
	public int getTiempoSinReproducirDefecto()
	{
		return 5;
	}
	
	/**
	 * Método que nos devuelve la localización del archivo del uso de las pistas
	 * @return String con el uso de las pistas
	 */
	public String getLocArchivoUsoPistas()
	{
		return PATH_USOPISTAS;
	}
	
	/**
	 * Método que inicializa el archivo de uso de las pistas
	 * @param localización del archivo
	 */
	public void setLocArchivoUsoPistas(String localizacion)
	{
		PATH_USOPISTAS=localizacion;
	}
	
	/**
	 * Método que obtiene la localización del archivo de base de datos
	 * @return String cadena con la localización del archivo
	 */
	public String getLocArchivoBBDD()
	{
		return PATH_DIRBBDD;
	}
	
	/**
	 * Método para introducir un valor con la ruta del archivo de localización de la base de datos
	 * @param localización del archivo
	 */
	public void setLocArchivoBBDD(String localizacion)
	{
		PATH_DIRBBDD=localizacion;
	}
	
	/**
	 * Método que devuelve la localización del fichero de lista de reproducción actual
	 * @return String con la dirección del archivo
	 */
	public String getLocArchivoLista()
	{
		return PATH_DIRLISTA;
	}
	
	/**
	 * Método para asociar un valor a la dirección del archivo de lista de reproducción actual
	 * @param localización del archivo
	 */
	public void setLocArchivoLista(String localizacion)
	{
		PATH_DIRLISTA=localizacion;
	}
	
	/**
	 * Método apra obtener la dirección del archivo de preferencias
	 * @return dirección con el archivo de preferencias
	 */
	public String getLocArchivoPreferencias()
	{
		return PATH_DIRPREFERENCIAS;
	}
	
	/**
	 * Método para otorgar un valor a la dirección del archivo de preferencias
	 * @param localización del archivo
	 */
	public void setLocArchivoPreferencias(String localizacion)
	{
		PATH_DIRPREFERENCIAS=localizacion;
	}
	
	/**
	 * Método para obtener el número máximo de pistas 
	 * @return int con el número máximo de pistas
	 */
	public int getMaxPistas()
	{
		return maxPistasRecomendadas;
	}
	
	/**
	 * Método para dar un valor al número máximo de pistas
	 * @param max número de pistas 
	 */
	public void setMaxPistas(int max)
	{
		maxPistasRecomendadas=max;
	}
	
	/**
	 * Método para establecer una URL
	 * @param url de la página
	 */
	public void setURLweb(String url)
	{
		URL=url;
	}
	
	/**
	 * Método para obtener una URL
	 * @param url de la página
	 */
	public String getURLweb()
	{
	 return	URL;
	}
	
	/**
	 * Método para especificar el tiempo sin reproducir de preferencias
	 * @param tiempo sin reproducir de preferencias
	 */
	public void setTiempoSinReproducir(int tiempo)
	{
		tiempoSinReproducir=tiempo;
	}
	
	/**
	 * Método para obtener el tiempo sin reproducir 
	 * @return int con el número máximo de tiempo sin reproducir
	 */
	public int getTiempoSinREproducir()
	{
		return tiempoSinReproducir;
	}
	
}
