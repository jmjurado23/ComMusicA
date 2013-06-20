package musica.reproductor;

/**
 * Reproduciendo.java
 * Clase encargada de gestionar la posición y la duración de la pista que está sonando
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class Reproduciendo {

	private int duracion;
	private int posicionactual;
	private String nombre;
	
	/**
	 * Constructor de la clase Reproduciendo
	 * @param nombre asigna el nombre a la canción actual
	 * @param duracion Asigna la duración de la canción actual
	 */
	Reproduciendo(String nombre,int duracion)
	{
	    this.duracion=duracion;
	    posicionactual=-1;
	    this.setNombre(nombre);
	}
	
	/**
	 * Este método devuelve la posición actual que por defecto coge el valor de -1
	 * @return entero con la posición actual
	 */
	public int getPosicionActual()
	{
		return posicionactual;

	}
	
	/**
	 * Este método devuelve la duración de la pista que debe ser siempre inicializada con el constructor
	 * @return  entero con la posición actual
	 */
	public int getDuracion()
	{
			return duracion;
	}
	
	/**
	 * Este método nos permite definir una posición actual que se irá actualizando convenientemente
	 * @param posicion entero con al posición actual
	 */
	public void setPosicionActual(int posicion)
	{
		posicionactual=posicion;
	}

	/**
	 * Mñetodo que nos permite definir el nombre de la pista actual
	 * @param nombre  cadena del nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * método que nos permite obtener el nombre de la canción que está sonando actualmente
	 * @return  string con el nombre de la pista
	 */
	public String getNombre() {
		return nombre;
	}

}
