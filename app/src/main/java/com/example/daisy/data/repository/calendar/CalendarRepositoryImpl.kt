package com.example.daisy.data.repository.calendar

import android.util.Log
import androidx.compose.ui.platform.LocalFocusManager
import com.example.daisy.domain.model.Calendar
import com.example.daisy.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CalendarRepository {

    override fun setCalendar(calendar: Calendar) {
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
            dbCalendars.add(updatedCalendar)
        }
    }

    override fun getCreatedCalendars(): Flow<List<Calendar?>> {
        return firestore.collection("calendars")
            .whereEqualTo("sender.uid", auth.currentUser?.uid)
            .snapshotFlow()
            .map { querySnapshot ->
                querySnapshot.documents.map { it.toObject<Calendar>() }
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
