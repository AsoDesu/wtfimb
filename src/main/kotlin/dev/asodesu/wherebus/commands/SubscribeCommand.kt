package dev.asodesu.wherebus.commands

import dev.asodesu.wherebus.stagecoach.StagecoachService
import dev.asodesu.wherebus.stagecoach.getTime
import dev.asodesu.wherebus.stagecoach.parseSeconds
import dev.asodesu.wherebus.stagecoach.schema.Direction
import dev.asodesu.wherebus.stagecoach.schema.Event
import dev.asodesu.wherebus.subscription.ServiceDetail
import dev.asodesu.wherebus.subscription.SubscriptionManager
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId
import net.dv8tion.jda.api.interactions.commands.Command.Choice as Choice

@Component
class SubscribeCommand(val stagecoach: StagecoachService) : Command {
    override val name = "subscribe"

    @Autowired
    lateinit var subscriptions: SubscriptionManager

    override fun create() = SubcommandData(name, "Subscribes to a service arrival")
        .addOption(OptionType.STRING, "stop", "The stop you're bus is stopping", true, true)
        .addOption(OptionType.STRING, "trip", "The trip you're taking", true, true)

    override fun handle(evt: SlashCommandInteractionEvent) {
        val stop = evt.getOption("stop")
            ?: return evt.reply("You must provide a stop!").setEphemeral(true).queue()
        val trip = evt.getOption("trip")
            ?: return evt.reply("You must provide a trip!").setEphemeral(true).queue()
        val service = decodeServiceTime(trip.asString, stop.asString)

        val subscription = subscriptions.newSubscription(evt.user, evt.channel.asGuildMessageChannel(), service)

        val hook = evt.deferReply(true).complete()
        val message = subscription.query()
        hook.sendMessage(message).queue()
    }

    override fun complete(evt: CommandAutoCompleteInteractionEvent) {
        val focused = evt.focusedOption
        val choices = when(focused.name) {
            "stop" -> {
                if (focused.value.isBlank()) listOf()
                else {
                    val searchResults = stagecoach.searchStops(focused.value)
                    searchResults.locations.location.map {
                        val label = it.stopData.stopLabel
                        Choice("${it.fullText} ($label)", label)
                    }
                }
            }
            "trip" -> {
                val stopId = evt.getOption("stop")?.asString ?: ""
                if (stopId.isBlank()) listOf()
                else {
                    val calls = stagecoach.stopEvents(stopId, 25)
                    val events = calls.events?.event ?: listOf()
                    events.map { event ->
                        val date = Instant.parse(event.scheduledArrivalTime.value).atZone(ZoneId.systemDefault())
                        val time = getTime(date)
                        val value = encodeServiceTime(event)
                        Choice("[$time] ${event.trip.service.serviceNumber} to ${event.trip.destinationBoard}", value)
                    }
                }
            }
            else -> listOf()
        }

        evt.replyChoices(choices).queue()
    }

    private fun encodeServiceTime(event: Event): String {
        val trip = event.trip
        val service = trip.service.serviceNumber + trip.service.direction[0].uppercase()
        val time = parseSeconds(event.scheduledArrivalTime.value)
        return "$service-$time"
    }
    private fun decodeServiceTime(string: String, stopId: String): ServiceDetail {
        val (service, time) = string.split("-")
        val seconds = time.toLongOrNull() ?: 0
        val direction = Direction.fromChar(service.last())!!
        val serviceNumber = service.dropLast(1)
        return ServiceDetail(serviceNumber, direction, seconds, stopId)
    }

    override val parentCommand = Commands.stagecoach
}