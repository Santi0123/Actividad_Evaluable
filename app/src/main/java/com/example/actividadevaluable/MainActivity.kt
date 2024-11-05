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

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initUI()
    }

    private fun initUI() {
        setListener()
        initSharedPreferences()
    }

    private fun setListener() {
        mainBinding.buttonTelefono.setOnClickListener { openTelefono() }
        mainBinding.buttonEnlace.setOnClickListener { openUrl("https://github.com/") }
        mainBinding.buttonGps.setOnClickListener { openMap() }
        mainBinding.buttonGmail.setOnClickListener { openAlarma() }
    }

    private fun initSharedPreferences() {
        this.sharedPreferences = getSharedPreferences(getString(R.string.shared_prefernces), Context.MODE_PRIVATE)
    }

    private fun openTelefono() {
        val phone = sharedPreferences.getString("shared_phone_key", null)
        if (phone == null) {
            val intent = Intent(this, MarcarNumero::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LlamarTelefono::class.java)
            startActivity(intent)
        }
    }

    // Para abrir la alarma
    private fun openAlarma() {

        val calendar = Calendar.getInstance()

        calendar.add(Calendar.SECOND, 120)

        val alarmHour = calendar.get(Calendar.HOUR_OF_DAY)
        val alarmMinute = calendar.get(Calendar.MINUTE)

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, alarmHour)
            putExtra(AlarmClock.EXTRA_MINUTES, alarmMinute)
            putExtra(AlarmClock.EXTRA_MESSAGE, "La alarma sonará en ")
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No se encontró una aplicación de reloj compatible", Toast.LENGTH_SHORT).show()
        }

    }

    // Para abrir el Google Maps
    private fun openMap() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
        Toast.makeText(this, getString(R.string.boton_mapa), Toast.LENGTH_SHORT).show()
    }

    // Para abrir mi URL
    private fun openUrl(link: String) {
        if (link.isNotEmpty() && Patterns.WEB_URL.matcher(link).matches()) {
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            // Añade las banderas para que el selector se muestre cada vez
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)

            // Crea el chooser para permitir al usuario seleccionar la aplicación
            val chooser = Intent.createChooser(intent, "Elige el navegador")

            try {
                startActivity(chooser)
                Toast.makeText(this, getString(R.string.boton_enlace), Toast.LENGTH_SHORT).show()
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No se puede abrir el enlace", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "URL no válida", Toast.LENGTH_SHORT).show()
        }
    }
}
