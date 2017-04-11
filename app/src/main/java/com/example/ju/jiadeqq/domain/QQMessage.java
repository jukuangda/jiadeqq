package com.example.ju.jiadeqq.domain;

import com.example.ju.jiadeqq.util.MyTime;

/**
 * ����Э�鶨�弴ʱͨѶ��ʵ���࣬�����������ݽ���
 * 
 * @author ZHY
 * 
 */
public class QQMessage extends ProtocalObj {
	public String type = QQMessageType.MSG_TYPE_CHAT_P2P;// ���͵����� chat login
	public long from = 0;// ������ account
	public String fromNick = "";// �ǳ�
	public int fromAvatar = 1;// ͷ��
	public long to = 0; // ������ account
	public String content = ""; // ��Ϣ������ Լ��?
	public String sendTime = MyTime.geTime(); // ����ʱ��
}
