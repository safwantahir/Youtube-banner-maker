package com.example.backgroundremoverlibrary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.example.backgroundremoverlibrary.databinding.ActivityMainPreviousBinding;
import com.slowmac.autobackgroundremover.BackgroundRemover;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.slowmac.autobackgroundremover.OnBackgroundChangeListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import yuku.ambilwarna.AmbilWarnaDialog;


public class SecondBanner extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_GALLERY_IMAGE_REQUEST = 20;
    ImageView selectedimageview1;
    private ImageView imageView;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int IMAGE_WIDTH = 2048;
    private static final int IMAGE_HEIGHT = 1152;

    private Bitmap selectedImage;
    private ImageView selectedImageView;
    private AlertDialog dialog;
    ImageView Main_Camera, Main_Gallery;
    ImageView Grid;
    //This is main menu that are images in scrollview
    ImageView backgroundimgButton, artimgButton, textButton, effectimgButton, imageButton, emojiButton;
    // These horizontal scrollviews will be opened when main options is selected
    HorizontalScrollView bgRcy, artRcy, textRcy, EffectRcy, imgRcy, emojiRcy;
    // These are second layered horizontal scrollviews
    //for background option
    HorizontalScrollView bg_ka_bgRcy, bg_ka_textureRcy;
    //for text options
    HorizontalScrollView text_ka_color, text_k_fonts;
    // background main option k andr jo text hn ye un k naam hn
    TextView bgbackground, bgtexture, bg_ka_galleryimage, bgcolor;
    //text k main option k andr jo images hn
    ImageView bgi1, bgi2, bgi3, bgi4, bgi5, bgi6, bgi7, bgi8;
    ImageView tgi1, tgi2, tgi3, tgi4, tgi5, tgi6, tgi7, tgi8;
    ImageView art1, art2, art3, art4, art5, art6, art7, art8, Bgemover;
    //emojis k andr jo options hn ye wo hn
    ImageView emoji1, emoji2, emoji3, emoji4, emoji5, emoji6, emoji7, emoji8, emoji9, emoji10, emoji11, emoji12, emoji13, emoji14, emoji15, emoji16, emoji17, emoji18;
    // text k andr jo options hn ye wo hn create new, resize waghera
    ImageView Createnewtext, textfont, fontcolor, fontsize, delete;
    int subi = 0;
    private ActivityMainPreviousBinding binding;

    private String currentPhotoPath;
    private ResizableTextView selectedTextView;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    RelativeLayout banner_background;
    private float lastTouchX;
    private float lastTouchY;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAMERA = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_REQUEST_CODE = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    ImageView Save_Button;


    private TextView textView;

    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_banenr, container, false);

        // Force landscape orientation
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        Save_Button = view.findViewById(R.id.savebtn);
        Save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    captureAndSaveScreenshot();
                } else {
                    requestPermission();
                }
            }
        });
        Bgemover = view.findViewById(R.id.bgremover);
        Bgemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivityPrevious.class);
                startActivity(intent);
            }
        });
        delete = view.findViewById(R.id.delbtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedItem();
            }
        });

        //Main option images
        backgroundimgButton = view.findViewById(R.id.bgbtn);
        artimgButton = view.findViewById(R.id.stickerbtn);
        textButton = view.findViewById(R.id.textbtn);
        effectimgButton = view.findViewById(R.id.effectbtn);
        imageButton = view.findViewById(R.id.imagebtn);
        emojiButton = view.findViewById(R.id.emojibtn);
        Grid = view.findViewById(R.id.grid);


        //Submenu recyclers
        bgRcy = view.findViewById(R.id.hrizntlscrlviewforbg);
        artRcy = view.findViewById(R.id.hrizntlscrlviewforart);
        textRcy = view.findViewById(R.id.hrizntlscrlviewfortext);
        EffectRcy = view.findViewById(R.id.hrizntlscrlviewforeffect);
        imgRcy = view.findViewById(R.id.hrizntlscrlviewforimages);
        emojiRcy = view.findViewById(R.id.hrizntlscrlviewforemojis);


        Main_Gallery = view.findViewById(R.id.main_gallery);

        Main_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainOpenGallery();
            }
        });

        //emojis ki declaration
        emoji1 = view.findViewById(R.id.emj1);
        emoji2 = view.findViewById(R.id.emj2);
        emoji3 = view.findViewById(R.id.emj3);
        emoji4 = view.findViewById(R.id.emj4);
        emoji5 = view.findViewById(R.id.emj5);
        emoji6 = view.findViewById(R.id.emj6);
        emoji7 = view.findViewById(R.id.emj7);
        emoji8 = view.findViewById(R.id.emj8);
        emoji9 = view.findViewById(R.id.emj9);
        emoji10 = view.findViewById(R.id.emj10);
        emoji11 = view.findViewById(R.id.emj11);
        emoji12 = view.findViewById(R.id.emj12);
        emoji13 = view.findViewById(R.id.emj13);
        emoji14 = view.findViewById(R.id.emj14);
        emoji15 = view.findViewById(R.id.emj15);
        emoji16 = view.findViewById(R.id.emj16);
        emoji17 = view.findViewById(R.id.emj17);
        emoji18 = view.findViewById(R.id.emj18);


        //submenu of submenu recyclers
        bg_ka_bgRcy = view.findViewById(R.id.bg_ka_bg);
        bg_ka_textureRcy = view.findViewById(R.id.bg_ka_texture);
        bg_ka_galleryimage = view.findViewById(R.id.bg_gallery);
        bgcolor = view.findViewById(R.id.color);

        text_k_fonts = view.findViewById(R.id.text_ka_fonts);
        text_ka_color = view.findViewById(R.id.text_ka_color);


        //art k andr k items
        art1 = view.findViewById(R.id.art1);
        art2 = view.findViewById(R.id.art2);
        art3 = view.findViewById(R.id.art3);
        art4 = view.findViewById(R.id.art4);
        art5 = view.findViewById(R.id.art5);
        art6 = view.findViewById(R.id.art6);
        art7 = view.findViewById(R.id.art7);
        art8 = view.findViewById(R.id.art8);


        // background main option k andr jo text hn ye un k naam hn
        bgbackground = view.findViewById(R.id.background);
        bgtexture = view.findViewById(R.id.texture);


        banner_background = view.findViewById(R.id.main_layout);

        bgi1 = view.findViewById(R.id.bg_ka_bg1);
        bgi2 = view.findViewById(R.id.bg_ka_bg2);
        bgi3 = view.findViewById(R.id.bg_ka_bg3);
        bgi4 = view.findViewById(R.id.bg_ka_bg4);
        bgi5 = view.findViewById(R.id.bg_ka_bg5);
        bgi6 = view.findViewById(R.id.bg_ka_bg6);
        bgi7 = view.findViewById(R.id.bg_ka_bg7);
        bgi8 = view.findViewById(R.id.bg_ka_bg8);

        tgi1 = view.findViewById(R.id.bg_ka_texture1);
        tgi2 = view.findViewById(R.id.bg_ka_texture2);
        tgi3 = view.findViewById(R.id.bg_ka_texture3);
        tgi4 = view.findViewById(R.id.bg_ka_texture4);
        tgi5 = view.findViewById(R.id.bg_ka_texture5);
        tgi6 = view.findViewById(R.id.bg_ka_texture6);
        tgi7 = view.findViewById(R.id.bg_ka_texture7);
        tgi8 = view.findViewById(R.id.bg_ka_texture8);

        //text k main option k andr jo submenus hn ye unki declaration hn like create new font etc
        Createnewtext = view.findViewById(R.id.newtxt);


        // text k jo main options hn ye unky click listeners hn
        Createnewtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });


        //ye main menu k click listener hn
        backgroundimgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgRcy.setVisibility(view.VISIBLE);
                artRcy.setVisibility(view.GONE);
                textRcy.setVisibility(view.GONE);
                EffectRcy.setVisibility(view.GONE);
                imgRcy.setVisibility(view.GONE);
                emojiRcy.setVisibility(view.GONE);
            }
        });
        artimgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgRcy.setVisibility(view.GONE);
                artRcy.setVisibility(view.VISIBLE);
                textRcy.setVisibility(view.GONE);
                EffectRcy.setVisibility(view.GONE);
                imgRcy.setVisibility(view.GONE);
                emojiRcy.setVisibility(view.GONE);
                bg_ka_bgRcy.setVisibility(view.GONE);
                bg_ka_textureRcy.setVisibility(view.GONE);
            }
        });
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgRcy.setVisibility(view.GONE);
                artRcy.setVisibility(view.GONE);
                textRcy.setVisibility(view.VISIBLE);
                EffectRcy.setVisibility(view.GONE);
                imgRcy.setVisibility(view.GONE);
                emojiRcy.setVisibility(view.GONE);
                bg_ka_bgRcy.setVisibility(view.GONE);
                bg_ka_textureRcy.setVisibility(view.GONE);
            }
        });
        effectimgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgRcy.setVisibility(view.GONE);
                artRcy.setVisibility(view.GONE);
                textRcy.setVisibility(view.GONE);
                EffectRcy.setVisibility(view.VISIBLE);
                imgRcy.setVisibility(view.GONE);
                emojiRcy.setVisibility(view.GONE);
                bg_ka_bgRcy.setVisibility(view.GONE);
                bg_ka_textureRcy.setVisibility(view.GONE);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgRcy.setVisibility(view.GONE);
                artRcy.setVisibility(view.GONE);
                textRcy.setVisibility(view.GONE);
                EffectRcy.setVisibility(view.GONE);
                imgRcy.setVisibility(view.VISIBLE);
                emojiRcy.setVisibility(view.GONE);
                bg_ka_bgRcy.setVisibility(view.GONE);
                bg_ka_textureRcy.setVisibility(view.GONE);
            }
        });
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgRcy.setVisibility(view.GONE);
                artRcy.setVisibility(view.GONE);
                textRcy.setVisibility(view.GONE);
                EffectRcy.setVisibility(view.GONE);
                imgRcy.setVisibility(view.GONE);
                emojiRcy.setVisibility(view.VISIBLE);
                bg_ka_bgRcy.setVisibility(view.GONE);
                bg_ka_textureRcy.setVisibility(view.GONE);
            }
        });

