package com.colinjp.inblo.datasource.network.message

import com.colinjp.inblo.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MessageDto(
    @SerialName("id")
    val id: Int,
    @SerialName("stable_id")
    val stableId: Int,
    @SerialName("sender_id")
    val senderId: Int,
    @SerialName("recipient_id")
    val recipientId: Int? = null,
    @SerialName("horse_name")
    val horseName: String,
    @SerialName("horse_id")
    val horseId: Int? = null,
    @SerialName("title")
    val title: String,
    @SerialName("notification_type")
    val notificationType: String,
    @SerialName("memo")
    val memo: String,
    @SerialName("is_read")
    val isRead: String,
    @SerialName("formatted_time")
    val formattedTime: String,
    @SerialName("formatted_date")
    val formattedDate: String,
    @SerialName("sender")
    val sender: User? = null,
    @SerialName("recipient")
    val recipient: User? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)