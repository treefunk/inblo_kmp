package com.colinjp.inblo.datasource.network.horse_detail

import com.colinjp.inblo.util.AndroidParcel
import com.colinjp.inblo.util.AndroidParcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@AndroidParcelize
class AttachedFileDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("file_path")
    val filePath: String,
    @SerialName("content_type")
    val contentType: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("created_at")
    val createdAt: String
): AndroidParcel