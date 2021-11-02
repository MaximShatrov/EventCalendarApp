package com.shatrovmaxim.myapplication.presentation

import com.shatrovmaxim.myapplication.repository.entity.EventEntity

interface OpenViewListener {

    fun openView(eventEntity: EventEntity)
}