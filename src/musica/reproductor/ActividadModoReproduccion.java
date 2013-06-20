package musica.reproductor;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.RadioButton;


/**
 * ActividadModoReproduccion.java
 * Clase encargada de manejar la actividad perteneciente al modo de reproducción. Se comunica con la actividad padre
 * y devuelve resultados sobre los objetos serializables.
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadModoReproduccion extends Activity implements RadioGroup.OnCheckedChangeListener,
View.OnClickListener{
	
	 private RadioGroup mGrupoBotones;
	 private RadioButton boton0;
	 private RadioButton boton1;
	 private RadioButton boton2;
	 private RadioButton boton3;
	 private RadioButton boton4;
	 private int idBoton0;
	 private int idBoton1;
	 private int idBoton2;
	 private int idBoton3;
	 private int idBoton4;
	 private ModoReproduccion modoRepro;
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
	        setContentView(R.layout.modoreproduccion);
	        
	        //inicialización de los botones
	        mGrupoBotones = (RadioGroup) findViewById(R.id.menuModo);
	        boton0=(RadioButton) findViewById(R.id.boton0);
	        boton1=(RadioButton) findViewById(R.id.boton1);
	        boton2=(RadioButton) findViewById(R.id.boton2);
	        boton3=(RadioButton) findViewById(R.id.boton3);
	        boton4=(RadioButton) findViewById(R.id.boton4);
	        aceptar= (Button) findViewById(R.id.aceptar);
	        
	        //asociación de cada id de cada botón con un nombre reconocible. Los id's son definidos con enteros bastante largos y engorrosos.
	        idBoton0=boton0.getId();
	        idBoton1=boton1.getId();
	        idBoton2=boton2.getId();
	        idBoton3=boton3.getId();
	        idBoton4=boton4.getId();
	        
	    
	        //mediante bundle es posible extraer los elementos serializables de una actividad anterior
	        Bundle bundle = getIntent().getExtras();
	        if(bundle!=null){
	              modoRepro = (ModoReproduccion) bundle.getSerializable("modo");
	        }
	        
	        //según el modo de reproducción que estuviese seleccionado, marcamos dicho botón
	        if(modoRepro.intestado()==0)
	        	mGrupoBotones.check(idBoton0);
	        else if(modoRepro.intestado()==1)
	        	mGrupoBotones.check(idBoton1);
	        else if(modoRepro.intestado()==2)
	        	mGrupoBotones.check(idBoton2);
	        else if(modoRepro.intestado()==3)
	        	mGrupoBotones.check(idBoton3);
	        else if(modoRepro.intestado()==4)
	        	mGrupoBotones.check(idBoton4);
	        
	        //inicializamos los listeners en dichos botones
	        mGrupoBotones.setOnCheckedChangeListener(this);
	        aceptar.setOnClickListener(this);
	        
	 }

	/**
	 * Este métdo es llamado en el momento en que cambiamos un botón de estado
	 * @return no devuelve nada
	 */
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		if(mGrupoBotones.getCheckedRadioButtonId()==idBoton0)
			modoRepro.cambiaEstado(0);
		else if(mGrupoBotones.getCheckedRadioButtonId()==idBoton1)
			modoRepro.cambiaEstado(1);
		else if(mGrupoBotones.getCheckedRadioButtonId()==idBoton2)
			modoRepro.cambiaEstado(2);
		else if(mGrupoBotones.getCheckedRadioButtonId()==idBoton3)
			modoRepro.cambiaEstado(3);
		else if(mGrupoBotones.getCheckedRadioButtonId()==idBoton4)
			modoRepro.cambiaEstado(4);
		
		
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
    		this.setResult(24);
    		finish();
    	}
	}
}
