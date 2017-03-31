package com.projet.pidr;


import java.io.File;

import android.os.Environment;

public class AllClass {
	public static String path;
	public static String project="";
    public static String namePressed;
    public static String prise;
    public static String lastPath;

	public static void CreateAllFolders(String path)
	{
     	File photo = new File(path+"/Photo");
     	File recording = new File(path+"/Recording");
     	File note = new File(path+"/Note");
     	if (!photo.exists())
     	 photo.mkdir();
     	if (!recording.exists())
			recording.mkdir();
     	if (!note.exists())
			note.mkdir();
	}
	
	public static void CreatePriseFolder(String path)
	//crée ce que contient une prise ie gps, photo...
	{
     	File gps = new File(path+"/Gps");
     	File orientation_measures = new File(path+"/Mesures d'orientation");
     	/*File faille = new File(path+"/Faille");*/
     	if (!gps.exists())
     	 gps.mkdir();
     	if (!orientation_measures.exists())
			orientation_measures.mkdir();
     	/*if (!faille.exists())
        	 faille.mkdir();*/
     	
	}
	
	public static void DeletePriseFolder(String path)
	//supprime le fichier de chemin passé en paramètre
	{
		
		File file = new File(path);
	    if(file.exists()) {
	        do {
	            delete(file);
	        } while(file.exists());
	    } else {
	        System.out.println("Fichier inexistant");
	    }
	}

		private static void delete(File file) {
			if(file.isDirectory()) {
				String fileList[] = file.list();
				if(fileList.length == 0) {
					// si le dossier on vide est le supprime
					System.out.println("Deleting Directory : "+file.getPath());
					file.delete();
				} else {
					// sinon on supprime tout ce qu'il contient avant de le supprimer lui-même
					int size = fileList.length;
					for(int i = 0 ; i < size ; i++) {
						String fileName = fileList[i];
						System.out.println("File path : "+ file.getPath() + " and name :" + fileName);
						String fullPath = file.getPath() + "/" + fileName;
						File fileOrFolder = new File(fullPath);
						System.out.println("Full Path :"+fileOrFolder.getPath());
						delete(fileOrFolder);
					}
				}
	    } else {
	        System.out.println("Deleting file : "+file.getPath());
	        file.delete();
	    }
	
		
	}
	
	
	public static void CreateAFolder(String path,String name) {
		//crée un dossier au chemin passé en paramètre
     	File NewFolder= new File(Environment.getExternalStorageDirectory().toString()+path+"/"+name);
     	if (!NewFolder.exists()) {
     	  NewFolder.mkdir();
     	}
	}
	
	
}
