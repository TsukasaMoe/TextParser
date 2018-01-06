package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import textparser.TParser;
import util.FileOpe;
import util.ObjectInfo;

/*
 * (1)统计每个人在小说中出现的次数并排序。
   (2)统计每个人在小说中出现的篇幅跨度（第一次出现距最后一次出现的篇幅）并排序。
   (3) 如果两人在相距较短的一段文字中出现，我们认为两人有关系，距离越短，关系越近。自行设计统计标准，找出10个人中：
    1)关系最紧密的两个人，关系最不紧密的两个人。
    2)输入一个人名，列出该人和其他人关系的紧密程度排名。
*/
public class MainFrame extends JFrame implements ActionListener{
	/**********************定义控件***********************/
	
	private JButton TimesButton = new JButton("次数统计");
	private JButton SpanButton = new JButton("跨度统计");
	private JButton IntimacyButton = new JButton("亲密度分析");
	private JTextArea jta = new JTextArea(20,36);
	
	private JScrollPane sp = new JScrollPane(jta);
	private JPanel panel = new JPanel();
	
	/**********************界面初始化***********************/
	
	public MainFrame(){		
		super.setTitle("MyScanner");
		this.setStyle();
		panel.add(TimesButton);
		TimesButton.setIcon(new ImageIcon("timesIcon.jpg"));
		
		panel.add(SpanButton);
		SpanButton.setIcon(new ImageIcon("spanIcon.jpg"));

		panel.add(IntimacyButton);
		IntimacyButton.setIcon(new ImageIcon("intimacyIcon.jpg"));
		
		jta.setSelectedTextColor(Color.RED);
		jta.setLineWrap(true);       
		jta.setWrapStyleWord(true); 
		
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		panel.add(sp);
		this.add(panel);
		           
		
		Toolkit tk = Toolkit.getDefaultToolkit();
        int screenWidth = tk.getScreenSize().width; 
        int screenHeight = tk.getScreenSize().height;
        int width = 600;
        int height = 450;
        
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width,height);
		this.setLocation((screenWidth - width)/2, (screenHeight - height)/2);
		this.setResizable(false);
		
		
	/**********************增加监听***********************/
		
		TimesButton.addActionListener(this);
		SpanButton.addActionListener(this);
		IntimacyButton.addActionListener(this);
	}
	
	public void setStyle(){
		UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		try{
			UIManager.setLookAndFeel(infos[1].getClassName());
			SwingUtilities.updateComponentTreeUI(MainFrame.this);
		}catch(Exception ex){
			
		}
	}

	public void actionPerformed(ActionEvent e){
		FileOpe fo = new FileOpe();
		String fileName = fo.getTextFile();
		String[][] names = fo.getTextPerson();//获得人名

		String[] contents = fo.fileLoader(fileName);//获得文本内容块
		
		ObjectInfo[] oi = new ObjectInfo[names.length];	//构造person.lenth个对象
		for(int i = 0; i < oi.length; i++){	//每个人进行初始化times,firstIndex等以及添加他们的邻居对象
			oi[i] = new ObjectInfo(names[i]);
			oi[i].setInfo(names);
		}


		TParser parser = new TParser(names);	//初始化TParser中的对象容器Vperson
		for(int i = 0; i < oi.length; i++){
			parser.counter(oi[i], contents);	//分别统计每个对象的次数以及个数
			
		}
		
		if(e.getSource().equals(TimesButton)){
			jta.append("\n*****************************出现次数统计********************************"+"\n");
			for(int i = 0; i < oi.length; i ++){
				jta.append(oi[i].key[0] + "------->" + oi[i].times+"\n");
			}

		}else if(e.getSource().equals(SpanButton)){
			jta.append("\n*****************************跨度统计结果********************************"+"\n");
			for(int i = 0; i < oi.length; i++){
				jta.append(oi[i].key[0]+"------->"+parser.getSpan(oi[i])+"\n");
				
			}
			
		}else if(e.getSource().equals(IntimacyButton)){
			
			for(int i = 0; i < oi.length; i++){
				parser.getIntimacy(oi[i], contents);
			}
			jta.append("\n*************************关系最不亲密的两个人**************************"+"\n");
			ObjectInfo temp = oi[0].nextKey.get(0);
			int j = 0;
			temp = parser.getMaxTimesPerson(oi[0].nextKey);
			jta.append(oi[j].key[0]+"和"+temp.key[0]+"------->"+temp.times+"\n");
			
			temp = oi[0].nextKey.get(8);	//与第一个人物亲密的最低的人为temp
			j = 8;
			jta.append("\n***********************关系最不亲密的两个人****************************"+"\n");
			temp = parser.getMinTimesPerson(oi[0].nextKey);
			jta.append(oi[j].key[0]+"和"+temp.key[0]+"------->"+temp.times+"\n");
			
			String str = JOptionPane.showInputDialog("请输入想要查询的人物:"+"\n");
			boolean flag = true;
			for(int i = 0; i < oi.length; i++){
				if(oi[i].key[0].equals(str)){
					flag = false;
					jta.append("\n***********************其他人与"+oi[i].key[0]+"亲密度排序情况*********************\n");
					for(j = 0; j < oi[i].nextKey.size(); j++){
						jta.append(oi[i].nextKey.get(j).key[0] +"------->"+ oi[i].nextKey.get(j).times+"\n");
					}
				}
			}
			if(flag == true){
				jta.append("小说中无此人，查找失败！"+"\n");
			}
		}
	}
}
