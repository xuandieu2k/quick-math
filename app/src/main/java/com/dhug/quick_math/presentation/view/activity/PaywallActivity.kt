package com.dhug.quick_math.presentation.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.cooldev.base.BaseConstants
import com.dhug.quick_math.R
import dagger.hilt.android.AndroidEntryPoint
import com.dhug.quick_math.base.AppActivity
import com.dhug.quick_math.base.wiget.AppToast
import com.dhug.quick_math.data.local.entities.PaywallItem
import com.dhug.quick_math.data.local.entities.PurchaseResult
import com.dhug.quick_math.databinding.ActivityPaywallBinding
import com.dhug.quick_math.presentation.adapter.PaywallAdapter
import com.dhug.quick_math.presentation.viewmodel.PaywallViewModel
import com.dhug.quick_math.utils.AppConstants
import com.dhug.quick_math.utils.AppUtils
import com.dhug.quick_math.utils.AppUtils.hide
import com.dhug.quick_math.utils.AppUtils.show
import com.dhug.quick_math.utils.BillingUtils
import com.dhug.quick_math.utils.MMKVUtils
import com.dhug.quick_math.utils.MoneyUtils
import com.dhug.quick_math.utils.PayWallConstants
import com.dhug.quick_math.utils.Resource
import com.dhug.quick_math.utils.SecurityUtils
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.apply
import kotlin.collections.filter
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.toMutableList
import kotlin.jvm.java
import kotlin.let
import kotlin.text.contains
import kotlin.text.ifEmpty
import kotlin.text.replace
import kotlin.text.substring
import kotlin.text.toInt
import kotlin.toBigDecimal


@AndroidEntryPoint
class PaywallActivity : AppActivity(), PaywallAdapter.OnClickItem {
    private val binding: ActivityPaywallBinding by lazy {
        ActivityPaywallBinding.inflate(
            layoutInflater
        )
    }

    private lateinit var paywallAdapter: PaywallAdapter
    private var productDetails: ProductDetails? = null

    private val viewModel: PaywallViewModel by viewModels()
    override fun isHasInterstitialAd(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseConstants.isInterstitialAdShowing = true
        remoteConfigViewModel.configData.removeObservers(this)
        setUpBackPressAndResultOnBack()
    }

    override fun onDestroy() {
        BaseConstants.isInterstitialAdShowing = false
        setResult(RESULT_OK, Intent().apply {
            putExtra(AppConstants.IS_BACK_FROM_PAYWALL, true)
        })
        super.onDestroy()
    }

    override fun getLayoutView(): View = binding.root
    override fun initView() {
        binding.btnClose.hide()
        setUpView()
        setUpRecycleView()
        setUpShowButtonClose()
    }

    private fun setUpShowButtonClose() {
        lifecycleScope.launch {
            delay(3000L)
            binding.btnClose.show()
        }
    }

    private fun setUpRecycleView() {
        paywallAdapter = PaywallAdapter(this)
        paywallAdapter.setData(viewModel.getDataPaywall())
        paywallAdapter.setListener(this)
        AppUtils.initRecyclerViewVertical(binding.rvPaywall, adapter = paywallAdapter, 1)
    }

    private fun setUpView() {
        setUpShimmer(true)
        setOnClickListener(
            binding.btnClose
        )

        binding.tvDescription.text = convertStoreDescription()
    }

    private fun setUpShimmer(turnOn: Boolean) {
        if (turnOn) {
            binding.layoutShimmerPaywall.shimmer.startShimmer()
            binding.layoutShimmerPaywall.root.show()
            binding.rvPaywall.hide()
            binding.tvSubContentBottom.hide()
        } else {
            binding.layoutShimmerPaywall.shimmer.stopShimmer()
            binding.layoutShimmerPaywall.root.hide()
            binding.rvPaywall.show()
            binding.tvSubContentBottom.show()
        }
    }

    private fun convertStoreDescription(): String {
        Timber.tag("Log Store Description: ").d(MMKVUtils.getRemoteConfig().directStoreDescription)
        val text = MMKVUtils.getRemoteConfig().directStoreDescription.replace("\\n", "\n")
        return text
    }

