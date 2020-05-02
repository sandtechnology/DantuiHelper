package sandtechnology.utils

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessage
import net.mamoe.mirai.message.GroupMessage
import net.mamoe.mirai.message.TempMessage
import sandtechnology.holder.ReadOnlyMessage

class AsyncEvents {

    companion object {
        fun registerEventsAsync(bot: Bot) {
            bot.subscribeAlways<GroupMessage> {
                sandtechnology.common.Listener.onGroupMsg(it.sender.id, it.group.id, ReadOnlyMessage(it.message))
            }
            bot.subscribeAlways<FriendMessage> {
                sandtechnology.common.Listener.onPrivateMsg(it.sender.id, ReadOnlyMessage(it.message))
            }
            bot.subscribeAlways<TempMessage> {
                sandtechnology.common.Listener.onTempMsg(it.group.id, it.sender.id, ReadOnlyMessage(it.message))
            }
        }
    }


}