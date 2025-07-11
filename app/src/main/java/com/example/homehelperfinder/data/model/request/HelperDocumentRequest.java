package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HelperDocumentRequest {

    @SerializedName("documentType")
    public String DocumentType;

    @SerializedName("documentUrl")
    public String DocumentUrl;

//    @SerializedName("uploadDate")
//    public Date UploadDate;
//
//    @SerializedName("verificationStatus")
//    public String VerificationStatus;
//
//    @SerializedName("verifiedByAdminId")
//    public int VerifiedByAdminId;
//
//    @SerializedName("verificationDate")
//    public Date VerificationDate;
//
//    @SerializedName("notes")
//    public String Notes;

}
