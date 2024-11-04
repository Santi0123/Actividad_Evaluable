package com.example.actividadevaluable

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
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
        calendar.add(Calendar.MINUTE, 2)

        val alarmHour = calendar.get(Calendar.HOUR_OF_DAY)
        val alarmMinute = calendar.get(Calendar.MINUTE)

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, alarmHour)
            putExtra(AlarmClock.EXTRA_MINUTES, alarmMinute)
            putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma programada desde la app")

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
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
        Toast.makeText(this, getString(R.string.boton_enlace), Toast.LENGTH_SHORT).show()
    }
}
