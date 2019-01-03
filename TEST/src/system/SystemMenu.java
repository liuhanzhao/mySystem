package system;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class SystemMenu extends JPanel {
	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;
	public final static int WIDTH = 782;
	public final static int HEIGHT = 578;
	// 定义鼠标的坐标；
	private int x;
	private int y;
	private static BufferedImage background,xuduoduo;
	private Data data;
	private DeskTopTime desk_time;
	private SysCharacter sc = null;
	//进度条，测试了很多次，未能实现，遗憾。
	private JProgressBar processBar=null;


	static {
		background = loadImg("images/background.png");
		xuduoduo = loadImg("images/xuduoduo.png");
	}

	// 构造方法
	public SystemMenu() {
		desk_time = new DeskTopTime();
		data = new Data();	
		createBar();

	}
	
	// 定义载入图片的方法
	public static BufferedImage loadImg(String url) {
		try {
			BufferedImage image = ImageIO.read(new File(url));
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	//创建processBar的方法
	public void createBar() {
		processBar=new JProgressBar();	
		processBar.setStringPainted(true);
		processBar.setBackground(Color.blue);	
		new Thread() {
			public void run() {
				for(int i=0;i<101;i++) {
					try {
						Thread.sleep(40);
						processBar.setBounds(280,200,200,30);
						processBar.setString("正在加载.."+i+"%");
						processBar.setValue(i);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
				
				processBar.setString("I miss you...");
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				processBar=null;
				
			}
		}.start();
		
		this.add(processBar);
	}

	// 定义鼠标双击事件
	private long time = 0;

	public boolean doubleClick() {
		long nowTime = System.currentTimeMillis();
		if (nowTime - time < 500) {
			time = nowTime;
			System.out.println("双击事件！");
			return true;
		}
		time = nowTime;
		return false;
	}

	public void start() {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				if(processBar==null) {
				data.change();
				desk_time.timeChange();
				repaint();
				}
				
			}

		}, 20, 50);
     
		MouseAdapter mouse = new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				if(processBar==null) {	
				boolean click = doubleClick();
				int left_right = 0;
				// 判断是左单击还是右单击事件
				if (e.getButton() == MouseEvent.BUTTON1) {
					left_right = -1;
					// start事件
					data.getSp().trigger(x, y);
					data.getSp().click();
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					left_right = 1;
					// 如果触发右单击事件
					sc = new SysCharacter(data, x, y);
					sc.trigger(x, y);
				}
				// 文件被操作事件
				for (Entry<File, Folder> folder : data.getSum_folder()) {
					folder.getValue().openFolder(x, y, click, left_right);
					Character cha = folder.getValue().getCha();
					if (cha != null) {

						String state = cha.click();
						if (state != null) {
							switch (state) {
							// copy操作
							case "copy":
								// 我需要将当前需要copy的对象传回到data去供sysCharacter调用
								data.setFile_copy(folder.getKey());
								System.out.println(folder.getKey().getName());
								break;
							// delete操作
							case "delete":
								folder.getValue().setShow_state(0);
								cha.setShow_state(0);
								// 物理上删除
								ModifyFolder.deleteFolder(folder.getKey());
								// 准备对磁盘扫描，然后对data数据进行跟新
								data.setCount_delete(1);
								break;
							// property操作，未实现。
							case "property":
								break;

							}
						}
					}

				}

				// 窗口被操作事件
				for (Entry<File, Window> window : data.getWindows()) {
					window.getValue().windowClick(x, y, click);
				}
				// 如果触发左单击事件
				if (left_right == -1 && sc != null) {
					// 重新排序功能
					int i = sc.click();
					if (i == 3) {
						// 排序事件
						data = new Data();
						sc = new SysCharacter(data, x, y);
					} else if (i == 4) {
						// 复制事件				
					}
				}
			}
			}

			// 鼠标按压事件,为拖拽事件做铺垫
			private File press_file = null;

			public void mousePressed(MouseEvent e) {
				// 左按压事件
				if (e.getButton() == MouseEvent.BUTTON1) {
					for (Entry<File, Folder> folder : data.getDeskfolders()) {
						if (folder.getValue().folderMove(x, y, 0, 0)) {
							press_file = folder.getKey();
						}
					}

				}

			}

			// 鼠标拖拽窗口和文件事件
			public void mouseDragged(MouseEvent e) {
				int orginal_X = x;
				int orginal_Y = y;
				x = e.getX();
				y = e.getY();
				int changeX = x - orginal_X;
				int changeY = y - orginal_Y;
				if (sc != null) {
					sc.setShow_state(0);
				}
				if (press_file != null) {
					data.getDeskfolders1().get(press_file).folderMove(orginal_X, orginal_Y, changeX, changeY);
				}

				for (Entry<File, Window> window : data.getWindows()) {
					window.getValue().windowMove(orginal_X, orginal_Y, changeX, changeY);
				}

			}

			// 监听鼠标的xy值；
			public void mouseMoved(MouseEvent e) {
				x = e.getX();
				y = e.getY();
				data.getSp().transform(x, y);
				for (Entry<File, Folder> folder : data.getSum_folder()) {
					Character cha = folder.getValue().getCha();
					if (cha != null) {
						cha.transform(x, y);
					}

				}
				if (sc != null) {
					sc.transform(x, y);
				}
			}

		};
      
		
		this.addMouseListener(mouse);
		this.addMouseMotionListener(mouse);
		
      

	}

	// 绘制桌面
	
	public void paint(Graphics g) {
		//开机中
		if(processBar!=null) {
			g.drawImage(xuduoduo, 0, 0, null);
			
			
		}else {
		// 显示背景
		g.drawImage(background, 0, 0, null);

		// 显示时间
		desk_time.paintTime(g);
		// 显示桌面文件夹
		for (Entry<File, Folder> entry : data.getDeskfolders()) {
			entry.getValue().paintFolder(g);
			// 画属性栏
			Character cha = entry.getValue().getCha();
			if (cha != null) {
				entry.getValue().getCha().paintCharacter(g);
			}
		}
		// 显示窗口
		for (Entry<File, Window> entry : data.getWindows()) {
			entry.getValue().paintWindow(g);
		}
		// 显示窗口里面的文件夹
		for (Entry<File, Folder> entry : data.getFolders()) {
			entry.getValue().paintFolder(g);
			// 画属性栏
			Character cha = entry.getValue().getCha();
			if (cha != null) {
				entry.getValue().getCha().paintCharacter(g);
			}
		}
		if (sc != null) {
			sc.paintCharacter(g);
		}
		data.getSp().paintCharacter(g); 
	}
	}
	
	

	public static void main(String[] args) {
		SystemMenu sys = new SystemMenu();
		JFrame frame = new JFrame();
		frame.add(sys);
		frame.setTitle("java操作系统");
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		sys.start();

	}

}
