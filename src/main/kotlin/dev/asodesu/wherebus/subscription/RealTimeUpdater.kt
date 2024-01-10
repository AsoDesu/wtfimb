package dev.asodesu.wherebus.subscription

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel

class RealTimeUpdater(private val subscription: Subscription, val channel: MessageChannel) {
}