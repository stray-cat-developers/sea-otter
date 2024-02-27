package io.mustelidae.seaotter.domain.editor.command

class RotateOption(
    val angle: Double?,
    val flip: Flip?,
) : Option {

    class Builder {
        var angle: Double? = null
        var flip: Flip? = null

        fun flip(flip: Flip) = apply {
            this.flip = flip
        }

        fun angle(angle: Double) = apply {
            this.angle = angle
        }

        fun build() = RotateOption(angle, flip)
    }

    enum class Flip {
        HORZ, VERT
    }
}
