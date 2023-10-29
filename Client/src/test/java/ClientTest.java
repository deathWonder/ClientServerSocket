import org.example.customization.Client;
import org.example.customization.WriterClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ClientTest {

    @Test
    public void testPort() {
        //arrange
        Client client = new Client();
        int expected = 8888; //указал в файле
        //act
        int result = client.getPort();
        //assert
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testHost() {
        //arrange
        Client client = new Client();
        String expected = "127.0.0.1"; //указал в файле
        //act
        String result = client.getHost();
        //assert
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testSaveMessage(){
        Client client = new Client();
        String message = "Hello world!";
        client.saveMessage(message);
        File file = new File("fileClient.log");
        StringBuilder builder = new StringBuilder();
        if (!file.exists()) {
            throw new RuntimeException("File not found");
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Assertions.assertTrue(builder.toString().contains(message));
    }
    @Test
    public void testLogIn(){
        //arrange
        Client client = new Client();
        //act
        client.logIn();
        //assert
        Assertions.assertDoesNotThrow(client::logIn);
    }

    @Test
    public void testClient() throws IOException {
        //arrange
        Socket socket = mock(Socket.class);
        Client client = mock(Client.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream("Сабир\n/exit".getBytes());

        when(socket.getOutputStream()).thenReturn(out);
        when(socket.getInputStream()).thenReturn(in);
        doNothing().when(client).setUserName("Сабир");

        WriterClient writerClient = new WriterClient(socket, client);

        //assert
        Assertions.assertDoesNotThrow(writerClient::start);

    }
}
