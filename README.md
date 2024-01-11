# Where the "heck" is my bus?

wtfimb is a Discord Bot & Web App which provides you with updates and additional information
about a bus trip, using Stagecoach's services

###### Note: Stagecoach's Vehicle tracking API likes to go down a lot, so don't be surprised if this is broken

### TODO:
 - [x] Real-time update pings
 - [ ] Save subscriptions to a database and load them on start
 - [ ] Move trip selection to a dropdown menu & buttons
 - [ ] Add a query command for any bus trip, not just the subscribed one

### How to use the bot

1. Run `/stagecoach subscribe` with your stop, and the bus trip you're taking
2. You will see the current info that is available about that trip
3. As more information becomes available you will be pinged in the channel you ran the command
   with updates
4. Running `/stagecoach query` will query the current info, and offer you a button to see
   your bus on a map!
5. Finally, once you're all done, run `/stagecoach unsubscribe` to stop receiving updates

### FAQ
**But doesn't the stagecoach app do this?**\
Yes, but (at least for my area) it doesn't offer many real-time updates (the map only updates when
you move it), and the real-time updates is does offer, is only for once your bus route has started.
So if you're waiting at the start of a route, you got no info.

**How does it work?**\
It takes info from several sources that can be found on the stagecoach website and app, and
checks for updates, and sends them too you!