package com.kristofkalmar.outfitzone.ui.components.authenticationcontainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.activities.LoginActivity;
import com.kristofkalmar.outfitzone.activities.MainActivity;
import com.kristofkalmar.outfitzone.activities.RegisterActivity;
import com.kristofkalmar.outfitzone.activities.WelcomeActivity;

public class AuthenticationContainerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            return inflater.inflate(R.layout.fragment_profile_bar, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_authentication_bar, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Button logoutButton = getActivity().findViewById(R.id.logout_button);

            logoutButton.setOnClickListener(event -> {
                auth.signOut();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            });
        } else {
            Button loginButton = getActivity().findViewById(R.id.login_button);

            loginButton.setOnClickListener(event -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right_bg);
            });

            Button registerButton = getActivity().findViewById(R.id.register_button);

            registerButton.setOnClickListener(event -> {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right_bg);
            });
        }
    }
}
