package musica.reproductor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Listado.java
 * Clase encargada de gestionar la actividad de mostrar un listado con el artista, album, a�o 
 * o lista de reproducci�n seleccionado en prelistado
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadListado extends ListActivity implements OnClickListener, OnLongClickListener {
	private String[] proyection={
    		MediaStore.Audio.Media._ID,
    		MediaStore.Audio.Media.DATA,
    		MediaStore.Audio.Media.TITLE,
    		MediaStore.Audio.Media.ARTIST,
    		MediaStore.Audio.Media.DURATION
    		
    };
	private Cursor cursor=null;
    //seleccionador del cursor
    String[] sel ;
    String ordenadoPor= MediaStore.Audio.Media.ARTIST ;
    private String[][] items; 
    private String tipo;
    private String consultawhere;
    private ListaDeReproduccionActual listaR;
    private AlertDialog.Builder  builder ;
    private ActividadListado actividad;
    private TextView Funcion;
    
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
    	//asociamos el layout al view
        setContentView(R.layout.listado);
        //creamos la selecci�n
        sel=new String[1];
        //obtenemos el tipo de busqueda y el id. Por ejemplo Album y nombre de album o Artista y nombre de artista
        tipo =getIntent().getExtras().getString("tipo");
        consultawhere=getIntent().getExtras().getString("id");
        //obtenemos la lista de reproducci�n que nos llega desde reproductorMusica,bibliotecaL y prelistado
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            listaR = (ListaDeReproduccionActual) bundle.getSerializable("lista");
        }
        //mostramos un mensaje con lo que vamos a consultar
        Toast.makeText( getBaseContext(),consultawhere,Toast.LENGTH_SHORT).show();
        //inicializamos el título de la actividad
        Funcion = (TextView) findViewById(R.id.funcion);
        
        if(tipo.equals("artista"))
        {
        	Funcion.setText(R.string.artista);
        	Funcion.setText(Funcion.getText()+" : "+consultawhere);
        }
         else if(tipo.equals("album"))
         {
         	Funcion.setText(R.string.album);
         	Funcion.setText(Funcion.getText()+" : "+consultawhere);
         }
         else if(tipo.equals("anno"))
         {
         	Funcion.setText(R.string.anno);
         	Funcion.setText(Funcion.getText()+" : "+consultawhere);
         }
         else if(tipo.equals("listaReproduccion"))
         {
         	Funcion.setText(R.string.playlist);
         	Funcion.setText(Funcion.getText()+" : "+consultawhere);
         }
        
        if(!tipo.equals("listaReproduccion"))
        {
	        //según el tipo hacemos una consulta
	        if(tipo.equals("artista"))
	        	consultawhere = MediaStore.Audio.Media.ARTIST+ " = \"" +consultawhere+"\"";
	        
	        else if(tipo.equals("album"))
	        	consultawhere = MediaStore.Audio.Media.ALBUM+ "  = \"" +consultawhere+"\"";
	         
	        else if(tipo.equals("anno"))
	         consultawhere = MediaStore.Audio.Media.YEAR + "=" +Integer.parseInt(consultawhere);
	       
	        //obtenemos la lista de las actividades anteriores
	        listaR=(ListaDeReproduccionActual)getIntent().getExtras().get("lista");
	        	
        }
        else //tenemos una lista de reproducci�n
        {
        	
        	if(consultawhere.equals("Todas Las pistas"))
        	{
        		consultawhere=MediaStore.Audio.Media.IS_MUSIC + " != 0";
        	}
        	else
        	{
        	 File archivo = null;
             FileReader fr = null;
             BufferedReader br = null;

             try {
                // Apertura del fichero y creacion de BufferedReader para poder
                archivo = new File ("/sdcard/ComMusicA/PlayList/"+consultawhere);
                fr = new FileReader (archivo);
                br = new BufferedReader(fr);

                // Lectura del fichero
                String cadenaLista;
                int i=0;
                while((cadenaLista=br.readLine())!=null)
                {
                	//abrimos el fichero con la lista de reproducci�n que hemos obtenido y creamos la consulta where
                	if(i==0)
        				consultawhere=MediaStore.Audio.Media.DATA+ " = \"" +cadenaLista+"\"";
        			else
        				consultawhere+=" OR "+MediaStore.Audio.Media.DATA+ " = \"" +cadenaLista+"\"";
                	i++;
                }
             }
             catch(Exception e){
                e.printStackTrace();
             }finally{
                // En el finally cerramos el fichero, para asegurarnos
                // que se cierra tanto si todo va bien como si salta 
                // una excepcion.
                try{                    
                   if( null != fr ){   
                      fr.close();     
                   }                  
                }catch (Exception e2){ 
                   e2.printStackTrace();
                }
             }
        	}
        }
        	
        //creamos el cursor con lo que hemos obtenido previamente    
        cursor = this.managedQuery(
	        	MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	        	proyection,
	        	consultawhere,
	        	null,
	        	ordenadoPor);
       
	    cursor.moveToFirst(); //ponemos el cursor al principio
	    items=new String[cursor.getCount()][]; //creamos una matriz para guardar los elementos del cursor
        for(int i=0;i<cursor.getCount();i++)
        	items[i]=new String[5];
       	
        //pasamos los elementos del cursor a la matriz de elementos
        for(int i=0;i<cursor.getCount();i++)
        {
        	items[i][0]=cursor.getString(2);
        	items[i][1]=cursor.getString(3);
        	items[i][2]=cursor.getString(1);
        	items[i][3]=cursor.getString(4);
        	cursor.moveToNext();
        }
        //inicializamos un diálogo de alertas que saldrán al mantener pulsado un elemento
        builder=new  AlertDialog.Builder(this);
        actividad=this;
        setListAdapter(new IconicAdapter(this));
        
    }

		
	

	/**
	 * Al hacer click en un elemento de la lista se activará este método.
	 * Su funci�n es a�adir a la lista un elemento vaciandola previamente puesot que si seleccionamos uno sería
	 * para escucharlo s�lo
	 */
    public void onListItemClick(ListView parent, View v, int position, long id) {
    	listaR.vaciarLista();
    	listaR.anadirAListaActual(items[position][2]);
    	//finalizamos la actividad con el resultado 3
    	this.setResult(3);
    	finish();
	          
	  }
   
    	
        
    /**
     * @class IconicAdapter
     * Esta clase se encarga de mostrar la lista de reproducci�n con sus elementos a partir del la matriz de items que previamente
     * inicializamos a partir del cursor. Es una clase que está definida dentro de la clase ReproductorMusica porque necesita de muchos
     * parámetros de esta primera. Además hace uso de elementos gráficos con lo que no puede definirse en otro sitio
     */
	 
	  class IconicAdapter extends ArrayAdapter<Object> {

	        Activity context;//cremaos una nueva actividad contexto

	        /**
	         * constructor de la clase Iconic Adapter
	         * @param context actividad anterior que se cre� en la clase
	         */
	        IconicAdapter(Activity context) {
	        	  //obtenemos de la clase superior la actividad, el layout y el vector de items 
	              super(context, R.layout.fila, items);
	              //inicalizamos la actividad de la clase con el obtenido de la anterior
	              this.context=context;
	              ListView lv = getListView();
	              //inicalizamos un long click item para mostrar un cuadro de diálogo con opciones para las pistas
	  	          lv.setOnItemLongClickListener(new OnItemLongClickListener(){
	  	          
	  	          /**
	  	           * Método que nos permite obtener un resultado en el caso de hacer un click largo en un elemento de la lista
	  	           * En este caso crea un menu de diálogo para las opciones de cada pista
	  	           */
	  	          public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int row, long arg3) {
	  	          // ponemos el título y las opciones para el menú de diálogo
	  	          builder.setTitle(R.string.opcionpista);
	  	          builder.setItems(new String[]{getString(R.string.detalles),getString(R.string.reproducir),getString(R.string.anadirop)}, new DialogInterface.OnClickListener() {
	  	          //creamos el listener para el menú
	  	          public void onClick(DialogInterface dialog, int pos) {
	  	             switch(pos)
	  	             {
	  	             	
	  	             	
	  	             	case 0:
	  	             		Intent intent = new Intent(); //creamos un nuevo intent
	  	      	  		    intent.setClass(ActividadListado.this, ActividadDetallesPista.class); // asociamos el cambio de actividad
	  	      	  		    intent.putExtra("nombrePista", items[row][2]); //a�adimos un elemento serializable con es modoRepro
	  	      	  		    startActivity(intent);
	  	             		break;
	  	             	case 1:
	  	             		if(listaR!=null)
	  	             			listaR.vaciarLista();
	  	             		listaR.anadirAListaActual(items[row][2]);
	  	             		actividad.setResult(3);
	  	             		actividad.finish();
	  	             		break;
	  	             	case 2:
	  	             		listaR.anadirAListaActual(items[row][2]);
	  	             		break;
	  	             }
	  	          }
	  	          }
	  	          );
	  	          AlertDialog alert = builder.create();
	  	          alert.show();
	  	          return true; //si hay éxito en la operaci�n de crear el menú con las opciones mandamos un true
	  	          }
	  	          }
	  	          );

	        }

	        /**
	         * método encargado de rellenar las filas con la informaci�n de las canciones
	         * @param position posiciíon a rellenar
	         * @param convertView vista de la fila
	         * @param parent grupo de vista al que pertenece
	         * @return vista modificada de convertView
	         */
	        public View getView(int position, View convertView, ViewGroup parent) {         
	            View row = convertView; //vista de la fila
	            ViewWrapper wrapper = null; //clase envoltorio de las vistas
	            
	            // si no se existe se crea
	            if (row == null) 
	            {
	                row = View.inflate(this.getContext(), R.layout.fila2, null);
	                wrapper = new ViewWrapper(row);
	                row.setTag(wrapper);
	            } else {
	            	//si existe se le asigna la etiqueta de esa columna wrapper
	               wrapper = (ViewWrapper) row.getTag();
	            }
	            //ponemos en cada fila los elementos que hemos introducido en la matriz de elementos
	            wrapper.getLabel().setText(items[position][0]);
	            wrapper.getLabel2().setText(items[position][1]);
	           
	            if (items[position]!= null && items[position][0].length() > 3) {
	            	//ponemos el icono
	                wrapper.getIcon().setImageResource(R.drawable.iconomusica);
	            }
	            return (row); //devolvemos la pista
	        }
	        /**
			 * @class  ViewWrapper : clase encargada de manejar las etiquetas de cada columna, encargándose de la parte  de actualizaci�n de datos en la pantalla de la aplicaci�n android
			 */
	        public class ViewWrapper {
	            View base;
	            TextView label = null;
	            TextView label2 =null;
	            TextView label3 =null;
	            ImageView icon = null;
	            
	            ViewWrapper(View base) {
	                this.base = base;
	            }

	            TextView getLabel() {
	                if ( label == null) {
	                    label = (TextView) base.findViewById(R.id.nombreCancion);
	                }
	                return (label);
	            }
	            TextView getLabel2() {
	                if ( label2 == null) {
	                    label2 = (TextView) base.findViewById(R.id.nombreArtista);
	                }
	                return (label2);
	            }
	            TextView getLabel3() {
	                if ( label3 == null) {
	                    label3 = (TextView) base.findViewById(R.id.nombreData);
	                }
	                return (label3);
	            }
	            ImageView getIcon() {
	                if (icon == null) {
	                    icon = (ImageView) base.findViewById(R.id.iconomusica);
	                }
	                return (icon);
	            }
	        }
	         

	  }
	  
    /**
     * Método que se activará cuando pulsemos un bot�n de nuestra aplicaci�n
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return no devuelve nada
     * @exception excepciones No lanza ninguna
     */
	public void onClick(View v) {
		
	}


	/**
     * Método que se activará cuando pulsemos durante al menos 2 segundos un bot�n de nuestra aplicaci�n
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return boolean true si no ha ocurrido ningún problema o false si ha ocurrido algún problema en el método
     * @exception excepciones No lanza ninguna
     */
	public boolean onLongClick(View v) {
		return true;
	}
    
}
