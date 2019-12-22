package com.example.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Graphics {
    private Drawable drawable;   //The image we are going to draw
    private int posX, posY;   //Central position
    private int width, heigth;     //Image dimensions
    private double incX, incY;   //Advance speed
    private int angle, rotation;//Rotation angle and speed
    private int collisionRadius;   //Determinate collision
   // private int previousX, previousY;   //Previous position
   // private int radiusInval;   //radius used in invalidate()
    private View view;   //Used in view.invalidate()
    public static final int MAX_SPEED= 20;

    /*
    private double posX, posY;   //Position
    private double incX, incY;   //Advance speed
    private int angle, rotation;//Rotation angle and speed
    private int width, heigth;     //Image dimensions
    private int collisionRadio;   //Para determinar colisión
    //Where whe draw the graphics (used in view.ivalidate)
    private View view;
    // To determine the space to delete (view.ivalidate)
    public static final int MAX_SPEED= 20;
     */

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public double getIncX() {
        return incX;
    }

    public void setIncX(double incX) {
        this.incX = incX;
    }

    public double getIncY() {
        return incY;
    }

    public void setIncY(double incY) {
        this.incY = incY;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getCollisionRadius() {
        return collisionRadius;
    }

    public void setCollisionRadius(int collisionRadius) {
        this.collisionRadius = collisionRadius;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Graphics(View view, Drawable drawable){
        this.view = view;
        this.drawable = drawable;
        width = drawable.getIntrinsicWidth();
        heigth = drawable.getIntrinsicHeight();
        collisionRadius = (width+heigth)/4;
        //radiusInval = (int) Math.hypot(width/2,heigth/2);
    }

    public void drawGraphics(Canvas canvas){
        canvas.save();
        int x = (int) (posX+width/2);
        int y = (int) (posY+heigth/2);
        canvas.rotate((float)rotation,(float)x,(float)y);
        drawable.setBounds((int)posX, (int)posY, (int)posX+width, (int)posY+heigth);
        drawable.draw(canvas);
        canvas.restore();
        int rInval = (int) (Math.hypot(width, heigth)/2 + MAX_SPEED);
        view.invalidate(x-rInval, y-rInval, x+rInval, y+rInval);
        /* view.invalidate(cenX-radiusInval, cenY-radiusInval,
                cenX+radiusInval, cenY+radiusInval);
        view.invalidate(previousX -radiusInval, previousY-radiusInval,
                previousX+radiusInval, previousY+radiusInval);
        previousX= cenX;
        previousY= cenY;
         */
    }
    public void advancePos(double factor){
        posX += incX * factor;
        posY += incY * factor;
        // If we exit the screen, fix position
        if (posX<-width/2) {
            posX = view.getWidth() - width/2;
        }
        if (posX > view.getWidth() - width/2) {
            posX=-width/2;
        }
        if(posY < -heigth/2) {
            posY = view.getHeight() - heigth/2;
        }
        if(posY > view.getHeight() - heigth/2) {
            posY = -heigth/2;
        }
        angle += rotation * factor;
    }
    public double distance(Graphics g) {
        return Math.hypot(posX-g.posX, posY-g.posY);
    }
    public boolean checkCollision(Graphics g) {
        return (distance(g) < collisionRadius + g.collisionRadius);
    }
}
