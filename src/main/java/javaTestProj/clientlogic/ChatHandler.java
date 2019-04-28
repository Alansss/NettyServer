package javaTestProj.clientlogic;

import com.alan.proto.MsgHello;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/28 15:30
 * @Description: TODO
 */
public class ChatHandler {

    public void handleMsg(MsgHello.Msg_Hello message) {

        System.out.println(message.getMsg());

    }


}
