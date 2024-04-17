package com.bnc.project.groupeleven.filepickereprep;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<String[]> filePickerLauncher;

    private TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textView);

        // Initialize ActivityResultLauncher
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.OpenMultipleDocuments(), this::handleSelectedFiles);


        // Open file picker when the activity starts
        openFilePicker();
    }

    private void openFilePicker() {
        String[] mimeTypes = {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain"};
        filePickerLauncher.launch(mimeTypes);
    }

    private void handleSelectedFiles(List<Uri> uris) {
        List<String> fileContents = new ArrayList<>();

        for (Uri uri : uris) {
            String content = readFileContent(uri);
            fileContents.add(content);
        }

        // Display file contents in TextView
        StringBuilder stringBuilder = new StringBuilder();
        for (String content : fileContents) {
            stringBuilder.append(content).append("\n\n");
        }
        textView.setText(stringBuilder.toString());
    }

    private String readFileContent(Uri uri) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while (true) {
                assert inputStream != null;
                if ((bytesRead = inputStream.read(buffer)) == -1) break;
                stringBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }
            inputStream.close();
        } catch (IOException e) {
            Log.e("TAG", "Error reading file", e);
        }
        return stringBuilder.toString();
    }



}