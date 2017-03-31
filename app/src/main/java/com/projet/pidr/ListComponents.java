package com.projet.pidr;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class ListComponents extends ListActivity implements OnClickListener{

	//Repr�sente le texte qui s'affiche quand la liste est vide
	protected TextView mEmpty = null;
	//La liste qui contient nos fichiers et r�pertoires
	protected ListView mList = null;
	//Notre Adapter personnalis� qui lie les fichiers � la liste
	protected FileAdapter mAdapter = null;
	//Repr�sente le r�pertoire actuel
	private File mCurrentFile = null;
	//Indique si l'utilisateur est � la racine ou pas pour savoir s'il veut quitter
	protected boolean mCountdown = false;
	protected Button button1;
	protected TextView titleList;
	protected int filesLength=0;
	protected String path="";
	protected int mode;
	protected int pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.listcomponents);
        if (mode==1) {
	        button1=(Button)findViewById(R.id.newComponents); // R�cup�ration de l'instance bouton 1
			titleList=(TextView)findViewById(R.id.titleList); //Instanciation titre (gras en haut)
	        // On r�cup�re la ListView de notre activit�
	        mList = (ListView) getListView();
	        // On v�rifie que le r�pertoire externe est bien accessible
	        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
	        	// S'il ne l'est pas, on affiche un message
	        	mEmpty = (TextView) mList.getEmptyView();
	        	mEmpty.setText("Il n'y a aucun enregistrement");
	        } else {
	        	// S'il l'est...
	        	// On d�clare qu'on veut un menu contextuel sur les �l�ments de la liste
	        	registerForContextMenu(mList);
	        	
	        	// On r�cup�re la racine de la carte SD pour qu'elle soit
	        	mCurrentFile = new File(path);
	        	Log.d("record",path);
	        	// On r�cup�re la liste des fichiers dans le r�pertoire actuel
	        	File[] fichiers = mCurrentFile.listFiles();
	        	//On prend le nombre de fichiers pr�sents
	        	filesLength=fichiers.length;
	        	// On transforme le tableau en une structure de donn�es de taille variable
	        	ArrayList<File> liste = new ArrayList<File>();
	        	for(File f : fichiers)
	        		liste.add(f);
	        	
	        	mAdapter = new FileAdapter(this, android.R.layout.simple_list_item_1, liste);
	        	// On ajoute l'adaptateur � la liste
	        	mList.setAdapter(mAdapter);
	        	// On trie la liste
	        	mAdapter.sort();
	        	
	        	// On ajoute un Listener sur les items de la liste
	        	mList.setOnItemClickListener(new OnItemClickListener() {
	
	        		// Que se passe-il en cas de cas de clic sur un �l�ment de la liste ?
					public void onItemClick(AdapterView<?> adapter, View view,
							int position, long id) {
						File fichier = mAdapter.getItem(position);
						// Si le fichier n'est pas un r�pertoire
						if(!fichier.isDirectory()){
							// On lance le fichier
							seeItem(fichier);
						}
		
					}
				});
	        }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // TODO Auto-generated method stub
	    super.onActivityResult(requestCode, resultCode, data);
	    // On prend la nouveau nombre fichiers
	    filesLength=mCurrentFile.listFiles().length;
	    // Et on recommence pour afficher toute les images
	    File[] fichiers = mCurrentFile.listFiles();
		filesLength=fichiers.length;
		// On transforme le tableau en une structure de donn�es de taille variable
		ArrayList<File> liste = new ArrayList<File>();
		for(File f : fichiers)
			liste.add(f);
		
		mAdapter = new FileAdapter(this, android.R.layout.simple_list_item_1, liste);
		// On ajoute l'adaptateur � la liste
		mList.setAdapter(mAdapter);
		// On trie la liste
		mAdapter.sort();
    }
    
    // Pour visualiser un fichier
   protected void seeItem(File pFile) {
    	// On cr�� un Intent
    	Intent i = new Intent(Intent.ACTION_VIEW);
    	
    	// On d�termine son type MIME
    	MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = pFile.getName().substring(pFile.getName().indexOf(".") + 1).toLowerCase();
        String type = mime.getMimeTypeFromExtension(ext);
    	if (type == null || type.equals("")) {
        	//Log.d("dossier",mAdapter.getItem(pos).getName());
    		Intent intent = new Intent(getBaseContext(), MainPrise.class);
    		AllClass.path+="/"+mAdapter.getItem(pos).getName();
    		intent.putExtra("Folder", mAdapter.getItem(pos).getName());
    		startActivity(intent);
    	} else{
	        // On ajoute les informations n�cessaires
	    	i.setDataAndType(Uri.fromFile(pFile), type);
	
	    	try {
	    		// On lance l'activit�
				startActivity(i);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
    	}
    }
    
    
    //On enl�ve tous les �l�ments de la liste
    public void setEmpty() {
    	// Si l'adapteur n'est pas vide...
    	if(!mAdapter.isEmpty())
    		// Alors on le vide !
    		mAdapter.clear();
    }
    
    //L'adaptateur sp�cifique � nos fichiers
    
    protected class FileAdapter extends ArrayAdapter<File> {

    	//Permet de comparer deux fichiers
        private class FileComparator implements Comparator<File> {

    		public int compare(File lhs, File rhs) {
    			// si lhs est un r�pertoire et pas l'autre, il est plus petit
    			if(lhs.isDirectory() && rhs.isFile())
    				return -1;
    			// dans le cas inverse, il est plus grand
    			if(lhs.isFile() && rhs.isDirectory())
    				return 1;
    			
    			//Enfin on ordonne en fonction de l'ordre alphab�tique sans tenir compte de la casse
    			return lhs.getName().compareToIgnoreCase(rhs.getName());
    		}
        	
        }
        
    	public FileAdapter(Context context, int textViewResourceId, List<File> objects) {
			super(context, textViewResourceId, objects);
			mInflater = LayoutInflater.from(context);
		}

		private LayoutInflater mInflater = null;
		
		//Construit la vue en fonction de l'item
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView vue = null;
			
			if(convertView != null)
				vue = (TextView) convertView;
			else
				vue = (TextView) mInflater.inflate(android.R.layout.simple_list_item_1, null);
			File item = getItem(position);
			//Si context'est un r�pertoire, on choisit la couleur dans les pr�f�rences
			if(item.isDirectory())
				vue.setTextColor(Color.BLACK);
			else
				// Sinon context'est du noir
				vue.setTextColor(Color.BLACK);
			vue.setText(item.getName());
			return vue;
		}
		
		//Pour trier rapidement les �l�ments de l'adaptateur
		public void sort () {
			super.sort(new FileComparator());
		}
    }
    

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
