package com.example.da106g2_main.tools;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.da106g2_main.R;

import java.util.ArrayList;

import static com.example.da106g2_main.tools.Util.CART;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToolsCreditCardFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ToolsCreditCardFragment";

    private EditText etCardNumber1, etCardNumber2, etCardNumber3, etCardNumber4, etCardMonth, etCardYear;
    private NavController navController;
    private CommunicationTask communicationTask;

    public ToolsCreditCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tools_credit_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etCardNumber1 = view.findViewById(R.id.etCardNumber1);
        etCardNumber2 = view.findViewById(R.id.etCardNumber2);
        etCardNumber3 = view.findViewById(R.id.etCardNumber3);
        etCardNumber4 = view.findViewById(R.id.etCardNumber4);
        etCardMonth = view.findViewById(R.id.etCardMonth);
        etCardYear = view.findViewById(R.id.etCardYear);
        view.findViewById(R.id.btnCardCheckOut).setOnClickListener(this);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.btnCardCheckOut) {
            return;
        }

        if (etCardNumber1.getText().length() <= 0 ||
                etCardNumber2.getText().length() <= 0 ||
                etCardNumber3.getText().length() <= 0 ||
                etCardNumber4.getText().length() <= 0 ||
                etCardMonth.getText().length() <= 0 ||
                etCardYear.getText().length() <= 0) {
            Util.showToast(getContext(), "請勿為空值");
            return;
        }

        switch (getArguments().getString("TAG")) {
            case "ShoppingCartFragment": {
                String jsonOut = getArguments().getString("jsonObject");
                communicationTask = new CommunicationTask(Util.URL + "Android/OrdersServlet", jsonOut);
                String jsonIn;
                try {
                    jsonIn = communicationTask.execute().get();
                } catch (Exception e) {
                    Util.showToast(v.getContext(), "上傳失敗");
                    Log.d(TAG, e.toString());
                    return;
                }

                if ("false".equals(jsonIn)) {
                    Util.showToast(v.getContext(), "上傳失敗");
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("order_id", jsonIn);
                Util.showToast(v.getContext(), "上傳成功");
                CART = new ArrayList<>();
                navController.navigate(R.id.action_toolsCreditCardFragment_to_memberOrderDetailFragment, bundle);
                return;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (communicationTask != null) {
            communicationTask.cancel(true);
            communicationTask = null;
        }
    }
}