    private fun convertStoreDescriptionWithProductList(products: List<ProductDetails>): String {
        Timber.tag("Log Store Description: ").d(MMKVUtils.getRemoteConfig().directStoreDescription)
        var text = MMKVUtils.getRemoteConfig().directStoreDescription.replace("\\n", "\n")

        // Free trial
        val productFreeTrial =
            products.firstOrNull { it.productId in mutableListOf(PayWallConstants.FREE_TRIAL) }
        val dataFreeTrial = getPriceFreeTrial(
            productFreeTrial?.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList
                ?: emptyList()
        )
        if (text.contains(PayWallConstants.FREE_TRIAL_TITLE)) {
            productFreeTrial?.let {
                text = text.replace(PayWallConstants.FREE_TRIAL_TITLE, it.description)
            }
        }

        if (text.contains(PayWallConstants.FREE_TRIAL_DURATION)) {
            productFreeTrial?.let {
                text = text.replace(
                    PayWallConstants.FREE_TRIAL_DURATION,
                    "${dataFreeTrial.third} ${getString(R.string.days)}"
                )
            }
        }

        if (text.contains(PayWallConstants.FREE_TRIAL_PRICE)) {
            productFreeTrial?.let {
                text = text.replace(
                    PayWallConstants.FREE_TRIAL_PRICE,
                    dataFreeTrial.second
                )
            }
        }

        if (text.contains(PayWallConstants.FREE_TRIAL_PERIOD)) {
            productFreeTrial?.let {
                text = text.replace(
                    PayWallConstants.FREE_TRIAL_PERIOD,
                    it.name
                )
            }
        }

        // monthly
        val productMonthly =
            products.firstOrNull { it.productId in mutableListOf(PayWallConstants.MONTHLY) }
        if (text.contains(PayWallConstants.MONTHLY_TITLE)) {
            productMonthly?.let {
                text = text.replace(PayWallConstants.MONTHLY_TITLE, it.description)
            }
        }

        if (text.contains(PayWallConstants.MONTHLY_PRICE)) {
            productMonthly?.let {
                text = text.replace(
                    PayWallConstants.MONTHLY_PRICE,
                    it.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()?.formattedPrice
                        ?: ""
                )
            }
        }
        if (text.contains(PayWallConstants.MONTHLY_PERIOD)) {
            productMonthly?.let {
                text = text.replace(
                    PayWallConstants.MONTHLY_PERIOD,
                    it.name
                )
            }
        }

        // Weekly
        val productWeekly =
            products.firstOrNull { it.productId in mutableListOf(PayWallConstants.WEEKLY_STANDARD) }
        if (text.contains(PayWallConstants.WEEKLY_TITLE)) {
            productWeekly?.let {
                text = text.replace(PayWallConstants.WEEKLY_TITLE, it.description)
            }
        }

        if (text.contains(PayWallConstants.WEEKLY_PRICE)) {
            productWeekly?.let {
                text = text.replace(
                    PayWallConstants.WEEKLY_PRICE,
                    it.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()?.formattedPrice
                        ?: ""
                )
            }
        }
        if (text.contains(PayWallConstants.WEEKLY_PERIOD)) {
            productWeekly?.let {
                text = text.replace(
                    PayWallConstants.WEEKLY_PERIOD,
                    it.name
                )
            }
        }

        // Lifetime
        val productLifetime =
            products.firstOrNull { it.productId in mutableListOf(PayWallConstants.LIFETIME) }
        if (text.contains(PayWallConstants.LIFETIME_TITLE)) {
            productLifetime?.let {
                text = text.replace(PayWallConstants.LIFETIME_TITLE, it.description)
            }
        }

        if (text.contains(PayWallConstants.LIFETIME_PRICE)) {
            productLifetime?.let {
                text = text.replace(
                    PayWallConstants.LIFETIME_PRICE,
                    it.oneTimePurchaseOfferDetails?.formattedPrice ?: ""
                    ?: ""
                )
            }
        }

        return text
    }

    private fun convertPrice(text: String): String {
        return ""
    }

    override fun initData() {
        viewModel.queryAllProductDetails()
    }