// ye submenu k click listeners hn
        bgbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bg_ka_bgRcy.setVisibility(view.VISIBLE);
                bg_ka_textureRcy.setVisibility(view.GONE);

            }
        });
        bgtexture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bg_ka_bgRcy.setVisibility(view.GONE);
                bg_ka_textureRcy.setVisibility(view.VISIBLE);
            }
        });
        bg_ka_galleryimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
            }
        });
        bgcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

        //ye submenu k andr jo items hn un k click listeners hn
        bgi1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg1);
            }
        });
        bgi2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg2);
            }
        });
        bgi3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg3);
            }
        });
        bgi4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg4);
            }
        });
        bgi5.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg5);
            }
        });
        bgi6.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg6);
            }
        });
        bgi7.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg7);
            }
        });
        bgi8.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg8);
            }
        });
        tgi1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg1);
            }
        });
        tgi2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg2);
            }
        });
        tgi3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg3);
            }
        });
        tgi4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg4);
            }
        });
        tgi5.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg5);
            }
        });
        tgi6.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg6);
            }
        });
        tgi7.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg7);
            }
        });
        tgi8.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                banner_background.setBackgroundResource(R.drawable.bg8);
            }
        });
        // Set up scale gesture detector for pinch-to-zoom

// art k andr jo images hn ye unky click listeners hn
        art1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new ImageView
                subi = 1;
                addNewImageView();
            }
        });
        art2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 2;
                addNewImageView();
            }
        });
        art3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 3;
                addNewImageView();
            }
        });
        art4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 4;
                addNewImageView();
            }
        });
        art5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 5;
                addNewImageView();
            }
        });
        art6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 6;
                addNewImageView();
            }
        });
        art7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 7;
                addNewImageView();
            }
        });
        art8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 8;
                addNewImageView();
            }
        });


        // emojis k onclick listeners
        emoji1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new ImageView
                subi = 11;
                addNewImageView();
            }
        });
        emoji2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 12;
                addNewImageView();
            }
        });
        emoji3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 13;
                addNewImageView();
            }
        });
        emoji4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 14;
                addNewImageView();
            }
        });
        emoji5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 15;
                addNewImageView();
            }
        });
        emoji6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 16;
                addNewImageView();
            }
        });
        emoji7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 17;
                addNewImageView();
            }
        });
        emoji8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 18;
                addNewImageView();
            }
        });
        emoji9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 19;
                addNewImageView();
            }
        });
        emoji10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 110;
                addNewImageView();
            }
        });
        emoji11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 111;
                addNewImageView();
            }
        });
        emoji12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 112;
                addNewImageView();
            }
        });
        emoji13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 113;
                addNewImageView();
            }
        });
        emoji14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 114;
                addNewImageView();
            }
        });
        emoji15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 115;
                addNewImageView();
            }
        });
        emoji16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 116;
                addNewImageView();
            }
        });
        emoji17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 117;
                addNewImageView();
            }
        });
        emoji18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subi = 118;
                addNewImageView();
            }
        });

        banner_background.setOnTouchListener(null);
        // Set up ScaleGestureDetector
        scaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Allow automatic orientation changes when the fragment is destroyed
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                // Decode the image URI to a Bitmap

                // Set the Bitmap as the background of the LinearLayout
                banner_background.setBackground(null); // Clear previous background
                banner_background.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_GALLERY_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();

            // Create a new ImageView
            ImageView newImageView = new ImageView(requireContext());
            newImageView.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));

            // Load image into the new ImageView using Glide
            Glide.with(requireContext())
                    .load(selectedImageUri)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);

            // Add the new ImageView to the banner_background RelativeLayout
            banner_background.addView(newImageView);
            selectedimageview1 = newImageView;

            // Set onTouchListener for moving the new ImageView
            setOnTouchListenerForImageView(newImageView);
        }

    }

    private void showColorPickerDialog() {
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(getContext(), Color.WHITE,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        // Set the background color of the LinearLayout
                        banner_background.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // Do nothing here
                    }
                });

        colorPickerDialog.show();
    }

    private void addNewImageView() {
        // Create a new ImageView
        ImageView newImageView = new ImageView(requireContext());
        newImageView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));


        // Load image into the new ImageView using Glide

        if (subi == 1) {
            Glide.with(requireContext())
                    .load(R.drawable.subscribe1)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 2) {
            Glide.with(requireContext())
                    .load(R.drawable.subscribe2)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 3) {
            Glide.with(requireContext())
                    .load(R.drawable.subscribe3)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 4) {
            Glide.with(requireContext())
                    .load(R.drawable.subscribe4)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 5) {
            Glide.with(requireContext())
                    .load(R.drawable.subscribe5)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 6) {
            Glide.with(requireContext())
                    .load(R.drawable.subscribe6)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 7) {
            Glide.with(requireContext())
                    .load(R.drawable.subscribe7)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 8) {
            Glide.with(requireContext())
                    .load(R.drawable.subscribe8)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 11) {
            Glide.with(requireContext())
                    .load(R.drawable.em1)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 12) {
            Glide.with(requireContext())
                    .load(R.drawable.em2)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 13) {
            Glide.with(requireContext())
                    .load(R.drawable.em3)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 14) {
            Glide.with(requireContext())
                    .load(R.drawable.em4)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 15) {
            Glide.with(requireContext())
                    .load(R.drawable.em5)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 16) {
            Glide.with(requireContext())
                    .load(R.drawable.em6)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 17) {
            Glide.with(requireContext())
                    .load(R.drawable.em7)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 18) {
            Glide.with(requireContext())
                    .load(R.drawable.em8)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 19) {
            Glide.with(requireContext())
                    .load(R.drawable.em9)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 110) {
            Glide.with(requireContext())
                    .load(R.drawable.em10)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 111) {
            Glide.with(requireContext())
                    .load(R.drawable.em11)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 112) {
            Glide.with(requireContext())
                    .load(R.drawable.em12)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 113) {
            Glide.with(requireContext())
                    .load(R.drawable.em13)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 114) {
            Glide.with(requireContext())
                    .load(R.drawable.em14)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 115) {
            Glide.with(requireContext())
                    .load(R.drawable.em15)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 116) {
            Glide.with(requireContext())
                    .load(R.drawable.em16)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 117) {
            Glide.with(requireContext())
                    .load(R.drawable.em17)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        } else if (subi == 118) {
            Glide.with(requireContext())
                    .load(R.drawable.em18)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newImageView);
        }
        // Add the new ImageView to the banner_background RelativeLayout
        banner_background.addView(newImageView);
        selectedimageview1 = newImageView;


        // Set onTouchListener for moving the new ImageView
