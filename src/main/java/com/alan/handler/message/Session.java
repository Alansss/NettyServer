package com.alan.handler.message;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/22 13:11
 * @Description: TODO
 */
public class Session
{
    public enum SessionType
    {
        EST_UNKNONW,
        EST_GAMECLENT,
        EST_GAMESERVER,
    };


    public static final int MSG_POS0 = 0;
    public static final int MSG_POS1 = 1;
    public static final int ID_POS0 = 2;
    public static final int ID_POS1 = 3;
    public static final int ID_POS2 = 4;
    public static final int ID_POS3 = 5;

    public static final int MSG_HEAD_SIZE = 6;
    public static final int MSG_TAIL_SIZE = 8;

    public static final int BYTESIZE_WITH_AUTOZIP = 300;

    private int m_nConnectionID = 0;
    private SessionType m_eIdentity = SessionType.EST_UNKNONW;


    private byte[] m_msgBuffer = new byte[6*1024];
    private int m_nCurBufferSize = 0;
    private ChannelHandlerContext ctx = null;

    public Session(int nSessionID, ChannelHandlerContext chc)
    {
        m_nConnectionID = nSessionID;
        ctx = chc;
    }

    public int GetSessionID()
    {
        return m_nConnectionID;
    }

    public boolean IsClosed()
    {
        return false;//todo
    }


    public int GetConnectionID()
    {
        return m_nConnectionID;
    }

    public boolean IsGameClient()
    {
        return m_eIdentity == SessionType.EST_GAMECLENT;
    }

    private boolean hasMsgBuffer()
    {
        return m_nCurBufferSize != 0;
    }

    public ArrayList<byte[]> GetTrueMsg(byte[] msg)
    {
        ArrayList<byte[]> msgList = new ArrayList<byte[]>();
        if(m_nCurBufferSize == 0 && IsValidMsg(msg))
        {
            msgList.add(msg);
            return msgList;
        }

        System.arraycopy(msg, 0, m_msgBuffer, m_nCurBufferSize, msg.length);
        m_nCurBufferSize += msg.length;

        return GetMsg();
    }


    //[0-1 size] [2-5 id] [data] [size-5 size-1 0xBE00DEAD check]
    private static boolean IsValidMsg(byte[] msg)
    {
        if(msg.length < MSG_HEAD_SIZE + MSG_TAIL_SIZE) {
            return false;
        }

        int nSize = (int)((msg[MSG_POS0]<<8 & 0xFF00) + msg[MSG_POS1]);
        if(nSize != msg.length) {
            return false;
        }

        int nCheckPos = nSize - 4;
        if(msg[nCheckPos++] != (byte)0xBE) {
            return false;
        }
        if(msg[nCheckPos++] != (byte)0x00) {
            return false;
        }
        if(msg[nCheckPos++] != (byte)0xDE) {
            return false;
        }
        if(msg[nCheckPos++] != (byte)0xAD) {
            return false;
        }

        int nEncryptionPos = nSize - 8;
        int encryption = msg[nEncryptionPos+0]<<24&0xFF000000 + msg[nEncryptionPos+1]<<16&0x00FF0000 + msg[nEncryptionPos+2]<<8&0x0000FF00 + msg[nEncryptionPos+3]<<0&0x000000FF;


        return true;
    }

    private boolean IsValidMsg(byte[] msg, int bufferBegin, int bufferEnd)
    {
        if(bufferBegin >= bufferEnd) {
            return false;
        }

        if(bufferBegin + MSG_POS1 >= bufferEnd) {
            return false;
        }

        int nSize = ((msg[bufferBegin + MSG_POS0]<<8 & 0xFF00) + (msg[bufferBegin + MSG_POS1] & 0x00FF)) & 0xFFFF;
        if(bufferBegin+nSize > bufferEnd) {
            return false;
        }

        int nCheckPos = bufferBegin + nSize - 4;
        if(msg[nCheckPos++] != (byte)0xBE) {
            return false;
        }
        if(msg[nCheckPos++] != (byte)0x00) {
            return false;
        }
        if(msg[nCheckPos++] != (byte)0xDE) {
            return false;
        }
        if(msg[nCheckPos++] != (byte)0xAD) {
            return false;
        }

        int nEncryptionPos = nSize - 8;
        int encryption = msg[nEncryptionPos+0]<<24&0xFF000000 + msg[nEncryptionPos+1]<<16&0x00FF0000 + msg[nEncryptionPos+2]<<8&0x0000FF00 + msg[nEncryptionPos+3]<<0&0x000000FF;


        return true;
    }

    private void CleanCheck(int msgPosStart, int nSize)
    {
        int nCheckPos = msgPosStart + nSize - 4;
        m_msgBuffer[nCheckPos++] = 0;
        m_msgBuffer[nCheckPos++] = 0;
        m_msgBuffer[nCheckPos++] = 0;
        m_msgBuffer[nCheckPos++] = 0;
    }

