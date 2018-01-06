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
 * (1)ͳ��ÿ������С˵�г��ֵĴ���������
   (2)ͳ��ÿ������С˵�г��ֵ�ƪ����ȣ���һ�γ��־����һ�γ��ֵ�ƪ����������
   (3) ������������϶̵�һ�������г��֣�������Ϊ�����й�ϵ������Խ�̣���ϵԽ�����������ͳ�Ʊ�׼���ҳ�10�����У�
    1)��ϵ����ܵ������ˣ���ϵ����ܵ������ˡ�
    2)����һ���������г����˺������˹�ϵ�Ľ��̶ܳ�������
*/
public class MainFrame extends JFrame implements ActionListener{
	/**********************����ؼ�***********************/
	
	private JButton TimesButton = new JButton("����ͳ��");
	private JButton SpanButton = new JButton("���ͳ��");
	private JButton IntimacyButton = new JButton("���ܶȷ���");
	private JTextArea jta = new JTextArea(20,36);
	
	private JScrollPane sp = new JScrollPane(jta);
	private JPanel panel = new JPanel();
	
	/**********************�����ʼ��***********************/
	
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
		
		
	/**********************���Ӽ���***********************/
		
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
		String[][] names = fo.getTextPerson();//�������

		String[] contents = fo.fileLoader(fileName);//����ı����ݿ�
		
		ObjectInfo[] oi = new ObjectInfo[names.length];	//����person.lenth������
		for(int i = 0; i < oi.length; i++){	//ÿ���˽��г�ʼ��times,firstIndex���Լ�������ǵ��ھӶ���
			oi[i] = new ObjectInfo(names[i]);
			oi[i].setInfo(names);
		}


		TParser parser = new TParser(names);	//��ʼ��TParser�еĶ�������Vperson
		for(int i = 0; i < oi.length; i++){
			parser.counter(oi[i], contents);	//�ֱ�ͳ��ÿ������Ĵ����Լ�����
			
		}
		
		if(e.getSource().equals(TimesButton)){
			jta.append("\n*****************************���ִ���ͳ��********************************"+"\n");
			for(int i = 0; i < oi.length; i ++){
				jta.append(oi[i].key[0] + "------->" + oi[i].times+"\n");
			}

		}else if(e.getSource().equals(SpanButton)){
			jta.append("\n*****************************���ͳ�ƽ��********************************"+"\n");
			for(int i = 0; i < oi.length; i++){
				jta.append(oi[i].key[0]+"------->"+parser.getSpan(oi[i])+"\n");
				
			}
			
		}else if(e.getSource().equals(IntimacyButton)){
			
			for(int i = 0; i < oi.length; i++){
				parser.getIntimacy(oi[i], contents);
			}
			jta.append("\n*************************��ϵ����ܵ�������**************************"+"\n");
			ObjectInfo temp = oi[0].nextKey.get(0);
			int j = 0;
			temp = parser.getMaxTimesPerson(oi[0].nextKey);
			jta.append(oi[j].key[0]+"��"+temp.key[0]+"------->"+temp.times+"\n");
			
			temp = oi[0].nextKey.get(8);	//���һ���������ܵ���͵���Ϊtemp
			j = 8;
			jta.append("\n***********************��ϵ����ܵ�������****************************"+"\n");
			temp = parser.getMinTimesPerson(oi[0].nextKey);
			jta.append(oi[j].key[0]+"��"+temp.key[0]+"------->"+temp.times+"\n");
			
			String str = JOptionPane.showInputDialog("��������Ҫ��ѯ������:"+"\n");
			boolean flag = true;
			for(int i = 0; i < oi.length; i++){
				if(oi[i].key[0].equals(str)){
					flag = false;
					jta.append("\n***********************��������"+oi[i].key[0]+"���ܶ��������*********************\n");
					for(j = 0; j < oi[i].nextKey.size(); j++){
						jta.append(oi[i].nextKey.get(j).key[0] +"------->"+ oi[i].nextKey.get(j).times+"\n");
					}
				}
			}
			if(flag == true){
				jta.append("С˵���޴��ˣ�����ʧ�ܣ�"+"\n");
			}
		}
	}
}
