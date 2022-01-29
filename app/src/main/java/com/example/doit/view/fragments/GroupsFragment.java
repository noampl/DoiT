package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.databinding.FragmentGroupsBinding;
import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.interfaces.IFragmentNavigitionHelper;
import com.example.doit.model.entities.Group;
import com.example.doit.view.adapters.GroupsAdapter;
import com.example.doit.viewmodel.GroupsViewModel;

import java.util.List;


public class GroupsFragment extends Fragment implements
        IDialogNavigationHelper,
        IFragmentNavigitionHelper {

    // region Members

    private GroupsViewModel _groupsViewModel;
    private FragmentGroupsBinding _binding;
    private GroupsAdapter adapter;

    // endregion

    // region C'tor

    public GroupsFragment() {
        // Required empty public constructor
    }

    // endregion

    // region LifeCycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups, container, false);
        _groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        _groupsViewModel.set_isBottomNavigationUp(true);
        _groupsViewModel.set_iDialogNavigationHelper(this);
        _groupsViewModel.set_iFragmentNavigitionHelper(this);
        _groupsViewModel.get_actionBarHelper().get().setNavIcon(null);
        _binding.setGroupsViewModel(_groupsViewModel);
        _binding.setLifecycleOwner(this);
        initAdapter();
        initObservers();
               return _binding.getRoot();
    }

    // endregion

    // region Private Methods

    private void initAdapter() {
        adapter = new GroupsAdapter(_groupsViewModel, getViewLifecycleOwner());
        adapter.submitList(_groupsViewModel.get_groups().getValue());
        _groupsViewModel.get_groups().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Group> groups) {
                synchronized (this){
                    adapter.submitList(groups);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        _binding.groupsList.setAdapter(adapter);
    }

    private void initObservers(){
        _groupsViewModel.get_selectedPosition().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer >= 0) {
                    _groupsViewModel.get_actionBarHelper().get().setTitle("");
                    _groupsViewModel.get_actionBarHelper().get().setNavIcon(R.drawable.ic_baseline_arrow_back_24);
                    _groupsViewModel.get_actionBarHelper().get().setMenu(R.menu.edit_menu);
                    _groupsViewModel.get_actionBarHelper().get().setMenuClickListener(menuItemClickListener);
                }
                else {
                    _groupsViewModel.get_actionBarHelper().get().setTitle("My Groups");
                    _groupsViewModel.get_actionBarHelper().get().setNavIcon(null);
                    _groupsViewModel.get_actionBarHelper().get().setMenu(R.menu.app_menu);
                    _groupsViewModel.get_actionBarHelper().get().setMenuClickListener(null);

                }
            }
        });
    }

    // endregion

    // region IDialogNavigationHelper

    @Override
    public void openDialog() {
        Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(R.id.action_groupsFragment2_to_addGroupDialog);
    }

    // endregion

    // region IFragmentNavigitionHelper

    @Override
    public void openFragment() {
        GroupsFragmentDirections.ActionGroupsFragment2ToChosenGroupFragment action =
        GroupsFragmentDirections.actionGroupsFragment2ToChosenGroupFragment(_groupsViewModel.getSelectedGroupId());
        Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(action);
    }

    // endregion

    // region Toolbar.OnMenuItemClickListener

    @SuppressLint("NonConstantResourceId")
    private final Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        switch (item.getItemId()) {
            case R.id.delete:
                Log.d("Peleg", "DELETE in Groups fragment");

                return true;
            case R.id.edit:

                Log.d("Peleg", "EDIT in Groups fragment");
                return true;
            default:

                break;
        }
        return false;
    };

    // endregion
}