    private ArrayList<byte[]> GetMsg()
    {
        ArrayList<byte[]> ret = new ArrayList<byte[]>();

        int nCurStart = 0;
        int nCurEnd = m_nCurBufferSize;

        while(IsValidMsg(m_msgBuffer, nCurStart, nCurEnd))
        {
            int nMsgSize = (int)((m_msgBuffer[nCurStart + MSG_POS0]<<8 & 0xFF00) + (m_msgBuffer[nCurStart + MSG_POS1] & 0xFF));
            byte[] msg = new byte[nMsgSize];
            System.arraycopy(m_msgBuffer, nCurStart, msg, 0, nMsgSize);
            ret.add(msg);

            CleanCheck(nCurStart, nMsgSize);
            nCurStart += nMsgSize;
            m_nCurBufferSize -= nMsgSize;
        }

        if(nCurStart > 0)
        {
            System.arraycopy(m_msgBuffer, nCurStart, m_msgBuffer, 0, m_nCurBufferSize);
        }
        return ret;
    }


    public void SendMsg(int id, byte[] protoBuff)
    {
		/*if(id == MsgID.MSG_VIEWPORTS)//todo test
		{
			try
			{
				byte[] aaZipped = test.gzip(protoBuff);
				int aaLen = protoBuff.length;
				int aaZipeedLen = aaZipped.length;
				protoBuff = aaZipped;
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}*/

        byte[] msg = ProtoBuffToMsgData(id, protoBuff);

        ctx.writeAndFlush(Unpooled.copiedBuffer(msg));
    }

    public static byte[] ProtoBuffToMsgData(int id, byte[] protoBuff) {
        byte[] tmp = protoBuff;
        boolean bAutoZip = false;
        if(protoBuff.length >= BYTESIZE_WITH_AUTOZIP)
        {
            try
            {
                tmp = gzip(protoBuff);
                bAutoZip = true;
            }
            catch(Exception e)
            {
                tmp = protoBuff;
                bAutoZip = false;
            }
        }

        int nSize = tmp.length + MSG_HEAD_SIZE + MSG_TAIL_SIZE;
        byte[] msg = new byte[nSize];
        System.arraycopy(tmp, 0, msg, MSG_HEAD_SIZE, tmp.length);

        msg[MSG_POS0] = (byte)(nSize>>8 & 0xFF);
        msg[MSG_POS1] = (byte)(nSize>>0 & 0xFF);

        msg[ID_POS0] = (byte)(id>>24 & 0xFF);
        msg[ID_POS1] = (byte)(id>>16 & 0xFF);
        msg[ID_POS2] = (byte)(id>>8  & 0xFF);
        msg[ID_POS3] = (byte)(id>>0  & 0xFF);

        int nCheckPos = msg.length - 4;
        msg[nCheckPos++] = (byte)0xBE;
        msg[nCheckPos++] = (byte)0x00;
        msg[nCheckPos++] = (byte)0xDE;
        msg[nCheckPos++] = (byte)0xAD;

        int nEncryptionPos = msg.length - 8;
        int nFlagPos = msg.length - 8;
        msg[nFlagPos] = (byte) (bAutoZip ? (msg[nFlagPos]|0x01) : (msg[nFlagPos]&0xFE));

        return msg;
    }

    private static byte[] gzip(byte[] content) throws IOException {
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        GZIPOutputStream gos=new GZIPOutputStream(output);

        ByteArrayInputStream bais=new ByteArrayInputStream(content);
        byte[ ] buffer=new byte[6*1024];
        int n;
        while((n=bais.read(buffer))!=-1){
            gos.write(buffer, 0, n);
        }
        gos.flush();
        gos.close();
        return output.toByteArray();
    }

    private static byte[] unGzip(byte[] content) throws IOException{
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        GZIPInputStream gis=new GZIPInputStream(new ByteArrayInputStream(content));
        byte[] buffer=new byte[6*1024];
        int n;
        while((n=gis.read(buffer))!=-1){
            output.write(buffer, 0, n);
        }

        return output.toByteArray();
    }

	public static void main(String[] args) throws Exception
	{
		Session session = new Session(0, null);

		byte[] msgs = new byte[1024*50];
		int nSize = 0;
		int nTestCount = 50;
		for(int i=0; i<nTestCount; i++)
		{
			byte[] msg = ProtoBuffToMsgData(i, String.valueOf(new Random().nextInt(1024)).getBytes());
			System.arraycopy(msg, 0, msgs, nSize, msg.length);
			nSize += msg.length;
		}

		ArrayList<byte[]> listMsgs = new ArrayList<>();

		boolean bDebug = false;
		if(bDebug)
		{
			listMsgs.add(msgs);
		}
		else
		{
			for(int i=0; i<nSize;)
			{
				int length = new Random().nextInt(20);
				if(i+length > nSize)
				{
					length = nSize-i;
				}
				byte[] tmp = new byte[length];
				System.arraycopy(msgs, i, tmp, 0, length);
				i += length;

				listMsgs.add(tmp);

				if(i >= nSize) {
                    break;
                }
			}
		}


		for(int i=0; i<listMsgs.size(); i++)
		{
			ArrayList<byte[]> listTrueMsg = session.GetTrueMsg(listMsgs.get(i));
			for(int j=0; j<listTrueMsg.size(); j++)
			{
				byte[] cur = listTrueMsg.get(j);
				System.out.println("id:" + ((cur[ID_POS0]<<24 & 0xFF000000) + (cur[ID_POS1]<<16 & 0x00FF0000) + (cur[ID_POS2]<<8 & 0x0000FF00) + (cur[ID_POS3]<<0)));
			}
		}
    }
}