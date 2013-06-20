package musica.reproductor;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * ActividadacercaDe.java: ActividadAcercaDe.java
 * Actividad encargada de mostrar un texto con los datos acerca de ComMusica 
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadAcercaDe extends Activity implements OnClickListener{
	
	private TextView texto;
	
	/**
     * Método que es llamado al iniciar una actividad Android. Encargado de inicializar algunos elementos
     * y de llamar a los Listeners.
     * @param savedInstanceState es un parámetro con el estado guardado de una actividad anterior
     * @return No devuelve ningun valor
     * @exception excepciones No lanza ninguna
     */
	protected void onCreate(Bundle savedInstanceState) {
        //guardamos el estado de la actividad anterior
        super.onCreate(savedInstanceState);
        
        //ponemos un icono en el título
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        //cargamos el layout
        setContentView(R.layout.acercade);
        
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, 
                android.R.drawable.ic_dialog_alert);
        
        //editamos el texto con un código html
        texto=(TextView) findViewById(R.id.acercade);
        texto.setText(Html.fromHtml("<p><b>ComMusica</b></p><p>Juan Manuel Jurado Ruiz " +
        		"</p><p><i>jmj23elviso@gmail.com</i><p><p>Aplicación de " +
        		"sistema de recomendación musical para Android</p>"),BufferType.SPANNABLE);
        texto.setOnClickListener(this);
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
		//si pulsamos cualquier cosa, salimos de la actividad
		this.finish();
	}
}


