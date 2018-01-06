package util;

import java.util.Vector;

public class ObjectInfo {
	public String[] key;		//���ؼ���
	public int times;
	public int firstIndex;
	public int lastIndex;
	public Vector<ObjectInfo> nextKey = new Vector<ObjectInfo>();	//��֮���ڵ�key
	
	public ObjectInfo(String[] names){	//����һ������ĸ��ֳƺ�����ʼ��һ����
		key = new String[names.length];
		for(int i = 0; i < key.length; i++)
			key[i] = names[i];
		times = 0;
		firstIndex = lastIndex = 0;
		
	}
	public ObjectInfo setInfo(String[][] others){	//�����뵱ǰ������������������ʼ����Ϊ��
		ObjectInfo next;
		for(int i = 0; i < others.length; i++){
			
			if(this.key[0] != others[i][0]){			
				next = new ObjectInfo(others[i]);
				this.nextKey.add(next);				
			}			
		}	
		return this;
	}

}
