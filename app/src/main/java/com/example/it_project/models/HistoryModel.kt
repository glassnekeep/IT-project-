package com.example.it_project.models

data class HistoryModel(val testName: String, val privacy: String, val testCreator: String, val subject: String, val total: TotalModel, val tableList: ArrayList<TableModel>) {
}