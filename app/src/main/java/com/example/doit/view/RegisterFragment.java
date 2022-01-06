package com.example.doit.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.doit.IResponseHelper;
import com.example.doit.Model.entities.User;
import com.example.doit.R;
import com.example.doit.databinding.FragmentRegisterBinding;
import com.example.doit.viewmodel.RegisterViewModel;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment{
    private FragmentRegisterBinding _binding;
    private RegisterViewModel viewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        viewModel.get_authUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.get_userId() != null) {
                    Toast.makeText(getContext(), "hello " + user.get_firstName(), Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                            R.id.action_registerFragment2_to_groupsFragment2);
                }
            }
        });
        _binding.setRegisterViewModel(viewModel);
        _binding.setLifecycleOwner(this);
        _binding.profileImageButton.setOnClickListener(replaceImage());
        return _binding.getRoot();
    }

    private final ActivityResultLauncher<Intent> pickPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data != null ? data.getData() : null;
                        Picasso.with(_binding.getRoot().getContext()).load(uri).fit().into(_binding.profileImageButton);
                        viewModel.setImageUri(uri);
                    }
                }
            }
    );

    private View.OnClickListener replaceImage() {
        return new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                pickPhotoResultLauncher.launch(pickPhoto);
            }
        };
    }

    private void saveUserForLater(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.email), viewModel.get_email());
        editor.putString(getString(R.string.password), viewModel.get_password()); // TODO figure out if needed to hash?
        editor.apply();
    }
}