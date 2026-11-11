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
package org.thunderdog.challegram.ui;

import android.content.Context;
import android.view.View;

import org.drinkless.tdlib.TdApi;
import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.unsorted.Settings;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

// TeleDan: MyTelegram Mod settings page. Collects every mod toggle in one
// place: Ghost Mode, Anti-Delete and the newer MyTelegram options. The same
// toggles for Ghost Mode and Anti-Delete also remain on the Privacy page.
public class SettingsMyTelegramController extends RecyclerViewController<SettingsMyTelegramController.Args> implements View.OnClickListener {
  public static class Args {
    public Args () { }
  }

  public SettingsMyTelegramController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override
  public CharSequence getName () {
    return Lang.getString(R.string.MyTelegramMod);
  }

  @Override
  public int getId () {
    return R.id.controller_myTelegramSettings;
  }

  private SettingsAdapter adapter;

  @Override
  protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this) {
      @Override
      public void setValuedSetting (ListItem item, SettingView v, boolean isUpdate) {
        final int itemId = item.getId();
        if (itemId == R.id.btn_ghostMode) {
          v.getToggler().setRadioEnabled(Settings.instance().dontReadMessages(), isUpdate);
        } else if (itemId == R.id.btn_antiDelete) {
          v.getToggler().setRadioEnabled(Settings.instance().antiDelete(), isUpdate);
        } else if (itemId == R.id.btn_hideOnlineStatus) {
          v.getToggler().setRadioEnabled(Settings.instance().hideOnlineStatus(), isUpdate);
        } else if (itemId == R.id.btn_hideTyping) {
          v.getToggler().setRadioEnabled(Settings.instance().hideTyping(), isUpdate);
        } else if (itemId == R.id.btn_hideOnlineDot) {
          v.getToggler().setRadioEnabled(Settings.instance().hideOnlineDot(), isUpdate);
        } else if (itemId == R.id.btn_antiEdit) {
          v.getToggler().setRadioEnabled(Settings.instance().antiEdit(), isUpdate);
        } else if (itemId == R.id.btn_blockScreenshotNotifications) {
          v.getToggler().setRadioEnabled(Settings.instance().blockScreenshotNotifications(), isUpdate);
        } else if (itemId == R.id.btn_anonymousStoryViews) {
          v.getToggler().setRadioEnabled(Settings.instance().anonymousStoryViews(), isUpdate);
        } else if (itemId == R.id.btn_keepAutoDeletedMessages) {
          v.getToggler().setRadioEnabled(Settings.instance().keepAutoDeletedMessages(), isUpdate);
        } else if (itemId == R.id.btn_saveViewOnceMedia) {
          v.getToggler().setRadioEnabled(Settings.instance().saveViewOnceMedia(), isUpdate);
        } else if (itemId == R.id.btn_autoSaveMediaToGallery) {
          v.getToggler().setRadioEnabled(Settings.instance().autoSaveMediaToGallery(), isUpdate);
        } else if (itemId == R.id.btn_sendOriginalQuality) {
          v.getToggler().setRadioEnabled(Settings.instance().sendMediaAsFiles(), isUpdate);
        } else if (itemId == R.id.btn_hideEditedLabel) {
          v.getToggler().setRadioEnabled(Settings.instance().hideEditedLabel(), isUpdate);
        } else if (itemId == R.id.btn_fullTimeInMessages) {
          v.getToggler().setRadioEnabled(Settings.instance().fullTimeInMessages(), isUpdate);
        } else if (itemId == R.id.btn_silentSendByDefault) {
          v.getToggler().setRadioEnabled(Settings.instance().silentSendByDefault(), isUpdate);
        }
      }
    };

    List<ListItem> items = new ArrayList<>();
    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.MyTelegramMod));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_ghostMode, 0, R.string.GhostMode));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_antiDelete, 0, R.string.AntiDelete));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideOnlineStatus, 0, R.string.HideOnlineStatus));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideTyping, 0, R.string.HideTyping));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideOnlineDot, 0, R.string.HideOnlineDot));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_antiEdit, 0, R.string.AntiEdit));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_blockScreenshotNotifications, 0, R.string.BlockScreenshotNotifications));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_anonymousStoryViews, 0, R.string.AnonymousStoryViews));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_keepAutoDeletedMessages, 0, R.string.KeepAutoDeletedMessages));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_saveViewOnceMedia, 0, R.string.SaveViewOnceMedia));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_autoSaveMediaToGallery, 0, R.string.AutoSaveMediaToGallery));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_sendOriginalQuality, 0, R.string.SendOriginalQuality));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideEditedLabel, 0, R.string.HideEditedLabel));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fullTimeInMessages, 0, R.string.FullTimeInMessages));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_silentSendByDefault, 0, R.string.SilentSendByDefault));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.MyTelegramModInfo));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.HideOnlineStatusInfo));

    adapter.setItems(items, false);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onClick (View v) {
    final int id = v.getId();
    if (id == R.id.btn_ghostMode) {
      Settings.instance().setDontReadMessages(adapter.toggleView(v));
    } else if (id == R.id.btn_antiDelete) {
      Settings.instance().setAntiDelete(adapter.toggleView(v));
    } else if (id == R.id.btn_hideOnlineStatus) {
      boolean enabled = adapter.toggleView(v);
      Settings.instance().setHideOnlineStatus(enabled);
      // TeleDan: Hide online status is applied immediately as the account
      // privacy setting for UserPrivacySettingShowStatus (on: nobody can
      // see the status, off: everybody can). This changes the real account
      // privacy setting; custom exceptions are replaced, and the user can
      // override it again from the Privacy page at any time.
      TdApi.UserPrivacySettingRules rules = new TdApi.UserPrivacySettingRules(new TdApi.UserPrivacySettingRule[] {
        enabled ? new TdApi.UserPrivacySettingRuleRestrictAll() : new TdApi.UserPrivacySettingRuleAllowAll()
      });
      tdlib.send(new TdApi.SetUserPrivacySettingRules(new TdApi.UserPrivacySettingShowStatus(), rules), tdlib.typedOkHandler());
    } else if (id == R.id.btn_hideTyping) {
      Settings.instance().setHideTyping(adapter.toggleView(v));
    } else if (id == R.id.btn_hideOnlineDot) {
      Settings.instance().setHideOnlineDot(adapter.toggleView(v));
    } else if (id == R.id.btn_antiEdit) {
      Settings.instance().setAntiEdit(adapter.toggleView(v));
    } else if (id == R.id.btn_blockScreenshotNotifications) {
      Settings.instance().setBlockScreenshotNotifications(adapter.toggleView(v));
    } else if (id == R.id.btn_anonymousStoryViews) {
      Settings.instance().setAnonymousStoryViews(adapter.toggleView(v));
    } else if (id == R.id.btn_keepAutoDeletedMessages) {
      Settings.instance().setKeepAutoDeletedMessages(adapter.toggleView(v));
    } else if (id == R.id.btn_saveViewOnceMedia) {
      Settings.instance().setSaveViewOnceMedia(adapter.toggleView(v));
    } else if (id == R.id.btn_autoSaveMediaToGallery) {
      Settings.instance().setAutoSaveMediaToGallery(adapter.toggleView(v));
    } else if (id == R.id.btn_sendOriginalQuality) {
      Settings.instance().setSendMediaAsFiles(adapter.toggleView(v));
    } else if (id == R.id.btn_hideEditedLabel) {
      Settings.instance().setHideEditedLabel(adapter.toggleView(v));
    } else if (id == R.id.btn_fullTimeInMessages) {
      Settings.instance().setFullTimeInMessages(adapter.toggleView(v));
    } else if (id == R.id.btn_silentSendByDefault) {
      Settings.instance().setSilentSendByDefault(adapter.toggleView(v));
    }
  }
}
