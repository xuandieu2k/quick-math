package com.dhug.quick_math.utils

import javax.inject.Singleton

@Singleton
object
AppConstants {

    enum class PaymentType {
        FREE_TRIAL,
        MONTHLY,
        WEEKLY,
        YEARLY,
        LIFETIME
    }

    enum class FragmentAddGasStationType {
        DEFAULT, // Add Gas Station
        MAP, // Add Gas Station and map
        LOCATION // Add Location and map
    }

    enum class TrackingMap {
        PERSONAL,
        WORK
    }

    enum class FilterType{
        TODAY,
        THIS_WEEK,
        THIS_MONTH,
        THIS_YEAR,
    }

    // Request code
    const val PAYMENT_METHOD_REQUEST_CODE = 1000
    const val GAS_STATION_REQUEST_CODE = 1001
    const val FUEL_TYPE_REQUEST_CODE = 1002
    const val EXPENSES_TYPE_REQUEST_CODE = 1003
    const val SERVICE_TYPE_REQUEST_CODE = 1004
    const val VEHICLE_TYPE_REQUEST_CODE = 1005
    const val MANUFACTURER_REQUEST_CODE = 1006
    const val VEHICLE_REQUEST_CODE = 1007
    const val LOCATION_TYPE_REQUEST_CODE = 1008
    const val LOCATION_RELATIONS_REQUEST_CODE = 10010
    const val INCOME_TYPE_REQUEST_CODE = 10011
    const val ROUTE_SETTING_REQUEST_CODE = 10012

    //
    const val IS_OPEN_A_PART = "IS_OPEN_A_PART" // open a part media

    // Activity Result
    const val IS_BACK_FROM_PAYWALL = "IS_BACK_FROM_PAYWALL"
    const val BY_PASS_PAYWALL = "BY_PASS_PAYWALL"


    const val PAYMENT_METHOD_OBJECT = "PAYMENT_METHOD_OBJECT"
    const val FUEL_TYPE_OBJECT = "FUEL_TYPE_OBJECT"
    const val GAS_STATION_OBJECT = "GAS_STATION_OBJECT"
    const val EXPENSES_TYPE_OBJECT = "EXPENSES_TYPE_OBJECT"
    const val SERVICE_TYPE_OBJECT = "SERVICE_TYPE_OBJECT"
    const val INCOME_TYPE_OBJECT = "INCOME_TYPE_OBJECT"
    const val VEHICLE_TYPE_OBJECT = "VEHICLE_TYPE_OBJECT"
    const val MANUFACTURER_OBJECT = "MANUFACTURER_OBJECT"
    const val VEHICLE_OBJECT = "VEHICLE_OBJECT"
    const val LOCATION_TYPE_OBJECT = "LOCATION_TYPE_OBJECT"
    const val LOCATION_RELATIONS_OBJECT = "LOCATION_RELATIONS_OBJECT"
    const val ROUTE_RELATIONS_OBJECT = "ROUTE_RELATIONS_OBJECT"
    const val TYPE_TRACKING_MAP_SETTING = "TYPE_TRACKING_MAP_SETTING"

    // SCREEN KEY
    const val TICKET_FULL_INFOR_OBJECT = "TICKET_FULL_INFOR_OBJECT"
    const val CODE_BACK_REMINDER = 3000
    const val CODE_BACK_MAINTENANCE = 3001
    const val CODE_BACK_REFUEL = 3002
    const val CODE_BACK_EXPENSES = 3003
    const val CODE_BACK_SERVICES = 3004
    const val CODE_BACK_INCOME = 3005
    const val CODE_BACK_ROUTE = 3006



    // KEY RATING API
    const val ONBOARDING = "onboarding"
    const val ADD_CAR = "add_car"
    const val HOME = "home"


    const val LOCATION_TYPE_ID_GAS_STATION = 5L
    const val LOCATION_TYPE_ID_NORMAL = 11L


    /**
     * NOTIFICATION CHANEL
     */
    const val CHANEL_ID_NOTIFICATION_REMINDER = "CHANEL_ID_NOTIFICATION_REMINDER_2001"
    const val CHANEL_NAME_NOTIFICATION_REMINDER = "CHANEL_NAME_NOTIFICATION_REMINDER"

}