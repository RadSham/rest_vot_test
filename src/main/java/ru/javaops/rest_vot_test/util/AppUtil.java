package ru.javaops.rest_vot_test.util;

import lombok.experimental.UtilityClass;
import org.h2.tools.Server;

import java.sql.SQLException;

@UtilityClass
public class AppUtil {
    private static Server server;
    private static final String[] CONNECTION_PARAMS = new String[] {"-tcp", "-tcpAllowOthers", "-tcpPort", "9092"};
    public static Server createTcpServer() throws SQLException {
        return server = Server.createTcpServer(CONNECTION_PARAMS);
    }
    public static void stopTCPServer() {
        if (server != null) server.stop();
    }
}
