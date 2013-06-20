package musica.reproductor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import android.database.Cursor;

/**
 * ManejaBBDD.java : ManejaBBDD.java
 * Clase encargada de gestionar la base de datos y que sirve de nexo entre la reproducción de contenidos y la base de datos.
 * Esta clase se encarga de ser la intermediaria entre ambos, proporcionando al reproductor una interfaz con la que actuar
 * sobre los parámetros de la base de datos además de tener una copia dinámica de todas las entidades (pistas) del sistema.
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 */
public class ManejaBBDD {
	
	
	private static BaseDatos BBDD;
	private List<Entidad> entidades;
	private List<Integer> modificados;
	private String PATH_USOPISTAS = "/sdcard/ComMusicA"+ "/" + "frec_uso.txt";
	private File archivo;
    private FileReader fr = null;
    private BufferedReader br = null;
    private FileWriter fw =null;
    private BufferedWriter bw=null;
    private StringTokenizer st;
	
    /**
     * Constructor de la clase ManejaBBDD parametrizado. Este constructor nos permite inicializar todos los parámetros
     * que necesita la clase para su correcto funcionamiento en el sistema
     * @param BBDD base de datos a manejar
     * @param cursor cursor con todas las pistas del sistema
     * @param tiempoSinReproducir parámetro de la clase preferencias
     * @param localizacionFicheroUso parámetro de la dirección del fichero de tiempo de pistas
     */
	ManejaBBDD(BaseDatos BBDD , Cursor cursor,int tiempoSinReproducir,String localizacionFicheroUso)
	{
		entidades= new ArrayList<Entidad>();
		modificados= new ArrayList<Integer>();
		ManejaBBDD.setBBDD(BBDD);
		
		
		
		//Ahora sacamos todas las pistas que tenemos en nuestra base de datos
		Cursor aux=importarBBDD();
		System.out.println("El cursor de la base de datos tiene :"+aux.getCount()+" elementos");
		System.out.println("Cursor de la base de datos exportada creado!!!");
		if(aux!=null)
		{
			aux.moveToFirst();
			
			for(int i=0;i<aux.getCount();i++)
			{
				//creamos una entidad con todos sus parámetros
				Entidad ent= new Entidad(aux.getString(1),aux.getInt(0));
				System.out.println(aux.getInt(2)+ " "+aux.getInt(3));
				if(aux.getInt(2)==0)
					ent.setLaborable(false);
				else
					ent.setLaborable(true);
				
				if(aux.getInt(3)==0)
					ent.setFinSemana(false);
				else
					ent.setFinSemana(true);
				
				ent.setAlegre(aux.getInt(4));
				ent.setNostalgica(aux.getInt(5));
				ent.setEnergica(aux.getInt(6));
				ent.setRelajante(aux.getInt(7));
				ent.setDespertar(aux.getInt(8));
				ent.setManana(aux.getInt(9));
				ent.setTarde(aux.getInt(10));
				ent.setNoche(aux.getInt(11));
				ent.setTiempoSinReproducir(tiempoSinReproducir);
				
				//almacenamos la entidad y movemos el cursor a la siguiente
				entidades.add(ent);
				aux.moveToNext();
			}
		}
		//ponemos todas las pistas como no modificadas para la recomendación
		for(int i=0;i<entidades.size();i++)
		{
			modificados.add(0);
		}
		
		//Ahora comprobamos que no hay pistas nuevas, en caso de haberlas las a�adimos a la lista de entidades
		// que será actualizado en la base de datos posteriormente.
		boolean igual=false; //crear variables para agilizar el proceso
		String cadena="";

		cursor.moveToFirst();
		for(int i=0;i<cursor.getCount();i++)
		{
			cadena=cursor.getString(1);
			
			igual=false;
			for(int j=0,salir=0;j<entidades.size() && salir==0;j++)
			{
				igual=false;
			
				if(cadena.equals(entidades.get(j).getNombre()))
				{
					igual=true;
					salir=1;
				}
			}
			if(!igual)
			{
				entidades.add(new Entidad(cursor.getString(1),cursor.getInt(0)));
				modificados.add(1);
			}
			cursor.moveToNext();
			
				
		}
		
		if(localizacionFicheroUso!=null)
			PATH_USOPISTAS=localizacionFicheroUso;
		
		//creamos si no está creada el fichero de configuración
		archivo = new File(PATH_USOPISTAS);
        
        actualizarBaseDatosConEntidadesModificadas();
		cargaContenidoUso();
		
		
			
	}
	
