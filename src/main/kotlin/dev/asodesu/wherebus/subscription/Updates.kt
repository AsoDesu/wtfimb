package dev.asodesu.wherebus.subscription

import dev.asodesu.wherebus.Values
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder

class Updates(val subscription: Subscription) {
    private val updates: MutableList<MessageEmbed> = mutableListOf()
    var vehicleRef: String? = null

    fun addUpdate(update: MessageEmbed) = updates.add(update)

    fun send(channel: MessageChannel) {
        if (updates.isEmpty()) return

        val message = MessageCreateBuilder()
            .addContent("<@${subscription.discordUserId}>")
            .addEmbeds(updates)
        message.addActionRow(subscription.getExternalButtons(vehicleRef))

        channel.sendMessage(message.build()).queue()
    }
}