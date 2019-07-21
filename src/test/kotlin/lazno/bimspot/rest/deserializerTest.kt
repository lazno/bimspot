package lazno.bimspot.rest

import lazno.bimspot.MeasuresBySpecies
import lazno.bimspot.Regions
import lazno.bimspot.SpeciesInRegion
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JacksonDeserializerTest {

    @Test
    fun `when regions json is valid then all fields should be filled`() {
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

    @Test
    fun `when required fields are missing in regions json then empty result will be returned`() {
        val invalidRegionsJson = "{\"count\":1, \"results\":[{\"name\":\"Northeastern Africa\"}]}"
        val regs: Regions? = JacksonDeserializer.regions().deserialize(invalidRegionsJson)
        assertThat(regs).isNotNull
        assertThat(regs?.count).isEqualTo(0)
        assertThat(regs?.results?.size).isEqualTo(0)
    }

    @Test
    fun `when species json is valid then all fields should be filled`() {
        val validSpeciesJson = "{\"count\":5,\"region_identifier\":\"europe\",\"page\":\"1\"," +
                "\"result\":[" +
                "{\"taxonid\":59,\"kingdom_name\":\"ANIMALIA\",\"phylum_name\":\"MOLLUSCA\",\"class_name\":\"GASTROPODA\",\"order_name\":\"STYLOMMATOPHORA\",\"family_name\":\"VALLONIIDAE\",\"genus_name\":\"Acanthinula\",\"scientific_name\":\"Acanthinula spinifera\",\"infra_rank\":null,\"infra_name\":null,\"population\":null,\"category\":\"DD\"}," +
                "{\"taxonid\":81,\"kingdom_name\":\"ANIMALIA\",\"phylum_name\":\"CHORDATA\",\"class_name\":\"ACTINOPTERYGII\",\"order_name\":\"SALMONIFORMES\",\"family_name\":\"SALMONIDAE\",\"genus_name\":\"Salmo\",\"scientific_name\":\"Salmo ohridanus\",\"infra_rank\":null,\"infra_name\":null,\"population\":null,\"category\":\"VU\"}," +
                "{\"taxonid\":215,\"kingdom_name\":\"ANIMALIA\",\"phylum_name\":\"MOLLUSCA\",\"class_name\":\"GASTROPODA\",\"order_name\":\"ARCHITAENIOGLOSSA\",\"family_name\":\"ACICULIDAE\",\"genus_name\":\"Acicula\",\"scientific_name\":\"Acicula norrisi\",\"infra_rank\":null,\"infra_name\":null,\"population\":null,\"category\":\"VU\"}," +
                "{\"taxonid\":217,\"kingdom_name\":\"ANIMALIA\",\"phylum_name\":\"MOLLUSCA\",\"class_name\":\"GASTROPODA\",\"order_name\":\"ARCHITAENIOGLOSSA\",\"family_name\":\"ACICULIDAE\",\"genus_name\":\"Acicula\",\"scientific_name\":\"Acicula hausdorfi\",\"infra_rank\":null,\"infra_name\":null,\"population\":null,\"category\":\"NT\"}," +
                "{\"taxonid\":224,\"kingdom_name\":\"ANIMALIA\",\"phylum_name\":\"CHORDATA\",\"class_name\":\"ACTINOPTERYGII\",\"order_name\":\"ACIPENSERIFORMES\",\"family_name\":\"ACIPENSERIDAE\",\"genus_name\":\"Acipenser\",\"scientific_name\":\"Acipenser naccarii\",\"infra_rank\":null,\"infra_name\":null,\"population\":null,\"category\":\"CR\"}]}"

        val spec: SpeciesInRegion? = JacksonDeserializer.speciesInRegion().deserialize(validSpeciesJson)

        assertThat(spec?.count).isEqualTo(5)
        assertThat(spec?.result?.size).isEqualTo(5)
        assertThat(spec?.region_identifier).isEqualTo("europe")
        assertThat(spec?.page).isEqualTo(1)

        val spec_1 = spec?.result?.get(0)
        assertThat(spec_1?.taxonid).isEqualTo(59)
        assertThat(spec_1?.category).isEqualTo("DD")
        val spec_5 = spec?.result?.get(4)
        assertThat(spec_5?.taxonid).isEqualTo(224)
        assertThat(spec_5?.category).isEqualTo("CR")
    }


    @Test
    fun `when required fields are missing in species json then empty result will be returned`() {
        val invalidSpeciesJson = "{\"count\":1, \"result\":[{\"taxonid\":\"not a number\"}]}"
        val spec: SpeciesInRegion? = JacksonDeserializer.speciesInRegion().deserialize(invalidSpeciesJson)
        assertThat(spec).isNotNull
        assertThat(spec?.count).isEqualTo(0)
        assertThat(spec?.result?.size).isEqualTo(0)
        assertThat(spec?.page).isEqualTo(0)
        assertThat(spec?.region_identifier).isEmpty()
    }

    @Test
    fun `when measure json is valid then all fields should be filled`() {
        val validMeasureJson = "{\"name\":\"Loxodonta africana\",\"result\":[" +
                "{\"code\":\"1.1\",\"title\":\"Site/area protection\"}," +
                "{\"code\":\"1.2\",\"title\":\"Resource & habitat protection\"}," +
                "{\"code\":\"2.1\",\"title\":\"Site/area management\"}," +
                "{\"code\":\"3.1\",\"title\":\"Species management\"}," +
                "{\"code\":\"3.1.1\",\"title\":\"Harvest management\"}," +
                "{\"code\":\"3.1.2\",\"title\":\"Trade management\"}]}"

        val meas: MeasuresBySpecies? = JacksonDeserializer.measureBySpecies().deserialize(validMeasureJson)

        assertThat(meas?.result?.size).isEqualTo(6)
        assertThat(meas?.name).isEqualTo("Loxodonta africana")

        val meas_1 = meas?.result?.get(0)
        assertThat(meas_1?.code).isEqualTo("1.1")
        assertThat(meas_1?.title).isEqualTo("Site/area protection")
        val meas_6 = meas?.result?.get(5)
        assertThat(meas_6?.code).isEqualTo("3.1.2")
        assertThat(meas_6?.title).isEqualTo("Trade management")
    }


    @Test
    fun `when required fields are missing in measures json then empty result will be returned`() {
        val invalidMeasuresJson = "{ \"result\":[{\"title\":\"random title\"}]}"
        val meas: MeasuresBySpecies? = JacksonDeserializer.measureBySpecies().deserialize(invalidMeasuresJson)
        assertThat(meas).isNotNull
        assertThat(meas?.result?.size).isEqualTo(0)
        assertThat(meas?.name).isEqualTo("")
    }
}