package com.projet.pidr;

import java.io.File;
import java.util.ArrayList;

//import com.projet.pidr.ListComponents.FileAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class 	FolderComponents extends ListComponents implements DialogInterface.OnClickListener,OnClickListener {
	
    private AlertDialog.Builder alert;
    String name;
	private EditText input;
	Button buttonDelete;
	Button buttonNew;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.mode=0;
        setContentView(R.layout.listcomponents);
        buttonNew=(Button)findViewById(R.id.newComponents); // R�cup�ration de l'instance bouton 1
        // On r�cup�re la ListView de notre activit�
        buttonDelete =(Button)findViewById(R.id.deleteProject);
        mList = (ListView) getListView();
        // On v�rifie que le r�pertoire externe est bien accessible

		if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
         	// S'il ne l'est pas, on affiche un message
         	mEmpty = (TextView) mList.getEmptyView();
            mEmpty.setText("Il n'y a aucun enregistrement");
        } else  {
         	// S'il l'est...
         	// On d�clare qu'on veut un menu contextuel sur les �l�ments de la liste
         	registerForContextMenu(mList);
         	AllClass.path="/Pidr";
         	// On r�cup�re la racine de la carte SD pour qu'elle soit
         	File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Pidr");
         	folder.mkdirs();
         	// On r�cup�re la liste des fichiers dans le r�pertoire actuel
         	File[] fichiers = folder.listFiles();
         	//On prend le nombre de fichiers pr�sents
         	filesLength=fichiers.length;
         	// On transforme le tableau en une structure de donn�es de taille variable
         	ArrayList<File> liste = new ArrayList<File>();

         	for(File f : fichiers) {
                liste.add(f);
            }

         	mAdapter = new FileAdapter(this, android.R.layout.simple_list_item_1, liste);
         	// On ajoute l'adaptateur � la liste
         	mList.setAdapter(mAdapter);
         	// On trie la liste
         	mAdapter.sort();
         	
         	// On ajoute un Listenezr sur les items de la liste
         	mList.setOnItemClickListener(new OnItemClickListener() {

                // Que se passe-il en cas de cas de clic sur un �l�ment de la liste ?
                public void onItemClick(AdapterView<?> adapter, View view,
                                        int position, long id) {
                    File fichier = mAdapter.getItem(position);
                    pos = position;
                    // Si le fichier n'est pas un r�pertoire
                    // On lance le fichier
                    seeItem(fichier);
                }
            });
        }

        this.buttonNew.setText("Nouveau");
        this.buttonNew.setOnClickListener(this);
        this.buttonDelete.setText("Supprimer");
        this.buttonDelete.setOnClickListener(this);
        super.setTitle("Folder");
    }
    
    
	@Override
	public void onClick(View v) {
        int id = v.getId();
		if (id == (R.id.newComponents)) {
			alert = new AlertDialog.Builder(this);
			alert.setTitle("Veuillez entrez le nom du projet");
			alert.setMessage("Nom");
			// Set an EditText view to get user input 
            input = new EditText(this);
			alert.setView(input);
			alert.setNegativeButton("Annuler",this);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
			  {
			    public void onClick(DialogInterface dialog, int whichButton) 
			    {
                    name = input.getText().toString();
					File folder = new File(Environment.getExternalStorageDirectory().toString()+"/Pidr/"+name);
					folder.mkdir();
					Intent intent = getIntent();
					finish();
					startActivity(intent);
			    }
			  });
		} else if (id == (R.id.deleteProject)) {
			alert = new AlertDialog.Builder(this);
			alert.setTitle("Voulez vous supprimer le dossier");
			alert.setMessage("Nom du dossier a supprimer");
			// Set an EditText view to get user input 
            input = new EditText(this);
			alert.setView(input);
			alert.setNegativeButton("Annuler",this);
			alert.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() 
			  {
			    public void onClick(DialogInterface dialog, int whichButton) 
			    {
			    	name = input.getText().toString();
			    	String nameWithPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Pidr/"+name;
			    	//la condition dans le if ne fonctionne pas, mais un mauvais non ne fait pas bugguer le programme
			    	
			    	/*File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Pidr");
			     	// folder.mkdirs();
			     	// On recupere la liste des fichiers dans le repertoire actuel
			     	File[] fichiers = folder.listFiles();
			     	boolean isAFolder = false;
			     	for (int i=0; i<fichiers.length;i++){
			     		if (nameWithPath == fichiers[i].toString()){
			     			isAFolder = true;
			     		}
			     	}
			     	if (isAFolder){ */
			     		AllClass.DeletePriseFolder(nameWithPath);
			     		Intent intent = getIntent();
						finish();
						startActivity(intent);
			     //	}
				}
			  });
		}
	      alert.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}