package com.istrong.wcedla.base.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "article")
data class Article(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String
) : Parcelable
