package com.example.doit.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.example.doit.R;
import com.example.doit.databinding.BaseModelItemBinding;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.UsersViewModel;

import java.util.List;

public class UsersAdapter extends BaseAdapter {

    private final UsersViewModel _userViewModel;
    private final Context _context;
    private List<User> _users;

    public UsersAdapter(UsersViewModel users, Context context){
        _userViewModel = users;
        _context = context;
        _users = _userViewModel.get_users().getValue();
    }

    public void set_users(List<User> _users) {
        this._users = _users;
    }

    @Override
    public int getCount() {
        return _users.size();
    }

    @Override
    public Object getItem(int i) {
        return _users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") BaseModelItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(_context), R.layout.base_model_item, viewGroup,
                        false);
                binding.setUsersViewModel(_userViewModel);
                binding.setPosition(i);
                binding.setUser(_users.get(i));

        return binding.getRoot();
    }
}
