package javatar.ficdev.com.whatsappstatuesdownloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;
class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.ViewHolder> {

    private List<Statue> List_Item;

    Context context;

    AuditAdapter(List<Statue> list_Item, Context context) {
        List_Item = list_Item;
        this.context = context;
    }

    @NonNull
    @Override
    public AuditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.statue_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AuditAdapter.ViewHolder holder, final int position) {
        final Statue statue = List_Item.get(position);
        if(statue.isVideo()){
            holder.image.setVisibility(View.GONE);
            holder.video.setVisibility(View.VISIBLE);
            holder.button.setVisibility(View.VISIBLE);
            holder.imageButton.setVisibility(View.VISIBLE);

            holder.video.setVideoURI(Uri.parse(statue.getPath()));

            holder.video.requestFocus();
            holder.video.pause();
            holder.button.setText("play");
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.video.isPlaying()){
                        holder.video.pause();
                        holder.button.setText("play");
                    }else {
                        holder.video.start();
                        holder.button.setText("stop");
                    }
                }
            });

            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint({"ResourceAsColor", "SetTextI18n"})
                @Override
                public void onClick(View view) {
                    save(statue.getPath(),".mp4");
                }
            });

        }else {
            Bitmap myBitmap = BitmapFactory.decodeFile(statue.getUri());
            holder.image.setImageBitmap(myBitmap);
            holder.image.setVisibility(View.VISIBLE);
            holder.video.setVisibility(View.GONE);
            holder.button.setVisibility(View.GONE);
            holder.imageButton.setVisibility(View.GONE);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @SuppressLint({"ResourceAsColor", "SetTextI18n"})
                @Override
                public void onClick(View view) {
                    save(statue.getPath(),".jpg");
                }
            });
        }


    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    void save(final String path, final String type){
        final AlertDialog dialog2 = new AlertDialog.Builder(context).create();
        final EditText text = new EditText(context);
        text.setTextColor(R.color.colorPrimaryDark);
        dialog2.setView(text);
        final String dir = Environment.getExternalStorageDirectory()+"/WhatsApp Statues";
        File f = new File(dir);
        boolean mkdir = false;
        if(!f.exists()){
            mkdir = f.mkdir();
        }


        text.setText("statues" + Objects.requireNonNull(f.listFiles()).length);
        text.requestFocus();
        dialog2.setTitle(Html.fromHtml("<font color='#000000'>Save this statue </font>"));
        dialog2.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = text.getText().toString();
                try {
                    copyFile(new File(path),new File(dir+ "/"+s+type));
                    Toast.makeText(context, "Saved in /WhatsApp Statues", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog2.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog2.show();
    }

    private void copyFile(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    @Override
    public int getItemCount() {
        return (null != List_Item ? List_Item.size() : 0);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        VideoView video;
        Button button;
        ImageButton imageButton;
        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.imageView);
            video = view.findViewById(R.id.videoView);
            button = view.findViewById(R.id.button);
            imageButton = view.findViewById(R.id.imageButton);
        }
    }
}
