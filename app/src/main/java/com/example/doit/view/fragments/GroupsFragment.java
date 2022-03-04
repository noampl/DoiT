package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.common.Consts;
import com.example.doit.databinding.FragmentGroupsBinding;
import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.interfaces.IFragmentNavigitionHelper;
import com.example.doit.model.entities.Group;
import com.example.doit.view.adapters.GroupsAdapter;
import com.example.doit.viewmodel.GroupsViewModel;

import java.util.Comparator;
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
    public void onPause() {
        _groupsViewModel.set_selectedGroupId(Consts.INVALID_STRING);
        super.onPause();
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
        _binding.setIsLoading(_groupsViewModel.get_isLoading().getValue());
        _binding.setLifecycleOwner(this);
        initAdapter();
        initObservers();
        return _binding.getRoot();
    }

    // endregion

    // region Private Methods

    private void initAdapter() {
        adapter = new GroupsAdapter(_groupsViewModel, getViewLifecycleOwner());
        _groupsViewModel.get_groups().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Group> groups) {
                groups.sort(new Comparator<Group>() {
                    @Override
                    public int compare(Group group, Group group2) {
                        return group.get_name().compareTo(group2.get_name());
                    }
                });
                adapter.submitList(groups);
                adapter.notifyDataSetChanged();
                if(!_groupsViewModel.get_isLoading().getValue()){
                    if (groups.size() > 0) {
                        _binding.noGroupsText.setVisibility(View.INVISIBLE);
                    }
                    else{
                        _binding.noGroupsText.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    _binding.noGroupsText.setVisibility(View.INVISIBLE);
                }
            }
        });
        _binding.groupsList.setAdapter(adapter);
    }

    private void initObservers(){
        _groupsViewModel.get_selectedGroupId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String id) {
                if (!id.equals(Consts.INVALID_STRING)){
                    _groupsViewModel.get_actionBarHelper().get().setTitle("");
                    _groupsViewModel.get_actionBarHelper().get().setNavIcon(R.drawable.ic_baseline_arrow_back_24);
                    _groupsViewModel.get_actionBarHelper().get().setNavigationClickListener(navigationMenuClickListener);
                    _groupsViewModel.get_actionBarHelper().get().setMenu(R.menu.edit_menu);
                    _groupsViewModel.get_actionBarHelper().get().setMenuClickListener(menuItemClickListener);
                }
                else {
                   regularMenu();

                }
            }
        });
        _groupsViewModel.get_isLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                _binding.setIsLoading(isLoading);
                if (isLoading){
                    if (_groupsViewModel.get_groups().getValue().size() > 0) {
                        _binding.noGroupsText.setVisibility(View.INVISIBLE);
                    }
                    else{
                        _binding.noGroupsText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void regularMenu(){
        _groupsViewModel.get_actionBarHelper().get().setTitle("My Groups");
        _groupsViewModel.get_actionBarHelper().get().setNavIcon(null);
        _groupsViewModel.get_actionBarHelper().get().setNavigationClickListener(null);
        _groupsViewModel.get_actionBarHelper().get().setMenu(R.menu.app_menu);
        _groupsViewModel.get_actionBarHelper().get().setMenuClickListener(null);
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
        GroupsFragmentDirections.actionGroupsFragment2ToChosenGroupFragment(_groupsViewModel.getSelectedGroupIdForDb());
        Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(action);
    }

    // endregion

    // region Toolbar.OnMenuItemClickListener

    @SuppressLint("NonConstantResourceId")
    private final Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        switch (item.getItemId()) {
            case R.id.delete:
                _groupsViewModel.deleteGroup(_groupsViewModel.getSelectedGroupIdForDb());
                _groupsViewModel.set_selectedGroupId(Consts.INVALID_STRING);
                return true;
            case R.id.edit:
                GroupsFragmentDirections.ActionGroupsFragment2ToGroupsDetailsFragment action =
                GroupsFragmentDirections.actionGroupsFragment2ToGroupsDetailsFragment(_groupsViewModel.getSelectedGroupIdForDb());
                _groupsViewModel.set_selectedGroupId(Consts.INVALID_STRING);
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(action);
                return true;


            default:

                break;
        }
        return false;
    };

    // endregion

    private final View.OnClickListener navigationMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _groupsViewModel.set_selectedGroupId(Consts.INVALID_STRING);
        }
    };

}