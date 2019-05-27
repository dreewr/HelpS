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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;


public class NewGroupDialog extends DialogFragment {

    MembersAdapter adapter;
    private static final String TAG = "CustomDialogAdapter";
    private final String URL = "http://ebmacshost.com/help/api/readAllUser";
    private final String cURL = "http://ebmacshost.com/help/api/group/create_group";
    private ArrayList<UserDetails> arrayList;
    RecyclerView recyclerView;
    String allmembers = null;


    EditText groupNameEditText, groupDescriptionEditText;
    Button btn_ok_dialog_group;
    LinearLayoutManager linearLayoutManager;


    public NewGroupDialog() {
    }

    public interface OnInputListener {
        void sendInput(String input);
    }

    public OnInputListener mOnInputListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_group, container, false);
        groupNameEditText = view.findViewById(R.id.groupNameEditText);
        btn_ok_dialog_group = view.findViewById(R.id.btn_ok_dialog_group);
        groupDescriptionEditText = view.findViewById(R.id.groupDescritionEditText);
        recyclerView = view.findViewById(R.id.memberRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        init();
        fetchAllUser();
        return view;
    }

    public void init() {
        btn_ok_dialog_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                craeteNewGroup();
                getDialog().dismiss();
            }
        });


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputListener = (NewGroupDialog.OnInputListener) getActivity();
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


    private void craeteNewGroup() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, cURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {
                                Log.i("Group created", "DONE");


//                                JSONArray array = jsonObject.getJSONArray("mygroup");
//                                Log.i("------UserData", array + "");
//
//                                arrayList = new ArrayList<>();
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject object = array.getJSONObject(i);
//                                    Groups result = new Groups();
//                                    result.setGroupName(object.getString("group_name"));
//                                    result.setGroupDescription(object.getString("group_discription"));
//                                    result.setGroupCreaterId(object.getString("group_creater_id"));
//
//                                    arrayList.add(result);
//                                    Log.i("-GroupDescription", result.getGroupDescription());
//                                    Log.i("-GroupName", result.getGroupName());
//
//                                    setRecyclerView();


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
                String groupName = groupNameEditText.getText().toString();
                String group_discription = groupDescriptionEditText.getText().toString();
                ArrayList<ArrayList<UserDetails>> temp = new ArrayList<>();

                params.put("user_id", user_id);
                params.put("group_name", groupName);
                params.put("group_discription", group_discription);
                String membIds = null;
                for (int i = 0; i < MembersAdapter.userrListTemp.size(); i++) {
                    membIds += MembersAdapter.userrListTemp.get(i).getUserId()+",";
                }
                params.put("group_member_id", membIds);
                Log.i("Parameters", String.valueOf(params));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}