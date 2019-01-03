package system;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

public class Folder {
	protected int x, y, x1, y1;
	protected int width;
	protected int height;
	protected int show_state=0;
	protected int logo_state = 0;
	protected int window_state=0;
	//这个参数表示文件夹在显示状态的时候能不能操作它，一般只用于桌面文件夹
	protected int operateion_state=1;
	protected BufferedImage folderImg1,txtImg1,homeImg1,videosImg,photosImg,
	trashImg1,imageImg1,bacImg,musicsImg,musicImg,videoImg,documentImg;
	protected BufferedImage currentImage1;
	//定义文件的类型
   protected int fileTpye=-1;
	// 定义文件夹指定的路径
	protected File file;
	//定义文夹是否要被删除状态
	private boolean delete=false;
	private Character cha=null;
	//定义文夹是否处于复制状态
	
	
	

	public Folder(File file, int x, int y) {
		
		this.x = x;
		this.y = y;
		width = 55;
		height = 55;
		x1 = width + x;
		y1 = height + y;
		this.file = file;
		folderImg1 = SystemMenu.loadImg("images/folder1.png");	
		txtImg1=SystemMenu.loadImg("images/txt1.png");
		homeImg1=SystemMenu.loadImg("images/home1.png");
		trashImg1=SystemMenu.loadImg("images/trash1.png");
		imageImg1=SystemMenu.loadImg("images/image1.png");
		bacImg=SystemMenu.loadImg("images/bac.png");
		videosImg=SystemMenu.loadImg("images/videos.png");
		photosImg=SystemMenu.loadImg("images/photos.png");
		musicsImg=SystemMenu.loadImg("images/musics.png");
		musicImg=SystemMenu.loadImg("images/music.png");
		videoImg=SystemMenu.loadImg("images/video.png");
		documentImg=SystemMenu.loadImg("images/document.png");
		confirmType();
	}
	//确认文件的类型来显示文件图片
	public void confirmType() {
		if(file.isDirectory()) {
			if(file.getName().equals("我的电脑")) {
				currentImage1=homeImg1;	
			}else if(file.getName().equals("trash")){
				currentImage1=trashImg1;			
			}else if(file.getName().equals("视频")){
				currentImage1=videosImg;		
			}else if(file.getName().equals("图片")){
				currentImage1=photosImg;		
			}else if(file.getName().equals("音乐")){
				currentImage1=musicsImg;		
			}else if(file.getName().equals("文档")){
				currentImage1=documentImg;		
			}else {
			  currentImage1=folderImg1;
			
			}
		}else {
		String[] array=file.getName().split("\\.");
		switch(array[array.length-1].toLowerCase()) {
		case "txt":currentImage1=txtImg1;break;
		case "png":
		case "jpg":
		case "gif":currentImage1=imageImg1;break;  
		case "mp3":currentImage1=musicImg;break;
		case "mp4":
		case "rmvb":
		case "avi":currentImage1=videoImg;break;
		}
		
	}
	}

   //绘制图形
	public void paintFolder(Graphics g) {
		if(show_state==1) {
		if (logo_state == 0) {
			
			g.drawImage(currentImage1, x, y,55,55, null);
		} else {
			g.drawImage(bacImg, x, y, 55, 55, null);
			g.drawImage(currentImage1, x, y,55,55, null);
		}
		// 获取文本的宽度
		int strWidth = g.getFontMetrics().stringWidth(file.getName());
		// 显示文本字体
		g.drawString(file.getName(), x + width / 2 - strWidth / 2+3, y + height + 18);
		}
	}


	// 单击或者双击文件夹
	public void openFolder(int mousex, int mousey, boolean doubleclick,int left_right) {
		// 点击窗口里面的图片变蓝
		if(show_state==1&&operateion_state==1) {
		if (mousex >= x && mousex <=x+width && mousey >= y && mousey <=y+height) {
			if(left_right==-1) {
			logo_state = 1;
			// 定义双击事件
			if (doubleclick) {
				// 这里需要打开一个标记打开的文件夹要打开了
				logo_state = 0;
				window_state=1;
			}
			}else if(left_right==1) {
				//写入右单击事件
				cha=new Character(mousex,mousey);
			}
		} else { 
			    logo_state = 0;
		}
		}
	}

	
	
	
	//定义文件拖动事件，这里只针对桌面上的文件

		public boolean folderMove(int mousex,int mousey,int changeX,int changeY) {
			if(show_state==1&&operateion_state==1) {	
			if(mousex>=x&&mousex<=x+43&&mousey>=y&&mousey<=y+40) {
			  x=x+changeX;
			  y=y+changeY;
			  return true;
			}
			  
			}
			return false;
		}

	public int getWindow_state() {
		return window_state;
	}



	public void setWindow_state(int window_state) {
		this.window_state = window_state;
	}



	

	public Character getCha() {
		return cha;
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
	public void setX1(int x1) {
		this.x1 = x1;
	}
	public int getY1() {
		return y1;
	}
	public void setY1(int y1) {
		this.y1 = y1;
	}
	public int getLogo_state() {
		return logo_state;
	}

	public void setLogo_state(int logo_state) {
		this.logo_state = logo_state;
	}



	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getShow_state() {
		return show_state;
	}

	public void setShow_state(int show_state) {
		this.show_state = show_state;
	}
	public int getOperateion_state() {
		return operateion_state;
	}
	public void setOperateion_state(int operateion_state) {
		this.operateion_state = operateion_state;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	

	
	

}
