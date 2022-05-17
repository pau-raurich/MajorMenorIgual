package cat.dam.pau.majormenorigual_avanat;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static ImageView imageViews [][] = new ImageView[4][4]; //Matriu on es guardaran tots les imatges d'animals
    public static ArrayList<Integer> randomImgPosition = new ArrayList<>();
    public static int leftCount = 8; //Comptador dels animals de l'esquerra
    public static int rightCount = 8; //Comptador dels animals de la dreta
    public static int points = 0; //Comptadaor dels punts del jugador

    //Declaració de components de l'aplicació
    public ImageView iv_circle;
    public LinearLayout l_linear_buttons;
    public ImageButton ib_bigger;
    public ImageButton ib_lesser;
    public ImageButton ib_equals;
    public ImageButton ib_restart;
    public TextView tv_points;
    public TextView tv_lostPoints;
    public FrameLayout fl_lostGame;


    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Desa l’estat actual del joc de l’usuari
        savedInstanceState.putInt("points", points);
        savedInstanceState.putIntegerArrayList("randomImgPosition", randomImgPosition);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onResume(){
        super.onResume();

        tv_points = (TextView) findViewById(R.id.punts);
        tv_points.setText("Punts:" + String.valueOf(points));

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MajorMenorIgual");

        if (savedInstanceState != null) {
            // Restaura els valors que s’han desat de l’estat
            points = savedInstanceState.getInt("points");

        }else{
            createAddImagesArray();
            iv_circle = (ImageView) findViewById(R.id.cercle);
            l_linear_buttons = (LinearLayout) findViewById(R.id.opcions);
            createImageButtons();
            tv_points = (TextView) findViewById(R.id.punts);
            tv_lostPoints = (TextView) findViewById(R.id.punts_partida_perduda);
            fl_lostGame = (FrameLayout) findViewById(R.id.has_perdut);
            setRandomVisible();

            ib_bigger.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    //Canvi d'imatge del cercle a la seleccionada
                    iv_circle.setImageResource(R.drawable.mes_gran);

                    //Comprovació de si s'ha guanyat o no
                    winnerLoser("bigger");
                }
            });

            ib_lesser.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    //Canvi d'imatge del cercle a la seleccionada
                    iv_circle.setImageResource(R.drawable.mes_petit);

                    //Comprovació de si s'ha guanyat o no
                    winnerLoser("lesser");
                }
            });

            ib_equals.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    //Canvi d'imatge del cercle a la seleccionada
                    iv_circle.setImageResource(R.drawable.iguals);

                    //Comprovació de si s'ha guanyat o no
                    winnerLoser("equals");
                }
            });

            ib_restart.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    fl_lostGame.setVisibility(View.GONE); //Fem fora el 'FrameLayout' de la derrota
                    points = 0; //Reinici de punts
                    tv_points.setText("Punts: "+Integer.toString(points)); //Restablim el text dels punts
                    setAllImagesVisible(); //Cridem la funció de renici de la visibilitat de les imatges dels animals
                    iv_circle.setImageResource(R.drawable.cercle); //Tornem a posar el cercle
                    l_linear_buttons.setVisibility(View.VISIBLE); //Tornem a fer visible layout dels botons
                    setRandomVisible(); //I randomitzem les imatges dels animals
                }
            });
        }



    }

    //Funció que posa en invisible una quanitat d'imatges d'animals de forma aleatoria
    public static void setRandomVisible(){
        Random r = new Random();
        for(int j=0;j<imageViews.length;j++){
            int x = r.nextInt(4);
            if(j==0 || j==1){
                leftCount -= x;
            }else{
                rightCount -= x;
            }
            for(int i=0;i<x;i++){
                int a = 0;
                boolean setInvisible = false;
                do{
                    a = r.nextInt(4);
                    if(imageViews[j][a].getVisibility() == View.VISIBLE){
                        imageViews[j][a].setVisibility(View.INVISIBLE);
                        randomImgPosition.add(a);
                        setInvisible = true; //I posem el valor a 'True'
                    }
                }while(!setInvisible && imageViews[j][a].getVisibility() == View.INVISIBLE);
            }
        }
    }


    //Funció que posa en visible totes les imatges d'animals
    public static void setAllImagesVisible(){
        leftCount = 8;
        rightCount = 8;

        for(int i=0;i<imageViews.length;i++){
            for(int j=0;j<imageViews.length;j++){
                imageViews[i][j].setVisibility(View.VISIBLE);
            }
        }
    }

    //Funció que crea totes les 'ImageView' dels animals i les afegeix a la matriu 'imageViews'
    public void createAddImagesArray(){
        for(int i=0;i<imageViews.length;i++){
            String imgID = "img_top_left_1";
            if(i==1){
                imgID = "img_bottom_left_1" ;
            }
            if(i==2){
                imgID = "img_top_right_1" ;
            }
            if(i==3){
                imgID = "img_bottom_right_1";
            }

            for(int j=0;j<imageViews.length;j++){
                String imgIdSubString = imgID.substring(0,imgID.length()-1);
                imgID = imgIdSubString + String.valueOf(j+1);
                //Agafem el valor amb Int del recurs del 'ImageView'
                int resID = getResources().getIdentifier(imgID, "id", this.getPackageName());
                ImageView image = (ImageView) findViewById(resID);
                imageViews[i][j] = image;
            }
        }
    }

    //Funció de creació dels 'ImageButons'
    public void createImageButtons(){
        ib_bigger = (ImageButton) findViewById(R.id.mes_gran);
        ib_lesser = (ImageButton) findViewById(R.id.mes_petit);
        ib_equals = (ImageButton) findViewById(R.id.iguals);
        ib_restart = (ImageButton) findViewById(R.id.tornar_principi);
    }

    //Funció que donat un String (bigger,lesser,equals) determina si la resposta donada és correcte o no
    public void winnerLoser(String choice){
        if(choice.equals("bigger")){
            if(leftCount>rightCount){
                System.out.println("Has guanyat!!");
                points++;
                tv_points.setText("Punts: "+Integer.toString(points));
                setAllImagesVisible();
                setRandomVisible();
            }else{
                System.out.println("Has perdut :(");
                l_linear_buttons.setVisibility(View.INVISIBLE);
                fl_lostGame.setVisibility(View.VISIBLE);
                tv_lostPoints.setText("La teva pontuació és: "+Integer.toString(points));
            }
        }else if (choice.equals("lesser")){
            if(leftCount<rightCount){
                System.out.println("Has guanyat!!");
                points++;
                tv_points.setText("Punts: "+Integer.toString(points));
                setAllImagesVisible();
                setRandomVisible();
            }else{
                System.out.println("Has perdut :(");
                l_linear_buttons.setVisibility(View.INVISIBLE);
                fl_lostGame.setVisibility(View.VISIBLE);
                tv_lostPoints.setText("La teva pontuació és: "+Integer.toString(points));
            }
        }else{
            if(leftCount==rightCount){
                System.out.println("Has guanyat!!");
                points++;
                tv_points.setText("Punts: "+Integer.toString(points));
                setAllImagesVisible();
                setRandomVisible();
            }else{//En cas contrari es mostra la part del layout de la derrota
                System.out.println("Has perdut :(");

                l_linear_buttons.setVisibility(View.INVISIBLE);
                fl_lostGame.setVisibility(View.VISIBLE);

                tv_lostPoints.setText("La teva pontuació és: "+Integer.toString(points));

            }
        }
    }
}

