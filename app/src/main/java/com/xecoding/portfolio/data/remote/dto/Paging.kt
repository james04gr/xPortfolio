package com.xecoding.portfolio.data.remote.dto

data class Paging(
    val current_page: Int,
    val pages_count: Int,
    val total_items: Int
)