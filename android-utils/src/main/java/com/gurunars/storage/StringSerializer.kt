package com.gurunars.storage


import android.util.Base64
import android.util.Base64InputStream
import android.util.Base64OutputStream

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.lang.Exception

internal object StringSerializer {

    /** Read the object from Base64 string.  */
    fun <ObjType : Serializable> fromString(serializedObject: String?): ObjType? {
        if (serializedObject == null) {
            return null
        }
        try {
            return ObjectInputStream(
                    Base64InputStream(
                            ByteArrayInputStream(
                                    serializedObject.toByteArray()), Base64.NO_PADDING or Base64.NO_WRAP)
            ).readObject() as ObjType
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /** Write the object to a Base64 string.  */
    fun toString(obj: Serializable?): String? {
        if (obj == null) {
            return null
        }
        try {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(
                    Base64OutputStream(baos, Base64.NO_PADDING or Base64.NO_WRAP))
            oos.writeObject(obj)
            oos.close()
            return baos.toString("UTF-8")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

}