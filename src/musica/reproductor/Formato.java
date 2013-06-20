package musica.reproductor;

import java.util.StringTokenizer;

/**
 * Formato.java
 * Clase encargada de manejar los formatos de hora por ejemplo de la aplicación
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class Formato {	
	
	/**
	 * Este método nos pasa la información de la reproducción según un formato especificado
	 * @param tipo es para el tipo de datos que hemos usado ("duración" corresponde al tiempo de reproducción)
	 * @param formato es el formato en el que sacar los datos ("hh" hora, "mm" minutos, "im" minutos restantes, etc)
	 * @param duracion Es la duración total de la pista para calcular los restantes
	 * @param posicion Es la posición en la que se encuentra
	 * @return string formateado convenientemente según los parámetros de entrada
	 */
	public String aCadena(String tipo,String formato,int duracion, int posicion)
	{
		String cadena ="";
		if(tipo.equals("duracion"))
		{
			//hacemos un tokenizer con el formato y separamos los datos (es posible crear "hh:mm:ss")
			StringTokenizer st = new StringTokenizer( formato,":" );
			int tokens=st.countTokens(),i=0;
			while( i<tokens ) {
				String aux=st.nextToken();
				//con el token lo pasamos la posicion o la duracion menos la posición a segundos, minutos u horas
				if(aux.equals("hh"))
					cadena=cadena+":"+Integer.toString(posicion/ 3600000);
				else if(aux.equals("mm"))
					if((posicion/ 60000)%60<10)
						cadena=cadena+":0"+Integer.toString((posicion/ 60000)%60);
					else
						cadena=cadena+":"+Integer.toString((posicion/ 60000)%60);
				else if(aux.equals("ss"))
					if((posicion/1000)%60<10)
						cadena=cadena+":0"+Integer.toString((posicion/1000)%60);
					else
						cadena=cadena+":"+Integer.toString((posicion/1000)%60);
				else if(aux.equals("is"))
					if(((duracion/1000)-(posicion/ 1000))% 60<60)
						cadena=cadena+":0"+Integer.toString(((duracion/1000)-(posicion/ 1000))% 60);
					else
						cadena=cadena+":"+Integer.toString(((duracion/1000)-(posicion/ 1000))% 60);
				else if(aux.equals("im"))
					if(((duracion/60000)-(posicion/60000))%60<10)
						cadena=cadena+":0"+Integer.toString(((duracion/60000)-(posicion/60000))%60);
					else
						cadena=cadena+":"+Integer.toString(((duracion/60000)-(posicion/60000))%60);

				i++; //cambiamos de token
				
			}
		}
		cadena=cadena.substring(1);
		return cadena; //devolvemos la cadena
			
	}
}
