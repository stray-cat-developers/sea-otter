package io.mustelidae.seaotter.domain.delivery

import java.net.URL

data class ShippingItem(
    val shippingImage: Image
) {
    val shippedImages: MutableList<Pair<Image, URL>> = mutableListOf()
}
