package com.shatrovmaxim.myapplication.repository.exceptions

/**
 * Exception при отсутствии EventEntity в репозитории
 */
class EventNotFoundException(message: String?) : Exception(message)