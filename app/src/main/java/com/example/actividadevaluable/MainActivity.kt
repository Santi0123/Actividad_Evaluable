package com.example.actividadevaluable

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.actividadevaluable.databinding.ActivityMainBinding
import java.util.*

/***
 * @author Santi
 * @version 0.1
 * @since 05-11-2024
 */

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    /**
     * Con es mainBinding infla el layout y inicializo el binding
     * y estblezco la vista del contenido que hay en la actividad.
     * Llamo al metodo initUI para iniciar la interfaz de usuario
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initUI()
    }

    /**
     * La funcion initUI llama a las funciones setListener y initSharedPreferences
     */
    private fun initUI() {
        setListener()
        initSharedPreferences()
    }

    /**
     * Llamo a todas las funcionalidades que tiene los botones del MainActivitity
     */
    private fun setListener() {
        mainBinding.buttonTelefono.setOnClickListener { openTelefono() } // El metodo que va hacer el boton de abrir el telefono
        mainBinding.buttonEnlace.setOnClickListener { openUrl("https://github.com/") } //El metodo va hacer que el boton de abra la url
        mainBinding.buttonGps.setOnClickListener { openMap() } //Boton para abrir el mapa
        mainBinding.buttonGmail.setOnClickListener { openAlarma() } //Boton para abrir la alarma
    }


    private fun initSharedPreferences() {
        this.sharedPreferences = getSharedPreferences(getString(R.string.shared_prefernces), Context.MODE_PRIVATE)
    }

    /**
     * Funcion que se va a encargar de abrir el teléfono,
     * la única dificultad es que si tiene un telefono guardado
     * en la caja de preferencias compartidas,
     * pues que nos mande a un activity o a otro.
     */
    private fun openTelefono() {
        val phone = sharedPreferences.getString("shared_phone_key", null) //se recoge si hay telefono o no
        if (phone == null) { //si no hay nos manda a la clase MarcarNumero
            val intent = Intent(this, MarcarNumero::class.java)
            startActivity(intent)
        } else { //Si hay nos va a llevar directamente a la clase LlamarTelefono
            val intent = Intent(this, LlamarTelefono::class.java)
            startActivity(intent)
        }
    }

    /**
     * Esta funcion es que dandole al boton de la alarma,
     * se nos habra el reloj en la seccion alarma,
     * y esta configurada para que suene en 2 min
     */
    private fun openAlarma() {

        val calendar = Calendar.getInstance()

        calendar.add(Calendar.SECOND, 120)//le decimos cuanto tiempo queremos que sea

        val alarmHour = calendar.get(Calendar.HOUR_OF_DAY)
        val alarmMinute = calendar.get(Calendar.MINUTE)

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, alarmHour) // dice la hora que sonara en la alarma
            putExtra(AlarmClock.EXTRA_MINUTES, alarmMinute) // dice los segundo que sonara en la alarma
            putExtra(AlarmClock.EXTRA_MESSAGE, "La alarma sonará en 2 min") // muestra un mensaje opcional
        }

        if (intent.resolveActivity(packageManager) != null) {//si no es null hace el startActivity
            startActivity(intent)
        } else {//si es null pues muestra un mensaje
            Toast.makeText(this, "No se encontró una aplicación de reloj compatible", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Esta funcion es una de las más basicas,
     * solo abre el google map
     */
    private fun openMap() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
        Toast.makeText(this, getString(R.string.boton_mapa), Toast.LENGTH_SHORT).show()
    }

    /**
     * Habre una URL en el caso que haya y si no la hay,
     * muestra un mensaje
     */
    private fun openUrl(link: String) {

        if (link == null) { // si no hay url manda un mensaje
            Toast.makeText(this, "No hay url", Toast.LENGTH_SHORT).show()
        }else{ // pone la alarma
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
            Toast.makeText(this, "Abriendo url...", Toast.LENGTH_SHORT).show()

        }

    }
}
