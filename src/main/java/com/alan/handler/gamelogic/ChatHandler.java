package com.alan.handler.gamelogic;

import com.alan.annotation.MessageCommandAnnotation;
import com.alan.handler.message.SessionsManager;
import com.alan.proto.CmdConst;
import com.alan.proto.CmdHello;
import com.alan.proto.MsgHello;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/28 11:20
 * @Description: TODO
 */
public class ChatHandler {

    @MessageCommandAnnotation(command = CmdConst.Cmd_ID.CHAT_VALUE)
    public void channelRead2(int sessionID, byte[] msg) throws InvalidProtocolBufferException {

        CmdHello.Cmd_Hello cmd_hello = CmdHello.Cmd_Hello.parseFrom(msg);

        System.out.println(cmd_hello.getMsg());

        SessionsManager.GetInstance().SendMsgToClient(sessionID,CmdConst.Cmd_ID.CHAT_VALUE,
                MsgHello.Msg_Hello.newBuilder().setMsg("for server cccccc").build());

    }

}
