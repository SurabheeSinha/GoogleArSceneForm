package com.surabheesinha.googlearsceneform;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArFragment arfragment;
    private ModelRenderable bearRederable,
                            carRenderable;

    ImageView bear,cat;
    View arrayView[];
    ViewRenderable name_animal;

    int selected = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arfragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);
        bear = (ImageView)findViewById(R.id.imageBear);
        cat = (ImageView)findViewById(R.id.imageCat);

        setArrayView();
        setClickListener();
        setupModel();

        arfragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arfragment.getArSceneView().getScene());
                    createModel(anchorNode,selected);
            }
        });



    }
    public void createModel (AnchorNode anchorNode, int selected){
        if(selected==1){
            TransformableNode bear = new TransformableNode(arfragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(bearRederable);
            bear.select();
            addName(anchorNode,bear,"Bear");

        }
        if(selected==2){
            TransformableNode cat = new TransformableNode(arfragment.getTransformationSystem());
            cat.setParent(anchorNode);
            cat.setRenderable(carRenderable);
            cat.select();
            addName(anchorNode,cat,"Cat");

        }

    }

    private void addName(AnchorNode anchorNode, TransformableNode model, String name){
        TransformableNode nameView = new TransformableNode(arfragment.getTransformationSystem());
        nameView.setLocalPosition(new Vector3(0f,model.getLocalPosition().y+0.5f,0));
        nameView.setParent(anchorNode);
        nameView.setRenderable(bearRederable);
        nameView.select();

        TextView txt_name = (TextView)name_animal.getView();
        txt_name.setText(name);

        txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                anchorNode.setParent(null);

            }
        });

    }


    private void setupModel(){

        ViewRenderable.builder()
                .setView(this,R.layout.name_animal)
                .build()
                .thenAccept(renderable->name_animal=renderable);


        ModelRenderable.builder()
                .setSource(this,R.raw.bear)
                .build().thenAccept(renderable->bearRederable=renderable)
                .exceptionally (
                    thorwable -> {
                        Toast.makeText(this, "Unable to load bear model", Toast.LENGTH_SHORT).show();

                    return null;

                    });


        ModelRenderable.builder()
                .setSource(this,R.drawable.cat)
                .build().thenAccept(renderable->carRenderable=renderable)
                .exceptionally (
                        thorwable -> {
                            Toast.makeText(this, "Unable to load cat model", Toast.LENGTH_SHORT).show();

                            return null;
                        });

    }


    private void setArrayView(){
        arrayView= new View[]{bear,cat};

    }
    private void setClickListener(){
        for(int i =0;i<arrayView.length;i++){
            arrayView[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imageBear){
            selected=1;
            setBackground(v.getId());
        }
        else if (v.getId()==R.id.imageCat){
            selected=2;
        }

    }

    private void setBackground(int id){
        for(int i=0;i<arrayView.length;i++){
            if(arrayView[i].getId() == id)
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"));
            else
                arrayView[i].setBackgroundColor(Color.TRANSPARENT);

        }
    }
}
