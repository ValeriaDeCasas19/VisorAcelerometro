package com.example.practica_acelerometro

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorManager: SensorManager
    lateinit var Img: ImageView
    lateinit var switch: Switch
    lateinit var sig: ImageButton
    lateinit var ant: ImageButton

    val imagenesRecursos = arrayOf(
        R.mipmap.arroz,  // Replace with actual image names
        R.mipmap.burrito,
        R.mipmap.caja,
        R.mipmap.cuernito,
        R.mipmap.cupcake,
        R.mipmap.curry,
        R.mipmap.hamburguesa,
        R.mipmap.helado,
        R.mipmap.holla,
        R.mipmap.masa,
        R.mipmap.natilla,
        R.mipmap.palomitas,
        R.mipmap.pescado,
        R.mipmap.pretzel,
        R.mipmap.wafle
    )

    val imagenesNombres = arrayOf(
        "Arroz",  // Replace with actual image names
        "Burrito",
        "Caja",
        "Cuernito",
        "Cupcake",
        "Curry",
        "Hamburguesa",
        "Helado",
        "Holla",
        "Masa",
        "Natilla",
        "Palomitas",
        "Pescado",
        "Pretzel",
        "Wafle"
    )

    var currentIndex = 0
    var lastX: Float = 0f
    var threshold: Float = 2f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        Img = findViewById(R.id.Imagen1)
        switch = findViewById(R.id.switch1)
        sig = findViewById(R.id.siguiente)
        ant = findViewById(R.id. anterior)

        Img.setImageResource(imagenesRecursos[currentIndex])

        sig.setOnClickListener {
            currentIndex = (currentIndex + 1) % imagenesRecursos.size
            Img.setImageResource(imagenesRecursos[currentIndex])
        }

        ant.setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) imagenesRecursos.size - 1 else currentIndex - 1
            Img.setImageResource(imagenesRecursos[currentIndex])
        }
        Img.setOnClickListener {
            showImageSelectionDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && switch.isChecked) {
            val x = event.values[0]
            if (lastX == 0f) {
                lastX = x
            }
            val deltaX = lastX - x

            if (deltaX > threshold) {
                // Moved to the right
                currentIndex = (currentIndex + 1) % imagenesRecursos.size
                Img.setImageResource(imagenesRecursos[currentIndex])
                lastX = x
            } else if (deltaX < -threshold) {
                // Moved to the left
                currentIndex = if (currentIndex - 1 < 0) imagenesRecursos.size - 1 else currentIndex - 1
                Img.setImageResource(imagenesRecursos[currentIndex])
                lastX = x
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun showImageSelectionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar imagen")
        builder.setItems(imagenesNombres) { _, which ->
            currentIndex = which
            Img.setImageResource(imagenesRecursos[currentIndex])
        }
        builder.create().show()
    }
}