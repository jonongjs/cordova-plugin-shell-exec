package org.apache.cordova.plugin;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShellExec extends CordovaPlugin {

@Override
public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if (action.equals("exec")) {
                final String cmd = (String) args.get(0);
                cordova.getThreadPool().execute(new Runnable() {
                        public void run() {
                                Process p;
                                StringBuffer output = new StringBuffer();
                                int exitStatus = 100;
                                try {
                                        p = Runtime.getRuntime().exec(cmd);
                                        BufferedReader reader = new BufferedReader( new InputStreamReader(p.getInputStream()) );
                                        String line = "";
                                        while ((line = reader.readLine()) != null) {
                                                output.append(line + "\n");
                                                //p.waitFor();
                                        }
                                        //exitStatus = p.exitValue();
                                        exitStatus = p.waitFor();
                                }
                                catch (IOException e) {
                                        e.printStackTrace();
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }

                                try {
                                        JSONObject json = new JSONObject();
                                        json.put("exitStatus", exitStatus);
                                        json.put("output", output.toString());
                                        callbackContext.success(json);
                                }
                                catch(JSONException e){
                                        e.printStackTrace();
                                        callbackContext.success();
                                }
                        }
                });
                return true;
        }
        return false;
}

}
