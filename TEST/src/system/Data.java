package system;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Data {

	private List<File> list = new ArrayList<File>();
	private Map<File, Window> windows1 = new HashMap<File, Window>();
	private Map<File, Folder> folders1 = new HashMap<File, Folder>();
	// 我觉得很有必要创建一个只装桌面文件夹的集合
	private Map<File, Folder> deskfolders1 = new HashMap<File, Folder>();
	// 装所有的文件
	private Map<File, Folder> sum_folder1 = new HashMap<File, Folder>();
	// 装窗口的集合
	private Set<Entry<File, Window>> windows = windows1.entrySet();
	// 装窗口里面的文件夹的集合
	private Set<Entry<File, Folder>> folders = folders1.entrySet();

	private Set<Entry<File, Folder>> deskfolders = deskfolders1.entrySet();

	private Set<Entry<File, Folder>> sum_folder = sum_folder1.entrySet();
	// 这个参数获取当前被激活的要复制的文件夹
	private File file_copy = null;
	// 这个参数监测是否需要对物理上的文件夹扫描一次，然后将不存在的文件从数组里面删除
	private int count_delete = 0;
	// start窗口
	private StartProgram sp = new StartProgram(sum_folder);
	// 定义音乐播放器
	private MP3Player mp3 = null;
	// 定义mp3文件名
	private File mp3_file = null;

	public Data() {
		add(new File("mySystem"));
		createWindow();
	}

	// 数据库创建windows和folders
	public void add(File file) {

		if (file.isDirectory()) {
			File[] subs = file.listFiles();
			for (int i = 0; i < subs.length; i++) {
				add(subs[i]);
			}
		}
		list.add(file);

	}

	public void createWindow() {
		for (File f : list) {

			if (f.getName().equals("mySystem")) {
				// 里面写桌面和桌面里面的文件夹位置，不创建它的窗口
				desktop(f.listFiles());

			} else {
				Window window = new Window(f);
				windows1.put(f, window); // 里面所有文件的窗口
				if (f.isDirectory()) {
					showFolder(f.listFiles(), window);// 装着窗口下面的文件夹和文档
				}
			}
		}

	}

	// 定义桌面文件夹的位置
	public void desktop(File[] subs) {
		int desktop_x = 0, desktop_y = 0;
		int countX = 1;
		int countY = 1;
		for (File f : subs) {
			desktop_x = SystemMenu.WIDTH / 8 * countX - SystemMenu.WIDTH / 8 / 2;
			desktop_y = SystemMenu.HEIGHT / 6 * countY - SystemMenu.HEIGHT / 6 / 2 - 30;
			Folder folder = new Folder(f, desktop_x, desktop_y);
			countY++;

			deskfolders1.put(f, folder);
			sum_folder1.put(f, folder);
			if (countY % 6 == 0) {
				countX++;
				countY = 1;
			}
			folder.setShow_state(1);
			folder.setLogo_state(0);
		}

	}

	// 根据窗口定义他里面的文件夹
	public void showFolder(File[] subs, Window window) {
		// 表示子文件夹想对窗口的相对坐标
		int folder_x = 0, folder_y = 0;
		for (File f : subs) {
			Folder folder = new Folder(f, window.getX() + folder_x, window.getY() + folder_y);
			folders1.put(f, folder);
			sum_folder1.put(f, folder);
		}
	}

	boolean folder_operate = false;
	// 监测数据库的动态

	public void change() {

		// 如果文件属性被显示，这桌面的文件不能被操作
		for (Entry<File, Folder> folder : sum_folder) {
			Character cha = folder.getValue().getCha();
			if ((cha != null && cha.getShow_state() == 1) || sp.getShow_state() == 1) {
				folder_operate = true;
			}
		}

		// 如果文件的属性在显示，这文件是不能被操作的
		if (folder_operate) {
			for (Entry<File, Folder> folder : sum_folder) {
				folder.getValue().setOperateion_state(0);
			}
			for (Entry<File, Window> window : windows) {
				window.getValue().setOperation_state(0);
			}
			folder_operate = false;

		} else {
			for (Entry<File, Folder> folder : sum_folder) {
				folder.getValue().setOperateion_state(1);
			}
			for (Entry<File, Window> window : windows) {
				window.getValue().setOperation_state(1);
			}

		}

		for (Entry<File, Window> e : windows) {

			// 如果桌面文件夹和窗口出现覆盖，则文件夹不再有激活状态
			if (e.getValue().getState() == 1) {
				for (Entry<File, Folder> f : deskfolders) {
					int x1 = e.getValue().getX() - f.getValue().getWidth() - 10;
					int y1 = e.getValue().getY() - f.getValue().getHeight() - 10;
					int x2 = e.getValue().getX() + e.getValue().getWidth() + 10;
					int y2 = e.getValue().getY() + e.getValue().getHeight() + 10;
					int x = f.getValue().getX();
					int y = f.getValue().getY();
					if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
						f.getValue().setOperateion_state(0);
					} else {
						f.getValue().setOperateion_state(1);
					}

				}
			}

			// 如果窗口打开，它里面的文件夹要求显示，文件夹的位置随着窗口动态的移动
			if (e.getValue().getState() == 1 && e.getKey().isDirectory()) {

				File[] subs = e.getKey().listFiles();
				int folder_x = 0, folder_y = 0;
				int countX = 1;
				int countY = 1;
				for (File f : subs) {
					for (Entry<File, Folder> m : folders) {
						if (f.getAbsolutePath().equals(m.getKey().getAbsolutePath())) {
							m.getValue().setShow_state(1);
							// 动态的定义文件夹的位置
							folder_x = (e.getValue().getWidth() - 20) / 5 * countX
									- (e.getValue().getWidth() - 20) / 5 / 2 - 43 / 2;
							folder_y = (e.getValue().getHeight() / 4 - 10) * countY
									- (e.getValue().getHeight() - 10) / 4 / 2;
							m.getValue().setX(e.getValue().getX() + folder_x);
							m.getValue().setY(e.getValue().getY() + folder_y);
							countX++;
							if (countX % 6 == 0) {
								countY++;
								countX = 1;
							}
						}
					}
				}

			} else if (e.getKey().isDirectory()) {

				File[] subs = e.getKey().listFiles();
				for (File f : subs) {
					for (Entry<File, Folder> m : folders) {
						if (f.getAbsolutePath().equals(m.getKey().getAbsolutePath())) {
							m.getValue().setShow_state(0);
						}
					}
				}
			}
			//如果窗口被按了后退
			if(e.getValue().getBackoff()==1) {
				String url=e.getKey().getPath();
				int index=url.lastIndexOf("/");
				//真TM搞人
				if(index==-1) {
					index=url.lastIndexOf("\\");
				}
				File up_file=new File(url.substring(0,index));
				if(!"mySystem".equals(up_file.getName())){
					e.getValue().setState(0);
				windows1.get(up_file).setState(1);
				
				}
			
				e.getValue().setBackoff(0);
			}

		}

		// 如果文夹夹被双击，对应的窗口要被打开
		for (Entry<File, Folder> folder : sum_folder) {
			// 文件夹被双击
			if (folder.getValue().getWindow_state() == 1) {
				// 判断是否是mp3文件
				File file = folder.getKey();
				if ((!file.isDirectory()) && file.getName().split("\\.")[1].toLowerCase().equals("mp3")) {
					// 是mp3则启动播放器功能，开启一个线程
					mp3 = new MP3Player(file.getPath());
					mp3_file = file;
					new Thread() {
						public void run() {
							mp3.play();		
							try {
								windows1.get(mp3_file).setState(0);
							} catch (Exception e) {
								System.out.println("退出mp3播放界面");
							}

						}
					}.start();
				}
				for (Entry<File, Window> w : windows) {
					if (folder.getKey().getAbsolutePath().equals(w.getKey().getAbsolutePath())) {
						w.getValue().setState(1);

					} else {
						w.getValue().setState(0);
					}
				}
				folder.getValue().setWindow_state(0);
			}

		}
		// 如果mp3窗口被关闭，则mp3停止播放
		if (mp3 != null && mp3_file != null) {
			if (windows1.get(mp3_file).getState() == 0) {
				mp3.stop();
				mp3 = null;
				mp3_file = null;
			}
		}

		// 将delete的元素从数组里面进行删除
		if (count_delete == 1) {

			Iterator<Entry<File, Folder>> it1 = folders.iterator();
			while (it1.hasNext()) {
				Entry<File, Folder> entry = it1.next();
				if (!entry.getKey().exists()) {
					it1.remove();
				}
			}

			Iterator<Entry<File, Window>> it2 = windows.iterator();
			while (it2.hasNext()) {
				Entry<File, Window> entry = it2.next();
				if (!entry.getKey().exists()) {
					it2.remove();
				}
			}

			Iterator<Entry<File, Folder>> it3 = deskfolders.iterator();
			while (it3.hasNext()) {
				Entry<File, Folder> entry = it3.next();
				if (!entry.getKey().exists()) {
					it3.remove();
				}
			}
			Iterator<Entry<File, Folder>> it4 = sum_folder.iterator();
			while (it4.hasNext()) {
				Entry<File, Folder> entry = it4.next();
				if (!entry.getKey().exists()) {
					it4.remove();
				}
			}
			Iterator<File> it5 = list.iterator();
			while (it5.hasNext()) {
				File f = it5.next();
				if (!f.exists()) {
					it5.remove();
				}
			}
			count_delete = 0;
		}

	}

	public Set<Entry<File, Window>> getWindows() {
		return windows;
	}

	public Set<Entry<File, Folder>> getFolders() {
		return folders;
	}

	public Set<Entry<File, Folder>> getDeskfolders() {
		return deskfolders;
	}

	public Set<Entry<File, Folder>> getSum_folder() {
		return sum_folder;
	}

	public List<File> getList() {
		return list;
	}

	public Map<File, Folder> getFolders1() {
		return folders1;
	}

	public Map<File, Folder> getDeskfolders1() {
		return deskfolders1;
	}

	public Map<File, Folder> getSum_folder1() {
		return sum_folder1;
	}

	public Map<File, Window> getWindows1() {
		return windows1;
	}

	public File getFile_copy() {
		return file_copy;
	}

	public void setFile_copy(File file_copy) {
		this.file_copy = file_copy;
	}

	public int getCount_delete() {
		return count_delete;
	}

	public void setCount_delete(int count_delete) {
		this.count_delete = count_delete;
	}

	public StartProgram getSp() {
		return sp;
	}

	public void setSp(StartProgram sp) {
		this.sp = sp;
	}

	/*
	 * 
	 * public static void main(String[] args) { Data data = new Data();
	 * System.out.println("sum_folder:"+data.sum_folder.size());
	 * System.out.println("文件夹的数量：" + data.folders.size());
	 * System.out.println("桌面文件夹的数量：" + data.deskfolders.size()); for (File l :
	 * data.list) { System.out.println(l.getName()); }
	 * 
	 * System.out.println("文件夹的数量：" + data.folders.size()); for (Entry<File, Folder>
	 * e : data.folders) { String url = e.getValue().file.getAbsolutePath();
	 * System.out.println("文件夹：" + url);
	 * 
	 * } System.out.println("窗口的数量：" + data.windows.size()); for (Entry<File,
	 * Window> e : data.windows) {
	 * System.out.println(e.getValue().getFile().getName()); } for (Entry<File,
	 * Folder> e : data.deskfolders) { System.out.println("桌面文件夹：" +
	 * e.getValue().file.getName()); }
	 * 
	 * }
	 */

}
