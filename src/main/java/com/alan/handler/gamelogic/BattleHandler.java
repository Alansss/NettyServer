package com.alan.handler.gamelogic;

import com.alan.annotation.MessageCommandAnnotation;
import com.alan.handler.message.SessionsManager;
import com.alan.proto.CmdConst.Cmd_ID;
import com.alan.proto.CmdHello;
import com.alan.proto.MsgConst.Msg_ID;
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

    @MessageCommandAnnotation(command = Cmd_ID.DOBATTLE_VALUE)
    public void channelRead(int sessionID, byte[] msg) throws InvalidProtocolBufferException {

        CmdHello.Cmd_Hello cmd_hello = CmdHello.Cmd_Hello.parseFrom(msg);

        System.out.println(cmd_hello.getMsg());

        battleService.doBattle(cmd_hello.getMsg());

        SessionsManager.GetInstance().SendMsgToClient(sessionID, Msg_ID.DOBATTLE_VALUE,
                MsgHello.Msg_Hello.newBuilder().setMsg("for server bbbbb").build());

    }

}
