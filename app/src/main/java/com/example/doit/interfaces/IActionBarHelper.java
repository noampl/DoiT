package com.example.doit.interfaces;

import android.view.View;

import androidx.appcompat.widget.Toolbar;

public interface IActionBarHelper {

    void setNavIcon(Integer resId);

    void setTitle(String title);

    void setMenu(int resId);

    void setMenuClickListener(Toolbar.OnMenuItemClickListener listener);

    void setNavigationClickListener(View.OnClickListener listener);

}
