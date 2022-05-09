package hernandez.rene.myfeelings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import hernandez.rene.myfeelings.utilities.CustomBarDrawable
import hernandez.rene.myfeelings.utilities.CustomCircleDrawable
import hernandez.rene.myfeelings.utilities.Emociones
import hernandez.rene.myfeelings.utilities.JSONFile
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    var veryHappy: Float = 0.0F
    var happy: Float = 0.0F
    var neutral: Float = 0.0F
    var sad: Float = 0.0F
    var verySad: Float = 0.0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val graph: ConstraintLayout = findViewById(R.id.graph)
        val graphVeryHappy: View = findViewById(R.id.graphVeryHappy)
        val graphHappy: View = findViewById(R.id.graphHappy)
        val graphNeutral: View = findViewById(R.id.graphNeutral)
        val graphSad: View = findViewById(R.id.graphSad)
        val graphVerySad: View = findViewById(R.id.graphVerySad)

        val btnGuardar: Button = findViewById(R.id.btnGuardar)
        val btnVeryHappy: ImageButton = findViewById(R.id.veryHappy)
        val btnHappy: ImageButton = findViewById(R.id.happy)
        val btnNeutral: ImageButton = findViewById(R.id.neutral)
        val btnSad: ImageButton = findViewById(R.id.sad)
        val btnVerySad: ImageButton = findViewById(R.id.verySad)


        jsonFile = JSONFile()

        fetchingData()
        if (!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)

            graph.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0F, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0F, R.color.orange, happy))
            graphSad.background = CustomBarDrawable(this, Emociones("Neutral", 0.0F, R.color.greenie, neutral))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy Triste", 0.0F, R.color.deepBlue, verySad))
        } else{
            actualizarGrafica()
            iconoMayoria()
        }

        btnGuardar.setOnClickListener {
            guardar()
        }

        btnVeryHappy.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        btnHappy.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        btnNeutral.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        btnSad.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        btnVerySad.setOnClickListener {
            verySad++
            iconoMayoria()
            actualizarGrafica()
        }
    }

    // Nos permite leer el archivo JSON y asignar variables globales
    fun fetchingData(){
        try{
            var json: String = jsonFile?.getData(this) ?: ""

            if (json != ""){
                this.data = true
                var jsonArray: JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                for (i in lista){

                    Log.d("objetos", i.toString())

                    when (i.nombre){
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy Triste" -> verySad = i.total
                    }
                }
            }
            else{
                this.data = false
            }
        } catch (e: JSONException){
            e.printStackTrace()
        }
    }

    // Permite convertir el arreglo JSON en lista de Emociones
    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>{
        var lista = ArrayList<Emociones>()

        for(i in 0..jsonArray.length()-1){
            try{
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                val emocion = Emociones(nombre, porcentaje, color, total)
            }catch (e: JSONException){
                e.printStackTrace()
            }
        }
        return lista
    }

    /* Realiza los cálculos de porcentajes y se llena la lista global
     * Tambien dibuja las gráficas circulares y de barra */
    fun actualizarGrafica(){
        val total = veryHappy + happy + neutral + sad + verySad

        var pVH: Float = (veryHappy * 100 / total).toFloat()
        var pH: Float = (happy * 100 / total).toFloat()
        var pN: Float = (neutral * 100 / total).toFloat()
        var pS: Float = (sad * 100 / total).toFloat()
        var pVS: Float = (verySad * 100 / total).toFloat()

        /*
        Log.d("porcentajes", "very happy " + pVH)
        Log.d("porcentajes", "happy " + pH)
        Log.d("porcentajes", "neutral " + pN)
        Log.d("porcentajes", "sad " + pS)
        Log.d("porcentajes", "very sad " + pVS)
        */

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy Triste", pVS, R.color.deepBlue, verySad))

        val fondo = CustomCircleDrawable(this, lista)

        val graph: ConstraintLayout = findViewById(R.id.graph)
        val graphVeryHappy: View = findViewById(R.id.graphVeryHappy)
        val graphHappy: View = findViewById(R.id.graphHappy)
        val graphNeutral: View = findViewById(R.id.graphNeutral)
        val graphSad: View = findViewById(R.id.graphSad)
        val graphVerySad: View = findViewById(R.id.graphVerySad)

        graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        graphSad.background = CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        graphNeutral.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this, Emociones("Muy Triste", pVS, R.color.deepBlue, verySad))

        graph.background = fondo
    }

    // Cambiamos el ícono que representa al sentimiento con más porcentaje
    fun iconoMayoria(){
        var icon: ImageView = findViewById(R.id.icon)

        if (veryHappy>happy && veryHappy>neutral && veryHappy>sad && veryHappy>verySad){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_veryhappy))
        }

        if (happy>veryHappy && happy>neutral && happy>sad && happy>verySad){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_happy))
        }

        if (neutral>veryHappy && neutral>happy && neutral>sad && neutral>verySad){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_neutral))
        }

        if (sad>veryHappy && sad>happy && sad>neutral && sad>verySad){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sad))
        }

        if (verySad>veryHappy && verySad>happy && verySad>neutral && verySad>sad){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_verysad))
        }
    }

    /* Obtenermos valores existentes y los convertimos en un arreglo JSON
     * para posteriormente, pasarlos a la instancia donde se almacena
     * el archivo JSON */

    fun guardar(){
        var jsonArray = JSONArray()
        var o : Int = 0

        try {
            for (i in lista) {
                Log.d("objetos", i.toString())
                var j: JSONObject = JSONObject()
                j.put("nombre", i.nombre)
                j.put("porcentaje", i.porcentaje)
                j.put("color", i.color)
                j.put("total", i.total)

                jsonArray.put(o, j)
                //Log.d("json", jsonArray[o].toString())

                o++
            }

            jsonFile?.saveData(this, jsonArray.toString())
            //Log.d("json", jsonArray.toString())

            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
        }catch (e: JSONException){
            e.printStackTrace()
            Toast.makeText(this, "No se guardo", Toast.LENGTH_SHORT).show()
        }
    }
}