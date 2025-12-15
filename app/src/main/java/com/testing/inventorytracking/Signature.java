package com.testing.inventorytracking;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.testing.inventorytracking.R;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class Signature extends AppCompatDialogFragment {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public sendImageName sendImageName;
    private SignaturePad mSignaturePad;
     private  String name ;
    private Button mClearButton;
    private Button mSaveButton;
    private CheckBox approvement ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   public String hrcode ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    public static Signature newInstance(String hr,sendImageName sendImageName) {
        Signature fragment = new Signature();
        fragment.hrcode = hr ;
        fragment.sendImageName=sendImageName;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_signature, null);
        builder.setView(view);
        mSignaturePad = view.findViewById(R.id.signature_pad);
        verifyStoragePermissions();
        approvement = view.findViewById(R.id.signature_pad_description);
        mSaveButton = view.findViewById(R.id.save_button);
        mClearButton =view.findViewById(R.id.clear_button);
        mSaveButton.setEnabled(false);
        mClearButton.setEnabled(false);
        approvement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(approvement.isChecked() == true) {
                    mSaveButton.setEnabled(true);
                    mClearButton.setEnabled(true);
                    InfoDialog infoDialog = new InfoDialog();
                    infoDialog.setStyle(STYLE_NO_FRAME,R.style.fragmenttt);
                    infoDialog.show(getFragmentManager(), "Dialog");
                }else {
                    mSaveButton.setEnabled(false);
                    mClearButton.setEnabled(false);
                }
            }
        });
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(getContext(), "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {

            }
        });
        final boolean[] clicked = {false};
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });
        final Bitmap[] signatureBitmap = new Bitmap[1];
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureBitmap[0] = mSignaturePad.getSignatureBitmap();
                if (mSignaturePad.isEmpty()) {
                    Toast.makeText(getContext(), "Please Sign First", Toast.LENGTH_SHORT).show();
                }else{
                    if (addJpgSignatureToGallery(signatureBitmap[0]) &&addSvgSignatureToGallery(mSignaturePad.getSignatureSvg())) {
                        Toast.makeText(getContext(), "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Unable to store the signature", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
                    }
                }
                clicked[0] = true ;
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if (mSignaturePad.isEmpty()) {
                        Toast.makeText(getContext(), "Please Sign First", Toast.LENGTH_SHORT).show();
                    }else{
                        if(clicked[0] == true){
                            dismiss();
                        }else{
                            Toast.makeText(getContext(), "Save Signature !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
                return  true;
            }
        });
        return builder.create();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        // If directory doesn't exist, try to create it. Only log an error if creation failed.
        if (!file.exists()) {
            boolean created = file.mkdirs();
            if (!created && !file.exists()) {
                Log.e("SignaturePad", "Directory not created and does not exist: " + file.getAbsolutePath());
            }
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        try {
            Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            OutputStream stream = new FileOutputStream(photo);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            stream.close();
        }catch (NullPointerException n){
            Toast.makeText(getContext(), "Null Signature ", Toast.LENGTH_SHORT).show();
        }

    }


    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format(hrcode+"_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
           name = photo.getName();
               if (sendImageName != null) sendImageName.imageName(name);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format(hrcode+"_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void verifyStoragePermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            Log.e("DB", "PERMISSION GRANTED");
        }
    }
}



