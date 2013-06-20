package musica.reproductor;

/**
 * Tabla.java 
 * Clase creada para el manejo de las tablas d ela base de datos. Podría verse con un handler. Esta clase nos da la posibilidad de crear elementos listados
 * @author  Juan Manuel Jurado Ruiz	
 * @author  <i72juruj@uco.es>
 * @version  1.0
 */
public class Tabla {
	private String nombre;
	
	/**
	 * Constructor parametrizado que necesita del nombre de la tabla
	 * @param nombre de la tabla
	 */
	Tabla(String nombre)
	{
		this.nombre=nombre;
	}
	
	/**
	 * Método que nos devuelve el nombre de la tabla
	 * @return  cadena con el nombre
	 */
	public String getNombre()
	{
		return nombre;
	}
}
