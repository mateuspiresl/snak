package com.forbait.games.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mateu on 12/06/2016.
 */
public class Utils {

    public static String getAddress()
    {
        String ip;
        try {
            URL ipChecker = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(ipChecker.openStream()));
            ip = in.readLine();
            in.close();

            return ip;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
