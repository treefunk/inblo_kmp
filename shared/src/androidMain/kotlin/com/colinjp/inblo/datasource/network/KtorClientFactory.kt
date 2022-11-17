package com.colinjp.inblo.datasource.network

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

actual class KtorClientFactory {
    actual fun build(): HttpClient {
        return HttpClient(Android){
            install(JsonFeature){
                serializer = KotlinxSerializer(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging){
                logger = Logger.DEFAULT
                level = LogLevel.ALL

            }
        }
    }
}