//        newImageView.setOnTouchListener(new View.OnTouchListener() {
//            private ScaleGestureDetector imageScaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());
//            float dX, dY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Handle touch events for moving and resizing the ImageView
//                imageScaleGestureDetector.onTouchEvent(event);
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        dX = v.getX() - event.getRawX();
//                        dY = v.getY() - event.getRawY();
//                        selectedImageView = (ImageView) v;
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//                        v.setX(event.getRawX() + dX);
//                        v.setY(event.getRawY() + dY);
//                        break;
//
//                    default:
//                        return false;
//                }
//
//                return true;
//            }
//        });

        // Set OnTouchListener for the TextView
        newImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                selectedImageView = (ImageView) v;
                v.bringToFront();
                viewTransformation(v, event);
                return true;
            }
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (selectedImageView != null) {

                // Handle pinch-to-zoom scaling for the selected image view
                float scaleFactor = detector.getScaleFactor();
                float newScale = selectedImageView.getScaleX() * scaleFactor;

                // Set bounds for scaling to prevent the image from becoming too small or too large
                newScale = Math.max(0.1f, Math.min(newScale, 5.0f));

                // Apply scaling to the selected ImageView
                selectedImageView.setScaleX(newScale);
                selectedImageView.setScaleY(newScale);
            }
            return true;
        }
    }

    private void mainOpenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_GALLERY_IMAGE_REQUEST);
    }

    // DraggableTextView.java
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        final AppCompatEditText editText = dialogView.findViewById(R.id.editText);
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        final AlertDialog dialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = editText.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    createResizableTextView(userInput);
                    dialog.dismiss();
                } else {
                    // Handle empty input case if needed
                }
            }
        });

        dialog.show();
    }

    private void createResizableTextView(String text) {
        ResizableTextView textView = new ResizableTextView(getContext());
        textView.setText(text);

        // Set layout parameters for the TextView (adjust as needed)
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Set initial position (adjust as needed)
        layoutParams.leftMargin = 100;
        layoutParams.topMargin = 100;

        // Increase the size of the TextView
        textView.setTextSize(40); // Change the size as needed

        // Change the color of the TextView
        textView.setTextColor(Color.BLUE);

        textView.setLayoutParams(layoutParams);

        // Set OnClickListener for the TextView
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomizeDialog();
                selectedTextView = (ResizableTextView) v;
            }
        });


