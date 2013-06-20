package musica.reproductor;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * ActividadDetallesPista.java: ActividadDetallesPista.java
 * Clase principal que muestra toda la información de una pista almacenada en la base de datos de Android
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadDetallesPista extends Activity implements OnClickListener{
	
	 private TextView cancion;
	 private TextView duracion;
	 private TextView anno;
	 private TextView album;
	 private TextView artista;
	 private TextView directorio;
	 private TextView track;
	 private LinearLayout aceptar;
     private Formato formato;
	 private String[] proyection={
	    		MediaStore.Audio.Media._ID,
	    		MediaStore.Audio.Media.DATA,
	    		MediaStore.Audio.Media.TITLE,
	    		MediaStore.Audio.Media.ARTIST,
	    		MediaStore.Audio.Media.DURATION,
	    		MediaStore.Audio.Media.ALBUM,
	    		MediaStore.Audio.Media.YEAR,
	    		MediaStore.Audio.Media.TRACK
	    };
	 
	 private Cursor cursor=null;
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
    	//cargamos el layout en la vista
        setContentView(R.layout.detalles);
        
        //asignamos los recursos del layout a los layout definidos en la clase para modificarlos
        cancion = (TextView) findViewById(R.id.detpista);
        duracion = (TextView) findViewById(R.id.detduracion);
        anno = (TextView) findViewById(R.id.detanno);
        album = (TextView) findViewById(R.id.detalbum);
        directorio = (TextView) findViewById(R.id.detdirectorio);
        artista = (TextView) findViewById(R.id.detartista);
        track = (TextView) findViewById(R.id.dettrack);
        aceptar= (LinearLayout) findViewById(R.id.detlayout);
        
        //inicialización del formateador de la duración de la pista
        formato=new Formato();
        
        //recuperamos el data para buscar todos los datos en la base de datos
        Bundle b = getIntent().getExtras();
        String nombre=(String)b.get("nombrePista");
        
        //Creamos el where de la consulta
        String seleccion=MediaStore.Audio.Media.DATA+ " = \"" +nombre+"\"";
        
        try
        {
	        //obtenemos el cursor
			cursor=this.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proyection, seleccion,null,null);
			if(cursor!=null)
			{
				cursor.moveToFirst(); //lo movemos al principio
				if(!cursor.isNull(0))
				{
					cancion.setText(cursor.getString(2));
					duracion.setText(formato.aCadena("duracion","mm:ss",cursor.getInt(4),cursor.getInt(4)));
					anno.setText(cursor.getString(6));
					album.setText(cursor.getString(5));
					directorio.setText(cursor.getString(1));
					artista.setText(cursor.getString(3));
					track.setText(cursor.getString(7));
				}
				
			}
        }catch(Throwable e) {
			e.printStackTrace();
        }	
        //iniciamos el iniciador de escucha del botón
        aceptar.setOnClickListener(this);
    }
    
	
	public void onClick(View v) {
		//si damos a aceptar mandamos el resultado a la actividad padre y finalizamos esta
    	if(v.equals(aceptar))
    	{
    		finish();
    	}
	}

	
}
