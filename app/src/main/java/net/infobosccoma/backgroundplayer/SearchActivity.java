package net.infobosccoma.backgroundplayer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.infobosccoma.backgroundplayer.Model.ItemVideo;
import net.infobosccoma.backgroundplayer.Model.YoutubeConnector;
import net.infobosccoma.backgroundplayer.dao.ResultsSqLiteHelper;
import net.infobosccoma.backgroundplayer.dao.VideoConversor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maxi on 10/02/2015.
 */
public class SearchActivity extends Activity {

    private AutoCompleteTextView searchInput;
    private ListView videosFound;
    final int ID_INTENT=1;
    private Handler handler;
    private ResultsSqLiteHelper helper;
    private VideoConversor videoConversor;
	private boolean writeInDb;
    private List<ItemVideo> searchResults;
    private List<String> listRecommendations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        searchInput = (AutoCompleteTextView) findViewById(R.id.search_input);
        videosFound = (ListView) findViewById(R.id.videos_found);
        handler = new Handler();
        //afegeix listener per poder triar videos del listview
        addClickListener();
        //configura el textview
        setUpSearchInput();
        //configura el textview per poder fer sugerencies
        assignarDadesLlista();

        helper=new ResultsSqLiteHelper(this, "videosInfo.db",null,2);
        videoConversor = new VideoConversor(helper);

        getFromDB();
        if(searchResults==null ||searchResults.size()<1){
            searchOnYoutube(" ");
        }else{
            updateVideosFound();
        }


    }

    private void setUpSearchInput(){
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    searchOnYoutube(v.getText().toString());
                    return false;
                }
                return true;
            }
        });

        searchInput.setThreshold(1);
    }


    /**
     * escriu les dades a la bd
     */
    private void writeDB(){
        videoConversor.save(searchResults);
	    writeInDb=false;
    }


    /**
     * Comprova si hi ha dades a la base de dades, en cas de trobar-ne comprova que aquestes dades no siguin més antiques a 1 dia
     *
     */
    private void getFromDB(){

        Cursor c = videoConversor.getAll();
        ArrayList<ItemVideo> list = new ArrayList<ItemVideo>();

        //comprova que el cursor contingui dades
        if(c!=null && c.getCount()>0) {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
            String strDate = sdfDate.format(new Date());
            c.moveToFirst();

            //-----------------------------------------
            //comprova l'antiguitat d'aquestes dades, en cas de que tinguin més de 1 dia les esborra
            //-----------------------------------------
            if((strDate.toString()).equals(c.getString(c.getColumnIndex("current_date")))){
                writeInDb=false;
                //assigna les dades del cursor a la llista
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    ItemVideo video = new ItemVideo();
                    video.setId(c.getString(c.getColumnIndex("video_id")));
                    video.setTitle(c.getString(c.getColumnIndex("title")));
                    video.setThumbnailURL(c.getString(c.getColumnIndex("thumbnail_url")));
                    list.add(video);
                }
            }else{
                videoConversor.removeAll();
                writeInDb=true;
            }

        }else{
            writeInDb=true;
        }

        searchResults = list;
    }


    /**
     * fa la cerca a youtube amb  la paraula especificada
     * @param keywords
     */
    private void searchOnYoutube(final String keywords){
        new Query().execute(keywords);
    }

    /**
     * Fa la cerca a youtube e insereix la keyword al diccionari de l'usuari en cas de que aquesta no existeixi
     */
    private class Query extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            YoutubeConnector yc = new YoutubeConnector(SearchActivity.this);
            searchResults = yc.search(params[0]);

            if(!wordExists(params[0])){
                insertSuggestion(params[0]);
            }
            handler.post(new Runnable(){
                public void run(){
                    updateVideosFound();
                }
            });
            return null;

        }

    }




    /**
     * insereix la keyword al diccionari de l'usuari
     * @param text
     */
    private void insertSuggestion(String text){
        ContentValues values = new ContentValues();
        values.put(UserDictionary.Words.WORD,text);
        getContentResolver().insert(UserDictionary.Words.CONTENT_URI,values);
    }

    /**
     * comprova les paraules que coincideixen o que comencen amb la paraula especificada al diccionari
     * @param whereClause
     * @return cursor amb les paraules que comencen amb aquest text
     */
    private Cursor enquire(String whereClause) {

        String searchQuery = "word like '"+whereClause
                + "%'";
        String[] mWordListColumns = {
                UserDictionary.Words._ID,
                UserDictionary.Words.WORD,
        };
        // fer la consulta
        Cursor mCursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI, 		// La URI de la taula Words
                mWordListColumns,    // Les columnes de cada fila que s'han de retornar
                searchQuery,     // Criteri de selecció
                null,
                null);    // Criteri d'ordenació de les files
        return mCursor;
    }


    /**
     * comprova si la paraula existeix al diccionari
     * @param word
     * @return
     */
    public boolean wordExists(String word){
        String searchQuery = "word= '"+word
                + "'";
        String[] mWordListColumns = {
                UserDictionary.Words._ID,
                UserDictionary.Words.WORD,
        };
        // fer la consulta
        Cursor mCursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI, 		// La URI de la taula Words
                mWordListColumns,    // Les columnes de cada fila que s'han de retornar
                searchQuery,     // Criteri de selecció
                null,
                null);    // Criteri d'ordenació de les files
        return mCursor.getCount()>0;
    }