    override fun observerData() {
        viewModel.products.observe(this) { products ->
            Timber.tag("LOG PRODUCTS IN BILLING")
                .d(GsonBuilder().setPrettyPrinting().create().toJson(products))
            if (products.isNotEmpty()) {
                val listPair = convertProductDetailsToPairData(products)
                binding.tvDescription.text = convertStoreDescriptionWithProductList(products)
                viewModel.updateListPaywall(listPair)
            }
            setUpShimmer(products.isEmpty())
        }

        viewModel.listPaywall.observe(this) {
            when (it) {
                is Resource.Error -> {
                    //
                }

                is Resource.Loading -> {
                    //
                }

                is Resource.Success -> {
                    it.data?.let { listPair ->
                        if (listPair.isNotEmpty()) {
                            paywallAdapter.setData(listPair.toMutableList())
                        }
                        binding.tvSubContentBottom.isVisible = listPair.isNotEmpty()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.purchaseResult.collect { result ->
                Timber.tag("Log Purchase: ")
                    .d(GsonBuilder().setPrettyPrinting().create().toJson(result))
                when (result) {
                    is PurchaseResult.Success -> {
                        val purchase = result.purchase
                        if (!SecurityUtils.isSignatureValid(purchase = purchase)) {
                            Timber.tag("Log Purchase: ").d("Signature Is Not Valid")
                            return@collect
                        }
                        // Verify if not refund package
                        if (!result.purchase.isAcknowledged) {
                            viewModel.acknowledgedPurchase(purchase = purchase, onResult = {
                                if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                                    flowPurchaseSuccessful(purchase)
                                } else {
                                    Timber.tag("Log Purchase: ").d("Failure!")
                                    AppToast(
                                        this@PaywallActivity,
                                        getString(R.string.something_went_wrong_try_later),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@acknowledgedPurchase
                                }
                            })
                        } else {
                            flowPurchaseSuccessful(purchase)
                        }
                    }

                    is PurchaseResult.Failure -> {
                        // Failure
                        Timber.tag("Log Purchase: ").d("Purchase Failure!")
                    }

                    PurchaseResult.Canceled -> {
                        // Canceled
                        Timber.tag("Log Purchase: ").d("Purchase Canceled!")
                    }

                    null -> {
                        Timber.tag("Log Purchase: ").d("Data null")
                    }
                }
            }
        }
    }

    private fun flowPurchaseSuccessful(purchase: Purchase) {
        // Success
//        if (!HomeActivity.isRunning) {
//            if (MMKVUtils.isSkipFlowAddCarAndLocation()) {
//                startActivity(Intent(this, HomeActivity::class.java))
//            } else {
//                val intent = Intent(this, CreateYourVehicleActivity::class.java)
//                try {
//                    startActivity(intent)
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//            }
//        }
        finish()
//        MMKVUtils.setShowedDialogPremium(true)
//        Timber.tag("Log Purchase: ")
//            .d("Purchase Success! ${GsonBuilder().create().toJson(productDetails)}")
//        productDetails?.let {
////            BillingUtils.savePremiumData(purchase, it)
//            productDetails = null
////            postDelayed({ // pending time dialog payment hide
////                showDialogPremium()
////            }, 1000)
//        }
    }

    /**
     * Convert ProductDetails to PairData
     * @param products list of all ProductDetails from billing
     */
    private fun convertProductDetailsToPairData(products: List<ProductDetails>): MutableList<Pair<PaywallItem, ProductDetails>> {
        val listPair: MutableList<Pair<PaywallItem, ProductDetails>> = mutableListOf()
        // Filter with condition firebase config remote
        val listFilter = mutableListOf<ProductDetails>()
        when (MMKVUtils.getRemoteConfig().isVersionPaywall) {
            PayWallConstants.VersionPaywall.WEEK.ordinal.toLong() -> {
                listFilter.addAll(products.filter {
                    it.productId in mutableListOf(
                        PayWallConstants.WEEKLY_STANDARD,
                        PayWallConstants.MONTHLY,
                        PayWallConstants.LIFETIME
                    )
                })
            }

            PayWallConstants.VersionPaywall.YEARLY_NON_TRIAL.ordinal.toLong() -> {
                listFilter.addAll(products.filter {
                    it.productId in mutableListOf(
                        PayWallConstants.MONTHLY,
                        PayWallConstants.YEARLY,
                        PayWallConstants.LIFETIME
                    )
                })
            }

            // include default
            else -> {
                listFilter.addAll(products.filter {
                    it.productId in mutableListOf(
                        PayWallConstants.FREE_TRIAL,
                        PayWallConstants.MONTHLY,
                        PayWallConstants.LIFETIME
                    )
                })
            }
        }
        listFilter.forEach {
            listPair.add(convertProductDetailToPair(it))
        }
        return listPair
    }

    private fun convertProductDetailToPair(productDetails: ProductDetails): Pair<PaywallItem, ProductDetails> {
        val paywallItem = PaywallItem()
        when (productDetails.productId) {
            PayWallConstants.FREE_TRIAL -> {
                val dataFreeTrial = getPriceFreeTrial(
                    productDetails.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList
                        ?: emptyList()
                )
                paywallItem.apply {
                    type = AppConstants.PaymentType.FREE_TRIAL
                    price = dataFreeTrial.first.toBigDecimal()
                    priceFormat = dataFreeTrial.second
                    subtitle =
                        getString(R.string.try_) + " " + dataFreeTrial.third + " " + getString(R.string.day_free_then) + " " + paywallItem.priceFormat.ifEmpty {
                            MoneyUtils.formatBigDecimal(price, asCurrency = true)
                        } + "/" + getString(R.string.common_year)
                }
            }

            PayWallConstants.WEEKLY_STANDARD -> {
                paywallItem.apply {
                    price =
                        ((productDetails.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()?.priceAmountMicros
                            ?: 0L) / 1000000L).toBigDecimal()
                    priceFormat =
                        productDetails.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()?.formattedPrice
                            ?: ""
                    type = AppConstants.PaymentType.WEEKLY
                    title = getString(
                        R.string.weekly
                    )
                    subtitle = getString(R.string.auto_renew)

                }
            }

            PayWallConstants.MONTHLY -> {
                paywallItem.apply {
                    price =
                        ((productDetails.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()?.priceAmountMicros
                            ?: 0L) / 1000000L).toBigDecimal()
                    priceFormat =
                        productDetails.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()?.formattedPrice
                            ?: ""
                    type = AppConstants.PaymentType.MONTHLY
                    title = getString(
                        R.string.monthly
                    )
                    subtitle =
                        if (MMKVUtils.getRemoteConfig().isVersionPaywall == PayWallConstants.VersionPaywall.DEFAULT.ordinal.toLong()) {
                            getString(R.string.renew_monthly)
                        } else {
                            getString(R.string.auto_renew)
                        }
                }
            }

            PayWallConstants.YEARLY -> {
                paywallItem.apply {
                    price =
                        ((productDetails.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()?.priceAmountMicros
                            ?: 0L) / 1000000L).toBigDecimal()
                    priceFormat =
                        productDetails.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()?.formattedPrice
                            ?: ""
                    type = AppConstants.PaymentType.YEARLY
                    title = getString(
                        R.string.yearly
                    )
                    subtitle = getString(R.string.auto_renew)
                }
            }

            PayWallConstants.LIFETIME -> {
                paywallItem.apply {
                    price = ((productDetails.oneTimePurchaseOfferDetails?.priceAmountMicros
                        ?: 0L) / 1000000L).toBigDecimal()
                    priceFormat = productDetails.oneTimePurchaseOfferDetails?.formattedPrice ?: ""
                    type = AppConstants.PaymentType.LIFETIME
                    title = getString(
                        R.string.lifetime
                    )
                    subtitle = getString(R.string.onetime_payment)
                }
            }

            else -> {
                paywallItem.apply {
                    type = AppConstants.PaymentType.FREE_TRIAL
                }
            }
        }
        return Pair(paywallItem, productDetails)
//        if (productDetails.productType == BillingClient.ProductType.INAPP) {
//            paywallItem.apply {
//                price = (productDetails.oneTimePurchaseOfferDetails?.priceAmountMicros
//                    ?: 0L).toBigDecimal()
//                type = AppConstants.PaymentType.LIFETIME
//                title = getString(
//                    R.string.lifetime
//                )
//                subtitle = getString(R.string.onetime_payment)
//            }
//        } else {
//
//        }
    }

    private fun getPriceFreeTrial(data: List<ProductDetails.PricingPhase>): Triple<Long, String, Int> {
        if (data.size > 1) {
            val item = data[1]
            return Triple(
                item.priceAmountMicros / 1000000L,
                item.formattedPrice,
                getDayFreeTrial(data.first().billingPeriod)
            )
        }
        if (data.size == 1) {
            val item = data.first()
            return Triple(
                item.priceAmountMicros / 1000000L,
                item.formattedPrice,
                getDayFreeTrial(item.billingPeriod)
            )
        }

        return Triple(0L, "", getDayFreeTrial(""))
    }

    private fun getDayFreeTrial(dayText: String): Int {
        Timber.tag("Log Number of Day:").d(dayText)
        try {
            if (dayText.contains("P")) {
                val numberDay = dayText.substring(1, dayText.length - 1).toInt()
                Timber.tag("Log Number of Day:").d("$numberDay")
                return numberDay
            }
            return 3
        } catch (ex: Exception) {
            ex.printStackTrace()
            return 3
        }
    }

    /**
     * Set result data on back
     * @param AppConstants.IS_BACK_FROM_PAYWALL is true when back not show paywall
     */
    private fun setUpBackPressAndResultOnBack() {
        setResult(RESULT_OK, Intent().apply {
            putExtra(AppConstants.IS_BACK_FROM_PAYWALL, true)
        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(RESULT_OK, Intent().apply {
                    putExtra(AppConstants.IS_BACK_FROM_PAYWALL, true)
                })
                finish()
            }
        })
    }

    override fun onClick(position: Int, item: Pair<PaywallItem, ProductDetails?>) {
        item.second?.let {
            viewModel.purchase(this, it)
            productDetails = it
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnClose -> {
                if (MMKVUtils.isSkipFlowAddCarAndLocation()) {
                    finish()
                } else {
                    //
                }
            }
        }
    }
}