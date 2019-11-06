package com.example.nasande;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckSms {

    private static final String TAG = CheckSms.class.getSimpleName();
    private static final String TIGO_CPL = "Vous avez recu (.*?) (.*?) de (24389[0-9]{7}) - (.*?), (.*?). Nouveau solde: (.*?) (.*?).Ref: (.*?).";


    public CheckSms() {
    }
    /*
    Les methodes pour gérer les sms de MTN MM
     */

    public String getNumberMtn(String body) {
        //Recupère le numéro Mtn qui a effectué la transaction
        Pattern trouve_num = Pattern.compile("0[456]{1}[0-9]{7}");
        Matcher num = trouve_num.matcher(body);

        if (num.find()) {
            // currentMessage.getDisplayOriginatingAddress();

            String senderAddress = num.group(0);
            Log.d(TAG, "Prends MTN " + senderAddress );
            return senderAddress;
        }
        else{
            Log.e(TAG, "Erreur sur le numero mtn");
            return "Something went wrong numero mtn";
        }
    }

    public String getAmountMTN(String body){

        Pattern pattern = Pattern.compile("recu(.*?)xaf");
        Matcher match = pattern.matcher(body);

        if(match.find()){
            String montant = match.group(1);
            Log.d(TAG, "Montant " + montant );
            return montant;
        }
        else {
            Log.e(TAG, "Erreur sur le montant ");
            return "Something went wrong montant mtn";
        }
    }

}

