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

import org.drinkless.tdlib.TdApi;
import org.thunderdog.challegram.unsorted.Settings;

import java.util.HashMap;
import java.util.Map;

import me.vkryl.core.StringUtils;
import tgx.td.ChatId;
import tgx.td.Td;
import tgx.td.TdConstants;

// TeleDan: Auto-reply (feature 32). Called from Tdlib.updateNewMessage for
// every incoming message of every account. Replies with the user-defined
// text to fresh messages in regular private chats, at most once per
// minute per chat, and only while the app process is alive (there is no
// background service for this; messages arriving while the app is closed
// are never answered, and the freshness check below also stops it from
// answering the backlog that syncs in when the app starts).
//
// Loop safety: outgoing messages (including the auto-replies themselves)
// are ignored, bots are ignored when their user object is known, the
// Telegram service account is ignored, and service/system message
// contents are excluded by only accepting ordinary content types.
public class TeleDanAutoReply {
  private static final long MIN_INTERVAL_PER_CHAT_MS = 60_000L;
  private static final long MAX_MESSAGE_AGE_SECONDS = 120L;

  private static final Map<Long, Long> lastReplyTimeByChat = new HashMap<>();

  private TeleDanAutoReply () { }

  public static void onNewMessage (Tdlib tdlib, TdApi.Message message) {
    if (!Settings.instance().autoReply()) {
      return;
    }
    String replyText = Settings.instance().autoReplyText();
    if (StringUtils.isEmpty(replyText) || StringUtils.isEmpty(replyText.trim())) {
      return;
    }
    if (message.isOutgoing || message.sendingState != null) {
      return;
    }
    // Private chats only, and not secret chats.
    if (!ChatId.isUserChat(message.chatId) || ChatId.isSecret(message.chatId)) {
      return;
    }
    if (!(message.senderId instanceof TdApi.MessageSenderUser)) {
      return;
    }
    long senderUserId = ((TdApi.MessageSenderUser) message.senderId).userId;
    if (senderUserId == tdlib.myUserId() || senderUserId == TdConstants.TELEGRAM_ACCOUNT_ID) {
      return;
    }
    // Never answer bots (when the user object is already cached; if it is
    // not, the per-chat cooldown still prevents any reply storm).
    TdApi.User sender = tdlib.cache().user(senderUserId);
    if (sender != null && sender.type != null && sender.type.getConstructor() == TdApi.UserTypeBot.CONSTRUCTOR) {
      return;
    }
    // Ordinary content only; anything else is a service/system message.
    switch (message.content.getConstructor()) {
      case TdApi.MessageText.CONSTRUCTOR:
      case TdApi.MessagePhoto.CONSTRUCTOR:
      case TdApi.MessageVideo.CONSTRUCTOR:
      case TdApi.MessageDocument.CONSTRUCTOR:
      case TdApi.MessageAudio.CONSTRUCTOR:
      case TdApi.MessageVoiceNote.CONSTRUCTOR:
      case TdApi.MessageVideoNote.CONSTRUCTOR:
      case TdApi.MessageAnimation.CONSTRUCTOR:
      case TdApi.MessageSticker.CONSTRUCTOR:
      case TdApi.MessageLocation.CONSTRUCTOR:
      case TdApi.MessageContact.CONSTRUCTOR:
      case TdApi.MessagePoll.CONSTRUCTOR:
        break;
      default:
        return;
    }
    // Only answer messages that just arrived, never synced history.
    long nowSeconds = System.currentTimeMillis() / 1000L;
    if (message.date > nowSeconds + 60L || nowSeconds - message.date > MAX_MESSAGE_AGE_SECONDS) {
      return;
    }
    long nowMs = System.currentTimeMillis();
    synchronized (lastReplyTimeByChat) {
      Long lastReply = lastReplyTimeByChat.get(message.chatId);
      if (lastReply != null && nowMs - lastReply < MIN_INTERVAL_PER_CHAT_MS) {
        return;
      }
      lastReplyTimeByChat.put(message.chatId, nowMs);
    }
    TdApi.InputMessageText content = new TdApi.InputMessageText(
      new TdApi.FormattedText(replyText.trim(), null),
      new TdApi.LinkPreviewOptions(true, "", false, false, false),
      false
    );
    tdlib.sendMessage(message.chatId, null, null, Td.newSendOptions(), content);
  }
}
