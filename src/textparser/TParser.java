package textparser;

import util.*;

import java.security.Key;
import java.util.Vector;

import exception.*;


public class TParser {
	public Vector<ObjectInfo> Vpersons = new Vector<ObjectInfo>();	//定义容器Vpersons存放十个人物的基本信息
	
	public TParser(String[][] names){	//初始化接受从getTextPerson()的返回值
		for(int i = 0; i < names.length; i++){
			ObjectInfo oi = new ObjectInfo(names[i]);
			Vpersons.add(oi);		
		}
	}

	public int countTimes(ObjectInfo person,String contents){
		int index = 0;	//初始搜索位置为0
		int times = 0;
		String[] key = new String[person.key.length];
		for(int i =0; i < key.length; i++){
			key[i] = person.key[i];
			
		}
		
		int position = 0;
		int temp1 = 0;
		int temp2 = 0;
		while(true){	//统计个数
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
			if(index == -1) break;	//表明从index开始，contents中没有关键字key了
			times++;
			index += key[temp2].length();//index往后移动关键字的大小
		}
		
		return times;					
	}	
	
	public ObjectInfo counter(ObjectInfo person,String[] contents){		//统计频率和最前最后索引
		
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
			
			index = 0;//每次从内容块位置为0
			//统计第一次出现的位置
			if(flag == true){
				position = index;
				index = contents[i].indexOf(key[0], index);
				for(int j = 1; j < key.length; j++){
					temp1 = contents[i].indexOf(key[j], position);
					if(temp1 == -1)  continue;
					else if(index > temp1 || index == -1){
						index = temp1;
						temp2 = j;	//关键字出现的最小位置数组下标
					}
				}
				if(index != -1){
					person.firstIndex = index + i*blockLength;
					flag = false;
				}
			}

			//统计次数
			person.times += countTimes(person, contents[i]);
			
			
			int tempLastIndex = 0;
			for(int k = 1; k < key.length; k++){
				temp3 = contents[i].lastIndexOf(key[0]);	//temp3存放的是key[0]最后出现的位置
				if(contents[i].lastIndexOf(key[k]) == -1)  continue;
				else if(contents[i].lastIndexOf(key[k]) > index){	//当前块有关键字，则最该块最后一次出现key的位置变为person.lastIndex
					tempLastIndex = contents[i].lastIndexOf(key[k]);
				}
			}
			if(tempLastIndex != 0)
				person.lastIndex = tempLastIndex + i*blockLength;
			
		}
		
		
		return person;
	}
	public int getSpan(ObjectInfo person){		//计算跨度
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
	public Vector<ObjectInfo> sortByTimes(Vector<ObjectInfo> v){	//排序函数
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
	//亲密人物标准，（模拟某段时间或者事件中）半径以内出现一次则亲密度加1
	public ObjectInfo getIntimacy(ObjectInfo person,String[] contents){	//对person的邻居进行亲密度排序
		int r = 100;		//设置半径,在半径以内出现的都为亲密人物
		Vector<ObjectInfo> Vnext = person.nextKey;	//Vnext为该人物的相邻人物的集合
		
		String str;
		int times;
		boolean flag;
		int temp1 = 0;
		int temp2 = 0;
		int tempIndex = 0;
		
		int index = 0;
		int position = 0;
		
		for(int j = 0; j < Vnext.size(); j++){	//邻居数
			times = 0;
						
			for(int i = 0; i < contents.length; i ++){	//内容块数	
				flag = true;
				position = r;
				int start = 0; 
				int end = 2*r;		//初始化检索位置
				
				while(true){	//一块一块的统计出现的次数
					tempIndex = contents[i].indexOf(person.key[0], position);
					//找出这一块内容中出现人物的最小位置
					for(int k = 1; k < person.key.length; k++){		//temp1表示key[k]的在contents[i]的位置,temp2表示最小位置的key[k]的下标
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
					times += countTimes(Vnext.get(j),str);	//计算str中有多少个Vnext.get(j)的人物名称
					position += 2*r;	//每次统计后position往后移动直径2r,这样就不会重复统计了
				}		
			}
			Vnext.get(j).times = times;
		}
		sortByTimes(Vnext);	
		return person;	
	}

}