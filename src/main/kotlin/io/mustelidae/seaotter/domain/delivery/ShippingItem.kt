package io.mustelidae.seaotter.domain.delivery

import java.net.URL

data class ShippingItem<T>(
    val item: T
) {
    val shippedItem: MutableList<Pair<T, URL>> = mutableListOf()
}
