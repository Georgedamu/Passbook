/*
* Copyright 2014 Qianqian Zhu <zhuqianqian.299@gmail.com> All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.z299studio.pb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Application{
    
    private static final String DATA_FILE = "data";
    
    private static Application __instance;
    
    public SharedPreferences mSP;
    
    public static class Options {
    	public static int mSync;
        public static int mTheme;
        public static boolean mTour;  
    }
    
    public static class FileHeader {
        public int version;  // 1 byte
        public int iterationCount; // 1 byte
        public int keyLength;  //1 byte
        public int ivLength;  // 1 byte
        public int revision;  // 10 bytes
        public int size;
        public boolean valid;
        public static final int HEADER_SIZE = 16;
        public static final int RESERVED = 10;
        
        public static FileHeader parse(byte[] buffer) {
            FileHeader fh = new FileHeader();
            fh.valid = false;
            if(buffer!=null) {
                if(buffer[0] == 0x50 && buffer[1] == 0x42) {
                    int i = 2;
                    fh.valid = true;
                    fh.version = buffer[i++];
                    fh.iterationCount = 100 * (buffer[i++]);
                    fh.keyLength = buffer[i++];
                    fh.ivLength =  buffer[i++];
                    fh.revision = Integer.parseInt(new String(buffer, i, RESERVED).trim());
                    i += RESERVED;
                    fh.size = i;                
                }
            }
            return fh;
        }
        
        public static byte[] build(int version, int count, int keyLength, int ivLength, int revision) {
            byte[] header = new byte[HEADER_SIZE];
            int i = 0;
            header[i++] = 0x50; header[i++] = 0x42;
            header[i++] = (byte) version;
            header[i++] = (byte) (count / 100);
            header[i++] = (byte) keyLength;
            header[i++] = (byte) ivLength;
            byte[] revBytes = String.format(Locale.ENGLISH, "%10d", revision).getBytes();
            System.arraycopy(revBytes, 0, header, i, revBytes.length);
            i += RESERVED;
            return header;
        }
    }
    
    private Activity mContext;
    private byte[] mBuffer;
    private long mDataSize;
    private FileHeader mFileHeader;
    
    public static Application getInstance(Activity context) {
        if(__instance == null) {
            __instance = new Application(context);
        }
        __instance.mContext = context;
        return __instance;
    }
    
    public static Application getInstance() {
        return __instance;
    }
    
    private Application(Activity context) {
        mContext = context;
        mSP = PreferenceManager.getDefaultSharedPreferences(context);
        Options.mTheme = mSP.getInt(C.Keys.THEME, 0);
        Options.mTour = mSP.getBoolean(C.Keys.TOUR, false);
    }
    
    public void onStart() {
        
    }
    
    public boolean hasDataFile() {
        boolean success = false;
        try {
            File file = new File(mContext.getFilesDir()+"/"+DATA_FILE);
            mDataSize = file.length();
            if(mDataSize > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;    
    }
    
    public void getData() {
        try {
            mBuffer = new byte[(int) mDataSize];
            FileInputStream fis = mContext.openFileInput(DATA_FILE);
            fis.read(mBuffer, 0, (int) mDataSize);
            mFileHeader = FileHeader.parse(mBuffer);
            fis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int getLocalVersion() {
    	int version = 0;
    	if(mFileHeader!=null) {
    		version = mFileHeader.revision;
    	}
    	return version;
    }
    
    public void saveData() {
        
    }
    
    public void onDataReceived(byte[] data) {
        
    }
}
