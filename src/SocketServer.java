import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class SocketServer {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	public void start(int port) throws IOException {
		System.out.println("Servidor socket iniciado... (En espera de cliente)");
		serverSocket = new ServerSocket(port);
		clientSocket = serverSocket.accept();

		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String mensaje = in.readLine();

		if (mensaje.contains("{REGISTRAR}")) {
			mensaje = mensaje.replace("{REGISTRAR}", "");
			BufferedWriter writer = new BufferedWriter(new FileWriter("datos.txt", true));
			writer.append(mensaje);
			writer.append("\n");
			writer.close();
			System.out.println("Registro grabado OK");
			out.println("Registro grabado OK");
		}

		if (mensaje.contains("{CONSULTAR}")) {
			mensaje = mensaje.replace("{CONSULTAR}", "");
			BufferedReader br = new BufferedReader(new FileReader("datos.txt"));
			List<Cuenta> listaCuenta = new ArrayList<>();

			try {
				String line = br.readLine();
				while (line != null) {
					String[] splited = line.split(",");
					Cuenta cuentatemp = new Cuenta();
					cuentatemp.numero = splited[0];
					cuentatemp.valor = splited[1];
					listaCuenta.add(cuentatemp);
					line = br.readLine();
				}
			} finally {
				br.close();
			}

			for (Cuenta cuenta : listaCuenta) {
				if (cuenta.numero.equals(mensaje)) {
					String respuesta = "La cuenta " + cuenta.numero + " tiene un valor de " + cuenta.valor;
					System.out.println(respuesta);
					out.println(respuesta);
					break;
				}
			}
		}
	}

	public void stop() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
		serverSocket.close();
	}

}