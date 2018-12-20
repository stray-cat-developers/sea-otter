package io.mustelidae.seaotter.domain.editor

class ResizeOption(
    val keepRatio: Boolean,
    val scale: Double?,
    val width: Int?,
    val height: Int?
) : Option {
    class Builder {
        var scale: Double? = null
            private set
        var keepRatio: Boolean? = null
            private set
        var width: Int? = null
            private set
        var height: Int? = null
            private set

        @Throws(IllegalArgumentException::class)
        fun scale(scale: Double) = apply {

            if (width != null && height != null)
                throw IllegalArgumentException("The width and height are already set.")

            this.scale = scale
            keepRatio = true
        }

        @Throws(IllegalArgumentException::class)
        fun size(width: Int, height: Int, keepRatio: Boolean) = apply {

            if (scale != null)
                throw IllegalArgumentException("The scale are already set.")

            this.width = width
            this.height = height
            this.keepRatio = keepRatio
        }

        fun build() = ResizeOption(keepRatio!!, scale, width, height)
    }
}
