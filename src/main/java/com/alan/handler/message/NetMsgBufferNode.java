package com.alan.handler.message;

public class NetMsgBufferNode
{
	int nSessionID = 0;
	byte[] arrMsg = null;
	
	public NetMsgBufferNode(int sessionid, byte[] msg)
	{
		nSessionID = sessionid;
		arrMsg = msg;
	}
	
	public int GetSessionID()
	{
		return nSessionID;
	}
	
	public int GetID()
	{
		if(arrMsg == null)
			return 0;
		int id = (arrMsg[Session.ID_POS0]<<24 & 0xFF000000) + (arrMsg[Session.ID_POS1]<<16 & 0x00FF0000) + (arrMsg[Session.ID_POS2]<<8 & 0x0000FF00) + (arrMsg[Session.ID_POS3]<<0 & 0x000000FF);
		return id;
	}
	
	public byte[] GetProtoMsg()
	{
		int nProtoMsgSize = arrMsg.length-Session.MSG_HEAD_SIZE-Session.MSG_TAIL_SIZE;
		byte[] ret = new byte[nProtoMsgSize];
		System.arraycopy(arrMsg, Session.MSG_HEAD_SIZE, ret, 0, nProtoMsgSize);
		return ret;
	}
}
