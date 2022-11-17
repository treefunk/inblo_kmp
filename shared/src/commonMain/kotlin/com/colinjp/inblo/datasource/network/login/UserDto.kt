package com.colinjp.inblo.datasource.network.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    @SerialName("id")
    var id: Int? = null,

    @SerialName("role_id")
    var roleId: Int? = null,

    @SerialName("stable_id")
    var stableId: Int? = null,

    @SerialName("first_name")
    var firstName: String? = null,

    @SerialName("last_name")
    var lastName: String? = null,

    @SerialName("username")
    var username: String? = null,

    @SerialName("login_name")
    var loginName: String? = null,

    @SerialName("email")
    var email: String? = null,

    @SerialName("email_verified_at")
    var emailVerifiedAt: String? = null,

    @SerialName("created_at")
    var createdAt: String? = null,

    @SerialName("updated_at")
    var updatedAt: String? = null,
)

