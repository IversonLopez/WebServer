import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Main{
    public static void main(String[] args) throws Exception{
        //Start receiving messages(ready to receive messages, basically like turning on your phone)
            try(ServerSocket serverSocket = new ServerSocket(8080)){
                System.out.println("Server Started. \nListening for messages."); // \n creates a new line 

                while(true){
                    
                    ///Handle new incoming message
                    
                    try(Socket client = serverSocket.accept()){

                        System.out.println(client.toString());

                        //Read the request // listen to the message

                        InputStreamReader isr = new InputStreamReader(client.getInputStream()); //inputstreamreader gives us the ability to read multiple messages and go back, but we can't get the contents from it(use bufferedreader)

                        BufferedReader br = new BufferedReader(isr); //parameter is the inputstreamreader

                        StringBuilder request = new StringBuilder(); // we create a stringbuilder to store the info from the buffered reader

                        String line; //temp holding variable, holds one line at a time ofour message

                        line = br.readLine(); // reading from our bufferedreader, our bufferedReader reads each line, now we need somewhere to store that info (stringbuilder)
                        while(!line.isBlank()){ //as long as our line is not a blank line, add it to our request
                            request.append(line + "\r\n"); //we read each individual thing from the request and then store it to our stringbuilder, use \r\n to create a space and then new line
                            line = br.readLine(); // read a new line before the loop starts again
                        }

                        System.out.println("--REQUEST--"); //debugging the request, we print to the console for debugging purposes
                        System.out.println(request); // this is to test, you can print out from StringBuilder, it implicitly prints it out as if you had a .toString() 

                        // Decide how we'd like to respond

                            //Get the first line of the request
                            String firstLine = request.toString().split("\n")[0];

                            //Get the second resource from the first line(seperated by spaces)
                            String resource = firstLine.split(" ")[1];

                            //Compare the "resource" to our list of things
                            
                            //if somebody types localhost:3000/bow, will return the image
                            if(resource.equals("/bow")){ //we need to use .equals() because remember Strings in java are treated as OBJECTS(references, etc)
                                
                                // Send back an image

                                try (FileInputStream image = new FileInputStream("bow9.png")) {  //FileInput requires try block
                                    OutputStream clientOutput = client.getOutputStream(); //we do client.getOutputStream(), because this is what the client is going to get back as a response, so we will store that as clientoutput
                                    clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes()); //remember our response starts with HTTP/1.1, don't forget \r\n to have lines, and convert to bytes
                                    clientOutput.write(("\r\n").getBytes());  //blank line
                                    clientOutput.write(image.readAllBytes());  //actual content(images), remember everything has to be converted to bytes
                                    clientOutput.flush(); //officially send the message
                                }
        

                            } else if (resource.equals("/hello")){

                                //Send back a text

                                OutputStream clientOutput = client.getOutputStream(); //we do client.getOutputStream(), because this is what the client is going to get back as a response, so we will store that as clientoutput
                                clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes()); //remember our response starts with HTTP/1.1, don't forget \r\n to have lines, and convert to bytes
                                clientOutput.write(("\r\n").getBytes());  //blank line
                                clientOutput.write(("Hello World").getBytes());  //actual content(images), remember everything has to be converted to bytes
                                clientOutput.flush(); //officially send the message


                                //easier way/if we want to only send back a simple text message
                                /*String response = "HTTP/1.1 200 OK\r\n" +
                                                    "\r\n" +        // Blank line separates headers from the body
                                                    "Hello World";
                                clientOutput.write(response.getBytes()); 
                                */

                            } else{

                                OutputStream clientOutput = client.getOutputStream(); //we do client.getOutputStream(), because this is what the client is going to get back as a response, so we will store that as clientoutput
                                clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes()); //remember our response starts with HTTP/1.1, don't forget \r\n to have lines, and convert to bytes
                                clientOutput.write(("\r\n").getBytes());  //blank line
                                clientOutput.write(("What are you looking for?").getBytes());  //actual content(images), remember everything has to be converted to bytes
                                clientOutput.flush(); //officially send the message


                            }
                    

                        client.close(); //always close the client 
                    }

                    
                }


                
            }
           
    }
}