package me.eldodebug.soarlauncher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFileChooser;

public class FileUtils {

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }

            return directory.delete();
        }
        return false;
    }
    
    public static long getDirectorySize(File directory) {
    	
        long size = 0;

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        size += file.length();
                    } else if (file.isDirectory()) {
                        size += getDirectorySize(file);
                    }
                }
            }
        } else if (directory.isFile()) {
            size = directory.length();
        }

        return size;
    }
    
	public static File selectDirectory() {
		
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int result = fileChooser.showOpenDialog(null);
		
		if(result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		
		return null;
	}
	
    public static void unzip(final File file, final File dest) {
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                final File f = new File(dest, ze.getName());
                if (ze.isDirectory()) {
                    f.mkdirs();
                }
                else {
                    final FileOutputStream fos = new FileOutputStream(f);
                    final byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
            }
            zis.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
	public static InputStream getResource(String path) {
		return FileUtils.class.getResourceAsStream("/" + path);
	}
}
