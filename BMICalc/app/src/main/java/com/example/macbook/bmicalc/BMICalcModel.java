package com.example.macbook.bmicalc;

import com.google.gson.Gson;

/**
 * Model - Generates a BMI for a given height in inches and weight in pounds
 */
public class BMICalcModel
{
    private double height, weight;

    public BMICalcModel()
    {
        this.height = 0;
        this.weight = 0;
    }

    public BMICalcModel(double height, double weight)
    {
        this.weight = checkAndGetGreaterThanZero(weight, "Weight");
        this.height = checkAndGetGreaterThanZero(height, "Height");
    }

    public void setHeight (double height)
    {
        this.height = checkAndGetGreaterThanZero(height, "Height");
    }

    public void setWeight (double weight)
    {
       this.weight = checkAndGetGreaterThanZero(weight, "Weight");
    }

    private double checkAndGetGreaterThanZero (double value, String description)
    {
        if (value >0)
            return value;
        else
            throw new IllegalArgumentException (description + " must be greater than zero.");
    }

    public double getHeight ()
    {
        return height;
    }

    public double getWeight ()
    {
        return weight;
    }

    public double getBMI()
    {
        if (height > 0 && weight > 0) {
            return 703 * (weight / (height * height));
        }
        else
            throw new IllegalStateException ("Cannot get BMI before setting weight and height.");
    }

    public String getBMIGroup()
    {
        double currentBMI = getBMI();
        String BMIGroup;
        if(currentBMI < 18.5) {
            BMIGroup = "Underweight";
        }
        else if(currentBMI < 25) {
            BMIGroup = "Healthy";
        }
        else if(currentBMI < 30) {
            BMIGroup = "Overweight";
        }
        else {
            BMIGroup = "Obese";
        }
        return  BMIGroup;
    }

    public static BMICalcModel getObjectFromJSONString (String json)
    {
        Gson gson = new Gson ();
        return gson.fromJson (json, BMICalcModel.class);
    }

    public static String getJSONStringFromObject (BMICalcModel object)
    {
        Gson gson = new Gson ();
        return gson.toJson (object);
    }
}
