package com.sit.common.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceLandmark;
import com.sit.common.app.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button selectImageBtn;
    private FaceDetector faceDetector;
    private Bitmap eyelashBitmap;
    private Bitmap eyelashCloseBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        selectImageBtn = findViewById(R.id.selectImageBtn);

        // Initialize the face detector with options
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();

        faceDetector = FaceDetection.getClient(options);

        // Load the eyelash image (ensure the image is in the drawable folder)
        eyelashBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eyelash);  // Add your eyelash image here
        eyelashCloseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eyelash_close);  // Add your eyelash image here

        // Set the button to trigger image selection
        selectImageBtn.setOnClickListener(v -> pickImage.launch("image/*"));
    }

    // Activity Result for image picking
    private final ActivityResultLauncher<String> pickImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap);

                        detectFaceAndApplyEyelashFilter(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

    // Detect face and apply eyelash filter
    private void detectFaceAndApplyEyelashFilter(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        faceDetector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        Face face = faces.get(0);
                        // Apply the eyelash filter on the detected face
                        applyEyelashFilter(bitmap, face);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    // Apply the eyelash filter to the detected face
    private void applyEyelashFilter(Bitmap bitmap, Face face) {
        // Get the landmarks for the eyes
        FaceLandmark leftEye = face.getLandmark(FaceLandmark.LEFT_EYE);
        FaceLandmark rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE);

        if (leftEye != null && rightEye != null) {
            // Extract the positions of the eyes
            float leftEyeX = leftEye.getPosition().x;
            float leftEyeY = leftEye.getPosition().y;
            float rightEyeX = rightEye.getPosition().x;
            float rightEyeY = rightEye.getPosition().y;

            // Check if eyes are closed using probability
            boolean leftEyeClosed = face.getLeftEyeOpenProbability() < 0.5;  // Eyes closed if probability is low
            boolean rightEyeClosed = face.getRightEyeOpenProbability() < 0.5;  // Eyes closed if probability is low

            Log.d("MyTag","leftEyeClosed::"+leftEyeClosed);
            Log.d("MyTag","rightEyeClosed::"+rightEyeClosed);

            // Calculate the width and height of the eyelash image based on eye positions
            float eyeDistance = Math.abs(rightEyeX - leftEyeX);
            int eyelashWidth = (int) (eyeDistance * 0.9);  // Adjust the size of the eyelash based on eye distance
            int eyelashHeight = (int) (eyelashWidth * 0.3); // Maintain a reasonable aspect ratio

            // Create a mutable bitmap to work with
            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            // Prepare the canvas to draw on the bitmap
            Canvas canvas = new Canvas(mutableBitmap);

            // Create a rectangle for where the eyelash will be drawn
            Rect leftEyelashRect = new Rect(
                    (int) (leftEyeX - eyelashWidth / 2), (int) (leftEyeY - eyelashHeight / 2),
                    (int) (leftEyeX + eyelashWidth / 2), (int) (leftEyeY + eyelashHeight / 2)
            );

            Rect rightEyelashRect = new Rect(
                    (int) (rightEyeX - eyelashWidth / 2), (int) (rightEyeY - eyelashHeight / 2),
                    (int) (rightEyeX + eyelashWidth / 2), (int) (rightEyeY + eyelashHeight / 2)
            );

            // Adjust the size and position of the eyelash bitmap for the left eye
            Bitmap scaledLeftEyelash;
            if (leftEyeClosed) {
                scaledLeftEyelash = Bitmap.createScaledBitmap(eyelashCloseBitmap, eyelashWidth, eyelashHeight, true);
            } else {
                scaledLeftEyelash = Bitmap.createScaledBitmap(eyelashBitmap, eyelashWidth, eyelashHeight, true);
            }
            // Draw the eyelash image on the canvas
            canvas.drawBitmap(scaledLeftEyelash, null, leftEyelashRect, null);

            Bitmap scaledRightEyelash;
            if (rightEyeClosed) {
                scaledRightEyelash = Bitmap.createScaledBitmap(eyelashCloseBitmap, eyelashWidth, eyelashHeight, true);
            } else {
                scaledRightEyelash = Bitmap.createScaledBitmap(eyelashBitmap, eyelashWidth, eyelashHeight, true);
            }
            // Adjust the size and position of the eyelash bitmap for the right eye
            canvas.drawBitmap(scaledRightEyelash, null, rightEyelashRect, null);

            // Set the modified bitmap to the ImageView
            imageView.setImageBitmap(mutableBitmap);
        }
    }
}