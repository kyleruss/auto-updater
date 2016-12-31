//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

public class UpdateException extends Exception 
{
    public static enum ErrorCode
    {
        CLIENT_CONN_FAIL,
        SVERSION_CHECK_FAIL,
        CVERSION_CHECK_FAIL,
        PATCH_DL_ERR,
        UNPACK_PATCH_ERR,
        REMOVE_PATCH_ERR,
    }
    
    private ErrorCode errorCode;
    
    public UpdateException(ErrorCode errorCode)
    {
        this.errorCode  =   errorCode;
    }
    

    public ErrorCode getErrorCode()
    {
        return errorCode;
    }
}
