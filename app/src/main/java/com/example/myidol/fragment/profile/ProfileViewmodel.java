package com.example.myidol.fragment.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.PhotoAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Photo;

import java.util.ArrayList;

public class ProfileViewmodel extends BaseViewmodel {
    PhotoAdapter adapter = new PhotoAdapter();
    MutableLiveData<ArrayList<Photo>> arrayPhoto = new MutableLiveData<>();
    public ProfileViewmodel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<Photo>> getArrayPhoto(){
        ArrayList<Photo> arr = new ArrayList<>();
        arr.add(new Photo("https://tinhte.cdnforo.com/store/2015/10/3525535_tuigirl-019-wangmingming-005.jpg"));
        arr.add(new Photo("https://congngheads.com/media/images/anh-dep/bo-anh-gai-xinh-voi-ve-dep-diu-dang-goi-cam-hinh-nen-dien-thoai-gentle-beauty-sexy-photo-phone-1556641845/6.jpg"));
        arr.add(new Photo("https://s3-ap-southeast-1.amazonaws.com/idoltv-website/wp-content/uploads/2018/10/18105554/HINH-NEN-IPHONE-X-GIRL-IDOLTV-12.jpg"));
        arr.add(new Photo("https://phonelumi.com/wp-content/uploads/2017/01/Hinh-anh-girl-xinh-lam-hinh-nen-cho-iphone-7-26.jpg"));
        arr.add(new Photo("https://phonelumi.com/wp-content/uploads/2017/01/Hinh-anh-girl-xinh-lam-hinh-nen-cho-iphone-7-24.jpg"));
        arr.add(new Photo("https://1.bp.blogspot.com/-7gUhYf_KzHE/XJpBM_3T2aI/AAAAAAAAA3s/RCHsgaT__lQJ4O1lYJT0g-sGZSHiOntTwCLcBGAs/s1600/EmXinh2k__anh-hot-girl-gai-xinh-sexy-nong-bong-khoe-vong-1%2B%25283%2529.jpg"));
        arr.add(new Photo("http://afamilycdn.com/fRhOWcbaG01Vd2ydvKbOwEYcba/Image/2016/08/ai-sexy-ho-hang-mac-ke-4-hot-girl-nay-chi-can-ngot-ngao-de-thuong-la-du_20160820081235490.jpg"));
        arr.add(new Photo("https://2.bp.blogspot.com/-VcdSn9CLOf0/WNZ70hZa1AI/AAAAAAAABU4/O8KAfRbVjjA_pc21qGE2jLlOBpCeQiXewCEw/s1600/stock-photo-174439461.jpg"));
        arr.add(new Photo("https://1.bp.blogspot.com/-e-BJfJKtYaA/XJpBMPJC03I/AAAAAAAAA3o/9SrsYC4nGcg-EhHcv2XaWQZkAYiHE_k5wCLcBGAs/s1600/EmXinh2k__anh-hot-girl-gai-xinh-sexy-nong-bong-khoe-vong-1%2B%25282%2529.jpg"));
        arrayPhoto.postValue(arr);
        return arrayPhoto;
    }
}
