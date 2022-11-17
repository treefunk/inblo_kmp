package com.colinjp.inblo.datasource.network.message

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.message.responses.GetMessageListResponse
import com.colinjp.inblo.datasource.network.message.responses.SingleMessageResponse
import io.ktor.client.*
import io.ktor.client.request.*

class MessageServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
): MessageService {
    override suspend fun getAllMessagesByUserId(userId: Int): GetMessageListResponse {
        return httpClient.get {
            url("$baseUrl/user/$userId/messages")
        }
    }

    override suspend fun markMessageAsRead(messageId: Int): GetMessageListResponse {
        return httpClient.get {
            url("$baseUrl/messages/mark-as-read/$messageId")
        }
    }

    override suspend fun sendMessage(
        stableId: Int,
        senderId: Int,
        recipientId: Int?,
        horseId: Int?,
        horseName: String,
        notificationType: String,
        title: String,
        memo: String,
        isRead: String,
        messageId: Int?
    ): SingleMessageResponse {
        if(messageId == null){
            return httpClient.post{
                url("$baseUrl/messages")
                parameter("stable_id",stableId)
                parameter("sender_id",senderId)
                if(recipientId != 0) {
                    parameter("recipient_id",recipientId)
                }
                parameter("horse_id",horseId)
                parameter("horse_name",horseName)
                parameter("notification_type", notificationType)
                parameter("title", title)
                parameter("memo", memo)
                parameter("is_read", isRead)
            }
        }else{
            return httpClient.put{
                url("$baseUrl/messages/$messageId")
                parameter("stable_id",stableId)
                parameter("sender_id",senderId)
                if(recipientId != 0) {
                    parameter("recipient_id",recipientId)
                }
                parameter("horse_id",horseId)
                parameter("horse_name",horseName)
                parameter("notification_type", notificationType)
                parameter("title", title)
                parameter("memo", memo)
                parameter("is_read", isRead)
            }
        }

    }

    override suspend fun removeMessage(messageId: Int): BooleanResponse {
        return httpClient.delete {
            url("$baseUrl/messages/$messageId")
        }
    }

}