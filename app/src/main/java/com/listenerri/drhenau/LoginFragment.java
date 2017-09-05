package com.listenerri.drhenau;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.listenerri.drhenau.drcom.DrcomService;
import com.listenerri.drhenau.drcom.HostInfo;
import com.listenerri.drhenau.drcom.WifiUtils;


public class LoginFragment extends Fragment {

    private View view = null;
    private AutoCompleteTextView mAccountView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String spFileName = "accont.passwd";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initFields();
        return view;
    }

    private void initFields() {
        // Set up the login form.
        mAccountView = (AutoCompleteTextView) view.findViewById(R.id.account);

        mPasswordView = (EditText) view.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // 还原记住的密码
        SharedPreferences sp = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        mAccountView.setText(sp.getString("account",""));
        mPasswordView.setText(sp.getString("password",""));

        Button mSignInButton = (Button) view.findViewById(R.id.account_sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击登录按钮开始一系列检查和启动登录服务
                attemptLogin();
            }
        });

        Button mSignOutButton = (Button) view.findViewById(R.id.account_sign_out_button);
        mSignOutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭登录服务(注销)
                getActivity().stopService(new Intent(getActivity(), DrcomService.class));
            }
        });

        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);
    }


    // 检查账户和密码是否规范, 是的话开始启动登录服务
    private void attemptLogin() {
        // 重置错误属性
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // 获取输入
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查账户规范
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        }

        // 检查密码规范
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // 输入不规范进行提示
            focusView.requestFocus();
        } else {
            // 显示一个进度条并开始登录
            //showProgress(true);
            //检查wifi是否打开且是否为学校wifi
            WifiUtils wifiUtils = new WifiUtils(getActivity().getApplicationContext());
            if(!wifiUtils.isWifiOpened()){
                Toast.makeText(getActivity(), "wifi未打开", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isSchoolWifi = WifiUtils.currentIsSchoolWifi(wifiUtils);
            if(!isSchoolWifi){
                Toast.makeText(getActivity(), "未连接学校wifi", Toast.LENGTH_SHORT).show();
                return;
            }

            String mac = wifiUtils.getMacAddress();
            HostInfo info = new HostInfo(account,password, mac);
            Intent startIntent = new Intent(getActivity(), DrcomService.class);
            startIntent.putExtra("info",info);
            getActivity().startService(startIntent);

            //记住密码
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE).edit();
            editor.putString("account", account);
            editor.putString("password",password);
            editor.apply();
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 3;
    }

    // 定义进度条样式
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

