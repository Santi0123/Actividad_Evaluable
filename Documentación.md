|              Nombre: | Santiago Fuentes Espinosa                        |
| -------------------: | ------------------------------------------------ |
|     **Fecha:** | 05-11-2024                                       |
| **Asignatura** | Programación multimedia y dispositivos móviles |
|  **Ejercicio** | Practica evaluable                               |
|       **Tema** | Unidad 1                                         |

# ¿Qué nos pide?

1. Un primer Intent, con cualquier acción de configuración de vuestra aplicación,
2. utilizando preferencias compartidas. Por ejemplo, configurar el teléfono.
3. Debéis utilizar el paso de datos entre Activitys, con putExtras.
4. Tenga un Activity principal, con una imagen en todo el centro o un texto a partir
   de un TextView. “Será algo identificativo vuestro”. Utilizar colores de fondo
   personalizados.
5. Tendréis al menos cuatro botones, en los que encontramos:
   a. Llamada telefónica.- Llamará a un segundo Activity en el que aparecerá
   un texto en grande y un botón con una imagen de llamada telefónica al
   que vosotros deseéis. Deberéis utilizar tanto un Intent Explícito como
   Implícito.
   b. Abrir una Url. Hacer que al pulsar el botón de Url, se abra el navegador
   a una dirección web. Intentar que sea lo más personalizado posible.
   c. Crear una alarma, para que suene en 2 minutos. El asunto de la alarma y
   demás parámetros, los podéis poner fijos. “Que sea lo más
   personalizado posible”
   d. ?. La interrogación os la dejo a vosotros para que utilices cualquier
   intención, por ejemplo abrir el correo, el reproductor, lo que queráis.
   “Que cada uno utilice uno distinto”. El botón será personalizado.
6. En la llamada telefónica, debéis programar la lógica necesaria para poder
   ejecutarlo tanto en API inferior a la 23 como superior. Deberéis solicitar
   permisos al usuario en el caso de API mayor o igual al 23.
7. Desde el Activity principal, debéis poder volver al primer activity de
   configuración.

# ¿Cómo se ha realizado?

He planteado la interfaz de la siguiente manera:

He creado una pantalla principal la cual va a contener en la interfaz gráfica se compone de 4 botones de diferente funcionalidad y una imagen la cual es el logo de la aplicación. 

Cada boton hace una funcionalidad diferente:

1. **El botón teléfono -->** Nos va a dar la funcionalidad de que podamos escribir (o  en mi caso he implementado la opcion de elegir en contactos), el numero que queremos tomar como referencia y una vez se haya puesto el número pues se va a otra pantalla la cual tiene varias funcionalidades. Una de las cosas que tiene es que se puede llamar desde el teléfono que se ha puesto anteriormente solo dandole a un botón, otra funcionalidad es que podamos cambiar el teléfono dirigiendonos otra vez a la pantalla donde habiamos escrito el telefono y podemos modificarlo. A todo lo anterior se le añade los permisos que tiene que pedir tanto para entrar en los contactos como para realizar la llamada (como especifica solo se le tendrá que pedir los permisos a todos los dispositivos mayores de API 23)
2. **El botón alarma -->** Va a consistir en solo pursando el boton se nos va a configurar una alarma la cual va a sonar en dos minutos. Como es logico tenemos que importar sus permisos corresponidentes
3. **El botón enlace -->** Es un boton muy simple el cual al pulsarlo nos va a redireccionar a un link (en nuestro caso git hub) el cual se haya definido previamente. Tambien es importante pedir los permisos para acceder a internet.
4. **El botón GPS -->** Su funcionalidad es que al darle se le abra en nuestro casa el google maps y que nos muestre el mapa

Esas serían todas las funcionalidades que tiene la aplicación.


# ¿Cómo se ha realizado la pantalla principal?


### A nivel de interfaz:

En el primer caso la pantalla principal la hemos hecho muy simple. Al iniciar la aplicación, nos sale una aplicación la cual tiene una imagen y justo debajo tiene cuatro botones, los cuales se verian de la siguiente manera:


    ![1730903106031](image/Documentación/1730903106031.png)

Como podemos ver a nivel de interfaz es muy simple e intuitivo.

### A nivel de programación: 

Cuando se planteo el proyecto hice pense en id haciendo lo más "facil" primero y decidí hacer la funcionalidad del botón del link y del gps.

Para hacer el botón del link fue tan sencillo como coger y crearme un una funcion que pasandole una url cogiese y se abriera. Para ello tenia primero que valorar la opción de si habia o no url en el caso de que hubiese url pues se abriria, pero si no hubiese url mostraria la aplicacion diciendo que no hay url.

Este seria el código: 

```kotlin
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

```


Despues realice el boton de abrir el mapa que era tan sencillo como decirle a la aplicacion que se abriese el mapa y que se muestre un mensaje:

```kotlin
private fun openMap() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
        Toast.makeText(this, getString(R.string.boton_mapa), Toast.LENGTH_SHORT).show()
    }
```


