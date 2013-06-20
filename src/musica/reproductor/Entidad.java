package musica.reproductor;

/**
 * Entidad.java
 * Clase encargada de abstraer una entidad perteneciente al programa y que 
 * será usada por clases cercanas a la base de datos para guardar las entidades. 
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 */ 
public class Entidad {

	private int id;
	private String nombre;
	private boolean laborable;
	private boolean fin_semana;
	private int alegre;
	private int nostalgica;
	private int energica;
	private int relajante;
	private int despertar;
	private int manana;
	private int tarde;
	private int noche;
	private int tiempoSinReproducirAlegre;
	private int tiempoSinReproducirEnergica;
	private int tiempoSinReproducirTranquila;
	private int tiempoSinReproducirNostalgica;
	
	
	/**
	 * Constructor de la clase Entidad que inicializa obligatoriamente el nombre y el id perteneciente a cada pista o entidad
	 * @param nombre
	 * @param id
	 */
	Entidad(String nombre, int id)
	{
		this.nombre=nombre;
		this.id=id;
		laborable=false;
		fin_semana=false;
		alegre=0;
		nostalgica=0;
		energica=0;
		relajante=0;
		despertar=0;
		manana=0;
		tarde=0;
		noche=0;
		tiempoSinReproducirAlegre=10000;
		tiempoSinReproducirEnergica=10000;
		tiempoSinReproducirTranquila=10000;
		tiempoSinReproducirNostalgica=10000;
		
	}
	
	/**
	 * asigna un valor a la variable nombre de la clase
	 * @param nombre  cadena a asignar
	 * @return  no devuelve nada
	 */
	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}
	
	/**
	 * asigna un valor a la variable idestadoR.getAnimo() de la clase
	 * @param id  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setId(int id)
	{
		this.id=id;
	}
	
	/**
	 * asigna un valor a la variable laborable de la clase
	 * @param laborable  booleano a asignar
	 * @return  no devuelve nada
	 */
	public void setLaborable( boolean laborable)
	{
		this.laborable=laborable;
	}
	
	/**
	 * asigna un valor a la variable no_laborable de la clase
	 * @param finSemana booleano a asignar
	 * @return no devuelve nada
	 */
	public void setFinSemana(boolean finSemana)
	{
		this.fin_semana=finSemana;
	}
	
	/**
	 * asigna un valor a la variable alegre de la clase
	 * @param alegre  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setAlegre(int alegre)
	{
		this.alegre=alegre;
	}
	
	/**
	 * asigna un valor a la variable energica de la clase
	 * @param energica  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setEnergica(int energica)
	{
		this.energica=energica;
	}
	
	/**
	 * asigna un valor a la variable nostalgica de la clase
	 * @param nostalgica  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setNostalgica(int nostalgica)
	{
		this.nostalgica=nostalgica;
	}
	
	/**
	 * asigna un valor a la variable relajante de la clase
	 * @param relajante  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setRelajante(int relajante)
	{
		this.relajante=relajante;
	}
	
	/**
	 * asigna un valor a la variable despertar de la clase
	 * @param despertar  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setDespertar(int despertar)
	{
		this.despertar=despertar;
	}
	
	/**
	 * asigna un valor a la variable ma�ana de la clase
	 * @param alegre  ma�ana a asignar
	 * @return  no devuelve nada
	 */
	public void setManana(int manana)
	{
		this.manana=manana;
	}
	
	/**
	 * asigna un valor a la variable tarde de la clase
	 * @param tarde  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setTarde(int tarde)
	{
		this.tarde=tarde;	
	}
	
	/**
	 * asigna un valor a la variable noche de la clase
	 * @param noche  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setNoche(int noche)
	{
		this.noche=noche;
	}
	
	/**
	 * asigna un valor a la variable tiempoSinReproducir de la clase
	 * @param tiempo entero a asignar
	 * @return no devuelve nada
	 */
	public void setTiempoSinReproducir(int tiempo)
	{
		this.tiempoSinReproducirAlegre=tiempo;
		this.tiempoSinReproducirEnergica=tiempo;
		this.tiempoSinReproducirTranquila=tiempo;
		this.tiempoSinReproducirNostalgica=tiempo;
		
	}

	/**
	 * asigna un valor a la variable tiempoSinReproducir de la clase
	 * @param tiempo  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setTiempoSinReproducirAlegre(int tiempo)
	{
		this.tiempoSinReproducirAlegre=tiempo;
		
	}
	
	/**
	 * asigna un valor a la variable tiempoSinReproducir de la clase
	 * @param tiempo  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setTiempoSinReproducirEnergica(int tiempo)
	{
		this.tiempoSinReproducirEnergica=tiempo;
		
	}
	
	/**
	 * asigna un valor a la variable tiempoSinReproducir de la clase
	 * @param tiempo  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setTiempoSinReproducirTranquila(int tiempo)
	{
		this.tiempoSinReproducirTranquila=tiempo;
		
	}
	
	/**
	 * asigna un valor a la variable tiempoSinReproducir de la clase
	 * @param tiempo  entero a asignar
	 * @return  no devuelve nada
	 */
	public void setTiempoSinReproducirNostalgica(int tiempo)
	{

		this.tiempoSinReproducirNostalgica=tiempo;
		
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  nombre cadena
	 */
	public String getNombre()
	{
		return nombre;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  id entero
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  booleano true si laborable y false si no laborable
	 */
	public int getLaborable()
	{
		if(!laborable)
			return 0;
		else
			return 1;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return booleano true si no_laborable y false si no no_laborable
	 */
	public int getFinSemana()
	{
		if(!fin_semana)
			return 0;
		else
			return 1;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  alegre entero
	 */
	public int getAlegre()
	{
		return alegre;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  energica entero
	 */
	public int getEnergica()
	{
		return energica;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  nostalgica entero
	 */
	public int getNostalgica()
	{
		return nostalgica;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  relajante entero
	 */
	public int getRelajante()
	{
		return relajante;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  despertar entero
	 */
	public int getDespertar()
	{
		return despertar;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  ma�ana entero
	 */
	public int getManana()
	{
		return manana;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  tarde entero
	 */
	public int getTarde()
	{
		return tarde;	
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  noche entero
	 */
	public int getNoche()
	{
		return noche;
	}
	
	/**
	 * devuelve un parámetro de la clase
	 * @return  tiempoSinReproducir entero
	 */
	public int getTiempoSinReproducirAlegre()
	{
		return tiempoSinReproducirAlegre;
	}
	/**
	 * devuelve un parámetro de la clase
	 * @return  tiempoSinReproducir entero
	 */
	public int getTiempoSinReproducirEnergica()
	{
		return tiempoSinReproducirEnergica;
	}
	/**
	 * devuelve un parámetro de la clase
	 * @return  tiempoSinReproducir entero
	 */
	public int getTiempoSinReproducirTranquila()
	{
		return tiempoSinReproducirTranquila;
	}
	/**
	 * devuelve un parámetro de la clase
	 * @return  tiempoSinReproducir entero
	 */
	public int getTiempoSinReproducirNostalgica()
	{
		return tiempoSinReproducirNostalgica;
	}
	
}

