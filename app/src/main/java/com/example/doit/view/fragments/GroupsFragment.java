package com.example.doit.view.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.databinding.FragmentGroupsBinding;
import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.interfaces.IGroupDialogHelper;
import com.example.doit.model.entities.Group;
import com.example.doit.view.adapters.GroupsAdapter;
import com.example.doit.viewmodel.GroupsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment implements IDialogNavigationHelper {

    // region Members

    private GroupsViewModel _groupsViewModel;
    private FragmentGroupsBinding _binding;

    // endregion

    public GroupsFragment() {
        // Required empty public constructor
    }

    public static GroupsFragment newInstance() {
        return new GroupsFragment();
    }

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
        _binding.setGroupsViewModel(_groupsViewModel);
        _binding.setLifecycleOwner(this);
        initAdapter();

        return _binding.getRoot();
    }

    private void initAdapter() {
        GroupsAdapter adapter = new GroupsAdapter(_groupsViewModel);
        adapter.submitList(_groupsViewModel.get_groups().getValue());
        _groupsViewModel.get_groups().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                adapter.submitList(groups);
                Log.d("PELEG", "submit group list ");
            }
        });
        _binding.groupsList.setAdapter(adapter);
    }

    @Override
    public void openDialog() {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                R.id.action_groupsFragment2_to_addGroupDialog);
    }
}