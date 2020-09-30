package com.zarra.lionortiger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    enum Player{
        ONE,
        TWO
    }
    enum Status{
        CONTINUE,
        WIN_1,
        WIN_2,
        DRAW
    }

    int[][] game_matrix=new int[3][3];

    private boolean game_is_end=false;
    Player currentPlayer=Player.ONE;

    TextView currentPlayerLabel;
    Button btn_reset;
    GridLayout image_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentPlayerLabel=findViewById(R.id.curPlayerLabel);
        setPlayerLabel();
        image_container=findViewById(R.id.image_container);
        btn_reset=findViewById(R.id.btn_reset);
        btn_reset.setVisibility(View.INVISIBLE);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_matrix=new int[3][3];
                game_is_end=false;
                currentPlayer=Player.ONE;
                setPlayerLabel();
                clear_image_views(image_container);
                btn_reset.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void clear_image_views(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            ImageView child = (ImageView) parent.getChildAt(i);
            child.setImageDrawable(null);
            }
        }

    private void mark_matrix(ImageView imageView){
        if(currentPlayer==Player.ONE){
        switch (imageView.getId()){
            case R.id.imageView1:
                game_matrix[0][0]=1;
                break;
            case R.id.imageView2:
                game_matrix[0][1]=1;
                break;
            case R.id.imageView3:
                game_matrix[0][2]=1;
                break;
            case R.id.imageView4:
                game_matrix[1][0]=1;
                break;
            case R.id.imageView5:
                game_matrix[1][1]=1;
                break;
            case R.id.imageView6:
                game_matrix[1][2]=1;
                break;
            case R.id.imageView7:
                game_matrix[2][0]=1;
                break;
            case R.id.imageView8:
                game_matrix[2][1]=1;
                break;
            case R.id.imageView9:
                game_matrix[2][2]=1;
                break;
        }
        }
        else
        {
            switch (imageView.getId()){
                case R.id.imageView1:
                    game_matrix[0][0]=2;
                    break;
                case R.id.imageView2:
                    game_matrix[0][1]=2;
                    break;
                case R.id.imageView3:
                    game_matrix[0][2]=2;
                    break;
                case R.id.imageView4:
                    game_matrix[1][0]=2;
                    break;
                case R.id.imageView5:
                    game_matrix[1][1]=2;
                    break;
                case R.id.imageView6:
                    game_matrix[1][2]=2;
                    break;
                case R.id.imageView7:
                    game_matrix[2][0]=2;
                    break;
                case R.id.imageView8:
                    game_matrix[2][1]=2;
                    break;
                case R.id.imageView9:
                    game_matrix[2][2]=2;
                    break;
            }
        }
    }


    private void setPlayerLabel(){
        if (currentPlayer==Player.ONE){
            currentPlayerLabel.setText("Current player #"+1);
        }
        else if (currentPlayer==Player.TWO){
            currentPlayerLabel.setText("Current player #"+2);
        }
    }

    public void ImageViewIsTapped(View imageView){
        ImageView tappedImageView=(ImageView) imageView;

        if (tappedImageView.getDrawable()==null && !game_is_end) {
            tappedImageView.setTranslationX(-2000);

            if (currentPlayer == Player.ONE) {
                tappedImageView.setImageResource(R.drawable.lion);
                mark_matrix(tappedImageView);
                Status status=check_for_win();
                if (status==Status.CONTINUE){
                currentPlayer = Player.TWO;
                setPlayerLabel();
                }
                else{
                    game_is_end=true;
                    setLabelEnd(status);}
            } else if (currentPlayer == Player.TWO) {
                tappedImageView.setImageResource(R.drawable.tiger);
                mark_matrix(tappedImageView);
                Status status=check_for_win();
                if (status==Status.CONTINUE){
                    currentPlayer = Player.ONE;
                    setPlayerLabel();
                }
                else{
                    game_is_end=true;
                    setLabelEnd(status);}
            }

            tappedImageView.animate().translationXBy(2000).alpha(1).rotation(3600).setDuration(1000);

        }
    }

    private void setLabelEnd(Status status){
        switch (status){
            case WIN_1:
                currentPlayerLabel.setText("Player 1 Won!");
                break;
            case WIN_2:
                currentPlayerLabel.setText("Player 2 Won!");
                break;
            case DRAW:
                currentPlayerLabel.setText("Draw!");
                break;
        }
        btn_reset.setVisibility(View.VISIBLE);
    }

    private Status check_for_win(){
        if (
            // horizontal check
            (game_matrix[0][0]==1 && game_matrix[0][1]==1 && game_matrix[0][2]==1)
            || (game_matrix[1][0]==1 && game_matrix[1][1]==1 && game_matrix[1][2]==1)
            || (game_matrix[2][0]==1 && game_matrix[2][1]==1 && game_matrix[2][2]==1)
            //vertical check
            || (game_matrix[0][0]==1 && game_matrix[1][0]==1 && game_matrix[2][0]==1)
            || (game_matrix[0][1]==1 && game_matrix[1][1]==1 && game_matrix[2][1]==1)
            || (game_matrix[0][2]==1 && game_matrix[1][2]==1 && game_matrix[2][2]==1)
            //diagonal check
            || (game_matrix[0][0]==1 && game_matrix[1][1]==1 && game_matrix[2][2]==1)
            || (game_matrix[0][2]==1 && game_matrix[1][1]==1 && game_matrix[2][0]==1)
        ) return Status.WIN_1;
        else if (
                // horizontal check
                (game_matrix[0][0]==2 && game_matrix[0][1]==2 && game_matrix[0][2]==2)
                || (game_matrix[1][0]==2 && game_matrix[1][1]==2 && game_matrix[1][2]==2)
                || (game_matrix[2][0]==2 && game_matrix[2][1]==2 && game_matrix[2][2]==2)
                //vertical check
                || (game_matrix[0][0]==2 && game_matrix[1][0]==2 && game_matrix[2][0]==2)
                || (game_matrix[0][1]==2 && game_matrix[1][1]==2 && game_matrix[2][1]==2)
                || (game_matrix[0][2]==2 && game_matrix[1][2]==2 && game_matrix[2][2]==2)
                //diagonal check
                || (game_matrix[0][0]==2 && game_matrix[1][1]==2 && game_matrix[2][2]==2)
                || (game_matrix[0][2]==2 && game_matrix[1][1]==2 && game_matrix[2][0]==2)
        ) return Status.WIN_2;

        else if (check_for_full())
                return Status.DRAW;
        else return Status.CONTINUE;
    }

    private boolean check_for_full(){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(game_matrix[i][j]==0)
                    return false;
            }
        }
        return true;
    }

}