//        // Set OnTouchListener for the TextView
//        textView.setOnTouchListener(new View.OnTouchListener() {
//            private long startTime = 0;
//            private static final long LONG_PRESS_DURATION = 5000; // 5 seconds
//            private boolean isLongPressHandled = false;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                TextView view = (TextView) v;
//                view.bringToFront();
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        startTime = System.currentTimeMillis();
//                        isLongPressHandled = false;
//                        viewTransformation(view, event);
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//                        if (!isLongPressHandled && System.currentTimeMillis() - startTime >= LONG_PRESS_DURATION) {
//                            // Long press detected
//                            isLongPressHandled = true;
//                            showCustomizeDialog();
//                        }
//                        viewTransformation(view, event);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        // Handle single click here
//                        if (!isLongPressHandled) {
//                            // Single click detected
//                            showCustomizeDialog();
//                        }
//                        break;
//                }
//                return true;
//            }
//        });


        // Set OnTouchListener for the TextView
//        textView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                TextView view = (TextView) v;
//                view.bringToFront();
//                viewTransformation(view, event);
//
//
//                return true;
//            }
//        });

        // Set OnTouchListener for the TextView
        textView.setOnTouchListener(new View.OnTouchListener() {
            private float lastClickX;
            private float lastClickY;
            private static final int CLICK_ACTION_THRESHOLD = 5;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                TextView view = (TextView) v;
                view.bringToFront();
                viewTransformation(view, event);


                float x = event.getRawX();
                float y = event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchX = x;
                        lastTouchY = y;
                        lastClickX = x;
                        lastClickY = y;
                        Grid.setVisibility(View.VISIBLE);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float deltaX = x - lastTouchX;
                        float deltaY = y - lastTouchY;

                        v.setX(v.getX() + deltaX);
                        v.setY(v.getY() + deltaY);

                        lastTouchX = x;
                        lastTouchY = y;
                        break;

                    case MotionEvent.ACTION_UP:
                        float clickDeltaX = Math.abs(x - lastClickX);
                        float clickDeltaY = Math.abs(y - lastClickY);

                        if (clickDeltaX < CLICK_ACTION_THRESHOLD && clickDeltaY < CLICK_ACTION_THRESHOLD) {
                            // Treat it as a click and call the click listener manually
                            if (textView.hasOnClickListeners()) {
                                textView.performClick();
                            }
                        }
                        Grid.setVisibility(View.INVISIBLE);
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });


        // Add the TextView to the "banner_background" layout
        banner_background.addView(textView);
    }

    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                Grid.setVisibility(View.VISIBLE);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);

                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();

                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                Grid.setVisibility(View.INVISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            view.setRotation((float) (view.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    private void showCustomizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.customize_textview_dialog, null);
        builder.setView(dialogView);

        Spinner fontSpinner = dialogView.findViewById(R.id.fontSpinner);
        Spinner colorSpinner = dialogView.findViewById(R.id.colorSpinner);
        EditText sizeEditText = dialogView.findViewById(R.id.sizeEditText);
        Button btnApply = dialogView.findViewById(R.id.btnApply);

        // Define color names directly in Java
        String[] colorNames = {"Black", "White", "Red", "Blue", "Yellow", "Orange", "Green", "Purple", "Pink", "Light Blue"};
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, colorNames);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);

        // Define font names directly in Java
        String[] fontNames = {"font1", "font2", "font3", "font4", "font5", "font6", "font7", "font8", "font9"};
        ArrayAdapter<String> fontAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, fontNames);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(fontAdapter);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCustomizations(
                        fontSpinner.getSelectedItem().toString(),
                        colorSpinner.getSelectedItem().toString(),
                        sizeEditText.getText().toString()
                );
                // Dismiss the dialog
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        // Initialize the dialog
        dialog = builder.create();
        dialog.show();
    }

    private void applyCustomizations(String selectedFont, String selectedColor, String textSize) {
        // Apply customizations to the selected TextView
        if (selectedTextView != null) {
            try {
                // Apply font
                if (selectedFont != null && !selectedFont.isEmpty()) {
                    int fontResourceId = getResources().getIdentifier(selectedFont, "font", getContext().getPackageName());
                    Log.d("Customization", "Font Resource ID: " + fontResourceId);
                    Typeface typeface = ResourcesCompat.getFont(getContext(), fontResourceId);
                    selectedTextView.setTypeface(typeface);
                    Log.d("Customization", "Font applied successfully");
                }

                // Map color names to corresponding color values
                String[] colorNames = {"Black", "White", "Red", "Blue", "Yellow", "Orange", "Green", "Purple", "Pink", "Light Blue"};
                String[] colorValues = {"#FF000000", "#FFFFFFFF", "#F40707", "#0A56EF", "#FFEB3B", "#FF5722", "#07EC10", "#673AB7", "#E50DED", "#08E0FB"};
                String selectedColorValue = colorValues[Arrays.asList(colorNames).indexOf(selectedColor)];

                // Apply color
                selectedTextView.setTextColor(Color.parseColor(selectedColorValue));
                Log.d("Customization", "Color applied successfully");

                // Apply text size
                if (!TextUtils.isEmpty(textSize)) {
                    float size = Float.parseFloat(textSize);
                    selectedTextView.setTextSize(size);
                    Log.d("Customization", "Text size applied successfully");
                }
            } catch (Resources.NotFoundException e) {
                // Handle font loading exception
                e.printStackTrace();
                Log.e("Customization", "Error applying customizations: " + e.getMessage());
            }
        }
    }


    private void setOnTouchListenerForImageView(ImageView imageView) {


        // Set OnTouchListener for the TextView
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                selectedImageView = (ImageView) view;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });


