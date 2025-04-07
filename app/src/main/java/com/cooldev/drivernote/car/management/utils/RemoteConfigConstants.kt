package com.dhug.example.utils

import com.dhug.example.data.local.entities.RemoteConfig
import javax.inject.Singleton

@Singleton
object RemoteConfigConstants {
    /**
     * Cho phép cấu hình nhập thông tin phần mô tả tại màn hình Paywall, trong đó sẽ có một số thông tin về gói sẽ được gán value
     *
     * Chi tiết tại Sheet Paywall Description
     */
    const val DIRECT_STORE_DESCRIPTION = "direct_store_description"

    /**
     * Key cho phép hiển thị gói Yearly cùng với 2 gói còn lại (monthly/ lifetime) tại màn hình Paywall, nếu key = true thì hiển thị gói Yearly, nếu key = false thì hiển thị gói Weekly
     *
     * Lưu ý: Với paywall có offer free-trial, khi thực hiện config gói yearly và weekly thì vẫn theo logic hiển thị cả offer trial của mỗi gói
     */
    const val IS_SHOW_YEARLY_PACKAGE = "is_show_yearly_package"

    /**
     * Key cho phép hiển thị inter ads trong quá trình khách hàng sử dụng app (không bao gồm open ads)
     *
     * Nếu key = true, hiển thị inter ads trong khi truy cập vào app
     *
     * Nếu key = false, không hiển thị inter ads trong khi truy cập app
     */
    const val IS_SHOW_ADS_INTERSTITIAL = "is_show_ads_interstitial"

    /**
     * Key cho phép hiển thị banner ads trong quá trình khách hàng sử dụng app
     *
     * Nếu key = true, hiển thị banner ads trong khi truy cập vào app
     *
     * Nếu key = false, không hiển thị banner ads trong khi truy cập app
     */
    const val IS_SHOW_ADS_BANNER = "is_show_ads_banner"

    /**
     * Key cho phép hiển thị banner ads trong quá trình khách hàng vào flow màn hình Onboarding
     *
     * Nếu key = true, hiển thị banner ads trong khi truy cập vào app
     *
     * Nếu key = false, không hiển thị banner ads trong khi truy cập app
     */
    const val IS_SHOW_ADS_BANNER_ONBOARDING = "is_show_ads_banner_onboarding"

    /**
     * Key cho phép on/off rating dialog tại màn hình Onboarding
     *
     * Nếu key = true, hiển thị rating dialog
     * Nếu key= false, không hiển thị rating dialog
     */
    const val IS_ONBOARDING_RATING_DIALOG = "is_onboarding_rating_dialog"


    /**
     * Biến cho phép config UI Paywall, trong đó: 0 = Default, 1: Promote Yearly Non-trial, 2: Promote Weekly Non-trial
     */
    const val IS_VERSION_PAYWALL = "is_version_paywall"

    /**
     * Khoảng thời gian tối thiểu tính bằng giây cần thiết giữa các lần hiển thị quảng cáo; quảng cáo sẽ không được hiển thị cho đến khi khoảng thời gian này trôi qua
     */
    const val INTERSTITIAL_INTERVAL = "interstitial_interval"

    /**
     * Số lượng tối đa quảng cáo xen kẽ có thể hiển thị trong một khoảng thời gian cụ thể khi hiển thị tất cả quảng cáo inter ads
     */
    const val MAX_INTER_ADS_COUNT = "max_inter_ads_count"

    /**
     * Key cho phép hiển thị open ads khi mở app
     */
    const val IS_SHOW_SPLASH = "is_show_splash"

    /**
     * Key giới hạn số lượng tính năng được add vào (tính tổng phần ở button dấu + bao gồm reminder/ maintenace/ expense/ service/ income)
     */
    const val IS_LIMIT_ADD_NOTE = "is_limit_add_note"

    /**
     * Key khoá tính năng Report tại màn hình Home. Nếu key = true, hiển thị paywall và ngược lại
     */
    const val IS_LOCK_FEATURE_REPORT = "is_lock_feature_report"


    const val IS_LIMIT_ADD_CAR = "is_limit_add_car"

    const val IS_API_RATING_INAPP = "is_api_rating_inapp"


    fun getRemoteConfigByHashmap(configs: Map<String, Any>): RemoteConfig {
        return RemoteConfig(
            directStoreDescription = configs[DIRECT_STORE_DESCRIPTION] as? String ?: "",
            isShowYearlyPackage = configs[IS_SHOW_YEARLY_PACKAGE] as? Boolean ?: false,
            isShowAdsInterstitial = configs[IS_SHOW_ADS_INTERSTITIAL] as? Boolean ?: false,
            isShowAdsBanner = configs[IS_SHOW_ADS_BANNER] as? Boolean ?: false,
            isShowAdsBannerOnboarding = configs[IS_SHOW_ADS_BANNER_ONBOARDING] as? Boolean ?: false,
            isOnboardingRatingDialog = configs[IS_ONBOARDING_RATING_DIALOG] as? Boolean ?: false,
            isVersionPaywall = configs[IS_VERSION_PAYWALL] as? Long ?: 0L,
            interstitialInterval = configs[INTERSTITIAL_INTERVAL] as? Long ?: 0L,
            maxInterAdsCount = configs[MAX_INTER_ADS_COUNT] as? Long ?: 0L,
            isShowSplash = configs[IS_SHOW_SPLASH] as? Boolean ?: false,

            isLimitAddCar = configs[IS_LIMIT_ADD_CAR] as? Long ?: 1,
            isLimitAddNote = configs[IS_LIMIT_ADD_NOTE] as? Long ?: 4,
            isLockFeatureReport = configs[IS_LOCK_FEATURE_REPORT] as? Boolean ?: false,
            isApiRatingInapp = configs[IS_API_RATING_INAPP] as? String ?: AppConstants.ONBOARDING,
        )
    }
}