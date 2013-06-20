package musica.reproductor;

import java.io.Serializable;

/**
 * ModoReproducción.java
 * Claseque gestiona lo referente al modo de reproducción (normal, repetición, aleatorio ,etc)
 * El manejo de la clase se hará mediante enteros
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 */
public class ModoReproduccion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8435549145873450630L;
	public static String[] sestado= {"normal", "repetir una","repetir todas","aleatorio","aleatorio repetir"}; 
	public static int iestado=0;
	
	/**
	 * Costructor parametrizado de la clase ModoReproduccion
	 * que según un estado incial es inicializado
	 * @param estadoInicial es el estado que puede ser ("normal", "repetir una","repetir todas","aleatorio","aleatorio repetir")
	 */
	ModoReproduccion (String estadoInicial)
	{
		for(int i=0;i<sestado.length;i++)
			if("normal".equals(estadoInicial))
				iestado=0;
			else if("repetir una".equals(estadoInicial))
				iestado=1;
			else if("repetir todas".equals(estadoInicial))
				iestado=2;
			else if("aleatorio".equals(estadoInicial))
				iestado=3;
			else if("aleatorio repetir".equals(estadoInicial))
				iestado=4;
	}
	/**
	 * Constructor vacío de la clase ModoReproducción que pone el estado de entero a normal
	 */
	ModoReproduccion ()
	{
		iestado=0;
	}
	
	/**
	 * Este método nos devuelve el estado actual en modo cadena
	 * @return string con el modo de reproduccióne en el que estemos
	 */
	public String estadoActual ()
	{
		return sestado[iestado];
	}
	
	/**
	 * Este método nos devuelve un vector de cadenas con los métodos de reproducción de los que dispone la clase
	 * @return sestado que es un vector de cadenas
	 */
	public String[] estados()
	{
		return sestado;
	}
	
	/**
	 * Método que nos permite cambiar de un estado a otro a partir de un entero. Cada entero 
	 * tiene una referencia con un elemento del vector de strings
	 * @param n entero que no puede pasar de 0-4
	 */
	public void cambiaEstado(int n)
	{
		if(n>=0 && n<=4)
			iestado=n;
	}
	
	/**
	 * Método que nos devuelve el modo en el que estamos esta vez en forma de entero
	 * @return entero con el eltado en el que estamos
	 */
	public int intestado()
	{
		return iestado;
	}
}
