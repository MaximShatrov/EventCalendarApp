package com.shatrovmaxim.myapplication.presentation

import com.shatrovmaxim.myapplication.repository.entity.EventEntity

/**
 * Интерфейс обработчика события используемый в EventMapAdapterRecyclerView
 */
interface OpenViewListener {
    /**
     * Открытие view
     */
    fun openView(eventEntity: EventEntity)
}