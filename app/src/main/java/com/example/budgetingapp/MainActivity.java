package com.example.budgetingapp;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText editTextAmount;
    private EditText editTextCategory;
    private Spinner spinnerType;
    private ListView listViewTransactions;
    private ArrayAdapter<String> transactionAdapter;
    private ArrayList<String> transactionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DatabaseHelper initialization
        dbHelper = new DatabaseHelper(this);

        //Interface elements references
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextCategory = findViewById(R.id.editTextCategory);
        spinnerType = findViewById(R.id.spinnerType);
        listViewTransactions = findViewById(R.id.listViewTransactions);

        //Spinner setup
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.transaction_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spinnerAdapter);

        //Transactions initialization
        transactionList = new ArrayList<>();
        transactionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionList);
        listViewTransactions.setAdapter(transactionAdapter);

        /*
        //Test Transactions List
        dbHelper.addTransaction(4.20, "Spesa", "uscita", "2024-11-04");
        dbHelper.addTransaction(1208.00, "Stipendio", "entrata", "2024-11-04");
         */

        //Button listener
        Button buttonAddTransaction = findViewById(R.id.buttonAddTransaction);
        buttonAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });

        //Load existing transactions in database
        loadTransactions();
    }

        private void addTransaction() {
            String amountStr = editTextAmount.getText().toString();
            String category = editTextCategory.getText().toString();
            String type = spinnerType.getSelectedItem().toString();
            String date = "2024-11-04";

            if(amountStr.isEmpty() || category.isEmpty()){
                Toast.makeText(this, "Inserisci tutti i campi", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try{
                amount = Double.parseDouble(amountStr);
            }
            catch(Exception e){
                Toast.makeText(this, "Amount should be a numeric value!", Toast.LENGTH_LONG).show();
                return;
            }
            dbHelper.addTransaction(amount, category, type, date);
            loadTransactions();
            editTextAmount.setText("");
            editTextCategory.setText("");
            loadTransactions();
        }


        private void loadTransactions() {
            transactionList.clear();
            Cursor cursor = dbHelper.getAllTransactions();
            if (cursor.moveToFirst()) {
                do {
                    try{
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                    double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
                    String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                    transactionList.add("ID: " + id + ", Amount: " + amount + ", Category: " + category + ", Type: " + type + ", Date: " + date);
                }
                    catch(Exception e){
                        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
                    }
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            transactionAdapter.notifyDataSetChanged();
        }
    }
