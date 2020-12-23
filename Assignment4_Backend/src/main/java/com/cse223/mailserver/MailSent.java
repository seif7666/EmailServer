package com.cse223.mailserver;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MailSent {

    private ArrayList<String> Receivers;
    private MultipartFile[] multipart;

    public MailSent(ArrayList<String> receivers, MultipartFile[] multipart) {
        Receivers = receivers;
        this.multipart = multipart;
    }

    public void setReceivers(ArrayList<String> receivers) {
        Receivers = receivers;
    }

    public void setMultipart(MultipartFile[] multipart) {
        this.multipart = multipart;
    }

    public ArrayList<String> getReceivers() {
        return Receivers;
    }

    public MultipartFile[] getMultipart() {
        return multipart;
    }

    public void saveFiles() throws IOException {
        for (MultipartFile multipartFile : multipart) {
            System.out.println(multipartFile.getSize());
            File file = new File("F:\\" + multipartFile.getOriginalFilename());
            System.out.println(file.getAbsolutePath());
            multipartFile.transferTo(file);
            System.out.println("Saved at " + file.getAbsolutePath());

        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MailSent{");
        sb.append("Receivers=").append(Receivers);
        sb.append(", multipart=").append(multipart == null ? "null" : Arrays.asList(multipart).toString());
        sb.append('}');
        return sb.toString();
    }
}
