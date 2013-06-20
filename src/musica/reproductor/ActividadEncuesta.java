package musica.reproductor;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;


/**
 * ActividadModoReproduccion.java
 * Clase encargada de crear un encuesta para bajar los parámetros a una pista que ha sido recomendada de forma errónea
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadEncuesta extends Activity implements View.OnClickListener{
	
	 private CheckBox checkAnimo;
	 private CheckBox checkHora;
	 private CheckBox checkDia;
	 private parametrosEncuesta encuesta;
	 private Button aceptar;
	 
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
	        setContentView(R.layout.encuesta);
	        
	        //inicialización de los botones
	        checkAnimo=(CheckBox) findViewById(R.id.check0);
	        checkHora=(CheckBox) findViewById(R.id.check1);
	        checkDia=(CheckBox) findViewById(R.id.check2);
	        aceptar= (Button) findViewById(R.id.aceptar);
	        
	        //creamos un objeto de la clase encuesta
	        encuesta=new parametrosEncuesta();
	    
	        //mediante bundle es posible extraer los elementos serializables de una actividad anterior
	        Bundle bundle = getIntent().getExtras();
	        if(bundle!=null){
	              encuesta = (parametrosEncuesta)bundle.getSerializable("encuesta");
	        }
	        
	        
	        //inicializamos los listeners en dichos botones
	        checkAnimo.setOnCheckedChangeListener
	        (
	                new OnCheckedChangeListener()
	                {
	                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	                    {
	                        if(isChecked)
	                        	encuesta.setAnimo(true);
	                        else
	                        	encuesta.setAnimo(false);
	                       
	                    }                           
	                }
	            );
	        
	        checkHora.setOnCheckedChangeListener
	        (
	                new OnCheckedChangeListener()
	                {
	                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	                    {
	                        if(isChecked)
	                        	encuesta.setHora(true);
	                        else
	                        	encuesta.setHora(false);
	                    }                           
	                }
	            );
	        
	        checkDia.setOnCheckedChangeListener
	        (
	                new OnCheckedChangeListener()
	                {
	                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	                    {
	                        if(isChecked)
	                        	encuesta.setDia(true);
	                        else
	                        	encuesta.setDia(false);
	                    }                           
	                }
	            );
	        //iniciamos el listener del botón aceptar
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
    		this.setResult(30);
    		finish();
    	}
	}


	
}
