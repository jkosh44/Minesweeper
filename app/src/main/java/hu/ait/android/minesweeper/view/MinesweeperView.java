package hu.ait.android.minesweeper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.minesweeper.MainActivity;
import hu.ait.android.minesweeper.R;
import hu.ait.android.minesweeper.model.MinesweeperModel;

/**
 * Created by joe on 9/17/15.
 */
public class MinesweeperView extends View {

    private Paint paintBg;
    private Paint paintLine;
    private Paint paintRed;
    private Paint paintNumber;
    private int horizontalOffSet;
    private int verticalOffSet;

    //private Bitmap backGround;


    public MinesweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setColor(Color.BLACK);
        paintBg.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintRed = new Paint();
        paintRed.setColor(Color.RED);
        paintRed.setStyle(Paint.Style.FILL);
        paintRed.setStrokeWidth(5);

        paintNumber = new Paint();
        paintNumber.setColor(Color.WHITE);
        paintNumber.setStyle(Paint.Style.STROKE);


        // backGround = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paintNumber.setTextSize(getWidth() / 9);
        horizontalOffSet = getWidth() / 30;
        verticalOffSet = getHeight() / 25;
    }

    private short getFieldContent(int x, int y) {
        return MinesweeperModel.getInstance().getFieldContent(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);

        //canvas.drawBitmap(backGround, 0, 0, null);


        drawGameArea(canvas);

        drawPlayers(canvas);
    }

    private void drawPlayers(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (getFieldContent(i, j) != MinesweeperModel.BOMB && MinesweeperModel.getInstance().getClickedContent(i, j) == true) {
                    //draw a the correct number here
                    drawNumber(canvas, i, j);
                }
                if (getFieldContent(i, j) == MinesweeperModel.BOMB && MinesweeperModel.getInstance().getGameOver()) {
                    //draw a bomb here
                    drawCircle(canvas, i, j);
                }
                if (MinesweeperModel.getInstance().getFlaggedContent(i, j)) {
                    //draw a flag here
                    drawCross(canvas, i, j);
                }

            }
        }
    }

    private void drawCross(Canvas canvas, int i, int j) {
        canvas.drawLine(i * getWidth() / 5, j * getHeight() / 5,
                (i + 1) * getWidth() / 5,
                (j + 1) * getHeight() / 5, paintLine);

        canvas.drawLine((i + 1) * getWidth() / 5, j * getHeight() / 5,
                i * getWidth() / 5, (j + 1) * getHeight() / 5, paintLine);
    }

    private void drawCircle(Canvas canvas, int i, int j) {
        float centerX = i * getWidth() / 5 + getWidth() / 10;
        float centerY = j * getHeight() / 5 + getHeight() / 10;
        int radius = getHeight() / 10 - 3;

        canvas.drawCircle(centerX, centerY, radius, paintRed);
    }

    private void drawNumber(Canvas canvas, int i, int j) {
        canvas.drawText(Short.toString(getFieldContent(i, j)), i * getWidth() / 5 + getWidth() / 10 - horizontalOffSet, j * getHeight() / 5 + getHeight() / 10 + verticalOffSet, paintNumber);
    }


    private void drawGameArea(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);

        drawHorizontalLines(canvas);


        drawVerticalLines(canvas);

    }

    private void drawVerticalLines(Canvas canvas) {
        canvas.drawLine(getWidth() / 5, 0, getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 5, 0, 2 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(3 * getWidth() / 5, 0, 3 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(4 * getWidth() / 5, 0, 4 * getWidth() / 5, getHeight(),
                paintLine);
    }

    private void drawHorizontalLines(Canvas canvas) {
        canvas.drawLine(0, getHeight() / 5, getWidth(), getHeight() / 5,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 5, getWidth(),
                2 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 3 * getHeight() / 5, getWidth(),
                3 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 4 * getHeight() / 5, getWidth(),
                4 * getHeight() / 5, paintLine);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!MinesweeperModel.getInstance().getGameOver()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int tX = ((int) event.getX()) / (getWidth() / 5);
                int tY = ((int) event.getY()) / (getHeight() / 5);

                if (!MinesweeperModel.getInstance().getFlagStatus()) {
                    handlePlayerMove(tX, tY);
                } else {
                    if(MinesweeperModel.getInstance().getFlaggedContent(tX, tY) == false) {
                        handleFlagPlayerMove(tX, tY);
                    }
                }
                invalidate();

            }
        }

        return super.onTouchEvent(event);
    }

    private void handleFlagPlayerMove(int tX, int tY) {
        if (tX < 5 && tY < 5) {
            if (MinesweeperModel.getInstance().getFieldContent(tX, tY) != MinesweeperModel.BOMB) {
                MinesweeperModel.getInstance().setGameOver();
                MinesweeperModel.getInstance().setFlaggedContent(tX, tY);
                ((MainActivity) getContext()).showSnackBarWithDelete(
                        getContext().getString(R.string.flagGameOver));
            } else {
                MinesweeperModel.getInstance().setFlaggedContent(tX, tY);
                MinesweeperModel.getInstance().increaseCorrectFlags();
                MinesweeperModel.getInstance().decideWinner();
                if (MinesweeperModel.getInstance().getGameOver()) {
                    ((MainActivity) getContext()).showSnackBarWithDelete(
                            getContext().getString(R.string.game_won));
                }
            }
        }
    }

    private void handlePlayerMove(int tX, int tY) {
        if (tX < 5 && tY < 5) {
            if (getFieldContent(tX, tY) == MinesweeperModel.BOMB) {
                MinesweeperModel.getInstance().setGameOver();
                ((MainActivity) getContext()).showSnackBarWithDelete(
                        getContext().getString(R.string.game_over));
            } else if (MinesweeperModel.getInstance().getClickedContent(tX, tY) == false) {
                MinesweeperModel.getInstance().setClickedContent(tX, tY);
                if (MinesweeperModel.getInstance().getFieldContent(tX, tY) == MinesweeperModel.EMPTY) {
                    MinesweeperModel.getInstance().dealWithZeros(tX, tY);
                }
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    public void clearGameArea() {
        MinesweeperModel.getInstance().resetModel();
        invalidate();
    }
}

