//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bossien.train.bossien;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    public HttpUtil() {
    }

    public static String postFile(File file, String RequestURL, String param) throws IOException {
        String boundary = "Boundary-bled-4060-99b9-fca7ff59c113";
        String Enter = "\r\n";
        FileInputStream fis = new FileInputStream(file);
        URL url = new URL(RequestURL + "?" + param);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary =" + boundary);
        conn.connect();
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String part1 = Enter + "--" + boundary + Enter + "Content-Type: application/octet-stream" + Enter + "Content-Disposition: form-data; filename=\"" + file.getName() + "\"; name=\"file\"" + Enter + "--" + boundary + "--" + Enter + Enter;
        dos.writeBytes(part1);
        byte[] buffer = new byte[1024];
        boolean var11 = false;

        int len;
        while((len = fis.read(buffer)) > 0) {
            dos.write(buffer, 0, len);
        }

        dos.flush();
        dos.writeBytes(Enter + "--" + boundary + "--" + Enter);
        dos.close();
        fis.close();
        BufferedReader in2 = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;
        String result;
        for(result = ""; (line = in2.readLine()) != null; result = result + line) {
            ;
        }

        in2.close();
        conn.disconnect();
        return result;
    }

    public static String postFile(InputStream inputStream, String RequestURL, String param, String fileName) throws IOException {
        String boundary = "Boundary-bled-4060-99b9-fca7ff59c113";
        String Enter = "\r\n";
        URL url = new URL(RequestURL + "?" + param);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary =" + boundary);
        conn.connect();
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String part1 = Enter + "--" + boundary + Enter + "Content-Type: application/octet-stream" + Enter + "Content-Disposition: form-data; filename=\"" + fileName + "\"; name=\"file\"" + Enter + "--" + boundary + "--" + Enter + Enter;
        dos.writeBytes(part1);
        byte[] buffer = new byte[1024];
        boolean var11 = false;

        int len;
        while((len = inputStream.read(buffer)) > 0) {
            dos.write(buffer, 0, len);
        }

        dos.flush();
        dos.writeBytes(Enter + "--" + boundary + "--" + Enter);
        dos.close();
        inputStream.close();
        BufferedReader in2 = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;
        String result;
        for(result = ""; (line = in2.readLine()) != null; result = result + line) {
            ;
        }

        in2.close();
        conn.disconnect();
        return result;
    }

    public static String getFile(String url, String param, File destFile) {
        String result = "";
        BufferedInputStream bufferedInputStream = null;

        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            FileOutputStream outputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            boolean var11 = false;

            int len;
            while((len = bufferedInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.flush();
            outputStream.close();
            String var12 = result;
            return var12;
        } catch (Exception var22) {
            System.out.println("发送 GET 请求出现异常" + var22);
            var22.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (Exception var21) {
                var21.printStackTrace();
            }

        }

        return result;
    }

    public static String getFile(String url, String param, OutputStream out) {
        String result = "";
        BufferedInputStream bufferedInputStream = null;

        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            byte[] buffer = new byte[1024];
            boolean var10 = false;

            int len;
            while((len = bufferedInputStream.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            out.flush();
            out.close();
            String var11 = result;
            return var11;
        } catch (Exception var21) {
            System.out.println("发送 GET 请求出现异常" + var21);
            var21.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (Exception var20) {
                var20.printStackTrace();
            }

        }

        return result;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;

        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();

            String line;
            for(in = new BufferedReader(new InputStreamReader(connection.getInputStream())); (line = in.readLine()) != null; result = result + line) {
                ;
            }
        } catch (Exception var17) {
            System.out.println("发送 GET 请求出现异常" + var17);
            var17.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception var16) {
                var16.printStackTrace();
            }

        }

        return result;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            File file = new File("D:" + File.separator + "output264.ts");
            DataInputStream inFile = new DataInputStream(new FileInputStream(file));
            //int bytes = false;
            byte[] bufferOut = new byte[1024];

            while(inFile.read(bufferOut) != -1) {
                out.print(bufferOut);
            }

            inFile.close();
            out.flush();

            String line;
            for(in = new BufferedReader(new InputStreamReader(conn.getInputStream())); (line = in.readLine()) != null; result = result + line) {
                ;
            }
        } catch (Exception var20) {
            System.out.println("发送 POST 请求出现异常" + var20);
            var20.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException var19) {
                var19.printStackTrace();
            }

        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:" + File.separator + "output264.ts");
        String sr = null;
        sr = postFile(file, "http://localhost:8480/storage/api/v1/file/upload", "timestamp=1494383026146&filesize=13.6&offset=0&token=a0fda380a4ebde5ad314665cb7bd3780");
        System.out.println(sr);
    }
}
