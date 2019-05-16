package com.pmnm.risk.network;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileReader {
	FileInputStream fis;
	FileOutputStream fos;
	
	public FileReader(String addressToRead, String addressToWrite) throws FileNotFoundException {
		//fis = new FileInputStream(addressToRead);
		fos = new FileOutputStream(addressToWrite);
	}

	public FileInputStream getFis() {
		return fis;
	}

	public void setFis(String fisName) throws FileNotFoundException {
		fis = new FileInputStream(fisName);
	}

	public FileOutputStream getFos() {
		return fos;
	}

	public void setFos(FileOutputStream fos) {
		this.fos = fos;
	}
	
	public void finishFos() throws IOException {
		fos.close();
	}
	
	public void finishFis() throws IOException {
		fis.close();
	}
	
	
	
	

}
