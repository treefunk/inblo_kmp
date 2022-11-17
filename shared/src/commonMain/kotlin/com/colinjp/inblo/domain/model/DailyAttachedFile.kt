package com.colinjp.inblo.domain.model

import com.colinjp.inblo.datasource.network.horse_detail.AttachedFileDto
import com.colinjp.inblo.util.AndroidParcel
import com.colinjp.inblo.util.AndroidParcelize
import kotlinx.serialization.SerialName

@AndroidParcelize
class DailyAttachedFile (
    val id: Int,
    val name: String,
    val filePath: String,
    val contentType: String,
    val updatedAt: String?,
    val createdAt: String?
): AndroidParcel {
    companion object {
        fun createFromDto(dto: AttachedFileDto): DailyAttachedFile {
            return DailyAttachedFile(
                id = dto.id,
                name = dto.name,
                filePath = dto.filePath,
                contentType = dto.contentType,
                updatedAt = dto.updatedAt,
                createdAt = dto.createdAt
            )
        }
    }
}