//        imageView.setOnTouchListener(new View.OnTouchListener() {
//            private ScaleGestureDetector imageScaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());
//            float dX, dY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Handle touch events for moving and resizing the ImageView
//                imageScaleGestureDetector.onTouchEvent(event);
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        dX = v.getX() - event.getRawX();
//                        dY = v.getY() - event.getRawY();
//                        selectedImageView = (ImageView) v;
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//                        v.setX(event.getRawX() + dX);
//                        v.setY(event.getRawY() + dY);
//                        break;
//
//                    default:
//                        return false;
//                }
//
//                return true;
//            }
//        });
    }


    private void deleteSelectedItem() {
        // Check if a ResizableTextView is selected
        if (selectedTextView != null) {
            // Remove the selected ResizableTextView from the banner_background layout
            banner_background.removeView(selectedTextView);
            selectedTextView = null; // Reset selectedTextView
            return; // Exit the method after deleting the selected text
        }

        // Add logic for deleting other types of selected items (image, color, emoji) if needed
        // Example: Check if an ImageView is selected and remove it from the layout
        if (selectedImageView != null) {
            banner_background.removeView(selectedImageView);
            selectedImageView = null;
            // Add similar logic for other types of selected items
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureAndSaveScreenshot();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Cannot save screenshot.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void captureAndSaveScreenshot() {
        int desiredWidth = 1280;
        int desiredHeight = 720;

        // Get the original dimensions of banner_background
        int originalWidth = banner_background.getWidth();
        int originalHeight = banner_background.getHeight();

        // Resize the bitmap to the desired dimensions
        Bitmap resizedBitmap = getBitmapFromView(banner_background, desiredWidth, desiredHeight);

        if (resizedBitmap != null) {
            saveImageToGallery(requireContext(), resizedBitmap);
            Toast.makeText(requireContext(), "Screenshot saved to gallery", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Error capturing screenshot", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitmapFromView(View view, int desiredWidth, int desiredHeight) {
        // Get the original dimensions of the view
        int originalWidth = view.getWidth();
        int originalHeight = view.getHeight();

        // Calculate the scale factor for resizing
        float scaleX = (float) desiredWidth / originalWidth;
        float scaleY = (float) desiredHeight / originalHeight;

        // Create a matrix for the scaling transformation
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);

        // Create a bitmap with the desired dimensions
        Bitmap resizedBitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, Bitmap.Config.ARGB_8888);

        // Apply the scaling transformation to the original view and draw it on the resized bitmap
        Canvas canvas = new Canvas(resizedBitmap);
        canvas.setMatrix(matrix);
        view.draw(canvas);

        return resizedBitmap;
    }
    private void saveImageToGallery(Context context, Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "myimg" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Get the directory path for the "youtube banner" folder
        File youtubeBannerDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "youtube banner");

        // Check if the directory exists, if not, create it
        if (!youtubeBannerDir.exists()) {
            youtubeBannerDir.mkdirs();
        }

        // Save the image to the "youtube banner" directory
        File imageFile = new File(youtubeBannerDir, values.getAsString(MediaStore.Images.Media.TITLE) + ".jpg");

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // Use MediaScannerConnection to update the MediaStore immediately
            MediaScannerConnection.scanFile(context,
                    new String[]{imageFile.getAbsolutePath()},
                    new String[]{"image/jpeg"},
                    (path, uri) -> {
                        Toast.makeText(context, "Screenshot saved to youtube banner folder", Toast.LENGTH_SHORT).show();
                    });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving screenshot", Toast.LENGTH_SHORT).show();
        }
    }

}