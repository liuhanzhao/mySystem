package system;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map.Entry;
import java.util.Set;

public class SysCharacter {
	private static BufferedImage character2Img, buildImg, sortImg, pasteImg;
	private int show_state;
	private int state;
	private int width, height;
	private int x, y;
	private boolean build;
	private boolean sort;
	private boolean paste;
	private Data data;
	private String path;

	static {
		character2Img = SystemMenu.loadImg("images/character2.png");
		buildImg = SystemMenu.loadImg("images/build.png");
		sortImg = SystemMenu.loadImg("images/sort.png");
		pasteImg = SystemMenu.loadImg("images/paste.png");
	}

	public SysCharacter(Data data, int mousex, int mousey) {
		this.data = data;
		width = 55;
		height = 75;
		x = mousex;
		y = mousey;
		show_state = 0;
		state = 1;

	}

	public void paintCharacter(Graphics g) {
		if (show_state == 1) {
			switch (state) {
			// 表示文件左键激活
			case 1:
				g.drawImage(character2Img, x, y, null);
				break;
			// 表示新建文件夹被激活
			case 2:
				g.drawImage(buildImg, x, y, null);
				break;
			case 3:
				// 表示排序被激活
				g.drawImage(sortImg, x, y, null);
				break;
			case 4:
				// 表示粘贴被激活
				g.drawImage(pasteImg, x, y, null);
				break;
			}
		}
	}

	// 动态的获取鼠标的xy值。
	public void transform(int mousex, int mousey) {
		// 如果窗口处于显示状态
		if (show_state == 1) {
			if (mousex >= x && mousex <= x + width && mousey >= y && mousey <= y + height / 3) {
				state = 2;

			} else if (mousex >= x && mousex <= x + width && mousey >= y + height / 3 && mousey <= y + height / 3 * 2) {
				state = 3;

			} else if (mousex >= x && mousex <= x + width && mousey >= y + height / 3 * 2 && mousey <= y + height) {
				state = 4;
			} else {
				state = 1;
			}

		}

	}

	public int click() {
		switch (state) {
		case 2: // 进行新建操作；
			build = true;
			System.out.println("build!");
			show_state = 0;
			break;

		case 3: // 进行排序操作；
			System.out.println("sort!");
			show_state = 0;
			return 3;

		case 4:// 进行粘贴操作；
			System.out.println("paste!");
			show_state = 0;
			state = 0;

			File file = data.getFile_copy();

			if (file != null) {
				// 进行copy操作
				try {
					// 物理上完成copy
					ModifyFolder.copyFolder(file, path);
					// data上完成升华
					new ChangeData(data, x, y);

					break;

				} catch (Exception eee) {
					eee.printStackTrace();
					System.out.println("复制不成功！");
				}
			}

			return 4;

		case 1:
			show_state = 0;
			state = 0;

		}
		return -1;

	}

	// 开始鼠标右键click触发事件
	public void trigger(int mousex, int mousey) {
		boolean can_do = true;
		Set<Entry<File, Folder>> entry = data.getSum_folder();
		for (Entry<File, Folder> e : entry) {
			Folder f = e.getValue();
			// 如果鼠标接触到了文件图标
			if (mousex >= f.getX() && mousex <= f.getX() + f.getWidth() && mousey >= f.getY()
					&& mousey <= f.getY() + f.getHeight()) {
				can_do = false;
			}
		}
		boolean path_new = false;
		if (can_do) {
			Set<Entry<File, Window>> windows = data.getWindows();
			for (Entry<File, Window> window : windows) {
				Window w = window.getValue();
				// 如果鼠标在窗口范围内
				if (w.getState() == 1 && mousex >= w.getX() && mousex <= w.getX() + w.getWidth()
						&& mousey >= w.getY() - 20 && mousey <= w.getY() + w.getHeight()) {
					path = w.getFile().getPath();
					path_new = true;
					break;
				}

			}
			if (!path_new) {
				
				path = "mySystem";
			}

			show_state = 1;
		}

	}

	public int getShow_state() {
		return show_state;
	}

	public void setShow_state(int show_state) {
		this.show_state = show_state;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
