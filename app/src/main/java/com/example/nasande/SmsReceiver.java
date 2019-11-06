package com.example.nasande;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    private static SmsMessage currentMessage = null;
    private static String message = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();


        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            for (Object aPdusObj : pdusObj) {
                currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
            }

        }
        message = currentMessage.getDisplayMessageBody().toLowerCase();
        String reseau = currentMessage.getDisplayOriginatingAddress();
        SmsRequest smsRequest = new SmsRequest();


        Log.d(TAG, "Message recu");



        if (message.contains("recu") && reseau.equalsIgnoreCase(MainActivity.getNetwork()) ){

            Log.d(TAG, "Dans le bloc mtn  ");
            Toast.makeText(context, "ORIGIN "+ MainActivity.getNetwork(), Toast.LENGTH_SHORT).show();


            CheckSms checkSms = new CheckSms();
            String sender = checkSms.getNumberMtn(message).replace("24206","06");
            String devise = "FCFA";
            String montant = checkSms.getAmountMTN(message);
            String trans_id = "0000";
            String fname = "Not defined";
            String lname = "Not defined";
            //db.addMessage(message,trans_id,devise,montant,reseau,sender,fname,lname);
            String notif = "Transfert du +242" + sender + " de " + montant + " XFA";
            try {
                MainActivity.getInstace().updateViews(notif);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SmsManager.getDefault().sendTextMessage("+242"+sender,null, "Merci: Transfert recu", null, null);

            //smsRequest.moneySms(message, sender, reseau, devise, montant,trans_id, context);
            //db.addMessage("Message body","Transaction Id","FCFA","Montant","Reseau","Mysel","Bien","KIt");

        }
    }


}
