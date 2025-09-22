package com.elian.integracion_comunitaria_android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
// Ya no necesitamos importar R si no usamos fuentes personalizadas de res/font
// import com.elian.integracion_comunitaria_android.R

// Usaremos FontFamily.Default en lugar de las fuentes personalizadas
// private val Montserrat = FontFamily(
// Font(R.font.montserrat_regular, FontWeight.Normal),
// Font(R.font.montserrat_bold, FontWeight.Bold)
// )

// private val OpenSans = FontFamily(
// Font(R.font.opensans_regular, FontWeight.Normal),
// Font(R.font.opensans_bold, FontWeight.Bold)
// )

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default, // Cambiado a Default
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // Cambiado a Default
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default, // Cambiado a Default
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
    /*
    // Ejemplo de cómo podrías usar otras fuentes genéricas si las necesitas:
    labelSmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)