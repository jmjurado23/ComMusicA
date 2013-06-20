package musica.reproductor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.InputType;

/**
 * ActividadPreferencias.java: ActividadPreferencias.java
 * Actividad encargada de las preferencias del programa. 
 * @author Juan Manuel Jurado Ruiz	
 * @author <i72juruj@uco.es>
 * @version 1.0
 * 
 */
public class ActividadPreferencias extends PreferenceActivity {
	
	private EditTextPreference maxPistasEdit ;
	private EditTextPreference tiempoSinRepro;
	private EditTextPreference web;
	private Preferencias preferencias;
	private Preference acercade;
	private Preference limpiarDatos;
	private Preference limpiarBBDD;
	private OnPreferenceClickListener onPreferenceClickListener;
	private OnPreferenceClickListener onPreferenceClickListener2;
	private File archivo;
    private FileWriter fw =null;
    private BufferedWriter bw=null;
    private ActividadPreferencias prefe;
	
	/**
     * Método que es llamado al iniciar una actividad Android. Encargado de inicializar algunos elementos
     * y de llamar a los Listeners.
     * @param savedInstanceState es un parámetro con el estado guardado de una actividad anterior
     * @return No devuelve ningun valor
     * @exception excepciones No lanza ninguna
     */
    protected void onCreate(Bundle savedInstanceState) {
    	//guardado del estado al iniciar esta actividad	
        super.onCreate(savedInstanceState);
        //se carga el layout de la actividad
        addPreferencesFromResource(R.xml.preferencias);
        
        onPreferenceClickListener=new OutputPreferenceClickListener();
        onPreferenceClickListener2=new OutputPreferenceClickListener2();
        
        
        //recuperamos los datos de la actividad anterior
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            preferencias = (Preferencias) bundle.getSerializable("prefe");
            
        }
        prefe=this;
        //insertamos la dirección para el archivo de preferencias
        archivo= new File(preferencias.getLocArchivoPreferencias());
        
        //editamos el cuadro de texto para poder sólo introducir números
        maxPistasEdit=(EditTextPreference)getPreferenceScreen().findPreference( "maxpistas"); 
        maxPistasEdit.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        maxPistasEdit.setText(Integer.toString(preferencias.getMaxPistas()));
        
        tiempoSinRepro=(EditTextPreference)getPreferenceScreen().findPreference( "tiemposinreproducir"); 
        tiempoSinRepro.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        tiempoSinRepro.setText(Integer.toString(preferencias.getTiempoSinREproducir()));
        
        web=(EditTextPreference)getPreferenceScreen().findPreference( "web"); 
        web.setText(preferencias.getURLweb());
        
        
        limpiarDatos=(Preference)getPreferenceScreen().findPreference("borrardatos");
        limpiarDatos.setOnPreferenceClickListener(onPreferenceClickListener);
        
        limpiarBBDD=(Preference)getPreferenceScreen().findPreference("borrarBBDD");
        limpiarBBDD.setOnPreferenceClickListener(onPreferenceClickListener2);
        
        //creamos la actividad por si se pulsa la opción de acercaDe
        acercade=(Preference)getPreferenceScreen().findPreference( "acercade"); 
        Intent intent = new Intent();
		intent.setClass(this, ActividadAcercaDe.class);
        acercade.setIntent(intent); //Lanzamos la actividad en el caso de pulsarla
       
    }
    
    
    protected void onStop(){
    	super.onStop();
        //obtenemos el valor máximo de las pistas a recomendar y lo guardamos en la clase de preferencias
        String cadenaaux= maxPistasEdit.getText();
        preferencias.setMaxPistas(Integer.parseInt(cadenaaux));
        String cadenaaux2= tiempoSinRepro.getText();
        preferencias.setTiempoSinReproducir(Integer.parseInt(cadenaaux2));
        String cadenaaux3= web.getText();
        preferencias.setURLweb(web.getText());
        
        try { //guardamos en el fichero las opciones
			 fw= new FileWriter(archivo);
			 bw= new BufferedWriter(fw);
			 
			 System.out.println(preferencias.getMaxPistas()+" "+preferencias.getTiempoSinREproducir());
			 //escribimos en el fichero los tiempos que lleva sin reproducirse cada estado de ánimo en cada entidad
			 bw.write(cadenaaux+'\n');
			 bw.write(cadenaaux2+'\n');
			 bw.write(cadenaaux3);
			 
			 bw.close();
			 fw.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		
		
    }
    
    
    protected Dialog onCreateDialog(int id) 
    { 
        switch (id) 
        { 
         //se crea el diálogo de borrado de datos de reproducción
         case (999): 
            return new AlertDialog.Builder(this) 
                .setTitle(R.string.borrar) 
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() { 
                	public void onClick(DialogInterface dialog, int whichButton) { 	
                		File archivoLista=new File(preferencias.getLocArchivoLista());
                		File archivoPreferencias=new File(preferencias.getLocArchivoPreferencias());
                		archivoLista.delete();
                		archivoPreferencias.delete();
                		preferencias.setMaxPistas(preferencias.getMaxPistasDefecto());
                		preferencias.setTiempoSinReproducir(preferencias.getTiempoSinReproducirDefecto());
                    } 
                }) 
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() { 
                    public void onClick(DialogInterface dialog, int whichButton) { 
                      
                    	return;
                    } 
                }) 
                .create(); 
         //se crea un diálogo de borrado de datos para la base de datos
         case (998): 
             return new AlertDialog.Builder(this) 
                 .setTitle(R.string.borrar) 
                 .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() { 
                 	public void onClick(DialogInterface dialog, int whichButton) { 	
                 		File archivoBBDD=new File(preferencias.getLocArchivoBBDD());
                 		File archivoUsoPistas=new File(preferencias.getLocArchivoUsoPistas());
                 		archivoBBDD.delete();
                 		archivoUsoPistas.delete();
                 		prefe.setResult(27);
                 		prefe.finish();
                 		
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
     * Clase que implementa una pulsación de una preferencia
     */
    class OutputPreferenceClickListener implements OnPreferenceClickListener {
        public boolean onPreferenceClick(Preference pref) {
        		showDialog(999); //se muestra el cuadro de diálogo de borrar datos de reproducción
                return true;
        }      
    }
    /**
     * Clase que implementa una pulsación de una preferencia
     */
    class OutputPreferenceClickListener2 implements OnPreferenceClickListener {
        public boolean onPreferenceClick(Preference pref) {
        		showDialog(998); //mostramos el cuadro de diálogo de borrar datos de recomendación 
                return true;
        }      
    }

}



