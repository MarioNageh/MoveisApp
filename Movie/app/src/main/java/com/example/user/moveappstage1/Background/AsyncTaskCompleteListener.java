package com.example.user.moveappstage1.Background;

import java.util.ArrayList;

/**
 * Created by user on 02/03/2018.
 */

public interface AsyncTaskCompleteListener<ArrayList,N,Boolean> {
    public void onTaskComplete(ArrayList Movies, N PageState, Boolean addPages);
}
