package com.example.huerto.ui.product

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.huerto.R
import java.text.Normalizer

@Composable
fun ProductImage(name: String, size: Dp = 72.dp) {
    val context = LocalContext.current
    val resId = productImageRes(context, name)

    Card(
        modifier = Modifier.size(size),
        shape = MaterialTheme.shapes.medium
    ) {
        val painter = painterResource(id = resId)
        Image(
            painter = painter,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Retorna un drawable existente, o el placeholder si no se encuentra.
 */
@DrawableRes
fun productImageRes(context: android.content.Context, name: String): Int {
    // Normaliza el nombre (minúsculas, sin acentos)
    val raw = name.trim().lowercase()
    val n = Normalizer.normalize(raw, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

    // Usa el nombre base esperado del drawable
    val guess = when {
        "espinaca" in n -> "espinaca"
        "platano" in n -> "platano"
        "zanahoria" in n -> "zanahoria"
        "piment" in n -> "pimenton"
        "quinua" in n -> "quinua"
        else -> "placeholder"
    }

    // Busca el ID del recurso; si no existe, usa el placeholder
    val resId = context.resources.getIdentifier(guess, "drawable", context.packageName)
    return if (resId != 0) resId else R.drawable.placeholder
}

@DrawableRes
fun productImageRes(name: String): Int {
    val raw = name.orEmpty().trim().lowercase()
    val n = Normalizer.normalize(raw, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

    return when {
        "espinaca" in n   -> R.drawable.espinaca
        "platano" in n    -> R.drawable.platano
        "zanahoria" in n  -> R.drawable.zanahoria
        "piment" in n     -> R.drawable.pimenton
        "quinua" in n     -> R.drawable.quinua
        else              -> R.drawable.placeholder
    }
}