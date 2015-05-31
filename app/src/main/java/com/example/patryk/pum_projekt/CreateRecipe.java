package com.example.patryk.pum_projekt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;


public class CreateRecipe extends Activity implements View.OnClickListener {


    private MyDBHandler myDBHandler;
    Button buttonAddIngredient, buttonAddAlarm, buttonRemoveAlarm, buttonGalleryImg, buttonPhotoImg,
    buttonSave;
    ListView ingredientList;
    private ImageView img;
    private IngredientListAdapter adapter;
    private ArrayList<Ingredient> ingredients;
    private EditText editIngredient, editAmount, editName;
    private LinkedList<Integer> alarmTimeList;
    private LinkedList<String> alarmNameList;
    private TextView textViewAlarms;
    private EditText editDesc;
    private int editId;
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);

        ingredientList = (ListView) findViewById(R.id.IngredientsList);
        ingredientList.addHeaderView(getLayoutInflater().inflate(R.layout.activity_recipe_add_header, ingredientList, false));
        ingredientList.addFooterView(getLayoutInflater().inflate(R.layout.activity_recipe_add_footer,ingredientList, false));

        ingredients = new ArrayList<>();
        adapter = new IngredientListAdapter(this, R.layout.create_ingredient_view, ingredients);
        ingredientList.setAdapter(adapter);


        myDBHandler = new MyDBHandler(this, null, null, 0);

        buttonAddIngredient = (Button) findViewById(R.id.ButtonAddIngredient);
        buttonAddIngredient.setOnClickListener(this);
        buttonAddAlarm = (Button) findViewById(R.id.ButtonAddAlarm);
        buttonAddAlarm.setOnClickListener(this);
        buttonRemoveAlarm = (Button) findViewById(R.id.ButtonRemoveAlarm);
        buttonRemoveAlarm.setOnClickListener(this);
        buttonPhotoImg = (Button) findViewById(R.id.ButtonTakePhoto);
        buttonPhotoImg.setOnClickListener(this);
        buttonGalleryImg = (Button) findViewById(R.id.ButtonImgGallery);
        buttonGalleryImg.setOnClickListener(this);
        buttonSave = (Button) findViewById(R.id.ButtonFinishAdding);
        buttonSave.setOnClickListener(this);


        editAmount = (EditText) findViewById(R.id.EditTextAmount);
        editIngredient = (EditText) findViewById(R.id.EditTextIngredient);
        editName = (EditText) findViewById(R.id.EditTextTitle);
        editDesc = (EditText) findViewById(R.id.EditTextDesc);
        //setListViewHeightBasedOnChildren(ingredientList);

        alarmNameList = new LinkedList<>();
        alarmTimeList = new LinkedList<>();
        textViewAlarms = (TextView) findViewById(R.id.TextViewAlarms);
        textViewAlarms.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        img = (ImageView) findViewById(R.id.ImageViewImg);

        img.setImageDrawable(getResources().getDrawable(R.drawable.recipe_logo));
        img.setMaxHeight(250);
        img.setMaxWidth(250);


        editId = getIntent().getIntExtra("id", -1);
        if (editId != -1) {
            Recipe editRecipe = myDBHandler.getRecipe(editId);
            loadRecipe(editRecipe);
            edit = true;
        }


    }

    private void loadRecipe(Recipe editRecipe) {
        editName.setText(editRecipe.getRecipename());
        editDesc.setText(editRecipe.getRecipredescription());

        if(!editRecipe.getRecipePath().equals(""))
        {
            imgPath = editRecipe.getRecipePath();
            img.setImageBitmap(decodeFile(editRecipe.getRecipePath()));
        }

        String[] ing = editRecipe.getIngredients();
        String[] ingA = editRecipe.getIngredientsAmount();

        for (int i = 0; i < ing.length; i++) {
            Ingredient ingredient = new Ingredient(ing[i],ingA[i]);
            adapter.add(ingredient);
        }

        String[] task = editRecipe.getTasks();
        int[] taskTime = editRecipe.getTasksTime();

        for (int i = 0; i < task.length; i++) {
            setUpAlarm(taskTime[i],task[i]);
        }


    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen

    }
    public void deleteIngredientOnClick(View v)
    {
        Ingredient ingredientToRemove = (Ingredient)v.getTag();
        adapter.remove(ingredientToRemove);
    }
