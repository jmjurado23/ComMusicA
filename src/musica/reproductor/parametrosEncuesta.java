package musica.reproductor;

import java.io.Serializable;

/**
 * ParametrosEncuesta.java
 * Clase serializable que se usará para el paso de parámetros entre la actividad principal
 * y la actividad de encuesta.
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 */
public class parametrosEncuesta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static boolean ranimo;
    public static boolean rhora;
    public static boolean rdia;
    
    parametrosEncuesta ()
    {
    	ranimo=false;
    	rhora=false;
    	rdia=false;
    }
    
    /**
     * Método que pone el parámetro animo de la encuestas a un valor boolean
     * @param animo 
     */
    public void setAnimo(boolean animo)
    {
    	ranimo=animo;
    }
    
    /**
     * Método que pone el parámetro hora de la encuestas a un valor boolean
     * @param hora 
     */
    public void setHora(boolean hora)
    {
    	rhora=hora;
    }
    
    /**
     * Método que pone el parámetro dia de la encuestas a un valor boolean
     * @param dia
     */
    public void setDia(boolean dia)
    {
    	rdia=dia;
    }
    
    /**
     * Método que da el valor de la variable animo
     * @return ranimo booleano 
     */
    public boolean getAnimo()
    {
    	return ranimo;
    }
    
    /**
     * Método que da el valor de la variable hora
     * @return rhora booleano 
     */
    public boolean getHora()
    {
    	return rhora;
    }
    
    /**
     * Método que da el valor de la variable dia
     * @return rdia booleano 
     */
    public boolean getDia()
    {
    	return rdia;
    }
    
}
