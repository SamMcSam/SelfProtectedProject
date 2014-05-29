/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * For the printing of each file in the folder (with icon)
 * based on this example : https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/
 */
package selfadaptative.constantino.smartcontentproject;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DirectoryAdapter extends ArrayAdapter<String> {

	private ArrayList<String> objects;

	public DirectoryAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}
	
	//define look of array
	public View getView(int position, View convertView, ViewGroup parent){

		View v = convertView;
	
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_row, null);
		}

		String i = objects.get(position);

		if (i != null) {

			String[] parts = i.split("\\.");
			String namefile = parts[0].trim();
			String extension = parts[1].trim();
			
			ImageView iconSecure = (ImageView) v.findViewById(R.id.iconSecure);
			ImageView iconDraft = (ImageView) v.findViewById(R.id.iconDraft);
			ImageView iconUnknown = (ImageView) v.findViewById(R.id.iconUnknown);
			TextView text = (TextView) v.findViewById(R.id.text1);

			if (text != null){
				text.setText(namefile);
			}
			
			//image selon l'extension
			if (iconSecure != null && iconDraft != null){	
				
				//on cache tout
				iconSecure.setVisibility(View.GONE);
				iconDraft.setVisibility(View.GONE);
				iconUnknown.setVisibility(View.GONE);
				
				if (extension.equals("smart")){
					iconSecure.setVisibility(View.VISIBLE);
				}
				else if (extension.equals("draft")){
					iconDraft.setVisibility(View.VISIBLE);
				}
				else{
					iconUnknown.setVisibility(View.VISIBLE);
				}
			}
		}
		
		return v;
	}

}