/*
 * Crea un adaptador per trectar les sugerencies del TextView
 */
    private void assignarDadesLlista() {
        //Cursor dades = null;
        SimpleCursorAdapter mCursorAdapter;

        // Llista de columens que es volen obtenir amb la consulta
        String[] mWordListColumns = {
                UserDictionary.Words._ID,
                UserDictionary.Words.WORD
        };

        // Llista de ids de les vistes del layout on es mostrarà cada fila del cursor
        int[] mWordListItems = { R.id.textViewSuggestion};

        // Es crea un objecte SimpleCursorAdapter
        mCursorAdapter = new SimpleCursorAdapter(
                getApplicationContext(),               // L'objecte Context de l'aplicació
                R.layout.suggestions_layout,           // Un layout XML per una fila del ListView
                null,                                 // Les dades a mostrar (el cursor de la consulta)
                mWordListColumns,                      // Array d'Strings amb els noms de les columnes
                mWordListItems,                        // Array d'enters amb els ids dels Views on mostrar les dades
                0);                                    // Flags (normalemnt no es necessita)


        //filtre per renovar les sugerencias cada cop que hi ha algun canvi al textview (consulta el diccionari de l'usuari)
        mCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                String searchQuery = "word like '"+str.toString() + "%'";
                String[] mWordListColumns = {
                        UserDictionary.Words._ID,
                        UserDictionary.Words.WORD,
                };

                // fer la consulta
                Cursor mCursor = getContentResolver().query(
                        UserDictionary.Words.CONTENT_URI, 		// La URI de la taula Words
                        mWordListColumns,    // Les columnes de cada fila que s'han de retornar
                        searchQuery,     // Criteri de selecció
                        null,
                        null);    // Criteri d'ordenació de les files
                return mCursor;
            } });

        //canvia la manera de la que es mostren les dades
        mCursorAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndex(UserDictionary.Words.WORD);
                return cur.getString(index);
            }
        });


        searchInput.setAdapter(mCursorAdapter);

        //assigna la paraula triada de la llista de sugerencies al textview
        searchInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the
                // result set

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String text =
                        cursor.getString(cursor.getColumnIndexOrThrow("word"));

                Log.i("nigga_debug", cursor.getColumnName(0));
                Log.i("nigga_debug",cursor.getColumnName(1));

                // Update the parent class's TextView
                searchInput.setText(text);
                searchOnYoutube(text);
            }
        });



    }

    /**
     * actualitza les entrades al listview
     */
    private void updateVideosFound(){
        ArrayAdapter<ItemVideo> adapter = new ArrayAdapter<ItemVideo>(getApplicationContext(), R.layout.search_layout, searchResults){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.search_layout, parent, false);
                }
                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView)convertView.findViewById(R.id.video_title);
                //TextView description = (TextView)convertView.findViewById(R.id.video_description);

                ItemVideo searchResult = searchResults.get(position);

                Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                //description.setText(searchResult.getDescription());
                return convertView;
            }
        };

        if(adapter!=null){
            videosFound.setAdapter(adapter);
            if(writeInDb){
                writeDB();
            }

        }


    }


    /**
     * afegeix el listener al listview per reproduir el video seleccionat de la llista
     */
    private void addClickListener(){
        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), activity_video.class);
                intent.putExtra("DESCRIPTION", searchResults.get(pos).getDescription());
                intent.putExtra("VIDEO_ID", searchResults.get(pos).getId());
                startActivityForResult(intent,ID_INTENT);
            }

        });
    }

}
