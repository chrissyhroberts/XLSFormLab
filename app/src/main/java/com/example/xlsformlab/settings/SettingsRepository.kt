package com.example.xlsformlab.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.capabilitySettingsDataStore by preferencesDataStore(
    name = "capability_settings"
)

object SettingsRepository {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    private var appContext: Context? = null

    fun initialise(context: Context) {
        appContext = context.applicationContext
    }

    suspend fun load(
        capabilityId: String,
        settings: List<CapabilitySetting>
    ): Map<String, Any> {
        val context = appContext ?: return emptyMap()

        return withContext(Dispatchers.IO) {
            val preferences = context.capabilitySettingsDataStore.data.first()

            settings.mapNotNull { setting ->
                val key = stringPreferencesKey(keyFor(capabilityId, setting.id))
                val storedValue = preferences[key] ?: return@mapNotNull null
                parseStoredValue(setting, storedValue)?.let { parsed ->
                    setting.id to parsed
                }
            }.toMap()
        }
    }

    fun save(
        capabilityId: String,
        settingId: String,
        value: Any
    ) {
        val context = appContext ?: return
        val key = stringPreferencesKey(keyFor(capabilityId, settingId))
        val storedValue = value.toString()

        scope.launch {
            withContext(Dispatchers.IO) {
                context.capabilitySettingsDataStore.edit { preferences ->
                    preferences[key] = storedValue
                }
            }
        }
    }

    private fun keyFor(
        capabilityId: String,
        settingId: String
    ): String = "$capabilityId.$settingId"

    private fun parseStoredValue(
        setting: CapabilitySetting,
        storedValue: String
    ): Any? {
        return when (setting) {
            is CapabilitySetting.BooleanSetting -> storedValue.toBooleanStrictOrNull()
            is CapabilitySetting.IntSetting -> storedValue.toIntOrNull()?.coerceIn(
                setting.minimum ?: Int.MIN_VALUE,
                setting.maximum ?: Int.MAX_VALUE
            )
            is CapabilitySetting.FloatSetting -> storedValue.toFloatOrNull()?.coerceIn(
                setting.minimum ?: -Float.MAX_VALUE,
                setting.maximum ?: Float.MAX_VALUE
            )
            is CapabilitySetting.TextSetting -> storedValue
            is CapabilitySetting.ChoiceSetting -> {
                if (storedValue in setting.choices) storedValue else setting.defaultValue
            }
        }
    }
}
