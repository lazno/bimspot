package lazno.bimspot.rest

import lazno.bimspot.Regions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JacksonDeserializerTest {

    @Test fun `when regions json is valid then all fields should be filled`() {
        val validRegionsJson = "{\"count\":2,\"results\":[{\"name\":\"Northeastern Africa\"," +
                "\"identifier\":\"northeastern_africa\"},{\"name\":\"Eastern Africa\",\"identifier\":\"eastern_africa\"}]}"

        val regs: Regions? = JacksonDeserializer.regions().deserialize(validRegionsJson)
        assertThat(regs?.count).isEqualTo(2)
        assertThat(regs?.results?.size).isEqualTo(2)

        val region_1 = regs?.results?.get(0)
        assertThat(region_1?.identifier).isEqualTo("northeastern_africa")
        assertThat(region_1?.name).isEqualTo("Northeastern Africa")

        val region_2 = regs?.results?.get(1)
        assertThat(region_2?.identifier).isEqualTo("eastern_africa")
        assertThat(region_2?.name).isEqualTo("Eastern Africa")

    }

    @Test fun `when required fields are missing then empty result will be returned`() {
        val validRegionsJson = "{\"count\":1, \"results\":[{\"name\":\"Northeastern Africa\"}]}"
        val regs: Regions? = JacksonDeserializer.regions().deserialize(validRegionsJson)
        assertThat(regs).isNotNull
        assertThat(regs?.count).isEqualTo(0)
        assertThat(regs?.results?.size).isEqualTo(0)
    }

}