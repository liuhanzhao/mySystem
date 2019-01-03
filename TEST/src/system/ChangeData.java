package system;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*
 * 我在复制了一堆文件夹后，需要用数组存放他们，最后和data数据完成融合。
 */
public class ChangeData {
	private Data data_old;
	private Data data_new;
	private int x, y;
	private Map<File, Folder> sum_folder = new HashMap<File, Folder>();
	private Set<Entry<File, Folder>> sum_folder_entrys = sum_folder.entrySet();

	public ChangeData(Data data, int mousex, int mousey) {
		data_new = new Data();
		data_old = data;
		x = mousex;
		y = mousey;
		start();

	}

	public void start() {
		changeDeskFolder(x, y);
		changeWindows();
		changeSubFolder();
		changeList();
		changeSumFolder();
		//test();

	}

	public void test() {
		for (Entry<File, Folder> entry_old : data_old.getDeskfolders()) {
			System.out.println("桌面文件：" + entry_old.getKey().getAbsolutePath());

		}
		System.out.println(data_old.getDeskfolders().size());
		for (Entry<File, Folder> entry_old : data_old.getFolders()) {
			System.out.println("非桌面文件：" + entry_old.getKey().getAbsolutePath());
		}
		System.out.println(data_old.getFolders().size());
		for (Entry<File, Window> entry_old : data_old.getWindows()) {
			System.out.println("Window：" + entry_old.getKey().getAbsolutePath());
		}
		System.out.println(data_old.getWindows().size());
		for (Entry<File, Folder> entry_old : data_old.getSum_folder()) {
			System.out.println("sum_folder：" + entry_old.getKey().getAbsolutePath());
		}
		System.out.println(data_old.getSum_folder().size());
		for (Entry<File, Folder> entry : sum_folder_entrys) {
			System.out.println("xixi:" + entry.getKey().getAbsolutePath());
		}
		System.out.println(sum_folder_entrys.size());
		for (File f : data_old.getList()) {
			System.out.println(f.getName());
		}
		System.out.println(data_old.getList().size());

	}

	/*
	 * 完成桌面文件的添加
	 */
	public void changeDeskFolder(int mousex, int mousey) {

		// 遍历新的窗口文件夹
		for (Entry<File, Folder> entry_new : data_new.getDeskfolders()) {
			boolean flag = false;
			// 遍历旧的窗口文件夹
			File f_new = entry_new.getKey();
			for (Entry<File, Folder> entry_old : data_old.getDeskfolders()) {
				File f_old = entry_old.getKey();
				if (f_old.getAbsolutePath().equals(f_new.getAbsolutePath())) {
					flag = true;
					break;
				}

			}
			// 如果flag未变，则此时f_new旧的里面没有，需要加的到旧的里面；
			if (!flag) {
				Folder folder = new Folder(f_new, mousex, mousey);
				folder.setShow_state(1);
				data_old.getDeskfolders1().put(f_new, folder);

			}
		}
	}

	/*
	 * 完成窗口的添加
	 */
	public void changeWindows() {
		// 遍历新的窗口
		for (Entry<File, Window> entry_new : data_new.getWindows()) {
			boolean flag = false;
			// 遍历旧的窗口
			File f_new = entry_new.getKey();
			for (Entry<File, Window> entry_old : data_old.getWindows()) {
				File f_old = entry_old.getKey();
				if (f_old.getAbsolutePath().equals(f_new.getAbsolutePath())) {
					flag = true;
					break;
				}

			}
			// 如果flag未变，则此时f_new旧的里面没有，需要加的到旧的里面；
			if (!flag) {
				data_old.getWindows1().put(f_new, entry_new.getValue());

			}
		}

	}

	// 我需要把窗口下的文件加全部替换掉
	public void changeSubFolder() {
		data_old.getFolders1().clear();
		data_old.getFolders1().putAll(data_new.getFolders1());

	}

	// 我需要把list全部替换掉
	public void changeList() {
		data_old.getList().clear();
		data_old.getList().addAll(data_new.getList());	
	}

	public void changeSumFolder() {
		sum_folder.putAll(data_old.getFolders1());
		sum_folder.putAll(data_old.getDeskfolders1());
		data_old.getSum_folder().clear();
		data_old.getSum_folder1().putAll(sum_folder);
	
	}

}
