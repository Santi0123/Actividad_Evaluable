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
import android.os.Build
import androidx.annotation.RequiresApi

class MarcarNumero : AppCompatActivity() {

    private lateinit var marcarNumeroBinding: ActivityMarcarNumeroBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var pickContactLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        marcarNumeroBinding = ActivityMarcarNumeroBinding.inflate(layoutInflater)
        setContentView(marcarNumeroBinding.root)

        initSharedPreferences()
        initPickContactLauncher()
        setListener()
    }
    /**
     *     Funcion que inicia las preferencias compartidas
     */
    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences(getString(R.string.shared_prefernces), Context.MODE_PRIVATE)
    }

    /**
     * Inicializamos el lanzador de los contacto y cuando le damos
     * al telefono que elegimos en contactos va a guardarse en el fichero
     * de preferencias compartidos
     */
    private fun initPickContactLauncher() {
        pickContactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                data?.data?.let { contactUri -> // Consulta la información del contacto seleccionado
                    val cursor = contentResolver.query(contactUri, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            // Obtiene el índice de la columna del número de teléfono
                            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            // Extrae el número de teléfono
                            val phoneNumber = it.getString(phoneIndex)
                            // Muestra el número de teléfono en un campo de texto
                            marcarNumeroBinding.textoTelefono.setText(phoneNumber)
                            // Guarda el número de teléfono en las SharedPreferences
                            sharedPreferences.edit().putString("shared_phone_key", phoneNumber).apply()
                        }
                    }
                }
            }
        }
    }

    /**
     * Esta funcion se encarga de aceptar un número,
     * verificar los permisos y volver a la panatalla principal
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListener() {
        marcarNumeroBinding.buttonAceptar.setOnClickListener {
            val phoneText = marcarNumeroBinding.textoTelefono.text
            if (phoneText.isNullOrBlank()) {  // Verifica si el la caja de telefono esta vacía
                Toast.makeText(this, "Número no válido", Toast.LENGTH_SHORT).show()
            } else {
                if (!formatoValido(phoneText)) {  // Verifica si el número de teléfono tiene un formato válido
                    Toast.makeText(this, getString(R.string.no_valido_numero_de_telefono), Toast.LENGTH_SHORT).show()
                } else {
                    sharedPreferences.edit().putString("shared_phone_key", phoneText.toString()).apply() //guarda el telefono en preferencias compartidas
                    val intent = Intent(this, LlamarTelefono::class.java)
                    startActivity(intent)// se  ha llamar telefono
                }
            }
        }

        marcarNumeroBinding.button.setOnClickListener {
            checkContactPermission() // verifica los permisos
        }

        marcarNumeroBinding.botonHome.setOnClickListener { volverCasa() } // boton que vueleve al menu
    }

    /**
     * Chequea los permisos si han sido concedido perfectamente o no
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkContactPermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), PICK_CONTACT_REQUEST)
        } else {
            // El permiso ya ha sido concedido
            openContactPicker()
        }
    }

    /**
     * Esta función crea un Intent que lanza los contactos
     * específicamente la lista de números de teléfono.
     */
    private fun openContactPicker() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        pickContactLauncher.launch(intent)
    }

    /**
     * Nos dice si el usuario ha aceptado los permisos correctamente
     */
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

    /**
     * Nos incica si el formato del telefono es correcto o no
     */
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

    /**
     * Vuelve a al menu principal
     */
    fun volverCasa() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    companion object {
        private const val PICK_CONTACT_REQUEST = 1 // Código de solicitud para el intent de selección de contacto
    }
}
