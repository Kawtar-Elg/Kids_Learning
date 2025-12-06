package com.kidslearning.app.data.database;

import android.database.Cursor;
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
import com.kidslearning.app.data.model.UserProgress;
import java.lang.Class;
import java.lang.Exception;
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
public final class ProgressDao_Impl implements ProgressDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserProgress> __insertionAdapterOfUserProgress;

  private final EntityDeletionOrUpdateAdapter<UserProgress> __updateAdapterOfUserProgress;

  private final SharedSQLiteStatement __preparedStmtOfIncrementPracticeCount;

  public ProgressDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserProgress = new EntityInsertionAdapter<UserProgress>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_progress` (`letterId`,`timesDrawn`,`lastPracticed`,`masteryLevel`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProgress entity) {
        statement.bindLong(1, entity.getLetterId());
        statement.bindLong(2, entity.getTimesDrawn());
        statement.bindLong(3, entity.getLastPracticed());
        statement.bindLong(4, entity.getMasteryLevel());
      }
    };
    this.__updateAdapterOfUserProgress = new EntityDeletionOrUpdateAdapter<UserProgress>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `user_progress` SET `letterId` = ?,`timesDrawn` = ?,`lastPracticed` = ?,`masteryLevel` = ? WHERE `letterId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProgress entity) {
        statement.bindLong(1, entity.getLetterId());
        statement.bindLong(2, entity.getTimesDrawn());
        statement.bindLong(3, entity.getLastPracticed());
        statement.bindLong(4, entity.getMasteryLevel());
        statement.bindLong(5, entity.getLetterId());
      }
    };
    this.__preparedStmtOfIncrementPracticeCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_progress SET timesDrawn = timesDrawn + 1, lastPracticed = ? WHERE letterId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertProgress(final UserProgress progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserProgress.insert(progress);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProgress(final UserProgress progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserProgress.handle(progress);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementPracticeCount(final int letterId, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementPracticeCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, letterId);
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
          __preparedStmtOfIncrementPracticeCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserProgress> getProgress(final int letterId) {
    final String _sql = "SELECT * FROM user_progress WHERE letterId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, letterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_progress"}, new Callable<UserProgress>() {
      @Override
      @Nullable
      public UserProgress call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLetterId = CursorUtil.getColumnIndexOrThrow(_cursor, "letterId");
          final int _cursorIndexOfTimesDrawn = CursorUtil.getColumnIndexOrThrow(_cursor, "timesDrawn");
          final int _cursorIndexOfLastPracticed = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPracticed");
          final int _cursorIndexOfMasteryLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "masteryLevel");
          final UserProgress _result;
          if (_cursor.moveToFirst()) {
            final int _tmpLetterId;
            _tmpLetterId = _cursor.getInt(_cursorIndexOfLetterId);
            final int _tmpTimesDrawn;
            _tmpTimesDrawn = _cursor.getInt(_cursorIndexOfTimesDrawn);
            final long _tmpLastPracticed;
            _tmpLastPracticed = _cursor.getLong(_cursorIndexOfLastPracticed);
            final int _tmpMasteryLevel;
            _tmpMasteryLevel = _cursor.getInt(_cursorIndexOfMasteryLevel);
            _result = new UserProgress(_tmpLetterId,_tmpTimesDrawn,_tmpLastPracticed,_tmpMasteryLevel);
          } else {
            _result = null;
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
  public Flow<List<UserProgress>> getRecentProgress() {
    final String _sql = "SELECT * FROM user_progress ORDER BY lastPracticed DESC LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_progress"}, new Callable<List<UserProgress>>() {
      @Override
      @NonNull
      public List<UserProgress> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLetterId = CursorUtil.getColumnIndexOrThrow(_cursor, "letterId");
          final int _cursorIndexOfTimesDrawn = CursorUtil.getColumnIndexOrThrow(_cursor, "timesDrawn");
          final int _cursorIndexOfLastPracticed = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPracticed");
          final int _cursorIndexOfMasteryLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "masteryLevel");
          final List<UserProgress> _result = new ArrayList<UserProgress>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserProgress _item;
            final int _tmpLetterId;
            _tmpLetterId = _cursor.getInt(_cursorIndexOfLetterId);
            final int _tmpTimesDrawn;
            _tmpTimesDrawn = _cursor.getInt(_cursorIndexOfTimesDrawn);
            final long _tmpLastPracticed;
            _tmpLastPracticed = _cursor.getLong(_cursorIndexOfLastPracticed);
            final int _tmpMasteryLevel;
            _tmpMasteryLevel = _cursor.getInt(_cursorIndexOfMasteryLevel);
            _item = new UserProgress(_tmpLetterId,_tmpTimesDrawn,_tmpLastPracticed,_tmpMasteryLevel);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
