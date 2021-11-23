package io.mustelidae.seaotter.api.controller

import io.kotlintest.matchers.asClue
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.mustelidae.seaotter.api.IntegrationTestSupport
import io.mustelidae.seaotter.api.resources.EditingUploadResources
import io.mustelidae.seaotter.common.Replies
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.utils.fromJson
import io.mustelidae.seaotter.utils.fromJsonByContent
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import org.junit.jupiter.api.Test
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.Base64Utils
import org.springframework.util.LinkedMultiValueMap
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

internal class EditingImageUploadControllerTest : IntegrationTestSupport() {

    @Test
    fun uploadMultipart() {
        // Given
        val fileName = "snapshot.png"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val parameters = LinkedMultiValueMap<String, String>().apply {
            add("1:crop", "coordinate:0,0,100,100")
            add("2:resize", "scale:50.0")
            add("3:rotate", "flip:HORZ")
            add("4:rotate", "angle:180.0")
            add("hasOriginal", "false")
        }

        // When
        val uri = linkTo<EditingImageUploadController> { upload(MockMultipartFile(file.name, file.inputStream()), mapOf(), false) }.toUri()
        val replies = mockMvc.perform(
            MockMvcRequestBuilders.multipart(uri)
                .file(MockMultipartFile("multiPartFile", fileName, null, file.inputStream()))
                .params(parameters)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andExpect(status().is2xxSuccessful)
            .andReturn()
            .response
            .contentAsString
            .fromJsonByContent<List<EditingUploadResources.ReplyOnImage>>()

        // Then
        replies.first().asClue {
            it.width shouldBe 50
            it.height shouldBe 50
            it.histories shouldNotBe null
            it.histories!!.first() shouldBe "crop"
        }
    }

    @Test
    fun uploadBase64Form() {
        // Given
        val fileName = "snapshot.png"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val bufferedImage = Image.from(file).bufferedImage

        val out = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "PNG", out)
        val base64 = "data:image/png;base64," + Base64Utils.encodeToString(out.toByteArray())
        val uri = linkTo<EditingImageUploadController> { upload("", mapOf(), false) }.toUri()
        val parameters = LinkedMultiValueMap<String, String>().apply {
            add("base64", base64)
            add("1:crop", "coordinate:0,0,100,100")
            add("2:rotate", "flip:HORZ")
            add("3:rotate", "angle:180.0")
        }
        // When
        val replies = mockMvc.post(uri) {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            params = parameters
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Replies<EditingUploadResources.ReplyOnImage>>()
            .getContent()

        // Then
        replies.first().asClue {
            it.histories!!.size shouldBe 3
            it.width shouldBe 100
            it.height shouldBe 100
        }
    }

    @Test
    fun uploadJson() {
        // Given
        val request = """
            {
              "edits": [
                {
                  "crop": {
                    "coordinate": {
                      "x1": 0,
                      "y1": 0,
                      "x2": 1,
                      "y2": 1
                    }
                  },
                  "rotate":{
                     "angle":{                  
                        "degree": 270.0
                     }
                  }      
                }
              ],
              "base64": "ZGF0YTppbWFnZS9wbmc7YmFzZTY0LGlWQk9SdzBLR2dvQUFBQU5TVWhFVWdBQUFBUUFBQUFFQ0FZQUFBQ3A4WjUrQUFBTVMybERRMUJKUTBNZ1VISnZabWxzWlFBQVNJbVZWd2RZVThrV25sdFNTV2lCVUtTRTNrUXAwcVdFMENJSVNCVkVKU1NCaEJKalFoQ3hzeXlyNE5wRkJOUVZYUlZSZEMyQXJCVjdXUlM3YTNrb2k4ckt1bGl3b2ZJbUJkWjF2L2ZlOTg3M3piMS96cHp6bjVLNTk4NEFvRlBIazByelVWMEFDaVNGc29USVVOYmt0SFFXcVJzZ0FBVlVRQVJNSGw4dVpjZkh4d0Fvdy9lL3k1dWIwQnJLTlZjbDF6L24vNnZvQ1lSeVBnQklQTVJaQWptL0FPSURBT0JsZkttc0VBQ2lMOVRiekNxVUtuRUd4QVl5bUNERVVpWE9VZU15SmM1UzQycVZUVklDQitKZEFKQnBQSjRzQndEdFZxaG5GZkZ6SUkvMmJZamRKQUt4QkFBZE1zUkJmQkZQQUhFVXhLTUxDbVlvTWJRRGpsbGY4T1Q4alROcmhKUEh5eG5CNmxwVVFnNFR5Nlg1dk5uL1p6dit0eFRrSzRaajJNTkJFOG1pRXBRMXc3N2R6cHNScmNRMGlQc2tXYkZ4RU90RC9FNHNVTmxEakZKRmlxaGt0VDFxeHBkellNOEFFMkkzQVM4c0dtSXppQ01rK2JFeEduMVd0amlDQ3pGY0lXaXh1SkNicFBGZExKU0hKMm80NjJRekV1S0djYmFNdzliNE52RmtxcmhLKzFPS3ZHUzJodisyU01nZDVuOWRJa3BLVmVlTVVZdkVLYkVRYTBQTWxPY2xScXR0TU5zU0VTZDIyRWFtU0ZEbWJ3dXh2MUFTR2FybXg2Wmx5eUlTTlBheUF2bHd2ZGhpa1pnYnE4RTFoYUtrS0EzUExqNVBsYjh4eEsxQ0NUdDVtRWNvbnh3elhJdEFHQmF1cmgyN0lwUWthK3JGdXFTRm9Ra2EzNWZTL0hpTlBVNFY1a2NxOWRZUW04bUxFalcrZUZBaFhKQnFmanhXV2hpZnBNNFR6OHJsVFloWDU0TVhneGpBQVdHQUJSUndaSUVaSUJlSU8vcGErdUF2OVV3RTRBRVp5QUZDNEtyUkRIdWtxbVlrOEpvSVNzQWZFQW1CZk1RdlZEVXJCRVZRLzJsRXE3NjZnbXpWYkpIS0l3ODhocmdBUklOOCtGdWg4cEtNUkVzQnYwR04rQi9SK1REWGZEaVVjLy9Vc2FFbVJxTlJEUE95ZElZdGllSEVNR0lVTVlMb2hKdmlRWGdBSGdPdklYQjQ0TDY0MzNDMmY5a1RIaE02Q1k4SU53aGRoRHZUeGFXeXIrcGhnWW1nQzBhSTBOU2M5V1hOdUQxazljSkQ4VURJRDdseEptNEtYUEZ4TUJJYkQ0YXh2YUNXbzhsY1dmM1gzSCtyNFl1dWErd29iaFNVWWtRSm9UaCs3YW50ck8wMXdxTHM2WmNkVXVlYU5kSlh6c2pNMS9FNVgzUmFBTy9SWDF0aWk3SDkyRm5zQkhZZU80eTFBQloyREd2RkxtRkhsSGhrRmYybVdrWEQwUkpVK2VSQkh2RS80dkUwTVpXZGxMczF1dlc2ZlZUUEZRcUxsZTlId0praG5TMFQ1NGdLV1d6NDVoZXl1QkwrbU5Fc0R6ZDNQd0NVM3hIMWErb1ZVL1Y5UUpnWC90S1Z2Z1lnVURBME5IVDRMMTBNZktZUGZBc0E5ZkZmT29lajhIVmdCTUM1U3I1Q1ZxVFc0Y29MQVg2ZGRPQVRaUUlzZ0Exd2hQVjRBRzhRQUVKQU9KZ0E0a0FTU0FQVFlKZEZjRDNMd0N3d0Z5d0M1YUFTckFCclFRM1lCTGFBSFdBMzJBZGF3R0Z3QXB3QkY4RVZjQVBjaGF1bkJ6d0QvZUFOR0VRUWhJVFFFUVppZ2xnaWRvZ0w0b0g0SWtGSU9CS0RKQ0JwU0NhU2cwZ1FCVElYK1FhcFJGWWhOY2htcEFINUNUbUVuRURPSTUzSUhlUWgwb3U4UkQ2Z0dFcEREVkJ6MUI0ZGkvcWliRFFhVFVLbm9qbm9UTFFFTFVPWG9kVm9QYm9MYlVaUG9CZlJHMmdYK2d3ZHdBQ21oVEV4Szh3Vjg4VTRXQnlXam1Wak1tdytWb0ZWWWZWWUU5WUcvK2RyV0JmV2g3M0hpVGdEWitHdWNBVkg0Y2s0SDUrSno4ZVg0alg0RHJ3WlA0VmZ3eC9pL2ZobkFwMWdSbkFoK0JPNGhNbUVITUlzUWptaGlyQ05jSkJ3R2o1TlBZUTNSQ0tSU1hRZytzQ25NWTJZUzV4RFhFcmNRTnhEUEU3c0pIWVRCMGdra2duSmhSUklpaVB4U0lXa2N0SjYwaTdTTWRKVlVnL3BIVm1MYkVuMklFZVEwOGtTY2ltNWlyeVRmSlI4bGZ5RVBFalJwZGhSL0NseEZBRmxObVU1WlN1bGpYS1owa01acE9wUkhhaUIxQ1JxTG5VUnRacmFSRDFOdlVkOXBhV2xaYTNscHpWSlM2eTFVS3RhYTYvV09hMkhXdTlwK2pSbkdvZVdRVlBRbHRHMjA0N1Q3dEJlMGVsMGUzb0lQWjFlU0Y5R2I2Q2ZwRCtndjlObWFJL1I1bW9MdEJkbzEybzNhMS9WZnE1RDBiSFRZZXRNMHluUnFkTFpyM05acDArWG9tdXZ5OUhsNmM3WHJkVTlwSHRMZDBDUG9lZXVGNmRYb0xkVWI2ZmVlYjJuK2lSOWUvMXdmWUYrbWY0Vy9aUDYzUXlNWWNQZ01QaU1ieGhiR2FjWlBRWkVBd2NEcmtHdVFhWEJib01PZzM1RGZjTnhoaW1HeFlhMWhrY011NWdZMDU3SlplWXpselAzTVc4eVB4aVpHN0dOaEVaTGpKcU1yaHE5TlI1bEhHSXNOSzR3M21OOHcvaURDY3NrM0NUUFpLVkppOGw5VTl6VTJYU1M2U3pUamFhblRmdEdHWXdLR01VZlZURnEzNmhmelZBelo3TUVzemxtVzh3dW1RMllXNWhIbWt2TjE1dWZOTyt6WUZxRVdPUmFyTEU0YXRGcnliQU1zaFJicnJFOFp2azd5NURGWnVXenFsbW5XUDFXWmxaUlZncXJ6VllkVm9QV0R0YkoxcVhXZTZ6djIxQnRmRzJ5YmRiWXROdjAyMXJhVHJTZGE5dG8rNnNkeGM3WFRtUzN6dTZzM1Z0N0IvdFUrKy9zVyt5Zk9oZzdjQjFLSEJvZDdqblNIWU1kWnpyV08xNTNJanI1T3VVNWJYQzY0b3c2ZXptTG5HdWRMN3VnTHQ0dVlwY05McDJqQ2FQOVJrdEcxNCsrNVVwelpic1d1VGE2UGh6REhCTXpwblJNeTVqblkyM0hwbzlkT2ZiczJNOXVYbTc1Ymx2ZDdycnJ1MDl3TDNWdmMzL3A0ZXpCOTZqMXVPNUo5NHp3WE9EWjZ2bGluTXM0NGJpTjQyNTdNYndtZW4zbjFlNzF5ZHZIVytiZDVOM3JZK3VUNlZQbmM4dlh3RGZlZDZudk9UK0NYNmpmQXIvRGZ1Lzl2ZjBML2ZmNS94bmdHcEFYc0RQZzZYaUg4Y0x4VzhkM0Ixb0g4Z0kzQjNZRnNZSXlnMzRJNmdxMkN1WUYxd2MvQ3JFSkVZUnNDM25DZG1MbnNuZXhuNGU2aGNwQ0Q0YSs1Zmh6NW5HT2gyRmhrV0VWWVIzaCt1SEo0VFhoRHlLc0kzSWlHaVA2STcwaTUwUWVqeUpFUlVldGpMckZOZWZ5dVEzYy9naytFK1pOT0JWTmkwNk1yb2wrRk9NY0k0dHBtNGhPbkRCeDljUjdzWGF4a3RpV09CREhqVnNkZHovZUlYNW0vTStUaUpQaUo5Vk9lcHpnbmpBMzRXd2lJM0Y2NHM3RU4wbWhTY3VUN2lZN0ppdVMyMU4wVWpKU0dsTGVwb2FscmtydG1qeDI4cnpKRjlOTTA4UnByZW1rOUpUMGJla0RVOEtuckozU2srR1ZVWjV4YzZyRDFPS3A1NmVaVHN1ZmRtUzZ6blRlOVAyWmhNelV6SjJaSDNseHZIcmVRQlkzcXk2cm44L2hyK00vRTRRSTFnaDZoWUhDVmNJbjJZSFpxN0tmNWdUbXJNN3BGUVdMcWtSOVlvNjRSdndpTnlwM1UrN2J2TGk4N1hsRCthbjVld3JJQlprRmh5VDZranpKcVJrV000cG5kRXBkcE9YU3JwbitNOWZPN0pkRnk3YkpFZmxVZVd1aEFkeXdYMUk0S3I1VlBDd0tLcW90ZWpjclpkYitZcjFpU2ZHbDJjNnpsOHgrVWhKUjh1TWNmQTUvVHZ0Y3E3bUw1ajZjeDU2M2VUNHlQMnQrK3dLYkJXVUxlaFpHTHR5eGlMb29iOUV2cFc2bHEwcGZmNVA2VFZ1WmVkbkNzdTV2STc5dExOY3VsNVhmK2k3Z3UwMkw4Y1hpeFIxTFBKZXNYL0s1UWxCeG9kS3RzcXJ5NDFMKzBndmZ1MzlmL2YzUXN1eGxIY3U5bDI5Y1FWd2hXWEZ6WmZES0hhdjBWcFdzNmw0OWNYWHpHdGFhaWpXdjEwNWZlNzVxWE5XbWRkUjFpblZkMVRIVnJldHQxNjlZLzdGR1ZIT2pOclIyVDUxWjNaSzZ0eHNFRzY1dURObll0TWw4VStXbUR6K0lmN2k5T1hKemM3MTlmZFVXNHBhaUxZKzNwbXc5KzZQdmp3M2JUTGRWYnZ1MFhiSzlhMGZDamxNTlBnME5PODEyTG05RUd4V052YnN5ZGwzWkhiYTd0Y20xYWZNZTVwN0t2V0N2WXUvdlAyWCtkSE5mOUw3Mi9iNzdtdzdZSGFnN3lEaFkwWXcwejI3dWJ4RzFkTFdtdFhZZW1uQ292UzJnN2VEUFkzN2VmdGpxY08wUnd5UExqMUtQbGgwZE9sWnliT0M0OUhqZmlad1QzZTNUMisrZW5IenkrcWxKcHpwT1I1OCtkeWJpek1tejdMUEh6Z1dlTzN6ZS8veWhDNzRYV2k1NlgyeSs1SFhwNEM5ZXZ4enM4TzVvdnV4enVmV0szNVcyenZHZFI2OEdYejF4TGV6YW1ldmM2eGR2eE43b3ZKbDg4L2F0akZ0ZHR3VzNuOTdKdi9QaTE2SmZCKzh1dkVlNFYzRmY5MzdWQTdNSDlmOXkrdGVlTHUrdUl3L0RIbDU2bFBqb2JqZS8rOWx2OHQ4KzlwUTlwait1ZW1MNXBPR3B4OVBEdlJHOVYzNmY4bnZQTSttendiN3lQL1QrcUh2dStQekFueUYvWHVxZjNOL3pRdlppNk9YU1Z5YXZ0cjhlOTdwOUlIN2d3WnVDTjROdks5Nlp2TnZ4M3ZmOTJRK3BINTRNenZwSStsajl5ZWxUMitmb3ovZUdDb2FHcER3WlQ3VVZ3T0JBczdNQmVMa2RBSG9hQUl3cmNQOHdSWDNPVXdtaVBwdXFFUGhQV0gwV1ZJazNBRTN3cHR5dWM0NERzQmNPKzRXUU93UUE1Vlk5S1FTZ25wNGpReVB5YkU4UE5SY05ubmdJNzRhR1hwa0RRR29ENEpOc2FHaHd3OURRcDYwdzJUc0FISitwUGw4cWhRalBCaitFS05FTjQ0eDM0Q3Y1TndyNGY3WVYrd2hxQUFBQUNYQklXWE1BQUJZbEFBQVdKUUZKVWlUd0FBQUJtV2xVV0hSWVRVdzZZMjl0TG1Ga2IySmxMbmh0Y0FBQUFBQUFQSGc2ZUcxd2JXVjBZU0I0Yld4dWN6cDRQU0poWkc5aVpUcHVjenB0WlhSaEx5SWdlRHA0YlhCMGF6MGlXRTFRSUVOdmNtVWdOUzQwTGpBaVBnb2dJQ0E4Y21SbU9sSkVSaUI0Yld4dWN6cHlaR1k5SW1oMGRIQTZMeTkzZDNjdWR6TXViM0puTHpFNU9Ua3ZNREl2TWpJdGNtUm1MWE41Ym5SaGVDMXVjeU1pUGdvZ0lDQWdJQ0E4Y21SbU9rUmxjMk55YVhCMGFXOXVJSEprWmpwaFltOTFkRDBpSWdvZ0lDQWdJQ0FnSUNBZ0lDQjRiV3h1Y3pwbGVHbG1QU0pvZEhSd09pOHZibk11WVdSdlltVXVZMjl0TDJWNGFXWXZNUzR3THlJK0NpQWdJQ0FnSUNBZ0lEeGxlR2xtT2xCcGVHVnNXRVJwYldWdWMybHZiajQwUEM5bGVHbG1PbEJwZUdWc1dFUnBiV1Z1YzJsdmJqNEtJQ0FnSUNBZ0lDQWdQR1Y0YVdZNlVHbDRaV3haUkdsdFpXNXphVzl1UGpROEwyVjRhV1k2VUdsNFpXeFpSR2x0Wlc1emFXOXVQZ29nSUNBZ0lDQThMM0prWmpwRVpYTmpjbWx3ZEdsdmJqNEtJQ0FnUEM5eVpHWTZVa1JHUGdvOEwzZzZlRzF3YldWMFlUNEtselBiMUFBQUFCeHBSRTlVQUFBQUFnQUFBQUFBQUFBQ0FBQUFLQUFBQUFJQUFBQUNBQUFBVjA0SW1HTUFBQUFqU1VSQlZCZ1pZbng0OTk1L1JrWkdodi8vL3pPQWFNWkg5KzREQlJpQUFneGdBQUFBQVAvLzVWL091UUFBQUNKSlJFRlVZM3g4Ly83Ly8vOFpHQmdaR1JqQTlKUDdELzcvKy84WEtNQU1GZ0FBemtFY2ZsMGJ6U1VBQUFBQVNVVk9SSzVDWUlJPQ",
              "hasOriginal": false 
            }
        """.trimIndent()

        val replies = mockMvc.post(linkTo<EditingImageUploadController> { upload(EditingUploadResources.Request(listOf(), "", false)) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = request
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Replies<EditingUploadResources.ReplyOnImage>>()
            .getContent()

        // Then
        replies.first().asClue {
            it.histories!!.size shouldBe 2
            it.width shouldBe 1
            it.height shouldBe 1
        }
    }
}
