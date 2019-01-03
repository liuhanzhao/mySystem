package system;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

public class Character {
	private static BufferedImage characterImg, copyImg, deleteImg, propertyImg;
	private int show_state;
	private int state;
	private int width, height;
	private int x, y;
   
	static {
		characterImg = SystemMenu.loadImg("images/character.png");
		copyImg = SystemMenu.loadImg("images/copy.png");
		deleteImg = SystemMenu.loadImg("images/delete.png");
		propertyImg = SystemMenu.loadImg("images/property.png");
	}

	public Character(int mousex_orginal, int mousey_orginal) {
		show_state = 1;
		state = 0;
		width = 68;
		height = 71;
		x = mousex_orginal;
		y = mousey_orginal;
	}

	public void paintCharacter(Graphics g) {
		if (show_state == 1) {
			switch (state) {
			// 表示文件左键激活
			case 1:
				g.drawImage(characterImg, x, y, null);
				break;
			// 表示复制被激活
			case 2:
				g.drawImage(copyImg, x, y, null);
				break;
			case 3:
				// 表示删除被激活
				g.drawImage(deleteImg, x, y, null);
				break;
			case 4:
				// 表示属性显示被激活
				g.drawImage(propertyImg, x, y, null);
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

		} else {
			state = 0;
		}

	}

	// 获取鼠标点击事件
	public String click() {
		switch (state) {

		case 2: // 进行复制操作；

			System.out.println("copy!");
			show_state = 0;
			return "copy";
		case 3: // delete
          
			System.out.println("delete!");
			show_state = 0;
			return "delete";
		case 4:// 查看属性操作

			System.out.println("property!");
			return "property";
		case 1:
			show_state = 0;
			state = 0;

		}
		return null;
	}

	public int getShow_state() {
		return show_state;
	}

	public void setShow_state(int show_state) {
		this.show_state = show_state;
	}

}
