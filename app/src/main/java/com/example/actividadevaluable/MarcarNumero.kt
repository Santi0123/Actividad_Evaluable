package com.example.actividadevaluable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividadevaluable.databinding.ActivityLlamarTelefonoBinding
import com.example.actividadevaluable.databinding.ActivityMarcarNumeroBinding
import android.content.SharedPreferences

class MarcarNumero : AppCompatActivity() {

    private lateinit var marcarNumeroBinding: ActivityMarcarNumeroBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        marcarNumeroBinding = ActivityMarcarNumeroBinding.inflate(layoutInflater)
        setContentView(marcarNumeroBinding.root)

        initSharedPreferences()
        start()
    }

    private fun initSharedPreferences() {
        this.sharedPreferences = getSharedPreferences(getString(R.string.shared_prefernces), Context.MODE_PRIVATE)

    }

    private fun start() {
        val phone = sharedPreferences.getString("shared_phone_key", null)

        phone?.let {
            marcarNumeroBinding.textoTelefono.setText(it)
        }

        marcarNumeroBinding.buttonAceptar.setOnClickListener {
            if (marcarNumeroBinding.textoTelefono.text.isNullOrBlank()) {
                Toast.makeText(this, "Número no válido", Toast.LENGTH_SHORT).show()
            } else {
                if (!formatoValido(marcarNumeroBinding.textoTelefono.text)) {
                    Toast.makeText(this, getString(R.string.no_valido_numero_de_telefono), Toast.LENGTH_SHORT).show()
                } else {
                    // Guardar el número ingresado
                    sharedPreferences.edit().putString("shared_phone_key", marcarNumeroBinding.textoTelefono.text.toString()).apply()
                    val intent = Intent(this, LlamarTelefono::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun formatoValido(numeroTelefono: Editable): Boolean {
        val validarLongitud = Regex("^(6|7|9)[0-9]{8}$")
        val numero = numeroTelefono.toString()
        return validarLongitud.matches(numero)
    }

    override fun onResume() {
        super.onResume()

        val phone = sharedPreferences.getString("shared_phone_key", null)

        phone?.let {
            marcarNumeroBinding.textoTelefono.setText(it)
        }
    }
}