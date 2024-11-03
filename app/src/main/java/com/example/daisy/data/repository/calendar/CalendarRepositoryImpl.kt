package com.example.daisy.data.repository.calendar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.daisy.domain.model.Calendar
import com.example.daisy.domain.model.Day
import com.example.daisy.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: StorageReference,
    private val auth: FirebaseAuth
) : CalendarRepository {

    override fun setCalendar(calendar: Calendar, drawing: Bitmap?) {
        val dbCalendars = firestore.collection("calendars")
        val currentUser = auth.currentUser

        val sender = currentUser?.let {
            val name = it.displayName
            val email = it.email
            if (name != null && email != null) {
                User(it.uid, name, email)
            } else {
                null
            }
        }

        sender?.let { user ->
            val updatedCalendar = calendar.copy(sender = user)
            dbCalendars.add(updatedCalendar).addOnSuccessListener {id ->
                drawing?.let { uploadBitmap(it, id.id) }
            }
        }
    }

    override fun getCreatedCalendars(): Flow<List<Calendar?>> = flow {
        val querySnapshot = firestore.collection("calendars")
            .whereEqualTo("sender.uid", auth.currentUser?.uid!!)
            .get(Source.SERVER)
            .await()

        emit(querySnapshot.documents.map {
            it.toObject<Calendar>()?.copy(id = it.id)
        })
    }


    override suspend fun getCalendarDrawing(filename: String): Bitmap? {
        return try {
            val imageRef = storage.child("calendarDrawings/$filename.png")
            val bytes = imageRef.getBytes(Long.MAX_VALUE).await()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            Log.e("CalendarRepository", "Failed to load bitmap: ${e.message}")
            null
        }
    }

    override fun saveModifications(calendar: Calendar) {
        val dbCalendarRef = firestore.collection("calendars").document(calendar.id)

        dbCalendarRef.update(mapOf(
            "title" to calendar.title,
            "icon" to calendar.icon,
            "recipients" to calendar.recipients,
        ))

    }

    override fun saveDayModifications(id: String?, day: Day) {

        val dbDayRef = id?.let { firestore.collection("calendars").document(it) }

        dbDayRef?.update(
            "days.days",
            FieldValue.arrayUnion(
                mapOf(
                    "number" to day.number,
                    "date" to day.date,
                    "message" to day.message
                )
            )
        )
    }

    override fun getCreatedCalendar(id: String): Flow<Calendar?> = flow {
        val querySnapshot = firestore.collection("calendars")
            .document(id)
            .get(Source.SERVER)
            .await()

        emit(querySnapshot.toObject<Calendar>()?.copy(id = querySnapshot.id))
    }



    override fun getCreatedCalendarDay(id: String, number: Int): Flow<Calendar?> = flow {
        val querySnapshot = firestore.collection("calendars")
            .document(id)
            .get(Source.SERVER)
            .await()

        emit(querySnapshot.toObject<Calendar>()?.copy(id = querySnapshot.id))
    }

    override fun getReceivedCalendarDay(id: String, number: Int): Flow<Calendar?> = flow {
        val querySnapshot = firestore.collection("calendars")
            .document(id)
            .get(Source.SERVER)
            .await()

        emit(querySnapshot.toObject<Calendar>()?.copy(id = querySnapshot.id))
    }

    override suspend fun addReceivedCalendarByCode(code: String) {

        val calendarsSnapshot = firestore.collection("calendars")
            .whereEqualTo("code", code)
            .get()
            .await()


        calendarsSnapshot.documents.forEach { document ->
            firestore.collection("calendars")
                .document(document.id)
                .update("recipients", FieldValue.arrayUnion(auth.currentUser?.email!!))
                .await()
        }
    }

    private fun uploadBitmap(
        bitmap: Bitmap,
        fileName: String,
    ) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        val imageRef = storage.child("calendarDrawings/$fileName.png")

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
            }
        }
    }

    override fun getReceivedCalendars(): Flow<List<Calendar?>> {
        return firestore.collection("calendars")
            .whereArrayContains("recipients", auth.currentUser?.email!!)
            .snapshotFlow()
            .map { querySnapshot ->
                querySnapshot.map {
                    it.toObject<Calendar>().copy(id = it.id)
                }
            }
    }

    private fun Query.snapshotFlow(): Flow<QuerySnapshot> = callbackFlow {
        val listenerRegistration = addSnapshotListener { value, error ->
            if (error != null) {
                close()
                return@addSnapshotListener
            }
            if (value != null)
                trySend(value)
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }
}
