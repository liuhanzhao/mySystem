package system;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

//定义了一个每个文件夹的窗口，有激活和隐藏状态；
public class Window {


	private int x, y, x1, y1;
	private int width;
	private int height;
	// 定义窗口的状态
	private int state = 0;
	private BufferedImage windowImg,windowImg2, now_window_Img, closeImg, narrowImg,musicbg,image;
	private File file;
	private int operation_state = 1;
	private int isWhat = -1;
	private int backoff=0;

	public Window(File file) {
		windowImg = SystemMenu.loadImg("images/windowImg.png");
		windowImg2 = SystemMenu.loadImg("images/windowImg2.png");
		closeImg = SystemMenu.loadImg("images/close.png");
		narrowImg = SystemMenu.loadImg("images/narrow.png");
		musicbg = SystemMenu.loadImg("images/musicbg.jpg");
		this.file = file;
		if (file.isDirectory()) {
			// 0表示是文件夹
			isWhat = 0;
			now_window_Img = windowImg2;
			x = 200;
			y = 70;
			width = 481;
			height = 399;
		} else if (file.getName().split("\\.")[1].equals("png") || file.getName().split("\\.")[1].equals("jpg")
				|| file.getName().split("\\.")[1].equals("gif")) {
			// 1表示是图片
			isWhat = 1;
			now_window_Img = windowImg;
			image = SystemMenu.loadImg(file.getAbsolutePath());
			x = 200;
			y = 70;
			width = 481;
			height = 399;
		} else if (file.getName().toLowerCase().split("\\.")[1].equals("mp3")) {
			// 2表示是mp3
			isWhat = 2;
			now_window_Img = null;
			x = 0;
			y = 0;
			width = 782;
			height = 578;
		}else {
		   //未实现其他窗口
			state=0;
		}
		

	}

	public void paintWindow(Graphics g) {
		if (state == 1) {
			// 判断是否是文件夹
			if (isWhat == 0) {
				g.drawImage(windowImg2, x, y, null);
				// 判断是否是图片
			} else if (isWhat == 1) {	
				if (now_window_Img == windowImg) {
					g.drawImage(now_window_Img, x, y, null);
					g.drawImage(image, x + 6, y + 33, width - 12, height - 39, null);
				} else if (now_window_Img == null) {
					g.drawImage(image, x + 5, y + 5, 779 - 10, 500, null);
					g.drawImage(closeImg, 712, 9, 50, 50, null);
					g.drawImage(narrowImg, 650, 14, 40, 40, null);
				}
				// 判断是否是mp3文件
			} else if (isWhat == 2) {
				
				g.drawImage(musicbg, x1 + 5, y1 + 5, 779 - 10, 500, null);
				g.drawImage(closeImg, 712, 9, 50, 50, null);
				g.setFont(new Font("宋体", Font.PLAIN, 30));
				String str = "正在播放：";
				g.drawString(str, 30, 220);
				g.setFont(new Font("宋体", Font.BOLD, 40));
             str=file.getName();
             g.drawString(str, 30, 300);
			}else {
               //暂时不支持格式
			}

		}
	}
	// 定义窗口拖动事件

	public boolean windowMove(int mousex, int mousey, int changeX, int changeY) {
		if (state == 1 && operation_state == 1 && now_window_Img !=null) {
			// 定义了窗口上方的一块区域为拖动区域
			if (mousex >= x && mousex <= x + width - 100 && mousey >= y && mousey <= y + 40) {
				x = x + changeX;
				y = y + changeY;
				return true;
			}

		}
		return false;
	}

	// 关闭窗口事件
	public void windowClick(int mousex, int mousey, boolean doubleclick) {
		if (state == 1 && operation_state == 1) {
			// 此时是小窗口
			if (now_window_Img != null) {
				// 关闭窗口功能
				if (mousex >= x + width - 35 && mousex <= x + width && mousey >= y && mousey <= y + 50) {
					// 窗口关闭，同时初始化坐标
					state = 0;
					x = 200;
					y = 70;
					System.out.println(file.getName() + "窗口被关闭了！");

				} else if(mousex >= x  && mousex <= x + 50 && mousey >= y && mousey <= y + 50) {
					//后退功能
					backoff=1;
					System.out.println("back off!");
					
					
				}else if (mousex >= x + width - 100 && mousex <= x + width - 50 && mousey >= y && mousey <= y + 50
						&& !file.isDirectory()) {
					// 图片放大窗口
					now_window_Img = null;
					x=0;
					y=0;
					width = 782;
					height = 578;

				}
			} else if (now_window_Img == null) {
				// 关闭窗口功能
				if (mousex >= 725 && mousex <= 779 && mousey >= 0 && mousey <= 50) {
					// 窗口关闭，同时初始化坐标
					state = 0;
					System.out.println(file.getName() + "大窗口被关闭了！");

				} else if (isWhat!=2&&mousex >= 650 && mousex <= 700 && mousey >= 0 && mousey <= 50) {
					// 缩小功能
					now_window_Img = windowImg;
					x = 200;
					y = 70;
					width = 481;
					height = 399;

				}
			}
		}

	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public File getFile() {
		return file;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}
	
	public int getBackoff() {
		return backoff;
	}

	public void setBackoff(int backoff) {
		this.backoff = backoff;
	}
	
	

	public int getOperation_state() {
		return operation_state;
	}

	public void setOperation_state(int operation_state) {
		this.operation_state = operation_state;
	}

}
