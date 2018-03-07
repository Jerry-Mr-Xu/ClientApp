package com.jerry.clientapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        Button btnAddListener = (Button) findViewById(R.id.btn_add_listener);
        btnAddListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookManager == null) {
                    return;
                }

                try {
                    bookManager.registerListener(listener);
                    Log.e(TAG, "AddListener: ");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private IOnBookAddedListener listener = new IOnBookAddedListener.Stub() {

        @Override
        public void onBookAdded(Book newBook) throws RemoteException {
            Log.e(TAG, "onBookAdded: currentThread = " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
            handler.obtainMessage(100, newBook).sendToTarget();
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Toast.makeText(ClientActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ");
            bookManager = IBookManager.Stub.asInterface(service);
            try {
                service.linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (bookManager == null) {
                return;
            }

            Log.e(TAG, "binderDied: ");
            bookManager.asBinder().unlinkToDeath(deathRecipient, 0);
            bookManager = null;

            bindService();
        }
    };

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }
}
