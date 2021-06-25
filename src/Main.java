import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main {


    public static void getsContentAndSaveLocally(URL url,File filePath)
    {

            InputStream inputStream=null;

            FileWriter fileWriter= null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader=null;
            String inputLine="";
            String urlString=url.toString();
            String fileName=urlString.substring(urlString.lastIndexOf("/"))+"";
            String fileType=urlString.substring(urlString.lastIndexOf(".")).toLowerCase();


            //checking if file type is specified and if not adding .html as file type

            String removedProtocolToCheckFiletype=urlString.substring(urlString.lastIndexOf(":")+2)+"";

            if(!(removedProtocolToCheckFiletype.contains("/")) ){
              fileName=fileName+".html";
            }
            else
            {
                if(!(removedProtocolToCheckFiletype.substring(removedProtocolToCheckFiletype.lastIndexOf("/")).contains(".")))
                    fileName=fileName+".html";
            }
                File path = new File(filePath,fileType.substring(1)); //Create new file to save the content
                if(!(path.exists()))
                {
                    path.mkdirs();
                }

                 File file=null;
                try {

                   file=new File(path,fileName);
                    if(!file.exists())
                    {
                        file.createNewFile();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



//if the file is image

        if(fileType.equals(".jpg") || fileType.equals(".jpeg")|| fileType.equals(".png") || fileType.equals(".gif")
          || fileType.equals(".ico"))
        {
            //connect to the url and fetch data
            ByteArrayOutputStream byteArrayOutputStream=null;
            byte[] response=new byte[1024];
            try {
                inputStream = new BufferedInputStream(url.openStream());
                byteArrayOutputStream = new ByteArrayOutputStream();

                byte[] buf = new byte[2048];
                int n = 0;
                while (-1!=(n=inputStream.read(buf)))
                {
                    byteArrayOutputStream.write(buf, 0, n);
                }
                byteArrayOutputStream.close();
                inputStream.close();
                response = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
            }
            //saving fetched data to local file
            FileOutputStream fileOutputStream=null;

            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(response);
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //if file is html css or other text files
        else
        {
            try {



                //connect to the specified url and read in byte
                URLConnection connection = url.openConnection();
                inputStream=connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                //writes the data from bufferedreader
                fileWriter=new FileWriter(file.getAbsoluteFile());
                bufferedWriter=new BufferedWriter(fileWriter);
                while ((inputLine = bufferedReader.readLine()) != null) {

                    bufferedWriter.append(inputLine);

                }
                System.out.println("written" + file +fileType);

            } catch (Exception ex) {
                System.out.println("nop");
                ex.printStackTrace();
            }

            try {
                bufferedReader.close();
                bufferedWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        URL url= null;
        try {
            url = new URL("http://www.fitbit.com/setup");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File file=new File("/home/rootuser/Desktop");
        if (!(file.exists()))
        {
            file.mkdirs();
        }
        getsContentAndSaveLocally(url,file);
    }
}
