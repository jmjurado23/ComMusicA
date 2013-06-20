package musica.reproductor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;

/**
 * ListaDeReproduccionActual.java
 * Clase encargada de gestionar las listas de reproducción, a�adiendo elementos a la lista e incluso creando un cursor de la base de datos
 * para simplificar el trabajo a las clases que hacen uso de ella
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 */
public class ListaDeReproduccionActual extends ListActivity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4149138924916185263L;
	public static List<String> listaReproduccion;
	private String nombre="";
	
	/**
	 * constructor vacío de la clase ListaDeReproducciónActual. Inicializa la lista de cadenas vacía
	 */
	ListaDeReproduccionActual()
	{
		listaReproduccion= new ArrayList<String>();
	}
	
	/**
	 * Método que nos sirve para asignar un nombre a la lista de reproducción actual
	 * @param  nombre
	 */
	public void setNombre(String nombre)
	{
		if(!listaReproduccion.isEmpty())
			this.nombre=nombre;
	}
	
	/**
	 * Método que nos devuelve el nombre de la lista de reproduccion
	 * @return  nombre de la lista de reproducción
	 */
	public String getNombre()
	{
		return nombre;
	}
	
	/**
	 * A�ade una pista a la lista actual, pero sólo en la lista  y no al cursor. Es un objeto synchromized 
	 * @param ID de la canción a insertar
	 */
	public synchronized void anadirAListaActual(String ID)
	{
		listaReproduccion.add(ID);
		nombre="lista modificada";
	}
	
	/**
	 * Nos devuelve el tama�o de la lista de reproducción en este momento
	 * @return entero
	 */
	public int tamano()
	{
		return listaReproduccion.size();
	}
	
	/**
	 * Quita un elemento de la lista según la id de la pista
	 * @param ID de la pista que queremos quitar de la lista de reproducción
	 * @return boolean true si hay éxito en el borrado o false si el elemento no está
	 */
	public synchronized boolean quitarDeListaActual(String ID)
	{
		int pos=listaReproduccion.indexOf(ID);
		if(pos!=-1)
		{
			listaReproduccion.remove(pos);
			if(listaReproduccion.isEmpty()) {
			}
			nombre="lista modificada";
			return true;
		}
		return false;	
	}
	
	/**
	 * Nos devuelve si la lista está vacía o si no
	 * @return True si vacía o false ni la lista contiene elementos
	 */
	public boolean estaVacia()
	{
		return listaReproduccion.isEmpty();
	}
	
	/**
	 * Este método nos devuelve el elemento que ocupa la posición pos
	 * @param pos posición del elemento que queremos encontrar en la lista
	 * @return devuelve un string con el id de la pista o un null en el caso de no encontrarlo
	 */
	public String verElemento(int pos)
	{
		if(pos>=0 && pos<listaReproduccion.size())
			return listaReproduccion.get(pos);
		return null;
	}
	
	
	/**
	 * Esta clase  nos devuelve la lista de string que tiene esta clase
	 * @return List<String> con las canciones
	 */
	public List<String> getLista()
	{
		return listaReproduccion;
	}
	
	
	/**
	 * Este método nos permite vaciar la lista de reproducción
	 * @return true si la lista se ha vaciado o false si no lo ha conseguido
	 */
	public boolean vaciarLista()
	{
		if(listaReproduccion!=null)
		listaReproduccion.clear();
		nombre="";
		if (estaVacia())
			return true;
		else
			return false;
	}
	
	
}
