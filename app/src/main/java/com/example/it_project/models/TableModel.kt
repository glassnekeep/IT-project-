package com.example.it_project.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TableModel(val name: String = "", val type: String = "", val correctAnswer: String = "", var chosenAnswer: String = "", var isCorrect: String = ""): Parcelable {
}