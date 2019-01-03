package system;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map.Entry;
import java.util.Set;

public class StartProgram {
	private static BufferedImage start_logo1_Img, start_logo2_Img, start_Img, start_home_Img, start_music_Img,
			start_trash_Img, start_doc_Img, start_photo_Img, start_video_Img;
	private int show_state;
	private int state;
	private int width, height;
	private int x, y;
	private Set<Entry<File, Folder>> sum_folders;

	static {
		start_logo1_Img = SystemMenu.loadImg("images/start_logo1.png");
		start_logo2_Img = SystemMenu.loadImg("images/start_logo2.png");
		start_Img = SystemMenu.loadImg("images/start.png");
		start_home_Img = SystemMenu.loadImg("images/start_home.png");
		start_music_Img = SystemMenu.loadImg("images/start_music.png");
		start_photo_Img = SystemMenu.loadImg("images/start_photo.png");
		start_trash_Img = SystemMenu.loadImg("images/start_trash.png");
		start_video_Img = SystemMenu.loadImg("images/start_video.png");
		start_doc_Img = SystemMenu.loadImg("images/start_doc.png");

	}

	public StartProgram(Set<Entry<File, Folder>> sum_folders) {
		this.sum_folders = sum_folders;
		width = 203;
		height = 277;
		x = 20;
		y = 220;
		show_state = 0;
		state = -1;

	}

	public void paintCharacter(Graphics g) {
		if (show_state == 1) {
			g.drawImage(start_logo2_Img, 10, 503, null);
			switch (state) {
			// 表示窗口右键激活
			case 1:
				g.drawImage(start_Img, x, y, null);

				break;
			// 表示home被激活
			case 2:
				g.drawImage(start_home_Img, x, y, null);
				break;
			case 3:
				// 表示video被激活
				g.drawImage(start_video_Img, x, y, null);
				break;
			case 4:
				// 表示photo被激活
				g.drawImage(start_photo_Img, x, y, null);
				break;
			case 5:
				// 表示doc被激活
				g.drawImage(start_doc_Img, x, y, null);
				break;
			case 6:
				// 表示music被激活
				g.drawImage(start_music_Img, x, y, null);
				break;
			case 7:
				// 表示trush被激活
				g.drawImage(start_trash_Img, x, y, null);
				break;
			}
		} else {
			g.drawImage(start_logo1_Img, 10, 503, null);
		}
	}

	// 动态的获取鼠标的xy值。
	public void transform(int mousex, int mousey) {
		// 如果窗口处于显示状态
		if (show_state == 1) {
			if (mousey >= y && mousey <= y + height / 8 * 2 && mousex >= x && mousex <= x + width) {
				state = 2;
			} else if (mousey >= y + height / 8 * 2 && mousey <= y + height / 8 * 3 && mousex >= x
					&& mousex <= x + width) {
				state = 3;
			} else if (mousey >= y + height / 8 * 3 && mousey <= y + height / 8 * 4 && mousex >= x
					&& mousex <= x + width) {
				state = 4;
			} else if (mousey >= y + height / 8 * 4 && mousey <= y + height / 8 * 5 && mousex >= x
					&& mousex <= x + width) {
				state = 5;
			} else if (mousey >= y + height / 8 * 6 && mousey <= y + height / 8 * 7 && mousex >= x
					&& mousex <= x + width) {
				state = 6;
			} else if (mousey >= y + height / 8 * 7 && mousey <= y + height && mousex >= x && mousex <= x + width) {
				state = 7;
			} else {
				state = 1;
			}
		}
	}

	// 点击窗口事件
	public void click() {
		switch (state) {
		case 1: // 关闭窗口
			show_state = 0;
			state = -1;
			break;
		case 2: // 打开home文件夹；
			for (Entry<File, Folder> f : sum_folders) {

				if (f.getKey().getPath().equals("mySystem/我的电脑") || f.getKey().getPath().equals("mySystem\\我的电脑")) {
					f.getValue().setWindow_state(1);
				}

			}
			System.out.println("打开home文件夹");
			state = -1;
			break;

		case 3: // 打开video文件夹；
			for (Entry<File, Folder> f : sum_folders) {
				if (f.getKey().getPath().equals("mySystem/我的电脑/视频")
						|| f.getKey().getPath().equals("mySystem\\我的电脑\\视频")) {
					f.getValue().setWindow_state(1);
				}

			}
			System.out.println("打开video文件夹");
			state = -1;
			break;
		case 4:// 打开photo文件夹；
			for (Entry<File, Folder> f : sum_folders) {
				if (f.getKey().getPath().equals("mySystem/我的电脑/图片")
						|| f.getKey().getPath().equals("mySystem\\我的电脑\\图片")) {
					f.getValue().setWindow_state(1);
				}

			}
			System.out.println("打开photo文件夹");
			state = -1;
			break;
		case 5:// 打开doc文件夹；
			for (Entry<File, Folder> f : sum_folders) {
				if (f.getKey().getPath().equals("mySystem/我的电脑/文档")
						|| f.getKey().getPath().equals("mySystem\\我的电脑\\文档")) {
					f.getValue().setWindow_state(1);
				}

			}
			System.out.println("打开doc文件夹");
			state = -1;
			break;
		case 6:// 打开music文件夹；
			for (Entry<File, Folder> f : sum_folders) {
				if (f.getKey().getPath().equals("mySystem/我的电脑/音乐")
						|| f.getKey().getPath().equals("mySystem\\我的电脑\\音乐")) {
					f.getValue().setWindow_state(1);
				}

			}
			System.out.println("打开music文件夹");
			state = -1;
			break;
		case 7:// 打开trash文件夹；
			for (Entry<File, Folder> f : sum_folders) {
				if (f.getKey().getPath().equals("mySystem/trash") || f.getKey().getPath().equals("mySystem\\trash")) {
					f.getValue().setWindow_state(1);
				}

			}
			System.out.println("打开trash文件夹");
			state = -1;
			break;

		}

	}

	// 开始鼠标右键click触发事件
	public void trigger(int mousex, int mousey) {

		if (mousex >= 0 && mousex <= 111 && mousey >= 500 && mousey <= 530) {
			show_state = 1;

		} else {
			show_state = 0;
		}

	}

	public int getShow_state() {
		return show_state;
	}

	public void setShow_state(int show_state) {
		this.show_state = show_state;
	}

}
