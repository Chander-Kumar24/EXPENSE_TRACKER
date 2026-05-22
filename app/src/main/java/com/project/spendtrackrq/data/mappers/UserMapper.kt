package com.project.spendtrackrq.data.mappers

import com.project.spendtrackrq.data.local.dto.UserDTO
import com.project.spendtrackrq.data.local.entities.user.UserEntity

fun UserEntity.toDTO(): UserDTO {
    return UserDTO(
        id = id,
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        termsAccepted = termsAccepted
    )
}

fun UserDTO.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        termsAccepted = termsAccepted
    )
}
