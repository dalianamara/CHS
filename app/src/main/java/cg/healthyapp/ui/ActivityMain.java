package cg.healthyapp.ui;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import cg.healthyapp.FragmentHome;
import cg.healthyapp.R;

public class ActivityMain extends FragmentActivity  {
    @Override
    protected void onCreate(final  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);
        loadhomefragment();
    }
    private void loadhomefragment(){
        Fragment newFragment=new FragmentHome();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment,newFragment)
                .commit();
    }
}
