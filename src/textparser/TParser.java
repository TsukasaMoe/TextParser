package textparser;

import util.*;

import java.security.Key;
import java.util.Vector;

import exception.*;


public class TParser {
	public Vector<ObjectInfo> Vpersons = new Vector<ObjectInfo>();	//��������Vpersons���ʮ������Ļ�����Ϣ
	
	public TParser(String[][] names){	//��ʼ�����ܴ�getTextPerson()�ķ���ֵ
		for(int i = 0; i < names.length; i++){
			ObjectInfo oi = new ObjectInfo(names[i]);
			Vpersons.add(oi);		
		}
	}

	public int countTimes(ObjectInfo person,String contents){
		int index = 0;	//��ʼ����λ��Ϊ0
		int times = 0;
		String[] key = new String[person.key.length];
		for(int i =0; i < key.length; i++){
			key[i] = person.key[i];
			
		}
		
		int position = 0;
		int temp1 = 0;
		int temp2 = 0;
		while(true){	//ͳ�Ƹ���
			position = index;
			index = contents.indexOf(key[0], index);
			for(int i = 1; i < key.length; i++){
				temp1 = contents.indexOf(key[i], position);
				if(temp1 == -1)  continue;
				else if(index > temp1 || index == -1){
					index = temp1;
					temp2 = i;
				}
			}
			if(index == -1) break;	//������index��ʼ��contents��û�йؼ���key��
			times++;
			index += key[temp2].length();//index�����ƶ��ؼ��ֵĴ�С
		}
		
		return times;					
	}	
	
	public ObjectInfo counter(ObjectInfo person,String[] contents){		//ͳ��Ƶ�ʺ���ǰ�������
		
		String[] key = new String[person.key.length];
		for(int i =0; i < person.key.length; i++){
			key[i] = person.key[i];
			//System.out.println(key[i]);
		}
		
		int index = 0;
		int temp1 = 0;
		int temp2 = 0;
		int temp3 = 0;
		boolean flag = true;
		int position = 0;
		int blockLength = contents[0].length();
		for(int i = 0; i < contents.length; i++){	
			
			index = 0;//ÿ�δ����ݿ�λ��Ϊ0
			//ͳ�Ƶ�һ�γ��ֵ�λ��
			if(flag == true){
				position = index;
				index = contents[i].indexOf(key[0], index);
				for(int j = 1; j < key.length; j++){
					temp1 = contents[i].indexOf(key[j], position);
					if(temp1 == -1)  continue;
					else if(index > temp1 || index == -1){
						index = temp1;
						temp2 = j;	//�ؼ��ֳ��ֵ���Сλ�������±�
					}
				}
				if(index != -1){
					person.firstIndex = index + i*blockLength;
					flag = false;
				}
			}

			//ͳ�ƴ���
			person.times += countTimes(person, contents[i]);
			
			
			int tempLastIndex = 0;
			for(int k = 1; k < key.length; k++){
				temp3 = contents[i].lastIndexOf(key[0]);	//temp3��ŵ���key[0]�����ֵ�λ��
				if(contents[i].lastIndexOf(key[k]) == -1)  continue;
				else if(contents[i].lastIndexOf(key[k]) > index){	//��ǰ���йؼ��֣�����ÿ����һ�γ���key��λ�ñ�Ϊperson.lastIndex
					tempLastIndex = contents[i].lastIndexOf(key[k]);
				}
			}
			if(tempLastIndex != 0)
				person.lastIndex = tempLastIndex + i*blockLength;
			
		}
		
		
		return person;
	}
	public int getSpan(ObjectInfo person){		//������
		return person.lastIndex - person.firstIndex;
	}	
	public ObjectInfo getMaxTimesPerson(Vector<ObjectInfo> v){
		ObjectInfo oi = v.get(0);
		for(int i = 1; i < v.size(); i++){
			if(oi.times < v.get(i).times){
				oi = v.get(i);
			}
		}
		return oi;
	}
	
	public ObjectInfo getMinTimesPerson(Vector<ObjectInfo> v){
		ObjectInfo oi = v.get(0);
		
		for(int i = 1; i < v.size(); i++){
			if(oi.times > v.get(i).times){
				oi = v.get(i);			
			}
		}
		return oi;
	}
	public Vector<ObjectInfo> sortByTimes(Vector<ObjectInfo> v){	//������
		ObjectInfo oi;
		int index = 0;
		int j = 0;
		for(int i = 0; i < v.size(); i++){
			oi = v.get(i);
			for(j = i+1; j < v.size(); j++){
				if(oi.times < v.get(j).times){
					oi = v.get(j);
					index = j;				
				}
			}
			if(oi.key[0].equals(v.get(i).key[0]) != true){
				v.add(index, v.get(i));
				v.remove(index+1);
				v.add(i, oi);
				v.remove(i+1);
			}
		}
		return v;
	}
	//���������׼����ģ��ĳ��ʱ������¼��У��뾶���ڳ���һ�������ܶȼ�1
	public ObjectInfo getIntimacy(ObjectInfo person,String[] contents){	//��person���ھӽ������ܶ�����
		int r = 100;		//���ð뾶,�ڰ뾶���ڳ��ֵĶ�Ϊ��������
		Vector<ObjectInfo> Vnext = person.nextKey;	//VnextΪ���������������ļ���
		
		String str;
		int times;
		boolean flag;
		int temp1 = 0;
		int temp2 = 0;
		int tempIndex = 0;
		
		int index = 0;
		int position = 0;
		
		for(int j = 0; j < Vnext.size(); j++){	//�ھ���
			times = 0;
						
			for(int i = 0; i < contents.length; i ++){	//���ݿ���	
				flag = true;
				position = r;
				int start = 0; 
				int end = 2*r;		//��ʼ������λ��
				
				while(true){	//һ��һ���ͳ�Ƴ��ֵĴ���
					tempIndex = contents[i].indexOf(person.key[0], position);
					//�ҳ���һ�������г����������Сλ��
					for(int k = 1; k < person.key.length; k++){		//temp1��ʾkey[k]����contents[i]��λ��,temp2��ʾ��Сλ�õ�key[k]���±�
						temp1 = contents[i].indexOf(person.key[k], position);
						if(temp1 == -1)  continue;
						else if(tempIndex > temp1 || tempIndex == -1){
							tempIndex = temp1;
							temp2 = k;
						}
					}
					
					if(tempIndex == -1){
						break;
					}
					if(contents[i].length() - tempIndex <=r){
						position = tempIndex;
						end = contents[i].length();
						start = position - r;
					}
					else {
						position = tempIndex;
						start = position - r;
						end = position + r;
					}
		
					str = contents[i].substring(start, end);
					times += countTimes(Vnext.get(j),str);	//����str���ж��ٸ�Vnext.get(j)����������
					position += 2*r;	//ÿ��ͳ�ƺ�position�����ƶ�ֱ��2r,�����Ͳ����ظ�ͳ����
				}		
			}
			Vnext.get(j).times = times;
		}
		sortByTimes(Vnext);	
		return person;	
	}

}