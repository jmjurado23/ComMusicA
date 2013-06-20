package musica.reproductor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * BibliotecaL.java
 * Clase encargada de manejar la actividad perteneciente a la biblioteca
 * Se comunica con la actividad padre y devuelve resultados sobre los objetos serializables.
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadBibliotecaL extends Activity implements OnLongClickListener, OnClickListener {
	
	private ImageButton album;
    private ImageButton artista;
    private ImageButton listaReproduccion;
    private ImageButton anno;
    
    /**
     * Método que es llamado al iniciar una actividad Android. Encargado de inicializar algunos elementos
     * y de llamar a los Listeners.
     * @param savedInstanceState es un parámetro con el estado guardado de una actividad anterior
     * @return No devuelve ningun valor
     * @exception excepciones No lanza ninguna
     */
    public void onCreate(Bundle savedInstanceState) {
    	//guardamos el estado de la actividad
    	super.onCreate(savedInstanceState);
    	//cargamos el layout para la actividad
        setContentView(R.layout.biblioteca);
        
        //inicialización y asociación de los elementos del layout con los de la actividad
        album = (ImageButton) findViewById(R.id.album);
        artista = (ImageButton) findViewById(R.id.artista);
        listaReproduccion = (ImageButton) findViewById(R.id.listaReproduccion);
        anno = (ImageButton) findViewById(R.id.anno);
        
        //inicialización de los listeners
        album.setOnClickListener(this);
        artista.setOnClickListener(this);
        listaReproduccion.setOnClickListener(this);
        anno.setOnClickListener(this);
   
        album.setOnLongClickListener(this);
        artista.setOnLongClickListener(this);
        listaReproduccion.setOnLongClickListener(this);
        anno.setOnLongClickListener(this);
        
    }
    
  
    /**
     * Método que se activará cuando pulsemos durante al menos 2 segundos un botón de nuestra aplicación
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return boolean true si no ha ocurrido ningún problema o false si ha ocurrido algún problema en el método
     * @exception excepciones No lanza ninguna
     */
    public boolean onLongClick(View v)
    {
    	
    	//para cualquier click largo, mostramos el nombre de la imágen
    	if(v.equals(album)){
    		Toast.makeText(ActividadBibliotecaL.this,R.string.album,50).show();
    		return true;
    	}
    	if(v.equals(artista)){
    		Toast.makeText(ActividadBibliotecaL.this,R.string.artista,50).show();
    		return true;
    	}
    	if(v.equals(listaReproduccion)){
    		Toast.makeText(ActividadBibliotecaL.this,R.string.playlist,50).show();
    		return true;
    	}
    	if(v.equals(anno)){
    		Toast.makeText(ActividadBibliotecaL.this,R.string.anno,50).show();
    		return true;
    	}
		return false;
    }

    /**
     * Este Método es el encargado de recoger todos los resultados producidos por las actividades
     * @param requestCode es el número de la actividad que ha acabado
     * @param resultCode es el número que determina e resultado de dicha operación
     * @param data si la actividad ha devuelto un dato, lo inserta en data
     * @return no devuelve nada
     */
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
    	if(requestCode==2)
           if (resultCode == 2) {
          	 this.setResult(23);
             finish();
            }
    	
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
		//para cada opción cremso una actividad y le añadimos el tipo de botón pulsado así como la lista de reproducción que viene de la actividad padre
		if(v.equals(album)){
	    	Intent intent = new  Intent(ActividadBibliotecaL.this, ActividadPreListado.class );
	    	intent.putExtra("id", "album");
	    	intent.putExtra("lista",(ListaDeReproduccionActual)getIntent().getExtras().get("lista"));
	    	startActivityForResult(intent, 2);
	    	
	   	}
	   	if(v.equals(artista)){
	   		Intent intent = new  Intent(ActividadBibliotecaL.this, ActividadPreListado.class );
	    	intent.putExtra("id", "artista");
	    	intent.putExtra("lista", (ListaDeReproduccionActual)getIntent().getExtras().get("lista"));
	    	startActivityForResult(intent, 2);
	   	
	   	}
	   	if(v.equals(listaReproduccion)){
	   		Intent intent = new  Intent(ActividadBibliotecaL.this, ActividadPreListado.class );
	    	intent.putExtra("id", "listaReproduccion");
	    	intent.putExtra("lista",(ListaDeReproduccionActual)getIntent().getExtras().get("lista"));
	    	startActivityForResult(intent, 2);
	  
	   	}
	   	if(v.equals(anno)){
	   		Intent intent = new  Intent(ActividadBibliotecaL.this, ActividadPreListado.class );
	    	intent.putExtra("id", "anno");
	    	intent.putExtra("lista",(ListaDeReproduccionActual)getIntent().getExtras().get("lista"));
	    	startActivityForResult(intent, 2);
	   	}
		    
	}
    
}
