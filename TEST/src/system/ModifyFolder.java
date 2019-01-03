package system;

import java.io.File;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

/*
 * 此类用于文件夹的复制，重命名，剪切，删除等功能
 */
public class ModifyFolder {

	// 删除功能
	public static void deleteFolder(File file) {

		// 物理上删除
		if (file.isDirectory()) {
			File[] subs = file.listFiles();
			for (int i = 0; i < subs.length; i++) {
				deleteFolder(subs[i]);
			}
		}
		file.delete();
	}
	// 复制粘贴功能

	public static void copyFolder(File file, String path) throws IOException {

		String up_path = path;
		File f = new File(up_path + "/" + file.getName());
		// 如果是文件夹
		if (file.isDirectory()) {
			// 如果文件存在
			if (f.exists()) {
				path = up_path + "/" + file.getName() + "副本";

			} else {
				path = up_path + "/" + file.getName();
			}
			// 先索取它的子目录，如果复制自己到自己的文件夹里面，再遍历子集会出现无限循环
			File subs[] = file.listFiles();
			for (int i = 0; i < subs.length; i++) {
				copyFolder(subs[i], path);
			}
		   f=new File(path);
			f.mkdirs();
		} else {
			File up_file = new File(up_path);
			up_file.mkdirs();
			RandomAccessFile src = new RandomAccessFile(file, "r");
			String path_new = null;
			if (!f.exists()) {
				path_new = up_path + "/" + file.getName();
			} else {
				String[] array = file.getName().split("\\.");
				path_new = up_path + "/" + array[0] + "副本." + array[1];
			}
			RandomAccessFile desc = new RandomAccessFile(path_new, "rw");
			byte[] data = new byte[1024 * 10];
			int len = -1;
			while ((len = src.read(data)) != -1) {
				desc.write(data, 0, len);
			}
			src.close();
			desc.close();
			System.out.println("复制完毕！");
		}
	}

	/*
	 * public static void main(String[] args) throws IOException { File file=new
	 * File("mySystem/home"); System.out.println(file.getAbsolutePath());
	 * copyFolder(file,"/home/soft01/桌面/TEST/mySystem");
	 * 
	 * }
	 * 
	 */
}
