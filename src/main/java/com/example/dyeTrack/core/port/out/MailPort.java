package com.example.dyeTrack.core.port.out;

public interface MailPort {
    void sendExportMail(String to, byte[] fileBytes, String filename) throws Exception;
}