package com.example.myidol.fragment.home;

import android.app.Application;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.myidol.adapter.PostAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Post;

import java.util.ArrayList;

public class HomeViewmodel extends BaseViewmodel {
    public PostAdapter adapter = new PostAdapter();
    private MutableLiveData<ArrayList<Post>> arrayPost = new MutableLiveData<>();
    public HomeViewmodel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<Post>> getarrPost(){
        ArrayList<Post> arr = new ArrayList<>();
        arr.add(new Post("Sơn tít","https://congngheads.com/media/images/anh-dep/tai-hinh-nen-may-tinh-dep-1559550627/anh-gai-xinh-goi-cam-tuoi-cuoi-lam-hinh-nen-full-hd-05-07-2019-1.jpg","Em này kute quá <3 ","999","999","999"));
        arr.add(new Post("Sơn tít","https://i1.wp.com/3.bp.blogspot.com/-a4v_x6n2C2s/Xdkkx-lW3RI/AAAAAAADWIs/9LEo6QrxM4Ayb1WusYXMT_lC3_mvOx4fACLcBGAsYHQ/s1600/XIUREN-No.1705-lele-MrCong.com-001.jpg?w=955&ssl=1","Em này kute quá <3","999","999","999"));
        arr.add(new Post("Sơn tít","https://i2.wp.com/3.bp.blogspot.com/-IHZZMuZft6c/Xdkhq2C1hNI/AAAAAAADV4g/4c8bpBwrBl8-Wcc_1vc2ZBd_fsopzejDACLcBGAsYHQ/s1600/IMISS-Vol.379-MrCong.com-032.jpg?w=955&ssl=1","Em này kute quá <3","999","999","999"));
        arr.add(new Post("Sơn tít","https://i0.wp.com/2.bp.blogspot.com/-UqXObs3p3To/XdkUBHkX57I/AAAAAAADVYw/wmsBiYvYp3gXVd_jVRwt711iojV3h6TQgCLcBGAsYHQ/s1600/UGIRLS-Ai-You-Wu-App-No.1639-Hui-Hui-Zi-MrCong.com-008.jpg?w=955&ssl=1","Em này kute quá <3","999","999","999"));
        arr.add(new Post("Sơn tít","https://i0.wp.com/1.bp.blogspot.com/-ON8Y_nAwYDo/XdKSXUQCfoI/AAAAAAADVOo/MkRCPAFnAII4oIU4BrDLc_68r6XZd7PEACLcBGAsYHQ/s1600/XIUREN-No.1698-YUNDUOER-MrCong.com-041.jpg?w=955&ssl=1","Em này kute quá <3","999","999","999"));

        arrayPost.postValue(arr);
        return arrayPost;
    }

}
