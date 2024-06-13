package api.mappers

import api.ErrorResponseDto
import domain.models.ErrorResponse

fun ErrorResponseDto.toDomain(): ErrorResponse {
    return ErrorResponse(
        success = this.success,
        statusCode = this.statusCode,
        statusMessage = this.statusMessage
    )
}
