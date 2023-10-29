import org.example.customization.ClientHandler;
import org.example.customization.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerTest {

    @Test
    public void testWhatPort(){
        //arrange
        int expected = 8888;//указал в settings.txt
        Server server = new Server();//при создании читает settings.txt
        //act
        int port = server.getPort();
        //assert
        Assertions.assertEquals(expected, port);
    }
    @Test
    public void testSendMessageToAll1(){
        //arrange
        Server server = new Server();
        ClientHandler clientHandler = mock(ClientHandler.class);
        //act
        server.sendMessageToAllClients("text", clientHandler);
        //assert
        Mockito.verify(clientHandler, Mockito.times(0)).sendMessage("text");
    }

    @Test
    public void testCounter(){
        //arrange
        new ClientHandler(mock(Socket.class), new Server());
        new ClientHandler(mock(Socket.class), new Server());
        int expected = 2;
        //act
        int count = ClientHandler.getCountClients();
        //assert
        Assertions.assertEquals(expected, count);
    }

    @Test
    public void testRun1() throws IOException {
        //arrange
        Server server = mock(Server.class);
        Socket socket = mock(Socket.class);
        ClientHandler clientHandler = new ClientHandler(socket, server);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream("Сабир\n/exit".getBytes());

        when(socket.getOutputStream()).thenReturn(out);
        when(socket.getInputStream()).thenReturn(in);
        in.close();

        //act
        clientHandler.run();

        //assert
        Assertions.assertEquals(out.toString(), """
               You are welcome Сабир!
               Now you are 1!
               Bye-bye, Сабир!
               """);
    }



}