	/**
	 * Método encargado de cargar en la lista de entidades los valores que tenían las entidades antes de cerrar la recomendación la
	 * última vez.
	 * @return este m�etodo no devuelve nada
	 */
	public void cargaContenidoUso()
	{
		
		int auxID=0;
		int auxValorTiempoAlegre=0;
		int auxValorTiempoEnergico=0;
		int auxValorTiempoTranquilo=0;
		int auxValorTiempoNostalgico=0;
		boolean salir=false;
		
			try {
			fr = new FileReader (archivo); //abrimos los flujos para leer un fichero
			br = new BufferedReader(fr); 
			String linea="";
			try {
				while((linea=br.readLine())!=null) //leemos línea a línea el fichero con los datos de repdocucción de canciones
				{
					if(linea!=null)
						 st= new StringTokenizer( linea,":" ); //creamos un tokenizer para facilitar el leer parámetros de enteros
						 auxID=Integer.parseInt(st.nextToken()); //leemos el id de la pista
						 auxValorTiempoAlegre=Integer.parseInt(st.nextToken()); //leemos el valor que tiene en el fichero
						 auxValorTiempoEnergico=Integer.parseInt(st.nextToken());
						 auxValorTiempoTranquilo=Integer.parseInt(st.nextToken());
						 auxValorTiempoNostalgico=Integer.parseInt(st.nextToken());
						 salir=false;
						 for(int i=0;i<entidades.size() && !salir;i++) //miramos en todas las entidades y en caso de estar actualizamos el valor.
						 {
							 if(entidades.get(i).getId()==auxID) //si encontramos la entidad buscada actualizamos parámetros
							 {
								 salir=true; //teminamos el bucle para no tener que terminarlo siempre.
								 entidades.get(i).setTiempoSinReproducirAlegre(auxValorTiempoAlegre);
								 entidades.get(i).setTiempoSinReproducirEnergica(auxValorTiempoEnergico);
								 entidades.get(i).setTiempoSinReproducirTranquila(auxValorTiempoTranquilo);
								 entidades.get(i).setTiempoSinReproducirNostalgica(auxValorTiempoNostalgico);
							 }
						 }
				   
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
		} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();}
	}
	
	/**
	 * Método que nos permite actualizar en la base de datos todas aquellas entidades que aparecen nuevas 
	 * en nuestras listas. Estas entidades nuevas suelen ser, entidades que se han a�adido a la base de datos principal
	 * de android.
	 */
	public void actualizarBaseDatosConEntidadesModificadas()
	{
		for(int i=0;i<entidades.size();i++)
		{
			if(modificados.get(i)!=0)
			{
				
				BBDD.crearEntidad(entidades.get(i));
				//ponemos la pista como no modificada
				modificados.set(i, 0);
			}
		}
	}
	
	/**
	 * Método que actualiza todas las entidades de la base de datos que hemos creado, independientemente de si han sido modificadas o no
	 * @return este método no devuelve nada
	 */
	public void actualizarBBDD()
	{
		for(int i=0;i<entidades.size();i++)
		{
			if(entidades.get(i)!=null )
			{
				getBBDD().crearEntidad(entidades.get(i));
			}
		}
	}
	
	/**
	 * Método que crea un cursor con todas las entidades de al base de datos que tengamos asignada en la clase maneja BBDD
	 * @return Cursor este es el cursor que contiene la dirección de todas las entidades leidas de la base de datos
	 */
	public Cursor importarBBDD()
	{
		return getBBDD().exportarBaseDatos();
	}

	
	/**
	 * Método para definir la base de datos de la clase ManejaBBDD
	 * @param bBDD  Base de datos que queremos asignar
	 */
	public static void setBBDD(BaseDatos bBDD) {
		BBDD = bBDD;
	}

	/**
	 * Método para obtener la base de datos que estamos usando en la clase ManejaBBDD
	 * @return  BBDD la base de datos o null si no está definida
	 */
	public static BaseDatos getBBDD() {
		if(BBDD!=null)
			return BBDD;
		return null;
	}
	
	/**
	 * Método que limpia la base de datos completamente y después crea todas las entidades a partir de la lista de entidades.
	 * @return este método no devuelve nada
	 */
	public void actualizaBBDDCompleta()
	{
		BBDD.limpiarTablasBaseDatos();
		System.out.println("Tablas limpias");
		actualizarBBDD();
		
	}
	
	/**
	 * Método encargado de aumentar lso parámetros de una canción.
	 * @param nombre nombre de la pista a la que vamos a aumentar los parámetros
	 * @param estado estado de ánimo que vamos a aumentar en la pista
	 * @param hora hora del día que vamos a incrementar en la entidad
	 * @param dia dia de la semana que vamos a habilitar
	 * @return no devuelve nada
	 */
	public synchronized void aumentarParametrosEntidad(String nombre, String estado, String hora, String dia)
	{
		int i=0,j=-1;
		boolean encontrado=false;
		//en primer lugar buscamos la entidad en la lista de entidades
		for(i=0;i<entidades.size() && encontrado==false;i++)
		{
			if(nombre.equals(entidades.get(i).getNombre()))
			{
				encontrado=true;
				j=i;
			}
		}
		//modificados.set(j, 1);
		//si la hemos encontrado aumentamos los parámetros 
		if(encontrado)
		{
			if(estado.equals("ALEGRE"))
				entidades.get(j).setAlegre(entidades.get(j).getAlegre()+1);
			else if(estado.equals("NOSTALGICA"))
				entidades.get(j).setNostalgica(entidades.get(j).getNostalgica()+1);
			else if(estado.equals("TRANQUILIZANTE"))
				entidades.get(j).setRelajante(entidades.get(j).getRelajante()+1);
			else if(estado.equals("ENERGICA"))
				entidades.get(j).setEnergica(entidades.get(j).getEnergica()+1);
			
			if(hora.equals("MAÑANA"))
				entidades.get(j).setManana(entidades.get(j).getManana()+1);
			else if(hora.equals("TARDE"))
				entidades.get(j).setTarde(entidades.get(j).getTarde()+1);
			else if(hora.equals("NOCHE"))
				entidades.get(j).setNoche(entidades.get(j).getNoche()+1);
			else if(hora.equals("DESPERTAR"))
				entidades.get(j).setDespertar(entidades.get(j).getDespertar()+1);
			
			if(dia.equals("LABORABLE"))
				entidades.get(j).setLaborable(true);
			if(dia.equals("FIN_DE_SEMANA"))
				entidades.get(j).setFinSemana(true);
			
				System.out.println("La pista "+nombre+" se ha modificado con los parámetros"+
						" Alegre:"+entidades.get(j).getAlegre()
						+'\n'+" Nostalgica: "+entidades.get(j).getNostalgica()
						+'\n'+" Relajante:"+entidades.get(j).getRelajante()+
						'\n'+" Energica: "+entidades.get(j).getEnergica()+" " +
						'\n'+"Ma�ana: "+entidades.get(j).getManana()+
						'\n'+" Tarde: "+entidades.get(j).getTarde()+
						'\n'+" Noche: "+entidades.get(j).getNoche()+
						'\n'+" Despertar: "+entidades.get(j).getDespertar()+
						'\n'+" laborable: "+entidades.get(j).getLaborable()+
						'\n'+" Fin_de_semana: "+entidades.get(j).getFinSemana()+
						'\n'+" TiempoAlegre: "+entidades.get(j).getTiempoSinReproducirAlegre()+
						
						'\n'+" TiempoEnergica: "+entidades.get(j).getTiempoSinReproducirEnergica()+
						'\n'+" TiempoRelajante: "+entidades.get(j).getTiempoSinReproducirTranquila()+
						'\n'+" TiempoNostalgica: "+entidades.get(j).getTiempoSinReproducirNostalgica());
				
			//ahora actualizamos el dato en la BBDD
			BBDD.crearEntidad(entidades.get(j));
		}
		
	}
	
	
	/**
	 * Método encargado de pedir a la base de datos a partir de las preferencias que hemos puesto, un tipo de música 
	 * @param n
	 * @param estado
	 * @param hora
	 * @param dia
	 * @return Cursor
	 */
	public synchronized Cursor recomiendaEntidades(int n, String estado, String hora, String dia)
	{
		if(BBDD !=null)
		{
			//si la base de datos está abierta, realizamos la consulta de las pistas a recomendar
			Cursor cursor=BBDD.consultaConParametros(n,estado,hora,dia);
			return cursor;
		}
		return null;
		
	}
	
	/**
	 * Método encargado de quitar parámetros a las entidades
	 * @param nombre nombre de la entidad
	 * @param estado estado de ánimo de la entidad a reinicializar
	 * @param reiniciaestado si true se reinicia y si false no se toca este parámetro
	 * @param hora de la entidad actual
	 * @param reiniciahora indica si la hora se va a reiniciar
	 * @param dia de la entidad actual
	 * @param reiniciadia booleano que nos indica si se tiene que reiniciar 
	 */
	public synchronized void reiniciaParametros(String nombre, String estado, boolean reiniciaestado, String hora, boolean reiniciahora, String dia , boolean reiniciadia)
	{
		int i=0,j=-1;
		boolean encontrado=false;
		
		//en primer lugar buscamos la entidad en la lista de entidades
		System.out.println(entidades.size());
		for(i=0;i<entidades.size() && encontrado==false;i++)
		{
			if(nombre.equals(entidades.get(i).getNombre()))
			{
				encontrado=true;
				j=i;
			}
		}
		//si la hemos encontrado aumentamos los parámetros 
		if(encontrado)
		{
			if(reiniciaestado)
			{
				if(estado.equals("ALEGRE"))
					entidades.get(j).setAlegre(0);
				else if(estado.equals("NOSTALGICA"))
					entidades.get(j).setNostalgica(0);
				else if(estado.equals("TRANQUILIZANTE"))
					entidades.get(j).setRelajante(0);
				else if(estado.equals("ENERGICA"))
					entidades.get(j).setEnergica(0);
			}
			
			if(reiniciahora)
			{
				if(hora.equals("MAÑANA"))
					entidades.get(j).setManana(0);
				else if(hora.equals("TARDE"))
					entidades.get(j).setTarde(0);
				else if(hora.equals("NOCHE"))
					entidades.get(j).setNoche(0);
				else if(hora.equals("DESPERTAR"))
					entidades.get(j).setDespertar(0);
			}
			if(reiniciadia)
			{
				if(dia.equals("LABORABLE"));
					entidades.get(j).setLaborable(false);
				if(dia.equals("FIN_DE_SEMANA"));
					entidades.get(j).setFinSemana(false);
			}
				System.out.println("La pista "+nombre+" se ha modificado con los parámetros"+entidades.get(j).getAlegre()+" "+entidades.get(j).getNostalgica()+entidades.get(j).getRelajante()+" "+entidades.get(j).getEnergica()+" "+entidades.get(j).getManana()+" "+entidades.get(j).getTarde()+" "+entidades.get(j).getNoche()+" "+entidades.get(j).getDespertar()+" "+entidades.get(j).getLaborable()+" "+entidades.get(j).getFinSemana()+" ");
			//ahora actualizamos el dato en la BBDD
			BBDD.crearEntidad(entidades.get(j));
		}
	}
	
	/**
	 * Método que se llama cuando el usuario ha finalizado el sistema de recomendación. Es el momento de guardar los cambios en el fichero de 
	 * veces sin reproducir un pista.
	 */
	public void finalizaRecomendacion()
	{
		if(entidades!=null)
		{	
			try {
				 fw= new FileWriter(archivo);
				 bw= new BufferedWriter(fw);
				 for(int i=0;i<entidades.size();i++)
				 {
					 //escribimos en el fichero los tiempos que lleva sin reproducirse cada estado de ánimo en cada entidad
					 bw.write(entidades.get(i).getId()+":"+entidades.get(i).getTiempoSinReproducirAlegre()+":"+entidades.get(i).getTiempoSinReproducirEnergica()
							 +":"+entidades.get(i).getTiempoSinReproducirTranquila()+":"+entidades.get(i).getTiempoSinReproducirNostalgica()+'\n');
				 }
				 bw.close();
				 fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Método que recupera al valor dado por tiempoSR como veces a activar la recomendación sin devaluar un parámetro 
	 * @param tiempoSR timepo a asignar a la entidad
	 * @param id id de la entidad
	 */
	public void recuperaTiemposSinReproducir(int id,int tiempoSR)
	{
		boolean salir=false;
	
		for(int i=0;i<entidades.size() && !salir;i++)
		{
			if(entidades.get(i).getId()==id)
			{
				entidades.get(i).setTiempoSinReproducir(tiempoSR);
				salir=true;
			}
		}
	}
	
	/**
	 * Método que actualiza las veces que una canción ha estado sin reproducirse. El método resta una unidad a todas las entidades y comprueba que no se 
	 * han quedado sin tiempo. Sólo restará unidades si esa entidad posee las características actuales de reproducción. En caso de quedarse sin ningún
	 * entero, es decir, pasar a 0, se procederá a actualizar la entidad, diviendo entre 2 los parámetros.
	 */
	public void actualizaTiemposSinReproducir(int estadoAnimo, String hora, int tiempoSR)
	{
		for(int i=0;i<entidades.size();i++)
		{
			
			switch (estadoAnimo)
			{
				case 1:
					if(entidades.get(i).getAlegre()>1) //miramos que la pista haya sido recomendada previamente
					{
						//restamos una unidad al estado de ánimo actual
						entidades.get(i).setTiempoSinReproducirAlegre(entidades.get(i).getTiempoSinReproducirAlegre()-1);
						
						if(entidades.get(i).getTiempoSinReproducirAlegre()<=0) //si llevamos mucho tiempo sin recomendarla pasamos a dividir sus parámetros
						{
							entidades.get(i).setTiempoSinReproducirAlegre(tiempoSR);
							entidades.get(i).setAlegre(entidades.get(i).getAlegre()/2);
							if(hora.equals("DESPERTAR"))
								entidades.get(i).setDespertar(entidades.get(i).getDespertar()/2);
							else  if(hora.equals("MAÑANA"))
								entidades.get(i).setManana(entidades.get(i).getManana()/2);
							else  if(hora.equals("TARDE"))
								entidades.get(i).setTarde(entidades.get(i).getTarde()/2);
							else  if(hora.equals("NOCHE"))
								entidades.get(i).setNoche(entidades.get(i).getNoche()/2);
							
							//actualizamos en la base da datos la entidad dada	
							BBDD.crearEntidad(entidades.get(i));
						}
					}
					break;
				case 2:
					if(entidades.get(i).getEnergica()>1) //miramos que la pista haya sido recomendada previamente
					{
						//restamos una unidad al estado de ánimo actual
						entidades.get(i).setTiempoSinReproducirEnergica(entidades.get(i).getTiempoSinReproducirEnergica()-1);
					
						if(entidades.get(i).getTiempoSinReproducirEnergica()<=0) //si llevamos mucho tiempo sin recomendarla pasamos a dividir sus parámetros
						{
							entidades.get(i).setTiempoSinReproducirEnergica(tiempoSR);
							entidades.get(i).setEnergica(entidades.get(i).getEnergica()/2);
							if(hora.equals("DESPERTAR"))
								entidades.get(i).setDespertar(entidades.get(i).getDespertar()/2);
							else  if(hora.equals("MAÑANA"))
								entidades.get(i).setManana(entidades.get(i).getManana()/2);
							else  if(hora.equals("TARDE"))
								entidades.get(i).setTarde(entidades.get(i).getTarde()/2);
							else  if(hora.equals("NOCHE"))
								entidades.get(i).setNoche(entidades.get(i).getNoche()/2);
							//actualizamos en la base da datos la entidad dada	
							BBDD.crearEntidad(entidades.get(i));
						}
					}
					break;
				
				case 3:
					if(entidades.get(i).getRelajante()>1) //miramos que la pista haya sido recomendada previamente
					{
						//restamos una unidad al estado de ánimo actual
						entidades.get(i).setTiempoSinReproducirTranquila(entidades.get(i).getTiempoSinReproducirTranquila()-1);
					
						if(entidades.get(i).getTiempoSinReproducirTranquila()<=0) //si llevamos mucho tiempo sin recomendarla pasamos a dividir sus parámetros
						{
							entidades.get(i).setTiempoSinReproducirTranquila(tiempoSR);
							entidades.get(i).setRelajante(entidades.get(i).getRelajante()/2);
							if(hora.equals("DESPERTAR"))
								entidades.get(i).setDespertar(entidades.get(i).getDespertar()/2);
							else  if(hora.equals("MAÑANA"))
								entidades.get(i).setManana(entidades.get(i).getManana()/2);
							else  if(hora.equals("TARDE"))
								entidades.get(i).setTarde(entidades.get(i).getTarde()/2);
							else  if(hora.equals("NOCHE"))
								entidades.get(i).setNoche(entidades.get(i).getNoche()/2);
							//actualizamos en la base da datos la entidad dada	
							BBDD.crearEntidad(entidades.get(i));
						}	
					}
					break;
					
				case 4:
					if(entidades.get(i).getNostalgica()>1) //miramos que la pista haya sido recomendada previamente
					{
						//restamos una unidad al estado de ánimo actual
						entidades.get(i).setTiempoSinReproducirNostalgica(entidades.get(i).getTiempoSinReproducirNostalgica()-1);
					
						if(entidades.get(i).getTiempoSinReproducirNostalgica()<=0) //si llevamos mucho tiempo sin recomendarla pasamos a dividir sus parámetros
						{
							entidades.get(i).setTiempoSinReproducirNostalgica(tiempoSR);
							entidades.get(i).setNostalgica(entidades.get(i).getNostalgica()/2);
							if(hora.equals("DESPERTAR"))
								entidades.get(i).setDespertar(entidades.get(i).getDespertar()/2);
							else  if(hora.equals("MAÑANA"))
								entidades.get(i).setManana(entidades.get(i).getManana()/2);
							else  if(hora.equals("TARDE"))
								entidades.get(i).setTarde(entidades.get(i).getTarde()/2);
							else  if(hora.equals("NOCHE"))
								entidades.get(i).setNoche(entidades.get(i).getNoche()/2);
							//actualizamos en la base da datos la entidad dada	
							BBDD.crearEntidad(entidades.get(i));
						}
					}
					break;
				
			}
			}
		}
	}


