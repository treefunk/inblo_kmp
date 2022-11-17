package com.colinjp.inblo.domain.model

import com.colinjp.inblo.datasource.network.message.MessageDto
import com.colinjp.inblo.util.AndroidParcel
import com.colinjp.inblo.util.AndroidParcelize
import kotlinx.serialization.SerialName


@AndroidParcelize
class Message(
    val id: Int,
    val stableId: Int,
    val senderId: Int,
    val recipientId: Int?,
    val horseName: String,
    val horseId: Int? = null,
    val title: String,
    val notificationType: String,
    val memo: String,
    val isRead: String,
    val formattedTime: String,
    val formattedDate: String,
    val sender: User?,
    val recipient: User?,
    val createdAt: String? = null,
    val updatedAt: String? = null
): AndroidParcel {
    companion object {
        fun createFromDto(messageDto: MessageDto): Message {
            return Message(
                id = messageDto.id,
                stableId = messageDto.stableId,
                senderId = messageDto.senderId,
                recipientId = messageDto.recipientId,
                horseName = messageDto.horseName,
                horseId = messageDto.horseId,
                title = messageDto.title,
                notificationType = messageDto.notificationType,
                memo = messageDto.memo,
                isRead = messageDto.isRead,
                formattedTime = messageDto.formattedTime,
                formattedDate = messageDto.formattedDate,
                sender = messageDto.sender,
                recipient = messageDto.recipient,
                createdAt = messageDto.createdAt,
                updatedAt = messageDto.updatedAt
            )
        }
    }
}