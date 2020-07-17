package javatar.ficdev.com.whatsappstatuesdownloader;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.m_RecyclerView);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        Dexter.withContext(this)
                .withPermissions(Arrays.asList(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        recyclerView.setAdapter(new AuditAdapter(getData(),MainActivity.this));
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
    }

    private ArrayList<Statue> getData() {
        ArrayList<Statue> spacecrafts=new ArrayList<>();
        //TARGET FOLDER
        String dir = Environment.getExternalStorageDirectory()+"/WhatsApp/Media/.Statuses";
        File downloadsFolder = new File(dir);

        if(downloadsFolder.isDirectory())
        {
            //GET ALL FILES IN DOWNLOAD FOLDER
            File[] files=downloadsFolder.listFiles();

            //LOOP THRU THOSE FILES GETTING NAME AND URI
            assert files != null;
            for (File file : files) {
                //Toast.makeText(this, ""+file.get, Toast.LENGTH_SHORT).show();
                Statue s = new Statue();
                s.setName(file.getName());
                s.setUri(file.getAbsolutePath());
                s.setPath(file.getPath());
                s.setVideo(file.getName().contains(".mp4"));
                spacecrafts.add(s);
            }

        }
        return spacecrafts;
    }
}

