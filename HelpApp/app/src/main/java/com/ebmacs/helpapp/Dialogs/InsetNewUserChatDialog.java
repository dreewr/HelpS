package com.ebmacs.helpapp.Dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.Adapters.MembersAdapter;
import com.ebmacs.helpapp.Models.UserDetails;
import com.ebmacs.helpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;


public class InsetNewUserChatDialog extends DialogFragment {

    MembersAdapter adapter;
    private static final String TAG = "CustomDialogAdapter";
    private final String URL = "http://ebmacshost.com/help/api/readAllUser";
    private final String cURL = "http://ebmacshost.com/help/api/group/insertindividualuser";
    private ArrayList<UserDetails> arrayList;
    private RecyclerView recyclerView;
    private String allmembers = null;
    private EditText chatEditText;

    private Button btn_ok_dialog;
    private LinearLayoutManager linearLayoutManager;


    public InsetNewUserChatDialog() {
    }

    public interface OnInputListener {
        void sendInput(String input);
    }

    public OnInputListener mOnInputListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_chat, container, false);
        chatEditText = view.findViewById(R.id.chatEditText);
        btn_ok_dialog = view.findViewById(R.id.btn_ok_dialog);
        recyclerView = view.findViewById(R.id.new_chatRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        init();
        fetchAllUser();
        return view;
    }

    public void init() {
        btn_ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePoints();
                getDialog().dismiss();
            }
        });


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputListener = (InsetNewUserChatDialog.OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: " + e.getMessage());
        }
    }


    private void fetchAllUser() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {


                                JSONArray array = jsonObject.getJSONArray("all_users");
                                Log.i("------UserData", array + "");

                                arrayList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    UserDetails result = new UserDetails();
                                    result.setUserName(object.getString("user_name"));
                                    result.setUserId(object.getString("user_id"));
                                    String url = object.getString("image_url");
                                    allmembers += object.getString("user_id") + ",";

                                    result.setImageUrl(url);
                                    arrayList.add(result);
                                    Log.i("------User Image", url);
                                    Log.i("------UserData", String.valueOf(arrayList));

                                    setRecyclerView();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "onErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setRecyclerView() {
        adapter = new MembersAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);

    }


    private void sharePoints() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, cURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Log.i("New UserChat has been ", "added DONE");


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "onErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences prefs = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
                String user_id = prefs.getString("user_id", "No name defined");

                String membIds = null;
                for (int i = 0; i < MembersAdapter.userrListTemp.size(); i++) {
                    membIds = MembersAdapter.userrListTemp.get(i).getUserId();
                }

                String msg = chatEditText.getText().toString();

                Date currentTime = Calendar.getInstance().getTime();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());


                params.put("selected_id", membIds);
                params.put("user_id", user_id);
                params.put("msg", msg);
                params.put("date", currentDateandTime);

                Log.i("Parameters", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}