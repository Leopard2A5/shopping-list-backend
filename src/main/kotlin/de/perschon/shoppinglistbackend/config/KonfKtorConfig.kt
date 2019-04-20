package de.perschon.shoppinglistbackend.config

import com.uchuhimo.konf.Config
import io.ktor.config.ApplicationConfig
import io.ktor.config.ApplicationConfigValue

class KonfKtorConfig(
    private val config: Config,
    private val prefix: String? = null
) : ApplicationConfig {

    override fun config(path: String): ApplicationConfig {
        return KonfKtorConfig(config = config, prefix = path)
    }

    override fun configList(path: String): List<ApplicationConfig> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun property(path: String): ApplicationConfigValue {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun propertyOrNull(path: String): ApplicationConfigValue? {
        val fullPath = buildPath(path)
        return when (config.contains(fullPath)) {
            true -> KonfApplicationConfigValue(value = config[buildPath(path)])
            else -> null
        }
    }

    private fun buildPath(path: String): String {
        return when (prefix) {
            null -> path
            else -> "$prefix.$path"
        }
    }

}

class KonfApplicationConfigValue(
    val value: Any
) : ApplicationConfigValue {
    override fun getList(): List<String> {
        return when (value) {
            is String -> listOf(value)
            is Array<*> -> value.map { it.toString() }
            else -> TODO("not implemented!")
        }
    }

    override fun getString(): String {
        return value.toString()
    }
}
