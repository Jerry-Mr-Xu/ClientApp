// IBookManager.aidl
package com.jerry.serverapp;

// Declare any non-default types here with import statements
import com.jerry.serverapp.Book;
import com.jerry.clientapp.IOnBookAddedListener;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
    void addBook(in Book book);

    void registerListener(IOnBookAddedListener listener);
    void unregisterListener(IOnBookAddedListener listener);
}
