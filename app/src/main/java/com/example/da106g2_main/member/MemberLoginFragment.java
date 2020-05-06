package com.example.da106g2_main.member;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.da106g2_main.tools.CommunicationTask;
import com.example.da106g2_main.tools.Util;
import com.example.da106g2_main.R;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberLoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MemberLoginFragment",
            SHARED_PREFERENCES_GUIDE = "member";

    //物件
    EditText etAccount, etPassword;
    TextView tvLogInMessage;

    //連線任務
    CommunicationTask communicationTask;

    public MemberLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etAccount = view.findViewById(R.id.etAccount);
        etPassword = view.findViewById(R.id.etPassword);
        view.findViewById(R.id.btnLogin).setOnClickListener(this);
        view.findViewById(R.id.btnRegistration).setOnClickListener(this);
        tvLogInMessage = view.findViewById(R.id.tvLogInMessage);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_GUIDE, Context.MODE_PRIVATE);

        //判斷會員之前是否有登入
        if (sharedPreferences.getBoolean("login", false)) {
            if (isMember(sharedPreferences.getString("memberID", ""), sharedPreferences.getString("password", ""))) {
                Activity activity = getActivity();
                activity.setResult(Activity.RESULT_OK);
                activity.onBackPressed();
            }
        }
    }

    private boolean isMember(String userID, String password) {
        boolean isMember = false;
        if (Util.networkConnected(getActivity())) {
            String url = Util.URL + "Android/MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isPasswordCorrect");
            jsonObject.addProperty("member_id", userID);
            jsonObject.addProperty("password", password);
            communicationTask = new CommunicationTask(url, jsonObject.toString());

            try {
                String result = communicationTask.execute().get();
                isMember = "true".equals(result);
            } catch (InterruptedException e) {
                Log.d(TAG, "中斷錯誤： " + e.toString());
                isMember = false;
            } catch (ExecutionException e) {
                Log.d(TAG, "執行錯誤： " + e.toString());
                isMember = false;
            } catch (Exception e) {
                Log.d(TAG, "倒大楣啦！錯誤：" + e.toString());
                isMember = false;
            }

        } else {
            tvLogInMessage.setText("目前沒有連線");
        }
        return isMember;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin: {
                String userID = etAccount.getText().toString(), password = etPassword.getText().toString();

                if (userID.length() <= 0 || password.length() <= 0 || !(isMember(userID, password))) {
                    tvLogInMessage.setText(R.string.err_account_password);//顯示帳號密碼錯誤
                    return;
                }
                Activity activity = getActivity();
                SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES_GUIDE, Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("login", true)
                        .putString("memberID", userID)
                        .putString("password", password).apply();
                activity.setResult(activity.RESULT_OK);
                activity.onBackPressed();
            }
            break;
            case R.id.btnRegistration:
                tvLogInMessage.setText("還沒做");
                break;
        }
    }

    //離開頁面時關閉連線
    @Override
    public void onStop() {
        super.onStop();
        if (communicationTask != null) {
            communicationTask.cancel(true);
            communicationTask = null;
        }
    }

}
