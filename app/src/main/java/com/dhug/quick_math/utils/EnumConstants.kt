package com.dhug.quick_math.utils

import javax.inject.Singleton

@Singleton
object EnumConstants {
    enum class MediaType {
        VIDEO,
        IMAGE
    }

    enum class LinkedEntityType {
        REMINDER, MAINTENANCE, REFUEL, INCOME, EXPENSES, SERVICES, ROUTE
    }

    enum class TriggerBy{
        PERSONAL,
        WORK
    }
}