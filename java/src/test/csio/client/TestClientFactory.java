package csio.client;

/**
 * Created by ike on 16-10-15.
 */
public class TestClientFactory {
    public static TestClient createClient() throws Exception {
        return new TestClient("127.0.0.1", 8899);
    }
}
