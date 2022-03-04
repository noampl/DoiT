package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doit.R;
import com.example.doit.databinding.FragmentGroupsDetailsBinding;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.User;
import com.example.doit.view.adapters.UsersAdapter;
import com.example.doit.viewmodel.GroupsViewModel;
import com.example.doit.viewmodel.UsersViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GroupsDetailsFragment extends Fragment {

    // region Members

    private FragmentGroupsDetailsBinding _binding;
    private GroupsViewModel _groupsViewModel;
    private UsersViewModel _usersViewModel;
    private Group _group;
    private UsersAdapter _usersAdapter;

    // endregion

    // region LifeCycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String groupId = GroupsDetailsFragmentArgs.fromBundle(getArguments()).getGroupId();
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups_details, container, false);
        _groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        _usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        _groupsViewModel.set_isEdit(true);
        CompletableFuture<Group> group = _groupsViewModel.getGroupById(groupId);
        group.thenAccept((group1) ->{
            _group = group1;
            _binding.setGroup(group1);
            _groupsViewModel.get_actionBarHelper().get().setTitle(_group.get_name());

        });
        _usersViewModel.setUsersById(groupId);
        init();

        _binding.executePendingBindings();
        return _binding.getRoot();
    }

    @Override
    public void onDestroy() {
        _usersViewModel.set_selectedUser(new ArrayList<>());
        super.onDestroy();
    }

    // endregion

    // region Private Methods

    private void init() {
       initBinding();
       initListeners();
       initMenu();
       initAdapter();
       initObserver();
    }

    private void initMenu() {
        _groupsViewModel.get_actionBarHelper().get().setMenuClickListener(menuItemClickListener);
        _groupsViewModel.get_actionBarHelper().get().setMenu(R.menu.edit_menu);
    }

    private void  initBinding() {
        _groupsViewModel.get_actionBarHelper().get().setNavIcon(R.drawable.ic_baseline_arrow_back_24);
        _binding.setGroupsViewModel(_groupsViewModel);
        _binding.setLifecycleOwner(getViewLifecycleOwner());

    }

    private void initListeners(){
        _binding.save.setOnClickListener(view -> {
            _groupsViewModel.saveEditGroup(_binding.groupNameEdit.getText().toString(),
                    _binding.groupDescEdit.getText().toString(), _group, _usersViewModel.get_users().getValue());
            Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigateUp();
            }

        );

        _binding.groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_groupsViewModel.get_isEdit().getValue()) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    pickPhotoResultLauncher.launch(pickPhoto);
                }
            }
        });

        _binding.addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupsDetailsFragmentDirections.ActionGroupsDetailsFragmentToAdditionDialog action =
                GroupsDetailsFragmentDirections.actionGroupsDetailsFragmentToAdditionDialog();
                action.setGroupId(_group.get_groupId());
                action.setIsChecked(true);
                action.setMultipleChoice(true);
                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action);
            }
        });

        _groupsViewModel.get_isEdit().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isEdit) {
                if (isEdit){
                    _binding.groupDescSwitcher.showNext();
                    _binding.groupNameSwitcher.showNext();
                }
                else if (_binding.groupNameSwitcher.getNextView() == _binding.groupNameTitle){
                    _binding.groupDescSwitcher.showNext();
                    _binding.groupNameSwitcher.showNext();
                }
            }
        });

    }

    private void initAdapter(){
        _usersAdapter = new UsersAdapter(_usersViewModel, requireContext());
        _usersViewModel.get_users().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                _usersAdapter.set_users(users);
                _usersAdapter.notifyDataSetChanged();
            }
        });
        _binding.membersSpinner.setAdapter(_usersAdapter);
    }

    private void initObserver(){
        _usersViewModel.get_selectedUser().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users.size() != _usersViewModel.get_users().getValue().size())
                    _usersViewModel.set_users(users);
            }
        });
    }
    // endregion

    // region Toolbar.OnMenuItemClickListener

    @SuppressLint("NonConstantResourceId")
    private final Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        switch (item.getItemId()) {
            case R.id.delete:
                _groupsViewModel.deleteGroup(_group);
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigateUp();
                return true;
            case R.id.edit:
                _groupsViewModel.set_isEdit(!_groupsViewModel.get_isEdit().getValue());
                return true;
            default:

                break;
        }
        return false;
    };

    // endregion

    // region ActivityResultLauncher

    ActivityResultLauncher<Intent> pickPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        _group.set_image(data != null ? data.getData().toString() : null);
                        Picasso.with(_binding.getRoot().getContext()).load(_group.get_image()).fit().into(_binding.groupImage);

                    }
                }
            }
    );

    // endregion
}