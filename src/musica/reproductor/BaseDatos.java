package musica.reproductor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * BaseDatos.java: BaseDatos.java
 * Clase encargada de almacenar y realizar las operaciones directamente sobre la base de datos.
 * Esta clase es al encargada de crear las bases de datos, hacer consultas, modificaciones y eliminaciones en 
 * la base de datos del sistema
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class BaseDatos {
	private static String rutaDefecto ="sdcard/ComMusicA/miBBDD.bd";
	private static final String FOLDER_SD = "sdcard/";
	private static final String FOLDER_APP = "ComMusicA/";

	        
	private File archivoBBDD;
	public static SQLiteDatabase miBBDD;
	private static List<Tabla> tablas;
	
	String CREATE_TABLE_1 = "CREATE TABLE IF NOT EXISTS dia (" +
     "id INTEGER PRIMARY KEY, " +
     "laborable INTEGER NOT NULL, " +
     "fin_de_semana INTEGER NOT NULL, "+
     "FOREIGN KEY (id) REFERENCES canciones(id) ON DELETE CASCADE);";
	String CREATE_TABLE_2 = "CREATE TABLE IF NOT EXISTS animo (" +
    "id INTEGER PRIMARY KEY , " +
    "alegre INTEGER NOT NULL, " +
    "nostalgica INTEGER NOT NULL, " +
    "energica INTEGER NOT NULL, " +
    "tranquilizante INTEGER NOT NULL," +
    "FOREIGN KEY (id) REFERENCES canciones(id) ON DELETE CASCADE);";
	String CREATE_TABLE_3 = "CREATE TABLE IF NOT EXISTS hora (" +
    "id INTEGER PRIMARY KEY , " +
    "despertar INTEGER NOT NULL, " +
    "manana INTEGER NOT NULL, " +
    "tarde INTEGER NOT NULL, " +
    "noche INTEGER NOT NULL," +
    "FOREIGN KEY (id) REFERENCES canciones(id) ON DELETE CASCADE)";
	String CREATE_TABLE_4 = "CREATE TABLE IF NOT EXISTS canciones (" +
    "nombre VARCHAR(500) NOT NULL , " +
    "id PRIMARY KEY);";
	
	/**
	 * Constructor de la clase base de datos. 
	 * El constructor se encargará de inciializar o crear la base de datos de recomendación del sistema así como
	 * de las tablas que usaremos en la misma
	 */
	 public BaseDatos(){
		 //creamos una carpeta con el nombre de la aplicación si no existe
		 File f = new File(FOLDER_SD +  FOLDER_APP);
	        if(!f.exists())
	        {
	            f.mkdir();
	        }
		 try{
			 archivoBBDD= new File(rutaDefecto);
			 
			 //apertura de base de datos
			 miBBDD = SQLiteDatabase.openOrCreateDatabase(archivoBBDD, null);
         
			 //apertura de las tablas
			 miBBDD.execSQL(CREATE_TABLE_4);
			 miBBDD.execSQL(CREATE_TABLE_2);
			 miBBDD.execSQL(CREATE_TABLE_3);
			 miBBDD.execSQL(CREATE_TABLE_1);
				 
			 //creación de las tablas
			 tablas= new ArrayList<Tabla>();
			 tablas.add(new Tabla("dia"));
			 tablas.add(new Tabla("animo"));
			 tablas.add(new Tabla("hora"));
			 tablas.add(new Tabla("canciones"));
		 }
		 catch(Exception e){
             Log.d("BASEDEDATOS", "Exception initDB: " + e.toString());
           }	 
	 }
	 
	 /**
	  * Constructor parametrizado de la clase BaseDatos.
	  * El paŕametro que se a�ade en este constructor es la localización del fichero de la base de datos.
	  * @param localizacionBBDD
	  */
	 public BaseDatos(String localizacionBBDD){
		 //creamos una carpeta con el nombre de la aplicación si no existe
		 File f = new File(FOLDER_SD + FOLDER_APP);
	        if(!f.exists())
	        {
	            f.mkdir();
	        }
		 try{
			 archivoBBDD= new File(localizacionBBDD);
			 rutaDefecto=localizacionBBDD;
			 //apertura de base de datos
			 miBBDD = SQLiteDatabase.openOrCreateDatabase(archivoBBDD, null);
         
			 //apertura de las tablas
			 miBBDD.execSQL(CREATE_TABLE_4);
			 miBBDD.execSQL(CREATE_TABLE_2);
			 miBBDD.execSQL(CREATE_TABLE_3);
			 miBBDD.execSQL(CREATE_TABLE_1);
				 
			 
			 tablas= new ArrayList<Tabla>();
			 tablas.add(new Tabla("dia"));
			 tablas.add(new Tabla("animo"));
			 tablas.add(new Tabla("hora"));
			 tablas.add(new Tabla("canciones"));
		 }
		 catch(Exception e){
             Log.d("BASEDEDATOS", "Exception initDB: " + e.toString());
           }	 
	 }
	 
	 /**
	  * Método encargado de eliminar todas las tablas de las bases de datos así como las tablas de la misma.
	  * @return boolean su se ha completado con éxito la operación
	  */
	 public boolean limpiarTablasBaseDatos()
	 {
		 if(!miBBDD.isOpen())
			 miBBDD = SQLiteDatabase.openOrCreateDatabase(archivoBBDD, null);
		 miBBDD.execSQL("DELETE FROM canciones");
		 miBBDD.execSQL("DELETE FROM dia");
		 miBBDD.execSQL("DELETE FROM animo");
		 miBBDD.execSQL("DELETE FROM hora");
		 
		 return true;
	 }
	 
	 /**
	  * Método encargado de cerrar al base de datos sobre la que trabajamos
	  * @return este métdo no devuelve nada
	  */
	 public void close()
	 {
		 miBBDD.close();
	 }
	 
	 
	 /**
	  * Método que se encarga de crear una entidad en todas las bases de datos a partir de los datos proporcionados
	  * en un elemento de la clase entidad. El método en caso de que exista el elemento , procederá a eliminarlo ara crearlo
	  * posteriormente.
	  * @param entidad objeto de la clae entidad con los datos a introducir en la base de datos
	  */
	 public void crearEntidad(Entidad entidad)
	 {
		 if(!miBBDD.isOpen())
			 miBBDD = SQLiteDatabase.openOrCreateDatabase(archivoBBDD, null);
	     try
		{
	    	 Cursor c= miBBDD.rawQuery("SELECT nombre FROM canciones WHERE nombre like \""+entidad.getNombre()+"\"", null);
	    	 
	    	 if(c!=null && c.moveToFirst()) //si la entidad existe la borramos, puesto que es más rápido borrarla y crear una que modificar los parámetros
	    	 {
	    		 
	    			miBBDD.execSQL("DELETE FROM canciones where id='"+entidad.getId()+"';");
	    		 	miBBDD.execSQL("DELETE FROM dia where id='"+entidad.getId()+"';");
	    		 	miBBDD.execSQL("DELETE FROM animo where id='"+entidad.getId()+"';");
	    		 	miBBDD.execSQL("DELETE FROM hora where id='"+entidad.getId()+"';");
	    		 	System.out.println("borrando elementos existentes");
	    		
	    	 }
	    	 c.close(); //cerramos el cursor e introducimos los datos nuevos
	    		 miBBDD.execSQL("INSERT INTO canciones (nombre,id) values(\""+entidad.getNombre()+"\",'"+entidad.getId()+"');");
	    		 miBBDD.execSQL("INSERT INTO dia (id,laborable,fin_de_semana) values ('"+entidad.getId()+"','"+entidad.getLaborable()+"','"+entidad.getFinSemana()+"');");
	    		 miBBDD.execSQL("INSERT INTO animo (id,alegre,nostalgica,energica,tranquilizante) values ('"+entidad.getId()+"','"+entidad.getAlegre()+"','"+entidad.getNostalgica()+"','"+entidad.getEnergica()+"','"+entidad.getRelajante()+"');");
	    		 miBBDD.execSQL("INSERT INTO hora (id,despertar,manana,tarde,noche) values ('"+entidad.getId()+"','"+entidad.getDespertar()+"','"+entidad.getManana()+"','"+entidad.getTarde()+"','"+ entidad.getNoche()+"');");
	    		 System.out.println("creando elementos existentes");
	    	 
		 }catch(Exception e){
             Log.d("BASEDEDATOS", "Exception crearEntidad: " + e.toString());
       }	 
		 
	 }
	 
	 /**
	  * Método encargado de eliminar todas las tablas que hemos creado.
	  * @param tabla
	  * @return
	  */
	 public boolean borrarTablas(String tabla)
	 {
		 
		 for(int i=0;i<tablas.size();i++)
			 miBBDD.delete(tablas.get(i).getNombre(),null,null);
		 return true;
	 }
	 
	 /**
	  * Método encargado de crear un cursor con todos los elementos de la base de datos. A partir de este cursor,
	  * podremos acceder a los datos que tenemos guardados en la misma.
	  * @return
	  */
	public Cursor exportarBaseDatos() {
		Cursor c = null;
		 try
			{
			 if(!miBBDD.isOpen())
				 miBBDD = SQLiteDatabase.openOrCreateDatabase(archivoBBDD, null);
			 //creación del cursor con todos los parámetros que necesitamos conocer
			 c= miBBDD.rawQuery("SELECT canciones.id,canciones.nombre,dia.laborable," +
				"dia.fin_de_semana,animo.alegre, animo.nostalgica, animo.energica, " +
				"animo.tranquilizante, hora.despertar, hora.manana, hora.tarde, hora.noche " +
				"FROM canciones,dia,animo,hora WHERE canciones.id == dia.id and canciones.id == animo.id " +
				"and canciones.id==hora.id ORDER BY canciones.id" , null);
			}catch(Exception e){
	             Log.d("BASEDEDATOS", "Exception exportarDB: " + e.toString());
			}
		return c;
	}
	
	/**
	 * Método encargado del sistema de recomendación. Este método permite obtener los elementos mejor puntuados en la tabla
	 * a partir de unos parámetros de decisión que le introducimos.
	 * @param n número de resultados (pistas) máximo a obtener
	 * @param estado estado anímico preferente
	 * @param hora hora del día preferente
	 * @param dia día de la semana preferente
	 * @return Cursor, devuelve un cursor con los elementos obtenidos
	 */
	public Cursor consultaConParametros(int n, String estado, String hora, String dia)
	{
		Cursor c=null;
		try
		{
		/*
		 * Para cambiar los parámtros de recomendación es necesario modificar las consultas de abajo. Se pueden adaptar
		 * tanto cómo se deseen, podemos poner una proporción en el modo de recomendar unas canciones u otras
		 * o no recomendar ciertas pistas si no satisfacen un parámetro. Estos parámetros son los que permitirán al
		 * sistema de recomendación más o menos eficaz.
		 */
		if(!miBBDD.isOpen())
			 miBBDD = SQLiteDatabase.openOrCreateDatabase(archivoBBDD, null);
		if(!hora.equals("MAÑANA"))
			c= miBBDD.rawQuery("SELECT canciones.id,canciones.nombre, animo.alegre, hora.tarde from canciones, animo, hora, dia" +
		 		" WHERE canciones.id==animo.id AND animo.id==hora.id AND hora.id == dia.id AND dia."+dia+"==1 AND animo."+estado+"!=0 AND hora."+hora+"!=0 order by animo."+estado+"+hora."+hora+" desc LIMIT "+n , null);
		else //caso especial para el caso de ma�ana puesto que no reconoce la �
			c= miBBDD.rawQuery("SELECT canciones.id,canciones.nombre, animo.alegre, hora.tarde from canciones, animo, hora, dia" +
			 		" WHERE canciones.id==animo.id AND animo.id==hora.id AND hora.id == dia.id AND dia."+dia+"==1 AND animo."+estado+"!=0 AND hora.manana !=0 order by animo."+estado+"+hora.manana desc LIMIT "+n , null);
			
		}catch(Exception e){
            Log.d("BASEDEDATOS", "Exception recomiendaDB: " + e.toString());
		}
		
		return c;
	}
	
	/**
	 * Método que devuelve si la base de datos se encuentra abierta
	 * @return true si la base está abierta o false si se encuentra cerrada
	 */
	public boolean isOpen()
	{
		boolean abierta=false;
		try{
			abierta = miBBDD.isOpen();
		}catch(Exception e){};
		return abierta;
	}
			 
}
