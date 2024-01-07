package dev.asodesu.wherebus.subscription

import dev.asodesu.wherebus.exceptions.DiscordLinkException
import dev.asodesu.wherebus.stagecoach.StagecoachService
import dev.asodesu.wherebus.stagecoach.getTime
import dev.asodesu.wherebus.stagecoach.parseSeconds
import dev.asodesu.wherebus.stagecoach.schema.Service
import dev.asodesu.wherebus.stagecoach.schema.ServiceTimetableResponse
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
    val stagecoach: StagecoachService,
) {
    var timetable: ServiceTimetableResponse? = null
    var assignedVehicleRef: String? = null

    private val queries = SubscriptionQueries(this)

    fun query() = queries.query()

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