package system;

import java.awt.Font;
import java.awt.Graphics;
import java.text.SimpleDateFormat;

public class DeskTopTime {
	private SimpleDateFormat sdf;
	private String time;
	public DeskTopTime() {
		sdf=new SimpleDateFormat("yy-MM-dd   HH：mm：ss E a");
		time="我爱你";
	}
	//获取当前的事件
	public void timeChange() {
		long t=System.currentTimeMillis();
		time=sdf.format(t);
		
	}
	//显示当前的时间
	public void paintTime(Graphics g){
	   g.setFont(new Font("宋体",Font.BOLD,13));
		g.drawString(time,540,528);
		g.setFont(new Font("Courier",Font.PLAIN,12));
	    
	}

}
