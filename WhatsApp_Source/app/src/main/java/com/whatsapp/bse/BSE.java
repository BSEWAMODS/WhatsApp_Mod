package com.whatsapp.bse;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import android.os.Build;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import java.security.SecureRandom;
import android.os.Environment;

public class BSE {
	static Context ctx;
	public static String installationStr = "com.android.vending";
	public static String packageName = "com.whatsapp";

    	public static void onCreate(Application application) {
              ctx=application.getApplicationContext();
    	}
	
	static SharedPreferences getSharedPreferences(Context contexto) {
              return contexto.getSharedPreferences(contexto.getPackageName() + "_privacy", 0);
    	}
	
	static String getStringPriv(String cadena) {
              return getSharedPreferences(ctx).getString(cadena, "");
        }
	
	static void setStringPriv(String str, String str2) {
              Editor editp = getSharedPreferences(ctx).edit();
              editp.putString(str, str2);
              editp.apply();
        }
	
	public static String getAndroidID(String str) {
        try {
            String android_id = getStringPriv("android_id1");
            if (!android_id.equals("")) {
                return android_id;
            }
            android_id = Long.toHexString(new SecureRandom().nextLong());
            setStringPriv("android_id1", android_id);
            return android_id;
        } catch (Exception e) {
            return str;
        }
    }

    static void showToast(String textoToast){
      Toast.makeText(ctx, textoToast, 0).show();
    }
	
	public static boolean enableFeatures(int valor){
		if(valor == 1825 || valor == 1863 || valor == 2434 || valor == 3140 || valor == 3223 || valor == 3289 || valor == 3354 || valor == 3792 || valor == 3931 || valor == 3935 || valor == 4023 || valor == 4268 || valor == 4460 || valor == 4168 || valor == 3844){
			return true;
		}
		return false;
	}

    public static void downloadFilesViewOnce(File archivo_entrada) {
        try {
            if (isSaved(archivo_entrada)) {
                showToast("Photo saved to gallery");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static boolean isSaved(File archivoEntradaFile){
		synchronized (BSE.class) {
            try {
				File salidaRutaFile = filePath();
				if(salidaRutaFile != null){
					if(new File(salidaRutaFile, archivoEntradaFile.getName()).exists()){
						showToast("Already saved!");
						return false;
					}
					copyFile(archivoEntradaFile, salidaRutaFile);
					ctx.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(salidaRutaFile, archivoEntradaFile.getName()))));
					return true;
				}
				return false;
            } catch (Exception e) {
                return false;
            }
        }
    }
    
	static void copyFile(File archivoEntradaFile, File salidaRutaFile) {
        try {
            FileInputStream fis = new FileInputStream(archivoEntradaFile);
			File archivoSalidaFile = new File(salidaRutaFile, archivoEntradaFile.getName());
			FileOutputStream fos = new FileOutputStream(archivoSalidaFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = fis.read(buf)) > 0) {
               fos.write(buf, 0, len);
            }
			fis.close();
			fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    static File filePath() {
        try {
			String salidaRuta=null;
            if(Build.VERSION.SDK_INT >=30){
				salidaRuta=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android" + File.separator + "media" + File.separator + "com.whatsapp" + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp ViewOnce" + File.separator;
			}else{
				salidaRuta=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp ViewOnce" + File.separator;
			}
			File salidaRutaFile = new File(salidaRuta);
			if(!salidaRutaFile.exists()){
				salidaRutaFile.mkdirs();
			}
			return salidaRutaFile;
        } catch (Exception e) {
			return null;
        }
    }
}
