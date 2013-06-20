package musica.reproductor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ActividadGuardaLista.java
 * Clase que se encarga de la lista de reproducción actual del reproductor
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadGuardaLista extends Activity implements View.OnClickListener {

	 
	 private EditText nombrePL;
	 private Button cancelar;
	 private Button guardar;
	 private ListaDeReproduccionActual lista;
	 private String[] listas;
	 
	 /**
      * Método que es llamado al iniciar una actividad Android. Encargado de inicializar algunos elementos
      * y de llamar a los Listeners.
      * @param savedInstanceState es un parámetro con el estado guardado de una actividad anterior
      * @return No devuelve ningun valor
      * @exception excepciones No lanza ninguna
      */
	 public void onCreate(Bundle savedInstanceState) 
	 {
		 	//guardado del estado al iniciar esta actividad	
	    	super.onCreate(savedInstanceState);
	    	//obtenemos el layout para la actividad
	        setContentView(R.layout.guardar);
	        
	        //inicialización de los botones y texto
	        nombrePL = (EditText) findViewById(R.id.editTextGuardarPL);
	        guardar = (Button) findViewById(R.id.BotonAceptarPL);
	        cancelar = (Button) findViewById(R.id.BotonCancelarPL);
	        
	        
	        //mediante bundle es posible extraer los elementos serializables de una actividad anterior
	        Bundle bundle = getIntent().getExtras();
	        if(bundle!=null){
	              lista = (ListaDeReproduccionActual) bundle.getSerializable("lista");
	        }
	        //si la lista que hemos usado tiene nombre ponermos el que tendría
	        if(!lista.getNombre().equals(""))
	        	nombrePL.setText(lista.getNombre());
	        
	        //lanzamos los listeners de los botones
	        cancelar.setOnClickListener(this);
	        guardar.setOnClickListener(this);
	        nombrePL.setOnClickListener(this);
	 }
	 
	 /**
	  * Método que guarda la lista de reproducción
	  * @return no devuelve nada
	  */
	 public void guardarLista()
	 {
		 if(lista!=null) //si existe una lista de reproducción
		 {
			//creamos un fichero con la lista
			FileWriter fichero=null;
			try {
				fichero = new FileWriter("/sdcard/ComMusicA/PlayList/"+nombrePL.getText().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			 PrintWriter pw = new PrintWriter(fichero);
			//guardamos en el las direcciones de las pistas 
			 for(int i=0;i<lista.getLista().size();i++)
			 {
				 pw.println(lista.getLista().get(i));
			 }
			 if (null != fichero)
				try {
					fichero.close(); //cerramos el fichero
				} catch (IOException e) {
					e.printStackTrace();
				}
		 }
		 
		 //mostramos un mensaje indicando que la lista se ha guardado
		 Toast.makeText(
	                getBaseContext(),
	                R.string.lalista+nombrePL.getText().toString()+R.string.lalistacor ,
	                Toast.LENGTH_SHORT).show();
		 
		 //Se ha guardado con éxito la lista de reproducción y salimos de la actividad
		 this.setResult(29);
		 finish();
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
		//si hemos pulsado el texto editable
		if(v.equals(nombrePL)) 
		{
			//selecionamos el texto
			nombrePL.setSelected(true);
			nombrePL.setFocusable(true);
			nombrePL.setSelectAllOnFocus(true);
			
		}
		
		//si pulsamos cancelar, salimos
		if(v.equals(cancelar))
		{
			finish();
		}
		
		//si pulsamos guardar
		if(v.equals(guardar))
		{
			//se coprueba que exista el directorio de las listas y se crea en caso de no estar
			File dir = new File("/sdcard/ComMusicA/PlayList/");
        	String[] ficheros = dir.list();
        	
        	//obtenemos el nombre de todas las listas que hay
        	listas= new String[(ficheros.length+1)];
        	if(listas.length!=1)
        	{
        		for(int i=0;i<ficheros.length;i++)
        			listas[i+1]=ficheros[i];
        	}
        	listas[0]=getString(R.string.todaspistas); //creamos la lista con todas las pistas de forma predeterminada
        	String nombre=nombrePL.getText().toString();
        	
        	if(nombre.equals(listas[0]))
        	{
        		//mostrmos un mensaje con lo seleccionado
    	        Toast.makeText(
    	                getBaseContext(),
    	                R.string.otronombre,
    	                Toast.LENGTH_SHORT).show();
        	}
        	
        	 //comprobamos el nombre que hemos puesto con el de las listas
        	for(int i=1;i<listas.length;i++)
        	{
        		if(nombre.equals(listas[i]))
        		{
        			showDialog(999);  //en caso de que un nombre coincida, lanzamos un diálogo de sobreescritura
        			return;
        		}
        	}
        	
        	//llamamos al método para guardar las listas
        	guardarLista();
		}
        	
		}
	
		//creamos un diálogo de sobreescritura
		protected Dialog onCreateDialog(int id) 
		 { 
	        switch (id) 
	        { 
	         case (999): 
	            return new AlertDialog.Builder(this) 
	                .setTitle(R.string.listaexiste) 
	                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() { 
	                    public void onClick(DialogInterface dialog, int whichButton) { 
	                        guardarLista(); //si aceptamos sobreescribir, pasamos a guardarla
	                    	
	                    } 
	                }) 
	                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() { 
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	return; //en caso de cancelar, volvemos a la actividad cerrando el cuadro de diálogo
	                    } 
	                }) 
	                .create(); //lo creamos
	        } 
	        return null; 
		  } 
}