//
//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }

    @Override
    public void onClick(View view) {
        if(view.getId() == buttonAddIngredient.getId())
        {
            String name = editIngredient.getText().toString();
            String amount = editAmount.getText().toString();

            adapter.add(new Ingredient(name,amount));

            editIngredient.setText("");
            editAmount.setText("");

        }
        if(view.getId() == buttonAddAlarm.getId())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            Context context = view.getContext();
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText alarmName = new EditText(context);
            alarmName.setHint("Tytuł alarmu");
            layout.addView(alarmName);

            final EditText alarmTime = new EditText(context);
            alarmTime.setHint("czas alarmu w minutach");
            alarmTime.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(alarmTime);


            alert.setView(layout);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String alarmTimeString;
                    String alarmString = alarmName.getText().toString();
                    alarmTimeString = alarmTime.getText().toString();

                    if(alarmTimeString.equals("")) {
                        alarmTimeString = "0";
                    }

                    int alarmTimeValue = Integer.parseInt(alarmTimeString);

                    if (alarmTimeValue < 0) {
                        alarmTimeValue = 1;
                    }
                    setUpAlarm(alarmTimeValue, alarmString);
                    // Do something with value!
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }
        if(view.getId() == buttonRemoveAlarm.getId())
        {
            removeAlarm();
        }
        if(view.getId() == buttonGalleryImg.getId())
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
        }
        if(view.getId() == buttonPhotoImg.getId()) {
            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
            startActivityForResult(intent, CAPTURE_IMAGE);
        }
        if(view.getId() == buttonSave.getId()) {

            Recipe newRecipe = getRecipe();
            if(edit) {
                myDBHandler.editRecipe(editId,newRecipe);
                edit = false;
                editId = -1;
            }
            else {
                myDBHandler.addRecipe(newRecipe);
            }
            Intent i = new Intent(this, RecipesList.class);
            startActivity(i);

        }

    }

    private Recipe getRecipe() {
        Recipe recipe;
        String name = editName.getText().toString();
        if(name.equals("")) { name = "default"; }

        String desc = editDesc.getText().toString();
        if(desc.equals("")) { desc = "default"; }



        String[] ingredientsArr = new String[ingredients.size()];
        String[] ingredientsAmountArr = new String[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientsAmountArr[i] = ingredients.get(i).getAmount();
            ingredientsArr[i] = ingredients.get(i).getName();
        }
        String[] taskName = new String[alarmNameList.size()];
        int[] taskTime = new int[alarmTimeList.size()];
        for (int i = 0; i < alarmNameList.size(); i++) {
            taskTime[i] = alarmTimeList.get(i)*60;
            taskName[i] = alarmNameList.get(i);
        }
        recipe = new Recipe(name, desc, imgPath, ingredientsArr, ingredientsAmountArr, taskName, taskTime);
        return recipe;
    }

    private void setUpAlarm(int alarmTimeValue, String alarmString) {
        alarmNameList.add(alarmString);
        alarmTimeList.add(alarmTimeValue);
        String current = textViewAlarms.getText().toString();
        current += "\n" + alarmString + " " + alarmTimeValue;
        textViewAlarms.setText(current);

    }

    private void removeAlarm() {
        if(!alarmNameList.isEmpty()) {
            alarmNameList.pop();
            alarmTimeList.pop();

        }
        String current = textViewAlarms.getText().toString();
        if(current.lastIndexOf("\n")!=-1) {
            current = current.substring(0,current.lastIndexOf("\n"));
        }
        textViewAlarms.setText(current);
    }

    private String imgPath = "";
   
    final private int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }


    public String getImagePath() {
        return imgPath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE) {
                imgPath = getAbsolutePath(data.getData());
                img.setImageBitmap(decodeFile(imgPath));
            } else if (requestCode == CAPTURE_IMAGE) {
                imgPath = getImagePath();
                img.setImageBitmap(decodeFile(imgPath));
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }


    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 300;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == SELECT_PICTURE) {
//                Bitmap bitmap = null;
//                Uri selectedImageUri = data.getData();
//                bitmap = decodeSampledBitmapFromUri(
//                        selectedImageUri,
//                        300, 300);
//
//                    if (bitmap == null) {
//                        Toast.makeText(getApplicationContext(), "the image data could not be decoded", Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        imgPath = getPath(selectedImageUri);
//                        System.out.println("Image Path : " + imgPath);
//                            img.setImageBitmap(bitmap);
//                    }
//                }
//            }
//        }


//    private String getPath(Uri contentUri) {
//        String res = null;
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if(cursor.moveToFirst()){;
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//    }
//
//    public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
//
//        Bitmap bm = null;
//
//        try{
//            // First decode with inJustDecodeBounds=true to check dimensions
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
//
//            // Calculate inSampleSize
//            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//            // Decode bitmap with inSampleSize set
//            options.inJustDecodeBounds = false;
//            bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
//        }
//
//        return bm;
//    }
//
//    public int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//            if (width > height) {
//                inSampleSize = Math.round((float)height / (float)reqHeight);
//            } else {
//                inSampleSize = Math.round((float)width / (float)reqWidth);
//            }
//        }
//        return inSampleSize;
//    }
}
