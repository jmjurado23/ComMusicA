package musica.reproductor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

/**
 * ActividadRecomendación.java
 * Clase encargada de manejar la actividad perteneciente al sistema de recomendación así como sus opciones
 * Se comunica con la actividad padre y devuelve resultados sobre los objetos serializables.
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadRecomendacion extends Activity implements OnClickListener, OnLongClickListener{
	
	private Button energica;
	private Button nostalgica;
	private Button tranquilizante;
	private Button alegre;
	private ToggleButton activaReco;
	public static Estado estado;
	private ImageButton imagen;
	
	/**
     * Método que es llamado al iniciar una actividad Android. Encargado de inicializar algunos elementos
     * y de llamar a los Listeners.
     * @param savedInstanceState es un parámetro con el estado guardado de una actividad anterior
     * @return No devuelve ningun valor
     * @exception excepciones No lanza ninguna
     */
	public void onCreate(Bundle savedInstanceState) 
	{
		//guardamos el estado de la actividad
		super.onCreate(savedInstanceState);
		//cargamos el layout para la actividad
		setContentView(R.layout.animo);

		//inicialización y asignación de los elementos del layout a los elementos de java
		
		tranquilizante = (Button) findViewById(R.id.botontranquilizante);
		activaReco = (ToggleButton) findViewById(R.id.botonactivar);
		nostalgica = (Button) findViewById(R.id.botonnostalgica);
		imagen= (ImageButton) findViewById(R.id.animoimagen);
		energica = (Button) findViewById(R.id.botonenergica);
		alegre = (Button) findViewById(R.id.botonalegre);
		
		//mediante bundle es posible extraer los elementos serializables de una actividad anterior
		Bundle bundle = getIntent().getExtras();    
	    if(bundle!=null)
	    {
	        estado = (Estado) bundle.getSerializable("reco");
	    }
		
	    //Dependiendo del estado del sistema de recomendación mostramos el modo o no lo mostramos
	    if(!estado.getActivo())
	    {
	    	activaReco.setChecked(false);
	    	energica.setVisibility(View.INVISIBLE);
	    	alegre.setVisibility(View.INVISIBLE);
	    	tranquilizante.setVisibility(View.INVISIBLE);
	    	nostalgica.setVisibility(View.INVISIBLE);
	    	imagen.setVisibility(View.INVISIBLE);
	    }
	    else
	    {
	    	activaReco.setChecked(true);
	    	energica.setVisibility(View.VISIBLE);
	    	alegre.setVisibility(View.VISIBLE);
	    	tranquilizante.setVisibility(View.VISIBLE);
	    	nostalgica.setVisibility(View.VISIBLE);
	    	imagen.setVisibility(View.VISIBLE);
	    }
	    
	    //inicializamos los listeners para los elementos de la actividad
	    energica.setOnClickListener(this);
	    tranquilizante.setOnClickListener(this);
	    alegre.setOnClickListener(this);
	    nostalgica.setOnClickListener(this);
	    activaReco.setOnClickListener(this);
	    
	}

	/**
     * Método que se activará cuando pulsemos un botón de nuestra aplicación
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return no devuelve nada
     * @exception excepciones No lanza ninguna
     */
	public void onClick(View v) 
	{	
		//si pulsamos en cualquier estado de animo, activamos el sistema de recomendación, cambiamos el estado, ponemos el estado de exito y finalizamos la actividad
		
		if(v.equals(energica))
		{
			estado.activarRecomendacion(true);
			estado.actualizaEstado("ENERGICA",0);
			this.setResult(25);
          	this.finish();
		}
		if(v.equals(alegre))
		{
			estado.activarRecomendacion(true);
			estado.actualizaEstado("ALEGRE",0);
			this.setResult(25);
			this.finish();
		}
		if(v.equals(tranquilizante))
		{
			estado.activarRecomendacion(true);
			estado.actualizaEstado("TRANQUILIZANTE",0);
			this.setResult(25);
			this.finish();
		}
		if(v.equals(nostalgica))
		{
			estado.activarRecomendacion(true);
			estado.actualizaEstado("NOSTALGICA",0);
			this.setResult(25);
			this.finish();
		}
		
		//Este botón tiene dos modos, desconectado y conectado, según uno u otro al hacer click cambiamos los elementos visibles en pantalla
		if(v.equals(activaReco))
		{
			if(activaReco.isChecked())
			{
		    	energica.setVisibility(View.VISIBLE);
		    	alegre.setVisibility(View.VISIBLE);
		    	tranquilizante.setVisibility(View.VISIBLE);
		    	nostalgica.setVisibility(View.VISIBLE);
		    	imagen.setVisibility(View.VISIBLE);
		    	
			}
			else
			{
				estado.activarRecomendacion(false);
		    	energica.setVisibility(View.INVISIBLE);
		    	alegre.setVisibility(View.INVISIBLE);
		    	tranquilizante.setVisibility(View.INVISIBLE);
		    	nostalgica.setVisibility(View.INVISIBLE);
		    	imagen.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
     * Método que se activará cuando pulsemos durante al menos 2 segundos un botón de nuestra aplicación
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return boolean true si no ha ocurrido ningún problema o false si ha ocurrido algún problema en el método
     * @exception excepciones No lanza ninguna
     */
	public boolean onLongClick(View v) {
		return true;
	}
}
