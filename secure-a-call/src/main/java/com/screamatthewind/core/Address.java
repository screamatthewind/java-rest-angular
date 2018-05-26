/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.screamatthewind.core;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Address {
    private Locale mLocale;
	private String mPlaceId;
    private String mFeatureName;
    private HashMap<Integer, String> mAddressLines;
    private int mMaxAddressLineIndex = -1;
    private String mStreetNumber;
    private String mStreetName;
    private String mAdminArea;
    private String mSubAdminArea;
    private String mLocality;
    private String mSubLocality;
    private String mThoroughfare;
    private String mSubThoroughfare;
    private String mPremises;
    private String mPostalCode;
    private String mCountryCode;
    private String mCountryName;
    private String mFormattedAddress;
    private double mLatitude;
    private double mLongitude;
    private boolean mHasLatitude = false;
    private boolean mHasLongitude = false;
    private String mPhone;
    private String mUrl;

    public Address(Locale locale) {
        this.mLocale = locale;

        this.mPlaceId = "";
        this.mFeatureName = "";
        this.mStreetNumber = "";
        this.mStreetName = "";
        this.mAdminArea = "";
        this.mSubAdminArea = "";
        this.mLocality = "";
        this.mSubLocality = "";
        this.mThoroughfare = "";
        this.mSubThoroughfare = "";
        this.mPremises = "";
        this.mPostalCode = "";
        this.mCountryCode = "";
        this.mCountryName = "";
        this.mFormattedAddress = "";
        this.mLatitude = 0.0;
        this.mLongitude = 0.0;
        this.mHasLatitude = false;
        this.mHasLongitude = false;
        this.mPhone = "";
        this.mUrl = "";
    }

    public Locale getLocale() {
        return mLocale;
    }
    
    public String getPlaceId() {
    	return mPlaceId;
    }
    
    public void setPlaceId(String placeId) {
    	mPlaceId = placeId;
    }
    
    public int getMaxAddressLineIndex() {
        return mMaxAddressLineIndex;
    }

    public String getAddressLine(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index = " + index + " < 0");
        }
        return mAddressLines == null? null :  mAddressLines.get(index);
    }

    public void setAddressLine(int index, String line) {
        if (index < 0) {
            throw new IllegalArgumentException("index = " + index + " < 0");
        }
        if (mAddressLines == null) {
            mAddressLines = new HashMap<Integer, String>();
        }
        mAddressLines.put(index, line);
        if (line == null) {
            // We've eliminated a line, recompute the max index
            mMaxAddressLineIndex = -1;
            for (Integer i : mAddressLines.keySet()) {
                mMaxAddressLineIndex = Math.max(mMaxAddressLineIndex, i);
            }
        } else {
            mMaxAddressLineIndex = Math.max(mMaxAddressLineIndex, index);
        }
    }
    
    public String getStreetName() {
    	return mStreetName;
    }
    
    public void setStreetName(String streetName) {
    	mStreetName = streetName;
    }
    
    public String getStreetNumber() {
    	return mStreetNumber;
    }
    
    public void setStreetNumber(String streetNumber) {
    	mStreetNumber = streetNumber;
    }
    
    public String getFeatureName() {
        return mFeatureName;
    }

    public void setFeatureName(String featureName) {
        mFeatureName = featureName;
    }

    public String getAdminArea() {
        return mAdminArea;
    }

    public void setAdminArea(String adminArea) {
        this.mAdminArea = adminArea;
    }
    
    public String getSubAdminArea() {
        return mSubAdminArea;
    }

    public void setSubAdminArea(String subAdminArea) {
        this.mSubAdminArea = subAdminArea;
    }

    public String getLocality() {
        return mLocality;
    }

    public void setLocality(String locality) {
        mLocality = locality;
    }

    public String getSubLocality() {
        return mSubLocality;
    }

    public void setSubLocality(String sublocality) {
        mSubLocality = sublocality;
    }

    public String getThoroughfare() {
        return mThoroughfare;
    }

    public void setThoroughfare(String thoroughfare) {
        this.mThoroughfare = thoroughfare;
    }

    public String getSubThoroughfare() {
        return mSubThoroughfare;
    }

    public void setSubThoroughfare(String subthoroughfare) {
        this.mSubThoroughfare = subthoroughfare;
    }

    public String getPremises() {
        return mPremises;
    }

    public void setPremises(String premises) {
        mPremises = premises;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String postalCode) {
        mPostalCode = postalCode;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getCountryName() {
        return mCountryName;
    }
 
    public void setCountryName(String countryName) {
        mCountryName = countryName;
    }
    
    public String getFormattedAddress() {
    	return mFormattedAddress;
    }
    
    public void setFormattedAddress(String formattedAdddress) {
    	mFormattedAddress = formattedAdddress;
    }
    
    public boolean hasLatitude() {
        return mHasLatitude;
    }

    public double getLatitude() {
        if (mHasLatitude) {
            return mLatitude;
        } else {
        	return 0.0;
        }
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
        mHasLatitude = true;
    }

    public void clearLatitude() {
        mHasLatitude = false;
    }

    public boolean hasLongitude() {
        return mHasLongitude;
    }

    public double getLongitude() {
        if (mHasLongitude) {
            return mLongitude;
        } else {
        	return 0.0;
        }
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
        mHasLongitude = true;
    }

    public void clearLongitude() {
        mHasLongitude = false;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String Url) {
        mUrl = Url;
    }
 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Address[addressLines=[");
        for (int i = 0; i <= mMaxAddressLineIndex; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(i);
            sb.append(':');
            String line = mAddressLines.get(i);
            if (line == null) {
                sb.append("null");
            } else {
                sb.append('\"');
                sb.append(line);
                sb.append('\"');
            }
        }
        sb.append(']');
        sb.append(",feature=");
        sb.append(mFeatureName);
        sb.append(",admin=");
        sb.append(mAdminArea);
        sb.append(",sub-admin=");
        sb.append(mSubAdminArea);
        sb.append(",locality=");
        sb.append(mLocality);
        sb.append(",thoroughfare=");
        sb.append(mThoroughfare);
        sb.append(",postalCode=");
        sb.append(mPostalCode);
        sb.append(",countryCode=");
        sb.append(mCountryCode);
        sb.append(",countryName=");
        sb.append(mCountryName);
        sb.append(",hasLatitude=");
        sb.append(mHasLatitude);
        sb.append(",latitude=");
        sb.append(mLatitude);
        sb.append(",hasLongitude=");
        sb.append(mHasLongitude);
        sb.append(",longitude=");
        sb.append(mLongitude);
        sb.append(",phone=");
        sb.append(mPhone);
        sb.append(",url=");
        sb.append(mUrl);
        sb.append(",extras=");
        sb.append(']');
        return sb.toString();
    }

}