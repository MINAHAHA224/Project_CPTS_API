package vn.javaweb.ComputerShop.utils;

public class FaceIdUtils {

    public static  float[] toVector ( String embeddedCode ){
        String [] vectorString = embeddedCode.split(",");

        float [] vector = new float[vectorString.length];

        for ( int i = 0 ; i < vectorString.length ; i++ ){
            vector[i] = Float.parseFloat(vectorString[i]);
        }
        return vector;
    }


    public static double euclideanDistance ( float [] vector1 , float [] vector2 ){
        double sum = 0.0;
        for ( int i = 0 ; i < vector1.length ; i++ ){
            double diff = vector1[i] - vector2[i];
            sum += ( diff * diff) ;
        }
        return Math.sqrt(sum);
    }


}
