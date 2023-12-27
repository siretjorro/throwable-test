package mobi.lab.throwabletest.infrastructure.common.json

import com.squareup.moshi.JsonAdapter
import se.ansman.kotshi.KotshiJsonAdapterFactory

@KotshiJsonAdapterFactory
object ApplicationJsonAdapterFactory : JsonAdapter.Factory by KotshiApplicationJsonAdapterFactory
