import io.github.aakira.napier.Napier
import kotlin.time.*
import kotlin.time.Duration.Companion.seconds

data class QrKey(val key: String, var generatedAt: TimeMark, var valid: Boolean = true) {
    fun isStillValid(): Boolean {
        Napier.d("TEST : TEMPO TRASCORSO DALLA GENERAZIONE ----> $generatedAt.")
        return generatedAt.elapsedNow() < 20.seconds
    }
}