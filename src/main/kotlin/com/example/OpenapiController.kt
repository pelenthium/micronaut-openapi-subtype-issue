package com.example

import com.example.BaseResponse.Companion.ACTION_REQUIRED
import com.example.BaseResponse.Companion.ERROR_RESPONSE
import com.example.BaseResponse.Companion.SUCCESS_RESPONSE
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.serde.annotation.Serdeable
import java.util.*

@Controller("/openapi")
class OpenapiController {

    @Get(uri = "/", produces = ["text/plain"])
    fun index(): BaseResponse {
        return SuccessResponse(
            "ok2", "ok1"
        )
    }
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "id",
)
@JsonSubTypes(
    JsonSubTypes.Type(
        value = ErrorResponse::class, name = ERROR_RESPONSE
    ),
    JsonSubTypes.Type(
        value = SuccessResponse::class, name = SUCCESS_RESPONSE
    ),
    JsonSubTypes.Type(
        value = RequiredActionsResponse::class, name = ACTION_REQUIRED
    )
)
@Serdeable
open class BaseResponse(
    val id: String
) {
    companion object {
        const val ERROR_RESPONSE = "error"
        const val SUCCESS_RESPONSE = "success"
        const val ACTION_REQUIRED = "action_required"
    }
}

@Introspected
@Serdeable
data class SuccessResponse(
    val accessToken: String,
    val refreshToken: String
) : BaseResponse(id = SUCCESS_RESPONSE)

@Introspected
@Serdeable
data class ErrorResponse(
    val code: ErrorCode
) : BaseResponse(id = ERROR_RESPONSE)

@Introspected
@Serdeable
data class RequiredActionsResponse(
    val sessionId: UUID,
    val actions: List<String>
) : BaseResponse(id = ACTION_REQUIRED)

@Introspected
@Serdeable
enum class ErrorCode {
    OTHER,
}