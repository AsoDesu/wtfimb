package dev.asodesu.wherebus.subscription

import dev.asodesu.wherebus.exceptions.DiscordLinkException
import dev.asodesu.wherebus.stagecoach.StagecoachService
import dev.asodesu.wherebus.stagecoach.getTime
import dev.asodesu.wherebus.stagecoach.parseSeconds
import dev.asodesu.wherebus.stagecoach.schema.Service
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.TemporalAccessor

class Subscription(
    val guildId: String,
    val discordUserId: String,
    val channelId: String,
    val service: ServiceDetail,

    private val jda: JDA,
    private val stagecoach: StagecoachService,
) {
    var assignedVehicleRef: String? = null

    fun query(): MessageCreateData {
        val event = service.getEvent(stagecoach)
        val date = Instant.ofEpochSecond(service.epochSeconds).atZone(ZoneId.systemDefault())

        val monitor = stagecoach.monitorStops(service.stopId, serviceFilter = listOf(service.number))
        val stops = monitor.stopMonitors.stopMonitor?.firstOrNull()
        val call = stops?.monitoredCalls?.monitoredCall?.firstOrNull { call ->
            call.lineRef == service.number
                    && call.direction.equals(service.direction.name, true)
                    && parseSeconds(call.aimedArrivalTime) == service.epochSeconds
        }
        val vehicle = call?.vehicleRef

        var description: String
        val emote: String
        if (call?.expectedArrivalTime == null) {
            emote = ":calendar_spiral:"
            description = ":alarm_clock: **Scheduled Arrival: ** ${getTime(date)}"
        } else {
            val expectedDate = Instant.parse(call.expectedArrivalTime).atZone(ZoneId.systemDefault())
            description = ":alarm_clock: **Expected Arrival: ** ${getTime(expectedDate)}"
            emote = if (vehicle.isNullOrBlank()) {
                ":infinity:"
            } else {
                ":busstop:"
            }
        }

        description += "\n"
        description += getVehicleDescription(vehicle)

        val embed = EmbedBuilder()
            .setTitle("$emote [${getTime(date)}] **${service.number}** | ${event.trip.service.description}")
            .setColor(stagecoach.getStagecoachColor())
            .setDescription(description)
            .setFooter("Data sourced directly from Stagecoach", "https://i.imgur.com/DOAvG3Y.png")
            .setTimestamp(Instant.now())
            .build()

        val message = MessageCreateBuilder()
            .addEmbeds(embed)

        if (!vehicle.isNullOrBlank()) {
            val (_, fleetNo) = vehicle.split("-")
            val button = Button.link("https://wtfimb.asodesu.dev/map?bus=$fleetNo", "View on map")
                .withEmoji(Emoji.fromUnicode("U+1F30D"))
            message.addActionRow(button)
        }

        return message.build()
    }

    private fun getVehicleDescription(vehicleRef: String?): String {
        val noBusDesc = ":bus: **No assigned bus, no additional data**"
        if (vehicleRef.isNullOrBlank()) return noBusDesc

        val (_, fleetNo) = vehicleRef.split("-")
        val vehicleInfo = getVehicleInfo(fleetNo) ?: return noBusDesc
        val lastUpdateMillis = vehicleInfo.updateTime.toLongOrNull() ?: 0

        return buildString {
            appendLine()
            appendLine(":bus: **Assigned Vehicle: ** `$vehicleRef`")
            appendLine("> :hourglass_flowing_sand: **Last Update: ** <t:${lastUpdateMillis / 1000}:R>")
            appendLine("> :round_pushpin: **On route: ** ${vehicleInfo.serviceNumber} | ${vehicleInfo.serviceDescription}")
            appendLine("> :busstop: **Next Stop: ** `TODO: make this work`")
        }
    }

    private fun getVehicleInfo(fleet: String): Service? {
        val info = stagecoach.getVehicle(fleet)
        return info.services.firstOrNull()
    }

    private val guild get() = jda.getGuildById(guildId)
        ?: throw DiscordLinkException("Guild ($guildId) for subscription $discordUserId was not found!")
    private val channel get() = guild.getChannelById(GuildMessageChannel::class.java, channelId)
        ?: throw DiscordLinkException("Channel ($channelId) for subscription $discordUserId was not found!")
    private val member get() = guild.getMemberById(discordUserId)
        ?: throw DiscordLinkException("Member ($guildId) for subscription $discordUserId was not found!")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Subscription
        return discordUserId == other.discordUserId
    }
    override fun hashCode(): Int {
        return discordUserId.hashCode()
    }
}