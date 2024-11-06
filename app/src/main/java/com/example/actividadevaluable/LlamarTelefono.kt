package com.example.actividadevaluable

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.actividadevaluable.databinding.ActivityLlamarTelefonoBinding

class LlamarTelefono : AppCompatActivity() {

    private lateinit var llamarTelefonoBinding: ActivityLlamarTelefonoBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val REQUEST_CALL_PHONE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        llamarTelefonoBinding = ActivityLlamarTelefonoBinding.inflate(layoutInflater)
        setContentView(llamarTelefonoBinding.root)

        initUI()
    }

    private fun initUI() {
        initSharedPreferences()
        start()
        setListener()
    }

    private fun initSharedPreferences() {
        this.sharedPreferences = getSharedPreferences(getString(R.string.shared_prefernces), Context.MODE_PRIVATE)
    }

    private fun start() {
        val phone = sharedPreferences.getString("shared_phone_key", null)

        phone?.let {
            llamarTelefonoBinding.muestraTelefono.setText(it)
        }
    }

    private fun setListener() {
        llamarTelefonoBinding.nuevoTelefono.setOnClickListener { volverMarcarNumero() }
        llamarTelefonoBinding.llamarSos.setOnClickListener { realizarLlamada() } // Cambia esto según el ID correcto
        llamarTelefonoBinding.botonHome.setOnClickListener{volverCasa()}
    }

    private fun volverMarcarNumero() {
        val intent = Intent(this, MarcarNumero::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    private fun realizarLlamada() {
        val phone = sharedPreferences.getString("shared_phone_key", null)

        if (!phone.isNullOrEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//comprueba la version de sdk
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {//comprueba los permisos
                    hacerLlamada(phone)
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE)
                }
            } else {
                hacerLlamada(phone)
            }
        } else {
            Toast.makeText(this, "No hay un número de teléfono disponible", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * realiza la accion de hacer la llamada
     */
    private fun hacerLlamada(phone: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
        startActivity(intent)
    }

    /**
     * Es una funcion que maneja los permisos si el usuario los ha acptado o lo ha denegado
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, realiza la llamada
                val phone = sharedPreferences.getString("shared_phone_key", null)
                phone?.let { hacerLlamada(it) }
            } else {
                Toast.makeText(this, "Ha sido denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun volverCasa(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
}
