package exception;

import java.io.BufferedReader;
import java.io.FileReader;

public class ExceptionHandler {
	public static void handler(Exception ex){
		String className = ex.getClass().getName();
		String handlerClassName = null;
		try{
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\LuoWe\\Desktop\\conf_exception.txt"));
			while(true){
				String str = br.readLine();
				if(str == null)  break;
				String[] arr = str.split("=");
				if(arr[0].equals(className)){
					handlerClassName = arr[1];
					break;
				}
			}
			
			Class cls = Class.forName(handlerClassName);
			Object obj = cls.newInstance();
			IExceptionReciever ie = (IExceptionReciever) obj;
			ie.solveException(ex);
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
