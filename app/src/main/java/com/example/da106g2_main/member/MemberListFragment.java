package com.example.da106g2_main.member;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.da106g2_main.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberListFragment extends Fragment implements View.OnClickListener {

    private static final String SHARED_PREFERENCES_GUIDE = "member";
    private NavController navController;

    public MemberListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.btnLogout).setOnClickListener(this);
        view.findViewById(R.id.btnOrderList).setOnClickListener(this);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_GUIDE, Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean("login", false)) {
            navController.navigate(R.id.action_memberListFragment_to_memberLoginFragment); //導入登入頁面
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogout: {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_GUIDE, Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("login", false)
                        .putString("memberID", null)
                        .putString("password", null).apply();
                navController.navigate(R.id.action_memberListFragment_self);
                return;
            }

            case R.id.btnOrderList: {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_GUIDE, Context.MODE_PRIVATE);
                Bundle bundle = new Bundle();
                bundle.putString("memberID", sharedPreferences.getString("memberID", ""));
                navController.navigate(R.id.action_memberListFragment_to_memberOrdersListFragment, bundle);
            }
        }
    }
}
