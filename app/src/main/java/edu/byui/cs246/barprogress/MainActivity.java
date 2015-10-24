package edu.byui.cs246.barprogress;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView loadView;
    private ProgressBar firstBar = null;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void createFile (View V) {
        String fileName = "numbers.txt";
        File file = new File(getFilesDir(), fileName);
        new CreateFile().execute(fileName);
    }
    public void loadFile(View V) {
        String fileName = "numbers.txt";
        loadView = (ListView) findViewById(R.id.listView);
        //File file = new File(getFilesDir(), fileName);
        new LoadFile().execute(fileName);
    }
    public void clear(View view){
        adapter.clear();
    }

    class CreateFile extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... fileName) {
            FileOutputStream outputStream;
            try {
//            String fileName = "numbers.txt";
//            File file = new File(MainActivity.getFilesDir(), fileName);
                outputStream = openFileOutput(fileName[0], Context.MODE_PRIVATE);
                for (int i = 1; i <= 10; i++) {
                    outputStream.write(i);
                    publishProgress(i);
                    Thread.sleep(250);
                }
                outputStream.close();
            } catch (InterruptedException e) {
            }catch (Exception ex){System.out.println("We fail managing files");}
            return null;
        }
        protected void onProgressUpdate(Integer...progress){
            //i = progress[0] * 10;
            if (progress[0] == 1) {
                //make the progress bar visible
                firstBar.setVisibility(View.VISIBLE);
                firstBar.setMax(10);
            }

            else if ( progress[0] < firstBar.getMax() ) {
                //Set first progress bar value
                firstBar.setProgress(progress[0]);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(progress[0] + 10);
            }
            else if (progress[0] == firstBar.getMax()){
                firstBar.setProgress(0);
                firstBar.setSecondaryProgress(0);
                firstBar.setVisibility(View.GONE);

            }

            System.out.println(progress[0]);

        }
    }

    class LoadFile extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... fileName) {

            ArrayList<String> items = new ArrayList<String>();
            String line;
            try {// try to open the file
                BufferedReader reader = new BufferedReader(new FileReader(fileName[0]));
                while ((line = reader.readLine()) != null){// Loop through each line
                    items.add(line);
                    i++;
                    publishProgress(i);
                    Thread.sleep(250);
                }
                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, items);
                loadView.setAdapter(adapter);


            } catch (InterruptedException e) {

            } catch (FileNotFoundException e) {// if fails: print error and exit
                System.out.println("Error reading file '" + fileName + "'..." );
                System.exit(1);
            }
            catch (IOException ex) {
                System.out.println("Error reading file '" + fileName + "'..." );
                System.exit(1);
            }

            return null;
        }
        protected void onProgressUpdate(Integer...progress){
            //i = progress[0] * 10;
            if (progress[0] == 1) {
                //make the progress bar visible
                firstBar.setVisibility(View.VISIBLE);
                firstBar.setMax(10);
            }

            else if ( progress[0] < firstBar.getMax() ) {
                //Set first progress bar value
                firstBar.setProgress(progress[0]);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(progress[0] + 10);
            }
            else if (progress[0] == firstBar.getMax()){
                firstBar.setProgress(0);
                firstBar.setSecondaryProgress(0);
                firstBar.setVisibility(View.GONE);

            }

            System.out.println(i);

        }
    }
}

