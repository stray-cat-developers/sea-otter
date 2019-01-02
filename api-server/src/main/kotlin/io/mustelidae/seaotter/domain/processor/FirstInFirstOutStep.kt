package io.mustelidae.seaotter.domain.processor

import io.mustelidae.seaotter.domain.command.CropOption
import io.mustelidae.seaotter.domain.command.Option
import io.mustelidae.seaotter.domain.command.ResizeOption
import io.mustelidae.seaotter.domain.command.RotateOption
import java.util.ArrayDeque
import java.util.Queue

class FirstInFirstOutStep {
    var queue: Queue<Option> = ArrayDeque()

    fun cropByCoordinates(x1: Int, y1: Int, x2: Int, y2: Int) {
        queue.offer(CropOption.Builder()
                .coordinates(x1, y1, x2, y2)
                .build())
    }

    fun cropByPosition(position: CropOption.Position, width: Int, height: Int) {
        queue.offer(CropOption.Builder()
                .position(position, width, height)
                .build())
    }

    fun cropByPoint(x1: Int, y1: Int, width: Int, height: Int) {
        queue.offer(CropOption.Builder()
                .pointScale(x1, y1, width, height)
                .build())
    }

    fun resizeByScale(scale: Double) {
        queue.offer(ResizeOption.Builder()
                .scale(scale)
                .build())
    }

    fun resizeBySize(width: Int, height: Int, keepRatio: Boolean) {
        queue.offer(ResizeOption.Builder()
                .size(width, height, keepRatio)
                .build())
    }

    fun rotateByDegree(degree: Double) {
        queue.offer(RotateOption.Builder()
                .angle(degree)
                .build())
    }

    fun rotateByFlip(flip: RotateOption.Flip) {
        queue.offer(RotateOption.Builder()
                .flip(flip)
                .build())
    }
}
