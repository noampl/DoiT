package com.example.doit.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.viewmodel.LoginViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitFragment extends Fragment {

    private String _email;
    private String _password;
    private NavController _navController;
    private LoginViewModel _loginViewModel;

    public InitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InitFragment newInstance(String param1, String param2) {
        return new InitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        if(isUserExist()){
            _loginViewModel.setResponseHelper(actionResult -> {
                if (actionResult) {
                    Log.d("Peleg" ,"it's here 1");
                    Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                            .navigate(InitFragmentD);
                }
                else {
                    Log.d("peleg","user failed to connect firebase");
                }
            });
            _loginViewModel.Login(_email, _password);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_init, container, false);
    }

    private boolean isUserExist(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        _email = sharedPref.getString(getString(R.string.email), "NONE");
        _password  = sharedPref.getString(getString(R.string.password), "NONE");
        return !(_email.equals("NONE") || _password.equals("NONE"));
    }
}