package sandtechnology.utils

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.TempMessageEvent
import sandtechnology.holder.ReadOnlyMessage

class AsyncEvents {

    companion object {
        fun registerEventsAsync(bot: Bot) {
            bot.subscribeAlways<GroupMessageEvent> {
                sandtechnology.common.Listener.onGroupMsg(it.sender.id, it.group.id, ReadOnlyMessage(it.message))
            }
            bot.subscribeAlways<FriendMessageEvent> {
                sandtechnology.common.Listener.onPrivateMsg(it.sender.id, ReadOnlyMessage(it.message))
            }
            bot.subscribeAlways<TempMessageEvent> {
                sandtechnology.common.Listener.onTempMsg(it.group.id, it.sender.id, ReadOnlyMessage(it.message))
            }
        }
    }


}