package com.colinjp.inblo.domain.model

import com.colinjp.inblo.datasource.network.login.UserDto
import com.colinjp.inblo.util.AndroidParcel
import com.colinjp.inblo.util.AndroidParcelize
import kotlinx.serialization.Serializable


@Serializable
@AndroidParcelize
data class User (
    var id: Int? = null,
    var roleId: Int? = null,
    var stableId: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var username: String? = null,
    var email: String? = null,
    var emailVerifiedAt: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
): AndroidParcel {

    companion object {

        fun createFromDto(
            userDto: UserDto?
        ): User {
            return User (
                id = userDto?.id,
                roleId = userDto?.roleId,
                stableId = userDto?.stableId,
                firstName = userDto?.firstName,
                lastName = userDto?.lastName,
                username = userDto?.username,
                email = userDto?.email,
                emailVerifiedAt = userDto?.emailVerifiedAt,
                createdAt = userDto?.createdAt,
                updatedAt = userDto?.updatedAt
            )
        }

    }
}
