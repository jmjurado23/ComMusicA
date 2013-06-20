package musica.reproductor;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * ActividadEstadoSistema.java
 * Clase que se encarga de la pantalla de estado del reproductor
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadEstadoSistema extends Activity implements View.OnClickListener{
	
	 private TextView animo;
	 private TextView dia;
	 private TextView hora;
	 private Button aceptar;
	 private Estado estado;
	 /**
      * Método que es llamado al iniciar una actividad Android. Encargado de inicializar algunos elementos
      * y de llamar a los Listeners.
      * @param savedInstanceState es un parámetro con el estado guardado de una actividad anterior
      * @return No devuelve ningun valor
      * @exception excepciones No lanza ninguna
      */
	 public void onCreate(Bundle savedInstanceState) {
		 	//guardado del estado al iniciar esta actividad	
	    	super.onCreate(savedInstanceState);
	    	//obtenemos el layout para la actividad
	        setContentView(R.layout.estado);
	        
	        //inicialización de los botones y texto
	        animo = (TextView) findViewById(R.id.animoestado);
	        dia = (TextView) findViewById(R.id.diaestado);
	        hora = (TextView) findViewById(R.id.horaestado);
	        aceptar= (Button) findViewById(R.id.aceptar);
	        
	        //mediante bundle es posible extraer los elementos serializables de una actividad anterior
	        Bundle bundle = getIntent().getExtras();
	        if(bundle!=null){
	              estado = (Estado) bundle.getSerializable("estado");
	        }
	        animo.setText(estado.getAnimo());
	        dia.setText(estado.getDia());
	        hora.setText(estado.getHora());
	        aceptar.setOnClickListener(this);
	        
	 }

	
	/**
     * Método que se activará cuando pulsemos un botón de nuestra aplicación
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return no devuelve nada
     * @exception excepciones No lanza ninguna
     */
	public void onClick(View v) {
		//si damos a aceptar mandamos el resultado a la actividad padre y finalizamos esta
    	if(v.equals(aceptar))
    	{
    		this.setResult(26);
    		finish();
    	}
	}
}
