package edu.byui.cs246.barprogress;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Integer> adapter;
    ListView loadView;
    private ProgressBar firstBar = null;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadView = (ListView) findViewById(R.id.listView);
        firstBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void createFile (View V) {
        String fileName = "numbers.txt";
        new CreateFile().execute(fileName);
    }
    public void loadFile(View V) {
        String fileName = "numbers.txt";
        new LoadFile().execute(fileName);
    }
    public void clear(View view){
        if(adapter != null)
            adapter.clear();
    }

    class LoadFile extends AsyncTask<String, Integer, ArrayAdapter<Integer>> {

        @Override
        protected ArrayAdapter<Integer> doInBackground(String... fileName) {

            ArrayList<Integer> items = new ArrayList<>();

            try (Scanner scanner = new Scanner(new File(getFilesDir(), fileName[0]))){// try to open the file
                System.out.println(fileName[0]);
                while (scanner.hasNextInt()){// Loop through each line
                    items.add(scanner.nextInt());
                    i++;
                    publishProgress(i);
                    Thread.sleep(250);
                }
                 adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, items);

            } catch (InterruptedException e) {

            } catch (FileNotFoundException e) {// if fails: print error and exit
                System.out.println("Error1 reading file '" + fileName[0] + "'..." );
                System.exit(1);
            }


            return adapter;
        }
        protected void onProgressUpdate(Integer...progress){
            if (progress[0] == 1) {
                //make the progress bar visible
                firstBar.setVisibility(View.VISIBLE);
                firstBar.setMax(10);
            }

            else if (progress[0] < firstBar.getMax() ) {
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
        protected void onPostExecute(ArrayAdapter<Integer> result){
            loadView.setAdapter(result);
        }
    }

    class CreateFile extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... fileName) {

            try (FileWriter writer = new FileWriter(new File(getFilesDir(), fileName[0]))){

                for (int i = 1; i <= 10; i++) {
                    writer.write(i + System.getProperty("line.separator"));
                    publishProgress(i);
                    Thread.sleep(250);
                }

            } catch (InterruptedException e) {
            }catch (Exception ex){System.out.println("We fail managing files");}
            return null;
        }
        protected void onProgressUpdate(Integer...progress){

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


}

