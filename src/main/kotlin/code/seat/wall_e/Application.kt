package code.seat.wall_e

import code.seat.wall_e.application.collect.CollectPollution
import code.seat.wall_e.domain.Report
import code.seat.wall_e.infrastructure.marshaller.ReportJsonMarshaller

data class State(val polyline: String, val running: Boolean = false)

data class App(val state: State) {
    private fun printConsole(message: String) = println(message)

    private fun readConsole(): Sequence<String> {
        return generateSequence(::readLine)
    }

    private fun validation(validOptions: Sequence<String>): String {
        return readConsole()
                .takeWhile { validOptions.contains(it) }
                .ifEmpty { sequenceOf(validOptions.last()) }
                .first()
    }

    private fun start(state: State) {
        val reports: List<Report>? = CollectPollution.collect(state.polyline)
        reports?.forEachIndexed { index, report -> printConsole("Report ${index}: ${ReportJsonMarshaller.reportConverter.toJson(report)}") }
    }

    private fun stop() = println("Bye, see you soon! 🦾")

    private fun loop(state: State) {
        val modeMessage =
                """Please, choose one of the following options
            1. ${if (state.running) "Stop ✋🏼" else "Start 🏁"}
            2. Re-route: 📍
            3. Generate report 📝
            > """.trimIndent()

        print(modeMessage)

        when (validation(sequenceOf("1", "2", "3", "4"))) {
            "1" -> {
                if (state.running) {
                    stop()
                } else {
                    start(state)
                    loop(state.copy(running = !state.running))
                }
            }
            "2" -> {
                println("Introduce a new polyline...")
                val polyline = readConsole().ifEmpty { sequenceOf(state.polyline) }.first()
                val newState = state.copy(polyline = polyline, running = true)
                start(newState)
                loop(newState)
            }
            "3" -> {
                start(state)
                loop(state)
            }
            else -> stop()
        }
    }

    fun run() {
        println("Hello! 🤖")
        loop(state)
    }
}

fun main() {
    val defaultPolyline: String = "mpjyHx`i@VjAVKnAh@BHHX@LZR@Bj@Ml@WWc@]w@bAyAfBmCb@o@pLeQfCsDVa@@ODQR}AJ{A?{BGu\n" +
            "AD_@FKb@MTUX]Le@^kBVcAVo@Ta@|EaFh@m@FWaA{DCo@q@mCm@cC{A_GWeA}@sGSeAcA_EOSMa\n" +
            "@}A_GsAwFkAiEoAaFaBoEGo@]_AIWW{AQyAUyBQqAI_BFkEd@aHZcDlAyJLaBPqDDeD?mBEiA}@F]yKWqGSkI\n" +
            "CmCIeZIuZi@_Sw@{WgAoXS{DOcAWq@KQGIFQDGn@Y`@MJEFIHyAVQVOJGHgFRJBBCCSKBcAKoACyA?m@^y\n" +
            "VJmLJ{FGGWq@e@eBIe@Ei@?q@Bk@Hs@Le@Rk@gCuIkJcZsDwLd@g@Oe@o@mB{BgHQYq@qBQYOMSM\n" +
            "GBUBGCYc@E_@H]DWJST?JFFHBDNBJ?LED?LBv@WfAc@@EDGNK|@e@hAa@`Bk@b@OEk@Go@IeACoA@\n" +
            "a@PyB`@yDDc@e@K{Bi@oA_@w@]m@_@]QkBoAwC{BmAeAo@s@uAoB_AaBmAwCa@mAo@iCgAwFg@iD\n" +
            "q@}G[uEU_GBuP@cICmA?eI?qCB{FBkCI}BOyCMiAGcAC{AN{YFqD^}FR}CNu@JcAHu@b@_E`@}DVsB^mBTsAQ\n" +
            "KkCmAg@[YQOIOvAi@[m@e@s@g@GKCKAEJIn@g@GYGIc@ScBoAf@{A`@uAlBfAG`@"

    val state = State(defaultPolyline)
    App(state).run()
}