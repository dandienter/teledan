/*
 * This file is a part of Telegram X
 * Copyright © 2014 (tgx-android@pm.me)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.thunderdog.challegram.telegram;

import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.drinkless.tdlib.TdApi;
import org.thunderdog.challegram.Log;
import org.thunderdog.challegram.U;
import org.thunderdog.challegram.core.Background;
import org.thunderdog.challegram.data.TD;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import me.vkryl.core.FileUtils;
import me.vkryl.core.StringUtils;
import me.vkryl.core.lambda.RunnableData;

// TeleDan: shared helper for the MyTelegram media features. Copies an
// already downloaded TDLib file into the public "MyTelegram" folder of the
// matching media directory (Pictures / Movies / Music), using the same
// plain-file mechanism as the built-in "save to downloads" flow. Copies run
// on a background thread and fail silently (logged), so they depend on the
// app holding storage access, exactly like the built-in flow does by the
// time it copies. Automatic saves (view-once, auto-save) are de-duplicated
// per file id for the lifetime of the process.
public class TeleDanSaver {
  public static final int TYPE_PHOTO = 0;
  public static final int TYPE_VIDEO = 1;
  public static final int TYPE_AUDIO = 2;

  private static final Set<String> autoSavedFiles = Collections.synchronizedSet(new HashSet<>());

  private TeleDanSaver () { }

  private static File destinationDirectory (int type) {
    final String root;
    switch (type) {
      case TYPE_VIDEO:
        root = Environment.DIRECTORY_MOVIES;
        break;
      case TYPE_AUDIO:
        root = Environment.DIRECTORY_MUSIC;
        break;
      case TYPE_PHOTO:
      default:
        root = Environment.DIRECTORY_PICTURES;
        break;
    }
    return new File(Environment.getExternalStoragePublicDirectory(root), "MyTelegram");
  }

  // Copies the file with its original name. Used by the automatic paths.
  public static void saveFile (@NonNull TdApi.File file, int type) {
    saveFile(file, type, null, null);
  }

  // Copies the file into the public MyTelegram folder. When preferredName
  // is null the source file name is kept and the copy is treated as
  // automatic (de-duplicated per file id). The optional callback receives
  // the destination file, or null when nothing was saved, and runs on a
  // background thread.
  public static void saveFile (@NonNull TdApi.File file, int type, @Nullable String preferredName, @Nullable RunnableData<File> after) {
    if (!TD.isFileLoaded(file) || file.local == null || StringUtils.isEmpty(file.local.path)) {
      if (after != null) {
        after.runWithData(null);
      }
      return;
    }
    final boolean automatic = preferredName == null;
    final String dedupKey = type + ":" + file.id;
    if (automatic && !autoSavedFiles.add(dedupKey)) {
      return;
    }
    final File sourceFile = new File(file.local.path);
    String name = preferredName;
    if (StringUtils.isEmpty(name)) {
      name = sourceFile.getName();
    } else if (StringUtils.isEmpty(U.getExtension(name))) {
      String extension = U.getExtension(sourceFile.getName());
      if (!StringUtils.isEmpty(extension)) {
        name = name + "." + extension;
      }
    }
    final String destName = name;
    Background.instance().post(() -> {
      File destFile = null;
      try {
        File destDir = destinationDirectory(type);
        if (FileUtils.createDirectory(destDir)) {
          File candidate = new File(destDir, destName);
          if (candidate.exists()) {
            destFile = candidate;
          } else if (FileUtils.copy(sourceFile, candidate)) {
            U.scanFile(candidate);
            if (type == TYPE_AUDIO) {
              U.addToGallery(candidate);
            }
            destFile = candidate;
          }
        }
      } catch (Throwable t) {
        Log.w("TeleDan: failed to save file to MyTelegram folder", t);
      }
      if (after != null) {
        after.runWithData(destFile);
      }
    });
  }

  // TeleDan: entry point for the "Auto-save media to gallery" toggle.
  // Decides from the file name extension whether a finished download is a
  // photo or a video worth keeping. The files layer cannot tell message
  // media apart from other downloads of the same type (e.g. profile
  // photos), and .webp is deliberately skipped so stickers and animated
  // emoji are not saved.
  public static void saveDownloadedFile (@NonNull TdApi.File file) {
    if (!TD.isFileLoaded(file) || file.local == null || StringUtils.isEmpty(file.local.path)) {
      return;
    }
    if (file.remote != null && file.remote.isUploadingCompleted) {
      return; // an outgoing file, not a download
    }
    String extension = U.getExtension(file.local.path);
    if (StringUtils.isEmpty(extension)) {
      return;
    }
    if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png")) {
      saveFile(file, TYPE_PHOTO);
    } else if (extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("mkv") || extension.equalsIgnoreCase("mov") || extension.equalsIgnoreCase("webm")) {
      saveFile(file, TYPE_VIDEO);
    }
  }

  // TeleDan: writes a text file into the public Download/MyTelegram
  // folder (used by the chat backup, feature 31). Runs on a background
  // thread; the callback receives the written file, or null on failure,
  // also on a background thread.
  public static void saveTextFile (@NonNull String name, @NonNull String content, @Nullable RunnableData<File> after) {
    Background.instance().post(() -> {
      File destFile = null;
      try {
        File destDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "MyTelegram");
        if (FileUtils.createDirectory(destDir)) {
          File candidate = new File(destDir, name);
          java.io.FileOutputStream stream = new java.io.FileOutputStream(candidate);
          try {
            stream.write(content.getBytes(java.nio.charset.StandardCharsets.UTF_8));
          } finally {
            stream.close();
          }
          U.scanFile(candidate);
          destFile = candidate;
        }
      } catch (Throwable t) {
        Log.w("TeleDan: failed to save text file to MyTelegram folder", t);
      }
      if (after != null) {
        after.runWithData(destFile);
      }
    });
  }
}