Una vez realizada esa fui a un boton el cual me dio unos pequeños problemas pero, gracias a la documentación de android me facilito mucho realizar el boton de la alarma, para ello tenia que configurar la alarma para decirle que suene en dos minutos, en el caso de que se pueda poner la alarma porque haya una aplicación de la alarma pues se pondria sin nigun tipo de problema.

```kotlin
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
```


Y por último, el boton que más tiempo me ha llevado hacer, pero que más me ha gustado desarrollarlo, es el del teléfono. Primero la primera incognita que hay es: ¿Donde me tiene que llevar ese boton?, la respuesta logicamente sería **depende** ya que un factor que va a influir va a ser el de si tenemos el numero de telefono guardado o no para ello realice el siguiente código: 

```kotlin
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
```



# ¿Cómo es la pantalla que hace que marques el número de teléfono?

### A nivel de interfaz:

Encontramos una interfaz muy simple, ya que encontramos un texto el cual dice que podmos configurar el teléfono, podemos escribir el teléfono, acceder a nuestro contactos o simplemente volver al menú principal, para 		ello realice la siguiente interfaz: 

    ![1730904553049](image/Documentación/1730904553049.png)



### A nivel de programación:

En esta parte podemos ver la mayor complición del ejercicio, ya que me plantee y pense, realmente nadie hoy en día se sabe los teléfonos de memoria, asi que por gusto de aprender estuve indagando por internet tanto en la documentacion como en Youtube y pude hacer que pudiese entrar en mis contactos y que encima se guradase en el fichero de preferencias compartidas, el cual lo hice de la siguiente manera (se ha puesto funciones en el código para que sino no se acepta los permisos para acceder a los contactos que no se pueda entrar):

( todo esto se encuentra en la documentación de android : [documentación](https://developer.android.com/identity/providers/contacts-provider/retrieve-names?hl=es-419))

```kotlin
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
```


Luego tambien tenemos que guardar ese telefono que o bien se elija o bien se escriba por lo que lo guardo en un fichreo de referncias compartidas, el cual va a ser una caja donde metamos toda la información y gracias a ella podemos usar los datos guardados cuando se necesiten: 

```kotlin
private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences(getString(R.string.shared_prefernces), Context.MODE_PRIVATE)
    }
```

```xml
    <string name="shared_prefernces">shared_preferences</string>
    <string name="shared_phone_key">shared_phone_key</string>
    <string name="shared_phone">shared_phone</string>
```


Una vez ya tengamos listo el nuemero antes de darle a aceptar tiene que comprobar que este bien el formato ya que sino estuviera bien no se podría llamar a un numero de teléfono con 4 numero o con 5, por lo que hice una expresion regular la cual dice que: puede o no empezar el nuemero por +34, luego le puede seguir numero que empiece por 9, 6 o 7 y que va seguido por 8 números, tambien podemos permitir que haya espacios en blanco. Este código se ha realiazado de la siguiente manera: 

```kotlin
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
```


Por último y más simple es el boton de que vuelva al menú principal el cual se hace de la siguiente manera(**ya no se comentara más como se han realizado los botones para volver a otros activity**):

```kotlin
fun volverCasa() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
```


# ¿Cómo se ha desarrollado para llamar por teléfono?



### A nivel de interfaz:

La interfaz la compone un texto que hasta que no haya un numero en las preferencias compartidas, no va a mostrar el numero elegido, va a ver un botón el cual llame, otro que vuleva para poner otro teléfono y otro que nos lleve al menú principal, esta interfaz se vería de la siguiente manera:

    ![1730906012846](image/Documentación/1730906012846.png)


### A nivel de programación:

Para realizar la llamada veo si tenemos los permisos aceptado en caso de que si se tenga los permisos acepatados, tenemos que ver si tenmos un numero de telefono en las preferencias compartidad en caso afirmativo pues realizare la llama en caso negativo mostrara un mensaje el cual nos indica que no tenemos nigun numero de telefono

```kotlin
 private fun realizarLlamada() {
        val phone = sharedPreferences.getString("shared_phone_key", null)

        if (!phone.isNullOrEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
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
```


# ¿Qué permisos he dicho a la aplicación que necesito?

Todos los permisos se tienen que meter en el AndoridManifest.xml ya que se va a encargar el manidest de pedirlos. tenemos los siguientes permiosos:

```xml
    <!--Permisos para llamar -->
    <uses-feature
        android:name="android.hardware.telephony"
        android:required="true" />
    <uses-permission android:name="android.permission.CALL_PHONE"/>
    <!--Permisos para entrar en internet -->
    <uses-permission android:name="android.permission.INTERNET"/>
    <!--Permiso para la alarma -->
    <uses-permission android:name="com.android.alarm.permission.SET_ALARM" />
    <!--Permisos para entrar en los contactos  -->
    <uses-permission android:name="android.permission.READ_CONTACTS"/>


```
