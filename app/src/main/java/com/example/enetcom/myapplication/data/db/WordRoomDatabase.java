package com.example.enetcom.myapplication.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.enetcom.myapplication.WordViewModel;
import com.example.enetcom.myapplication.data.WordDao;
import com.example.enetcom.myapplication.model.word;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {word.class}, version = 1, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {

    public abstract WordDao wordDao();

    private static volatile WordRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WordRoomDatabase.class, "word_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // Si vous souhaitez conserver les données au redémarrage de l'application
            // commentez le bloc suivant
            databaseWriteExecutor.execute(() -> {
                // Remplir la base de données en arrière-plan
                // Si vous voulez commencer avec plus de mots, il suffit de les ajouter.
                WordDao dao = INSTANCE.wordDao();
                dao.deleteAll();

                word word = new word("Hello");
                dao.insert(word);
                word = new word("Hichem");
                dao.insert(word);
            });
        }
    };
    databaseWriteExecutor.execute(() -> {
        // Remplir la base de données en arrière-plan
        // Si vous voulez commencer avec plus de mots, il suffit de les ajouter.
        WordDao dao = INSTANCE.wordDao();
        // Si nous n'avons pas de mots, alors créons la liste initiale de mots.
        if (dao.getAnyWord().length < 1) {
            word word = new word("Hello");
            dao.insert(word);
            word = new word("World");
            dao.insert(word);
        }
    });
}
