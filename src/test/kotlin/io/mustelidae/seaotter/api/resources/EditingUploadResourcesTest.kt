package io.mustelidae.seaotter.api.resources

import io.kotlintest.matchers.asClue
import io.kotlintest.shouldBe
import io.mustelidae.seaotter.utils.Jackson
import org.junit.jupiter.api.Test

internal class EditingUploadResourcesTest {

    @Test
    fun jsonParse() {
        val json = """ 
            {
               "base64": "asdf",
               "edits":[
                     {
                        "crop":{
                           "coordinate":{
                              "x1":1,
                              "y1":2,
                              "x2":3,
                              "y2":4
                           }
                        }
                     }
                  ]
            }
        """.trimIndent()

        val mapper = Jackson.getMapper()

        val request = mapper.readValue(json, EditingUploadResources.Request::class.java)
        request.edits.size shouldBe 1
        val coordinate = request.edits[0] as EditingUploadResources.Crop.Coordinate
        coordinate.asClue {
            it.x1 shouldBe 1
            it.y1 shouldBe 2
        }
    }
}
