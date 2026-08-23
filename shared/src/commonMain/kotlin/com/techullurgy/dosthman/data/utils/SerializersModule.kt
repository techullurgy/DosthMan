package com.techullurgy.dosthman.data.utils

import com.techullurgy.dosthman.domain.models.AppHttpMethod
import com.techullurgy.dosthman.domain.models.AppHttpResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

@OptIn(ExperimentalSerializationApi::class)
internal fun SerializersModuleBuilder.provideAppHttpMethodSerializersModule() {
    polymorphic(AppHttpMethod::class) {
        subclassesOfSealed(AppHttpMethod.serializer())
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal fun SerializersModuleBuilder.provideAppHttpResponseSerializersModule() {
    polymorphic(AppHttpResponse::class) {
        subclassesOfSealed(AppHttpResponse.serializer())
    }
}