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
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideEditedLabel, 0, R.string.HideEditedLabel));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fullTimeInMessages, 0, R.string.FullTimeInMessages));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_silentSendByDefault, 0, R.string.SilentSendByDefault));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.MyTelegramModInfo));

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
    } else if (id == R.id.btn_hideEditedLabel) {
      Settings.instance().setHideEditedLabel(adapter.toggleView(v));
    } else if (id == R.id.btn_fullTimeInMessages) {
      Settings.instance().setFullTimeInMessages(adapter.toggleView(v));
    } else if (id == R.id.btn_silentSendByDefault) {
      Settings.instance().setSilentSendByDefault(adapter.toggleView(v));
    }
  }
}
