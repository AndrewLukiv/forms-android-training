package com.example.formsandroidtraining.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.formsandroidtraining.Form

@Entity(tableName = "forms")
data class LocalForm(
    @PrimaryKey val id: String,
    val name: String
)

fun LocalForm.toExternal() =
    Form(
        id = id,
        name = name
    )
