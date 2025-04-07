package com.dhug.example.data.local.entities

import com.dhug.example.utils.AppConstants

/**
 * Key remote config
 *
 * @Author: NGUYEN XUAN DIEU
 * @Date: 21 / 12 / 2024
 */
data class RemoteConfig(
    /**
     * Cho phép cấu hình nhập thông tin phần mô tả tại màn hình Paywall, trong đó sẽ có một số thông tin về gói sẽ được gán value
     *
     * Chi tiết tại Sheet Paywall Description
     */
    var directStoreDescription: String = "",
    /**
     * Key cho phép hiển thị gói Yearly cùng với 2 gói còn lại (monthly/ lifetime) tại màn hình Paywall, nếu key = true thì hiển thị gói Yearly, nếu key = false thì hiển thị gói Weekly
     *
     * Lưu ý: Với paywall có offer free-trial, khi thực hiện config gói yearly và weekly thì vẫn theo logic hiển thị cả offer trial của mỗi gói
     */
    var isShowYearlyPackage: Boolean = false,
    /**
     * Key cho phép hiển thị inter ads trong quá trình khách hàng sử dụng app (không bao gồm open ads)
     *
     * Nếu key = true, hiển thị inter ads trong khi truy cập vào app
     *
     * Nếu key = false, không hiển thị inter ads trong khi truy cập app
     */
    var isShowAdsInterstitial: Boolean = false,
    /**
     * Key cho phép hiển thị banner ads trong quá trình khách hàng sử dụng app
     *
     * Nếu key = true, hiển thị banner ads trong khi truy cập vào app
     *
     * Nếu key = false, không hiển thị banner ads trong khi truy cập app
     */
    var isShowAdsBanner: Boolean = false,
    /**
     * Key cho phép hiển thị banner ads trong quá trình khách hàng vào flow màn hình Onboarding
     *
     * Nếu key = true, hiển thị banner ads trong khi truy cập vào app
     *
     * Nếu key = false, không hiển thị banner ads trong khi truy cập app
     */
    var isShowAdsBannerOnboarding: Boolean = false,
    /**
     * Nếu = false thì hiển thị UI khoá, khách hàng chỉ được xem các hình ảnh ở hàng đầu tiên, khách hàng phải mua hàng để xem hình ảnh full
     *
     */
    var isOnboardingRatingDialog: Boolean = false,
    /**
     * Key hiển thị biến khoá hình ảnh tại màn hình library (Tại các tính năng ở màn hình Library như Media, photos & Lock)
     *
     * Nếu = true thì cho phép khách hàng xem hết các ảnh thông thường
     *
     * Nếu = false thì hiển thị UI khoá, khách hàng chỉ được xem các hình ảnh ở hàng đầu tiên, khách hàng phải mua hàng để xem hình ảnh full
     */
    var isFreeLibrary: Boolean = false,
    /**
     * Key hiển thị biến khoá bắt buộc khách hàng phải mua hàng mới được sử dụng tính năng Lock tại màn hình Library
     *
     * Nếu = true thì khách hàng không cần mua hàng để sử dụng tính năng, khách hàng click vào button sử dụng bình thường
     *
     * Nếu = false thì hiển thị UI Paywall khi khách hàng click vào button Lock tại màn hình Library
     */
    var isFreeLibraryLock: Boolean = false,
    /**
     * 1. Một số lưu ý:
     * - Chỉ áp dụng cho các user chưa là Premium, các user là Premium thì fix 1 tham số cố định như trong docs
     * - Áp dụng cho cả alert khi sử dụng app và khi để app chạy ngầm
     * - Tham số dạng number sẽ được tính theo % dựa theo settings của khách hàng tại màn hình Settings
     *
     * 2. Chi tiết:
     * - Alert xuất hiện khi = tham số config (number)%
     * Công thức ra tham số config (number)% = dung lượng quay video thực tế/ dung lượng video user đã setting *100%
     *
     */
    var percentRemainVideoSize: Long = 0L,

    /**
     * 1. Một số lưu ý:
     * - Chỉ áp dụng cho các user chưa là Premium, các user là Premium thì fix 1 tham số cố định như trong docs
     * - Áp dụng cho cả alert khi sử dụng app và khi để app chạy ngầm
     * - Tham số dạng number sẽ được tính theo % dựa theo settings của khách hàng tại màn hình Settings
     *
     * 2. Chi tiết:
     * - Alert xuất hiện khi = tham số config (number)%
     * Công thức ra tham số config (number)% = độ dài video thực tế/ độ dài video user đã setting *100%
     *
     */
    var percentRemainVideoDuration: Long = 0L,
    /**
     * Biến cho phép config UI Paywall, trong đó: 0 = Default, 1: Promote Yearly Non-trial, 2: Promote Weekly Non-trial
     */
    var isVersionPaywall: Long = 0L,
    /**
     * Khoảng thời gian tối thiểu tính bằng giây cần thiết giữa các lần hiển thị quảng cáo; quảng cáo sẽ không được hiển thị cho đến khi khoảng thời gian này trôi qua
     */
    var interstitialInterval: Long = 0L,
    /**
     * Số lượng tối đa quảng cáo xen kẽ có thể hiển thị trong một khoảng thời gian cụ thể khi hiển thị tất cả quảng cáo inter ads
     */
    var maxInterAdsCount: Long = 0L,
    /**
     * Key cho phép hiển thị open ads khi mở app
     */
    var isShowSplash: Boolean = false,

    /**
     * Key giới hạn số lượng tính năng được add vào (tính tổng phần ở button dấu + bao gồm reminder/ maintenace/ expense/ service/ income)
     */
    var isLimitAddNote: Long = 4,

    /**
     * Key khoá tính năng Report tại màn hình Home. Nếu key = true, hiển thị paywall và ngược lại
     */
    var isLockFeatureReport: Boolean = false,

    var isLimitAddCar: Long = 1,

    /**
     * Nếu key = onboarding=> Hiển thị rating tại dialog Rating.
     * Nếu key= add_car => Hiển thị api rating sau khi user add xe thành công đầu tiên.
     * Nếu key = home, hiển thị api rating khi truy cập màn hình Home lần đầu tiên
     * (Lưu ý: value này sẽ được sử dụng 1 trong 3 chứ không có tính chất lần lượt)
     */

    var isApiRatingInapp: String = AppConstants.ONBOARDING
)