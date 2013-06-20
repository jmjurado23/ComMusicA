package musica.reproductor;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.MediaStore;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * ReproductorMusica.java: ReproductorMusica.java
 * Clase principal de la aplicaci�n ComMusica. Encargada de la reproducci�n de la música
 * y de gestionar todas las dem�s clases.
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadReproductorMusica extends ListActivity implements OnClickListener, Runnable, OnLongClickListener,SeekBar.OnSeekBarChangeListener {

    private TextView estado;
    private TextView duracion;
    private TextView sonandom;
    private TextView sonandoa;
    private TextView textoSonando;
    private TextView textoArtista;
    private SeekBar progreso;
    private ImageButton detener;
    private ImageButton reproducir;
    private ImageButton avance;
    private ImageButton retroceso;
    private ImageButton modo;
    private ImageButton r;    
    private ImageButton estrella;
    private Formato formato=new Formato();
    private Reproduciendo reproduciendoAhora;
    private ListaDeReproduccionActual lista;
    private ModoReproduccion modoRepro;
    private Drawable bg ;
    private Estado estadoR;
    private AlertDialog.Builder  builder ;
    private String sonando;
    /*Clase que manejar� el reproductor de música*/
    private MediaPlayer mp;
    private String posicionReloj;
    private String sel = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    private String[][] items; 
    Cursor cursor;
    private Cursor cursorLista=null;
    public static BaseDatos BBDD; 
    private ManejaBBDD mBaseDatos;
    private Thread hiloActualizar;
    private boolean reproduciendo;
    private boolean procesando;
    private boolean iniciado;
    private boolean pausado;
    private File archivoLista = null;
    private File archivoPreferencias = null;
    private FileReader fr = null;
    private BufferedReader br = null;
    private FileWriter fw =null;
    private BufferedWriter bw=null;
    private ProgressDialog pd = null;
    private ProgressDialog pd2 = null;
    private Preferencias preferencias;
    private NotificationManager notificacionManager;
    private Notification notificacion;
    private CharSequence tituloNotificacion;
    private boolean estadoRecomendando;
    private PendingIntent intentNotificacion;
    private LinearLayout fondoLetra; 
    private String PATH_DIRLISTA = "/sdcard/ComMusicA"+ "/" + "reproduciendo.txt";
    private String PATH_DIRPREFERENCIAS = "/sdcard/ComMusicA"+ "/" + "preferencias.txt";
    private String PATH_DIRBBDD ="/sdcard/ComMusicA/miBBDD.bd";
    private String PATH_USOPISTAS ="/sdcard/ComMusicA"+ "/" + "frec_uso.txt";
    private parametrosEncuesta encuesta;
    private String cancionEncuestada="";
    private String cancionSonando="";
    private boolean moviendo;
    
    private String[] proyection={
    		MediaStore.Audio.Media._ID,
    		MediaStore.Audio.Media.DATA,
    		MediaStore.Audio.Media.TITLE,
    		MediaStore.Audio.Media.ARTIST,
    		MediaStore.Audio.Media.DURATION
    };
    
    //inicializamos un handler para usar con el hilo. Nos servir� para actualizar el reloj
    final Handler mHandler = new Handler();
    

    //inicializaci�n de los runnables que ser�n llamados mediante los handlers
    
    final Runnable mActualizarReloj = new Runnable() {
        public void run() {
            duracion.setText(posicionReloj); //como desde un hilo no se puede actualizar la pantalla se hace desde un runnable
        }
    };
    final Runnable mActualizaPista = new Runnable(){
        public void run() {
        	if(mBaseDatos!=null)
        	{
        		mBaseDatos.aumentarParametrosEntidad(cursor.getString(1),estadoR.getAnimo(),estadoR.getHora(),estadoR.getDia());
        		mBaseDatos.recuperaTiemposSinReproducir(Integer.parseInt(cursor.getString(0)),preferencias.getTiempoSinREproducir() );
        	}
        	 
        }
    };
    
    
    final Runnable mCompruebaFinPista = new Runnable() {
        public void run() {
        		if(!procesando) //si la variable sem�foro procesando no est� activa
        		{
	        		procesando=true; //la activamos
		        	mp.reset(); //reiniciamos la clase multimedia
		        	if(cursor!=null) //si el cursor ya ha sido inicializado
		        	{
		        		
			        	if(modoRepro.intestado()==0 || modoRepro.intestado()==2) //en caso de tener una reproducci�n normal o repetir todo
			        	{
			        
			        		if(cursor.moveToNext() && !cursor.isAfterLast() && !cursor.isBeforeFirst() ) //no última pista
			        			areproducir(cursor,true);
			        		else  //última pista
			        		{
			        			cursor.moveToFirst(); //movemos al principio
			        			
			        			if(modoRepro.intestado()==2) //repetir todas
			        				areproducir(cursor,true);//reproducimos
			        			else //normal
			        			{
			        				reproduciendo=false;
			        				areproducir(cursor,false); //no reproducimos
			        			}
			        		}
			        	}
			        	else if(modoRepro.intestado()==1) //repetir una
			        		areproducir(cursor,true);
			        	else if(modoRepro.intestado()==3 || modoRepro.intestado()==4) //aleatorio
			        	{
			        		cursor.moveToPosition((int)(Math.random()*100)%cursor.getCount());
			        		areproducir(cursor,true);
			        	}
		        	}		
		       }
        	procesando=false; //quitamos el bloqueo
        	
        }
    };
    
    final Runnable mCompruebaBoton = new Runnable(){
    	public void run(){
    		if(!reproduciendo)
    			reproducir.setBackgroundDrawable(getResources().getDrawable(R.drawable.botonplay));
    		else
    			if(mp!= null && !mp.isPlaying())
    				reproducir.setBackgroundDrawable(getResources().getDrawable(R.drawable.botonplay));
    			else if(mp != null && mp.isPlaying()) 
    				reproducir.setBackgroundDrawable(getResources().getDrawable(R.drawable.botonpause));
    	}
    };
    /**
    * M�todo encargado de rellenar la lista de las canciones disponibles a partir de un cursor
    * @param cursorLista cursor con los datos a pasar a los vectores de la lista de elementos
    * @return No devuelve ningun valor
    * @exception excepciones No lanza ninguna
    */

     public void rellenaLista(Cursor cursorLista)
     {
    	 //inicializacion de la matriz de elementos
    	 items=new String[cursorLista.getCount()][];
         for(int i=0;i<cursorLista.getCount();i++)
         	items[i]=new String[4];
         //recorremos el cursor y copiamos la informaci�n en los elementos de la matriz
         for(int i=0;i<cursorLista.getCount();i++)
         {
         	items[i][0]=cursorLista.getString(2);
         	items[i][1]=cursorLista.getString(3);
         	items[i][2]=cursorLista.getString(1);
         	items[i][3]=cursorLista.getString(4);
         	//System.out.println("id:" +cursorLista.getString(0));
         	cursorLista.moveToNext();
         }
         
     }
    
     /**
      * Sobrecarga del m�todo onResume, que es invocado cuando volvemos de un estado de Pausa de la actividad.
      */
     public void onResume()
     {
    	 super.onResume();//llamamos al m�todo original para que haga sus funciones
    	 //si estamos reproduciendo una lista, cargamos de nuevo la reproducci�n.
    	 if(lista!=null)
		 {
			 actualizaReproduccion(1); 
		 }
     }
     /**
      * M�todo encargado de crear todos los directoriso necesarios para la aplicaci�n  si no est�n creados
     * @throws IOException 
     * @throws NumberFormatException 
      * @throws FileNotFoundException 
      * 
      */
     private void preparaContenido() throws NumberFormatException, IOException 
     {
    	 int i=0; 

		//inicializamos las preferencias
	    preferencias= new Preferencias(PATH_DIRPREFERENCIAS,PATH_DIRLISTA,PATH_DIRBBDD,PATH_USOPISTAS);
	    
        //creamos si no est� creada el fichero de configuraci�n
        archivoLista = new File(PATH_DIRLISTA);
	    
    	//creamos una carpeta con el nombre de la aplicaci�n si no existe
 		//File f = new File("/sdcard/" + "\\" + "ComMusicA/");
        File f = new File("sdcard/ComMusicA/");
 		
 	        if(!f.exists())
 	        {
 	            f.mkdir();
 	        }
 	      // Toast.makeText(ActividadReproductorMusica.this,Boolean.toString(f.exists()),10).show();
         //creamos si no est� creada la carpeta para las listas de reproduci�n
         File f1 = new File("sdcard/ComMusicA/PlayList/");
         if(!f1.exists())
         {
             f1.mkdir();
         }
         
         
       
       
         //inicializacion del objeto lista que se usara para enviar las pistas entre actividades
         lista=new ListaDeReproduccionActual();
         
			try {
				fr = new FileReader (archivoLista);
			
			br = new BufferedReader(fr); 
			String linea="";
			try {
				while((linea=br.readLine())!=null)
				{
					if(linea!=null)
						lista.anadirAListaActual(linea);
				    i++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//creamos si no est� creada el fichero de cpreferencias
	         archivoPreferencias = new File(PATH_DIRPREFERENCIAS);
	     
				fr = new FileReader (archivoPreferencias);
				br = new BufferedReader(fr); 
				String linea1="";
				String linea2="";
				String linea3="";
				if((linea1=br.readLine())!=null && (linea2=br.readLine())!=null && (linea3=br.readLine())!=null) //si podemos leer el fichero, cargamos lo que hay en el fichero
				{
					preferencias.setMaxPistas(Integer.parseInt(linea1)); //cargamos el primer valor en el par�metro
					preferencias.setTiempoSinReproducir(Integer.parseInt(linea2)); //cargamos el segundo valor en el segundo par�metro
					preferencias.setURLweb(linea3);
				}
				System.out.println(preferencias.getMaxPistas());
				System.out.println(preferencias.getTiempoSinREproducir());
				System.out.println(preferencias.getURLweb());
				br.close();
				fr.close();
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
         
        
         
        if(i==0) //cargamos todo lo que tenemos en nuestro reproductor
         {
        	 
        	 Toast.makeText(ActividadReproductorMusica.this,R.string.nopista,10).show();
        	//meter despu�s dentor del else  
             //inicializacion del cursor a partir de la Base de datos del dispositivo
           	 cursorLista = this.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proyection, sel,null,null);
             cursorLista.moveToFirst();
             
             //prepara la lista de reproducci�n que se mostrar�
             rellenaLista(cursorLista);  
             rellenaCursor(null); //rellena el cursor con toda la lista
         }
        else 
        	rellenaCursor(lista.getLista()); //rellenamos el cursor de reproducci�n
		  
		  //prepara la lista de reproducci�n que se mostrar�
	      rellenaLista(cursor);
	               
     }
     /**
      * M�todo que es llamado al iniciar una actividad Android. Encargado de inicializar algunos elementos
      * y de llamar a los Listeners.
      * @param savedInstanceState es un par�metro con el estado guardado de una actividad anterior
      * @return No devuelve ningun valor
      * @exception excepciones No lanza ninguna
      */
    public void onCreate(Bundle savedInstanceState) {
    	//guardado del estado al iniciar esta actividad
    	super.onCreate(savedInstanceState);
    	//obtenemos el layout de la actividad
        setContentView(R.layout.main);
      
        
        //cramos todos los directorios y los contenidos necesarios
        try {
			preparaContenido();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        //definimos esta variable para que s�lo accedamos una vez a la preparaci�n del programa. Esta s�lo se efectuar� la primera vez que activemos la recomendaci�n
        iniciado=false;
        
        //inicializamos los modos de reproduccion
        modoRepro=new ModoReproduccion();
        //inicializamos el estado del sistema de recomendacion
        estadoR= new Estado();
        
        encuesta=new parametrosEncuesta();
        //inicialización de la lista de elementos
        setListAdapter(new IconicAdapter(this));
      
        
        //preparamos las notificaciones
        notificacionManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificacion= new Notification(R.drawable.notificacion,getString(R.string.notiso), System.currentTimeMillis());
        tituloNotificacion = getString(R.string.notico);
	    intentNotificacion = PendingIntent.getActivity(this, 0, this.getIntent(), 0);
		notificacion.defaults |= Notification.FLAG_AUTO_CANCEL;
		notificacion.flags |= Notification.FLAG_ONGOING_EVENT;
		
        //creamos el progressDialog
		pd= new ProgressDialog(this);
		pd.setTitle(R.string.espere);
     	pd.setMessage(""+getString(R.string.crearbbdd));
     
     
     	pd2= new ProgressDialog(this);
     	pd2.setTitle(R.string.espere);
     	pd2.setMessage(""+getString(R.string.guardando));
     
        moviendo=false;
        estadoRecomendando=false;
        sonando=""; 
        builder=new  AlertDialog.Builder(this);
        reproduciendo=false;

      	 //creamos la BBDD de ComMusica
      	 BBDD = new BaseDatos(PATH_DIRBBDD);
        
        // Obtenermos una referencia a todos los elementos generados en main.xml
        duracion = (TextView) findViewById(R.id.duracion);
        estado   = (TextView) findViewById(R.id.estado);
        sonandom = (TextView) findViewById(R.id.sonandom);
        sonandoa = (TextView) findViewById(R.id.sonandoa);
        progreso = (SeekBar) findViewById(R.id.progreso);
        textoSonando= (TextView) findViewById(R.id.textosonando);
        textoArtista= (TextView) findViewById(R.id.textoartista);
        fondoLetra = (LinearLayout) findViewById(R.id.fondoletrasrepro);
        estrella=(ImageButton) findViewById(R.id.estrella);
        reproducir = (ImageButton) findViewById(R.id.reproducir);
        detener = (ImageButton) findViewById(R.id.detener);
        avance = (ImageButton)findViewById(R.id.avance);
        retroceso = (ImageButton)findViewById(R.id.retroceso);
        modo = (ImageButton) findViewById(R.id.modo);
        r = (ImageButton) findViewById(R.id.r);
        progreso.setOnSeekBarChangeListener(this); 
        r.setVisibility(View.INVISIBLE);
        
        
        // Establecemos que la clase actual sea la que maneje los clics sobre estos botones
        reproducir.setOnClickListener(this);
        detener.setOnClickListener(this);
        avance.setOnClickListener(this);
        retroceso.setOnClickListener(this);
        modo.setOnClickListener(this);
        estrella.setOnClickListener(this);
        fondoLetra.setOnClickListener(this);
        r.setOnClickListener(this);
        
        reproducir.setOnLongClickListener(this);
        detener.setOnLongClickListener(this);
        avance.setOnLongClickListener(this);
        retroceso.setOnLongClickListener(this);
        estrella.setOnLongClickListener(this);
        fondoLetra.setOnLongClickListener(this);
        r.setOnLongClickListener(this);
        
		
		
		
		
        
    }

    /**
     * M�todo que reproduce el primer elemento del cursor que se le pase
     * @param c cursor a partir del cual se reproducir�
     * @param reproducir si true, la pista indicada en el cursor pasar� a sonar si false, no lo har� pero actualizar� los datos
     * @return No devuelve ningun valor
     * @exception excepciones No lanza ninguna
     */
    private void areproducir(Cursor c, boolean reproducir)
    {   
    	try {
    		
    		 //MediaPlayer se encarga de la reproduccion de contenido multimedia y es aquí donde le introduciomos la direcci�n de la pista de audio
	        mp=MediaPlayer.create(ActividadReproductorMusica.this,Uri.parse(c.getString(1)));
	        sonando=c.getString(1);
	       
            notificacion.setLatestEventInfo(this, tituloNotificacion, c.getString(2) +" - "+ c.getString(3), intentNotificacion);
			notificacionManager.notify(23, notificacion);
	        
	         // Iniciamos la reproducci�n
	        if(reproducir)
	        {
	        	mp.start();
	        	reproduciendo=true;
	        }
	        else
	        {
	        	mp.stop();
	        	reproduciendo=false;
	        	estado.setText(R.string.detenido);
	        }
	         cancionSonando = c.getString(1);
	         //Establecemos el TextView a Reproduciendo
	         estado.setText(R.string.reproduciendo);   
	         sonandom.setText(c.getString(2));
	         sonandoa.setText(c.getString(3));
	         //Hacemos visible la barra de progreso  
	         progreso.setVisibility(ProgressBar.VISIBLE);
	         duracion.setText("--:--");
	         //Establecemos su valor actual al principio
	         progreso.setProgress(0);
	         //Establecemos que el valor del final sea el total de milisegundos que dura la canci�n
	         progreso.setMax(mp.getDuration());
	         //iniciamos el proceso de actualizaci�n de los par�metros de la pista
	         if(estadoR.getActivo())
	        	 mHandler.post(mActualizaPista);
	        	
	         // Ejecutamos un hilo que se encargar� de actualizar la barra de progreso cada segundo...
	         if(hiloActualizar==null)
	         {
	        	 hiloActualizar= new Thread(this);
	        	 hiloActualizar.start();
	         }
	         //cambiamos el bot�n de play/pause según el estado de la reproducci�n
	         mHandler.post(mCompruebaBoton);
    	
	         
		} catch (Exception e){
            Log.d("ComMusiCa", "Exception aReproducir: " + e.toString());	
		}
	       
			//actualizamos la vista para se�alar la pista que est� reproduciendo
			  setListAdapter(new IconicAdapter(this));
			 //cerramos la variable sem�foro
	 	     procesando=false;
	 		   
    }
         
    
    
    /**
     * M�todo que se activar� cuando pulsemos durante al menos 2 segundos un bot�n de nuestra aplicaci�n
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return boolean true si no ha ocurrido ningún problema o false si ha ocurrido algún problema en el m�todo
     * @exception excepciones No lanza ninguna
     */
    public boolean onLongClick(View v)
    {
    	
    	//Los que hacen todos los casos es mostrar un mensaje por pantalla con la informaci�n correspondiente al bot�n pulsado
    	
    	if(v.equals(reproducir))
    	{
    		Toast.makeText(ActividadReproductorMusica.this,R.string.repropau,50).show();
    		return true;
    	}
    	
    	if(v.equals(detener))
    	{
    		Toast.makeText(ActividadReproductorMusica.this,R.string.detener,10).show();
    		return true;
    	}
    	
    	if(v.equals(avance))
    	{
    		Toast.makeText(ActividadReproductorMusica.this,R.string.sigcan,10).show();
    		return true;
    	}
    	
    	
    	if(v.equals(retroceso))
    	{
    		Toast.makeText(ActividadReproductorMusica.this,R.string.antcan,10).show();
    		return true;
    	}    
    	
    	if(v.equals(estrella))
    	{
    		Toast.makeText(ActividadReproductorMusica.this,R.string.recoact,10).show();
    		return true;
    	}  
    	
    	if(v.equals(fondoLetra))
    	{
    		if(mp!=null && mp.isPlaying())
    		{
	    		Intent intent = new Intent(); //creamos un nuevo intent
		  		intent.setClass(ActividadReproductorMusica.this, ActividadDetallesPista.class); // asociamos el cambio de actividad
		  		intent.putExtra("nombrePista", cancionSonando); //a�adimos un elemento serializable con es modoRepro
		  		startActivityForResult(intent,31);
		  		return true;
    		}
    	}
    	
    	if(v.equals(r))
    	{
    		Toast.makeText(ActividadReproductorMusica.this,R.string.botonreco,10).show();
    		return true;
    	}
    	
		return false;
    }
    
    /**
     * M�todo definido para realizar listeners de las teclas físicas de nuestro tel�fono
     */
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	if(keyCode == KeyEvent.KEYCODE_BACK) //si la tecla es la de retroceso
    	{
    		cerrarComMusicA();
    		return true;
    		
    	}
    	if(keyCode == KeyEvent.KEYCODE_MENU) //si hemos pulsado la tecla de menu
    	{
    		this.openOptionsMenu();
    		return true;
    	}
    	
    	
    	if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK) //si hemos pulsado la tecla del manos libres (play/pause)
    	{
    		botonPulsado(1);
    	}
    	
    	if(keyCode == KeyEvent.KEYCODE_MEDIA_NEXT) //si hemos pulsado la tecla de siguiente canci�n
    	{
    		botonPulsado(2);
    	}
    	
    	if(keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) //si hemos pulsado la tecla de retroceso
    	{
    		botonPulsado(3);
    	}
    	
    	if(keyCode == KeyEvent.KEYCODE_MEDIA_STOP) //si hemos pulsado la tecla de stop
    	{
    		botonPulsado(4);
    	}
    	//si no es ningún bot�n de la aplicaci�n dejamos decidir al sistema.
    	return super.onKeyDown(keyCode, event);
    	
    	
    }
    
    
    /**
     * M�todo que llama  a todo lo necesario para cuando ComMusicA sea cerrado por el usuario
     */
    private void cerrarComMusicA() {
    	try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(estadoR.getActivo())
			if(mBaseDatos!=null)
			{
        		 
        		 new Thread() 
        		 {
        			 
                     public void run() 
                     {
             
                    	 //actualizamos los par�metros
                    	 if(estadoR.getAnimo().equals("ALEGRE"))
                    		 mBaseDatos.actualizaTiemposSinReproducir(1,estadoR.getHora() , preferencias.getTiempoSinREproducir());
                 		else if(estadoR.getAnimo().equals("ENERGICA"))
                 			mBaseDatos.actualizaTiemposSinReproducir(2,estadoR.getHora() , preferencias.getTiempoSinREproducir());
                 		else if(estadoR.getAnimo().equals("TRANQUILIZANTE"))
                 			mBaseDatos.actualizaTiemposSinReproducir(3,estadoR.getHora() , preferencias.getTiempoSinREproducir());
                 		else if(estadoR.getAnimo().equals("NOSTALGICA"))
                 			mBaseDatos.actualizaTiemposSinReproducir(4,estadoR.getHora() , preferencias.getTiempoSinREproducir());
		                    	 
		                    	
                    	 //salimos sin actualizar la base de datos, esto har� perder los datos de uso de las canciones, por lo que no se le restar�n puntos
                    	 //Así ganaremos velocidad para salir de la aplicaci�n y no tener que esperar
                    	 mBaseDatos.finalizaRecomendacion();
                     }
        		 }.start();
			}
			
		if(mp!=null)
			mp.stop();
		
		if( BBDD.isOpen())
			BBDD.close();
		
		reproduciendo=false;
		notificacionManager.cancel(23);
		
		finish();
		
	}


	/**
     * M�todo que realiza la acci�n al pulsar un bot�n de reproducci�n. 
     * @param boton entero con el bot�n pulsado (1=play, 2=avance, 3=retroceso , 4=detener)
     * @return este m�todo no devuelve nada 
     */
    public void botonPulsado(int boton)
    {
    	switch (boton)
    	{
    	case 1: //play/pause
    		
    		
            
        	if( !reproduciendo)
        	{
        		cursor.moveToFirst();
        		areproducir(cursor,true);
        		mHandler.post(mCompruebaBoton);
        		pausado=false;
        	}
        	else
        		//si no estamos reproduciendo, reproducimos
            	if(mp!= null && !mp.isPlaying())
            	{
            		mp.start();
                	progreso.setVisibility(ProgressBar.KEEP_SCREEN_ON);
                	estado.setText(R.string.reproduciendo);
                	mHandler.post(mCompruebaBoton);
                	procesando=false;
                	pausado=false;
                }
            	else
            		//si estamos reproduciendo, pausamos la reproduci�n
            		if(mp != null && mp.isPlaying()) 
            		{	
            			mp.pause();
            			progreso.setVisibility(ProgressBar.KEEP_SCREEN_ON);
            			estado.setText(R.string.pause);  
            			mHandler.post(mCompruebaBoton);
            			procesando=true;
            			pausado=true;
            		}
        	reproduciendo=true;
    		break;
    		
    		
    	case 2: //avance
    		procesando=true;
    		reproduciendo=true;
    		mHandler.post(mCompruebaFinPista); //utilizamos el handler para cambiar de pista
    		procesando=false;
    		mHandler.post(mCompruebaBoton);
    		break;
    		
    	case 3: //retroceso
    		procesando=true;
    		reproduciendo=true;
    		//paramos la reproduccion
    		if(mp!=null && mp.isPlaying())
				mp.stop();
    		//con esto pasamos de la primera a la primera posici�n en caso de no estar en modo aleatorio
    		if(modoRepro.intestado()!=3 && modoRepro.intestado()!=4)
    		{
	    		if(cursor.moveToPrevious() && !cursor.isBeforeFirst())
	    		{
	    			if(mp!=null && mp.isPlaying())
	    				mp.stop();
	    			areproducir(cursor,true);
	    		}
	    		else
	    		{
	    			cursor.moveToFirst();
	    			areproducir(cursor,true);
	    			
	    		}
	    			
    		}
    		else
    			//miramos los dem�s modos de reproducci�n inicializando un handler
	    	    mHandler.post(mCompruebaFinPista);
    		
    		procesando=false;
    		mHandler.post(mCompruebaBoton);
    		break;
    		
    		
    	case 4: //detener
    		//se establece el sem�foro
    		procesando=true;
    		
    		// Detenemos la reproducci�n
            mp.stop();       

            //quitamos el bloqueo del pausado
            pausado=false;
            
            // Establecemos el estado a Detenido
            estado.setText(R.string.detenido);

            //establecemos la canci�n actual a ninguna
            sonando="";
            
            // Ocultamos la barra de progreso
            progreso.setVisibility(ProgressBar.GONE);
            
            procesando=false;
            
            //cambiamos el bot�n
            mHandler.post(mCompruebaBoton);
            
            //ponemos la variable reproduciendo a false
            reproduciendo=false;
            
            sonandom.setText(R.string.sinPista);
            sonandoa.setText(R.string.sinPista);
            //quitamos la notificaci�n
            notificacionManager.cancel(23);
            
    		break;
    	}
    }
    /**
     * M�todo que se activar� cuando pulsemos un bot�n de nuestra aplicaci�n
     * que haya sido capacitado para esta tarea mediante un listener
     * @param v vista de la actividad en la que estamos
     * @return no devuelve nada
     * @exception excepciones No lanza ninguna
     */
    public void onClick(View v) {
    	    
    		
    	
        // Si el usuario hace clic sobre el bot�n Reproducir...
        if(v.equals(reproducir))
        {
        	//llamamos al m�todo encargado del manejo de los botones
        	botonPulsado(1);
        }
        
        // Si el usuario ha hecho clic sobre el bot�n Detener y estamos reproduciendo
        if(v.equals(detener) && mp!=null)
        {
        	//llamamos al m�todo encargado del manejo de los botones
            botonPulsado(4);
        }
        
    	//Si hemos pulsado el bot�n de avance de pista
    	if(v.equals(avance))
    	{
    		//llamamos al m�todo encargado del manejo de los botones
    		botonPulsado(2);
    	}
    	
    	//retroceso de pista
    	if(v.equals(retroceso))
    	{
    		//llamamos al m�todo encargado del manejo de los botones
    		botonPulsado(3);
    	}
    	
    	//si pulsamos el bot�n de modo, inicamos la actividad de selecci�n de modo
    	if(v.equals(modo))
    	{
    		Intent intent = new Intent(); //creamos un nuevo intent
	  		intent.setClass(ActividadReproductorMusica.this, ActividadModoReproduccion.class); // asociamos el cambio de actividad
	  		intent.putExtra("modo", modoRepro); //a�adimos un elemento serializable con es modoRepro
	  		startActivityForResult(intent, 24); //inicamos la actividad a la espera de un resultado con el numero 24
    	}
    	
    	//hemos pulsado el bot�n de la estrella
    	if(v.equals(estrella))
    	{
    		//recomendamos las canciones o ponemos mostramos un mensaje para activar
    		recomindaCanciones(); 
    	}
    	
    	//hemos pulsado sobre la informaci�n de la pista
    	if(v.equals(fondoLetra))
    	{
	    	//mostramos un mensaje de Biblioteca por pantalla
	        Toast.makeText(getBaseContext(),R.string.biblioteca,Toast.LENGTH_SHORT).show();
	        //creamos e iniciamos la actividad de biblioteca
	        Intent intent = new Intent();
	        intent.setClass(ActividadReproductorMusica.this, ActividadBibliotecaL.class);
	        intent.putExtra("lista", lista);
	        startActivityForResult(intent, 23); // el resultado de las actividades de la biblioteca viene dado por el 23
    	}   
    	
    	//hemos pulsado sobre la informaci�n de la pista
    	if(v.equals(r))
    	{
    	  Intent intent2 = new Intent();
  	  	  intent2.setClass(ActividadReproductorMusica.this, ActividadRecomendacion.class);
  	  	  intent2.putExtra("reco", estadoR);
  	  	  startActivityForResult(intent2, 25); // el resultado de las actividades de la recomendaci�n viene dado por el 25
      	  
    	}
    	
    }
    
    /**
     * M�todo que se iniciar� al llamar a un hilo Thread y es el encargado de actualizar el reloj de la pista
     * así como de llamar a un actualizador puesto que la pantalla no se puede llamar desde un hilo
     * @return no devuelve nada
     * @exception excepciones No lanza ninguna
     */
    public void run() {
    	
        int posicionActual = 0; //posicion actual de la pista
        mp.getDuration();
        String cadena=""; //cadena para mostar
        
        // Mientras estemos reproduciendo y no hayamos llegado al final...
        while(mp!=null){
           
    		
        	//System.out.println( mp.isPlaying() +" "+ reproduciendo +" "+ !procesando +" "+ !pausado);
        	
        	if(mp!=null && !mp.isPlaying() && reproduciendo && !procesando && !pausado)
            {
            	mHandler.post(mCompruebaFinPista);	
            }
        		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			// Obtenemos la posici�n por la que se est� reproduciendo. (En milisegundos)
			posicionActual = mp.getCurrentPosition();

        	//obtenemos la cadena formatada convenientemente
        	cadena=formato.aCadena("duracion","mm:ss",mp.getDuration(),posicionActual);
				
			
            
            // Hacemos que la barra de progreso referencie el valor actual.
            if(mp!=null && mp.isPlaying())
            {
            	progreso.setProgress(posicionActual);		
            }	
            //cambiamos la cadena del reloj	    	
            posicionReloj=cadena;
            
            if(!moviendo) //si no estamos moviendo la reproducci�n
            //llamamos al actualizador apra que lo actualice
            mHandler.post(mActualizarReloj);
            
        }
    }
    
    /**
     * M�todo que se llama al hacer clic en un elemento de la lista de reproducci�n
     * @param parent vista de la actividad padre
     * @param v vista de la actividad
     * @param position posici�n que ocupa el elemento seleccionado
     * @param id identificador del elemento seleccionado
     * @return no devuelve nada
     * @exception excepciones No lanza ninguna
     */
    public void onListItemClick(ListView parent, View v, int position, long id) {
    	//aquí es donde tiene que sacar el nombre d ela base de datos
        procesando=true;
        if(mp != null && mp.isPlaying())
        	mp.reset();
        cursor.moveToPosition(position);
        reproducir.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.botonpause));
        areproducir(cursor,true);
       
  }
   
  /**
   * @class IconicAdapter
   * Esta clase se encarga de mostrar la lista de reproducci�n con sus elementos a partir del la matriz de items que previamente
   * inicializamos a partir del cursor. Es una clase que est� definida dentro de la clase ReproductorMusica porque necesita de muchos
   * par�metros de esta primera. Adem�s hace uso de elementos gr�ficos con lo que no puede definirse en otro sitio
   */
  class IconicAdapter extends ArrayAdapter<Object> {

        Activity context; //cremaos una nueva actividad contexto

        /**
         * constructor de la clase Iconic Adapter
         * @param context actividad anterior que se cre� en la clase
         */
        IconicAdapter(Activity context) {
        	  //obtenemos de la clase superior la actividad, el layout y el vector de items 
              super(context, R.layout.fila, items);
              //inicalizamos la actividad de la clase con el obtenido de la anterior
              this.context=context;
              
              if(!estadoR.getActivo())
              {
              ListView lv = getListView();
              
              //inicalizamos un long click item para mostrar un cuadro de di�logo con opciones para las pistas
  	          lv.setOnItemLongClickListener(new OnItemLongClickListener(){
  	          
  	          /**
  	           * M�todo que nos permite obtener un resultado en el caso de hacer un click largo en un elemento de la lista
  	           * En este caso crea un menu de di�logo para las opciones de cada pista
  	           */
  	          public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int row, long arg3) {
  	          // ponemos el título y las opciones para el menú de di�logo
  	          builder.setTitle(R.string.opcionpista);
  	          builder.setItems(new String[]{getString(R.string.detalless),getString(R.string.quitarlista)}, new DialogInterface.OnClickListener() {
  	          //creamos el listener para el menú
  	          public void onClick(DialogInterface dialog, int pos) {
  	             switch(pos)
  	             {
  	             	case 0:
  	             		Intent intent = new Intent(); //creamos un nuevo intent
  	      	  		    intent.setClass(ActividadReproductorMusica.this, ActividadDetallesPista.class); // asociamos el cambio de actividad
  	      	  		    intent.putExtra("nombrePista", items[row][2]); //a�adimos un elemento serializable con es modoRepro
  	      	  		    startActivityForResult(intent,31);
  	             		break;
  	             	case 1:
  	             		procesando=true;
  	             		lista.quitarDeListaActual(items[row][2]);
  	             		actualizaReproduccion(1); 
  	             		procesando=false;
  	             		break;
  	             }
  	          }
  	          }
  	          );
  	          AlertDialog alert = builder.create();
  	          alert.show();
  	          return true; //si hay �xito en la operaci�n de crear el menú con las opciones mandamos un true
  	          }
  	          }
  	       );
              }
              
              /*
               * Caso que en el que la recomendaci�n est� activa, que nos muestra en el menú de pista m�s opciones como quitar de ese día
               */
              else //creamos un menu para la pista con m�s opciones
              {
                  ListView lv = getListView();
                  //inicalizamos un long click item para mostrar un cuadro de di�logo con opciones para las pistas
      	          lv.setOnItemLongClickListener(new OnItemLongClickListener(){
      	          
      	          /**
      	           * M�todo que nos permite obtener un resultado en el caso de hacer un click largo en un elemento de la lista
      	           * En este caso crea un menu de di�logo para las opciones de cada pista
      	           */
      	          public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int row, long arg3) {
      	          // ponemos el título y las opciones para el menú de di�logo
      	          builder.setTitle(R.string.opcionpista);
      	          
      	          builder.setItems(new String[]{getString(R.string.detalless),getString(R.string.quitarlista),getString(R.string.nohumor)}, new DialogInterface.OnClickListener() {
      	          //creamos el listener para el menú
      	          public void onClick(DialogInterface dialog, int pos) {
      	             switch(pos)
      	             {
      	             	case 0:
      	             		Intent intent = new Intent(); //creamos un nuevo intent
      	      	  		    intent.setClass(ActividadReproductorMusica.this, ActividadDetallesPista.class); // asociamos el cambio de actividad
      	      	  		    intent.putExtra("nombrePista", items[row][2]); //a�adimos un elemento serializable con es modoRepro
      	      	  		    startActivityForResult(intent,31);
      	             		break;
      	             	case 1:
      	             		procesando=true;
      	             		lista.quitarDeListaActual(items[row][2]);
      	             		actualizaReproduccion(1); 
      	             		procesando=false;
      	             		break;
      	             	case 2:
      	             		cancionEncuestada=items[row][2];
      	             		lista.quitarDeListaActual(items[row][2]);
      	             		Intent intent2 = new Intent(); //creamos un nuevo intent
      	      	  		    intent2.setClass(ActividadReproductorMusica.this, ActividadEncuesta.class); // asociamos el cambio de actividad
      	      	  		    intent2.putExtra("encuesta",encuesta); //a�adimos un para cambiar en la actividad
      	      	  	        startActivityForResult(intent2, 30);
      	             		
      	             		
      	             		
      	             }
      	          }
      	          }
      	          );
      	          AlertDialog alert = builder.create();
      	          alert.show();
      	          return true; //si hay �xito en la operaci�n de crear el menú con las opciones mandamos un true
      	          }
      	          }
      	       );
                  }
            	  
        }
        /**
         * m�todo encargado de rellenar las filas con la informaci�n de las canciones
         * @param position posiciíon a rellenar
         * @param convertView vista de la fila
         * @param parent grupo de vista al que pertenece
         * @return vista modificada de convertView
         */
        public View getView(int position, View convertView, ViewGroup parent) {         
            View row = convertView; //vista de la fila
            ViewWrapper wrapper = null; //clase envoltorio de las vistas

            // si no se existe se crea
            if (row == null) { 
                row = View.inflate(this.getContext(), R.layout.fila, null);
                wrapper = new ViewWrapper(row);
                row.setTag(wrapper);
            } else {
               //si existe se le asigna la etiqueta de esa columna wrapper
               wrapper = (ViewWrapper) row.getTag();
            }
            //ponemos en cada fila los elementos que hemos introducido en la matriz de elementos
            wrapper.getLabel().setText(items[position][0]);
            wrapper.getLabel2().setText(items[position][1]);
            wrapper.getLabel3().setText("  -  "+formato.aCadena("duracion","mm:ss",Integer.parseInt(items[position][3]),Integer.parseInt(items[position][3])));
            
            if (items[position][0].length() > 3) {
            	if(!cancionSonando.equals(items[position][2]))
            		wrapper.getIcon(items[position][2]).setImageResource(R.drawable.iconomusica); //ponemos el icono
            	else
            		wrapper.getIcon(items[position][2]).setImageResource(R.drawable.iconomusica2);
            }
            return (row); //devolvemos la vista
        }
        
        /**
		 * @class  ViewWrapper : clase encargada de manejar las etiquetas de cada columna, encarg�ndose de la parte  de actualizaci�n de datos en la pantalla de la aplicaci�n android
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
                    label3 = (TextView) base.findViewById(R.id.duracionPista);
                }
                return (label3);
            }
            ImageView getIcon(String nombre) {
                if (icon == null) {
                	icon = (ImageView) base.findViewById(R.id.iconomusica);
                }
                return (icon);
            }
        }
         

  }
  
  /**
   * M�todo encargado de crear el menú de opciones que sale al pulsar la tecla menú del dispositivo 
   * @param menu variable que contiene el menu de la actividad android
   * @return true si no ha ocurrido nungún problema
   */
  public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      menu.add(0, 0, 0, R.string.biblioteca).setIcon(R.drawable.biblioteca);
      menu.add(0, 1, 0, R.string.recomendacion).setIcon(R.drawable.recomendacion);
      menu.add(0, 2, 0, R.string.herramientas).setIcon(R.drawable.herramientas);
      menu.add(0, 3, 0, R.string.listare).setIcon(R.drawable.botonpl);
      return true;
  }	

  /**
   * M�todo encargado de escuchar si hemos pulsado algún bot�n del menú.
   * @param featureId 
   * @param item elemento seleccionado
   * @return booleano indicando el exito de la operaci�n
   */
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
      switch(item.getItemId()) {
      
      case 0: //Hemos pulsado en el bot�n de la biblioteca
    	  //mostramos un mensaje de Biblioteca por pantalla
          Toast.makeText(getBaseContext(),R.string.biblioteca,Toast.LENGTH_SHORT).show();
          //creamos e iniciamos la actividad de biblioteca
          Intent intent = new Intent();
          intent.setClass(ActividadReproductorMusica.this, ActividadBibliotecaL.class);
          intent.putExtra("lista", lista);
          startActivityForResult(intent, 23); // el resultado de las actividades de la biblioteca viene dado por el 23
          break;
          
          
      case 1: //hemos pulsado el bot�n de recomendaci�n
    	//creamos e iniciamos la actividad de recomendaci�n
    	  Intent intent2 = new Intent();
	  	  intent2.setClass(ActividadReproductorMusica.this, ActividadRecomendacion.class);
	  	  intent2.putExtra("reco", estadoR);
	  	  startActivityForResult(intent2, 25); // el resultado de las actividades de la recomendaci�n viene dado por el 25
    	  break;
    	  
    	  
      case 2: //hemos pulsado el bot�n de opciones
    	  //creamos un di�logo de opciones en pantalla
    	  AlertDialog.Builder  builder = new  AlertDialog.Builder(this);
          builder.setTitle(R.string.escogeopcion); //le asignamos un título
          //creamos las opciones del menu de di�logo
          builder.setItems(new String[]{getString(R.string.estado),getString(R.string.modoop),
        		  getString(R.string.recoop),getString(R.string.webop),getString(R.string.prefeop)}, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int pos) {
            	  switch (pos)
            	  {
            	  case(0):
            		 
            		  Toast.makeText(ActividadReproductorMusica.this,estadoR.getAnimo(),10).show();
            		  estadoR.actualizaEstado(estadoR.getAnimo(),0);
            		  Intent intent = new Intent();
      	  			  intent.setClass(ActividadReproductorMusica.this, ActividadEstadoSistema.class);
      	  		      intent.putExtra("estado", estadoR);
      	  		      startActivityForResult(intent, 26);
            		  break;
            	  
            	  case(1):
            		    Intent intent2 = new Intent();
            	  		intent2.setClass(ActividadReproductorMusica.this, ActividadModoReproduccion.class);
            	  		intent2.putExtra("modo", modoRepro);
            	  		startActivityForResult(intent2, 24);
            	  		
            		  break;
            	  case(2):
            		    Intent intent3 = new Intent();
            	  		intent3.setClass(ActividadReproductorMusica.this, ActividadRecomendacion.class);
            	  		intent3.putExtra("reco", estadoR);
            	  		startActivityForResult(intent3, 25);
            	  		break;
            	  case(3):
          	  	    ActividadReproductorMusica.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(preferencias.getURLweb())));
            	  
            	  case(4):
	          		    Intent intent4 = new Intent();
	          	  		intent4.setClass(ActividadReproductorMusica.this, ActividadPreferencias.class);
	          	  		intent4.putExtra("prefe", preferencias);
	          	  		startActivityForResult(intent4, 27);
	            	  	break;
            	 
            	  }
                 
             
          }
          });
          AlertDialog alert = builder.create(); //creamos el menú
          alert.show(); //lo mostramos
          break;
          
      case 3:
    	
	      Toast.makeText(ActividadReproductorMusica.this,R.string.listaguardre,10).show();
	      if(lista.estaVacia())
	      {
	          Toast.makeText(ActividadReproductorMusica.this,R.string.listavacia,10).show();
	       }
	      else
	      {
	    	  Intent intentli = new Intent();
	    	  intentli.setClass(ActividadReproductorMusica.this, ActividadGuardaLista.class);
	    	  intentli.putExtra("lista", lista );
	    	  startActivityForResult(intentli, 29);
	      }
	        
    	  break;
         
      }

      return super.onMenuItemSelected(featureId, item);
}
  
  /**
   * Este M�todo es el encargado de recoger todos los resultados producidos por las actividades
   * @param requestCode es el número de la actividad que ha acabado
   * @param resultCode es el número que determina e resultado de dicha operaci�n
   * @param data si la actividad ha devuelto un dato, lo inserta en data
   * @return no devuelve nada
   */
  protected void onActivityResult(int requestCode, int resultCode,Intent data) 
  {
	  
	  if(requestCode==23) //resultado para lista de reproduccion
	  {
		
		 boolean entro=false;
		 //entramos aquí cuando hemos salido dando al bot�n de reproducir, es decir, cuando queremos reproducir algo
         if (resultCode ==23) {
        	 if(lista!=null)
    		 {
    			 actualizaReproduccion(0); 
    		 }
        	 //entramos aquí cuando llegamos desde la última actividad pulsando una canci�n para reproducir
        	 if(mp != null && mp.isPlaying())
				  mp.stop();
			  procesando=true;
			  cursor.moveToFirst();
			  areproducir(cursor,true);
			  entro=true;
          }
         if(!entro)
         {
        	 actualizaReproduccion(1); 
         }
	  }
	  
	  if(requestCode==24) //resultado para modo de reproduccion
	  {
		 
         if (resultCode ==24) {
        	 //entramos aquí cuando llegamos desde la última actividad pulsando un modo para reproducir
        	 
        	 
        	 if(modoRepro.intestado()==0)
        		 bg = getResources().getDrawable(R.drawable.modo0);
        	 else  if(modoRepro.intestado()==1)
        		 bg = getResources().getDrawable(R.drawable.modo1);
        	 else  if(modoRepro.intestado()==2)
        		 bg = getResources().getDrawable(R.drawable.modo2);
        	 else  if(modoRepro.intestado()==3)
        		 bg = getResources().getDrawable(R.drawable.modo3);
        	 else  if(modoRepro.intestado()==4)
        		 bg = getResources().getDrawable(R.drawable.modo4);
        	 
        	 
        	 modo.setBackgroundDrawable(bg);
          }
	  }
	  
	  if(requestCode==25) //resultado para modo de reproduccion
	  {
		 
         if (resultCode ==25) {
        	 //entramos aquí cuando llegamos desde la última actividad pulsando un modo para reproducir
        	 
          }
         
         //si est� activo cambiamos los colores del cuadro de texto para saber cu�l es el estado en el que estamos
         if(estadoR.getActivo())
         {
    		 r.setVisibility(View.VISIBLE);
    		 estadoRecomendando=true;
    		 if(estadoR.getAnimo().equals("ENERGICA"))
    		 {
    			 fondoLetra.setBackgroundDrawable(getResources().getDrawable(R.drawable.fondoletraene));
    			 sonandom.setTextColor(Color.WHITE);
    			 sonandoa.setTextColor(Color.WHITE);
    			 textoSonando.setTextColor(Color.BLACK);
    			 textoArtista.setTextColor(Color.BLACK);
    		 }
    		 else if(estadoR.getAnimo().equals("ALEGRE"))
    		 {
    			 fondoLetra.setBackgroundDrawable(getResources().getDrawable(R.drawable.fondoletraale));
    			 sonandom.setTextColor(Color.BLACK);
    		 	 sonandoa.setTextColor(Color.BLACK);
    		 	 textoSonando.setTextColor(Color.BLUE);
    		 	 textoArtista.setTextColor(Color.BLUE);
    		 }
    		 else if(estadoR.getAnimo().equals("NOSTALGICA"))
    		  {
    			 fondoLetra.setBackgroundDrawable(getResources().getDrawable(R.drawable.fondoletranos));
    		 	 sonandom.setTextColor(Color.BLACK);
    		 	 sonandoa.setTextColor(Color.BLACK);
    		 	 textoSonando.setTextColor(Color.BLUE);
    		 	 textoArtista.setTextColor(Color.BLUE);
         	 }	
    		 else if(estadoR.getAnimo().equals("TRANQUILIZANTE"))
    		 {
    			 fondoLetra.setBackgroundDrawable(getResources().getDrawable(R.drawable.fondoletratran));
    			 sonandom.setTextColor(Color.WHITE);
    			 sonandoa.setTextColor(Color.WHITE);
    			 textoSonando.setTextColor(Color.YELLOW);
    			 textoArtista.setTextColor(Color.YELLOW);
    		 }
         }	 
    	 else //si hemos desactivado la recomendaci�n cambiamos todo a su formato original
    	 {
    		 r.setVisibility(View.INVISIBLE);
    		 fondoLetra.setBackgroundDrawable(getResources().getDrawable(R.drawable.fondoletra));
    		 sonandom.setTextColor(Color.BLACK);
			 sonandoa.setTextColor(Color.BLACK);
			 textoSonando.setTextColor(Color.BLUE);
			 textoArtista.setTextColor(Color.BLUE);
			 
    	 } 
         
         estadoR.actualizaEstado(estadoR.getAnimo(),0);
         if(estadoR.getActivo())
         {
        	 /* 
        	  * Esta parte del c�digo se hace aquí para s�lo hacerla la primera vez que vamos a usar la recomendaci�n y así no entorpecer
        	  * el uso normal de la aplicaci�n puesto que puede tardar varios segundos
        	  */
        	 if(!iniciado)
        	 {// Mostramos un dialogo de espera
        		 
        		 pd.show();
        		 
        		 new Thread() {
        			 
                     public void run() { 
                    	 
	                   	 String[] saux= {MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DATA};
	                   	 String sel2 = MediaStore.Audio.Media.IS_MUSIC + " != 0";
	                   	 //obtenemos un cursor con todas las pistas
	                   	 Cursor aux= managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,saux,sel2,null,null);
	                   	 //creamos un menejador para la base de datos que se encargar� de abrirla, inicializarla o actualizarla.
	                   	 mBaseDatos= new ManejaBBDD(BBDD,aux,preferencias.getTiempoSinREproducir(),PATH_USOPISTAS);	
	                   	 //quitamos el mensaje de espera
	                   	 pd.dismiss();

               	 
                 iniciado=true;	 
                     }
                     
                 }.start();
	       
        	 }
         	}
	         //en caso de haber tenido la recomendaci�n activa y haberla quitado guardamos datos
	         if(estadoRecomendando)
	         {
	        	 if(mBaseDatos!=null)
					{
						pd2.show();
		        		 
		        		 new Thread() 
		        		 {
		        			 
		                     public void run() 
		                     {
		                    	 //actualizamos los par�metros
		                    	 if(estadoR.getAnimo().equals("ALEGRE"))
		                    		 mBaseDatos.actualizaTiemposSinReproducir(1,estadoR.getHora() , preferencias.getTiempoSinREproducir());
		                 		else if(estadoR.getAnimo().equals("ENERGICA"))
		                 			mBaseDatos.actualizaTiemposSinReproducir(2,estadoR.getHora() , preferencias.getTiempoSinREproducir());
		                 		else if(estadoR.getAnimo().equals("TRANQUILIZANTE"))
		                 			mBaseDatos.actualizaTiemposSinReproducir(3,estadoR.getHora() , preferencias.getTiempoSinREproducir());
		                 		else if(estadoR.getAnimo().equals("NOSTALGICA"))
		                 			mBaseDatos.actualizaTiemposSinReproducir(4,estadoR.getHora() , preferencias.getTiempoSinREproducir());
		                    	 
		                    	 mBaseDatos.finalizaRecomendacion();
		                    	 estadoRecomendando=false;
		                    	 pd2.dismiss();
		                     }
		        		 }.start();
					}
         }
	  }
	  
	  if(requestCode==27) //resultado de las preferencias
	  {System.out.println("ENTRO A BORRA LA BASE DE DATOS2");
		  if (resultCode ==27) //si la encuesta se ha completado satisfactoriamente reiniciamos los par�metros oportunos
		  {System.out.println("ENTRO A BORRA LA BASE DE DATOS3");
			  cerrarComMusicA();
		  }
	  }
	  
	  if(requestCode==30) //resultado de la encuesta
	  {
		  if (resultCode ==30) //si la encuesta se ha completado satisfactoriamente reiniciamos los par�metros oportunos
		  {
			  
			mBaseDatos.reiniciaParametros(cancionEncuestada, estadoR.getAnimo(), encuesta.getAnimo(), 
     			estadoR.getHora(), encuesta.getHora(), estadoR.getDia(), encuesta.getDia());
			cancionEncuestada="";
		  }
	  }
	  	  
		  
		 
		 
  }
  
  	/**
  	 * M�todo encargado de actualizar el cursor de reproducci�n así como de actualizar la lista que se muestra por pantalla
  	 * @param situaReproduccion : 0 para no situar y !=0 para situar el cursor de reproducci�n en su sitio
  	 * @return no devuelve nada
  	 */
	  public void actualizaReproduccion(int situaReproduccion)
	  {
		  procesando=true;
		  //cursor.close();
		  rellenaCursor(lista.getLista());
		  
		  //prepara la lista de reproducci�n que se mostrar�
	      rellenaLista(cursor);
	      if(situaReproduccion!=0) // en caso de no tener que situar el cursor no lo llamamos
	    	  situaCursor();
	      listaAFichero(lista,archivoLista);
	      setListAdapter(new IconicAdapter(this));
	      procesando=false;
	  }

	
	/**
	 * M�todo encargado de situar el cursor correctamente despu�s de a�adir pistas 
	 */
	public  void situaCursor() {
		for(int i=0;i<items.length;i++)
		{
			if(sonando.equals(items[i][2]))
				cursor.moveToPosition(i);
		}
	}


	/**
	 * M�todo que nos rellena el cursor de reproducci�n a partir de una lista de reproducci�n que hemos creado
	 * o en caso de ser null la lista, nos cargar� toda la música que tengamos en nuestro sistema
	 * @param listaReproduccion
	 */
	public void rellenaCursor(List<String> listaReproduccion)
	{
		String[] proyection={
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.DURATION
			};
		//creamos la selecci�n y proyection para los par�metros que queremos introducir en el cursor
		String seleccion="";
		
		if(listaReproduccion==null) //si queremos reproducir toda la música
		{
			seleccion=sel;
		}
		else //si vamos a reproducir la lista de reproducci�n
		{
			//creamos el where de la consulta a la base de datos
			for(int i=0;i<listaReproduccion.size();i++)
			{
				if(i==0)
					seleccion+=MediaStore.Audio.Media.DATA+ " = \"" +listaReproduccion.get(i)+"\"";
				else
					seleccion+=" OR "+MediaStore.Audio.Media.DATA+ " = \"" +listaReproduccion.get(i)+"\"";
			}
		}
		//obtenemos el cursor
		cursor=this.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proyection, seleccion,null,null);
		cursor.moveToFirst(); //lo movemos al principio
		
	}
	/**
	 * M�todo que nos va a permitir aplicar configuraciones desde AndroidManifest para por ejemplo no reiniciar la actividad al girar el m�vil
	 */
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}
	
	/**
	 *M�todo encargado de pedir la lista de canciones a la clase ManejaBBDD para que nos devuelva la m�s indicada 
	 *
	 */
	public void recomindaCanciones()
	{
		if(estadoR.getActivo())
        {	
			procesando=true;
			//recomendamos una lista
			Cursor c=mBaseDatos.recomiendaEntidades(preferencias.getMaxPistas(), estadoR.getAnimo(), estadoR.getHora(), estadoR.getDia());
			if(c!=null)
			{
				c.moveToFirst(); //inicializamos el cursor
				lista.vaciarLista(); //vaciamos la lista e introducimos en ella los datos provenientes de las pistas recomendadas
				while(!c.isAfterLast())
				{
					lista.anadirAListaActual(c.getString(1));
					c.moveToNext();
				}
				c.close();
				actualizaReproduccion(1); //actualizo las listas d ereproducci�n
				Toast.makeText(ActividadReproductorMusica.this,R.string.recomendando,10).show();
			}
			else
				Toast.makeText(ActividadReproductorMusica.this,R.string.norecomendando,10).show();
			 procesando=false;
        }
		else
		{
			showDialog(999); 
		}
		
	}
	
	//definici�n de un objeto de la clase di�logo para mostrar los cuadros de di�logo que se muestran por pantalla
	protected Dialog onCreateDialog(int id) 
    { 
        switch (id) 
        { 
         case (999): 
            return new AlertDialog.Builder(this) 
                .setTitle(R.string.activalista) 
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() { 
                    public void onClick(DialogInterface dialog, int whichButton) { 
                    	Intent intent3 = new Intent();
            	  		intent3.setClass(ActividadReproductorMusica.this, ActividadRecomendacion.class);
            	  		intent3.putExtra("reco", estadoR);
            	  		startActivityForResult(intent3, 25);
                    	
                    } 
                }) 
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() { 
                    public void onClick(DialogInterface dialog, int whichButton) { 
                      
                    	return;
                    } 
                }) 
                .create(); 
        } 
        return null; 
        } 


	/**
	 * M�todo encargado de actualizar la reproducci�n si la barra de progreso es cambiada de sitio
	 * @param seekBar es la barra de progreso
	 * @param progress progresso que lleva la barra
	 * @param fromUser el usuario que ha movido la barra
	 * @return no devuelve nada 
	 */
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
	{
		
		if(reproduciendoAhora!=null) //si estamos reproduciendo ahora cambiamos el progreso de la pista
		{
		   reproduciendoAhora.setPosicionActual(progress);
		   posicionReloj=formato.aCadena("duracion","mm:ss",mp.getDuration(),reproduciendoAhora.getPosicionActual());
		   mHandler.post(mActualizarReloj);
		   
		}
		
			
	}
	
	/**
	 * M�todo que se llama al hacer click sobre la barra de progreso
	 * @param seekBar barra de progreso
	 * @return no devuelve nada
	 */
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		procesando=true;
		moviendo=true;
		//inicializamos la clase que definimos al principio que contendr� la informaci�n sobre la pista
		reproduciendoAhora=new Reproduciendo(mp.toString(),mp.getDuration());
		estado.setText(R.string.barra); 
		//pausamos la reproducci�n
		mp.pause();
		
	}
	
	/**
	 * M�todo que se llama autom�ticamente cuando soltamos la barra de progreso
	 * @param seekBar
	 */
	public void onStopTrackingTouch(SeekBar seekBar) 
	{
		procesando=false;
		moviendo=false;
		estado.setText(R.string.reproduciendo);
		//movemos la pista a la posici�n indicada al soltar la barra
		mp.seekTo(reproduciendoAhora.getPosicionActual());
		//seguimos con la reproducci�n en ese punto
		mp.start();
		//ponemos el bot�n de play de forma adecuada
		mHandler.post(mCompruebaBoton);
	}
	
	/**
	 * M�todo que crea un archivo de texto con los nombres de la lista de reproducci�n
	 * @param lista lista de reproduccion de la que se vana  asacar los par�metros
	 * @param fichero en el que se escribir�
	 */
	public void listaAFichero(ListaDeReproduccionActual lista,File fichero)
	{
		try {
			 fw= new FileWriter(fichero);
			 bw= new BufferedWriter(fw);
			 for(int i=0;i<lista.tamano();i++)
				 bw.write(lista.verElemento(i)+'\n');
			 bw.close();
			 fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}