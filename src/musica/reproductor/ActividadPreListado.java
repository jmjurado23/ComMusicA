package musica.reproductor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
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


/**
 * Listado.java
 * Clase encargada de gestionar la actividad de mostrar un listado con todos los artistas, álbunes , a�os o listas
 * que hemos seleccionado en la actividad de bibliotecaL
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadPreListado extends ListActivity implements OnClickListener, OnLongClickListener {
	private String[] proyection={
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.YEAR,
			MediaStore.Audio.Media.DATA
			
			};
	private Cursor cursor=null;
	private TextView Funcion; 
	String[] itemsSinRep=null;
	
	//seleccionador del cursor
    String sel = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    String ordenadoPor=new String();
    
    private String[] items; 
    private String tipo;
    private AlertDialog.Builder  builder ;
    private ListaDeReproduccionActual listaR;
    
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
    	//cargamos el layout en la vista
        setContentView(R.layout.listado);
       
        //inicializamos el t�tulo de la actividad
        Funcion = (TextView) findViewById(R.id.funcion);
        //vemos el tipo de listado que tenemos que hacer
        tipo =getIntent().getExtras().getString("id");
        
        //formateamos el t�tulo de la actividad
        
        if(tipo.equals("artista"))
        	Funcion.setText(R.string.artista);
         else if(tipo.equals("album"))
        	Funcion.setText(R.string.album);
         else if(tipo.equals("anno"))
        	Funcion.setText(R.string.anno);
         else if(tipo.equals("listaReproduccion"))
        	Funcion.setText(R.string.playlist);
        
        
        if(!tipo.equals("listaReproduccion"))//si no es una lista de reproducci�n de las que tenemos en el teléfono hacemos uan consulta a la BBDD.
        {
        	
        	//según el tipo de selecci�n en bibliotecaL creamos un elemento para las búsquedas en la base de datos de android
        	if(tipo.equals("artista"))
        		ordenadoPor= MediaStore.Audio.Media.ARTIST ;
        	else if(tipo.equals("album"))
        		ordenadoPor= MediaStore.Audio.Media.ALBUM ;
        	else if(tipo.equals("anno"))
        		ordenadoPor= MediaStore.Audio.Media.YEAR;
        
	        //mostrmos un mensaje con lo seleccionado
	       /* Toast.makeText(
	                getBaseContext(),
	                tipo,
	                Toast.LENGTH_SHORT).show();*/
	        
	        //creamos el cursor para las pistas de la base de datos
	        cursor = this.managedQuery(
		        	MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
		        	proyection,
		        	sel,
		        	null,
		        	ordenadoPor);
	        
	        cursor.moveToFirst(); //ponemos el cursor al principio
		        
	        
	        /*existe un problema con las consultas de este tipo (managedQuery) y esque no nos permite hacer un distinc por lo que nos devuleve los resultados repetidos
	        además de otros resultados no válidos. Es por eso por lo que este proceso es más engorroso y lento pero es necesario para no tener dulicados en los resultado
	        .Se pasarán ahora los resultados según el tipo a un nuevo vector de elementos, esta vez eliminando repeticiones*/
	        int i=0;
	        items=new String[cursor.getCount()];
	        String aux = "";
	    	while(!cursor.isLast())
	        {
	        	  if(tipo.equals("artista"))
	        		 aux=cursor.getString(0);
	              else if(tipo.equals("album"))
	            	  aux=cursor.getString(1);
	              else if(tipo.equals("anno"))
	            	  aux=cursor.getString(2);
	              else if(tipo.equals("listaReproduccion"))
	            	  aux=cursor.getString(0);
	        	//quitamos las réplias que nos ha devuelto la consulta
	        	if(i!=0)
	        	{	//si los elementos son distintos los a�adimos a items
	        		if(items[i-1]!=null && !items[i-1].equals(aux))  
	        		{
	        			items[i]=aux;
	        			i++;
	        		}
	        			
	        	}
	        	else if(aux!=null)
	        	{
	        		items[i]=aux;
	        		i++;
	        	}
	        	//movemos el cursor al principio
	        	cursor.moveToNext();
	        	
	        	
	        }
	    	
	    	// esto nos sirve para hacer una lista de elementos sin repetir, que será la que mostraremos.
	    	itemsSinRep= new String[i];
	    	for(int j=0;j<i;j++)
	    		itemsSinRep[j]=items[j];
	    	
        }
        else //queremos ver las listas de reproducci�n que hay en el dispositivo
        {
        	File dir = new File("/sdcard/ComMusicA/PlayList/");
        	String[] ficheros = dir.list();
        	itemsSinRep= new String[(ficheros.length+1)];
        	if(itemsSinRep.length!=1)
        	{
        		for(int i=0;i<ficheros.length;i++)
        			itemsSinRep[i+1]=ficheros[i];
        	}
        	itemsSinRep[0]="Todas Las pistas";
        }
    	//inicializamos la clase para la lista
        this.setListAdapter(new IconicAdapter(this));
        //lanzador de diálogos
        builder=new  AlertDialog.Builder(this);
    }
    
    /**
     * Método que se activará cuando pulsemos durante al menos 2 segundos un bot�n de nuestra aplicaci�n
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return boolean true si no ha ocurrido ningún problema o false si ha ocurrido algún problema en el método
     * @exception excepciones No lanza ninguna
     */
    public boolean onLongClick(View v)
    {
     return false;
    }


    /**
     * Método que se activará cuando pulsemos un bot�n de nuestra aplicaci�n
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return no devuelve nada
     * @exception excepciones No lanza ninguna
     */
	public void onClick(View v) 
	{
	}
	
	/**
     * Método que se llama al hacer clic en un elemento de la lista de reproducci�n
     * @param parent vista de la actividad padre
     * @param v vista de la actividad
     * @param position posici�n que ocupa el elemento seleccionado
     * @param id identificador del elemento seleccionado
     * @return no devuelve nada
     * @exception excepciones No lanza ninguna
     */
    public void onListItemClick(ListView parent, View v, int position, long id) {
	    	//aqu� es donde tiene que sacar el nombre de la base de datos
	        //creamos una nueva actividad con los datos obtenidos y la mandamos a la actividad Listado
    		Intent intent = new  Intent(ActividadPreListado.this, ActividadListado.class );
    		intent.putExtra("tipo", tipo);
    		intent.putExtra("lista", (ListaDeReproduccionActual)getIntent().getExtras().get("lista"));
    		intent.putExtra("id", itemsSinRep[position]);
    		startActivityForResult(intent, 3);
    		
	       
	  }
    
    /**
     * Método para borrar una lista de reproducci�n de nuestra SD
     * @param elem elemnto a eliminar de la SD
     * @return boolean con el resultado de la operaci�n. True si ha habido éxito o false si no lo ha habido
     */
    private void borrarLista(String elem)
    {
    	File file = new File("/sdcard/ComMusicA/PlayList/"+elem);
    	boolean eliminado = file.delete();
   		if(eliminado)
        	{
        	    Toast.makeText( getBaseContext(), R.string.eliminadocorrecto,Toast.LENGTH_SHORT).show();
        	    this.setResult(999);
           		this.finish();
        	    
        	}
        	else
        	{
        		Toast.makeText( getBaseContext(), R.string.eliminadiincorrecto,Toast.LENGTH_SHORT).show();
        	}
   		
    	
    }
    
    
    /**
     * Método encargado de manejar los cuadros de mandar a reproducir los elementos la lista de elementos
     * @param reproducir
     * @param pos
     * @param elem
     */
    private void anadirAListaDeReproduccion(boolean reproducir, int pos, String elem) {
    	Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            listaR = (ListaDeReproduccionActual) bundle.getSerializable("lista");
        }
    		String consultawhere = "";
    		//según el tipo hacemos una consulta
            if(tipo.equals("artista"))
            	consultawhere = MediaStore.Audio.Media.ARTIST+ " = \"" +elem+"\"";
            
            else if(tipo.equals("album"))
            	consultawhere = MediaStore.Audio.Media.ALBUM+ "  = \"" +elem+"\"";
             
            else if(tipo.equals("ano"))
             consultawhere = MediaStore.Audio.Media.YEAR + "=" +Integer.parseInt(elem);
           
            else if(tipo.equals("listaReproduccion"))
            {
            	File archivo = null;
                FileReader fr = null;
                BufferedReader br = null;

                try {
                   // Apertura del fichero y creacion de BufferedReader para poder
                   archivo = new File ("/sdcard/ComMusicA/PlayList/"+elem);
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
                   	System.out.println("entro");
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
            
            //creamos el cursor con lo que hemos obtenido previamente    
            cursor = this.managedQuery(
    	        	MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    	        	proyection,
    	        	consultawhere,
    	        	null,
    	        	null);
           
    	    cursor.moveToFirst(); //ponemos el cursor al principio
    	    
    	    String[] items2 = new String[cursor.getCount()]; //creamos una matriz para guardar los elementos del cursor
        
           	if(reproducir)
           	{
           		if(listaR!=null)
           			listaR.vaciarLista();
           	}
           	if(tipo.equals("listaReproduccion")) //si tenemos una lista de reproducci�n no miramos réplicas de nombres
           	{
           		listaR.setNombre(elem);
           		for(int i=0;i<cursor.getCount();i++)
           		{
           			items2[i]=cursor.getString(3);
           			listaR.anadirAListaActual(items2[i]);
           			cursor.moveToNext();
           		}
           		
           	}
           	else
           	{	
           		//pasamos los elementos del cursor a la matriz de elementos
           		for(int i=0;i<cursor.getCount();i++)
           		{
           			items2[i]=cursor.getString(3);
           			listaR.anadirAListaActual(items2[i]);
           			cursor.moveToNext();
           		}
           	}
            //mostramos texto de contenido a�adido
    	    Toast.makeText(
                    getBaseContext(),
                    "Contenido añadido",
                    Toast.LENGTH_SHORT).show();
    	if(reproducir)
    	{
    		
    		this.setResult(2);
       		this.finish();
		}
	}
    
    
    /** Este Método es el encargado de recoger todos los resultados producidos por las actividades
    * @param requestCode es el número de la actividad que ha acabado
    * @param resultCode es el número que determina e resultado de dicha operaci�n
    * @param data si la actividad ha devuelto un dato, lo inserta en data
    * @return no devuelve nada
    */
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
    	
    	if(requestCode==3)
           if (resultCode == 3) {
        	   this.setResult(2);
          	 finish();
            }
           
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
	              super(context, R.layout.fila2, itemsSinRep);
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
	  	        	  if(!tipo.equals("listaReproduccion"))
	  	        	  {
			  	          // ponemos el t�tulo y las opciones para el menú de diálogo
			  	          builder.setTitle(R.string.opcionpista);
			  	          builder.setItems(new String[]{getString(R.string.reproducir),getString(R.string.anadirop)}, new DialogInterface.OnClickListener() {
			  	          //creamos el listener para el menú
			  	          public void onClick(DialogInterface dialog, int pos) {
			  	             switch(pos)
			  	             {
			  	             	case 0:
			  	             		anadirAListaDeReproduccion(true,pos,itemsSinRep[row]);
			  	             		
			  	             		break;
			  	             	case 1:
			  	             		anadirAListaDeReproduccion(false,pos,itemsSinRep[row]);
			  	             		break;
			  	             }
			  	          }
			  	          }
			  	          );
			  	          AlertDialog alert = builder.create();
			  	          alert.show();
			  	          return true; //si hay éxito en la operaci�n de crear el menú con las opciones mandamos un true
	  	          }
	  	        	  else
	  	        	  {
	  	        		// ponemos el t�tulo y las opciones para el menú de diálogo
			  	          builder.setTitle(R.string.opcionpista);
			  	          builder.setItems(new String[]{getString(R.string.reproducir),getString(R.string.anadirop),getString(R.string.borrarlista)}, new DialogInterface.OnClickListener() {
			  	          //creamos el listener para el menú
			  	          public void onClick(DialogInterface dialog, int pos) {
			  	             switch(pos)
			  	             {
			  	             	case 0:
			  	             		anadirAListaDeReproduccion(true,pos,itemsSinRep[row]);
			  	             		
			  	             		break;
			  	             	case 1:
			  	             		anadirAListaDeReproduccion(false,pos,itemsSinRep[row]);
			  	             		break;
			  	             	case 2:
			  	             		borrarLista(itemsSinRep[row]);
			  	             		
			  	             }
			  	          }
			  	          }
			  	          );
			  	          AlertDialog alert = builder.create();
			  	          alert.show();
			  	          return true; 
	  	        	  }
	  	          }
	  	         }
	  	       );


	        }
	        
	        
	        /**
	         * método encargado de rellenar las filas con la informaci�n de las canciones
	         * @param position posici�on a rellenar
	         * @param convertView vista de la fila
	         * @param parent grupo de vista al que pertenece
	         * @return vista modificada de convertView
	         */
	        public View getView(int position, View convertView, ViewGroup parent) {         
	            View row = convertView;
	            ViewWrapper wrapper = null;
	            
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
	            wrapper.getLabel().setText(itemsSinRep[position]);
	            
	          //ponemos el icono correspondiente a cada tipo, es decir, artista, lista de reproducci�n, album o a�o
	            if (itemsSinRep[position]!= null && itemsSinRep[position].length() > 1) {
	            	if(tipo.equals("artista"))
	            		wrapper.getIcon().setImageResource(R.drawable.artistap);
	                else if(tipo.equals("album"))
	                	wrapper.getIcon().setImageResource(R.drawable.albump);
	                else if(tipo.equals("anno"))
	                	wrapper.getIcon().setImageResource(R.drawable.annop);
	                else if(tipo.equals("listaReproduccion"))
	                	wrapper.getIcon().setImageResource(R.drawable.listap);
	            	
	            }
	            return (row); //devolvemso la vista de la fila
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
	                    label = (TextView) base.findViewById(R.id.nombreArtista);
	                }
	                return (label);
	            }
	            ImageView getIcon() {
	                if (icon == null) {
	                	icon = (ImageView) base.findViewById(R.id.iconomusica);
	                	
	                }
	                return (icon);
	            }
	        }
	  } 
}
