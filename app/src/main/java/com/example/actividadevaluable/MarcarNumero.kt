package com.example.actividadevaluable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.actividadevaluable.databinding.ActivityMarcarNumeroBinding
import android.content.SharedPreferences
import android.content.pm.PackageManager

class MarcarNumero : AppCompatActivity() {

    private lateinit var marcarNumeroBinding: ActivityMarcarNumeroBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var pickContactLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        marcarNumeroBinding = ActivityMarcarNumeroBinding.inflate(layoutInflater)
        setContentView(marcarNumeroBinding.root)

        initSharedPreferences()
        initPickContactLauncher()
        setListener()
    }

    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences(getString(R.string.shared_prefernces), Context.MODE_PRIVATE)
    }

    private fun initPickContactLauncher() {
        pickContactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                data?.data?.let { contactUri ->
                    val cursor = contentResolver.query(contactUri, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val phoneNumber = it.getString(phoneIndex)
                            marcarNumeroBinding.textoTelefono.setText(phoneNumber)
                            sharedPreferences.edit().putString("shared_phone_key", phoneNumber).apply()
                        }
                    }
                }
            }
        }
    }

    private fun setListener() {
        marcarNumeroBinding.buttonAceptar.setOnClickListener {
            val phoneText = marcarNumeroBinding.textoTelefono.text
            if (phoneText.isNullOrBlank()) {
                Toast.makeText(this, "Número no válido", Toast.LENGTH_SHORT).show()
            } else {
                if (!formatoValido(phoneText)) {
                    Toast.makeText(this, getString(R.string.no_valido_numero_de_telefono), Toast.LENGTH_SHORT).show()
                } else {
                    sharedPreferences.edit().putString("shared_phone_key", phoneText.toString()).apply()
                    val intent = Intent(this, LlamarTelefono::class.java)
                    startActivity(intent)
                }
            }
        }

        marcarNumeroBinding.button.setOnClickListener {
            checkContactPermission()
        }

        marcarNumeroBinding.botonHome.setOnClickListener { volverCasa() }
    }

    private fun checkContactPermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), PICK_CONTACT_REQUEST)
        } else {
            // El permiso ya ha sido concedido
            openContactPicker()
        }
    }

    private fun openContactPicker() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        pickContactLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_CONTACT_REQUEST) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openContactPicker()
            } else {
                Toast.makeText(this, "Permiso de contactos denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatoValido(numeroTelefono: Editable): Boolean {
        // Expresión regular para validar el número de teléfono
        val validarLongitud = Regex("^(\\+34\\s*|\\s*)(6|7|9)\\s*[0-9]{8}$")
        val numero = numeroTelefono.toString().trim().replace("\\s+".toRegex(), "")  // Eliminar espacios en blanco antes de validar
        val esValido = validarLongitud.matches(numero)

        // Mensaje de depuración
        if (!esValido) {
            Toast.makeText(this, "Formato inválido para el número: $numero", Toast.LENGTH_SHORT).show()
        }
        return esValido
    }


    fun volverCasa() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val PICK_CONTACT_REQUEST = 1 // Código de solicitud para el intent de selección de contacto
    }
}
