package com.alan.handler.gamelogic;

import com.alan.annotation.MessageCommandAnnotation;
import com.alan.handler.message.SessionsManager;
import com.alan.proto.CmdHello;
import com.alan.proto.MsgHello;
import com.google.inject.Inject;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/19 14:05
 * @Description: TODO
 */
public class BattleHandler {

    @Inject
    private BattleService battleService;

    @MessageCommandAnnotation(command = 333)
    public void channelRead(int sessionID, byte[] msg) throws InvalidProtocolBufferException {

        CmdHello.Cmd_Hello cmd_hello = CmdHello.Cmd_Hello.parseFrom(msg);

        System.out.println(cmd_hello.getMsg());

        battleService.doBattle(cmd_hello.getMsg());

        SessionsManager.GetInstance().SendMsgToClient(sessionID,333,
                MsgHello.Msg_Hello.newBuilder().setMsg("for server bbbbb").build());

    }

}
