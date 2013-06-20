package musica.reproductor;
import java.io.Serializable;
import java.util.Calendar;



/**
 * Estado.java
 * Clase encargada de gestionar el estado del sistema, su actualización y el paso de los parámetros
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 */
public class Estado implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -698022367390760969L;
	public static boolean activaRecomendacion;
	public static String animo;
	public static String dia;
	public static String hora;
	public static Calendar tiempo;
	
	/**
	 * Constructor de la clase entidad. Este constructor se encarga de inicializar las variables en el momento de la creación
	 */
	Estado()
	{
		tiempo=Calendar.getInstance();
		//iniciamos con la recomendación apagada
		activaRecomendacion=false;
		animo="Recomendación no activa";
		
		int d=tiempo.get(Calendar.DAY_OF_WEEK);
		
		if(d==1 || d==7)
			dia="FIN_DE_SEMANA";
		else
			dia="LABORABLE";
		
		//ponemos la franja horaria en la que estamos pusto que es indiferente de la recomendación
		int h=((tiempo.get(Calendar.HOUR_OF_DAY))%24);
		if(h>=21 || h<6)
			hora ="NOCHE";
		else if(h>=6 && h <9)
			hora ="DESPERTAR";
		else if(h>=9 && h <12)
			hora ="MAÑANA";
		else if(h>=12 && h <21)
			hora ="TARDE";
	}
	
	/**
	 * este método nos permite activar la recomendación mediante un booleano
	 * @param reco booleano que activara o desactivará la recomendación
	 */
	public void activarRecomendacion(boolean reco)
	{
		activaRecomendacion=reco;
	}
	
	/**
	 * Este método nos permite actualizar el estado de ánimo segñun la cadena que nos den así como los distintos
	 * parámetros de la clase
	 * @param animo asignación del estado de ánimo
	 * @return no devuelve nada
	 */
	public void actualizaEstado(String animo,int zonaHoraria)
	{
		tiempo=Calendar.getInstance();
		if(activaRecomendacion)
			Estado.animo=animo;
		else
			Estado.animo="ANIMO : Recomendación no activa";
			
		
		
		int d=tiempo.get(Calendar.DAY_OF_WEEK);
		if(d==1 || d==7)
			dia="FIN_DE_SEMANA";
		else
			dia="LABORABLE";
		
		int h=((tiempo.get(Calendar.HOUR_OF_DAY)+zonaHoraria)%24);
		System.out.println("LA HORA ES ="+h);
		if(h>=21 || h<6)
			hora ="NOCHE";
		else if(h>=6 && h <9)
			hora ="DESPERTAR";
		else if(h>=9 && h <12)
			hora ="MAÑANA";
		else if(h>=12 && h <21)
			hora ="TARDE";
		System.out.println("LA HORA ES ="+h+" "+hora);
	}
	
	/**
	 * este métdo nos devuelve la cadena con el estado de ánimo asignado en este momento
	 * @return  animo string
	 */
	public String getAnimo()
	{
		return animo;
	}
	
	/**
	 * Este método nos devuelve la cadena correspondiente a la hora en la que estamos
	 * @return  hora string
	 */
	public String getHora()
	{
		return hora;
	}
	
	/**
	 * Este método nos devuelve la cadena correspondiente al día (laborable o no)
	 * @return  dia string
	 */
	public String getDia()
	{
		return dia;
	}
	
	/**
	 * este método nos devuelve el estado del sistema de recomendación
	 * @return boolean true si activo o false si no activo
	 */
	public boolean getActivo()
	{
		return activaRecomendacion;
	}
}
