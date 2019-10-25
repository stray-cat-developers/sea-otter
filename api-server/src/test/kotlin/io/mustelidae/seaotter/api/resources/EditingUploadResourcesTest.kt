package io.mustelidae.seaotter.api.resources

import com.google.common.truth.Truth.assertThat
import io.mustelidae.seaotter.utils.Jackson
import org.junit.jupiter.api.Test

internal class EditingUploadResourcesTest {

    @Test
    fun jsonParse() {
        val json = """ 
            {
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
        assertThat(request.edits.size).isEqualTo(1)
        val coordinate = request.edits[0] as EditingUploadResources.Crop.Coordinate
        assertThat(coordinate.x1).isEqualTo(1)
        assertThat(coordinate.y1).isEqualTo(2)
    }
}
