// IOnBookAddedListener.aidl
package com.jerry.clientapp;

// Declare any non-default types here with import statements
import com.jerry.serverapp.Book;

interface IOnBookAddedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onBookAdded(in Book newBook);
}