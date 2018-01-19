package com.sky.app.mobileplayer.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/1/19
 * Time: 15:15
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class BaseFragment extends Fragment {
    public BasePager basePager;

    public void setBasePager(BasePager basePager) {
        this.basePager = basePager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (basePager != null) {
            return basePager.rootView;
        }
        return null;
    }
}
