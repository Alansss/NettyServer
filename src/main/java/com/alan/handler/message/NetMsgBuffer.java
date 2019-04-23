package com.alan.handler.message;

import java.util.LinkedList;
import java.util.Queue;

public class NetMsgBuffer
{
	private NetMsgBuffer(){}
	static NetMsgBuffer _instance = null;
	public static NetMsgBuffer GetInstance()
	{
		if(_instance == null)
			_instance = new NetMsgBuffer();
		return _instance;
	}
	
	private Queue<NetMsgBufferNode> queueReceivedBuffer = new LinkedList<NetMsgBufferNode>();
	
	synchronized void PutMsg(NetMsgBufferNode node)
	{
		queueReceivedBuffer.add(node);
	}
	
	public synchronized Queue<NetMsgBufferNode> GetMsgs()
	{
		Queue<NetMsgBufferNode> queueRet = new LinkedList<NetMsgBufferNode>();
		while(queueReceivedBuffer.size() > 0)
		{
			queueRet.add(queueReceivedBuffer.poll());
		}
		return queueRet;
	}
	
	public int GetMsgCount()
	{
		return queueReceivedBuffer.size();
	}
}
