# DantuiHelper
QQ群内B站动态/直播提醒机器人

### 使用的QQ机器人内核
- JCQ：https://github.com/Meowya/JCQ-CoolQ
- Mirai：https://github.com/mamoe/mirai

### 构建
在确保拥有maven环境之后使用`mvn build`构建，输出的构建jar位于`target`文件夹内，文件名为`sandtechnology.dantui.jar`

### 开始使用
构建之后在命令行中以此命令运行：

`java -jar 构建后的jar名.jar`

然后根据提示填写机器人QQ号和密码以及管理群（需预先加入）和主人账户，之后打开配置文件（config/config.json）进行配置：
```
{
  //机器人QQ号
  "QQ": 123456,
  //机器人QQ号密码，需要更改时请手动清除PasswordMD5和Password项后重新输入
  "Password": "[已自动转换]",
  //订阅配置
  "subscribeConfig": {
    //动态订阅配置
    "subscribeDynamic": {
      //订阅的用户UID，多个用户以“,”分隔
      "123456": [
        //转发动态的QQ群列表，多个QQ群以“,”分隔
        123456
      ],
      //订阅的用户UID，多个用户以“,”分隔
      "123456": [
        //转发动态的QQ群列表，多个QQ群以“,”分隔
        123456
      ]
    },
    "subscribeLiveRoom": {
      //订阅的直播间号码，多个直播间以“,”分隔
      "123456": [
        //开播通知的QQ群群号列表，多个QQ群以“,”分隔
        123456
      ]
    }
  },
  //主人账号，用于执行指令
  "master": 123456,
  //存储的MD5密码
  "PasswordMD5": [],
  //管理群号
  "masterGroup": 123456,
  //是否使用新直播检测API 出错时可用
  "usingLiveNewAPI": false,
  //存储的直播开播时间数据，用于防止重启后反复提醒
  "liveData": {
    "liveStatus": {
      "123456": 12345678
    }
  },
  //机器人模块设置
  "moduleData": {
    "moduleEnablerMap": {
      //复读模块，复读两次后自动复读
      "REPEATER": [
      //启用的QQ群群号列表，多个QQ群以“,”分隔
      123456
      ]
    }
  }
}

```
### 命令帮助
#### 管理命令
- /test 在管理群发送测试消息以测试机器人状态
- /reload 重载配置文件
- /info 查看机器人运行状态
- /stats 查看机器人统计的群聊记录
- /send [QQ群] 向指定QQ群发送消息
- /reply [QQ群] [QQ号] 回复指定QQ群的指定成员的临时会话

注意：
- 在发送消息时回复`cancel`可以取消发送
- 当私聊无法执行指令时，可以在管理群内发送指令进行替代
- 还有部分指令是我自用的，只有加入特定群时才会生效，一般情况下不会影响正常使用

#### 用户命令
- /info 查看机器人运行状态
- /msg [内容] 进行留言