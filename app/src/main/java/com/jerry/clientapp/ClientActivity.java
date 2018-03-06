package com.jerry.clientapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jerry.serverapp.Book;
import com.jerry.serverapp.IBookManager;

import java.util.List;

public class ClientActivity extends AppCompatActivity {
    private static final String TAG = ClientActivity.class.getSimpleName();

    private IBookManager bookManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        initView();

        bindService();
    }

    private void initView() {
        Button btnGetBookList = (Button) findViewById(R.id.btn_get_book_list);
        btnGetBookList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookManager == null) {
                    return;
                }

                try {
                    List<Book> bookList = bookManager.getBookList();
                    if (bookList == null) {
                        return;
                    }
                    for (int i = 0; i < bookList.size(); i++) {
                        Log.e(TAG, "bookList[" + i + "]: " + bookList.get(i).toString());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnAddBook = (Button) findViewById(R.id.btn_add_book);
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookManager == null) {
                    return;
                }

                Book newBook = new Book("New Book", "New Author", 10.4f);
                try {
                    bookManager.addBook(newBook);
                    Log.e(TAG, "AddBook: " + newBook.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManager = null;
        }
    };

    private void bindService() {
        Intent intent = new Intent("com.jerry.serverapp");
        intent.setPackage("com.jerry.serverapp");
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }
}
