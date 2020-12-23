package com.cse223.mailserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@SpringBootApplication
public class MailserverApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MailserverApplication.class, args);
//        File source = new File("C:\\Users\\Seif\\Desktop\\Figure_1.png");
//        File dest = new File("Fi.png");
//        System.out.println(dest.getAbsoluteFile());
//        String fileName = source.getName();
//        String[] pathAndName = fileName.split("\\\\");
//        System.out.println(pathAndName[pathAndName.length-1]);
//        copyFileUsingChannel(source , dest);
    }

//    private static void copyFileUsingChannel(File source, File dest) throws IOException {
//        FileChannel sourceChannel = null;
//        FileChannel destChannel = null;
//        sourceChannel = new FileInputStream(source).getChannel();
//        destChannel = new FileOutputStream(dest).getChannel();
//        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
//        sourceChannel.close();
//        destChannel.close();
//    }
}

