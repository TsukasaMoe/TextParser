package util;

import java.util.Vector;

public class ObjectInfo {
	public String[] key;		//主关键字
	public int times;
	public int firstIndex;
	public int lastIndex;
	public Vector<ObjectInfo> nextKey = new Vector<ObjectInfo>();	//与之相邻的key
	
	public ObjectInfo(String[] names){	//传入一个人物的各种称呼，初始化一个人
		key = new String[names.length];
		for(int i = 0; i < key.length; i++)
			key[i] = names[i];
		times = 0;
		firstIndex = lastIndex = 0;
		
	}
	public ObjectInfo setInfo(String[][] others){	//设置与当前对象相关联的人物表，初始计数为空
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
