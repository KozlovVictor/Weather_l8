package com.geekbrains.weather.city;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.geekbrains.weather.R;
import com.geekbrains.weather.base.BaseFragment;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CreateActionFragment extends BaseFragment {

    OnHeadlineSelectedListener mCallback;
    //объявление переменных
    private EditText editTextCountry;
    private TextInputLayout textInputLayout;
    private RecyclerView recyclerView;
    private ArrayList<SelectedCity> cityList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = getBaseActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getBaseActivity().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //обращаемся к layout который будет содержать наш фрагмент
        return inflater.inflate(R.layout.create_action_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        initCountryList();

        recyclerView = view.findViewById(R.id.list_view);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        final CustomAdapter customAdapter = new CustomAdapter(getContext(), getBaseActivity(), cityList, mCallback);
        recyclerView.setAdapter(customAdapter);

        //инициализация edittext и листенер на ключи при взаимодействии с ним, когда мы нашимаем enter у нас опускается клавиатура и запускается WeatherFragment
        editTextCountry = view.findViewById(R.id.et_country);

        editTextCountry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Pattern pattern = Pattern.compile("[A-Za-zА-Яа-я]{1,10}");
                    String string = editTextCountry.getText().toString();
                    if (pattern.matcher(string).matches()) {
                        hideError();
                    } else
                        showError();

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String city = editTextCountry.getText().toString().trim();
                    getBaseActivity().getCollapsingToolbarLayout().setTitle(city);
                    editTextCountry.setText("");
                    return true;
                }
                return false;
            }
        });

        textInputLayout = view.findViewById(R.id.text_input);
    }

    private void showError() {
        textInputLayout.setError("This field can not contain numbers or special characters!");
    }

    private void hideError() {
        textInputLayout.setError("");
    }

    private void initCountryList() {
        cityList = new ArrayList<>();
        SelectedCity selectedCity1 = new SelectedCity();
        selectedCity1.setCity("Moscow");
        selectedCity1.setSelected(false);

        SelectedCity selectedCity2 = new SelectedCity();
        selectedCity2.setCity("St. Peterburg");
        selectedCity2.setSelected(false);

        SelectedCity selectedCity3 = new SelectedCity();
        selectedCity3.setCity("Kazan");
        selectedCity3.setSelected(false);

        cityList.add(selectedCity1);
        cityList.add(selectedCity2);
        cityList.add(selectedCity3);
    }

    public interface OnHeadlineSelectedListener {
        void onArticleSelected(SelectedCity selectedCity);
    }
}
