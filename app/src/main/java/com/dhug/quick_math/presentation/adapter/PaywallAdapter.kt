package com.dhug.quick_math.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.ProductDetails
import com.dhug.quick_math.R
import com.dhug.quick_math.base.AppAdapter
import com.dhug.quick_math.data.local.entities.PaywallItem
import com.dhug.quick_math.databinding.ItemPaywallFreeTrialBinding
import com.dhug.quick_math.databinding.ItemPaywallLifetimeBinding
import com.dhug.quick_math.databinding.ItemPaywallMonthlyBinding
import com.dhug.quick_math.utils.AppConstants
import com.dhug.quick_math.utils.AppUtils.hide
import com.dhug.quick_math.utils.AppUtils.show
import com.dhug.quick_math.utils.MMKVUtils
import com.dhug.quick_math.utils.MoneyUtils
import com.dhug.quick_math.utils.PayWallConstants
import java.math.BigDecimal
import kotlin.div
import kotlin.text.ifEmpty
import kotlin.text.lowercase

class PaywallAdapter(private val context: Context) :
    AppAdapter<Pair<PaywallItem, ProductDetails?>>(context) {
    private lateinit var onClickItem: OnClickItem

    fun setListener(listener: OnClickItem) {
        this.onClickItem = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val viewHolder: AppViewHolder = when (viewType) {
            AppConstants.PaymentType.FREE_TRIAL.ordinal -> {
                FreeTrialViewHolder(
                    ItemPaywallFreeTrialBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                )
            }

            AppConstants.PaymentType.MONTHLY.ordinal -> {
                MonthlyViewHolder(
                    ItemPaywallMonthlyBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                )
            }

            AppConstants.PaymentType.WEEKLY.ordinal -> {
                WeeklyViewHolder(
                    ItemPaywallLifetimeBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                )
            }

            AppConstants.PaymentType.YEARLY.ordinal -> {
                YearlyViewHolder(
                    ItemPaywallLifetimeBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                )
            }

            else -> {
                LifetimeViewHolder(
                    ItemPaywallLifetimeBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                )
            }
        }

        return viewHolder

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).first.type.ordinal
    }


    inner class FreeTrialViewHolder(private val binding: ItemPaywallFreeTrialBinding) :
        AppViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onClickItem.onClick(adapterPosition, getItem(adapterPosition))
                }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindView(position: Int) {
            val item = getItem(position).first
            binding.tvTopContent.text = item.subtitle
        }

    }

    inner class MonthlyViewHolder(private val binding: ItemPaywallMonthlyBinding) :
        AppViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onClickItem.onClick(adapterPosition, getItem(adapterPosition))
                }
            }
        }

        override fun onBindView(position: Int) {
            val item = getItem(position).first
            binding.textTop = item.title
            binding.textBottom = item.subtitle
            binding.textPrice = getPriceFormat(item)
        }

    }

    inner class WeeklyViewHolder(private val binding: ItemPaywallLifetimeBinding) :
        AppViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onClickItem.onClick(adapterPosition, getItem(adapterPosition))
                }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindView(position: Int) {
            val item = getItem(position).first
            binding.textTop = item.title
            binding.textBottom = item.subtitle
            binding.textPrice = getPriceFormat(item)
            binding.tvSubTopTitle.text = "${getString(R.string.only)} ${
                getPriceFormatWithCurrency(
                    item,
                    OnlyType.FOR_DAY
                )
            }/${getString(R.string.common_day)?.lowercase()}"
            binding.btnLike.hide()
//            binding.tvSubContentBottom.hide()
        }

    }

    inner class YearlyViewHolder(private val binding: ItemPaywallLifetimeBinding) :
        AppViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onClickItem.onClick(adapterPosition, getItem(adapterPosition))
                }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindView(position: Int) {
            val item = getItem(position).first
//            binding.tvSubContentBottom.hide()
            binding.textTop = item.title
            binding.textBottom = item.subtitle
            binding.textPrice = getPriceFormat(item)
            binding.tvSubTopTitle.text = "${getString(R.string.only)} ${
                getPriceFormatWithCurrency(
                    item,
                    OnlyType.FOR_MONTH
                )
            }/${getString(R.string.common_month)?.lowercase()}"
            binding.btnLike.hide()
        }

    }

    inner class LifetimeViewHolder(private val binding: ItemPaywallLifetimeBinding) :
        AppViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onClickItem.onClick(adapterPosition, getItem(adapterPosition))
                }
            }
        }

        override fun onBindView(position: Int) {
            val item = getItem(position).first
            binding.llBestChoice.isVisible =
                MMKVUtils.getRemoteConfig().isVersionPaywall == PayWallConstants.VersionPaywall.DEFAULT.ordinal.toLong()
            binding.textTop = item.title
            binding.textBottom = item.subtitle
            binding.textPrice = getPriceFormat(item)
            binding.tvSubTopTitle.text = getString(R.string.best_choice)
            binding.btnLike.show()
        }

    }

    private fun getPriceFormat(item: PaywallItem): String {
        return item.priceFormat.ifEmpty {
            MoneyUtils.formatBigDecimal(item.price, asCurrency = true)
        }
    }

    private fun getPriceFormatWithCurrency(item: PaywallItem, onlyType: OnlyType): String {
        return when (onlyType) {
            OnlyType.FOR_MONTH -> {
                MoneyUtils.getCodeCurrency(item.priceFormat) + MoneyUtils.formatCurrency(
                    (item.price / BigDecimal(
                        30
                    )).toDouble()
                )
            }

            OnlyType.FOR_DAY -> {
                MoneyUtils.getCodeCurrency(item.priceFormat) + MoneyUtils.formatCurrency(
                    (item.price / BigDecimal(
                        30
                    )).toDouble()
                )
            }
        }
    }

    interface OnClickItem {
        fun onClick(position: Int, item: Pair<PaywallItem, ProductDetails?>)
    }

    enum class OnlyType {
        FOR_MONTH, FOR_DAY,
    }
}