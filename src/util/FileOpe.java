package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.RandomAccessFile;

import exception.*;

public class FileOpe {
	private int NUM_PERSON = 10;	//预设person的个数为10
	private long BLOCKSIZE = 1024*256;	// 预设contents读取的内容大小为0.25MB
	private String[] contents;		//内容块
	
	public String getTextFile(){	//得到conf_textInfo中需要分析的文本文件名
		String fileName = null;
		try
		{
			FileReader fr = new FileReader("conf_textInfo.txt");
			BufferedReader br = new BufferedReader(fr);
			String str = br.readLine();
			String[] arr = str.split("=");	
			if(arr[0].equals("fileName")){
				fileName = arr[1];	//fileName保存需要分析的文本文件
			}
			
			br.close();
			fr.close();
		}catch(Exception ex){
			ExceptionHandler.handler(ex);
		}
		return fileName;
	}
	
	public String[][] getTextPerson(){	//得到conf_textInfo中的需要分析的人物
		String[][] name = new String[NUM_PERSON][];
		int i = 0;
		try{
			FileReader fr = new FileReader("conf_textInfo.txt");
			BufferedReader br = new BufferedReader(fr);
			while(true){		
				String str = br.readLine();
				if(str == null)  break;
				String[] arr = str.split("=");
				if(arr[0].equals("person")){
					name[i] = new String[(arr.length-1)];
					for(int j = 0; j < name[i].length; j++){
						name[i][j] = arr[j+1];
					}
					i++;
				}				
			}
			br.close();
			fr.close();
		}catch(Exception ex){
			ExceptionHandler.handler(ex);
		}
		return name;
	}
	public String[] fileLoader(String fileName){	//得到小说中的内容
		
		File file = new File(fileName);
		long length = file.length();	//得到当前文件的大小
		
		long blockSize = BLOCKSIZE;		//每一块的大小
		int blockNum = (int)(length/blockSize) + 1;	//总共划分的块数
		contents = new String[blockNum];
		for(int i = 0; i < contents.length; i++){
			contents[i] = "";//初始每一块无内容
		}
		try{
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			//FileReader fr = new FileReader(fileName);
			//BufferedReader bf = new BufferedReader(fr);
			//使用BufferedReader的话可以利用read(char[] cbuff,int off, int len)函数或者readLine()函数
			int index = 0;
			byte[] data = new byte[(int) blockSize];			
			int hasRead = 0; 
			
			while(true){
				hasRead = raf.read(data);	
				if(hasRead == -1)	break;
				String str = new String(data,0,hasRead,"GB2312");				
				contents[index] += str;			
				index++;
			}
			raf.close();
		}catch(Exception ex){
			ExceptionHandler.handler(ex);
		}
		return contents;
	}	
	
}
