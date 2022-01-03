package com.example.doit.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doit.IResponseHelper;
import com.example.doit.R;
import com.example.doit.databinding.FragmentAccountBinding;
import com.example.doit.viewmodel.AccountViewModel;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {
    //region members
    private static final String TAG = "Account Fragment";
    private FragmentAccountBinding _binding;
    private AccountViewModel viewModel;
    private boolean editDetails;
    private boolean image_changed;
    //endregion

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() { return new AccountFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_account, container,false);
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        _binding.setAccountViewModel(viewModel);
        _binding.setLifecycleOwner(this);
        editDetails = false;
        _binding.userEditButton.setOnClickListener(userEditButtonListener);
        _binding.profileImage.setOnClickListener(profileImageListener);
        _binding.profileImage.setClickable(false);
        _binding.profileImage.setEnabled(false);
        viewModel.updateUserDetails();
        viewModel.getLoggedOut().observe(getViewLifecycleOwner(), loggedOutObserver);
        viewModel.getFirstName().observe(getViewLifecycleOwner(), invalidFirstName);
        viewModel.getLastName().observe(getViewLifecycleOwner(), invalidLastName);
        viewModel.getUserEmailAddress().observe(getViewLifecycleOwner(), invalidEmail);
        return _binding.getRoot();
    }

    Observer<String> invalidFirstName = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if (s.length() == 0) {
                _binding.FirstNameTextView.setText(" ");
            }
        }
    };

    Observer<String> invalidEmail = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if (s.length() == 0) {
                _binding.EmailTextView.setText(" ");
            }
        }
    };

    Observer<String> invalidLastName = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if (s.length() == 0) {
                _binding.LastNameTextView.setText(" ");
            }
        }
    };

    ActivityResultLauncher<Intent> pickPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data != null ? data.getData() : null;
                        Picasso.with(_binding.getRoot().getContext()).load(uri).fit().into(_binding.profileImage);
                        assert uri != null;
                        viewModel.setImageUrl(uri.toString());
                        viewModel.setImageChanged(true);
                    }
                }
            }
    );

    Observer<Boolean> loggedOutObserver = aBoolean -> {
        if (aBoolean) {
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                    R.id.action_accountFragment2_to_logInFragment2);
        }
    };

    //region listeners
    View.OnClickListener profileImageListener = new View.OnClickListener() {
        @SuppressLint("IntentReset")
        @Override
        public void onClick(View v) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.setType("image/*");
            pickPhotoResultLauncher.launch(pickPhoto);
        }
    };

    View.OnClickListener userEditButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editDetails = !editDetails;
            if (editDetails) {
                _binding.EmailTextView.setEnabled(true);
                _binding.EmailTextView.setBackground(new ColorDrawable(Color.rgb(240, 240, 240)));
                _binding.FirstNameTextView.setEnabled(true);
                _binding.FirstNameTextView.setBackground(new ColorDrawable(Color.rgb(240, 240, 240)));
                _binding.LastNameTextView.setEnabled(true);
                _binding.LastNameTextView.setBackground(new ColorDrawable(Color.rgb(240, 240, 240)));
                _binding.profileImageText.setText(getString(R.string.replace_image));
                _binding.profileImage.setClickable(true);
                _binding.profileImage.setEnabled(true);
                _binding.userEditButton.setText(R.string.submit_changes);
            } else {
                IResponseHelper helper = new IResponseHelper() {
                    @Override
                    public void actionFinished(boolean actionResult) {
                        if (!actionResult) { Toast.makeText(getContext(), "Email is already exist or invalid", Toast.LENGTH_SHORT).show(); }
                        else { Toast.makeText(getContext(), "User details updated", Toast.LENGTH_SHORT).show(); }
                        _binding.EmailTextView.setEnabled(false);
                        _binding.EmailTextView.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
                        _binding.FirstNameTextView.setEnabled(false);
                        _binding.FirstNameTextView.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
                        _binding.LastNameTextView.setEnabled(false);
                        _binding.LastNameTextView.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
                        _binding.profileImageText.setText(null);
                        _binding.profileImage.setClickable(false);
                        _binding.profileImage.setEnabled(false);
                        _binding.userEditButton.setText(R.string.user_details_edit);
                    }
                };
                viewModel.changeDetails(helper);
            }
        }
    };
    // endregion

}