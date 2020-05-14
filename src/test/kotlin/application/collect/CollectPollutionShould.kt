package application.collect

import code.seat.wall_e.application.collect.CollectPollution
import code.seat.wall_e.domain.Report
import com.google.maps.model.LatLng
import org.junit.Assert.*
import org.spekframework.spek2.Spek

object CollectPollutionShould :  Spek({
    group("CollectPollutionShould") {
        test("Return the correct reports for the given polyline") {
            val polyline: String = "mpjyHx`i@VjAVKnAh@BHHX@LZR@Bj@Ml@WWc@]w@bAyAfBmCb@o@pLeQfCsDVa@@ODQR}AJ{A?{BGu\n" +
                    "AD_@FKb@MTUX]Le@^kBVcAVo@Ta@|EaFh@m@FWaA{DCo@q@mCm@cC{A_GWeA}@sGSeAcA_EOSMa\n" +
                    "@}A_GsAwFkAiEoAaFaBoEGo@]_AIWW{AQyAUyBQqAI_BFkEd@aHZcDlAyJLaBPqDDeD?mBEiA}@F]yKWqGSkI\n" +
                    "CmCIeZIuZi@_Sw@{WgAoXS{DOcAWq@KQGIFQDGn@Y`@MJEFIHyAVQVOJGHgFRJBBCCSKBcAKoACyA?m@^y\n" +
                    "VJmLJ{FGGWq@e@eBIe@Ei@?q@Bk@Hs@Le@Rk@gCuIkJcZsDwLd@g@Oe@o@mB{BgHQYq@qBQYOMSM\n" +
                    "GBUBGCYc@E_@H]DWJST?JFFHBDNBJ?LED?LBv@WfAc@@EDGNK|@e@hAa@`Bk@b@OEk@Go@IeACoA@\n" +
                    "a@PyB`@yDDc@e@K{Bi@oA_@w@]m@_@]QkBoAwC{BmAeAo@s@uAoB_AaBmAwCa@mAo@iCgAwFg@iD\n" +
                    "q@}G[uEU_GBuP@cICmA?eI?qCB{FBkCI}BOyCMiAGcAC{AN{YFqD^}FR}CNu@JcAHu@b@_E`@}DVsB^mBTsAQ\n" +
                    "KkCmAg@[YQOIOvAi@[m@e@s@g@GKCKAEJIn@g@GYGIc@ScBoAf@{A`@uAlBfAG`@"
            val locationsExpected: List<LatLng> = listOf(
                    LatLng(51.50928738,-0.21710272),
                    LatLng(51.50428359,-0.19494644),
                    LatLng(51.52823557,-0.19446686),
                    LatLng(51.52750011,-0.17693331),
                    LatLng(51.54696252,-0.16977966),
                    LatLng(51.56530778,-0.16828255)
            )
            val reports: List<Report> = CollectPollution.collect(polyline)!!
            val locationsReported: List<LatLng> = reports.map { report -> report.location }

            assert(reports.size == 6)
            assertEquals(locationsExpected.toString(), locationsReported.toString())
        }

        test("Return the correct reports for the given polyline (stations path)") {
            val polyline = "s~kyHvtUtGcDsAqI}G~CaBdINrG`BbEvDrA`EtK~FhKnCtLzDtHdH~BFtH~@vJxGtQhGnQrF`TlKpL~GtErKoEgEug@w@sGyHkZkCiJkD{Kf@}n@"
            val locationsExpected: List<LatLng> = listOf(
                    LatLng(51.51098790,-0.11482560),
                    LatLng(51.50230312,-0.13916796),
                    LatLng(51.50099777,-0.14106470),
                    LatLng(51.50078280,-0.11844716)
            )
            val reports: List<Report> = CollectPollution.collect(polyline)!!
            val locationsReported: List<LatLng> = reports.map { report -> report.location }

            assert(reports.size == 4)
            assertEquals(reports[0].source, "Temple Station")
            assertEquals(reports[2].source, "Buckingham Palace")
            assertEquals(locationsExpected.toString(), locationsReported.toString())
        }
    }
})
