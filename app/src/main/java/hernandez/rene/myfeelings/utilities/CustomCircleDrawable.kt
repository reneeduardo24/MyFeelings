package hernandez.rene.myfeelings.utilities

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import hernandez.rene.myfeelings.R

class CustomCircleDrawable: Drawable {
    var coordenadas: RectF? = null
    var anguloBarrido: Float = 0.00F
    var anguloInicio: Float = 0.00F
    var grosorMetrica: Int = 0
    var grosorFondo: Int = 0
    var context: Context? = null
    var emociones = ArrayList<Emociones>()

    constructor(context: Context, emociones: ArrayList<Emociones>){
        this.context = context
        grosorMetrica = context.resources.getDimensionPixelOffset(R.dimen.graphWith)
        grosorFondo = context.resources.getDimensionPixelOffset(R.dimen.graphBackground)
        this.emociones = emociones
    }


    override fun draw(canvas: Canvas) {
        val fondo: Paint = Paint()
        fondo.style = Paint.Style.STROKE
        fondo.strokeWidth = (this.grosorFondo).toFloat()
        fondo.isAntiAlias = true
        fondo.strokeCap = Paint.Cap.ROUND
        fondo.color = context?.resources?.getColor(R.color.gray) ?: R.color.gray

        val ancho: Float = (canvas.width - 25).toFloat()
        val alto: Float = (canvas.height - 25).toFloat()

        coordenadas = RectF(25.0F, 25.0F, ancho, alto)

        canvas.drawArc(coordenadas!!, 0.0f, 360.0f, false, fondo)

        if (emociones.size!= 0){
            for (e in emociones){

                val degree: Float = (e.porcentaje*360)/100
                this.anguloBarrido = degree

                var seccion: Paint = Paint()
                seccion.style = Paint.Style.STROKE
                seccion.strokeWidth = (this.grosorMetrica).toFloat()
                seccion.isAntiAlias = true
                seccion.strokeCap = Paint.Cap.SQUARE
                seccion.color = ContextCompat.getColor(this.context!!, e.color)

                canvas.drawArc(coordenadas!!, this.anguloInicio, this.anguloBarrido, false, seccion)

                this.anguloInicio += this.anguloBarrido
            }
        }


    }

    override fun setAlpha(alpha: Int) {  }

    override fun setColorFilter(colorFilter: ColorFilter?) {  }

    override fun getOpacity(): Int {
     return PixelFormat.OPAQUE
    }


}