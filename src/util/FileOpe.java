package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.RandomAccessFile;

import exception.*;

public class FileOpe {
	private int NUM_PERSON = 10;	//Ԥ��person�ĸ���Ϊ10
	private long BLOCKSIZE = 1024*256;	// Ԥ��contents��ȡ�����ݴ�СΪ0.25MB
	private String[] contents;		//���ݿ�
	
	public String getTextFile(){	//�õ�conf_textInfo����Ҫ�������ı��ļ���
		String fileName = null;
		try
		{
			FileReader fr = new FileReader("conf_textInfo.txt");
			BufferedReader br = new BufferedReader(fr);
			String str = br.readLine();
			String[] arr = str.split("=");	
			if(arr[0].equals("fileName")){
				fileName = arr[1];	//fileName������Ҫ�������ı��ļ�
			}
			
			br.close();
			fr.close();
		}catch(Exception ex){
			ExceptionHandler.handler(ex);
		}
		return fileName;
	}
	
	public String[][] getTextPerson(){	//�õ�conf_textInfo�е���Ҫ����������
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
	public String[] fileLoader(String fileName){	//�õ�С˵�е�����
		
		File file = new File(fileName);
		long length = file.length();	//�õ���ǰ�ļ��Ĵ�С
		
		long blockSize = BLOCKSIZE;		//ÿһ��Ĵ�С
		int blockNum = (int)(length/blockSize) + 1;	//�ܹ����ֵĿ���
		contents = new String[blockNum];
		for(int i = 0; i < contents.length; i++){
			contents[i] = "";//��ʼÿһ��������
		}
		try{
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			//FileReader fr = new FileReader(fileName);
			//BufferedReader bf = new BufferedReader(fr);
			//ʹ��BufferedReader�Ļ���������read(char[] cbuff,int off, int len)��������readLine()����
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
