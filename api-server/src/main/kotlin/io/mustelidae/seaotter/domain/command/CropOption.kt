package io.mustelidae.seaotter.domain.command

class CropOption(
    val type: Type,
    val width: Int?,
    val height: Int?,
    val position: Position?,
    val x1: Int?,
    val y1: Int?,
    val x2: Int?,
    val y2: Int?
) : Option {
    class Builder {
        var type: Type? = null
        var width: Int? = null
        var height: Int? = null
        var position: Position? = null
        var x1: Int? = null
        var y1: Int? = null
        var x2: Int? = null
        var y2: Int? = null

        @Throws(IllegalArgumentException::class)
        fun coordinates(x1: Int, y1: Int, x2: Int, y2: Int) = apply {
            if (width != null && height != null)
                throw IllegalArgumentException("width and height are already set.")

            this.x1 = x1
            this.y1 = y1
            this.x2 = x2
            this.y2 = y2
            this.type = Type.COORDINATES
        }

        @Throws(IllegalArgumentException::class)
        fun position(position: Position, width: Int, height: Int) = apply {
            if (x1 != null || y1 != null || x2 != null || y2 != null)
                throw IllegalArgumentException("coordinates is already set.")

            this.position = position
            this.width = width
            this.height = height
            this.type = Type.POSITION
        }

        fun pointScale(x1: Int, y1: Int, width: Int, height: Int) = apply {
            this.x1 = x1
            this.y1 = y1
            this.width = width
            this.height = height
            this.type = Type.POINT_SCALE
        }

        fun build() = CropOption(type!!, width, height, position, x1, y1, x2, y2)
    }

    enum class Position {
        CENTER, LEFT_UPPER
    }

    enum class Type {
        COORDINATES, POSITION, POINT_SCALE
    }
}
