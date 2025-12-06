package com.kidslearning.app.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.kidslearning.app.data.model.Letter;
import com.kidslearning.app.data.model.LetterLanguage;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LetterDao_Impl implements LetterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Letter> __insertionAdapterOfLetter;

  private final EntityDeletionOrUpdateAdapter<Letter> __deletionAdapterOfLetter;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllLetters;

  public LetterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLetter = new EntityInsertionAdapter<Letter>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `letters` (`id`,`character`,`language`,`soundFileName`,`soundUrl`,`pronunciation`,`order`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Letter entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCharacter());
        statement.bindString(3, __LetterLanguage_enumToString(entity.getLanguage()));
        statement.bindString(4, entity.getSoundFileName());
        if (entity.getSoundUrl() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSoundUrl());
        }
        statement.bindString(6, entity.getPronunciation());
        statement.bindLong(7, entity.getOrder());
      }
    };
    this.__deletionAdapterOfLetter = new EntityDeletionOrUpdateAdapter<Letter>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `letters` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Letter entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllLetters = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM letters";
        return _query;
      }
    };
  }

  @Override
  public Object insertLetters(final List<Letter> letters,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLetter.insert(letters);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLetter(final Letter letter, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfLetter.handle(letter);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllLetters(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllLetters.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllLetters.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Letter>> getLettersByLanguage(final LetterLanguage language) {
    final String _sql = "SELECT * FROM letters WHERE language = ? ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, __LetterLanguage_enumToString(language));
    return CoroutinesRoom.createFlow(__db, false, new String[] {"letters"}, new Callable<List<Letter>>() {
      @Override
      @NonNull
      public List<Letter> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCharacter = CursorUtil.getColumnIndexOrThrow(_cursor, "character");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfSoundFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "soundFileName");
          final int _cursorIndexOfSoundUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUrl");
          final int _cursorIndexOfPronunciation = CursorUtil.getColumnIndexOrThrow(_cursor, "pronunciation");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final List<Letter> _result = new ArrayList<Letter>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Letter _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCharacter;
            _tmpCharacter = _cursor.getString(_cursorIndexOfCharacter);
            final LetterLanguage _tmpLanguage;
            _tmpLanguage = __LetterLanguage_stringToEnum(_cursor.getString(_cursorIndexOfLanguage));
            final String _tmpSoundFileName;
            _tmpSoundFileName = _cursor.getString(_cursorIndexOfSoundFileName);
            final String _tmpSoundUrl;
            if (_cursor.isNull(_cursorIndexOfSoundUrl)) {
              _tmpSoundUrl = null;
            } else {
              _tmpSoundUrl = _cursor.getString(_cursorIndexOfSoundUrl);
            }
            final String _tmpPronunciation;
            _tmpPronunciation = _cursor.getString(_cursorIndexOfPronunciation);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            _item = new Letter(_tmpId,_tmpCharacter,_tmpLanguage,_tmpSoundFileName,_tmpSoundUrl,_tmpPronunciation,_tmpOrder);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLetterById(final int letterId, final Continuation<? super Letter> $completion) {
    final String _sql = "SELECT * FROM letters WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, letterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Letter>() {
      @Override
      @Nullable
      public Letter call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCharacter = CursorUtil.getColumnIndexOrThrow(_cursor, "character");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfSoundFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "soundFileName");
          final int _cursorIndexOfSoundUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUrl");
          final int _cursorIndexOfPronunciation = CursorUtil.getColumnIndexOrThrow(_cursor, "pronunciation");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final Letter _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCharacter;
            _tmpCharacter = _cursor.getString(_cursorIndexOfCharacter);
            final LetterLanguage _tmpLanguage;
            _tmpLanguage = __LetterLanguage_stringToEnum(_cursor.getString(_cursorIndexOfLanguage));
            final String _tmpSoundFileName;
            _tmpSoundFileName = _cursor.getString(_cursorIndexOfSoundFileName);
            final String _tmpSoundUrl;
            if (_cursor.isNull(_cursorIndexOfSoundUrl)) {
              _tmpSoundUrl = null;
            } else {
              _tmpSoundUrl = _cursor.getString(_cursorIndexOfSoundUrl);
            }
            final String _tmpPronunciation;
            _tmpPronunciation = _cursor.getString(_cursorIndexOfPronunciation);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            _result = new Letter(_tmpId,_tmpCharacter,_tmpLanguage,_tmpSoundFileName,_tmpSoundUrl,_tmpPronunciation,_tmpOrder);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLetterCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM letters";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __LetterLanguage_enumToString(@NonNull final LetterLanguage _value) {
    switch (_value) {
      case ARABIC: return "ARABIC";
      case FRENCH: return "FRENCH";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private LetterLanguage __LetterLanguage_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "ARABIC": return LetterLanguage.ARABIC;
      case "FRENCH": return LetterLanguage.FRENCH;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
